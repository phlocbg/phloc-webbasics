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
package com.phloc.bootstrap3.breadcrumbs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrap3CSS;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCOL;
import com.phloc.html.hc.htmlext.HCA_JS;
import com.phloc.html.js.IJSCodeProvider;

/**
 * Breadcrumbs
 * 
 * @author Philip Helger
 */
public class Bootstrap3Breadcrumbs extends HCOL
{
  public Bootstrap3Breadcrumbs ()
  {
    addClass (CBootstrap3CSS.BREADCRUMB);
  }

  @Nonnull
  public Bootstrap3Breadcrumbs addLink (@Nonnull final ISimpleURL aURL, @Nonnull final String sText)
  {
    addItem (new HCA (aURL).addChild (sText));
    return this;
  }

  @Nonnull
  public Bootstrap3Breadcrumbs addLink (@Nonnull final IJSCodeProvider aJSCodeProvider, @Nonnull final String sText)
  {
    addItem (new HCA_JS (aJSCodeProvider).addChild (sText));
    return this;
  }

  @Nonnull
  public Bootstrap3Breadcrumbs addActive (@Nullable final String sText)
  {
    addAndReturnItem (sText).addClass (CBootstrap3CSS.ACTIVE);
    return this;
  }
}
