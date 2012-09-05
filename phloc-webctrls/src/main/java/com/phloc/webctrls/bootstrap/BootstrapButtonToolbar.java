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

import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.AbstractHCDiv;
import com.phloc.html.hc.html.HCHiddenField;
import com.phloc.html.js.IJSCodeProvider;
import com.phloc.webctrls.custom.EDefaultIcon;

/**
 * Bootstrap block help.
 * 
 * @author philip
 */
public class BootstrapButtonToolbar extends AbstractHCDiv <BootstrapButtonToolbar>
{
  private void _init ()
  {
    addClass (CBootstrapCSS.BTN_TOOLBAR);
  }

  public BootstrapButtonToolbar ()
  {
    super ();
    _init ();
  }

  public BootstrapButtonToolbar (@Nonnull final IPredefinedLocaleTextProvider aChild)
  {
    this (aChild.getText ());
  }

  public BootstrapButtonToolbar (@Nullable final String sChild)
  {
    super (sChild);
    _init ();
  }

  public BootstrapButtonToolbar (@Nullable final String... aChildren)
  {
    super (aChildren);
    _init ();
  }

  public BootstrapButtonToolbar (@Nullable final IHCNode aChild)
  {
    super (aChild);
    _init ();
  }

  public BootstrapButtonToolbar (@Nullable final IHCNode... aChildren)
  {
    super (aChildren);
    _init ();
  }

  public BootstrapButtonToolbar (@Nullable final Iterable <? extends IHCNode> aChildren)
  {
    super (aChildren);
    _init ();
  }

  @Nonnull
  public final BootstrapButtonToolbar addHiddenField (@Nullable final String sName, @Nullable final String sValue)
  {
    addChild (new HCHiddenField (sName, sValue));
    return this;
  }

  @Nonnull
  public final BootstrapButtonToolbar addButton (final IJSCodeProvider aJSCode, final String sCaption)
  {
    addChild (new BootstrapButton (sCaption, aJSCode));
    return this;
  }

  @Nonnull
  public final BootstrapButtonToolbar addButton (final ISimpleURL aURL, final String sCaption)
  {
    addChild (new BootstrapButton (sCaption, aURL));
    return this;
  }

  @Nonnull
  public final BootstrapButtonToolbar addButton (final IJSCodeProvider aJSCode,
                                                 final String sCaption,
                                                 final EDefaultIcon eIcon)
  {
    addChild (new BootstrapButton (sCaption, aJSCode, eIcon));
    return this;
  }

  @Nonnull
  public final BootstrapButtonToolbar addButton (final ISimpleURL aURL, final String sCaption, final EDefaultIcon eIcon)
  {
    addChild (new BootstrapButton (sCaption, aURL, eIcon));
    return this;
  }

  @Nonnull
  public final BootstrapButtonToolbar addSubmitButton (final String sCaption)
  {
    addChild (new BootstrapButton_Submit (sCaption));
    return this;
  }

  @Nonnull
  public final BootstrapButtonToolbar addSubmitButtonSave ()
  {
    addChild (new BootstrapButton_Submit ("Speichern", EDefaultIcon.SAVE));
    return this;
  }

  @Nonnull
  public final BootstrapButtonToolbar addSubmitButtonYes ()
  {
    addChild (new BootstrapButton_Submit ("Ja", EDefaultIcon.YES));
    return this;
  }
}
