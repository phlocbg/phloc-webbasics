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

    return TipTip.create (HCUtils.list2divList (aTexts));
  }
}
