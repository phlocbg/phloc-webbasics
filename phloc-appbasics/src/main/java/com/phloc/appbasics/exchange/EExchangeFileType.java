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
package com.phloc.appbasics.exchange;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.lang.EnumHelper;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.poi.excel.EExcelVersion;

/**
 * Defines common file types for import/export.
 * 
 * @author Philip Helger
 */
public enum EExchangeFileType implements IHasID <String>, IHasDisplayText
{
  CSV ("csv", ".csv", CMimeType.TEXT_CSV, true, EExchangeFileTypeName.CSV),
  XLS ("xls", EExcelVersion.XLS.getFileExtension (), EExcelVersion.XLS.getMimeType (), true, EExchangeFileTypeName.XLS),
  XLSX ("xlsx", EExcelVersion.XLSX.getFileExtension (), EExcelVersion.XLSX.getMimeType (), true, EExchangeFileTypeName.XLSX),
  XML ("xml", ".xml", CMimeType.TEXT_XML, false, EExchangeFileTypeName.XML),
  TXT ("txt", ".txt", CMimeType.TEXT_PLAIN, false, EExchangeFileTypeName.TXT);

  private final String m_sID;
  private final String m_sExt;
  private final IMimeType m_aMimeType;
  private final boolean m_bLineBased;
  private final IHasDisplayText m_aName;

  private EExchangeFileType (@Nonnull @Nonempty final String sID,
                             @Nonnull @Nonempty final String sExt,
                             @Nonnull final IMimeType aMimeType,
                             final boolean bLineBased,
                             @Nonnull final EExchangeFileTypeName aName)
  {
    m_sID = sID;
    m_sExt = sExt;
    m_aMimeType = aMimeType;
    m_bLineBased = bLineBased;
    m_aName = aName;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  /**
   * @return The desired file extension including the leading dot.
   */
  @Nonnull
  @Nonempty
  public String getFileExtension ()
  {
    return m_sExt;
  }

  /**
   * @return The MIMe type for created files.
   */
  @Nonnull
  public IMimeType getMimeType ()
  {
    return m_aMimeType;
  }

  /**
   * @return <code>true</code> if this file type is line based. This is e.g. the
   *         case for CSV or Excel files.
   */
  public boolean isLineBased ()
  {
    return m_bLineBased;
  }

  @Nullable
  public String getDisplayText (@Nonnull final Locale aContentLocale)
  {
    return m_aName.getDisplayText (aContentLocale);
  }

  @Nullable
  public String getExportAsText (@Nonnull final Locale aContentLocale)
  {
    return EExchangeFileTypeText.EXPORT_AS.getDisplayTextWithArgs (aContentLocale, getDisplayText (aContentLocale));
  }

  @Nullable
  public String getSaveAsText (@Nonnull final Locale aContentLocale)
  {
    return EExchangeFileTypeText.SAVE_AS.getDisplayTextWithArgs (aContentLocale, getDisplayText (aContentLocale));
  }

  @Nullable
  public static EExchangeFileType getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EExchangeFileType.class, sID);
  }
}
