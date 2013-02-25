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
package com.phloc.web.smtp.failed;

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
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.StatisticsManager;

/**
 * This is a singleton object that keeps all the mails that could not be send.
 * 
 * @author philip
 */
@ThreadSafe
public class FailedMailQueue
{
  private static final class SingletonHolder
  {
    static final FailedMailQueue s_aInstance = new FailedMailQueue ();
  }

  private static final IStatisticsHandlerCounter s_aStatsCount = StatisticsManager.getCounterHandler (FailedMailQueue.class);

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final Map <String, FailedMailData> m_aMap = new LinkedHashMap <String, FailedMailData> ();

  private FailedMailQueue ()
  {}

  @Nonnull
  public static FailedMailQueue getInstance ()
  {
    return SingletonHolder.s_aInstance;
  }

  public void add (@Nonnull final FailedMailData aFailedMailData)
  {
    if (aFailedMailData == null)
      throw new NullPointerException ("failedMailData");

    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aMap.put (aFailedMailData.getID (), aFailedMailData);
      s_aStatsCount.increment ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Remove the failed mail at the given index.
   * 
   * @param sID
   *        The ID of the failed mail data to be removed.
   * @return <code>null</code> if no such data exists
   */
  @Nullable
  public FailedMailData remove (final String sID)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      return m_aMap.remove (sID);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnegative
  public int size ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aMap.size ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public FailedMailData getFailedMailOfID (@Nullable final String sID)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aMap.get (sID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <FailedMailData> getAllFailedMails ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (m_aMap.values ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
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
    m_aRWLock.writeLock ().lock ();
    try
    {
      final List <FailedMailData> aTempList = new ArrayList <FailedMailData> (m_aMap.size ());
      if (!m_aMap.isEmpty ())
      {
        aTempList.addAll (m_aMap.values ());
        m_aMap.clear ();
      }
      return aTempList;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }
}
