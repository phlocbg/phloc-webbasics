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
package com.phloc.webctrls.custom.table;

import java.math.BigDecimal;
import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.masterdata.currency.ECurrency;

/**
 * A cell comparator that handles value formatted currency cells that all use
 * the same currency.
 * 
 * @author Philip Helger
 */
public final class ComparatorCellFixedCurrencyValueFormat extends ComparatorCellDouble
{
  private final ECurrency m_eCurrency;

  public ComparatorCellFixedCurrencyValueFormat (@Nonnull final Locale aLocale, @Nonnull final ECurrency eCurrency)
  {
    super (aLocale);
    if (eCurrency == null)
      throw new NullPointerException ("currency");
    m_eCurrency = eCurrency;
  }

  @Override
  protected double asDouble (final String sCellText)
  {
    return m_eCurrency.parseValueFormat (sCellText, BigDecimal.ZERO).doubleValue ();
  }
}
