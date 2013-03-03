package com.phloc.appbasics.bmx;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;

import com.phloc.commons.charset.CCharset;
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

  public BMXWriter ()
  {}

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
            ret.addString (((IMicroCDATA) aChildNode).getData ().toString ());
            break;
          case COMMENT:
            ret.addString (((IMicroComment) aChildNode).getData ().toString ());
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
            final IMicroText aText = (IMicroText) aChildNode;
            if (!aText.isElementContentWhitespace ())
              ret.addString (aText.getData ().toString ());
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
      throw new IllegalStateException ("Internal error: " + nByteCount);
    return nByteCount == 3 ? 4 : nByteCount;
  }

  @Nonnull
  public ESuccess writeToDataOutput (@Nonnull final IMicroNode aNode, @Nonnull final DataOutput aDO)
  {
    if (aNode == null)
      throw new NullPointerException ("node");
    if (aDO == null)
      throw new NullPointerException ("dataOutput");

    try
    {
      // Main format version
      aDO.write (VERSION1.getBytes (CCharset.CHARSET_ISO_8859_1_OBJ));

      // Write string table
      final BMXWriterStringTable aST = _createStringTable (aNode);
      final int nLengthStorageByteCount = _getStorageByteCount (aST.getLengthStorageByteCount ());

      aDO.writeInt (aST.getStringCount ());
      aDO.writeByte (nLengthStorageByteCount);
      for (final byte [] aStringData : aST.getAllByteArrays ())
      {
        switch (nLengthStorageByteCount)
        {
          case 1:
            aDO.writeByte (aStringData.length);
            break;
          case 2:
            aDO.writeShort (aStringData.length);
            break;
          case 4:
            aDO.writeInt (aStringData.length);
            break;
        }
        aDO.write (aStringData);
      }

      final int nReferenceStorageByteCount = _getStorageByteCount (aST.getReferenceStorageByteCount ());

      // Write main content
      MicroWalker.walkNode (aNode, new DefaultHierarchyWalkerCallback <IMicroNode> ()
      {
        @Override
        public void onItemBeforeChildren (final IMicroNode aChildNode)
        {
          try
          {
            final EMicroNodeType eNodeType = aChildNode.getType ();
            aDO.writeByte (eNodeType.getID ());
            switch (eNodeType)
            {
              case CDATA:
                break;
              case COMMENT:
                break;
              case CONTAINER:
                break;
              case DOCUMENT:
                break;
              case DOCUMENT_TYPE:
                break;
              case ELEMENT:
                break;
              case ENTITY_REFERENCE:
                break;
              case PROCESSING_INSTRUCTION:
                break;
              case TEXT:
                break;
              default:
                throw new IllegalStateException ("Illegal node type:" + aChildNode);
            }
          }
          catch (final IOException ex)
          {
            throw new MicroException ("Failed to write BMX content to output stream", ex);
          }
        }
      });

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
