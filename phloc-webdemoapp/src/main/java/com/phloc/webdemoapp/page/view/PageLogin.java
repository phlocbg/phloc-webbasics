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
package com.phloc.webdemoapp.page.view;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.security.CSecurity;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webdemoapp.ui.DemoAppAccessUI;
import com.phloc.webpages.AbstractWebPageExt;

public final class PageLogin extends AbstractWebPageExt
{
  public PageLogin (@Nonnull @Nonempty final String sID)
  {
    super (sID, "Login");
  }

  @Override
  @Nullable
  public String getHeaderText (@Nonnull final WebPageExecutionContext aWPEC)
  {
    return null;
  }

  @Override
  protected void fillContent (final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();

    aNodeList.addChild (DemoAppAccessUI.createViewLoginForm (aWPEC.getDisplayLocale (), null));
    aNodeList.addChild (HCDiv.create ("Demo login: " +
                                      CSecurity.USER_ADMINISTRATOR_EMAIL +
                                      " with password '" +
                                      CSecurity.USER_ADMINISTRATOR_PASSWORD +
                                      "'"));
  }
}
