package com.phloc.appbasics.bmx;

import java.io.OutputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;

import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.IMicroCDATA;
import com.phloc.commons.microdom.IMicroComment;
import com.phloc.commons.microdom.IMicroDocumentType;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroEntityReference;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.IMicroProcessingInstruction;
import com.phloc.commons.microdom.IMicroText;
import com.phloc.commons.microdom.utils.MicroWalker;
import com.phloc.commons.state.ESuccess;

/**
 * Binary Micro XML (BMX) Writer
 * 
 * @author philip
 */
public class BMXWriter
{
  public BMXWriter ()
  {}

  @Nonnull
  private static BMXStringTable _createStringTable (@Nonnull final IMicroNode aNode)
  {
    final BMXStringTable ret = new BMXStringTable ();
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
    return ret;
  }

  @Nonnull
  public ESuccess writeToStream (@Nonnull final IMicroNode aNode, @Nonnull @WillClose final OutputStream aOS)
  {
    try
    {
      if (aNode == null)
        throw new NullPointerException ("node");
      if (aOS == null)
        throw new NullPointerException ("OS");

      final BMXStringTable aST = _createStringTable (aNode);
      System.out.println (aST.getLengthStorageByteCount ());
      return ESuccess.SUCCESS;
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
    if (writeToStream (aNode, aBAOS).isFailure ())
      return null;
    return aBAOS.toByteArray ();
  }
}
