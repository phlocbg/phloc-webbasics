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
package com.phloc.appbasics.security.password.constraint;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * The default implementation of {@link IPasswordConstraintList}.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class PasswordConstraintList implements IPasswordConstraintList
{
  private final List <IPasswordConstraint> m_aConstraints = new ArrayList <IPasswordConstraint> ();

  public PasswordConstraintList ()
  {}

  public PasswordConstraintList (@Nonnull final IPasswordConstraintList aOther)
  {
    if (aOther == null)
      throw new NullPointerException ("other");
    m_aConstraints.addAll (aOther.getAllPasswordConstraints ());
  }

  public PasswordConstraintList (@Nullable final IPasswordConstraint... aPasswordConstraints)
  {
    if (aPasswordConstraints != null)
      for (final IPasswordConstraint aPasswordConstraint : aPasswordConstraints)
        addConstraint (aPasswordConstraint);
  }

  public PasswordConstraintList (@Nullable final Iterable <? extends IPasswordConstraint> aPasswordConstraints)
  {
    if (aPasswordConstraints != null)
      for (final IPasswordConstraint aPasswordConstraint : aPasswordConstraints)
        addConstraint (aPasswordConstraint);
  }

  public boolean hasConstraints ()
  {
    return !m_aConstraints.isEmpty ();
  }

  @Nonnegative
  public int getConstraintCount ()
  {
    return m_aConstraints.size ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IPasswordConstraint> getAllPasswordConstraints ()
  {
    return ContainerHelper.newList (m_aConstraints);
  }

  @Nonnull
  public IPasswordConstraintList addConstraint (@Nonnull final IPasswordConstraint aPasswordConstraint)
  {
    if (aPasswordConstraint == null)
      throw new NullPointerException ("passwordConstraint");
    m_aConstraints.add (aPasswordConstraint);
    return this;
  }

  /**
   * Check if the passed password is valid. Breaks after the first unfulfilled
   * constrained
   * 
   * @param sPlainTextPassword
   *        The password to check. May be <code>null</code>.
   * @return <code>true</code> if no constraint was unfulfilled (meaning that if
   *         no constrained is defined, every password is valid).
   */
  public boolean isPasswordValid (@Nullable final String sPlainTextPassword)
  {
    for (final IPasswordConstraint aPasswordConstraint : m_aConstraints)
      if (!aPasswordConstraint.isPasswordValid (sPlainTextPassword))
        return false;
    return true;
  }

  /**
   * Check if the passed password is valid. The descriptions of all failed
   * constraints are returned.
   * 
   * @param sPlainTextPassword
   *        The password to check. May be <code>null</code>.
   * @param aContentLocale
   *        The content locale to be used to determine the descriptions.
   * @return A non-<code>null</code> but empty list if no constraint was
   *         unfulfilled (meaning that if no constrained is defined, every
   *         password is valid). If the returned list is not empty than the
   *         password is invalid.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <String> getInvalidPasswordDescriptions (@Nullable final String sPlainTextPassword,
                                                       @Nonnull final Locale aContentLocale)
  {
    final List <String> ret = new ArrayList <String> ();
    for (final IPasswordConstraint aPasswordConstraint : m_aConstraints)
      if (!aPasswordConstraint.isPasswordValid (sPlainTextPassword))
        ret.add (aPasswordConstraint.getDescription (aContentLocale));
    return ret;
  }

  /**
   * Get a list of all password constraint descriptions in the specified locale
   * (e.g. for hinting a user)
   * 
   * @param aContentLocale
   *        The locale to be used for text resolving.
   * @return A non-<code>null</code> list with all constraint descriptions. If
   *         the returned list is empty, it means that no constraint is defined.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllPasswordConstraintDescriptions (@Nonnull final Locale aContentLocale)
  {
    final List <String> ret = new ArrayList <String> ();
    for (final IPasswordConstraint aPasswordConstraint : m_aConstraints)
      ret.add (aPasswordConstraint.getDescription (aContentLocale));
    return ret;
  }

  @Nonnull
  public PasswordConstraintList getClone ()
  {
    return new PasswordConstraintList (this);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("constraints", m_aConstraints).toString ();
  }
}
