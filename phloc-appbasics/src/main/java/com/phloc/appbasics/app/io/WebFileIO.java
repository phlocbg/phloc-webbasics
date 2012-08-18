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
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.io.EAppend;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.resource.FileSystemResource;

/**
 * Abstract for accessing files inside the web application.
 * 
 * @author philip
 */
@ThreadSafe
public final class WebFileIO
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (WebFileIO.class);
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
      WebIO.getFileOpMgr ().createDirRecursiveIfNotExisting (s_aBasePath);
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

  /**
   * Reset the base path - no matter if it was initialized or not.
   */
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

  /**
   * @return <code>true</code> if the base path was initialized,
   *         <code>false</code> otherwise
   */
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

  /**
   * @return The base path.
   * @throws IllegalStateException
   *         if no base path was provided
   */
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

  /**
   * @return The absolute base path that is used
   * @throws IllegalStateException
   *         if no base path was provided
   */
  @Nonnull
  @Nonempty
  public static String getBasePath ()
  {
    return getBasePathFile ().getAbsolutePath ();
  }

  /**
   * Get a {@link File} relative to the base path
   * 
   * @param sRelativePath
   *        the relative path
   * @return The "absolute" {@link File} and never <code>null</code>.
   * @throws IllegalStateException
   *         if no base path was provided
   */
  @Nonnull
  public static File getFile (@Nonnull final String sRelativePath)
  {
    return new File (getBasePathFile (), sRelativePath);
  }

  /**
   * Check if a file relative to the base path exists
   * 
   * @param sRelativePath
   *        the relative path
   * @return <code>true</code> if the {@link File} is a file and exists,
   *         <code>false</code> otherwise.
   * @throws IllegalStateException
   *         if no base path was provided
   */
  @Nonnull
  public static boolean existsFile (@Nonnull final String sRelativePath)
  {
    return FileUtils.existsFile (getFile (sRelativePath));
  }

  /**
   * Check if a directory relative to the base path exists
   * 
   * @param sRelativePath
   *        the relative path
   * @return <code>true</code> if the {@link File} is a directory and exists,
   *         <code>false</code> otherwise.
   * @throws IllegalStateException
   *         if no base path was provided
   */
  @Nonnull
  public static boolean existsDir (@Nonnull final String sRelativePath)
  {
    return FileUtils.existsDir (getFile (sRelativePath));
  }

  /**
   * Get the file system resource relative to the base path
   * 
   * @param sRelativePath
   *        the relative path
   * @return The "absolute" {@link FileSystemResource} and never
   *         <code>null</code>.
   * @throws IllegalStateException
   *         if no base path was provided
   */
  @Nonnull
  public static FileSystemResource getResource (@Nonnull final String sRelativePath)
  {
    return new FileSystemResource (getFile (sRelativePath));
  }

  /**
   * Get the {@link InputStream} relative to the base path
   * 
   * @param sRelativePath
   *        the relative path
   * @return <code>null</code> if the path does not exist
   * @throws IllegalStateException
   *         if no base path was provided
   */
  @Nullable
  public static InputStream getInputStream (@Nonnull final String sRelativePath)
  {
    return getResource (sRelativePath).getInputStream ();
  }

  /**
   * Get the {@link Reader} relative to the base path
   * 
   * @param sRelativePath
   *        the relative path
   * @param aCharset
   *        The charset to use. May not be <code>null</code>.
   * @return <code>null</code> if the path does not exist
   * @throws IllegalStateException
   *         if no base path was provided
   */
  @Nullable
  public static Reader getReader (@Nonnull final String sRelativePath, @Nonnull final Charset aCharset)
  {
    return getResource (sRelativePath).getReader (aCharset);
  }

  /**
   * Get the {@link Reader} relative to the base path
   * 
   * @param sRelativePath
   *        the relative path
   * @param sCharset
   *        The charset to use. May not be <code>null</code>.
   * @return <code>null</code> if the path does not exist
   * @throws IllegalStateException
   *         if no base path was provided
   */
  @Nullable
  public static Reader getReader (@Nonnull final String sRelativePath, @Nonnull final String sCharset)
  {
    return getResource (sRelativePath).getReader (sCharset);
  }

  /**
   * Get the {@link OutputStream} relative to the base path
   * 
   * @param sRelativePath
   *        the relative path
   * @param eAppend
   *        Append or truncate mode. May not be <code>null</code>.
   * @return <code>null</code> if the path is not writable
   */
  @Nullable
  public static OutputStream getOutputStream (@Nonnull final String sRelativePath, @Nonnull final EAppend eAppend)
  {
    return getResource (sRelativePath).getOutputStream (eAppend);
  }

  /**
   * Get the {@link Writer} relative to the base path
   * 
   * @param sRelativePath
   *        the relative path
   * @param aCharset
   *        The charset to use. May not be <code>null</code>.
   * @param eAppend
   *        Append or truncate mode. May not be <code>null</code>.
   * @return <code>null</code> if the path is not writable
   */
  @Nullable
  public static Writer getWriter (@Nonnull final String sRelativePath,
                                  @Nonnull final Charset aCharset,
                                  @Nonnull final EAppend eAppend)
  {
    return getResource (sRelativePath).getWriter (aCharset, eAppend);
  }

  /**
   * Get the {@link Writer} relative to the base path
   * 
   * @param sRelativePath
   *        the relative path
   * @param sCharset
   *        The charset to use. May not be <code>null</code>.
   * @param eAppend
   *        Append or truncate mode. May not be <code>null</code>.
   * @return <code>null</code> if the path is not writable
   */
  @Nullable
  public static Writer getWriter (@Nonnull final String sRelativePath,
                                  @Nonnull final String sCharset,
                                  @Nonnull final EAppend eAppend)
  {
    return getResource (sRelativePath).getWriter (sCharset, eAppend);
  }
}
