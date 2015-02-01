package com.phloc.webctrls.slider.html;

import javax.annotation.Nullable;

import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCDiv;

public abstract class AbstractSliderHTMLElement implements IHCNodeBuilder
{
  protected final String ATTRIBUTE_USAGE = "u"; //$NON-NLS-1$
  private final String m_sUsage;

  protected AbstractSliderHTMLElement ()
  {
    this (null);
  }

  protected AbstractSliderHTMLElement (@Nullable final String sUsage)
  {
    this.m_sUsage = sUsage;
  }

  @Override
  public final HCDiv build ()
  {
    final HCDiv aNode = new HCDiv ();
    if (StringHelper.hasText (this.m_sUsage))
    {
      aNode.setCustomAttr (this.ATTRIBUTE_USAGE, this.m_sUsage);
    }
    onBuildNode (aNode);
    return aNode;
  }

  protected abstract void onBuildNode (final HCDiv aNode);

}
