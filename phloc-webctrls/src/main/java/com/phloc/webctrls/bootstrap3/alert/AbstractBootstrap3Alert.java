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
package com.phloc.webctrls.bootstrap3.alert;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;
import com.phloc.html.hc.html.AbstractHCDiv;
import com.phloc.webctrls.bootstrap3.CBootstrap3CSS;
import com.phloc.webctrls.bootstrap3.base.Bootstrap3CloseIcon;

/**
 * Bootstrap alert box
 * 
 * @author Philip Helger
 */
public abstract class AbstractBootstrap3Alert <THISTYPE extends AbstractBootstrap3Alert <THISTYPE>> extends AbstractHCDiv <THISTYPE>
{
  /** By default the close box is not shown */
  public static final boolean DEFAULT_SHOW_CLOSE = false;

  private EBootstrap3AlertType m_eType;
  private boolean m_bShowClose = DEFAULT_SHOW_CLOSE;

  public AbstractBootstrap3Alert (@Nonnull final EBootstrap3AlertType eType)
  {
    super ();
    setType (eType);
  }

  @Nonnull
  public EBootstrap3AlertType getType ()
  {
    return m_eType;
  }

  @Nonnull
  public THISTYPE setType (@Nonnull final EBootstrap3AlertType eType)
  {
    if (eType == null)
      throw new NullPointerException ("type");
    m_eType = eType;
    return thisAsT ();
  }

  public boolean isShowClose ()
  {
    return m_bShowClose;
  }

  @Nonnull
  public THISTYPE setShowClose (final boolean bShowClose)
  {
    m_bShowClose = bShowClose;
    return thisAsT ();
  }

  @Override
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void internalBeforeConvertToNode (@Nonnull final IHCConversionSettingsToNode aConversionSettings)
  {
    super.internalBeforeConvertToNode (aConversionSettings);
    addClasses (CBootstrap3CSS.ALERT, m_eType);
    if (m_bShowClose)
    {
      addClass (CBootstrap3CSS.ALERT_DISMISSABLE);
      addChild (0, new Bootstrap3CloseIcon ().setCustomAttr ("data-dismiss", "alert"));
    }
  }
}
