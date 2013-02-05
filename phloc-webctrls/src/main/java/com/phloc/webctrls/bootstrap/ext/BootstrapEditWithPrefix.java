package com.phloc.webctrls.bootstrap.ext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.impl.AbstractHCInput;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webbasics.form.RequestField;
import com.phloc.webctrls.bootstrap.CBootstrapCSS;

public class BootstrapEditWithPrefix implements IHCNodeBuilder
{
  private final IHCNode m_aPrefix;
  private final AbstractHCInput <?> m_aInput;

  public BootstrapEditWithPrefix (@Nonnull @Nonempty final String sPrefix, @Nonnull final RequestField aRF)
  {
    this (sPrefix, new HCEdit (aRF));
  }

  public BootstrapEditWithPrefix (@Nonnull @Nonempty final String sPrefix, @Nonnull final AbstractHCInput <?> aInput)
  {
    if (StringHelper.hasNoText (sPrefix))
      throw new IllegalArgumentException ("prefix");
    if (aInput == null)
      throw new NullPointerException ("input");
    m_aPrefix = new HCTextNode (sPrefix);
    m_aInput = aInput;
  }

  public BootstrapEditWithPrefix (@Nonnull final IHCNode aPrefix, @Nonnull final RequestField aRF)
  {
    this (aPrefix, new HCEdit (aRF));
  }

  public BootstrapEditWithPrefix (@Nonnull final IHCNode aPrefix, @Nonnull final AbstractHCInput <?> aInput)
  {
    if (aPrefix == null)
      throw new NullPointerException ("prefix");
    if (aInput == null)
      throw new NullPointerException ("input");
    m_aPrefix = aPrefix;
    m_aInput = aInput;
  }

  @Nonnull
  public IHCNode getPrefix ()
  {
    return m_aPrefix;
  }

  @Nonnull
  public AbstractHCInput <?> getInput ()
  {
    return m_aInput;
  }

  @Nullable
  public IHCNode build ()
  {
    final HCDiv aDiv = new HCDiv ().addClass (CBootstrapCSS.INPUT_PREPEND);
    IHCElement <?> aElement;
    if (m_aPrefix instanceof IHCElement <?>)
      aElement = (IHCElement <?>) m_aPrefix;
    else
      aElement = HCSpan.create (m_aPrefix);
    aDiv.addChild (aElement.addClass (CBootstrapCSS.ADD_ON));
    aDiv.addChild (m_aInput);
    return aDiv;
  }
}
