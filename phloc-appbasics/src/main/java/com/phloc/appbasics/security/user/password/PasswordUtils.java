package com.phloc.appbasics.security.user.password;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.appbasics.security.CSecurity;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.messagedigest.MessageDigestGeneratorHelper;

@Immutable
public final class PasswordUtils
{
  private PasswordUtils ()
  {}

  /**
   * The one and only method to create a message digest hash from a password.
   * 
   * @param sPlainTextPassword
   *        Plain text password
   * @return The String representation of the password hash
   */
  @Nonnull
  @Nonempty
  public static String createUserPasswordHash (@Nonnull final String sPlainTextPassword)
  {
    if (sPlainTextPassword == null)
      throw new NullPointerException ("plainTextPassword");
  
    final byte [] aDigest = MessageDigestGeneratorHelper.getDigest (CSecurity.USER_PASSWORD_ALGO,
                                                                    sPlainTextPassword,
                                                                    CCharset.CHARSET_UTF_8);
    return MessageDigestGeneratorHelper.getHexValueFromDigest (aDigest);
  }
}
