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

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.compare.CompareUtils;
import com.phloc.commons.format.IFormatter;
import com.phloc.commons.locale.LocaleFormatter;
import com.phloc.commons.string.ToStringGenerator;

public class ComparatorTableFloat extends AbstractComparatorTable
{
  protected final Locale m_aParseLocale;

  public ComparatorTableFloat (@Nonnull final Locale aParseLocale)
  {
    this (null, aParseLocale);
  }

  public ComparatorTableFloat (@Nullable final IFormatter aFormatter, @Nonnull final Locale aParseLocale)
  {
    super (aFormatter);
    m_aParseLocale = ValueEnforcer.notNull (aParseLocale, "ParseLocale");
  }

  @Nonnull
  public final Locale getParseLocale ()
  {
    return m_aParseLocale;
  }

  @OverrideOnDemand
  protected float getAsFloat (@Nonnull final String sCellText)
  {
    // Ensure that columns without text are sorted consistently compared to the
    // ones with non-numeric content
    if (sCellText.isEmpty ())
      return Float.MIN_VALUE;
    return LocaleFormatter.parseFloat (sCellText, m_aParseLocale, 0);
  }

  @Override
  protected final int internalCompare (@Nonnull final String sText1, @Nonnull final String sText2)
  {
    final float f1 = getAsFloat (sText1);
    final float f2 = getAsFloat (sText2);
    return CompareUtils.compare (f1, f2);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("parseLocale", m_aParseLocale).toString ();
  }
}
