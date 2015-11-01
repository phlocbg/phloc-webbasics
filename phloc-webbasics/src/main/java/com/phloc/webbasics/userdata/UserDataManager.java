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
package com.phloc.webbasics.userdata;

import java.io.File;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.io.PathRelativeFileIO;
import com.phloc.appbasics.app.io.WebFileIO;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.io.resource.FileSystemResource;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.SimpleURL;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Manager for {@link UserDataObject} objects.
 * 
 * @author Philip Helger
 */
public final class UserDataManager
{
  /**
   * The default user data path, relative to a URL context and the servlet
   * context directory.
   */
  public static final String DEFAULT_USER_DATA_PATH = "/user";
  /** By default the user data is accessed via the servletContext IO */
  public static final boolean DEFAULT_SERVLET_CONTEXT_IO = true;

  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  private static String s_sUserDataPath = DEFAULT_USER_DATA_PATH;
  private static boolean s_bServletContextIO = DEFAULT_SERVLET_CONTEXT_IO;

  private UserDataManager ()
  {}

  /**
   * Set the user data path, relative to the URL context and relative to the
   * servlet context directory.
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

  /**
   * Get the base path, where all user objects reside. It is relative to the URL
   * context and relative to the servlet context directory.
   * 
   * @return The current user data path. Always starting with a "/", but without
   *         any context information. By default the return value is
   *         {@value #DEFAULT_USER_DATA_PATH}.
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
   * Determine whether file resources are located relative to the servlet
   * context (inside the web application) or inside the data directory (outside
   * the web application). By default the files reside inside the web
   * application.
   * 
   * @param bServletContextIO
   *        <code>true</code> to use servlet context IO, <code>false</code> to
   *        use data IO.
   */
  public static void setServletContextIO (final boolean bServletContextIO)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_bServletContextIO = bServletContextIO;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  public static boolean isServletContextIO ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_bServletContextIO;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @return Context and user data path. Always starting with a "/". E.g.
   *         <code>/user</code> or <code>/context/user</code>
   */
  @Nonnull
  @Nonempty
  public static String getContextAndUserDataPath (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    return LinkUtils.getURIWithContext (aRequestScope, getUserDataPath ());
  }

  /**
   * Get the URL to the passed UDO object.
   * 
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @param aUDO
   *        The UDO object to get the URL from.
   * @return The path to the user data object as an URL, including the context
   *         path. Always starting with a "/". E.g.
   *         <code>/context/user/file.txt</code> if this object points to
   *         <code>/file.txt</code> and the user data path is <code>/user</code>
   *         .
   */
  @Nonnull
  @Nonempty
  public static String getURLPath (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                   @Nonnull final UserDataObject aUDO)
  {
    ValueEnforcer.notNull (aUDO, "UDO");

    return LinkUtils.getURIWithContext (aRequestScope, getUserDataPath () + aUDO.getPath ());
  }

  /**
   * Get the URL to the passed UDO object.
   * 
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @param aUDO
   *        The UDO object to get the URL from.
   * @return The URL to the user data object, including the context path. Always
   *         starting with a "/". E.g. <code>/context/user/file.txt</code> if
   *         this object points to <code>/file.txt</code> and the user data path
   *         is <code>/user</code> .
   */
  @Nonnull
  @Nonempty
  public static SimpleURL getURL (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                  @Nonnull final UserDataObject aUDO)
  {
    return new SimpleURL (getURLPath (aRequestScope, aUDO));
  }

  @Nonnull
  private static PathRelativeFileIO _getFileIO ()
  {
    return isServletContextIO () ? WebFileIO.getServletContextIO () : WebFileIO.getDataIO ();
  }

  /**
   * @return The file system base path for UDOs.
   */
  @Nonnull
  public static File getBasePathFile ()
  {
    return _getFileIO ().getBasePathFile ();
  }

  /**
   * Get the file system resource of the passed UDO object.
   * 
   * @param aUDO
   *        The UDO object to get the resource from.
   * @return The matching file system resource. No check is performed, whether
   *         the resource exists or not!
   */
  @Nonnull
  public static FileSystemResource getResource (@Nonnull final UserDataObject aUDO)
  {
    if (aUDO == null)
      throw new NullPointerException ("UDO");
    return _getFileIO ().getResource (getUserDataPath () + aUDO.getPath ());
  }

  /**
   * Get the File of the passed UDO object.
   * 
   * @param aUDO
   *        The UDO object to get the resource from.
   * @return The matching File. No check is performed, whether the file exists
   *         or not!
   */
  @Nonnull
  public static File getFile (@Nonnull final UserDataObject aUDO)
  {
    if (aUDO == null)
      throw new NullPointerException ("UDO");
    return _getFileIO ().getFile (getUserDataPath () + aUDO.getPath ());
  }
}
