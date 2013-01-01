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
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;
import com.phloc.html.hc.html.HCButton;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webctrls.custom.DefaultIcons;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webctrls.custom.IIcon;

public class BootstrapButton_Button extends HCButton
{
  private EBootstrapButtonType m_eType = EBootstrapButtonType.DEFAULT;
  private EBootstrapButtonSize m_eSize;
  private IIcon m_aIcon;

  public BootstrapButton_Button ()
  {
    super ();
    addClass (CBootstrapCSS.BTN);
  }

  @Nonnull
  public BootstrapButton_Button setButtonType (@Nonnull final EBootstrapButtonType eType)
  {
    if (eType == null)
      throw new NullPointerException ("type");
    m_eType = eType;
    return this;
  }

  @Nonnull
  public BootstrapButton_Button setSize (@Nullable final EBootstrapButtonSize eSize)
  {
    m_eSize = eSize;
    return this;
  }

  @Nonnull
  public BootstrapButton_Button setIcon (@Nullable final IIcon aIcon)
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
    addClasses (m_eType, m_eSize);
    if (m_aIcon != null)
    {
      final boolean bAddSeparator = hasChildren ();
      addChild (0, m_aIcon.getAsNode ());
      if (bAddSeparator)
      {
        // Add spacer
        addChild (1, new HCTextNode (" "));
      }
    }
  }

  @Nonnull
  public static BootstrapButton_Button create (@Nullable final String sLabel)
  {
    final BootstrapButton_Button ret = new BootstrapButton_Button ();
    ret.addChild (sLabel);
    return ret;
  }

  @Nonnull
  public static BootstrapButton_Button create (@Nullable final IIcon aIcon)
  {
    return new BootstrapButton_Button ().setIcon (aIcon);
  }

  @Nonnull
  public static BootstrapButton_Button create (@Nullable final String sLabel, @Nullable final IIcon aIcon)
  {
    return create (sLabel).setIcon (aIcon);
  }

  @Nonnull
  public static BootstrapButton_Button create (@Nonnull final EDefaultIcon eIcon)
  {
    return create (DefaultIcons.get (eIcon));
  }

  @Nonnull
  public static BootstrapButton_Button create (@Nullable final String sLabel, @Nonnull final EDefaultIcon eIcon)
  {
    return create (sLabel, DefaultIcons.get (eIcon));
  }
}
