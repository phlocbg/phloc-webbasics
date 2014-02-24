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
package com.phloc.bootstrap2.ext;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.security.login.ELoginResult;
import com.phloc.bootstrap2.BootstrapButton_Submit;
import com.phloc.bootstrap2.BootstrapContainer;
import com.phloc.bootstrap2.BootstrapFormActions;
import com.phloc.bootstrap2.BootstrapPageHeader;
import com.phloc.bootstrap2.BootstrapRow;
import com.phloc.bootstrap2.CBootstrapCSS;
import com.phloc.bootstrap2.EBootstrapSpan;
import com.phloc.bootstrap2.derived.BootstrapErrorBox;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.html.HCBody;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.hc.html.HCEditPassword;
import com.phloc.html.hc.html.HCFieldSet;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCH2;
import com.phloc.html.hc.html.HCHiddenField;
import com.phloc.html.hc.html.HCHtml;
import com.phloc.html.hc.html.HCLabel;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.login.CLogin;
import com.phloc.webbasics.login.LoginHTMLProvider;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

public class BootstrapLoginHTMLProvider extends LoginHTMLProvider
{
  private final String m_sPageTitle;

  public BootstrapLoginHTMLProvider (final boolean bLoginError,
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
    final HCForm aForm = new HCForm (aRequestScope.getURL ());

    // The hidden field that triggers the validation
    aForm.addChild (new HCHiddenField (CHCParam.PARAM_ACTION, CLogin.ACTION_DO_LOGIN));

    if (showLoginError ())
      aForm.addChild (BootstrapErrorBox.create (getTextErrorMessage (aDisplayLocale, getLoginResult ())));

    final HCFieldSet aFieldSet = aForm.addAndReturnChild (new HCFieldSet ());

    // User name and password table
    final String sUserName = EWebBasicsText.EMAIL_ADDRESS.getDisplayText (aDisplayLocale);
    aFieldSet.addChildren (HCLabel.create (sUserName),
                           new HCEdit (CLogin.REQUEST_ATTR_USERID).setPlaceholder (sUserName)
                                                                  .addClass (CBootstrapCSS.INPUT_BLOCK_LEVEL));

    final String sPassword = EWebBasicsText.LOGIN_FIELD_PASSWORD.getDisplayText (aDisplayLocale);
    aFieldSet.addChildren (HCLabel.create (sPassword),
                           new HCEditPassword (CLogin.REQUEST_ATTR_PASSWORD).setPlaceholder (sPassword)
                                                                            .addClass (CBootstrapCSS.INPUT_BLOCK_LEVEL));

    // Submit button
    aForm.addChild (new BootstrapFormActions ().addChild (BootstrapButton_Submit.create (EWebBasicsText.LOGIN_BUTTON_SUBMIT.getDisplayText (aDisplayLocale))));

    final BootstrapRow aRow = new BootstrapRow ();
    aRow.addColumn (EBootstrapSpan.SPAN3);
    aRow.addColumn (EBootstrapSpan.SPAN6,
                    StringHelper.hasText (m_sPageTitle) ? new BootstrapPageHeader ().addChild (HCH2.create (m_sPageTitle))
                                                       : null,
                    aForm);
    aRow.addColumn (EBootstrapSpan.SPAN3);

    final BootstrapContainer aContentLayout = new BootstrapContainer (true);
    aContentLayout.setContent (aRow);

    // Build body
    final HCBody aBody = aHtml.getBody ();
    final HCSpan aSpan = aBody.addAndReturnChild (new HCSpan ().setID (CLogin.LAYOUT_AREAID_LOGIN));
    aSpan.addChild (aContentLayout);
  }
}
