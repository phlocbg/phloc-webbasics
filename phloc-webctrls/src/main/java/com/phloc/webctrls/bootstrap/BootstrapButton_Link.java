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
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;
import com.phloc.html.hc.html.HCA;

/**
 * A bootstrap button that has a target URL if you click it.
 * 
 * @author Philip Helger
 */
public class BootstrapButton_Link extends HCA
{
  private EBootstrapButtonType m_eType = EBootstrapButtonType.LINK;
  private EBootstrapButtonSize m_eSize;
  private boolean m_bDisabled = false;

  private void _init ()
  {
    addClass (CBootstrapCSS.BTN);
  }

  public BootstrapButton_Link ()
  {
    super ();
    _init ();
  }

  public BootstrapButton_Link (@Nonnull final ISimpleURL aHref)
  {
    super (aHref);
    _init ();
  }

  @Nonnull
  public EBootstrapButtonType getButtonType ()
  {
    return m_eType;
  }

  @Nonnull
  public BootstrapButton_Link setButtonType (@Nonnull final EBootstrapButtonType eType)
  {
    if (eType == null)
      throw new NullPointerException ("type");
    m_eType = eType;
    return this;
  }

  @Nullable
  public EBootstrapButtonSize getSize ()
  {
    return m_eSize;
  }

  @Nonnull
  public BootstrapButton_Link setSize (@Nullable final EBootstrapButtonSize eSize)
  {
    m_eSize = eSize;
    return this;
  }

  @Nonnull
  public boolean isDisabled ()
  {
    return m_bDisabled;
  }

  @Nonnull
  public BootstrapButton_Link setDisabled (final boolean bDisabled)
  {
    m_bDisabled = bDisabled;
    return this;
  }

  @Override
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void internalBeforeConvertToNode (@Nonnull final IHCConversionSettingsToNode aConversionSettings)
  {
    super.internalBeforeConvertToNode (aConversionSettings);
    addClasses (getButtonType (), getSize ());
    if (isDisabled ())
      addClass (CBootstrapCSS.DISABLED);
  }

  @Nonnull
  public static BootstrapButton_Link create (@Nullable final String sLabel)
  {
    final BootstrapButton_Link ret = new BootstrapButton_Link ();
    ret.addChild (sLabel);
    return ret;
  }
}
