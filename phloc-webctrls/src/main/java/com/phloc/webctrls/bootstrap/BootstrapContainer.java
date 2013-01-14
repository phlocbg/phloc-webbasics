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
package com.phloc.webctrls.bootstrap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCHR;
import com.phloc.html.hc.html5.HCFooter;

public class BootstrapContainer implements IHCNodeBuilder
{
  public static final boolean DEFAULT_FLUID = true;

  private final boolean m_bFluid;
  private IHCNode m_aContent;
  private IHCNode m_aFooter;

  public BootstrapContainer ()
  {
    this (DEFAULT_FLUID);
  }

  public BootstrapContainer (final boolean bFluid)
  {
    m_bFluid = bFluid;
  }

  public boolean isFluid ()
  {
    return m_bFluid;
  }

  @Nullable
  public IHCNode getContent ()
  {
    return m_aContent;
  }

  @Nonnull
  public BootstrapContainer setContent (@Nullable final IHCNode aContent)
  {
    m_aContent = aContent;
    return this;
  }

  @Nullable
  public IHCNode getFooter ()
  {
    return m_aFooter;
  }

  @Nonnull
  public BootstrapContainer setFooter (@Nullable final IHCNode aFooter)
  {
    m_aFooter = aFooter;
    return this;
  }

  @Nonnull
  public HCDiv build ()
  {
    final HCDiv aContainer = new HCDiv ().addClass (m_bFluid ? CBootstrapCSS.CONTAINER_FLUID : CBootstrapCSS.CONTAINER);
    if (m_aContent != null)
      aContainer.addChild (m_aContent);
    if (m_aFooter != null)
      aContainer.addChildren (new HCHR (), new HCFooter ().addChild (m_aFooter));
    return aContainer;
  }
}
