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
package com.phloc.webctrls.bootstrap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.ReadonlySimpleURL;
import com.phloc.html.hc.conversion.IHCConversionSettings;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.html.js.CJS;
import com.phloc.html.js.EJSEvent;
import com.phloc.html.js.IJSCodeProvider;
import com.phloc.html.js.builder.JSExpr;
import com.phloc.html.js.builder.JSReturn;
import com.phloc.html.js.builder.JSStatementList;
import com.phloc.html.js.builder.html.JSHtml;
import com.phloc.html.js.provider.CollectingJSCodeProvider;
import com.phloc.webctrls.custom.DefaultIcons;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webctrls.custom.IIcon;

public class BootstrapButton extends HCA
{
  private static final ISimpleURL JS_URL = new ReadonlySimpleURL (CJS.JS_VOID);
  private EBootstrapButtonType m_eType = EBootstrapButtonType.DEFAULT;
  private EBootstrapButtonSize m_eSize;
  private IIcon m_aIcon;

  private void _init ()
  {
    addClass (CBootstrapCSS.BTN);
  }

  public BootstrapButton (@Nonnull final ISimpleURL aURL)
  {
    super (JS_URL);
    _init ();
    addEventHandler (EJSEvent.ONCLICK, new JSStatementList (JSHtml.windowLocationHref ().assign (aURL.getAsString ()),
                                                            new JSReturn (JSExpr.FALSE)));
  }

  public BootstrapButton (@Nonnull final IJSCodeProvider aJSCode)
  {
    super (JS_URL);
    _init ();
    addEventHandler (EJSEvent.ONCLICK, new CollectingJSCodeProvider (aJSCode, new JSReturn (JSExpr.FALSE)));
  }

  public BootstrapButton (@Nonnull final ISimpleURL aURL, @Nullable final String sLabel)
  {
    this (aURL);
    addChild (sLabel);
  }

  public BootstrapButton (@Nonnull final IJSCodeProvider aJSCode, @Nullable final String sLabel)
  {
    this (aJSCode);
    addChild (sLabel);
  }

  public BootstrapButton (@Nonnull final ISimpleURL aURL, @Nullable final IIcon aIcon)
  {
    this (aURL);
    setIcon (aIcon);
  }

  public BootstrapButton (@Nonnull final IJSCodeProvider aJSCode, @Nullable final IIcon aIcon)
  {
    this (aJSCode);
    setIcon (aIcon);
  }

  public BootstrapButton (@Nonnull final ISimpleURL aURL, @Nonnull final EDefaultIcon eIcon)
  {
    this (aURL, DefaultIcons.get (eIcon));
  }

  public BootstrapButton (@Nonnull final IJSCodeProvider aJSCode, @Nonnull final EDefaultIcon eIcon)
  {
    this (aJSCode, DefaultIcons.get (eIcon));
  }

  public BootstrapButton (@Nonnull final ISimpleURL aURL, @Nullable final String sLabel, @Nullable final IIcon aIcon)
  {
    this (aURL, sLabel);
    setIcon (aIcon);
  }

  public BootstrapButton (@Nonnull final IJSCodeProvider aJSCode,
                          @Nullable final String sLabel,
                          @Nullable final IIcon aIcon)
  {
    this (aJSCode, sLabel);
    setIcon (aIcon);
  }

  public BootstrapButton (@Nonnull final ISimpleURL aURL,
                          @Nullable final String sLabel,
                          @Nonnull final EDefaultIcon eIcon)
  {
    this (aURL, sLabel, DefaultIcons.get (eIcon));
  }

  public BootstrapButton (@Nonnull final IJSCodeProvider aJSCode,
                          @Nullable final String sLabel,
                          @Nonnull final EDefaultIcon eIcon)
  {
    this (aJSCode, sLabel, DefaultIcons.get (eIcon));
  }

  @Nonnull
  public BootstrapButton setType (@Nonnull final EBootstrapButtonType eType)
  {
    if (eType == null)
      throw new NullPointerException ("type");
    m_eType = eType;
    return this;
  }

  @Nonnull
  public BootstrapButton setSize (@Nullable final EBootstrapButtonSize eSize)
  {
    m_eSize = eSize;
    return this;
  }

  @Nonnull
  public BootstrapButton setIcon (@Nullable final IIcon aIcon)
  {
    m_aIcon = aIcon;
    return this;
  }

  @Override
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void prepareOnceBeforeCreateElement (@Nonnull final IHCConversionSettings aConversionSettings)
  {
    super.prepareOnceBeforeCreateElement (aConversionSettings);
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
}
