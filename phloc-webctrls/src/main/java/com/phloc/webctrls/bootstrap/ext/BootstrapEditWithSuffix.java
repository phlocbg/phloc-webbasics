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
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webbasics.form.RequestField;
import com.phloc.webctrls.bootstrap.CBootstrapCSS;

public class BootstrapEditWithSuffix implements IHCNodeBuilder
{
  private final IHCNode m_aInput;
  private final IHCNode m_aSuffix;

  public BootstrapEditWithSuffix (@Nonnull final RequestField aRF, @Nonnull @Nonempty final String sSuffix)
  {
    this (new HCEdit (aRF), sSuffix);
  }

  public BootstrapEditWithSuffix (@Nonnull final IHCNodeBuilder aInput, @Nonnull @Nonempty final String sSuffix)
  {
    this (aInput.build (), sSuffix);
  }

  public BootstrapEditWithSuffix (@Nonnull final IHCNode aInput, @Nonnull @Nonempty final String sSuffix)
  {
    if (aInput == null)
      throw new NullPointerException ("input");
    if (StringHelper.hasNoText (sSuffix))
      throw new IllegalArgumentException ("suffix");
    m_aInput = aInput;
    m_aSuffix = new HCTextNode (sSuffix);
  }

  public BootstrapEditWithSuffix (@Nonnull final RequestField aRF, @Nonnull final IHCNode aSuffix)
  {
    this (new HCEdit (aRF), aSuffix);
  }

  public BootstrapEditWithSuffix (@Nonnull final IHCNodeBuilder aInput, @Nonnull final IHCNode aSuffix)
  {
    this (aInput.build (), aSuffix);
  }

  public BootstrapEditWithSuffix (@Nonnull final IHCNode aInput, @Nonnull final IHCNode aSuffix)
  {
    if (aInput == null)
      throw new NullPointerException ("input");
    if (aSuffix == null)
      throw new NullPointerException ("suffix");
    m_aInput = aInput;
    m_aSuffix = aSuffix;
  }

  @Nonnull
  public IHCNode getInput ()
  {
    return m_aInput;
  }

  @Nonnull
  @Nonempty
  public IHCNode getSuffix ()
  {
    return m_aSuffix;
  }

  @Nullable
  public IHCNode build ()
  {
    final HCDiv aDiv = new HCDiv ().addClass (CBootstrapCSS.INPUT_APPEND);
    aDiv.addChild (m_aInput);
    IHCElement <?> aElement;
    if (m_aSuffix instanceof IHCElement <?>)
      aElement = (IHCElement <?>) m_aSuffix;
    else
      aElement = HCSpan.create (m_aSuffix);
    aDiv.addChild (aElement.addClass (CBootstrapCSS.ADD_ON));
    return aDiv;
  }
}
