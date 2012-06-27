/**
 * Copyright (C) 2006-2012 phloc systems
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

import com.phloc.appbasics.app.WebFileIO;
import com.phloc.commons.string.StringHelper;

@Immutable
public final class LoggedInUserStorage
{
  private LoggedInUserStorage ()
  {}

  @Nonnull
  private static String _getLoggedInUserID ()
  {
    final String ret = LoggedInUserManager.getInstance ().getCurrentUserID ();
    if (StringHelper.hasNoText (ret))
      throw new IllegalStateException ("No user is logged in!");
    return ret;
  }

  /**
   * @return The base directory for all user-related data
   */
  @Nonnull
  public static File getUserdataDirectory ()
  {
    final File aDir = WebFileIO.getFile ("userdata/" + _getLoggedInUserID ());
    WebFileIO.getFileOpMgr ().createDirRecursiveIfNotExisting (aDir);
    return aDir;
  }

  /**
   * @return The upload directory of the current user.
   */
  @Nonnull
  public static File getUploadDirectory ()
  {
    final File aDir = new File (getUserdataDirectory (), "upload");
    WebFileIO.getFileOpMgr ().createDirIfNotExisting (aDir);
    return aDir;
  }
}
