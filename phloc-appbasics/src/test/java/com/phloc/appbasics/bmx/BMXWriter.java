/**
 * Copyright (C) 2006-2013 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.appbasics.bmx;

import java.io.BufferedOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.io.file.FileUtils;
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
  /** The storage encoding of all strings in this table */
  public static final Charset ENCODING = CCharset.CHARSET_UTF_8_OBJ;

  /** Version number of format v1 - must be 4 bytes, all ASCII! */
  public static final String VERSION1 = "BMX1";

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

  private static void _writeContent (@Nonnull final AbstractBMXWriterStringTable aST,
                                     @Nonnull final IMicroNode aNode,
                                     @Nonnull final DataOutput aDOS)
  {
    // Write main content
    MicroWalker.walkNode (aNode, new DefaultHierarchyWalkerCallback <IMicroNode> ()
    {
      @Override
      public void onItemBeforeChildren (final IMicroNode aChildNode)
      {
        try
        {
          final EMicroNodeType eNodeType = aChildNode.getType ();
          switch (eNodeType)
          {
            case CDATA:
              aDOS.writeByte (CBMXIO.NODETYPE_CDATA);
              final IMicroCDATA aCDATA = (IMicroCDATA) aChildNode;
              aDOS.writeInt (aST.addString (aCDATA.getData ()));
              break;
            case COMMENT:
              aDOS.writeByte (CBMXIO.NODETYPE_COMMENT);
              final IMicroComment aComment = (IMicroComment) aChildNode;
              aDOS.writeInt (aST.addString (aComment.getData ()));
              break;
            case CONTAINER:
              aDOS.writeByte (CBMXIO.NODETYPE_CONTAINER);
              break;
            case DOCUMENT:
              aDOS.writeByte (CBMXIO.NODETYPE_DOCUMENT);
              break;
            case DOCUMENT_TYPE:
              aDOS.writeByte (CBMXIO.NODETYPE_DOCUMENT_TYPE);
              final IMicroDocumentType aDocType = (IMicroDocumentType) aChildNode;
              aDOS.writeInt (aST.addString (aDocType.getQualifiedName ()));
              aDOS.writeInt (aST.addString (aDocType.getPublicID ()));
              aDOS.writeInt (aST.addString (aDocType.getSystemID ()));
              break;
            case ELEMENT:
              aDOS.writeByte (CBMXIO.NODETYPE_ELEMENT);
              final IMicroElement aElement = (IMicroElement) aChildNode;
              aDOS.writeInt (aST.addString (aElement.getNamespaceURI ()));
              aDOS.writeInt (aST.addString (aElement.getTagName ()));
              if (aElement.hasAttributes ())
              {
                final Map <String, String> aAttrs = aElement.getAllAttributes ();
                aDOS.writeInt (aAttrs.size ());
                for (final Map.Entry <String, String> aEntry : aAttrs.entrySet ())
                {
                  aDOS.writeInt (aST.addString (aEntry.getKey ()));
                  aDOS.writeInt (aST.addString (aEntry.getValue ()));
                }
              }
              else
              {
                aDOS.writeInt (0);
              }
              break;
            case ENTITY_REFERENCE:
              aDOS.writeByte (CBMXIO.NODETYPE_ENTITY_REFERENCE);
              final IMicroEntityReference aER = (IMicroEntityReference) aChildNode;
              aDOS.writeInt (aST.addString (aER.getName ()));
              break;
            case PROCESSING_INSTRUCTION:
              aDOS.writeByte (CBMXIO.NODETYPE_PROCESSING_INSTRUCTION);
              final IMicroProcessingInstruction aPI = (IMicroProcessingInstruction) aChildNode;
              aDOS.writeInt (aST.addString (aPI.getTarget ()));
              aDOS.writeInt (aST.addString (aPI.getData ()));
              break;
            case TEXT:
              aDOS.writeByte (CBMXIO.NODETYPE_TEXT);
              final IMicroText aText = (IMicroText) aChildNode;
              aDOS.writeInt (aST.addString (aText.getData ()));
              aDOS.writeBoolean (aText.isElementContentWhitespace ());
              break;
            default:
              throw new IllegalStateException ("Illegal node type:" + aChildNode);
          }

          if (aChildNode.hasChildren ())
            aDOS.writeByte (CBMXIO.SPECIAL_CHILDREN_START);
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
            aDOS.writeByte (CBMXIO.SPECIAL_CHILDREN_END);
        }
        catch (final IOException ex)
        {
          throw new MicroException ("Failed to write BMX content to output stream", ex);
        }
      }
    });
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

      // Write settings
      aDO.writeInt (m_aSettings.getStorageValue ());

      // The string table to be filled
      final AbstractBMXWriterStringTable aST = new AbstractBMXWriterStringTable ()
      {
        @Override
        protected void onNewString (@Nonnull final String sString, @Nonnegative final int nIndex) throws IOException
        {
          aDO.writeByte (CBMXIO.NODETYPE_STRING);
          final byte [] aBytes = sString.getBytes (ENCODING);
          aDO.writeInt (aBytes.length);
          aDO.write (aBytes, 0, aBytes.length);
        }
      };

      // Write the main content and filling the string table
      _writeContent (aST, aNode, aDO);

      // Write EOF marker
      aDO.writeByte (CBMXIO.NODETYPE_EOF);

      return ESuccess.SUCCESS;
    }
    catch (final IOException ex)
    {
      throw new MicroException ("Failed to write BMX content to output stream", ex);
    }
  }

  @Nonnull
  public ESuccess writeToStream (@Nonnull final IMicroNode aNode, @Nonnull @WillClose final OutputStream aOS)
  {
    if (aOS == null)
      throw new NullPointerException ("OS");

    // Wrap the passed output stream in a buffered output stream
    OutputStream aOSToUse;
    if (aOS instanceof BufferedOutputStream)
      aOSToUse = aOS;
    else
      aOSToUse = new BufferedOutputStream (aOS);

    try
    {
      return writeToDataOutput (aNode, new DataOutputStream (aOSToUse));
    }
    finally
    {
      StreamUtils.close (aOSToUse);
    }
  }

  @Nonnull
  public ESuccess writeToFile (@Nonnull final IMicroNode aNode, @Nonnull final File aFile)
  {
    if (aFile == null)
      throw new NullPointerException ("file");

    final FileOutputStream aFOS = FileUtils.getOutputStream (aFile);
    if (aFOS == null)
      return ESuccess.FAILURE;

    return writeToStream (aNode, aFOS);
  }

  @Nullable
  public byte [] getAsBytes (@Nonnull final IMicroNode aNode)
  {
    final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ();
    if (writeToStream (aNode, aBAOS).isFailure ())
      return null;
    return aBAOS.toByteArray ();
  }
}
