/**
 * Copyright (C) 2006-2014 phloc systems
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

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
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
    ValueEnforcer.notNull (aOther, "Other");
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

  /**
   * Added a password constraint.
   * 
   * @param aPasswordConstraint
   *        The constraint to be added. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public PasswordConstraintList addConstraint (@Nonnull final IPasswordConstraint aPasswordConstraint)
  {
    ValueEnforcer.notNull (aPasswordConstraint, "PasswordConstraint");
    m_aConstraints.add (aPasswordConstraint);
    return this;
  }

  /**
   * Remove the passed constraint.
   * 
   * @param aPasswordConstraint
   *        The constraint to be removed. May be <code>null</code>.
   * @return {@link EChange}.
   */
  @Nonnull
  public EChange removeConstraint (@Nullable final IPasswordConstraint aPasswordConstraint)
  {
    if (aPasswordConstraint == null)
      return EChange.UNCHANGED;
    return EChange.valueOf (m_aConstraints.remove (aPasswordConstraint));
  }

  public boolean isPasswordValid (@Nullable final String sPlainTextPassword)
  {
    for (final IPasswordConstraint aPasswordConstraint : m_aConstraints)
      if (!aPasswordConstraint.isPasswordValid (sPlainTextPassword))
        return false;
    return true;
  }

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
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final PasswordConstraintList rhs = (PasswordConstraintList) o;
    return m_aConstraints.equals (rhs.m_aConstraints);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aConstraints).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("constraints", m_aConstraints).toString ();
  }
}
