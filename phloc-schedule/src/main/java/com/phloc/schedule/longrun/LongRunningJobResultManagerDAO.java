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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.appbasics.app.dao.impl.DAOException;
import com.phloc.appbasics.app.dao.xml.AbstractXMLDAO;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;

@NotThreadSafe
final class LongRunningJobResultManagerDAO extends AbstractXMLDAO
{
  private final Map <String, LongRunningJobData> m_aMap = new LinkedHashMap <String, LongRunningJobData> ();

  public LongRunningJobResultManagerDAO (@Nullable final String sFilename) throws DAOException
  {
    super (sFilename);
    setXMLDataProvider (new LongRunningJobResultManagerXMLDAO (this));
    readFromFile ();
  }

  void internalClear ()
  {
    m_aMap.clear ();
  }

  void internalAdd (@Nonnull final LongRunningJobData aJobData)
  {
    m_aMap.put (aJobData.getID (), aJobData);
  }

  @Nonnull
  @ReturnsMutableCopy
  Collection <LongRunningJobData> internalGetAllForSave ()
  {
    return ContainerHelper.getSortedByKey (m_aMap).values ();
  }

  public void addResult (@Nonnull final LongRunningJobData aJobData)
  {
    if (aJobData == null)
      throw new NullPointerException ("jobData");
    if (!aJobData.isEnded ())
      throw new IllegalArgumentException ("Passed jobData is not yet finished");

    m_aRWLock.writeLock ().lock ();
    try
    {
      internalAdd (aJobData);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    markAsChanged ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <LongRunningJobData> getAllJobResults ()
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

  @Nullable
  public LongRunningJobData getJobResultOfID (@Nullable final String sJobResultID)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aMap.get (sJobResultID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }
}
