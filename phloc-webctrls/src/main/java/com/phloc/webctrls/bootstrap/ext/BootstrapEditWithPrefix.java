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

public class BootstrapEditWithPrefix implements IHCNodeBuilder
{
  private final String m_sPrefix;
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
    m_sPrefix = sPrefix;
    m_aInput = aInput;
  }

  @Nullable
  public IHCNode build ()
  {
    final HCDiv aDiv = new HCDiv ().addClass (CBootstrapCSS.INPUT_PREPEND);
    aDiv.addChild (HCSpan.create (m_sPrefix).addClass (CBootstrapCSS.ADD_ON));
    aDiv.addChild (m_aInput);
    return aDiv;
  }
}
