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
package com.phloc.appbasics.app;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class manages the locales available in the application.
 * 
 * @author philip
 */
@NotThreadSafe
public class LocaleManager implements ILocaleManager
{
  private final Set <Locale> m_aLocales = new LinkedHashSet <Locale> ();
  private Locale m_aDefaultLocale;

  public LocaleManager ()
  {}

  @Nonnull
  public EChange registerLocale (@Nonnull final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ("locale");

    if (!m_aLocales.add (aLocale))
      return EChange.UNCHANGED;

    if (m_aLocales.size () == 1)
    {
      // If it is the first locale, automatically make it the default locale
      // until further notice!
      setDefaultLocale (aLocale);
    }
    return EChange.CHANGED;
  }

  @Nonnull
  public EChange setDefaultLocale (@Nonnull final Locale aDefaultLocale)
  {
    if (aDefaultLocale == null)
      throw new NullPointerException ("defaultLocale");
    if (!m_aLocales.contains (aDefaultLocale))
      throw new IllegalArgumentException ("The supposed default locale " +
                                          aDefaultLocale +
                                          " is not a valid application locale!");
    if (aDefaultLocale.equals (m_aDefaultLocale))
      return EChange.UNCHANGED;
    m_aDefaultLocale = aDefaultLocale;
    return EChange.UNCHANGED;
  }

  @Nullable
  public Locale getDefaultLocale ()
  {
    return m_aDefaultLocale;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <Locale> getAllAvailableLocales ()
  {
    return ContainerHelper.newList (m_aLocales);
  }

  public boolean isSupportedLocale (@Nullable final Locale aLocale)
  {
    return m_aLocales.contains (aLocale);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("locale", m_aLocales)
                                       .append ("defaultLocale", m_aDefaultLocale)
                                       .toString ();
  }
}
