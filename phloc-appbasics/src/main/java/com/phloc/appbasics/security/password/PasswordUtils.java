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
package com.phloc.appbasics.security.password;

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

import com.phloc.appbasics.security.password.constraint.IPasswordConstraintList;
import com.phloc.appbasics.security.password.constraint.PasswordConstraintList;
import com.phloc.appbasics.security.password.hash.IPasswordHashCreator;
import com.phloc.appbasics.security.password.hash.PasswordHash;
import com.phloc.appbasics.security.password.hash.PasswordHashCreatorDefault;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;

@ThreadSafe
public final class PasswordUtils
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (PasswordUtils.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  @GuardedBy ("s_aRWLock")
  private static IPasswordConstraintList s_aPasswordConstraints = new PasswordConstraintList ();

  @GuardedBy ("s_aRWLock")
  private static final Map <String, IPasswordHashCreator> s_aPasswordHashCreators = new HashMap <String, IPasswordHashCreator> ();
  @GuardedBy ("s_aRWLock")
  private static IPasswordHashCreator s_aDefaultPasswordHashCreator;

  static
  {
    // Always register the old, default creator
    registerPasswordHashCreator (new PasswordHashCreatorDefault ());
    setDefaultPasswordHashCreatorAlgorithm (PasswordHashCreatorDefault.ALGORITHM);
  }

  private PasswordUtils ()
  {}

  @Nonnull
  public static IPasswordConstraintList getPasswordConstraints ()
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

  public static void setPasswordConstraints (@Nonnull final IPasswordConstraintList aPasswordConstraints)
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
    s_aLogger.info ("Registered password hash creator algorithm '" + sAlgorithmName + "' to " + aPasswordHashCreator);
  }

  /**
   * Get the password hash creator of the specified algorithm name.
   * 
   * @param sAlgorithmName
   *        The algorithm name to query. May be <code>null</code>.
   * @return <code>null</code> if no such hash creator is registered.
   */
  @Nullable
  public static IPasswordHashCreator getPasswordHashCreatorOfAlgorithm (@Nullable final String sAlgorithmName)
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
   * Set the default password hash creator algorithm. A matching
   * {@link IPasswordHashCreator} object must be registered previously using
   * {@link #registerPasswordHashCreator(IPasswordHashCreator)}.
   * 
   * @param sAlgorithm
   *        The name of the algorithm to use as the default. May neither be
   *        <code>null</code> nor empty.
   */
  public static void setDefaultPasswordHashCreatorAlgorithm (@Nonnull @Nonempty final String sAlgorithm)
  {
    if (StringHelper.hasNoText (sAlgorithm))
      throw new IllegalArgumentException ("algorithm");

    s_aRWLock.writeLock ().lock ();
    try
    {
      final IPasswordHashCreator aPHC = s_aPasswordHashCreators.get (sAlgorithm);
      if (aPHC == null)
        throw new IllegalArgumentException ("No PasswordHashCreator registered for algorithm '" + sAlgorithm + "'");
      s_aDefaultPasswordHashCreator = aPHC;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
    s_aLogger.info ("Default PasswordHashCreator algorithm set to '" + sAlgorithm + "'");
  }

  /**
   * @return The default {@link IPasswordHashCreator} algorithm to use. Never
   *         <code>null</code>.
   */
  @Nonnull
  public static IPasswordHashCreator getDefaultPasswordHashCreator ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      final IPasswordHashCreator ret = s_aDefaultPasswordHashCreator;
      if (ret == null)
        throw new IllegalStateException ("No default PasswordHashCreator present!");
      return ret;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return The default password hash creator algorithm name currently in use.
   */
  @Nonnull
  public static String getDefaultPasswordHashCreatorAlgorithm ()
  {
    return getDefaultPasswordHashCreator ().getAlgorithmName ();
  }

  /**
   * Create the password hash from the passed plain text password, using the
   * default password hash creator.
   * 
   * @param sPlainTextPassword
   *        Plain text password. May not be <code>null</code>.
   * @return The password hash. Never <code>null</code>.
   * @see #getDefaultPasswordHashCreator()
   */
  @Nonnull
  public static PasswordHash createUserDefaultPasswordHash (@Nonnull final String sPlainTextPassword)
  {
    if (sPlainTextPassword == null)
      throw new NullPointerException ("plainTextPassword");

    final IPasswordHashCreator aPHC = getDefaultPasswordHashCreator ();
    final String sPasswordHash = aPHC.createPasswordHash (sPlainTextPassword);
    return new PasswordHash (aPHC.getAlgorithmName (), sPasswordHash);
  }

  /**
   * Create the password hash from the passed plain text password, using the
   * default password hash creator.
   * 
   * @param sAlgorithmName
   *        The password hash creator algorithm name to query. May neither be
   *        <code>null</code> nor empty.
   * @param sPlainTextPassword
   *        Plain text password. May not be <code>null</code>.
   * @return The password hash. Never <code>null</code>.
   * @see #getDefaultPasswordHashCreator()
   */
  @Nonnull
  public static PasswordHash createUserPasswordHash (@Nonnull @Nonempty final String sAlgorithmName,
                                                     @Nonnull final String sPlainTextPassword)
  {
    if (sPlainTextPassword == null)
      throw new NullPointerException ("plainTextPassword");

    final IPasswordHashCreator aPHC = getPasswordHashCreatorOfAlgorithm (sAlgorithmName);
    if (aPHC == null)
      throw new IllegalArgumentException ("No password hash creator for algorithm '" + sAlgorithmName + "' registered!");
    final String sPasswordHash = aPHC.createPasswordHash (sPlainTextPassword);
    return new PasswordHash (sAlgorithmName, sPasswordHash);
  }
}
