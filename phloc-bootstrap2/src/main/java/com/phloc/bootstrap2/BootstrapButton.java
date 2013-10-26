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
package com.phloc.bootstrap2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;
import com.phloc.html.hc.htmlext.HCA_JS;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.html.js.IJSCodeProvider;
import com.phloc.html.js.builder.html.JSHtml;
import com.phloc.webctrls.custom.DefaultIcons;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webctrls.custom.IIcon;

public class BootstrapButton extends HCA_JS
{
  private EBootstrapButtonType m_eType = EBootstrapButtonType.DEFAULT;
  private EBootstrapButtonSize m_eSize;
  private IIcon m_aIcon;
  private boolean m_bDisabled = false;

  private void _init ()
  {
    addClass (CBootstrapCSS.BTN);
  }

  public BootstrapButton (@Nonnull final ISimpleURL aURL)
  {
    super (JSHtml.windowLocationHref (aURL));
    _init ();
  }

  public BootstrapButton (@Nonnull final IJSCodeProvider aJSCode)
  {
    super (aJSCode);
    _init ();
  }

  @Nonnull
  public EBootstrapButtonType getButtonType ()
  {
    return m_eType;
  }

  @Nonnull
  public BootstrapButton setButtonType (@Nonnull final EBootstrapButtonType eType)
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
  public BootstrapButton setSize (@Nullable final EBootstrapButtonSize eSize)
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
  public BootstrapButton setIcon (@Nullable final EDefaultIcon eIcon)
  {
    return setIcon (eIcon == null ? null : eIcon.getIcon ());
  }

  @Nonnull
  public BootstrapButton setIcon (@Nullable final IIcon aIcon)
  {
    m_aIcon = aIcon;
    return this;
  }

  public boolean isDisabled ()
  {
    return m_bDisabled;
  }

  @Nonnull
  public BootstrapButton setDisabled (final boolean bDisabled)
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
    if (isDisabled ())
      addClass (CBootstrapCSS.DISABLED);
  }

  @Nonnull
  public static BootstrapButton create (@Nullable final String sLabel, @Nonnull final ISimpleURL aURL)
  {
    final BootstrapButton ret = new BootstrapButton (aURL);
    ret.addChild (sLabel);
    return ret;
  }

  public static BootstrapButton create (@Nullable final String sLabel, @Nonnull final IJSCodeProvider aJSCode)
  {
    final BootstrapButton ret = new BootstrapButton (aJSCode);
    ret.addChild (sLabel);
    return ret;
  }

  @Nonnull
  public static BootstrapButton create (@Nonnull final ISimpleURL aURL, @Nullable final IIcon aIcon)
  {
    final BootstrapButton ret = new BootstrapButton (aURL);
    ret.setIcon (aIcon);
    return ret;
  }

  @Nonnull
  public static BootstrapButton create (@Nonnull final IJSCodeProvider aJSCode, @Nullable final IIcon aIcon)
  {
    final BootstrapButton ret = new BootstrapButton (aJSCode);
    ret.setIcon (aIcon);
    return ret;
  }

  @Nonnull
  public static BootstrapButton create (@Nonnull final ISimpleURL aURL, @Nullable final EDefaultIcon eIcon)
  {
    return create (aURL, DefaultIcons.get (eIcon));
  }

  @Nonnull
  public static BootstrapButton create (@Nonnull final IJSCodeProvider aJSCode, @Nullable final EDefaultIcon eIcon)
  {
    return create (aJSCode, DefaultIcons.get (eIcon));
  }

  @Nonnull
  public static BootstrapButton create (@Nullable final String sLabel,
                                        @Nonnull final ISimpleURL aURL,
                                        @Nullable final IIcon aIcon)
  {
    return create (sLabel, aURL).setIcon (aIcon);
  }

  @Nonnull
  public static BootstrapButton create (@Nullable final String sLabel,
                                        @Nonnull final IJSCodeProvider aJSCode,
                                        @Nullable final IIcon aIcon)
  {
    return create (sLabel, aJSCode).setIcon (aIcon);
  }

  @Nonnull
  public static BootstrapButton create (@Nullable final String sLabel,
                                        @Nonnull final ISimpleURL aURL,
                                        @Nullable final EDefaultIcon eIcon)
  {
    return create (sLabel, aURL, DefaultIcons.get (eIcon));
  }

  @Nonnull
  public static BootstrapButton create (@Nullable final String sLabel,
                                        @Nonnull final IJSCodeProvider aJSCode,
                                        @Nullable final EDefaultIcon eIcon)
  {
    return create (sLabel, aJSCode, DefaultIcons.get (eIcon));
  }
}
