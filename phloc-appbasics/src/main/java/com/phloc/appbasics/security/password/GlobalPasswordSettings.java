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

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.security.password.constraint.IPasswordConstraintList;
import com.phloc.appbasics.security.password.constraint.PasswordConstraintList;
import com.phloc.appbasics.security.password.hash.IPasswordHashCreator;
import com.phloc.appbasics.security.password.hash.IPasswordHashCreatorRegistrarSPI;
import com.phloc.appbasics.security.password.hash.PasswordHash;
import com.phloc.appbasics.security.password.hash.PasswordHashCreatorDefault;
import com.phloc.appbasics.security.password.hash.PasswordHashCreatorManager;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.lang.ServiceLoaderUtils;

/**
 * Central class for all password related elements.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class GlobalPasswordSettings
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (GlobalPasswordSettings.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  @GuardedBy ("s_aRWLock")
  private static IPasswordConstraintList s_aPasswordConstraintList = new PasswordConstraintList ();

  private static final PasswordHashCreatorManager s_aPHCMgr = new PasswordHashCreatorManager ();

  static
  {
    // Register default implementation so that something is present
    s_aPHCMgr.registerPasswordHashCreator (new PasswordHashCreatorDefault ());
    s_aPHCMgr.setDefaultPasswordHashCreatorAlgorithm (PasswordHashCreatorDefault.ALGORITHM);

    // Register all custom SPI implementations
    for (final IPasswordHashCreatorRegistrarSPI aSPI : ServiceLoaderUtils.getAllSPIImplementations (IPasswordHashCreatorRegistrarSPI.class))
      aSPI.registerPasswordHashCreators (s_aPHCMgr);
  }

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final GlobalPasswordSettings s_aInstance = new GlobalPasswordSettings ();

  private GlobalPasswordSettings ()
  {}

  /**
   * @return The current password constraint list. Never <code>null</code> but
   *         maybe empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  public static IPasswordConstraintList getPasswordConstraintList ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aPasswordConstraintList.getClone ();
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Set the global password constraint list.
   * 
   * @param aPasswordConstraintList
   *        The list to be set. May not be <code>null</code>.
   */
  public static void setPasswordConstraintList (@Nonnull final IPasswordConstraintList aPasswordConstraintList)
  {
    if (aPasswordConstraintList == null)
      throw new NullPointerException ("passwordConstraintList");

    // Create a copy
    final IPasswordConstraintList aRealPasswordConstraints = aPasswordConstraintList.getClone ();
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aPasswordConstraintList = aRealPasswordConstraints;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
    s_aLogger.info ("Set global password constraints to " + aRealPasswordConstraints);
  }

  /**
   * @return <code>true</code> if any password constraint is defined,
   *         <code>false</code> if not.
   */
  public static boolean isAnyPasswordConstraintDefined ()
  {
    return getPasswordConstraintList ().hasConstraints ();
  }

  /**
   * @return The central {@link PasswordHashCreatorManager}.
   */
  @Nonnull
  public static PasswordHashCreatorManager getPasswordHashCreatorManager ()
  {
    return s_aPHCMgr;
  }

  /**
   * Create the password hash from the passed plain text password, using the
   * default password hash creator.
   * 
   * @param sPlainTextPassword
   *        Plain text password. May not be <code>null</code>.
   * @return The password hash. Never <code>null</code>.
   */
  @Nonnull
  public static PasswordHash createUserDefaultPasswordHash (@Nonnull final String sPlainTextPassword)
  {
    if (sPlainTextPassword == null)
      throw new NullPointerException ("plainTextPassword");

    final IPasswordHashCreator aPHC = s_aPHCMgr.getDefaultPasswordHashCreator ();
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
   */
  @Nonnull
  public static PasswordHash createUserPasswordHash (@Nonnull @Nonempty final String sAlgorithmName,
                                                     @Nonnull final String sPlainTextPassword)
  {
    if (sPlainTextPassword == null)
      throw new NullPointerException ("plainTextPassword");

    final IPasswordHashCreator aPHC = s_aPHCMgr.getPasswordHashCreatorOfAlgorithm (sAlgorithmName);
    if (aPHC == null)
      throw new IllegalArgumentException ("No password hash creator for algorithm '" + sAlgorithmName + "' registered!");
    final String sPasswordHash = aPHC.createPasswordHash (sPlainTextPassword);
    return new PasswordHash (sAlgorithmName, sPasswordHash);
  }
}
