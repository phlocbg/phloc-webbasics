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
package com.phloc.appbasics.app.io;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.io.EAppend;
import com.phloc.commons.io.IReadWriteResource;
import com.phloc.commons.io.file.FileOperationManager;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.file.IFileOperationManager;
import com.phloc.commons.io.file.LoggingFileOperationCallback;
import com.phloc.commons.io.resource.FileSystemResource;

/**
 * Abstract for accessing files inside the web application
 * 
 * @author philip
 */
@ThreadSafe
public final class WebFileIO
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (WebFileIO.class);
  private static final IFileOperationManager s_aFOM = new FileOperationManager (new LoggingFileOperationCallback ());
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  private static File s_aBasePath;

  private WebFileIO ()
  {}

  public static void initBasePath (@Nonnull final File aBasePath)
  {
    if (aBasePath == null)
      throw new NullPointerException ("basePath");

    s_aRWLock.writeLock ().lock ();
    try
    {
      if (s_aBasePath != null)
        throw new IllegalStateException ("Another base path is already present: " + s_aBasePath);

      s_aLogger.info ("Using '" + aBasePath + "' as the storage base");
      s_aBasePath = aBasePath;

      // Ensure the base directory is present
      s_aFOM.createDirRecursiveIfNotExisting (s_aBasePath);
      if (!FileUtils.canRead (s_aBasePath))
        throw new IllegalArgumentException ("Cannot read in " + s_aBasePath);
      if (!FileUtils.canWrite (s_aBasePath))
        throw new IllegalArgumentException ("Cannot write in " + s_aBasePath);
      if (!FileUtils.canExecute (s_aBasePath))
        throw new IllegalArgumentException ("Cannot execute in " + s_aBasePath);
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  public static void resetBasePath ()
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aBasePath = null;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  public static boolean isBasePathInited ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aBasePath != null;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  public static File getBasePathFile ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      if (s_aBasePath == null)
        throw new IllegalStateException ("Base path was not initialized!");
      return s_aBasePath;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @Nonempty
  public static String getBasePath ()
  {
    return getBasePathFile ().getAbsolutePath ();
  }

  @Nonnull
  public static File getFile (@Nonnull final String sPath)
  {
    return new File (getBasePathFile (), sPath);
  }

  @Nonnull
  public static IReadWriteResource getResource (@Nonnull final String sPath)
  {
    return new FileSystemResource (getFile (sPath));
  }

  @Nonnull
  public static InputStream getInputStream (@Nonnull final String sPath)
  {
    return getResource (sPath).getInputStream ();
  }

  @Nonnull
  public static OutputStream getOutputStream (@Nonnull final String sBasePathRelativePath,
                                              @Nonnull final EAppend eAppend)
  {
    return getResource (sBasePathRelativePath).getOutputStream (eAppend);
  }

  @Nonnull
  public static IFileOperationManager getFileOpMgr ()
  {
    return s_aFOM;
  }
}
