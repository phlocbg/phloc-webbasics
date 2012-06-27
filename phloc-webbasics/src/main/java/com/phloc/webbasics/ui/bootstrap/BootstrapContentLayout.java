/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webbasics.ui.bootstrap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.conversion.IHCConversionSettings;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCHR;
import com.phloc.html.hc.html5.HCFooter;
import com.phloc.html.hc.impl.AbstractWrappedHCNode;

public class BootstrapContentLayout extends AbstractWrappedHCNode
{
  private HCDiv m_aContainer;
  private IHCNode m_aContent;
  private IHCNode m_aFooter;

  public BootstrapContentLayout ()
  {}

  @Nonnull
  public BootstrapContentLayout setContent (@Nullable final IHCNode aContent)
  {
    m_aContent = aContent;
    return this;
  }

  @Nonnull
  public BootstrapContentLayout setFooter (@Nullable final IHCNode aFooter)
  {
    m_aFooter = aFooter;
    return this;
  }

  @Override
  protected void prepareBeforeGetAsNode (@Nonnull final IHCConversionSettings aConversionSettings)
  {
    m_aContainer = new HCDiv ().addClass (CBootstrapCSS.CONTAINER_FLUID);
    if (m_aContent != null)
      m_aContainer.addChild (m_aContent);
    if (m_aFooter != null)
      m_aContainer.addChildren (new HCHR (), new HCFooter (m_aFooter));
  }

  @Override
  protected IHCNode getContainedHCNode ()
  {
    return m_aContainer;
  }
}
