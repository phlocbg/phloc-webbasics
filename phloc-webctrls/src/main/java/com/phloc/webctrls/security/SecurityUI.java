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
package com.phloc.webctrls.security;

import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.security.AccessManager;
import com.phloc.appbasics.security.password.GlobalPasswordSettings;
import com.phloc.appbasics.security.user.IUser;
import com.phloc.appbasics.security.util.SecurityUtils;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.htmlext.HCUtils;
import com.phloc.webctrls.tiptip.TipTip;

public final class SecurityUI
{
  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final SecurityUI s_aInstance = new SecurityUI ();

  private SecurityUI ()
  {}

  @Nullable
  @Deprecated
  public static String getUserDisplayName (@Nullable final String sUserID)
  {
    if (StringHelper.hasNoText (sUserID))
      return "Gast";
    final IUser aUser = AccessManager.getInstance ().getUserOfID (sUserID);
    return aUser == null ? sUserID : getUserDisplayName (aUser, Locale.getDefault ());
  }

  /**
   * Get the display name of the guest user in the specified locale.
   *
   * @param aDisplayLocale
   *        The locale to be used. May not be <code>null</code>.
   * @return <code>null</code> if no translation is present.
   */
  @Nullable
  @Deprecated
  public static String getGuestUserDisplayName (@Nonnull final Locale aDisplayLocale)
  {
    return SecurityUtils.getGuestUserDisplayName (aDisplayLocale);
  }

  /**
   * Get the display name of the user.
   *
   * @param sUserID
   *        User ID. May be <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to be used.
   * @return The "guest" text if no user ID was provided, the display name of
   *         the user if a valid user ID was provided or the ID of the user if
   *         an invalid user was provided.
   */
  @Nullable
  @Deprecated
  public static String getUserDisplayName (@Nullable final String sUserID, @Nonnull final Locale aDisplayLocale)
  {
    return SecurityUtils.getUserDisplayName (sUserID, aDisplayLocale);
  }

  /**
   * Get the display name of the user. If no display name is present (because
   * first name and last name are empty), the login name is returned.
   *
   * @param aUser
   *        User. May be <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to be used to resolve the "Guest" text if the
   *        passed user is <code>null</code>.
   * @return Never <code>null</code>. Either the display name or the login name
   *         of the user.
   */
  @Nullable
  @Deprecated
  public static String getUserDisplayName (@Nullable final IUser aUser, @Nonnull final Locale aDisplayLocale)
  {
    return SecurityUtils.getUserDisplayName (aUser, aDisplayLocale);
  }

  /**
   * Create a tooltip with all the requirements for a password
   *
   * @param aDisplayLocale
   *        Display locale to use.
   * @return <code>null</code> if not special constraints are defined.
   */
  @Nullable
  public static IHCNode createPasswordConstraintTip (@Nonnull final Locale aDisplayLocale)
  {
    final List <String> aTexts = GlobalPasswordSettings.getPasswordConstraintList ()
        .getAllPasswordConstraintDescriptions (aDisplayLocale);
    if (aTexts.isEmpty ())
      return null;

    return TipTip.create (HCUtils.list2divList (aTexts));
  }
}
