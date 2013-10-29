package com.phloc.appbasics.security.user.password;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.messagedigest.EMessageDigestAlgorithm;
import com.phloc.commons.messagedigest.MessageDigestGeneratorHelper;
import com.phloc.commons.string.ToStringGenerator;

public final class PasswordHashCreatorDefault implements IPasswordHashCreator
{
  public static final String ALGORITHM = "default";

  /** Hashing algorithm to use for user passwords - never change it! */
  public static final EMessageDigestAlgorithm USER_PASSWORD_ALGO = EMessageDigestAlgorithm.SHA_512;

  @Nonnull
  @Nonempty
  public String getAlgorithmName ()
  {
    return ALGORITHM;
  }

  @Nonnull
  public String createPasswordHash (final String sPlainTextPassword)
  {
    if (sPlainTextPassword == null)
      throw new NullPointerException ("plainTextPassword");

    final byte [] aDigest = MessageDigestGeneratorHelper.getDigest (USER_PASSWORD_ALGO,
                                                                    sPlainTextPassword,
                                                                    CCharset.CHARSET_UTF_8_OBJ);
    return MessageDigestGeneratorHelper.getHexValueFromDigest (aDigest);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
