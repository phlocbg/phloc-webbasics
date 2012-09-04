package com.phloc.webctrls.famfam;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.string.StringHelper;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCElement;

/**
 * A wrapper around the FamFam flags icon set
 * 
 * @author philip
 */
public final class FamFamFlags
{
  private static final ICSSClassProvider CSS_CLASS_FAMFAM_FLAG = DefaultCSSClassProvider.create ("famfam-flag");

  private FamFamFlags ()
  {}

  /**
   * Add a specific CSS class, that adds a padding and a background-image with a
   * flag to the passed object
   * 
   * @param <T>
   *        Type of object
   * @param aObject
   *        The source object. May not be <code>null</code>
   * @param aFlagLocale
   *        The locale who's flag is to be applied. Is only effective if the
   *        locale contains a country element. May not be <code>null</code>
   * @return The source object
   */
  @Nonnull
  public static <T extends IHCElement <?>> T getWithFlagImage (@Nonnull final T aObject,
                                                               @Nonnull final Locale aFlagLocale)
  {
    if (aObject == null)
      throw new NullPointerException ("object");
    if (aFlagLocale == null)
      throw new NullPointerException ("flagLocale");

    final String sCountry = aFlagLocale.getCountry ();
    if (StringHelper.hasText (sCountry))
    {
      // Note: dynamic CSS class is OK here
      aObject.addClasses (CSS_CLASS_FAMFAM_FLAG,
                          DefaultCSSClassProvider.create ("famfam-flag-" + sCountry.toLowerCase ()));
    }
    return aObject;
  }
}
