/**
 * Copyright (C) 2006-2012 phloc systems
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
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.url.SimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.AbstractHCCell;
import com.phloc.html.hc.html.HCButton_Submit;
import com.phloc.html.hc.html.HCCenter;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.hc.html.HCEditPassword;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.html.HCTable;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.scopes.web.domain.IRequestWebScope;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.html.LayoutHTMLProvider;

public class BasicLoginHTML extends LayoutHTMLProvider
{
  private final boolean m_bLoginError;
  private final ELoginResult m_eLoginResult;

  public BasicLoginHTML (final boolean bLoginError, @Nonnull final ELoginResult eLoginResult)
  {
    super (CLogin.LAYOUT_AREAID_LOGIN);
    if (eLoginResult == null)
      throw new NullPointerException ("loginResult");
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
  protected String getTextErrorMessage (@Nonnull final Locale aDisplayLocale)
  {
    return EWebBasicsText.LOGIN_ERROR_MSG.getDisplayText (aDisplayLocale);
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

  @OverrideOnDemand
  @Nullable
  protected HCNodeList getLoginScreen (@Nonnull final Locale aDisplayLocale)
  {
    final HCNodeList ret = new HCNodeList ();
    final HCCenter aCenter = ret.addAndReturnChild (new HCCenter ());
    final HCForm aForm = aCenter.addAndReturnChild (new HCForm (new SimpleURL ()));
    aForm.setSubmitPressingEnter (true);

    aForm.addChild (new HCDiv ().addClass (CLogin.CSS_CLASS_LOGIN_APPLOGO));
    if (showHeaderText ())
      aForm.addChild (new HCDiv ().addChild (getTextHeader (aDisplayLocale)).addClass (CLogin.CSS_CLASS_LOGIN_HEADER));
    if (m_bLoginError)
      aForm.addChild (new HCDiv ().addChild (getTextErrorMessage (aDisplayLocale))
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
    final AbstractHCCell aCell = aTable.addBodyRow ().addCell ().setColspan (aTable.getColumnCount ());
    aCell.addChild (new HCButton_Submit (EWebBasicsText.LOGIN_BUTTON_SUBMIT.getDisplayText (aDisplayLocale)));

    return ret;
  }

  @Override
  @Nullable
  protected final IHCNode getContentOfArea (@Nonnull final IRequestWebScope aRequestScope,
                                            @Nonnull final String sAreaID,
                                            @Nonnull final Locale aDisplayLocale)
  {
    return getLoginScreen (aDisplayLocale);
  }
}
