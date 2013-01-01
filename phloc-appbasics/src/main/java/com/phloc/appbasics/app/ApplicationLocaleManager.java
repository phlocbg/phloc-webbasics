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

/**
 * This class manages the locales available in the application.
 * 
 * @author philip
 */
@NotThreadSafe
public final class ApplicationLocaleManager
{
  private static final Set <Locale> s_aAppLocales = new LinkedHashSet <Locale> ();
  private static Locale s_aDefaultLocale;

  private ApplicationLocaleManager ()
  {}

  @Nonnull
  public static EChange registerLocale (@Nonnull final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ("locale");

    if (!s_aAppLocales.add (aLocale))
      return EChange.UNCHANGED;

    if (s_aAppLocales.size () == 1)
    {
      // If it is the first locale, automatically make it the default locale
      // until further notice!
      setDefaultLocale (aLocale);
    }
    return EChange.CHANGED;
  }

  @Nonnull
  public static EChange setDefaultLocale (@Nonnull final Locale aDefaultLocale)
  {
    if (aDefaultLocale == null)
      throw new NullPointerException ("defaultLocale");
    if (!s_aAppLocales.contains (aDefaultLocale))
      throw new IllegalArgumentException ("The supposed default locale " +
                                          aDefaultLocale +
                                          " is not a valid application locale!");
    if (aDefaultLocale.equals (s_aDefaultLocale))
      return EChange.UNCHANGED;
    s_aDefaultLocale = aDefaultLocale;
    return EChange.UNCHANGED;
  }

  /**
   * @return The application default locale. May be <code>null</code> if non is
   *         defined
   */
  @Nullable
  public static Locale getDefaultLocale ()
  {
    return s_aDefaultLocale;
  }

  /**
   * @return All available locales for the application.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static List <Locale> getAllAvailableLocales ()
  {
    return ContainerHelper.newList (s_aAppLocales);
  }

  /**
   * Check if the passed locale is a supported locale.
   * 
   * @param aLocale
   *        The locale to check
   * @return <code>true</code> if the passed locale is supported,
   *         <code>false</code> otherwise.
   */
  public static boolean isSupportedLocale (@Nullable final Locale aLocale)
  {
    return s_aAppLocales.contains (aLocale);
  }
}
