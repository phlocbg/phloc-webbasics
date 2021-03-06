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
package com.phloc.webctrls.datatables.comparator;

import java.math.BigDecimal;
import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.masterdata.currency.ECurrency;

/**
 * A cell comparator that handles value formatted currency cells that all use
 * the same currency.
 * 
 * @author Philip Helger
 */
public class ComparatorTableFixedCurrencyValueFormat extends ComparatorTableBigDecimal
{
  private final ECurrency m_eCurrency;

  public ComparatorTableFixedCurrencyValueFormat (@Nonnull final Locale aLocale, @Nonnull final ECurrency eCurrency)
  {
    super (aLocale);
    m_eCurrency = ValueEnforcer.notNull (eCurrency, "Currency");
  }

  @Nonnull
  public final ECurrency getCurrency ()
  {
    return m_eCurrency;
  }

  @Override
  @Nonnull
  protected BigDecimal getAsBigDecimal (@Nonnull final String sCellText)
  {
    return m_eCurrency.parseValueFormat (sCellText, DEFAULT_VALUE);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("currency", m_eCurrency).toString ();
  }
}
