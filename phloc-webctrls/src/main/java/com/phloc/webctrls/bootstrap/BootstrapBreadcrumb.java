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

import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.html.HCUL;
import com.phloc.html.hc.impl.HCTextNode;

/**
 * Breadcrumbs
 * 
 * @author philip
 */
public class BootstrapBreadcrumb extends HCUL
{
  public BootstrapBreadcrumb ()
  {
    addClass (CBootstrapCSS.BREADCRUMB);
  }

  @Nonnull
  public BootstrapBreadcrumb addLink (@Nonnull final ISimpleURL aURL, @Nonnull final String sText)
  {
    addItem (new HCA (aURL).addChild (sText));
    return this;
  }

  @Nonnull
  public BootstrapBreadcrumb addSeparator (@Nullable final String sText)
  {
    if (hasChildren () && StringHelper.hasText (sText))
      getLastItem ().addChildren (new HCTextNode (" "), new HCSpan ().addChild (sText).addClass (CBootstrapCSS.DIVIDER));
    return this;
  }

  @Nonnull
  public BootstrapBreadcrumb addActive (@Nullable final String sText)
  {
    addAndReturnItem (sText).addClass (CBootstrapCSS.ACTIVE);
    return this;
  }
}
