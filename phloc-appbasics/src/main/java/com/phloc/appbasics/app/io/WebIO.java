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
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.io.EAppend;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.IWritableResource;
import com.phloc.commons.io.IWritableResourceProvider;
import com.phloc.commons.io.file.FileOperationManager;
import com.phloc.commons.io.file.LoggingFileOperationCallback;
import com.phloc.commons.io.resourceprovider.ClassPathResourceProvider;
import com.phloc.commons.io.resourceprovider.FileSystemResourceProvider;
import com.phloc.commons.io.resourceprovider.WritableResourceProviderChain;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.state.ISuccessIndicator;

/**
 * This class handles the path to the settings file for writing back. This is
 * e.g. the case for the user manager or the access manager.<br>
 * The path may be relative in case when the application resides in a JAR file
 * or a WAR file.
 * 
 * @author philip
 */
public final class WebIO
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (WebIO.class);
  private static IWritableResourceProvider s_aResourceProvider;
  private static FileOperationManager s_aFileOpMgr = new FileOperationManager (new LoggingFileOperationCallback ());

  static
  {
    // Ensure that some resource provider is present!
    s_aResourceProvider = new WritableResourceProviderChain (new FileSystemResourceProvider (),
                                                             new ClassPathResourceProvider ());
  }

  private WebIO ()
  {}

  /**
   * This method is required to be called with a working base path.
   * 
   * @param aResourceProvider
   *        Never <code>null</code>.
   */
  public static void init (@Nonnull final IWritableResourceProvider aResourceProvider)
  {
    if (aResourceProvider == null)
      throw new NullPointerException ("resourceProvider");

    if (!aResourceProvider.equals (s_aResourceProvider))
    {
      s_aResourceProvider = aResourceProvider;
      if (GlobalDebug.isDebugMode ())
        s_aLogger.info ("Setting resource provider to " + aResourceProvider);
    }
  }

  @Nonnull
  public static void setFileOpMgr (@Nonnull final FileOperationManager aFileOpMgr)
  {
    if (aFileOpMgr == null)
      throw new NullPointerException ("fileOpMgr");
    s_aFileOpMgr = aFileOpMgr;
  }

  @Nonnull
  public static FileOperationManager getFileOpMgr ()
  {
    return s_aFileOpMgr;
  }

  /**
   * Get a read-only resource object matching the current name using the global
   * resource provider.
   * 
   * @param sFilename
   *        The resource to be resolved.
   * @return Never <code>null</code> but may be a non-existing resource.
   */
  @Nonnull
  public static IReadableResource getReadableResource (@Nonnull final String sFilename)
  {
    if (sFilename == null)
      throw new NullPointerException ("fileName");

    return s_aResourceProvider.getReadableResource (sFilename);
  }

  /**
   * Get a writable resource object matching the current name using the global
   * resource provider.
   * 
   * @param sFilename
   *        The resource to be resolved.
   * @return Never <code>null</code> but may be a non-existing resource.
   */
  @Nonnull
  public static IWritableResource getWritableResource (@Nonnull final String sFilename)
  {
    if (sFilename == null)
      throw new NullPointerException ("fileName");

    return s_aResourceProvider.getWritableResource (sFilename);
  }

  /**
   * Resolve a resource using the resource provider and convert it than to a
   * file.
   * 
   * @param sFilename
   *        The file name to be resolved.
   * @return <code>null</code> if the passed file name cannot be converted to a
   *         {@link File}.
   */
  @Nullable
  public static File getReadableFile (@Nonnull final String sFilename)
  {
    final IReadableResource aResource = getReadableResource (sFilename);
    try
    {
      // should throw NPE if file does not exist
      final File aFile = aResource.getAsFile ();
      if (aFile.exists () && !aFile.canRead ())
        s_aLogger.warn ("Cannot read the desired file " + aFile);
      return aFile;
    }
    catch (final Exception ex)
    {
      s_aLogger.error ("Failed to convert " + aResource + " to a file", ex);
      return null;
    }
  }

  /**
   * Resolve a resource using the resource provider and convert it than to a
   * file.
   * 
   * @param sFilename
   *        The file name to be resolved.
   * @return <code>null</code> if the passed file name cannot be converted to a
   *         {@link File}.
   */
  @Nullable
  public static File getWritableFile (@Nonnull final String sFilename)
  {
    final IWritableResource aResource = getWritableResource (sFilename);
    try
    {
      // should throw NPE if file does not exist
      final File aFile = aResource.getAsFile ();
      if (aFile.exists () && !aFile.canWrite ())
        s_aLogger.warn ("Cannot write the desired file " + aFile);
      return aFile;
    }
    catch (final Exception ex)
    {
      s_aLogger.error ("Failed to convert " + aResource + " to file", ex);
      return null;
    }
  }

  @Nullable
  public static File getDirectoryFile (@Nonnull final String sFilename)
  {
    final File aFile = getWritableFile (sFilename);
    if (aFile == null)
      return null;
    if (aFile.exists () && !aFile.isDirectory ())
    {
      s_aLogger.warn ("You tried to retrieve the non-directory " + aFile + " as a directory - wont work!");
      return null;
    }
    return aFile;
  }

  public static boolean resourceExists (@Nonnull final String sFilename)
  {
    return getReadableResource (sFilename).exists ();
  }

  @Nonnull
  public static ISuccessIndicator deleteFile (@Nonnull final String sFilename)
  {
    return deleteFile (getWritableFile (sFilename));
  }

  @Nonnull
  public static ISuccessIndicator deleteFile (@Nonnull final File aFile)
  {
    return s_aFileOpMgr.deleteFile (aFile);
  }

  @Nonnull
  public static ISuccessIndicator deleteFileIfExisting (@Nonnull final String sFilename)
  {
    return deleteFileIfExisting (getWritableFile (sFilename));
  }

  @Nonnull
  public static ISuccessIndicator deleteFileIfExisting (@Nonnull final File aFile)
  {
    return s_aFileOpMgr.deleteFileIfExisting (aFile);
  }

  @Nonnull
  public static ISuccessIndicator deleteDirectory (@Nonnull final File fDir, final boolean bDeleteContent)
  {
    return bDeleteContent ? s_aFileOpMgr.deleteDirRecursive (fDir) : s_aFileOpMgr.deleteDir (fDir);
  }

  @Nonnull
  public static ISuccessIndicator deleteDirectory (@Nonnull final String sDirName, final boolean bDeleteContent)
  {
    return deleteDirectory (getDirectoryFile (sDirName), bDeleteContent);
  }

  @Nonnull
  public static ISuccessIndicator mkDir (@Nonnull final String sDirName, final boolean bRecursive)
  {
    final File f = getDirectoryFile (sDirName);
    return bRecursive ? s_aFileOpMgr.createDirRecursiveIfNotExisting (f) : s_aFileOpMgr.createDirIfNotExisting (f);
  }

  @Nonnull
  public static ISuccessIndicator renameFile (@Nonnull final String sOldFilename, @Nonnull final String sNewFilename)
  {
    final File fOld = getReadableFile (sOldFilename);
    final File fNew = getWritableFile (sNewFilename);
    return fOld == null ? ESuccess.FAILURE : s_aFileOpMgr.renameFile (fOld, fNew);
  }

  /**
   * Get an output stream for writing to a file.
   * 
   * @param sFilename
   *        the relative filename to write. May not be <code>null</code>.
   * @return <code>null</code> if the file could not be opened
   */
  @Nullable
  public static OutputStream getOutputStream (@Nonnull final String sFilename)
  {
    return getOutputStream (sFilename, EAppend.DEFAULT);
  }

  /**
   * Get an output stream for writing to a file.
   * 
   * @param sFilename
   *        the relative filename to write. May not be <code>null</code>.
   * @param eAppend
   *        Appending mode. May not be <code>null</code>.
   * @return <code>null</code> if the file could not be opened
   */
  @Nullable
  public static OutputStream getOutputStream (@Nonnull final String sFilename, @Nonnull final EAppend eAppend)
  {
    return getWritableResource (sFilename).getOutputStream (eAppend);
  }

  @Nullable
  public static InputStream getInputStream (@Nonnull final String sFilename)
  {
    return getInputStream (sFilename, true);
  }

  @Nullable
  public static InputStream getInputStream (@Nonnull final String sFilename, final boolean bShowWarning)
  {
    final IReadableResource aRes = getReadableResource (sFilename);
    final InputStream aIS = aRes.getInputStream ();
    if (aIS == null && bShowWarning)
      s_aLogger.warn ("File '" + sFilename + "' does not exist; resource = " + aRes.toString ());
    return aIS;
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
  private static ESuccess _writeFile (@Nonnull final String sFilename,
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
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  public static ESuccess saveFile (@Nonnull final String sFilename,
                                   @Nonnull final String sContent,
                                   @Nonnull final String sCharset)
  {
    return saveFile (sFilename, CharsetManager.getAsBytes (sContent, sCharset));
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
  public static ESuccess saveFile (@Nonnull final String sFilename,
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
  public static ESuccess saveFile (@Nonnull final String sFilename, @Nonnull final byte [] aBytes)
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
   * @param sCharset
   *        The character set to use. May not be <code>null</code>.
   * @return {@link ESuccess}
   */
  @Nonnull
  public static ESuccess appendFile (@Nonnull final String sFilename,
                                     @Nonnull final String sContent,
                                     @Nonnull final String sCharset)
  {
    return appendFile (sFilename, CharsetManager.getAsBytes (sContent, sCharset));
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
  public static ESuccess appendFile (@Nonnull final String sFilename,
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
  public static ESuccess appendFile (@Nonnull final String sFilename, @Nonnull final byte [] aBytes)
  {
    return _writeFile (sFilename, EAppend.APPEND, aBytes);
  }
}
