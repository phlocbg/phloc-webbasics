package com.phloc.appbasics.app;

import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.EChange;

public interface ILocaleManager
{
  /**
   * Register a new locale
   * 
   * @param aLocale
   *        The locale to be added. May not be <code>null</code>.
   * @return {@link EChange}.
   */
  @Nonnull
  EChange registerLocale (@Nonnull Locale aLocale);

  /**
   * Set the default locale. Must be one of the previously registred locales!
   * 
   * @param aDefaultLocale
   *        The locale to be used as the default. May not be <code>null</code>.
   * @return {@link EChange}
   */
  @Nonnull
  EChange setDefaultLocale (@Nonnull Locale aDefaultLocale);

  /**
   * @return The application default locale. May be <code>null</code> if non is
   *         defined
   */
  @Nullable
  Locale getDefaultLocale ();

  /**
   * @return All available locales for the application.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <Locale> getAllAvailableLocales ();

  /**
   * Check if the passed locale is a supported locale.
   * 
   * @param aLocale
   *        The locale to check
   * @return <code>true</code> if the passed locale is supported,
   *         <code>false</code> otherwise.
   */
  boolean isSupportedLocale (@Nullable Locale aLocale);
}
