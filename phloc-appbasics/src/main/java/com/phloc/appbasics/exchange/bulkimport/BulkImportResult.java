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
package com.phloc.appbasics.exchange.bulkimport;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.state.ISuccessIndicator;
import com.phloc.commons.type.ITypedObject;

/**
 * This class represents the results of a bulk import.
 * 
 * @author boris, philip
 */
@ThreadSafe
public class BulkImportResult implements ISuccessIndicator
{
  /**
   * Default success value
   */
  public static final boolean DEFAULT_SUCCESS = true;
  /**
   * Default value for maximum number of warnings to maintain in a list.
   */
  public static final int DEFAULT_MAX_WARNINGS = 1000;

  protected final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final int m_nMaxWarnings;
  private boolean m_bSuccess = DEFAULT_SUCCESS;
  private final Map <String, ITypedObject <String>> m_aAdded = new LinkedHashMap <String, ITypedObject <String>> ();
  private final Map <String, ITypedObject <String>> m_aChanged = new LinkedHashMap <String, ITypedObject <String>> ();
  private final List <String> m_aFailed = new ArrayList <String> ();
  private final List <String> m_aWarnings = new ArrayList <String> ();
  private int m_nAdditionalWarnings = 0;

  public BulkImportResult ()
  {
    this (DEFAULT_MAX_WARNINGS);
  }

  public BulkImportResult (@Nonnegative final int nMaxWarnings)
  {
    this.m_nMaxWarnings = nMaxWarnings;
  }

  public final void registerAdded (@Nonnull final ITypedObject <String> aObj)
  {
    this.m_aRWLock.writeLock ().lock ();
    try
    {
      this.m_aAdded.put (aObj.getID (), aObj);
    }
    finally
    {
      this.m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <ITypedObject <String>> getAdded ()
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (this.m_aAdded.values ());
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnegative
  public final int getAddedCount ()
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      return this.m_aAdded.size ();
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  public final boolean containsAdded (@Nonnull final ITypedObject <String> aObj)
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      // linear scanning :(
      return this.m_aAdded.containsKey (aObj.getID ());
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  public final void registerChanged (@Nonnull final ITypedObject <String> aObj)
  {
    this.m_aRWLock.writeLock ().lock ();
    try
    {
      this.m_aChanged.put (aObj.getID (), aObj);
    }
    finally
    {
      this.m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <ITypedObject <String>> getChanged ()
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (this.m_aChanged.values ());
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnegative
  public final int getChangedCount ()
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      return this.m_aChanged.size ();
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  public final boolean containsChanged (@Nonnull final ITypedObject <String> aObj)
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      // linear scanning :(
      return this.m_aChanged.containsKey (aObj.getID ());
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  public final void registerFailed (final String sID)
  {
    this.m_aRWLock.writeLock ().lock ();
    try
    {
      this.m_aFailed.add (sID);
    }
    finally
    {
      this.m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <String> getFailed ()
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (this.m_aFailed);
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnegative
  public final int getFailedCount ()
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      return this.m_aFailed.size ();
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  public final boolean containsFailed (@Nullable final String sID)
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      // linear scanning :(
      return this.m_aFailed.contains (sID);
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  public final void addWarning (final String sWarningMsg)
  {
    this.m_aRWLock.writeLock ().lock ();
    try
    {
      if (this.m_aWarnings.size () < this.m_nMaxWarnings)
      {
        this.m_aWarnings.add (sWarningMsg);
      }
      else
        this.m_nAdditionalWarnings++;
    }
    finally
    {
      this.m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <String> getWarnings ()
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (this.m_aWarnings);
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return The number of all warnings. Always &ge; 0.
   */
  @Nonnegative
  public final int getWarningsCount ()
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      return this.m_aWarnings.size () + this.m_nAdditionalWarnings;
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  public final void setSuccess (final boolean bSuccess)
  {
    this.m_aRWLock.writeLock ().lock ();
    try
    {
      this.m_bSuccess = bSuccess;
    }
    finally
    {
      this.m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Indicates, that the overall import succeeded. Default is <code>true</code>.
   * 
   * @return <code>true</code> for import success, <code>false</code> for import
   *         failure.
   */
  @Override
  public final boolean isSuccess ()
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      return this.m_bSuccess;
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Indicates, that the overall import failed.
   * 
   * @return Inverse of {@link #isSuccess()}
   */
  @Override
  public final boolean isFailure ()
  {
    return !isSuccess ();
  }
}
