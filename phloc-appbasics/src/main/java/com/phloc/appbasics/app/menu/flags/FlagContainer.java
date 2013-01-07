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
package com.phloc.appbasics.app.menu.flags;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * The not-thread safe implementation of {@link IFlagContainer}.
 * 
 * @author philip
 */
@NotThreadSafe
public class FlagContainer implements IFlagContainer
{
  /**
   * flag storage.
   */
  private final Set <String> m_aFlags = new HashSet <String> ();

  public FlagContainer ()
  {}

  public FlagContainer (@Nonnull final Collection <String> aValues)
  {
    if (aValues == null)
      throw new NullPointerException ("Values");
    m_aFlags.addAll (aValues);
  }

  public FlagContainer (@Nonnull final String... aValues)
  {
    if (aValues == null)
      throw new NullPointerException ("Values");
    for (final String sValue : aValues)
      m_aFlags.add (sValue);
  }

  public FlagContainer (@Nonnull final IReadonlyFlagContainer aCont)
  {
    if (aCont == null)
      throw new NullPointerException ("cont");
    m_aFlags.addAll (aCont.getAllFlags ());
  }

  public boolean containsFlag (@Nullable final String sName)
  {
    return sName != null && m_aFlags.contains (sName);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <String> getAllFlags ()
  {
    return ContainerHelper.newSet (m_aFlags);
  }

  @Nonnull
  public EChange addFlag (@Nonnull final String sName)
  {
    if (sName == null)
      throw new NullPointerException ("name");

    return EChange.valueOf (m_aFlags.add (sName));
  }

  @Nonnull
  public final EChange addFlags (@Nullable final Collection <String> aValues)
  {
    EChange ret = EChange.UNCHANGED;
    if (aValues != null)
      for (final String sName : aValues)
        ret = ret.or (addFlag (sName));
    return ret;
  }

  @Nonnull
  public final EChange addFlags (@Nullable final String... aValues)
  {
    EChange ret = EChange.UNCHANGED;
    if (aValues != null)
      for (final String sName : aValues)
        ret = ret.or (addFlag (sName));
    return ret;
  }

  @Nonnull
  public EChange removeFlag (@Nullable final String sName)
  {
    return EChange.valueOf (sName != null && m_aFlags.remove (sName));
  }

  @Nonnegative
  public int getFlagCount ()
  {
    return m_aFlags.size ();
  }

  public boolean containsNoFlag ()
  {
    return m_aFlags.isEmpty ();
  }

  @Nonnull
  public EChange clear ()
  {
    if (m_aFlags.isEmpty ())
      return EChange.UNCHANGED;
    m_aFlags.clear ();
    return EChange.CHANGED;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final FlagContainer rhs = (FlagContainer) o;
    return m_aFlags.equals (rhs.m_aFlags);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aFlags).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("flags", m_aFlags).toString ();
  }
}
