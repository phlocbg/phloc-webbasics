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
package com.phloc.bootstrap2.derived;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap2.BootstrapButton;
import com.phloc.bootstrap2.BootstrapButton_Submit;
import com.phloc.commons.url.SimpleURL;
import com.phloc.html.js.IJSCodeProvider;
import com.phloc.webctrls.custom.IIcon;
import com.phloc.webctrls.custom.toolbar.AbstractButtonToolbar;

/**
 * Bootstrap button toolbar
 * 
 * @author Philip Helger
 */
public class BootstrapToolbar extends AbstractButtonToolbar <BootstrapToolbar>
{
  public BootstrapToolbar ()
  {}

  public BootstrapToolbar (@Nonnull final SimpleURL aSelfHref)
  {
    super (aSelfHref);
  }

  @Nonnull
  public final BootstrapToolbar addButton (@Nullable final String sCaption,
                                           @Nonnull final IJSCodeProvider aJSCode,
                                           @Nullable final IIcon aIcon)
  {
    addChild (BootstrapButton.create (sCaption, aJSCode, aIcon));
    return this;
  }

  @Nonnull
  public final BootstrapToolbar addSubmitButton (@Nullable final String sCaption,
                                                 @Nullable final IJSCodeProvider aOnClick,
                                                 @Nullable final IIcon aIcon)
  {
    addChild (BootstrapButton_Submit.create (sCaption, aIcon).setOnClick (aOnClick));
    return this;
  }
}
