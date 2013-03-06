package com.phloc.appbasics.bmx;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.charset.StringDecoder;
import com.phloc.commons.collections.NonBlockingStack;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.impl.MicroCDATA;
import com.phloc.commons.microdom.impl.MicroComment;
import com.phloc.commons.microdom.impl.MicroContainer;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.microdom.impl.MicroDocumentType;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.microdom.impl.MicroEntityReference;
import com.phloc.commons.microdom.impl.MicroProcessingInstruction;
import com.phloc.commons.microdom.impl.MicroText;

public final class BMXReader
{
  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final BMXReader s_aInstance = new BMXReader ();

  private BMXReader ()
  {}

  @Nullable
  public static IMicroNode readFromFile (@Nonnull final File aFile)
  {
    if (aFile == null)
      throw new NullPointerException ("file");

    final InputStream aFIS = FileUtils.getInputStream (aFile);
    if (aFIS == null)
      return null;

    return readFromStream (aFIS);
  }

  @Nullable
  public static IMicroNode readFromStream (@Nonnull @WillClose final InputStream aIS)
  {
    if (aIS == null)
      throw new NullPointerException ("inputStream");

    // Ensure stream is buffered!
    final InputStream aISToUse = StreamUtils.getBuffered (aIS);
    try
    {
      final DataInput aDIS = new DataInputStream (aISToUse);
      return readFromDataInput (aDIS);
    }
    finally
    {
      StreamUtils.close (aISToUse);
    }
  }

  @Nullable
  public static IMicroNode readFromDataInput (@Nonnull @WillClose final DataInput aDIS)
  {
    if (aDIS == null)
      throw new NullPointerException ("dataInput");

    try
    {
      // Read version
      final byte [] aVersion = new byte [4];
      aDIS.readFully (aVersion);
      if (!Arrays.equals (CBMXIO.VERSION1, aVersion))
        throw new BMXReadException ("This is not a BMX file!");

      // Read settings
      final int nSettings = aDIS.readInt ();
      final BMXSettings aSettings = BMXSettings.createFromStorageValue (nSettings);

      final DataInput aContentDIS = aDIS;

      // Start iterating the main content
      IMicroNode aResultNode = null;
      final NonBlockingStack <IMicroNode> aNodeStack = new NonBlockingStack <IMicroNode> ();
      IMicroNode aLastNode = null;
      final BMXReaderStringTable aST = new BMXReaderStringTable (!aSettings.isSet (EBMXSetting.NO_STRINGTABLE));
      byte [] aBuf = new byte [1024];
      final StringDecoder aDecoder = new StringDecoder (CBMXIO.ENCODING);

      int nNodeType;
      while ((nNodeType = aContentDIS.readByte () & 0xff) != CBMXIO.NODETYPE_EOF)
      {
        IMicroNode aCreatedNode = null;
        switch (nNodeType)
        {
          case CBMXIO.NODETYPE_CDATA:
            aCreatedNode = new MicroCDATA (aST.getString (aContentDIS.readInt ()));
            break;
          case CBMXIO.NODETYPE_COMMENT:
            aCreatedNode = new MicroComment (aST.getString (aContentDIS.readInt ()));
            break;
          case CBMXIO.NODETYPE_CONTAINER:
            aCreatedNode = new MicroContainer ();
            break;
          case CBMXIO.NODETYPE_DOCUMENT:
            aCreatedNode = new MicroDocument ();
            break;
          case CBMXIO.NODETYPE_DOCUMENT_TYPE:
          {
            final String sQualifiedName = aST.getString (aContentDIS.readInt ());
            final String sPublicID = aST.getString (aContentDIS.readInt ());
            final String sSystemID = aST.getString (aContentDIS.readInt ());
            aCreatedNode = new MicroDocumentType (sQualifiedName, sPublicID, sSystemID);
            break;
          }
          case CBMXIO.NODETYPE_ELEMENT:
          {
            final String sNamespaceURI = aST.getString (aContentDIS.readInt ());
            final String sTagName = aST.getString (aContentDIS.readInt ());
            final IMicroElement aElement = new MicroElement (sNamespaceURI, sTagName);
            final int nAttrCount = aContentDIS.readInt ();
            for (int i = 0; i < nAttrCount; ++i)
            {
              final String sAttrName = aST.getString (aContentDIS.readInt ());
              final String sAttrValue = aST.getString (aContentDIS.readInt ());
              aElement.setAttribute (sAttrName, sAttrValue);
            }
            aCreatedNode = aElement;
            break;
          }
          case CBMXIO.NODETYPE_ENTITY_REFERENCE:
            aCreatedNode = new MicroEntityReference (aST.getString (aContentDIS.readInt ()));
            break;
          case CBMXIO.NODETYPE_PROCESSING_INSTRUCTION:
          {
            final String sTarget = aST.getString (aContentDIS.readInt ());
            final String sData = aST.getString (aContentDIS.readInt ());
            aCreatedNode = new MicroProcessingInstruction (sTarget, sData);
            break;
          }
          case CBMXIO.NODETYPE_TEXT:
          {
            final String sText = aST.getString (aContentDIS.readInt ());
            final boolean bIgnorableWhitespace = aContentDIS.readBoolean ();
            aCreatedNode = new MicroText (sText, bIgnorableWhitespace);
            break;
          }
          case CBMXIO.NODETYPE_STRING:
          {
            final int nLength = aContentDIS.readInt ();
            if (nLength > aBuf.length)
              aBuf = new byte [Math.max (nLength, aBuf.length * 2)];
            aContentDIS.readFully (aBuf, 0, nLength);
            aST.add (aDecoder.finish (aBuf, 0, nLength));
            break;
          }
          case CBMXIO.SPECIAL_CHILDREN_START:
            aNodeStack.push (aLastNode);
            break;
          case CBMXIO.SPECIAL_CHILDREN_END:
            aLastNode = aNodeStack.pop ();
            break;
          default:
            throw new BMXReadException ("Unsupported node type " + nNodeType);
        }

        if (aCreatedNode != null)
        {
          if (aResultNode == null)
            aResultNode = aCreatedNode;
          else
            aNodeStack.peek ().appendChild (aCreatedNode);
          aLastNode = aCreatedNode;
        }
      }

      return aResultNode;
    }
    catch (final IOException ex)
    {
      throw new BMXReadException ("Failed to read from InputStream", ex);
    }
  }

