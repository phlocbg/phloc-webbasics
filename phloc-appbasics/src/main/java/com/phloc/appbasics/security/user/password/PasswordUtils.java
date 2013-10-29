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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;

@ThreadSafe
public final class PasswordUtils
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (PasswordUtils.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  @GuardedBy ("s_aRWLock")
  private static IPasswordConstraints s_aPasswordConstraints = new PasswordConstraints ();
  @GuardedBy ("s_aRWLock")
  private static Map <String, IPasswordHashCreator> s_aPasswordHashCreators = new HashMap <String, IPasswordHashCreator> ();

  static
  {
    // Always register the old, default creator
    registerPasswordHashCreator (new PasswordHashCreatorDefault ());
  }

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
   * @return <code>true</code> if any password constraint is defined,
   *         <code>false</code> if not.
   */
  public static boolean isAnyPasswordConstraintDefined ()
  {
    return getPasswordConstraints ().hasConstraints ();
  }

  /**
   * Register a new password hash creator. No other password hash creator with
   * the same algorithm name may be registered.
   * 
   * @param aPasswordHashCreator
   *        The password hash creator to be registered. May not be
   *        <code>null</code>.
   */
  public static void registerPasswordHashCreator (@Nonnull final IPasswordHashCreator aPasswordHashCreator)
  {
    if (aPasswordHashCreator == null)
      throw new NullPointerException ("PasswordHashCreator");

    final String sAlgorithmName = aPasswordHashCreator.getAlgorithmName ();
    if (StringHelper.hasNoText (sAlgorithmName))
      throw new IllegalArgumentException ("PasswordHashCreator algorithm '" +
                                          sAlgorithmName +
                                          "' is already registered!");

    s_aRWLock.writeLock ().lock ();
    try
    {
      if (s_aPasswordHashCreators.containsKey (sAlgorithmName))
        throw new IllegalArgumentException ("Another PasswordHashCreator for algorithm '" +
                                            sAlgorithmName +
                                            "' is already registered!");
      s_aPasswordHashCreators.put (sAlgorithmName, aPasswordHashCreator);
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Get the password hash creator of the specified algorithm name.
   * 
   * @param sAlgorithmName
   *        The algorithm name to query. May be <code>null</code>.
   * @return <code>null</code> if no such hash creator is registered.
   */
  @Nullable
  public static IPasswordHashCreator getPaswordHashCreatorOfAlgorithm (@Nullable final String sAlgorithmName)
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aPasswordHashCreators.get (sAlgorithmName);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
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

    return getPaswordHashCreatorOfAlgorithm (PasswordHashCreatorDefault.ALGORITHM).createPasswordHash (sPlainTextPassword);
  }
}
