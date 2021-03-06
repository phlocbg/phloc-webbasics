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
package com.phloc.bootstrap2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;
import com.phloc.html.hc.html.HCButton_Submit;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webctrls.custom.DefaultIcons;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webctrls.custom.IIcon;

public class BootstrapButton_Submit extends HCButton_Submit
{
  private EBootstrapButtonType m_eType = EBootstrapButtonType.PRIMARY;
  private EBootstrapButtonSize m_eSize;
  private IIcon m_aIcon;

  public BootstrapButton_Submit ()
  {
    super ();
    addClass (CBootstrapCSS.BTN);
  }

  @Nonnull
  public EBootstrapButtonType getButtonType ()
  {
    return m_eType;
  }

  @Nonnull
  public BootstrapButton_Submit setButtonType (@Nonnull final EBootstrapButtonType eType)
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
  public BootstrapButton_Submit setSize (@Nullable final EBootstrapButtonSize eSize)
  {
    m_eSize = eSize;
    return this;
  }

  @Nullable
  public IIcon getIcon ()
  {
    return m_aIcon;
  }

  @Nonnull
  public BootstrapButton_Submit setIcon (@Nullable final EDefaultIcon eIcon)
  {
    return setIcon (eIcon == null ? null : eIcon.getIcon ());
  }

  @Nonnull
  public BootstrapButton_Submit setIcon (@Nullable final IIcon aIcon)
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
    addClasses (getButtonType (), getSize ());
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
    if (isDisabled ())
      addClass (CBootstrapCSS.DISABLED);
  }

  @Nonnull
  public static BootstrapButton_Submit create (@Nullable final String sLabel)
  {
    final BootstrapButton_Submit ret = new BootstrapButton_Submit ();
    ret.addChild (sLabel);
    return ret;
  }

  @Nonnull
  public static BootstrapButton_Submit create (@Nullable final IIcon aIcon)
  {
    return new BootstrapButton_Submit ().setIcon (aIcon);
  }

  @Nonnull
  public static BootstrapButton_Submit create (@Nullable final String sLabel, @Nullable final IIcon aIcon)
  {
    return create (sLabel).setIcon (aIcon);
  }

  @Nonnull
  public static BootstrapButton_Submit create (@Nullable final EDefaultIcon eIcon)
  {
    return create (DefaultIcons.get (eIcon));
  }

  @Nonnull
  public static BootstrapButton_Submit create (@Nullable final String sLabel, @Nullable final EDefaultIcon eIcon)
  {
    return create (sLabel, DefaultIcons.get (eIcon));
  }
}
