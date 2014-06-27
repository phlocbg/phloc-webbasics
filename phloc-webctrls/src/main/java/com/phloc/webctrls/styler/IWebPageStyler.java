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
package com.phloc.webctrls.styler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.email.IEmailAddress;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCTable;
import com.phloc.html.hc.html.HCA_Target;
import com.phloc.html.hc.html.HCCol;
import com.phloc.webbasics.app.page.IWebPageExecutionContext;
import com.phloc.webbasics.userdata.UserDataObject;
import com.phloc.webctrls.custom.tabbox.ITabBox;
import com.phloc.webctrls.custom.table.IHCTableForm;
import com.phloc.webctrls.custom.table.IHCTableFormView;
import com.phloc.webctrls.custom.toolbar.IButtonToolbar;
import com.phloc.webctrls.datatables.DataTables;

public interface IWebPageStyler
{
  @Nonnull
  IHCNode createImageView (@Nonnull IWebPageExecutionContext aWPEC, @Nullable UserDataObject aUDO);

  @Nonnull
  IHCNode createImageView (@Nonnull IWebPageExecutionContext aWPEC, @Nullable UserDataObject aUDO, int nMaxWidth);

  @Nullable
  IHCNode createEmailLink (@Nullable String sEmailAddress);

  @Nullable
  IHCNode createEmailLink (@Nullable IEmailAddress aEmail);

  @Nullable
  IHCNode createWebLink (@Nullable String sWebSite);

  @Nullable
  IHCNode createWebLink (@Nullable String sWebSite, @Nullable HCA_Target aTarget);

  /**
   * Create a box that shows a constant note, that changes could not be saved,
   * because the user did not enter all form data correctly.
   * 
   * @param aWPEC
   *        Simple web execution context. Never <code>null</code>.
   * @return The control to display
   */
  @Nonnull
  IHCNode createIncorrectInputBox (@Nonnull IWebPageExecutionContext aWPEC);

  @Nonnull
  IHCElement <?> createErrorBox (@Nonnull IWebPageExecutionContext aWPEC, @Nullable String sText);

  @Nonnull
  IHCElement <?> createInfoBox (@Nonnull IWebPageExecutionContext aWPEC, @Nullable String sText);

  @Nonnull
  IHCElement <?> createSuccessBox (@Nonnull IWebPageExecutionContext aWPEC, @Nullable String sText);

  @Nonnull
  IHCElement <?> createQuestionBox (@Nonnull IWebPageExecutionContext aWPEC, @Nullable String sText);

  @Nonnull
  IHCTable <?> createTable (@Nullable HCCol... aCols);

  @Nonnull
  IHCTableForm <?> createTableForm (@Nullable HCCol... aCols);

  @Nonnull
  IHCTableFormView <?> createTableFormView (@Nullable HCCol... aCols);

  @Nonnull
  DataTables createDefaultDataTables (@Nonnull IWebPageExecutionContext aWPEC, @Nonnull IHCTable <?> aTable);

  @Nonnull
  IHCElement <?> createUploadButton (@Nonnull IWebPageExecutionContext aWPEC);

  @Nonnull
  IButtonToolbar <?> createToolbar (@Nonnull IWebPageExecutionContext aWPEC);

  @Nonnull
  ITabBox <?> createTabBox (@Nonnull IWebPageExecutionContext aWPEC);
}
