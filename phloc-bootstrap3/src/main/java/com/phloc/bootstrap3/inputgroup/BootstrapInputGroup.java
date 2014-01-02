/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.bootstrap3.inputgroup;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.bootstrap3.button.BootstrapButton;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.impl.HCTextNode;

public class BootstrapInputGroup implements IHCNodeBuilder
{
  private final EBootstrapInputGroupSize m_eSize;
  private IHCNode m_aPrefix;
  private final IHCNode m_aInput;
  private IHCNode m_aSuffix;

  public BootstrapInputGroup (@Nonnull final IHCNodeBuilder aNodeBuilder)
  {
    this (EBootstrapInputGroupSize.DEFAULT, aNodeBuilder);
  }

  public BootstrapInputGroup (@Nonnull final IHCNode aInput)
  {
    this (EBootstrapInputGroupSize.DEFAULT, aInput);
  }

  public BootstrapInputGroup (@Nonnull final EBootstrapInputGroupSize eSize, @Nonnull final IHCNodeBuilder aNodeBuilder)
  {
    this (eSize, aNodeBuilder.build ());
  }

  public BootstrapInputGroup (@Nonnull final EBootstrapInputGroupSize eSize, @Nonnull final IHCNode aInput)
  {
    if (eSize == null)
      throw new NullPointerException ("size");
    if (aInput == null)
      throw new NullPointerException ("input");
    m_eSize = eSize;
    m_aInput = aInput;
  }

  @Nonnull
  public EBootstrapInputGroupSize getSize ()
  {
    return m_eSize;
  }

  @Nonnull
  public BootstrapInputGroup setPrefix (@Nullable final String sPrefix)
  {
    return setPrefix (HCTextNode.create (sPrefix));
  }

  @Nonnull
  public BootstrapInputGroup setPrefix (@Nullable final IHCNode aPrefix)
  {
    m_aPrefix = aPrefix;
    return this;
  }

  @Nullable
  public IHCNode getPrefix ()
  {
    return m_aPrefix;
  }

  @Nonnull
  public IHCNode getInput ()
  {
    return m_aInput;
  }

  @Nonnull
  public BootstrapInputGroup setSuffix (@Nullable final String sSuffix)
  {
    return setSuffix (HCTextNode.create (sSuffix));
  }

  @Nonnull
  public BootstrapInputGroup setSuffix (@Nullable final IHCNode aSuffix)
  {
    m_aSuffix = aSuffix;
    return this;
  }

  @Nullable
  public IHCNode getSuffix ()
  {
    return m_aSuffix;
  }

  @Nullable
  public IHCNode build ()
  {
    if (m_aPrefix == null && m_aSuffix == null)
      return m_aInput;

    final HCDiv aDiv = new HCDiv ().addClasses (CBootstrapCSS.INPUT_GROUP, m_eSize);
    if (m_aPrefix != null)
    {
      if (m_aPrefix instanceof BootstrapButton)
        aDiv.addChild (new HCSpan ().addClass (CBootstrapCSS.INPUT_GROUP_BTN).addChild (m_aPrefix));
      else
        aDiv.addChild (new HCSpan ().addClass (CBootstrapCSS.INPUT_GROUP_ADDON).addChild (m_aPrefix));
    }
    aDiv.addChild (m_aInput);
    if (m_aSuffix != null)
    {
      if (m_aSuffix instanceof BootstrapButton)
        aDiv.addChild (new HCSpan ().addClass (CBootstrapCSS.INPUT_GROUP_BTN).addChild (m_aSuffix));
      else
        aDiv.addChild (new HCSpan ().addClass (CBootstrapCSS.INPUT_GROUP_ADDON).addChild (m_aSuffix));
    }
    return aDiv;
  }
}
