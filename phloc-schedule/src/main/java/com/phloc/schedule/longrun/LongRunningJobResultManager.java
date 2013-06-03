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
package com.phloc.schedule.longrun;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.appbasics.app.dao.impl.DAOException;
import com.phloc.appbasics.app.io.WebIORegistry;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.scopes.singleton.GlobalSingleton;

@ThreadSafe
public final class LongRunningJobResultManager extends GlobalSingleton
{
  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final LongRunningJobResultManagerDAO m_aDAO;

  @Deprecated
  @UsedViaReflection
  public LongRunningJobResultManager () throws DAOException
  {
    m_aDAO = new LongRunningJobResultManagerDAO (WebIORegistry.getRegistryFilename ("longrunningjobresults.xml"));
  }

  @Nonnull
  public static LongRunningJobResultManager getInstance ()
  {
    return getGlobalSingleton (LongRunningJobResultManager.class);
  }

  public void addResult (@Nonnull final LongRunningJobData aJobData)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aDAO.addResult (aJobData);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <LongRunningJobData> getAllJobResults ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aDAO.getAllJobResults ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public LongRunningJobData getJobResultOfID (@Nullable final String sJobResultID)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aDAO.getJobResultOfID (sJobResultID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }
}
