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
package com.phloc.webctrls.bootstrap3.button;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;
import com.phloc.html.hc.html.AbstractHCButton;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webctrls.bootstrap3.CBootstrap3CSS;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webctrls.custom.IIcon;

public class Bootstrap3Button extends AbstractHCButton <Bootstrap3Button>
{
  private EBootstrap3ButtonType m_eButtonType = EBootstrap3ButtonType.DEFAULT;
  private EBootstrap3ButtonSize m_eButtonSize = EBootstrap3ButtonSize.DEFAULT;
  private IIcon m_aIcon;

  public Bootstrap3Button ()
  {
    this (EBootstrap3ButtonType.DEFAULT, EBootstrap3ButtonSize.DEFAULT);
  }

  public Bootstrap3Button (@Nonnull final EBootstrap3ButtonType eButtonType)
  {
    this (eButtonType, EBootstrap3ButtonSize.DEFAULT);
  }

  public Bootstrap3Button (@Nonnull final EBootstrap3ButtonSize eButtonSize)
  {
    this (EBootstrap3ButtonType.DEFAULT, eButtonSize);
  }

  public Bootstrap3Button (@Nonnull final EBootstrap3ButtonType eButtonType,
                           @Nonnull final EBootstrap3ButtonSize eButtonSize)
  {
    addClass (CBootstrap3CSS.BTN);
    setButtonType (eButtonType);
    setButtonSize (eButtonSize);
  }

  @Nonnull
  public EBootstrap3ButtonType getButtonType ()
  {
    return m_eButtonType;
  }

  @Nonnull
  public Bootstrap3Button setButtonType (@Nonnull final EBootstrap3ButtonType eButtonType)
  {
    if (eButtonType == null)
      throw new NullPointerException ("ButtonType");
    m_eButtonType = eButtonType;
    return this;
  }

  @Nullable
  public EBootstrap3ButtonSize getButtonSize ()
  {
    return m_eButtonSize;
  }

  @Nonnull
  public Bootstrap3Button setButtonSize (@Nonnull final EBootstrap3ButtonSize eButtonSize)
  {
    if (eButtonSize == null)
      throw new NullPointerException ("ButtonSize");
    m_eButtonSize = eButtonSize;
    return this;
  }

  @Nullable
  public IIcon getIcon ()
  {
    return m_aIcon;
  }

  @Nonnull
  public Bootstrap3Button setIcon (@Nullable final EDefaultIcon eIcon)
  {
    return setIcon (eIcon == null ? null : eIcon.getIcon ());
  }

  @Nonnull
  public Bootstrap3Button setIcon (@Nullable final IIcon aIcon)
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
