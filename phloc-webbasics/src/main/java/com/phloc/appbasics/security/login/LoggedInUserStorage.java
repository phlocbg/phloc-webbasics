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
package com.phloc.appbasics.security.login;

import java.io.File;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.io.WebFileIO;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.string.StringHelper;

/**
 * This class encapsulates the file IO base directory for the current user
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class LoggedInUserStorage
{
  /**
   * The name of the default base directory relative to the WebFileIO where the
   * data is referenced
   */
  public static final String BASE_DIRECTORY = "userdata/";

  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggedInUserStorage.class);

  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  private static String s_sBaseDirectory = BASE_DIRECTORY;

  private LoggedInUserStorage ()
  {}

  /**
   * @return The base directory to be used. By default {@link #BASE_DIRECTORY}
   *         is used.
   */
  @Nonnull
  public static String getBaseDirectory ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_sBaseDirectory;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Set the base directory to be used.
   * 
   * @param sBaseDirectory
   *        The new base directory. May not be <code>null</code> but maybe
   *        empty.
   */
  public static void setBaseDirectory (@Nonnull final String sBaseDirectory)
  {
    ValueEnforcer.notNull (sBaseDirectory, "BaseDirectory");

    s_aRWLock.writeLock ().lock ();
    try
    {
      s_sBaseDirectory = sBaseDirectory;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return The base directory for all user-related data of the current user
   */
  @Nonnull
  public static File getUserdataDirectory ()
  {
    final String sCurrentUserID = LoggedInUserManager.getInstance ().getCurrentUserID ();
    return getUserdataDirectory (sCurrentUserID);
  }

  /**
   * @param sUserID
   *        the ID of the user for which the user data is requested. May neither
   *        be <code>null</code> nor empty.
   * @return The base directory for all user-related data
   */
  @Nonnull
  public static File getUserdataDirectory (@Nonnull @Nonempty final String sUserID)
  {
    ValueEnforcer.notEmpty (sUserID, "UserID");

    // Ensure user ID is valid as a filename!
    final String sRealUserID = FilenameHelper.getAsSecureValidASCIIFilename (sUserID);
    if (StringHelper.hasNoText (sRealUserID))
      throw new IllegalStateException ("Passed user ID '" + sUserID + "' is an empty filename!");
    if (!sRealUserID.equals (sUserID))
      s_aLogger.warn ("User ID '" +
                      sUserID +
                      "' was modified to '" +
                      sRealUserID +
                      "' to be used as a file system name!");

    final File aDir = WebFileIO.getFile (getBaseDirectory () + sRealUserID);
    WebFileIO.getFileOpMgr ().createDirRecursiveIfNotExisting (aDir);
    return aDir;
  }
}
