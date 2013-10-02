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
package com.phloc.bootstrap3.button;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrap3CSS;
import com.phloc.html.js.IJSCodeProvider;
import com.phloc.webctrls.custom.IIcon;
import com.phloc.webctrls.custom.toolbar.AbstractButtonToolbar;

/**
 * Bootstrap3 button toolbar
 * 
 * @author Philip Helger
 */
public class Bootstrap3ButtonToolbar extends AbstractButtonToolbar <Bootstrap3ButtonToolbar>
{
  public Bootstrap3ButtonToolbar ()
  {
    addClass (CBootstrap3CSS.BTN_TOOLBAR);
  }

  @Nonnull
  public final Bootstrap3ButtonToolbar addButton (@Nullable final String sCaption,
                                                  @Nonnull final IJSCodeProvider aJSCode,
                                                  @Nullable final IIcon aIcon)
  {
    addChild (new Bootstrap3Button ().setIcon (aIcon).addChild (sCaption).setOnClick (aJSCode));
    return this;
  }

  @Nonnull
  public final Bootstrap3ButtonToolbar addSubmitButton (@Nullable final String sCaption,
                                                        @Nullable final IJSCodeProvider aOnClick,
                                                        @Nullable final IIcon aIcon)
  {
    addChild (new Bootstrap3SubmitButton ().setIcon (aIcon).setOnClick (aOnClick).addChild (sCaption));
    return this;
  }
}