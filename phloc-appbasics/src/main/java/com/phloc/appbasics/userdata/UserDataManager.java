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
package com.phloc.appbasics.userdata;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;

public final class UserDataManager
{
  public static final String DEFAULT_USER_DATA_PATH = "/user";

  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  private static String s_sUserDataPath = DEFAULT_USER_DATA_PATH;

  private UserDataManager ()
  {}

  /**
   * Get the base path, where all user objects reside.
   * 
   * @return The current user data path. Always starting with a "/".
   */
  @Nonnull
  @Nonempty
  public static String getUserDataPath ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_sUserDataPath;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Set the user data path.
   * 
   * @param sUserDataPath
   *        The path to be set. May neither be <code>null</code> nor empty and
   *        must start with a "/" character.
   */
  public static void setUserDataPath (@Nonnull @Nonempty final String sUserDataPath)
  {
    if (StringHelper.getLength (sUserDataPath) < 2)
      throw new IllegalArgumentException ("userDataPath is too short");
    if (!sUserDataPath.startsWith ("/"))
      throw new IllegalArgumentException ("userDataPath must start with a slash");

    s_aRWLock.writeLock ().lock ();
    try
    {
      s_sUserDataPath = sUserDataPath;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }
}
