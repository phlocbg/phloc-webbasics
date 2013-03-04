package com.phloc.appbasics.bmx;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.codec.LZWCodec;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.EMicroNodeType;
import com.phloc.commons.microdom.IMicroCDATA;
import com.phloc.commons.microdom.IMicroComment;
import com.phloc.commons.microdom.IMicroDocumentType;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroEntityReference;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.IMicroProcessingInstruction;
import com.phloc.commons.microdom.IMicroText;
import com.phloc.commons.microdom.MicroException;
import com.phloc.commons.microdom.utils.MicroWalker;
import com.phloc.commons.state.ESuccess;

/**
 * Binary Micro XML (BMX) Writer
 * 
 * @author philip
 */
public class BMXWriter
{
  /** Version number of format v1 - must be 4 bytes, all ASCII! */
  public static final String VERSION1 = "BMX1";

  /** EOF marker to be printed at the end of the stream */
  public static final int EOF_MARKER = 0xff;

  private final BMXSettings m_aSettings;

  public BMXWriter ()
  {
    this (BMXSettings.createDefault ());
  }

  public BMXWriter (@Nonnull final BMXSettings aSettings)
  {
    if (aSettings == null)
      throw new NullPointerException ("settings");
    m_aSettings = aSettings.getClone ();
  }

  @Nonnull
  private static BMXWriterStringTable _createStringTable (@Nonnull final IMicroNode aNode)
  {
    final BMXWriterStringTable ret = new BMXWriterStringTable ();
    MicroWalker.walkNode (aNode, new DefaultHierarchyWalkerCallback <IMicroNode> ()
    {
      @Override
      public void onItemBeforeChildren (final IMicroNode aChildNode)
      {
        switch (aChildNode.getType ())
        {
          case CDATA:
            ret.addString (((IMicroCDATA) aChildNode).getData ());
            break;
          case COMMENT:
            ret.addString (((IMicroComment) aChildNode).getData ());
            break;
          case DOCUMENT_TYPE:
            final IMicroDocumentType aDocType = (IMicroDocumentType) aChildNode;
            ret.addString (aDocType.getQualifiedName ());
            ret.addString (aDocType.getPublicID ());
            ret.addString (aDocType.getSystemID ());
            break;
          case ELEMENT:
            final IMicroElement aElement = (IMicroElement) aChildNode;
            ret.addString (aElement.getNamespaceURI ());
            ret.addString (aElement.getTagName ());
            ret.addStrings (aElement.getAllAttributeNames ());
            ret.addStrings (aElement.getAllAttributeValues ());
            break;
          case ENTITY_REFERENCE:
            ret.addString (((IMicroEntityReference) aChildNode).getName ());
            break;
          case PROCESSING_INSTRUCTION:
            final IMicroProcessingInstruction aPI = (IMicroProcessingInstruction) aChildNode;
            ret.addString (aPI.getTarget ());
            ret.addString (aPI.getData ());
            break;
          case TEXT:
            ret.addString (((IMicroText) aChildNode).getData ());
            break;
          case CONTAINER:
          case DOCUMENT:
            break;
          default:
            throw new IllegalStateException ("Illegal node type:" + aChildNode);
        }
      }
    });
    return ret.finish ();
  }

  @Nonnegative
  private static int _getStorageByteCount (final int nByteCount)
  {
    if (nByteCount < 1 || nByteCount > 4)
      throw new IllegalStateException ("Internal error byte count is too huge: " + nByteCount);
    return nByteCount == 3 ? 4 : nByteCount;
  }

  private static final class RefWriter
  {
    private final BMXWriterStringTable m_aST;
    private final DataOutput m_aDO;
    private final int m_nReferenceStorageByteCount;

    public RefWriter (@Nonnull final BMXWriterStringTable aST, @Nonnull final DataOutput aDO)
    {
      m_aST = aST;
      m_aDO = aDO;
      m_nReferenceStorageByteCount = _getStorageByteCount (aST.getReferenceStorageByteCount ());
    }

    public void writeStringRef (@Nullable final CharSequence aString) throws IOException
    {
      writeStringRef (aString == null ? null : aString.toString ());
    }

    public void writeStringRef (@Nullable final String sString) throws IOException
    {
      final int nIndex = m_aST.getIndex (sString);
      switch (m_nReferenceStorageByteCount)
      {
        case 1:
          m_aDO.writeByte (nIndex);
          break;
        case 2:
          m_aDO.writeShort (nIndex);
          break;
        case 4:
          m_aDO.writeInt (nIndex);
          break;
      }
    }
  }

  @Nonnull
  private static byte [] _getStringTableBytes (@Nonnull final BMXWriterStringTable aST) throws IOException
  {
    final int nLengthStorageByteCount = _getStorageByteCount (aST.getLengthStorageByteCount ());

    final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ();
    final DataOutputStream aDOS = new DataOutputStream (aBAOS);
    aDOS.writeInt (aST.getStringCount ());
    aDOS.writeByte (nLengthStorageByteCount);
    for (final byte [] aStringData : aST.getAllByteArrays ())
    {
      switch (nLengthStorageByteCount)
      {
        case 1:
          aDOS.writeByte (aStringData.length);
          break;
        case 2:
          aDOS.writeShort (aStringData.length);
          break;
        case 4:
          aDOS.writeInt (aStringData.length);
          break;
      }
      aDOS.write (aStringData);
    }
    aDOS.close ();

    return aBAOS.toByteArray ();
  }

