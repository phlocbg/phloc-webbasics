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

import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
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
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.html.HTMLCreationManager;

public class BasicLoginHTML extends HTMLCreationManager
{
  private final boolean m_bLoginError;

  public BasicLoginHTML (final boolean bLoginError)
  {
    m_bLoginError = bLoginError;
  }

  /**
   * @return <code>true</code> if the login screen is shown for an error,
   *         <code>false</code> if the login screen is shown for the first time.
   */
  public final boolean showLoginError ()
  {
    return m_bLoginError;
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
   * Add additional rows. Called after username and password row are added but
   * before the submit button is added.
   * 
   * @param aTable
   *        The table to be modified.
   */
  @OverrideOnDemand
  protected void customizeLoginFields (@Nonnull final HCTable aTable)
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
      aForm.addChild (new HCDiv (EWebBasicsText.LOGIN_HEADER.getDisplayText (aDisplayLocale)).addClass (CLogin.CSS_CLASS_LOGIN_HEADER));
    if (m_bLoginError)
      aForm.addChild (new HCDiv (EWebBasicsText.LOGIN_ERROR_MSG.getDisplayText (aDisplayLocale)).addClass (CLogin.CSS_CLASS_LOGIN_ERRORMSG));

    // User name and password table
    final HCTable aTable = aForm.addAndReturnChild (new HCTable (new HCCol (200), HCCol.star ()));
    HCRow aRow = aTable.addBodyRow ();
    aRow.addCell (EWebBasicsText.LOGIN_FIELD_USERNAME.getDisplayText (aDisplayLocale));
    aRow.addCell (new HCEdit (CLogin.REQUEST_ATTR_USERID));

    aRow = aTable.addBodyRow ();
    aRow.addCell (EWebBasicsText.LOGIN_FIELD_PASSWORD.getDisplayText (aDisplayLocale));
    aRow.addCell (new HCEditPassword (CLogin.REQUEST_ATTR_PASSWORD));

    // Customize
    customizeLoginFields (aTable);

    // Submit button
    final AbstractHCCell aCell = aTable.addBodyRow ().addCell ().setColspan (aTable.getColumnCount ());
    aCell.addChild (new HCButton_Submit (EWebBasicsText.LOGIN_BUTTON_SUBMIT.getDisplayText (aDisplayLocale)));

    return ret;
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  protected final List <String> getAllAreaIDs ()
  {
    // Name of the area ID is used in CSS!
    return ContainerHelper.newList (CLogin.LAYOUT_AREAID_LOGIN);
  }

  @Override
  @Nullable
  protected final IHCNode getContentOfArea (@Nonnull final String sAreaID, @Nonnull final Locale aDisplayLocale)
  {
    return getLoginScreen (aDisplayLocale);
  }
}
