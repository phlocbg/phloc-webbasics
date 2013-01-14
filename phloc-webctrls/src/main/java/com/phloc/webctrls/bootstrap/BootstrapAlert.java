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
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.html.entities.EHTMLEntity;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;
import com.phloc.html.hc.html.AbstractHCDiv;
import com.phloc.html.hc.html.HCButton;
import com.phloc.html.hc.impl.HCEntityNode;

/**
 * Bootstrap alert box
 * 
 * @author philip
 */
public class BootstrapAlert extends AbstractHCDiv <BootstrapAlert>
{
  public static final boolean DEFAULT_SHOW_CLOSE = false;
  public static final boolean DEFAULT_BLOCK = false;

  private EBootstrapAlertType m_eType = EBootstrapAlertType.DEFAULT;
  private boolean m_bShowClose = DEFAULT_SHOW_CLOSE;
  private boolean m_bBlock = DEFAULT_BLOCK;

  private void _init ()
  {
    addClass (CBootstrapCSS.ALERT);
  }

  public BootstrapAlert ()
  {
    super ();
    _init ();
  }

  @Nonnull
  public EBootstrapAlertType getType ()
  {
    return m_eType;
  }

  @Nonnull
  public BootstrapAlert setType (@Nonnull final EBootstrapAlertType eType)
  {
    if (eType == null)
      throw new NullPointerException ("type");
    m_eType = eType;
    return this;
  }

  public boolean isBlock ()
  {
    return m_bBlock;
  }

  @Nonnull
  public BootstrapAlert setBlock (final boolean bBlock)
  {
    m_bBlock = bBlock;
    return this;
  }

  public boolean isShowClose ()
  {
    return m_bShowClose;
  }

  @Nonnull
  public BootstrapAlert setShowClose (final boolean bShowClose)
  {
    m_bShowClose = bShowClose;
    return this;
  }

  @Override
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void internalBeforeConvertToNode (@Nonnull final IHCConversionSettingsToNode aConversionSettings)
  {
    super.internalBeforeConvertToNode (aConversionSettings);
    addClass (m_eType);
    if (m_bShowClose)
      addChild (0,
                new HCButton ().addClass (CBootstrapCSS.CLOSE)
                               .setCustomAttr ("data-dismiss", "alert")
                               .addChild (new HCEntityNode (EHTMLEntity.times, "x")));
    if (m_bBlock)
      addClass (CBootstrapCSS.ALERT_BLOCK);
  }
}
