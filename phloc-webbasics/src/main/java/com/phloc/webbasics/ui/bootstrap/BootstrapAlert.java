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
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.conversion.IHCConversionSettings;
import com.phloc.html.hc.html.AbstractHCDiv;
import com.phloc.html.hc.html.HCA;

/**
 * Bootstrap alert box
 * 
 * @author philip
 */
public class BootstrapAlert extends AbstractHCDiv <BootstrapAlert>
{
  public static final boolean DEFAULT_SHOW_CLOSE = false;

  private EBootstrapAlertType m_eType = EBootstrapAlertType.DEFAULT;
  private boolean m_bShowClose = DEFAULT_SHOW_CLOSE;

  private void _init ()
  {
    addClass (CBootstrapCSS.ALERT);
  }

  public BootstrapAlert ()
  {
    super ();
    _init ();
  }

  public BootstrapAlert (@Nonnull final IPredefinedLocaleTextProvider aChild)
  {
    this (aChild.getText ());
  }

  public BootstrapAlert (@Nullable final String sChild)
  {
    super (sChild);
    _init ();
  }

  public BootstrapAlert (@Nullable final String... aChildren)
  {
    super (aChildren);
    _init ();
  }

  public BootstrapAlert (@Nullable final IHCNode aChild)
  {
    super (aChild);
    _init ();
  }

  public BootstrapAlert (@Nullable final IHCNode... aChildren)
  {
    super (aChildren);
    _init ();
  }

  public BootstrapAlert (@Nullable final Iterable <? extends IHCNode> aChildren)
  {
    super (aChildren);
    _init ();
  }

  @Nonnull
  public BootstrapAlert setType (@Nullable final EBootstrapAlertType eType)
  {
    m_eType = eType;
    return this;
  }

  @Nonnull
  public BootstrapAlert setBlock (final boolean bBlock)
  {
    if (bBlock)
      addClass (CBootstrapCSS.ALERT_BLOCK);
    else
      removeClass (CBootstrapCSS.ALERT_BLOCK);
    return this;
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
  protected void prepareOnceBeforeCreateElement (@Nonnull final IHCConversionSettings aConversionSettings)
  {
    super.prepareOnceBeforeCreateElement (aConversionSettings);
    addClass (m_eType);
    if (m_bShowClose)
      addChild (0, new HCA ().addClass (CBootstrapCSS.CLOSE).addChild ("*"));
  }
}
