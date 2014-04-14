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
package com.phloc.bootstrap3.button;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webctrls.custom.IIcon;

/**
 * Bootstrap button based on an &lt;a&gt;
 * 
 * @author Philip Helger
 */
public class BootstrapLinkButton extends HCA
{
  private EBootstrapButtonType m_eButtonType = EBootstrapButtonType.DEFAULT;
  private EBootstrapButtonSize m_eButtonSize = EBootstrapButtonSize.DEFAULT;
  private IIcon m_aIcon;

  public BootstrapLinkButton ()
  {
    this (EBootstrapButtonType.DEFAULT, EBootstrapButtonSize.DEFAULT);
  }

  public BootstrapLinkButton (@Nonnull final EBootstrapButtonType eButtonType)
  {
    this (eButtonType, EBootstrapButtonSize.DEFAULT);
  }

  public BootstrapLinkButton (@Nonnull final EBootstrapButtonSize eButtonSize)
  {
    this (EBootstrapButtonType.DEFAULT, eButtonSize);
  }

  public BootstrapLinkButton (@Nonnull final EBootstrapButtonType eButtonType,
                              @Nonnull final EBootstrapButtonSize eButtonSize)
  {
    addClass (CBootstrapCSS.BTN);
    setButtonType (eButtonType);
    setButtonSize (eButtonSize);
  }

  @Nonnull
  public EBootstrapButtonType getButtonType ()
  {
    return m_eButtonType;
  }

  @Nonnull
  public BootstrapLinkButton setButtonType (@Nonnull final EBootstrapButtonType eButtonType)
  {
    m_eButtonType = ValueEnforcer.notNull (eButtonType, "ButtonType");
    return this;
  }

  @Nullable
  public EBootstrapButtonSize getButtonSize ()
  {
    return m_eButtonSize;
  }

  @Nonnull
  public BootstrapLinkButton setButtonSize (@Nonnull final EBootstrapButtonSize eButtonSize)
  {
    m_eButtonSize = ValueEnforcer.notNull (eButtonSize, "ButtonSize");
    return this;
  }

  @Nullable
  public IIcon getIcon ()
  {
    return m_aIcon;
  }

  @Nonnull
  public BootstrapLinkButton setIcon (@Nullable final EDefaultIcon eIcon)
  {
    return setIcon (eIcon == null ? null : eIcon.getIcon ());
  }

  @Nonnull
  public BootstrapLinkButton setIcon (@Nullable final IIcon aIcon)
  {
    m_aIcon = aIcon;
    return this;
  }

  @Override
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void internalBeforeConvertToNode (@Nonnull final IHCConversionSettingsToNode aConversionSettings)
  {
    super.internalBeforeConvertToNode (aConversionSettings);

    // apply type and size
    addClasses (getButtonType (), getButtonSize ());

    // apply icon
    if (getIcon () != null)
    {
      final boolean bAddSeparator = hasChildren ();
      addChild (0, getIcon ().getAsNode ());
      if (bAddSeparator)
      {
        // Add spacer
        addChild (1, new HCTextNode (" "));
      }
    }
  }
}
