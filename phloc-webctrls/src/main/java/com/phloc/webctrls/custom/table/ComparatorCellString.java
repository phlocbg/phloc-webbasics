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
package com.phloc.webctrls.custom.table;

import java.util.Locale;

import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.compare.AbstractCollationComparator;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCCell;

public class ComparatorCellString extends AbstractCollationComparator <IHCCell <?>>
{
  private final String m_sCommonPrefix;
  private final String m_sCommonSuffix;

  public ComparatorCellString (@Nullable final Locale aParseLocale)
  {
    this (aParseLocale, null, null);
  }

  public ComparatorCellString (@Nullable final Locale aParseLocale,
                               @Nullable final String sCommonPrefix,
                               @Nullable final String sCommonSuffix)
  {
    super (aParseLocale);
    m_sCommonPrefix = StringHelper.hasText (sCommonPrefix) ? sCommonPrefix : null;
    m_sCommonSuffix = StringHelper.hasText (sCommonSuffix) ? sCommonSuffix : null;
  }

  @OverrideOnDemand
  protected String getCellText (@Nullable final IHCCell <?> aCell)
  {
    if (aCell == null)
      return "";

    String sText = aCell.getPlainText ();

    // strip common prefix and suffix
    if (m_sCommonPrefix != null)
      sText = StringHelper.trimStart (sText, m_sCommonPrefix);
    if (m_sCommonSuffix != null)
      sText = StringHelper.trimEnd (sText, m_sCommonSuffix);

    return sText;
  }

  @Override
  protected final String asString (@Nullable final IHCCell <?> aCell)
  {
    return getCellText (aCell);
  }
}
