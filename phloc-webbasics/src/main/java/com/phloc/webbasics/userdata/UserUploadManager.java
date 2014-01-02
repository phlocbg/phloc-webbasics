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
package com.phloc.webbasics.userdata;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.io.WebFileIO;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.string.StringHelper;
import com.phloc.webbasics.userdata.UserDataObject;
import com.phloc.webscopes.singleton.SessionWebSingleton;

/**
 * A per-session manager, that handles all the uploaded files while the process
 * to which the files belong is still in process.
 * 
 * @author Philip Helger
 */
public class UserUploadManager extends SessionWebSingleton
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (UserUploadManager.class);
  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final Map <String, UserDataObject> m_aMap = new HashMap <String, UserDataObject> ();

  @Deprecated
  @UsedViaReflection
  public UserUploadManager ()
  {}

  @Nonnull
  public static UserUploadManager getInstance ()
  {
    return getSessionSingleton (UserUploadManager.class);
  }

  @Override
  protected void onDestroy ()
  {
    // Delete all unconfirmed files
    for (final UserDataObject aUDO : m_aMap.values ())
      _deleteUDO (aUDO);
    m_aMap.clear ();
  }

  private void _deleteUDO (@Nonnull final UserDataObject aUDO)
  {
    s_aLogger.info ("Deleting uploaded file " + aUDO);
    WebFileIO.getFileOpMgr ().deleteFile (aUDO.getAsFile ());
  }

  public void removeUploadedFile (@Nullable final String sID)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      final UserDataObject aUDO = m_aMap.remove (sID);
      if (aUDO != null)
        _deleteUDO (aUDO);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  public void addUploadedFile (@Nonnull @Nonempty final String sID, @Nonnull final UserDataObject aUDO)
  {
    if (StringHelper.hasNoText (sID))
      throw new IllegalArgumentException ("ID");
    if (aUDO == null)
      throw new NullPointerException ("UDO");

    m_aRWLock.writeLock ().lock ();
    try
    {
      // Remove an eventually existing old UDO
      final UserDataObject aOldUDO = m_aMap.remove (sID);
      if (aOldUDO != null)
        _deleteUDO (aOldUDO);

      // Add the new one
      m_aMap.put (sID, aUDO);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  public void confirmUploadedFiles (@Nullable final String... aIDs)
  {
    if (aIDs != null)
    {
      m_aRWLock.writeLock ().lock ();
      try
      {
        for (final String sID : aIDs)
        {
          // Remove an eventually existing old UDO
          final UserDataObject aUDO = m_aMap.remove (sID);
          if (aUDO != null)
            s_aLogger.info ("Confirmed uploaded file " + aUDO);
        }
      }
      finally
      {
        m_aRWLock.writeLock ().unlock ();
      }
    }
  }

  @Nullable
  public UserDataObject getUploadedFile (@Nullable final String sID)
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
}
