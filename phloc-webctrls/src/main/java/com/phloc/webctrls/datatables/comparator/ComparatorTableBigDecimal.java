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
package com.phloc.webctrls.datatables.comparator;

import java.math.BigDecimal;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.format.IFormatter;
import com.phloc.commons.locale.LocaleFormatter;

/**
 * This comparator is responsible for sorting cells by BigDecimal
 * 
 * @author philip
 */
public class ComparatorTableBigDecimal extends AbstractComparatorTable
{
  protected static final BigDecimal DEFAULT_VALUE = BigDecimal.ZERO;
  private final Locale m_aParseLocale;

  public ComparatorTableBigDecimal (@Nonnull final Locale aParseLocale)
  {
    this (null, aParseLocale);
  }

  public ComparatorTableBigDecimal (@Nullable final IFormatter aFormatter, @Nonnull final Locale aParseLocale)
  {
    super (aFormatter);
    if (aParseLocale == null)
      throw new NullPointerException ("parseLocale");
    m_aParseLocale = aParseLocale;
  }

  @OverrideOnDemand
  @Nonnull
  protected BigDecimal getAsBigDecimal (@Nonnull final String sCellText)
  {
    // Ensure that columns without text are sorted consistently compared to the
    // ones with non-numeric content
    if (sCellText.isEmpty ())
      return DEFAULT_VALUE;
    return LocaleFormatter.parseBigDecimal (sCellText, m_aParseLocale, DEFAULT_VALUE);
  }

  @Override
  protected int internalCompare (@Nonnull final String sText1, @Nonnull final String sText2)
  {
    final BigDecimal aBD1 = getAsBigDecimal (sText1);
    final BigDecimal aBD2 = getAsBigDecimal (sText2);
    return aBD1.compareTo (aBD2);
  }
}