  @Nonnull
  private static byte [] _getContentBytes (@Nonnull final BMXWriterStringTable aST, @Nonnull final IMicroNode aNode)
  {
    final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ();
    final DataOutputStream aDOS = new DataOutputStream (aBAOS);
    final RefWriter aRW = new RefWriter (aST, aDOS);

    // Write main content
    MicroWalker.walkNode (aNode, new DefaultHierarchyWalkerCallback <IMicroNode> ()
    {
      @Override
      public void onItemBeforeChildren (final IMicroNode aChildNode)
      {
        try
        {
          final EMicroNodeType eNodeType = aChildNode.getType ();
          aDOS.writeByte (eNodeType.getID ());
          switch (eNodeType)
          {
            case CDATA:
              aRW.writeStringRef (((IMicroCDATA) aChildNode).getData ());
              break;
            case COMMENT:
              aRW.writeStringRef (((IMicroComment) aChildNode).getData ());
              break;
            case CONTAINER:
              break;
            case DOCUMENT:
              break;
            case DOCUMENT_TYPE:
              final IMicroDocumentType aDocType = (IMicroDocumentType) aChildNode;
              aRW.writeStringRef (aDocType.getQualifiedName ());
              aRW.writeStringRef (aDocType.getPublicID ());
              aRW.writeStringRef (aDocType.getSystemID ());
              break;
            case ELEMENT:
              final IMicroElement aElement = (IMicroElement) aChildNode;
              aRW.writeStringRef (aElement.getNamespaceURI ());
              aRW.writeStringRef (aElement.getTagName ());
              final Map <String, String> aAttrs = aElement.getAllAttributes ();
              aDOS.writeInt (ContainerHelper.getSize (aAttrs));
              if (aAttrs != null)
                for (final Map.Entry <String, String> aEntry : aAttrs.entrySet ())
                {
                  aRW.writeStringRef (aEntry.getKey ());
                  aRW.writeStringRef (aEntry.getValue ());
                }
              break;
            case ENTITY_REFERENCE:
              aRW.writeStringRef (((IMicroEntityReference) aChildNode).getName ());
              break;
            case PROCESSING_INSTRUCTION:
              final IMicroProcessingInstruction aPI = (IMicroProcessingInstruction) aChildNode;
              aRW.writeStringRef (aPI.getTarget ());
              aRW.writeStringRef (aPI.getData ());
              break;
            case TEXT:
              final IMicroText aText = (IMicroText) aChildNode;
              aRW.writeStringRef (aText.getData ());
              aDOS.writeBoolean (aText.isElementContentWhitespace ());
              break;
            default:
              throw new IllegalStateException ("Illegal node type:" + aChildNode);
          }

          if (aChildNode.hasChildren ())
            aDOS.writeByte ('{');
        }
        catch (final IOException ex)
        {
          throw new MicroException ("Failed to write BMX content to output stream", ex);
        }
      }

      @Override
      public void onItemAfterChildren (final IMicroNode aChildNode)
      {
        try
        {
          if (aChildNode.hasChildren ())
            aDOS.writeByte ('}');
        }
        catch (final IOException ex)
        {
          throw new MicroException ("Failed to write BMX content to output stream", ex);
        }
      }
    });

    return aBAOS.toByteArray ();
  }

  @Nonnull
  public ESuccess writeToDataOutput (@Nonnull final IMicroNode aNode, @Nonnull final DataOutput aDO)
  {
    if (aNode == null)
      throw new NullPointerException ("node");
    if (aDO == null)
      throw new NullPointerException ("dataOutput");

    // Create the string table to use
    final BMXWriterStringTable aST = _createStringTable (aNode);

    try
    {
      // Main format version
      aDO.write (VERSION1.getBytes (CCharset.CHARSET_ISO_8859_1_OBJ));

      // Write settings
      aDO.writeInt (m_aSettings.getStorageValue ());

      // Write the string table content LZW encoded
      byte [] aSTBytes = _getStringTableBytes (aST);
      if (m_aSettings.isSet (EBMXSetting.LZW_ENCODING))
      {
        final byte [] aEncodedST = LZWCodec.encodeLZW (aSTBytes);
        System.out.println ("ST saved " + (aSTBytes.length - aEncodedST.length) + " of " + aSTBytes.length + " bytes");
        aSTBytes = aEncodedST;
      }
      aDO.writeInt (aSTBytes.length);
      aDO.write (aSTBytes);

      // Write the main content LZW encoded
      byte [] aContentBytes = _getContentBytes (aST, aNode);
      if (m_aSettings.isSet (EBMXSetting.LZW_ENCODING))
      {
        final byte [] aEncodedContent = LZWCodec.encodeLZW (aContentBytes);
        System.out.println ("Content saved " +
                            (aContentBytes.length - aEncodedContent.length) +
                            " of " +
                            aContentBytes.length +
                            " bytes");
        aContentBytes = aEncodedContent;
      }
      aDO.writeInt (aContentBytes.length);
      aDO.write (aContentBytes);

      // Write EOF marker
      aDO.writeByte (EOF_MARKER);

      return ESuccess.SUCCESS;
    }
    catch (final IOException ex)
    {
      throw new MicroException ("Failed to write BMX content to output stream", ex);
    }
  }

  @Nonnull
  public ESuccess writeToOutputStream (@Nonnull final IMicroNode aNode, @Nonnull @WillClose final OutputStream aOS)
  {
    if (aOS == null)
      throw new NullPointerException ("OS");

    try
    {
      return writeToDataOutput (aNode, new DataOutputStream (aOS));
    }
    finally
    {
      StreamUtils.close (aOS);
    }
  }

  @Nullable
  public byte [] getAsBytes (@Nonnull final IMicroNode aNode)
  {
    final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ();
    if (writeToOutputStream (aNode, aBAOS).isFailure ())
      return null;
    return aBAOS.toByteArray ();
  }
}
