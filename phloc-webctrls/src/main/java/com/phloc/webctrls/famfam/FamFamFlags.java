/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webctrls.famfam;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.string.StringHelper;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCElement;

/**
 * A wrapper around the FamFam flags icon set
 * 
 * @author philip
 */
@Immutable
public final class FamFamFlags
{
  public static final ICSSClassProvider CSS_CLASS_FAMFAM_FLAG = EFamFamFlagIcon.CSS_CLASS_FAMFAM_FLAG;

  private FamFamFlags ()
  {}

  /**
   * Get the flag from the passed locale
   * 
   * @param aFlagLocale
   *        The locale to resolve. May be <code>null</code>.
   * @return <code>null</code> if the passed locale is <code>null</code>, if the
   *         locale has no country or if the no flag is present for the passed
   *         locale.
   */
  @Nullable
  public static EFamFamFlagIcon getFlagFromLocale (@Nullable final Locale aFlagLocale)
  {
    if (aFlagLocale != null)
    {
      final String sCountry = aFlagLocale.getCountry ();
      if (StringHelper.hasText (sCountry))
        return EFamFamFlagIcon.getFromIDOrNull (sCountry);
    }
    return null;
  }

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
                                                               @Nullable final Locale aFlagLocale)
  {
    if (aObject == null)
      throw new NullPointerException ("object");

    final EFamFamFlagIcon eIcon = getFlagFromLocale (aFlagLocale);
    if (eIcon != null)
      eIcon.applyToNode (aObject);
    return aObject;
  }
}
