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

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.exceptions.InitializationException;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.EAppend;
import com.phloc.commons.io.file.FileIOError;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.io.file.iterate.FileSystemRecursiveIterator;
import com.phloc.commons.io.resource.FileSystemResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.timing.StopWatch;

/**
 * Abstract for accessing files inside a specific path.
 * 
 * @author Philip Helger
 */
@Immutable
public final class PathRelativeFileIO
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (PathRelativeFileIO.class);

  @Nonnull
  private final File m_aBasePath;

  public PathRelativeFileIO (@Nonnull final File aBasePath, final boolean bCheckAccessRights)
  {
    ValueEnforcer.notNull (aBasePath, "BasePath");

    // Ensure the directory is present
    WebFileIO.getFileOpMgr ().createDirRecursiveIfNotExisting (aBasePath);

    // Must be an existing directory
    if (!aBasePath.isDirectory ())
      throw new InitializationException ("The passed base path " + aBasePath + " exists but is not a directory!");
    m_aBasePath = aBasePath;

    if (bCheckAccessRights)
    {
      // Check read/write/execute
      final StopWatch aSW = new StopWatch (true);
      s_aLogger.info ("Checking file access in " + aBasePath);
      int nFiles = 0;
      int nDirs = 0;
      for (final File aFile : new FileSystemRecursiveIterator (aBasePath))
        if (aFile.isFile ())
        {
          // Check if files are read-write
          if (!FileUtils.canRead (aFile))
            throw new IllegalArgumentException ("Cannot read file " + aFile);
          if (!FileUtils.canWrite (aFile))
            s_aLogger.warn ("Cannot write file " + aFile);
          ++nFiles;
        }
        else
          if (aFile.isDirectory ())
          {
            if (!FileUtils.canRead (aFile))
              throw new IllegalArgumentException ("Cannot read in directory " + aFile);
            if (!FileUtils.canWrite (aFile))
              s_aLogger.warn ("Cannot write in directory " + aFile);
            if (!FileUtils.canExecute (aFile))
              s_aLogger.warn ("Cannot execute in directory " + aFile);
            ++nDirs;
          }
          else
            s_aLogger.warn ("Neither file nor directory: " + aFile);
      s_aLogger.info ("Finished checking file access for " +
                      nFiles +
                      " files and " +
                      nDirs +
                      " directories in " +
                      aSW.stopAndGetMillis () +
                      " milliseconds");
    }
  }

  /**
   * @return The base path. Never <code>null</code>.
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
   * Get the {@link OutputStream} relative to the base path. An eventually
   * existing file is truncated.
   * 
   * @param sRelativePath
   *        the relative path
   * @return <code>null</code> if the path is not writable
   * @see #getBasePathFile()
   */
  @Nullable
  public OutputStream getOutputStream (@Nonnull final String sRelativePath)
  {
    return getOutputStream (sRelativePath, EAppend.TRUNCATE);
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
    return bRecursive ? WebFileIO.getFileOpMgr ().createDirRecursiveIfNotExisting (aDir)
                     : WebFileIO.getFileOpMgr ().createDirIfNotExisting (aDir);
  }

  @Nonnull
  public FileIOError deleteFile (@Nonnull final String sFilename)
  {
    return deleteFile (getFile (sFilename));
  }

  @Nonnull
  public FileIOError deleteFile (@Nonnull final File aFile)
  {
    return WebFileIO.getFileOpMgr ().deleteFile (aFile);
  }

  @Nonnull
  public FileIOError deleteFileIfExisting (@Nonnull final String sFilename)
  {
    return deleteFileIfExisting (getFile (sFilename));
  }

  @Nonnull
  public FileIOError deleteFileIfExisting (@Nonnull final File aFile)
  {
    return WebFileIO.getFileOpMgr ().deleteFileIfExisting (aFile);
  }

  @Nonnull
  public FileIOError deleteDirectory (@Nonnull final String sDirName, final boolean bDeleteRecursively)
  {
    return deleteDirectory (getFile (sDirName), bDeleteRecursively);
  }

  @Nonnull
  public FileIOError deleteDirectory (@Nonnull final File fDir, final boolean bDeleteRecursively)
  {
    return bDeleteRecursively ? WebFileIO.getFileOpMgr ().deleteDirRecursive (fDir) : WebFileIO.getFileOpMgr ()
                                                                                               .deleteDir (fDir);
  }

  @Nonnull
  public FileIOError deleteDirectoryIfExisting (@Nonnull final String sDirName, final boolean bDeleteRecursively)
  {
    return deleteDirectoryIfExisting (getFile (sDirName), bDeleteRecursively);
  }

  @Nonnull
  public FileIOError deleteDirectoryIfExisting (@Nonnull final File fDir, final boolean bDeleteRecursively)
  {
    return bDeleteRecursively ? WebFileIO.getFileOpMgr ().deleteDirRecursiveIfExisting (fDir)
                             : WebFileIO.getFileOpMgr ().deleteDirIfExisting (fDir);
  }

  @Nonnull
  public FileIOError renameFile (@Nonnull final String sOldFilename, @Nonnull final String sNewFilename)
  {
    final File fOld = getFile (sOldFilename);
    final File fNew = getFile (sNewFilename);
    return WebFileIO.getFileOpMgr ().renameFile (fOld, fNew);
  }

  @Nonnull
  public FileIOError renameDir (@Nonnull final String sOldDirName, @Nonnull final String sNewDirName)
  {
    final File fOld = getFile (sOldDirName);
    final File fNew = getFile (sNewDirName);
    return WebFileIO.getFileOpMgr ().renameDir (fOld, fNew);
  }

  /**
   * Helper function for saving a file with correct error handling.
   * 
   * @param sFilename
   *        name of the file. May not be <code>null</code>.
   * @param eAppend
   *        Appending mode. May not be <code>null</code>.
   * @param aBytes
   *        the bytes to be written. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  private ESuccess _writeFile (@Nonnull final String sFilename,
                               @Nonnull final EAppend eAppend,
                               @Nonnull final byte [] aBytes)
  {
    // save to file
    final OutputStream aOS = getOutputStream (sFilename, eAppend);
    if (aOS == null)
    {
      s_aLogger.error ("Failed to open output stream for file '" + sFilename + "'");
      return ESuccess.FAILURE;
    }

    // Close the OS automatically!
    return StreamUtils.writeStream (aOS, aBytes);
  }

  /**
   * Helper function for saving a file with correct error handling.
   * 
   * @param sFilename
   *        name of the file. May not be <code>null</code>.
   * @param sContent
   *        the content to save. May not be <code>null</code>.
   * @param aCharset
   *        The character set to use. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  public ESuccess saveFile (@Nonnull final String sFilename,
                            @Nonnull final String sContent,
                            @Nonnull final Charset aCharset)
  {
    return saveFile (sFilename, CharsetManager.getAsBytes (sContent, aCharset));
  }

  /**
   * Helper function for saving a file with correct error handling.
   * 
   * @param sFilename
   *        name of the file. May not be <code>null</code>.
   * @param aBytes
   *        the bytes to be written. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  public ESuccess saveFile (@Nonnull final String sFilename, @Nonnull final byte [] aBytes)
  {
    return _writeFile (sFilename, EAppend.TRUNCATE, aBytes);
  }

  /**
   * Helper function for saving a file with correct error handling.
   * 
   * @param sFilename
   *        name of the file. May not be <code>null</code>.
   * @param sContent
   *        the content to save. May not be <code>null</code>.
   * @param aCharset
   *        The character set to use. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  public ESuccess appendFile (@Nonnull final String sFilename,
                              @Nonnull final String sContent,
                              @Nonnull final Charset aCharset)
  {
    return appendFile (sFilename, CharsetManager.getAsBytes (sContent, aCharset));
  }

  /**
   * Helper function for saving a file with correct error handling.
   * 
   * @param sFilename
   *        name of the file. May not be <code>null</code>.
   * @param aBytes
   *        the bytes to be written. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  public ESuccess appendFile (@Nonnull final String sFilename, @Nonnull final byte [] aBytes)
  {
    return _writeFile (sFilename, EAppend.APPEND, aBytes);
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
