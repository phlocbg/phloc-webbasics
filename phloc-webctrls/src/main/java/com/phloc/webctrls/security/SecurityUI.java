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
package com.phloc.webctrls.security;

import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.security.AccessManager;
import com.phloc.appbasics.security.user.IUser;
import com.phloc.appbasics.security.user.password.PasswordUtils;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.htmlext.HCUtils;
import com.phloc.webctrls.tiptip.TipTip;

public final class SecurityUI
{
  private SecurityUI ()
  {}

  @Nullable
  public static String getUserDisplayName (@Nullable final String sUserID)
  {
    if (StringHelper.hasNoText (sUserID))
      return "Gast";
    final IUser aUser = AccessManager.getInstance ().getUserOfID (sUserID);
    return aUser == null ? sUserID : aUser.getDisplayName ();
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
    final List <String> aTexts = PasswordUtils.getPasswordConstraints ()
                                              .getAllPasswordConstraintDescriptions (aDisplayLocale);
    if (aTexts.isEmpty ())
      return null;

    return TipTip.create (HCUtils.list2divList (aTexts)).build ();
  }
}
