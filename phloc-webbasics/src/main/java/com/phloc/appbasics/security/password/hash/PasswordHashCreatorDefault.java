/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.appbasics.security.password.hash;

import javax.annotation.Nonnull;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.messagedigest.EMessageDigestAlgorithm;
import com.phloc.commons.messagedigest.MessageDigestGeneratorHelper;

/**
 * The default implementation of {@link IPasswordHashCreator}.
 * 
 * @author Philip Helger
 */
public final class PasswordHashCreatorDefault extends AbstractPasswordHashCreator
{
  public static final String ALGORITHM = "default";

  /** Hashing algorithm to use for user passwords - never change it! */
  @Nonnull
  public static final EMessageDigestAlgorithm USER_PASSWORD_ALGO = EMessageDigestAlgorithm.SHA_512;

  public PasswordHashCreatorDefault ()
  {
    super (ALGORITHM);
  }

  @Nonnull
  public String createPasswordHash (@Nonnull final String sPlainTextPassword)
  {
    ValueEnforcer.notNull (sPlainTextPassword, "PlainTextPassword");

    final byte [] aDigest = MessageDigestGeneratorHelper.getDigest (USER_PASSWORD_ALGO,
                                                                    sPlainTextPassword,
                                                                    CCharset.CHARSET_UTF_8_OBJ);
    return MessageDigestGeneratorHelper.getHexValueFromDigest (aDigest);
  }
}
