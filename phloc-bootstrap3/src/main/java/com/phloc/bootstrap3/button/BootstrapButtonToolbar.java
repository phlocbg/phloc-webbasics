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

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.commons.url.SimpleURL;
import com.phloc.html.EHTMLRole;
import com.phloc.html.js.IJSCodeProvider;
import com.phloc.webbasics.app.layout.ILayoutExecutionContext;
import com.phloc.webctrls.custom.IIcon;
import com.phloc.webctrls.custom.toolbar.AbstractButtonToolbar;

/**
 * Bootstrap3 button toolbar
 *
 * @author Philip Helger
 */
public class BootstrapButtonToolbar extends AbstractButtonToolbar <BootstrapButtonToolbar>
{
  @Deprecated
  public BootstrapButtonToolbar ()
  {
    addClass (CBootstrapCSS.BTN_TOOLBAR);
    setRole (EHTMLRole.TOOLBAR);
  }

  public BootstrapButtonToolbar (@Nonnull final ILayoutExecutionContext aLEC)
  {
    this (aLEC.getSelfHref ());
  }

  public BootstrapButtonToolbar (@Nonnull final SimpleURL aSelfHref)
  {
    super (aSelfHref);
    addClass (CBootstrapCSS.BTN_TOOLBAR);
  }

  @Nonnull
  public final BootstrapButtonToolbar addButton (@Nullable final String sCaption,
                                                 @Nonnull final IJSCodeProvider aJSCode,
                                                 @Nullable final IIcon aIcon)
  {
    addChild (new BootstrapButton ().setIcon (aIcon).addChild (sCaption).setOnClick (aJSCode));
    return this;
  }

  @Nonnull
  public final BootstrapButtonToolbar addSubmitButton (@Nullable final String sCaption,
                                                       @Nullable final IJSCodeProvider aOnClick,
                                                       @Nullable final IIcon aIcon)
  {
    addChild (new BootstrapSubmitButton ().setIcon (aIcon).setOnClick (aOnClick).addChild (sCaption));
    return this;
  }
}
