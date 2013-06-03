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
package com.phloc.appbasics.app.dao.impl;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

import com.phloc.appbasics.app.dao.IDAO;
import com.phloc.appbasics.app.dao.IDAOIO;
import com.phloc.appbasics.app.dao.IDAOReadExceptionHandler;
import com.phloc.appbasics.app.dao.IDAOWriteExceptionHandler;
import com.phloc.commons.annotations.MustBeLocked;
import com.phloc.commons.annotations.MustBeLocked.ELockType;
import com.phloc.commons.callback.AdapterRunnableToCallable;
import com.phloc.commons.callback.INonThrowingCallable;
import com.phloc.commons.callback.INonThrowingRunnable;
import com.phloc.commons.collections.NonBlockingStack;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Base implementation of {@link IDAO}
 * 
 * @author Philip Helger
 */
public abstract class AbstractDAO implements IDAO
{
  /** By default auto-save is enabled */
  public static final boolean DEFAULT_AUTO_SAVE_ENABLED = true;

  protected static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  @GuardedBy ("s_aRWLock")
  private static IDAOReadExceptionHandler s_aExceptionHandlerRead;
  @GuardedBy ("s_aRWLock")
  private static IDAOWriteExceptionHandler s_aExceptionHandlerWrite;

  protected final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final IDAOIO m_aIO;

  @GuardedBy ("m_aRWLock")
  private final NonBlockingStack <Boolean> m_aAutoSaveStack = new NonBlockingStack <Boolean> ();
  @GuardedBy ("m_aRWLock")
  private boolean m_bPendingChanges = false;
  @GuardedBy ("m_aRWLock")
  private boolean m_bAutoSaveEnabled = DEFAULT_AUTO_SAVE_ENABLED;

  protected AbstractDAO (@Nonnull final IDAOIO aDAOIO)
  {
    if (aDAOIO == null)
      throw new NullPointerException ("No data IO passed");
    m_aIO = aDAOIO;
  }

  /**
   * Set a custom exception handler that is called in case reading a file fails.
   * 
   * @param aExceptionHandler
   *        The exception handler to be set. May be <code>null</code> to
   *        indicate no custom exception handler.
   */
  public static final void setCustomExceptionHandlerRead (@Nullable final IDAOReadExceptionHandler aExceptionHandler)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aExceptionHandlerRead = aExceptionHandler;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  public static final IDAOReadExceptionHandler getCustomExceptionHandlerRead ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aExceptionHandlerRead;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Set a custom exception handler that is called in case writing a file fails.
   * 
   * @param aExceptionHandler
   *        The exception handler to be set. May be <code>null</code> to
   *        indicate no custom exception handler.
   */
  public static final void setCustomExceptionHandlerWrite (@Nullable final IDAOWriteExceptionHandler aExceptionHandler)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aExceptionHandlerWrite = aExceptionHandler;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  public static final IDAOWriteExceptionHandler getCustomExceptionHandlerWrite ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aExceptionHandlerWrite;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  protected final IDAOIO getIO ()
  {
    return m_aIO;
  }

  /**
   * @return <code>true</code> if auto save is enabled, <code>false</code>
   *         otherwise.
   */
  @MustBeLocked (ELockType.READ)
  protected final boolean internalIsAutoSaveEnabled ()
  {
    return m_bAutoSaveEnabled;
  }

  /**
   * @return <code>true</code> if auto save is enabled, <code>false</code>
   *         otherwise.
   */
  public final boolean isAutoSaveEnabled ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_bAutoSaveEnabled;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @Deprecated
  public final EChange setAutoSaveEnabled (final boolean bAutoSaveEnabled)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_bAutoSaveEnabled == bAutoSaveEnabled)
        return EChange.UNCHANGED;
      m_bAutoSaveEnabled = bAutoSaveEnabled;
      return EChange.CHANGED;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @MustBeLocked (ELockType.WRITE)
  public final void internalSetPendingChanges (final boolean bPendingChanges)
  {
    m_bPendingChanges = bPendingChanges;
  }

  /**
   * @return <code>true</code> if unsaved changes are present
   */
  @MustBeLocked (ELockType.READ)
  public final boolean internalHasPendingChanges ()
  {
    return m_bPendingChanges;
  }

  /**
   * @return <code>true</code> if unsaved changes are present
   */
  public final boolean hasPendingChanges ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_bPendingChanges;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public final void beginWithoutAutoSave ()
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      // Save old auto save state
      m_aAutoSaveStack.push (Boolean.valueOf (m_bAutoSaveEnabled));
      m_bAutoSaveEnabled = false;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  public final void endWithoutAutoSave ()
  {
    // Restore previous auto save state
    boolean bPreviouslyAutoSaveEnabled;
    m_aRWLock.writeLock ().lock ();
    try
    {
      bPreviouslyAutoSaveEnabled = m_aAutoSaveStack.pop ().booleanValue ();
      m_bAutoSaveEnabled = bPreviouslyAutoSaveEnabled;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }

    if (bPreviouslyAutoSaveEnabled)
    {
      // And in case something was changed - writeLocked itself
      writeToFileOnPendingChanges ();
    }
  }

  /**
   * Execute a callback with autosave being disabled. Must be called outside a
   * writeLock, as this method locks itself!
   * 
   * @param aRunnable
   *        The callback to be executed
   */
  public final void performWithoutAutoSave (@Nonnull final INonThrowingRunnable aRunnable)
  {
    performWithoutAutoSave (AdapterRunnableToCallable.createAdapter (aRunnable));
  }

  /**
   * Execute a callback with autosave being disabled. Must be called outside a
   * writeLock, as this method locks itself!
   * 
   * @param aCallable
   *        The callback to be executed
   * @return The result of the callback. May be <code>null</code>.
   */
  @Nullable
  public final <RETURNTYPE> RETURNTYPE performWithoutAutoSave (@Nonnull final INonThrowingCallable <RETURNTYPE> aCallable)
  {
    if (aCallable == null)
      throw new NullPointerException ("callable");

    beginWithoutAutoSave ();
    try
    {
      // Main call of callable
      return aCallable.call ();
    }
    finally
    {
      endWithoutAutoSave ();
    }
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("IO", m_aIO)
                                       .append ("autoSaveStack", m_aAutoSaveStack)
                                       .append ("pendingChanges", m_bPendingChanges)
                                       .append ("autoSaveEnabled", m_bAutoSaveEnabled)
                                       .toString ();
  }
}
