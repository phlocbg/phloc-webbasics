package com.phloc.appbasics.bmx;

import java.io.OutputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;

import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
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
  private BMXStringTable _buildStringTable (@Nonnull final IMicroNode aNode)
  {
    final BMXStringTable ret = new BMXStringTable ();
    MicroWalker.walkNode (aNode, new DefaultHierarchyWalkerCallback <IMicroNode> ()
    {
      @Override
      public void onItemBeforeChildren (final IMicroNode aChildNode)
      {
        if (aChildNode.isElement ())
        {
          final IMicroElement aElement = (IMicroElement) aChildNode;
          ret.addWord (aElement.getNamespaceURI ());
          ret.addWord (aElement.getTagName ());
          ret.addWords (aElement.getAllAttributeNames ());
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

      final BMXStringTable aST = _buildStringTable (aNode);
      System.out.println (aST);
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
