package com.phloc.webctrls.bootstrap.ext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.impl.AbstractHCInput;
import com.phloc.webbasics.form.RequestField;
import com.phloc.webctrls.bootstrap.CBootstrapCSS;

public class BootstrapEditWithSuffix implements IHCNodeBuilder
{
  private final AbstractHCInput <?> m_aInput;
  private final String m_sSuffix;

  public BootstrapEditWithSuffix (@Nonnull final RequestField aRF, @Nonnull @Nonempty final String sSuffix)
  {
    this (new HCEdit (aRF), sSuffix);
  }

  public BootstrapEditWithSuffix (@Nonnull final AbstractHCInput <?> aInput, @Nonnull @Nonempty final String sSuffix)
  {
    if (aInput == null)
      throw new NullPointerException ("input");
    if (StringHelper.hasNoText (sSuffix))
      throw new IllegalArgumentException ("suffix");
    m_aInput = aInput;
    m_sSuffix = sSuffix;
  }

  @Nonnull
  public AbstractHCInput <?> getInput ()
  {
    return m_aInput;
  }

  @Nonnull
  @Nonempty
  public String getSuffix ()
  {
    return m_sSuffix;
  }

  @Nullable
  public IHCNode build ()
  {
    final HCDiv aDiv = new HCDiv ().addClass (CBootstrapCSS.INPUT_APPEND);
    aDiv.addChild (m_aInput);
    aDiv.addChild (HCSpan.create (m_sSuffix).addClass (CBootstrapCSS.ADD_ON));
    return aDiv;
  }
}
