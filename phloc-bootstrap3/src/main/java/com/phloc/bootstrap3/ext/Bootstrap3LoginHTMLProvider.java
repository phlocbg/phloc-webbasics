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
package com.phloc.bootstrap3.ext;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.security.login.ELoginResult;
import com.phloc.bootstrap3.alert.Bootstrap3ErrorBox;
import com.phloc.bootstrap3.base.Bootstrap3Container;
import com.phloc.bootstrap3.button.Bootstrap3SubmitButton;
import com.phloc.bootstrap3.form.Bootstrap3Form;
import com.phloc.bootstrap3.grid.Bootstrap3Row;
import com.phloc.bootstrap3.grid.EBootstrap3GridMD;
import com.phloc.bootstrap3.pageheader.Bootstrap3PageHeader;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.html.HCBody;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.hc.html.HCEditPassword;
import com.phloc.html.hc.html.HCH2;
import com.phloc.html.hc.html.HCHtml;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.login.CLogin;
import com.phloc.webbasics.login.LoginHTMLProvider;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

public class Bootstrap3LoginHTMLProvider extends LoginHTMLProvider
{
  private final String m_sPageTitle;

  public Bootstrap3LoginHTMLProvider (final boolean bLoginError,
                                      @Nonnull final ELoginResult eLoginResult,
                                      @Nullable final String sPageTitle)
  {
    super (bLoginError, eLoginResult);
    m_sPageTitle = sPageTitle;
  }

  @Override
  protected void fillHead (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                           @Nonnull final HCHtml aHtml,
                           @Nonnull final Locale aDisplayLocale)
  {
    super.fillHead (aRequestScope, aHtml, aDisplayLocale);
    aHtml.getHead ().setPageTitle (m_sPageTitle);
  }

  @Override
  protected void fillBody (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                           @Nonnull final HCHtml aHtml,
                           @Nonnull final Locale aDisplayLocale)
  {
    final Bootstrap3Form aForm = new Bootstrap3Form (aRequestScope.getURL ());

    if (showLoginError ())
      aForm.addChild (Bootstrap3ErrorBox.create (getTextErrorMessage (aDisplayLocale, getLoginResult ())));

    // User name and password table
    final String sUserName = EWebBasicsText.EMAIL_ADDRESS.getDisplayText (aDisplayLocale);
    aForm.addFormGroup (sUserName, new HCEdit (CLogin.REQUEST_ATTR_USERID).setPlaceholder (sUserName));

    final String sPassword = EWebBasicsText.LOGIN_FIELD_PASSWORD.getDisplayText (aDisplayLocale);
    aForm.addFormGroup (sPassword, new HCEditPassword (CLogin.REQUEST_ATTR_PASSWORD).setPlaceholder (sPassword));

    // Submit button
    aForm.addChild (new Bootstrap3SubmitButton ().addChild (EWebBasicsText.LOGIN_BUTTON_SUBMIT.getDisplayText (aDisplayLocale)));

    // Layout
    final Bootstrap3Container aContentLayout = new Bootstrap3Container ();
    final Bootstrap3Row aRow = aContentLayout.addAndReturnChild (new Bootstrap3Row ());
    aRow.createColumn (EBootstrap3GridMD.MD_3);
    final HCDiv aCol2 = aRow.createColumn (EBootstrap3GridMD.MD_6);
    aRow.createColumn (EBootstrap3GridMD.MD_3);
    if (StringHelper.hasText (m_sPageTitle))
      aCol2.addChild (new Bootstrap3PageHeader ().addChild (HCH2.create (m_sPageTitle)));
    aCol2.addChild (aForm);

    // Build body
    final HCBody aBody = aHtml.getBody ();
    final HCSpan aSpan = aBody.addAndReturnChild (new HCSpan ().setID (CLogin.LAYOUT_AREAID_LOGIN));
    aSpan.addChild (aContentLayout);
  }
}
