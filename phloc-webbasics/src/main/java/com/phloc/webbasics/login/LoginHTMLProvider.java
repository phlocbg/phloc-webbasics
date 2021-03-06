/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.webbasics.login;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.security.login.ELoginResult;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.html.hc.IHCCell;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCBody;
import com.phloc.html.hc.html.HCButton_Submit;
import com.phloc.html.hc.html.HCCenter;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.hc.html.HCEditPassword;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCHiddenField;
import com.phloc.html.hc.html.HCHtml;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.html.HCTable;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.ISimpleWebExecutionContext;
import com.phloc.webbasics.app.html.AbstractHTMLProvider;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Provide a login screen
 * 
 * @author Philip Helger
 */
public class LoginHTMLProvider extends AbstractHTMLProvider
{
  private final boolean m_bLoginError;
  private final ELoginResult m_eLoginResult;

  public LoginHTMLProvider (final boolean bLoginError, @Nonnull final ELoginResult eLoginResult)
  {
    ValueEnforcer.notNull (eLoginResult, "LoginResult");
    m_bLoginError = bLoginError;
    m_eLoginResult = eLoginResult;
  }

  /**
   * @return <code>true</code> if the login screen is shown for an error,
   *         <code>false</code> if the login screen is shown for the first time.
   */
  public final boolean showLoginError ()
  {
    return m_bLoginError;
  }

  @Nonnull
  public final ELoginResult getLoginResult ()
  {
    return m_eLoginResult;
  }

  /**
   * @return <code>true</code> if the header text should be shown,
   *         <code>false</code> to not show the header text
   */
  @OverrideOnDemand
  protected boolean showHeaderText ()
  {
    return false;
  }

  /**
   * Define how label elements should look like
   * 
   * @param sText
   *        Text to use
   * @return The label node
   */
  @OverrideOnDemand
  @Nullable
  protected IHCNode createLabelNode (@Nullable final String sText)
  {
    return new HCTextNode (sText);
  }

  @Nullable
  @OverrideOnDemand
  protected String getTextHeader (@Nonnull final Locale aDisplayLocale)
  {
    return EWebBasicsText.LOGIN_HEADER.getDisplayText (aDisplayLocale);
  }

  @Nullable
  @OverrideOnDemand
  protected String getTextErrorMessage (@Nonnull final Locale aDisplayLocale, @Nonnull final ELoginResult eLoginResult)
  {
    return EWebBasicsText.LOGIN_ERROR_MSG.getDisplayText (aDisplayLocale) +
           " " +
           eLoginResult.getDisplayText (aDisplayLocale);
  }

  @Nullable
  @OverrideOnDemand
  protected String getTextFieldUserName (@Nonnull final Locale aDisplayLocale)
  {
    return EWebBasicsText.LOGIN_FIELD_USERNAME.getDisplayText (aDisplayLocale);
  }

  @Nullable
  @OverrideOnDemand
  protected String getTextFieldPassword (@Nonnull final Locale aDisplayLocale)
  {
    return EWebBasicsText.LOGIN_FIELD_PASSWORD.getDisplayText (aDisplayLocale);
  }

  /**
   * Add additional rows. Called after username and password row are added but
   * before the submit button is added.
   * 
   * @param aTable
   *        The table to be modified.
   * @param aDisplayLocale
   *        Display locale to use
   */
  @OverrideOnDemand
  protected void customizeLoginFields (@Nonnull final HCTable aTable, @Nonnull final Locale aDisplayLocale)
  {}

  @Override
  protected void fillBody (@Nonnull final ISimpleWebExecutionContext aSWEC, @Nonnull final HCHtml aHtml)
  {
    final IRequestWebScopeWithoutResponse aRequestScope = aSWEC.getRequestScope ();
    final Locale aDisplayLocale = aSWEC.getDisplayLocale ();

    final HCBody aBody = aHtml.getBody ();
    final HCSpan aSpan = aBody.addAndReturnChild (new HCSpan ().setID (CLogin.LAYOUT_AREAID_LOGIN));
    final HCCenter aCenter = aSpan.addAndReturnChild (new HCCenter ());
    final HCForm aForm = aCenter.addAndReturnChild (new HCForm (aRequestScope.getURL ()));
    aForm.setSubmitPressingEnter (true);

    // The hidden field that triggers the validation
    aForm.addChild (new HCHiddenField (CLogin.REQUEST_PARAM_ACTION, CLogin.REQUEST_ACTION_VALIDATE_LOGIN_CREDENTIALS));

    aForm.addChild (new HCDiv ().addClass (CLogin.CSS_CLASS_LOGIN_APPLOGO));
    if (showHeaderText ())
      aForm.addChild (new HCDiv ().addChild (getTextHeader (aDisplayLocale)).addClass (CLogin.CSS_CLASS_LOGIN_HEADER));
    if (m_bLoginError)
      aForm.addChild (new HCDiv ().addChild (getTextErrorMessage (aDisplayLocale, m_eLoginResult))
                                  .addClass (CLogin.CSS_CLASS_LOGIN_ERRORMSG));

    // User name and password table
    final HCTable aTable = aForm.addAndReturnChild (new HCTable (new HCCol (200), HCCol.star ()));
    HCRow aRow = aTable.addBodyRow ();
    aRow.addCell (createLabelNode (getTextFieldUserName (aDisplayLocale)));
    aRow.addCell (new HCEdit (CLogin.REQUEST_ATTR_USERID));

    aRow = aTable.addBodyRow ();
    aRow.addCell (createLabelNode (getTextFieldPassword (aDisplayLocale)));
    aRow.addCell (new HCEditPassword (CLogin.REQUEST_ATTR_PASSWORD));

    // Customize
    customizeLoginFields (aTable, aDisplayLocale);

    // Submit button
    final IHCCell <?> aCell = aTable.addBodyRow ().addCell ().setColspan (aTable.getColumnCount ());
    aCell.addChild (new HCButton_Submit (EWebBasicsText.LOGIN_BUTTON_SUBMIT.getDisplayText (aDisplayLocale)));
  }
}
