/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.web.smtp.failed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.MustBeLocked;
import com.phloc.commons.annotations.MustBeLocked.ELockType;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This is a singleton object that keeps all the mails that could not be send.
 * 
 * @author Boris Gregorcic
 */
@ThreadSafe
public class FailedMailQueue implements Serializable
{
  private static final long serialVersionUID = 5407187580582970941L;
  private static final Logger LOG = LoggerFactory.getLogger (FailedMailQueue.class);
  private static final IStatisticsHandlerCounter s_aStatsCountAdd = StatisticsManager.getCounterHandler (FailedMailQueue.class.getName () +
                                                                                                         "$add");
  private static final IStatisticsHandlerCounter s_aStatsCountRemove = StatisticsManager.getCounterHandler (FailedMailQueue.class.getName () +
                                                                                                            "$remove");

  protected final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  @GuardedBy ("m_aRWLock")
  private final Map <String, FailedMailData> m_aMap = new LinkedHashMap <String, FailedMailData> ();

  public FailedMailQueue ()
  {}

  @MustBeLocked (ELockType.WRITE)
  protected void internalAdd (@Nonnull final FailedMailData aFailedMailData)
  {
    this.m_aMap.put (aFailedMailData.getID (), aFailedMailData);
    s_aStatsCountAdd.increment ();
  }

  public void add (@Nonnull final FailedMailData aFailedMailData)
  {
    ValueEnforcer.notNull (aFailedMailData, "FailedMailData");
    if (aFailedMailData.getEmailData () != null && !aFailedMailData.getEmailData ().isUseFailedMailQueue ())
    {
      if (LOG.isDebugEnabled ())
      {
        LOG.debug ("Not adding failed mail as it was marked to not use the failed mail queue: {}",
                   aFailedMailData.getEmailData ().getTo ());
      }
      return;
    }
    this.m_aRWLock.writeLock ().lock ();
    try
    {
      internalAdd (aFailedMailData);
    }
    finally
    {
      this.m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  @MustBeLocked (ELockType.WRITE)
  protected FailedMailData internalRemove (@Nullable final String sID)
  {
    final FailedMailData ret = this.m_aMap.remove (sID);
    if (ret != null)
      s_aStatsCountRemove.increment ();
    return ret;
  }

  /**
   * Remove the failed mail at the given index.
   * 
   * @param sID
   *        The ID of the failed mail data to be removed.
   * @return <code>null</code> if no such data exists
   */
  @Nullable
  public FailedMailData remove (@Nullable final String sID)
  {
    if (StringHelper.hasNoText (sID))
      return null;

    this.m_aRWLock.writeLock ().lock ();
    try
    {
      return internalRemove (sID);
    }
    finally
    {
      this.m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnegative
  @MustBeLocked (ELockType.READ)
  protected int internalSize ()
  {
    return this.m_aMap.size ();
  }

  @Nonnegative
  public int size ()
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      return internalSize ();
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  @MustBeLocked (ELockType.READ)
  protected FailedMailData internalGetFailedMailOfID (@Nullable final String sID)
  {
    return this.m_aMap.get (sID);
  }

  @Nullable
  public FailedMailData getFailedMailOfID (@Nullable final String sID)
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      return internalGetFailedMailOfID (sID);
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnegative
  @MustBeLocked (ELockType.READ)
  protected int internalGetFailedMailCount ()
  {
    return this.m_aMap.size ();
  }

  @Nonnegative
  public int getFailedMailCount ()
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      return internalGetFailedMailCount ();
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  @MustBeLocked (ELockType.READ)
  protected List <FailedMailData> internalGetAllFailedMails ()
  {
    return ContainerHelper.newList (this.m_aMap.values ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <FailedMailData> getAllFailedMails ()
  {
    this.m_aRWLock.readLock ().lock ();
    try
    {
      return internalGetAllFailedMails ();
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  @MustBeLocked (ELockType.WRITE)
  protected List <FailedMailData> internalRemoveAll ()
  {
    final List <FailedMailData> aTempList = new ArrayList <FailedMailData> (this.m_aMap.size ());
    if (!this.m_aMap.isEmpty ())
    {
      aTempList.addAll (this.m_aMap.values ());
      this.m_aMap.clear ();
    }
    return aTempList;
  }

  /**
   * Remove and return all failed mails.
   * 
   * @return All currently available failed mails. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <FailedMailData> removeAll ()
  {
    this.m_aRWLock.writeLock ().lock ();
    try
    {
      return internalRemoveAll ();
    }
    finally
    {
      this.m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("map", this.m_aMap).toString ();
  }
}
