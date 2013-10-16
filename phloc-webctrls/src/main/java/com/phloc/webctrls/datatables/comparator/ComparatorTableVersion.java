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

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.format.IFormatter;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.version.Version;

/**
 * This comparator is responsible for sorting cells by {@link Version}
 * 
 * @author Philip Helger
 */
public class ComparatorTableVersion extends AbstractComparatorTable
{
  protected final Locale m_aParseLocale;

  public ComparatorTableVersion (@Nonnull final Locale aParseLocale)
  {
    this (null, aParseLocale);
  }

  public ComparatorTableVersion (@Nullable final IFormatter aFormatter, @Nonnull final Locale aParseLocale)
  {
    super (aFormatter);
    if (aParseLocale == null)
      throw new NullPointerException ("locale");
    m_aParseLocale = aParseLocale;
  }

  @Nonnull
  public final Locale getParseLocale ()
  {
    return m_aParseLocale;
  }

  @OverrideOnDemand
  protected Version getAsVersion (@Nonnull final String sCellText)
  {
    return new Version (sCellText);
  }

  @Override
  protected final int internalCompare (@Nonnull final String sText1, @Nonnull final String sText2)
  {
    final Version aVer1 = getAsVersion (sText1);
    final Version aVer2 = getAsVersion (sText2);
    return aVer1.compareTo (aVer2);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("parseLocale", m_aParseLocale).toString ();
  }
}
