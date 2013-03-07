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
package com.phloc.appbasics.security.user.password;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.security.CSecurity;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.messagedigest.MessageDigestGeneratorHelper;

@ThreadSafe
public final class PasswordUtils
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (PasswordUtils.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  private static IPasswordConstraints s_aPasswordConstraints = new PasswordConstraints ();

  private PasswordUtils ()
  {}

  @Nonnull
  public static IPasswordConstraints getPasswordConstraints ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aPasswordConstraints;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  public static void setPasswordConstraints (@Nonnull final IPasswordConstraints aPasswordConstraints)
  {
    if (aPasswordConstraints == null)
      throw new NullPointerException ("passwordConstraints");

    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aPasswordConstraints = aPasswordConstraints;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
    s_aLogger.info ("Set global password constraints to " + aPasswordConstraints);
  }

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
                                                                    CCharset.CHARSET_UTF_8_OBJ);
    return MessageDigestGeneratorHelper.getHexValueFromDigest (aDigest);
  }
}
