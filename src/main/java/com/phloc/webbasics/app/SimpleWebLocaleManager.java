package com.phloc.webbasics.app;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.EChange;

/**
 * This class manages the locales available in the application.
 * 
 * @author philip
 */
@NotThreadSafe
public final class SimpleWebLocaleManager
{
  private static final Set <Locale> s_aAppLocales = new LinkedHashSet <Locale> ();
  private static Locale s_aDefaultLocale;

  private SimpleWebLocaleManager ()
  {}

  @Nonnull
  public static EChange registerLocale (@Nonnull final Locale aLocale)
  {
    if (aLocale == null)
      throw new NullPointerException ("locale");
    return EChange.valueOf (s_aAppLocales.add (aLocale));
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
   * @return The application default locale.
   */
  @Nonnull
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
    return new ArrayList <Locale> (s_aAppLocales);
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