  // @Nullable
  // public static IMicroNode readFromChannel (@Nonnull @WillClose final
  // ReadableByteChannel aChannel)
  // {
  // if (aChannel == null)
  // throw new NullPointerException ("channel");
  //
  // try
  // {
  // // Read version
  // final byte [] aVersion = new byte [4];
  // aChannel.readFully (aVersion);
  // final String sVersion = new String (aVersion,
  // CCharset.CHARSET_ISO_8859_1_OBJ);
  // if (!sVersion.equals (CBMXIO.VERSION1))
  // throw new BMXReadException ("This is not a BMX file!");
  //
  // // Read settings
  // final int nSettings = aDIS.readInt ();
  // final BMXSettings aSettings = BMXSettings.createFromStorageValue
  // (nSettings);
  //
  // DataInputStream aContentDIS = aDIS;
  // Inflater aInflater = null;
  // InflaterInputStream aInflaterIS = null;
  // if (false)
  // {
  // aInflater = new Inflater ();
  // aInflaterIS = new InflaterInputStream (aDIS, aInflater);
  // aContentDIS = new DataInputStream (aInflaterIS);
  // }
  //
  // // Start iterating the main content
  // IMicroNode aResultNode = null;
  // final NonBlockingStack <IMicroNode> aNodeStack = new NonBlockingStack
  // <IMicroNode> ();
  // IMicroNode aLastNode = null;
  // final BMXReaderStringTable aST = new BMXReaderStringTable (!aSettings.isSet
  // (EBMXSetting.NO_STRINGTABLE));
  //
  // int nNodeType;
  // while ((nNodeType = aContentDIS.readByte () & 0xff) != CBMXIO.NODETYPE_EOF)
  // {
  // IMicroNode aCreatedNode = null;
  // switch (nNodeType)
  // {
  // case CBMXIO.NODETYPE_CDATA:
  // aCreatedNode = new MicroCDATA (aST.get (aContentDIS.readInt ()));
  // break;
  // case CBMXIO.NODETYPE_COMMENT:
  // aCreatedNode = new MicroComment (aST.get (aContentDIS.readInt ()));
  // break;
  // case CBMXIO.NODETYPE_CONTAINER:
  // aCreatedNode = new MicroContainer ();
  // break;
  // case CBMXIO.NODETYPE_DOCUMENT:
  // aCreatedNode = new MicroDocument ();
  // break;
  // case CBMXIO.NODETYPE_DOCUMENT_TYPE:
  // {
  // final String sQualifiedName = aST.get (aContentDIS.readInt ());
  // final String sPublicID = aST.get (aContentDIS.readInt ());
  // final String sSystemID = aST.get (aContentDIS.readInt ());
  // aCreatedNode = new MicroDocumentType (sQualifiedName, sPublicID,
  // sSystemID);
  // break;
  // }
  // case CBMXIO.NODETYPE_ELEMENT:
  // {
  // final String sNamespaceURI = aST.get (aContentDIS.readInt ());
  // final String sTagName = aST.get (aContentDIS.readInt ());
  // final IMicroElement aElement = new MicroElement (sNamespaceURI, sTagName);
  // final int nAttrCount = aContentDIS.readInt ();
  // for (int i = 0; i < nAttrCount; ++i)
  // {
  // final String sAttrName = aST.get (aContentDIS.readInt ());
  // final String sAttrValue = aST.get (aContentDIS.readInt ());
  // aElement.setAttribute (sAttrName, sAttrValue);
  // }
  // aCreatedNode = aElement;
  // break;
  // }
  // case CBMXIO.NODETYPE_ENTITY_REFERENCE:
  // aCreatedNode = new MicroEntityReference (aST.get (aContentDIS.readInt ()));
  // break;
  // case CBMXIO.NODETYPE_PROCESSING_INSTRUCTION:
  // {
  // final String sTarget = aST.get (aContentDIS.readInt ());
  // final String sData = aST.get (aContentDIS.readInt ());
  // aCreatedNode = new MicroProcessingInstruction (sTarget, sData);
  // break;
  // }
  // case CBMXIO.NODETYPE_TEXT:
  // {
  // final String sText = aST.get (aContentDIS.readInt ());
  // final boolean bIgnorableWhitespace = aContentDIS.readBoolean ();
  // aCreatedNode = new MicroText (sText, bIgnorableWhitespace);
  // break;
  // }
  // case CBMXIO.NODETYPE_STRING:
  // {
  // final int nLength = aContentDIS.readInt ();
  // final byte [] aString = new byte [nLength];
  // aContentDIS.readFully (aString);
  // aST.add (new String (aString, CBMXIO.ENCODING));
  // break;
  // }
  // case CBMXIO.SPECIAL_CHILDREN_START:
  // aNodeStack.push (aLastNode);
  // break;
  // case CBMXIO.SPECIAL_CHILDREN_END:
  // aLastNode = aNodeStack.pop ();
  // break;
  // default:
  // throw new BMXReadException ("Unsupported node type " + nNodeType);
  // }
  //
  // if (aCreatedNode != null)
  // {
  // if (aResultNode == null)
  // aResultNode = aCreatedNode;
  // else
  // aNodeStack.peek ().appendChild (aCreatedNode);
  // aLastNode = aCreatedNode;
  // }
  // }
  //
  // if (aInflater != null)
  // aInflater.end ();
  //
  // return aResultNode;
  // }
  // catch (final IOException ex)
  // {
  // throw new BMXReadException ("Failed to read from InputStream", ex);
  // }
  // finally
  // {
  // StreamUtils.close (aISToUse);
  // }
  // }
}
