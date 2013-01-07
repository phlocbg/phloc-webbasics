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
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.EChange;

/**
 * Base class for all kind of string-object mapping container. This
 * implementation is a thread-safe wrapper around {@link FlagContainer}!
 * 
 * @author philip
 */
@ThreadSafe
public class FlagContainerThreadSafe extends FlagContainer
{
  protected final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();

  public FlagContainerThreadSafe ()
  {
    super ();
  }

  public FlagContainerThreadSafe (@Nonnull final Collection <String> aValues)
  {
    super (aValues);
  }

  public FlagContainerThreadSafe (@Nonnull final String... aValues)
  {
    super (aValues);
  }

  public FlagContainerThreadSafe (@Nonnull final IReadonlyFlagContainer aCont)
  {
    super (aCont);
  }

  @Override
  public boolean containsFlag (@Nullable final String sName)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return super.containsFlag (sName);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public Set <String> getAllFlags ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return super.getAllFlags ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  @Nonnull
  public EChange addFlag (@Nonnull final String sName)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      return super.addFlag (sName);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  @Nonnull
  public EChange removeFlag (@Nullable final String sName)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      return super.removeFlag (sName);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  @Nonnegative
  public int getFlagCount ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return super.getFlagCount ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  public boolean containsNoFlag ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return super.containsNoFlag ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  @Nonnull
  public EChange clear ()
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      return super.clear ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  public boolean equals (final Object o)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return super.equals (o);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  public int hashCode ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return super.hashCode ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  public String toString ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return super.toString ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }
}
