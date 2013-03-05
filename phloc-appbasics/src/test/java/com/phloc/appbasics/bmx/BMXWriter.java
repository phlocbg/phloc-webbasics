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
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;

import org.apache.commons.collections.primitives.ArrayIntList;

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

  private static void _writeNodeBeforeChildren (@Nonnull final BMXWriterStringTable aST,
                                                @Nonnull final IMicroNode aChildNode,
                                                @Nonnull final DataOutput aDOS)
  {
    try
    {
      final EMicroNodeType eNodeType = aChildNode.getType ();
      switch (eNodeType)
      {
        case CDATA:
        {
          final IMicroCDATA aCDATA = (IMicroCDATA) aChildNode;
          final int nStringIdx = aST.addString (aCDATA.getData ());
          aDOS.writeByte (CBMXIO.NODETYPE_CDATA);
          aDOS.writeInt (nStringIdx);
          break;
        }
        case COMMENT:
        {
          final IMicroComment aComment = (IMicroComment) aChildNode;
          final int nStringIdx = aST.addString (aComment.getData ());
          aDOS.writeByte (CBMXIO.NODETYPE_COMMENT);
          aDOS.writeInt (nStringIdx);
          break;
        }
        case CONTAINER:
          aDOS.writeByte (CBMXIO.NODETYPE_CONTAINER);
          break;
        case DOCUMENT:
          aDOS.writeByte (CBMXIO.NODETYPE_DOCUMENT);
          break;
        case DOCUMENT_TYPE:
        {
          final IMicroDocumentType aDocType = (IMicroDocumentType) aChildNode;
          final int nStringIdx1 = aST.addString (aDocType.getQualifiedName ());
          final int nStringIdx2 = aST.addString (aDocType.getPublicID ());
          final int nStringIdx3 = aST.addString (aDocType.getSystemID ());
          aDOS.writeByte (CBMXIO.NODETYPE_DOCUMENT_TYPE);
          aDOS.writeInt (nStringIdx1);
          aDOS.writeInt (nStringIdx2);
          aDOS.writeInt (nStringIdx3);
          break;
        }
        case ELEMENT:
          final IMicroElement aElement = (IMicroElement) aChildNode;
          final ArrayIntList aIntList = new ArrayIntList ();
          aIntList.add (aST.addString (aElement.getNamespaceURI ()));
          aIntList.add (aST.addString (aElement.getTagName ()));
          if (aElement.hasAttributes ())
          {
            final Map <String, String> aAttrs = aElement.getAllAttributes ();
            aIntList.add (aAttrs.size ());
            for (final Map.Entry <String, String> aEntry : aAttrs.entrySet ())
            {
              aIntList.add (aST.addString (aEntry.getKey ()));
              aIntList.add (aST.addString (aEntry.getValue ()));
            }
          }
          else
          {
            aIntList.add (0);
          }
          aDOS.writeByte (CBMXIO.NODETYPE_ELEMENT);
          for (final int nInt : aIntList.toArray ())
            aDOS.writeInt (nInt);
          break;
        case ENTITY_REFERENCE:
        {
          final IMicroEntityReference aER = (IMicroEntityReference) aChildNode;
          final int nStringIdx = aST.addString (aER.getName ());
          aDOS.writeByte (CBMXIO.NODETYPE_ENTITY_REFERENCE);
          aDOS.writeInt (nStringIdx);
          break;
        }
        case PROCESSING_INSTRUCTION:
        {
          final IMicroProcessingInstruction aPI = (IMicroProcessingInstruction) aChildNode;
          final int nStringIdx1 = aST.addString (aPI.getTarget ());
          final int nStringIdx2 = aST.addString (aPI.getData ());
          aDOS.writeByte (CBMXIO.NODETYPE_PROCESSING_INSTRUCTION);
          aDOS.writeInt (nStringIdx1);
          aDOS.writeInt (nStringIdx2);
          break;
        }
        case TEXT:
        {
          final IMicroText aText = (IMicroText) aChildNode;
          final int nStringIdx = aST.addString (aText.getData ());
          aDOS.writeByte (CBMXIO.NODETYPE_TEXT);
          aDOS.writeInt (nStringIdx);
          aDOS.writeBoolean (aText.isElementContentWhitespace ());
          break;
        }
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

  private static void _writeNodeAfterChildren (@Nonnull final IMicroNode aChildNode, @Nonnull final DataOutput aDOS)
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

  private static void _writeContent (@Nonnull final BMXWriterStringTable aST,
                                     @Nonnull final IMicroNode aNode,
                                     @Nonnull final DataOutput aDOS)
  {
    // Write main content
    _writeNodeBeforeChildren (aST, aNode, aDOS);
    MicroWalker.walkNode (aNode, new DefaultHierarchyWalkerCallback <IMicroNode> ()
    {
      @Override
      public void onItemBeforeChildren (final IMicroNode aChildNode)
      {
        _writeNodeBeforeChildren (aST, aChildNode, aDOS);
      }

      @Override
      public void onItemAfterChildren (@Nonnull final IMicroNode aChildNode)
      {
        _writeNodeAfterChildren (aChildNode, aDOS);
      }
    });
    _writeNodeAfterChildren (aNode, aDOS);
  }

  @Nonnull
  public ESuccess writeToDataOutput (@Nonnull final IMicroNode aNode, @Nonnull final DataOutputStream aDOS)
  {
    if (aNode == null)
      throw new NullPointerException ("node");
    if (aDOS == null)
      throw new NullPointerException ("dataOutput");

    try
    {
      // Main format version
      aDOS.write (CBMXIO.VERSION1.getBytes (CCharset.CHARSET_ISO_8859_1_OBJ));

      // Write settings
      aDOS.writeInt (m_aSettings.getStorageValue ());

      DataOutputStream aContentDOS = aDOS;
      Deflater aDeflater = null;
      DeflaterOutputStream aDeflaterOS = null;
      if (false)
      {
        aDeflater = new Deflater ();
        aDeflaterOS = new DeflaterOutputStream (aDOS, aDeflater);
        aContentDOS = new DataOutputStream (aDeflaterOS);
      }

      // The string table to be filled
      final BMXWriterStringTable aST = new BMXWriterStringTable (aContentDOS);

      // Write the main content and filling the string table
      _writeContent (aST, aNode, aContentDOS);

      // Write EOF marker
      aContentDOS.writeByte (CBMXIO.NODETYPE_EOF);

      // Finish deflate
      if (aDeflaterOS != null)
      {
        aDeflaterOS.finish ();
        aDeflater.end ();
      }
      aContentDOS.flush ();

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
