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
package com.phloc.appbasics.security.login;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.io.WebFileIO;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.string.StringHelper;

/**
 * This class encapsulates the the file IO base directory for the current user
 * 
 * @author philip
 */
@Immutable
public final class LoggedInUserStorage
{
  /**
   * The name of the base directory relative to the WebFileIO where the data is
   * referenced
   */
  public static final String BASE_DIRECTORY = "userdata/";

  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggedInUserStorage.class);

  private LoggedInUserStorage ()
  {}

  /**
   * @return The base directory for all user-related data
   */
  @Nonnull
  public static File getUserdataDirectory ()
  {
    return getUserdataDirectory (LoggedInUserManager.getInstance ().getCurrentUserID ());
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
    if (StringHelper.hasNoText (sUserID))
      throw new IllegalArgumentException ("userID may not be empty!");

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

    final File aDir = WebFileIO.getFile (BASE_DIRECTORY + sRealUserID);
    WebFileIO.getFileOpMgr ().createDirRecursiveIfNotExisting (aDir);
    return aDir;
  }
}
