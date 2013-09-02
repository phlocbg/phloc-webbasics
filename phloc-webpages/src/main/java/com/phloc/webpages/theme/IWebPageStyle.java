package com.phloc.webpages.theme;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.email.IEmailAddress;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.AbstractHCBaseTable;
import com.phloc.html.hc.html.AbstractHCTable;
import com.phloc.html.hc.html.HCA_Target;
import com.phloc.html.hc.html.HCCol;
import com.phloc.webbasics.userdata.UserDataObject;
import com.phloc.webctrls.datatables.DataTables;

public interface IWebPageStyle
{
  @Nonnull
  IHCNode createImageView (@Nullable UserDataObject aUDO, @Nonnull Locale aDisplayLocale);

  @Nonnull
  IHCNode createImageView (@Nullable UserDataObject aUDO, int nMaxWidth, @Nonnull Locale aDisplayLocale);

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
   * @param aDisplayLocale
   *        The display locale to use.
   * @return The control to display
   */
  @Nonnull
  IHCNode createIncorrectInputBox (@Nonnull Locale aDisplayLocale);

  @Nonnull
  IHCElement <?> createErrorBox (@Nullable String sText);

  @Nonnull
  IHCElement <?> createSuccessBox (@Nullable String sText);

  @Nonnull
  IHCElement <?> createQuestionBox (@Nullable String sText);

  @Nonnull
  AbstractHCTable <?> createTable (@Nullable HCCol... aCols);

  @Nonnull
  DataTables createDefaultDataTables (@Nonnull AbstractHCBaseTable <?> aTable, @Nonnull Locale aDisplayLocale);

  @Nonnull
  IHCElement <?> createUploadButton (@Nonnull Locale aDisplayLocale);
}
