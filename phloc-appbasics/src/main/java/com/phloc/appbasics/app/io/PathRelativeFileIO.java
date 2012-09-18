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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.exceptions.InitializationException;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.EAppend;
import com.phloc.commons.io.file.FileIOError;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.io.resource.FileSystemResource;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Abstract for accessing files inside a specific path.
 * 
 * @author philip
 */
@Immutable
public final class PathRelativeFileIO
{
  private final Logger s_aLogger = LoggerFactory.getLogger (PathRelativeFileIO.class);

  private final File m_aBasePath;

  public PathRelativeFileIO (@Nonnull final File aBasePath)
  {
    if (aBasePath == null)
      throw new NullPointerException ("basePath");

    // Ensure the directory is present
    WebIO.getFileOpMgr ().createDirRecursiveIfNotExisting (aBasePath);

    // Must be an existing directory
    if (!aBasePath.isDirectory ())
      throw new InitializationException ("The passed base path exists but is not a directory!");

    // Check read/write/execute
    if (!FileUtils.canRead (aBasePath))
      throw new IllegalArgumentException ("Cannot read in " + aBasePath);
    if (!FileUtils.canWrite (aBasePath))
      s_aLogger.warn ("Cannot write in " + aBasePath);
    if (!FileUtils.canExecute (aBasePath))
      s_aLogger.warn ("Cannot execute in " + aBasePath);

    m_aBasePath = aBasePath;
  }

  /**
   * @return The storage base path. Never <code>null</code>.
   */
  @Nonnull
  public File getBasePathFile ()
  {
    return m_aBasePath;
  }

  /**
   * @return The absolute base path that is used. Neither <code>null</code> nor
   *         empty.
   */
  @Nonnull
  @Nonempty
  public String getBasePath ()
  {
    return getBasePathFile ().getAbsolutePath ();
  }

  /**
   * Get a {@link File} relative to the base path.
   * 
   * @param sRelativePath
   *        the relative path
   * @return The "absolute" {@link File} and never <code>null</code>.
   * @see #getBasePathFile()
   */
  @Nonnull
  public File getFile (@Nonnull final String sRelativePath)
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
   * @see #getBasePathFile()
   */
  public boolean existsFile (@Nonnull final String sRelativePath)
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
   * @see #getBasePathFile()
   */
  public boolean existsDir (@Nonnull final String sRelativePath)
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
   * @see #getBasePathFile()
   */
  @Nonnull
  public FileSystemResource getResource (@Nonnull final String sRelativePath)
  {
    return new FileSystemResource (getFile (sRelativePath));
  }

  /**
   * Get the {@link InputStream} relative to the base path
   * 
   * @param sRelativePath
   *        the relative path
   * @return <code>null</code> if the path does not exist
   * @see #getBasePathFile()
   */
  @Nullable
  public InputStream getInputStream (@Nonnull final String sRelativePath)
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
   * @see #getBasePathFile()
   */
  @Nullable
  public Reader getReader (@Nonnull final String sRelativePath, @Nonnull final Charset aCharset)
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
   * @see #getBasePathFile()
   */
  @Nullable
  public Reader getReader (@Nonnull final String sRelativePath, @Nonnull final String sCharset)
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
   * @see #getBasePathFile()
   */
  @Nullable
  public OutputStream getOutputStream (@Nonnull final String sRelativePath, @Nonnull final EAppend eAppend)
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
   * @see #getBasePathFile()
   */
  @Nullable
  public Writer getWriter (@Nonnull final String sRelativePath,
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
   * @see #getBasePathFile()
   */
  @Nullable
  public Writer getWriter (@Nonnull final String sRelativePath,
                           @Nonnull final String sCharset,
                           @Nonnull final EAppend eAppend)
  {
    return getResource (sRelativePath).getWriter (sCharset, eAppend);
  }

  /**
   * Create the appropriate directory if it is not existing
   * 
   * @param sRelativePath
   *        the relative path
   * @param bRecursive
   *        if <code>true</code> all missing parent directories will be created
   * @return Success indicator. Never <code>null</code>.
   * @see #getBasePathFile()
   */
  @Nonnull
  public FileIOError createDirectory (@Nonnull final String sRelativePath, final boolean bRecursive)
  {
    final File aDir = getFile (sRelativePath);
    return bRecursive ? WebIO.getFileOpMgr ().createDirRecursiveIfNotExisting (aDir)
                     : WebIO.getFileOpMgr ().createDirIfNotExisting (aDir);
  }

  /**
   * Get the relative file name for the passed absolute file.
   * 
   * @param aAbsoluteFile
   *        The non-<code>null</code> absolute file to make relative.
   * @return <code>null</code> if the passed file is not a child of this base
   *         directory.
   */
  @Nullable
  public String getRelativeFilename (@Nonnull final File aAbsoluteFile)
  {
    return FilenameHelper.getRelativeToParentDirectory (aAbsoluteFile, m_aBasePath);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof PathRelativeFileIO))
      return false;
    final PathRelativeFileIO rhs = (PathRelativeFileIO) o;
    return m_aBasePath.equals (rhs.m_aBasePath);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aBasePath).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("basePath", m_aBasePath).toString ();
  }
}
