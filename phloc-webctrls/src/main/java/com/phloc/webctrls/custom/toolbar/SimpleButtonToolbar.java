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
package com.phloc.webctrls.custom.toolbar;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.url.SimpleURL;
import com.phloc.html.hc.html.HCButton;
import com.phloc.html.js.IJSCodeProvider;
import com.phloc.webbasics.app.layout.LayoutExecutionContext;
import com.phloc.webctrls.custom.IIcon;

/**
 * Simple button toolbar
 * 
 * @author Philip Helger
 */
public class SimpleButtonToolbar extends AbstractButtonToolbar <SimpleButtonToolbar>
{
  @Deprecated
  public SimpleButtonToolbar ()
  {}

  public SimpleButtonToolbar (@Nonnull final LayoutExecutionContext aLEC)
  {
    this (aLEC.getSelfHref ());
  }

  public SimpleButtonToolbar (@Nonnull final SimpleURL aSelfHref)
  {
    super (aSelfHref);
  }

  @Nonnull
  public final SimpleButtonToolbar addButton (@Nullable final String sCaption,
                                              @Nonnull final IJSCodeProvider aOnClick,
                                              @Nullable final IIcon aIcon)
  {
    addChild (new HCButton ().addChild (aIcon == null ? null : aIcon.getAsNode ())
                             .addChild (sCaption)
                             .setOnClick (aOnClick));
    return this;
  }

  @Nonnull
  public final SimpleButtonToolbar addSubmitButton (@Nullable final String sCaption,
                                                    @Nullable final IJSCodeProvider aOnClick,
                                                    @Nullable final IIcon aIcon)
  {
    addChild (new HCButton ().addChild (aIcon == null ? null : aIcon.getAsNode ())
                             .addChild (sCaption)
                             .setOnClick (aOnClick));
    return this;
  }
}
