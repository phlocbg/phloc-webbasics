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
package com.phloc.fileupload;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.exceptions.InitializationException;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.io.misc.SizeHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.web.CWebCharset;
import com.phloc.web.fileupload.FileUploadException;
import com.phloc.web.fileupload.IFileItem;
import com.phloc.web.fileupload.IFileItemFactory;
import com.phloc.web.fileupload.IProgressListener;
import com.phloc.web.fileupload.AbstractFileUploadBase.FileSizeLimitExceededException;
import com.phloc.web.fileupload.io.DiskFileItemFactory;
import com.phloc.web.fileupload.servlet.ServletFileUpload;
import com.phloc.web.mock.MockHttpServletRequest;
import com.phloc.webscopes.singleton.GlobalWebSingleton;

public final class FileUploadHelper
{
  /**
   * Wrapper around a {@link DiskFileItemFactory}, that is correctly cleaning
   * up, when the servlet context is destroyed.
   * 
   * @author Boris Gregorcic
   */
  public static final class GlobalNotifyingDiskFileItemFactory extends GlobalWebSingleton implements IFileItemFactory
  {
    private final NotifyingDiskFileItemFactory m_aFactory = new NotifyingDiskFileItemFactory (CGlobal.BYTES_PER_MEGABYTE,
                                                                                              null);

    // ESCA-JAVA0057: needed for reflection
    @UsedViaReflection
    @Deprecated
    public GlobalNotifyingDiskFileItemFactory ()
    {
      // used via reflection
    }

    @Nonnull
    public static GlobalNotifyingDiskFileItemFactory getInstance ()
    {
      return getGlobalSingleton (GlobalNotifyingDiskFileItemFactory.class);
    }

    @Override
    protected void onDestroy ()
    {
      this.m_aFactory.deleteAllTemporaryFiles ();
    }

    public void setRepository (@Nullable final File aRepository)
    {
      this.m_aFactory.setRepository (aRepository);
    }

    @Nonnull
    public IFileItem createItem (final String sFieldName,
                                 final String sContentType,
                                 final boolean bIsFormField,
                                 final String sFileName)
    {
      return this.m_aFactory.createItem (sFieldName, sContentType, bIsFormField, sFileName);
    }

    @Nonnull
    public List <File> getAllTemporaryFiles ()
    {
      return this.m_aFactory.getAllTemporaryFiles ();
    }
  }

  private static final Logger s_aLogger = LoggerFactory.getLogger (FileUploadHelper.class);

  /**
   * The maximum size of a single file (in bytes) that will be handled
   */
  private static final long MAX_REQUEST_SIZE = 5 * CGlobal.BYTES_PER_GIGABYTE;

  private FileUploadHelper ()
  {}

  public static void setTempDirectory (@Nonnull final File fUploadTempDir)
  {
    if (fUploadTempDir == null)
    {
      throw new NullPointerException ("uploadTempDir"); //$NON-NLS-1$
    }
    if (!fUploadTempDir.exists ())
    {
      if (fUploadTempDir.mkdirs ())
      {
        s_aLogger.info ("Created upload temp directory " + fUploadTempDir); //$NON-NLS-1$
      }
      else
      {
        throw new InitializationException ("Failed to create upload temp directory " + //$NON-NLS-1$
                                           fUploadTempDir);
      }
    }
    GlobalNotifyingDiskFileItemFactory.getInstance ().setRepository (fUploadTempDir);
  }

  @Nonnegative
  public static long getMaxRequestSize ()
  {
    return MAX_REQUEST_SIZE;
  }

  /**
   * Check if the parsed request is a multi part request, potentially containing
   * uploaded files.
   * 
   * @param aHttpRequest
   *        The non-<code>null</code> HTTP request.
   * @return <code>true</code> if the passed request is a multi part request
   */
  public static boolean isMultipartContent (@Nonnull final HttpServletRequest aHttpRequest)
  {
    // The instanceof check is required for the check whether it is a FileUpload
    // request or not
    return !(aHttpRequest instanceof MockHttpServletRequest) && ServletFileUpload.isMultipartContent (aHttpRequest);
  }

  /**
   * This method parses an HTTP request and analyzes it for FILE uploads.
   * 
   * @param aHttpRequest
   *        The request to scan. May not be <code>null</code>.
   * @param aProgressListener
   *        An optional progress listener to be used. May be <code>null</code> .
   * @return An object having the appropriate access methods.
   * @throws FileUploadException
   *         if there are problems reading/parsing the request or storing files.
   * @see ServletFileUpload#parseRequest(HttpServletRequest)
   */
  @Nonnull
  public static FileUploadRequest getUploadedFilesList (@Nonnull final HttpServletRequest aHttpRequest,
                                                        @Nullable final IProgressListener aProgressListener) throws FileUploadException
  {
    // Setup the ServletFileUpload....
    final ServletFileUpload aUpload = new ServletFileUpload (GlobalNotifyingDiskFileItemFactory.getInstance ());
    aUpload.setSizeMax (MAX_REQUEST_SIZE);
    if (aProgressListener != null)
    {
      aUpload.setProgressListener (aProgressListener);
    }
    aUpload.setHeaderEncoding (CWebCharset.CHARSET_MULTIPART);
    try
    {
      aHttpRequest.setCharacterEncoding (CWebCharset.CHARSET_REQUEST);
    }
    catch (final UnsupportedEncodingException ex)
    {
      s_aLogger.error ("Failed to set request character encoding to '" + //$NON-NLS-1$
                       CWebCharset.CHARSET_REQUEST +
                       "'", ex); //$NON-NLS-1$
    }
    try
    {
      final List <IFileItem> aFileItems = aUpload.parseRequest (aHttpRequest);
      return new FileUploadRequest (aFileItems);
    }
    catch (final FileSizeLimitExceededException ex)
    {
      // Catch exception because it is relevant for a user
      // Note: this exception is only triggered, once the maximum file size is
      // uploaded -> causes unnecessary data transfer!
      final FileUploadProgressListener aListener = FileUploadProgressListener.getInstance ();
      aListener.setFailed (EFileUploadText.ERROR_FILESIZE_EXCEEDED.getStringPropertyName (),
                           EFileUploadText.ERROR_FILESIZE_EXCEEDED,
                           aListener.getFilename (),
                           SizeHelper.getSizeHelperOfLocale (Locale.US).getAsMatching (getMaxRequestSize (), 2));
      throw ex;
    }
  }

  /**
   * Save a FileItem to disk. If a file with the same name already exists, a
   * unique file name is created!
   * 
   * @param aFileItem
   *        The file item to use. May not be <code>null</code>.
   * @param aBaseDir
   *        The base directory to store to. May not be <code>null</code>.
   * @param sBaseName
   *        The base name of the target file (without the extension). May not be
   *        <code>null</code> .
   * @param sExt
   *        The file extension to use, including the leading '.'. May not be
   *        <code>null</code>.
   * @return The written file object, or <code>null</code> if the passed file
   *         item is <code>null</code>, if it is a form field or if an error
   *         occurred while writing the file item to disk.
   */
  @Nullable
  public static File saveUploadedFile (@Nullable final IFileItem aFileItem,
                                       @Nonnull final File aBaseDir,
                                       @Nonnull final String sBaseName,
                                       @Nonnull final String sExt)
  {
    if (aBaseDir == null)
    {
      throw new NullPointerException ("baseDir"); //$NON-NLS-1$
    }
    if (sBaseName == null)
    {
      throw new NullPointerException ("baseName"); //$NON-NLS-1$
    }
    if (sExt == null)
    {
      throw new NullPointerException ("extension"); //$NON-NLS-1$
    }
    try
    {
      // Anything to upload?
      if (aFileItem != null && !aFileItem.isFormField ())
      {
        // all data is saved in the directory "email"
        if (!aBaseDir.exists () && !aBaseDir.mkdirs ())
        {
          s_aLogger.error ("Failed to create directory '" + sBaseName + "'"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        else
        {
          // make a unique filename by appending a numeric index
          File aFileToSaveTo = new File (aBaseDir, sBaseName + FilenameHelper.EXTENSION_SEPARATOR + sExt);
          int nUniqueIndex = 1;
          while (aFileToSaveTo.exists ())
          {
            aFileToSaveTo = new File (aBaseDir, sBaseName +
                                                " (" + nUniqueIndex + ")" + FilenameHelper.EXTENSION_SEPARATOR + sExt); //$NON-NLS-1$ //$NON-NLS-2$
            ++nUniqueIndex;
          }

          // write and by
          aFileItem.write (aFileToSaveTo);
          return aFileToSaveTo;
        }
      }
    }
    catch (final Exception ex)
    {
      s_aLogger.error ("Error saving uploaded file", ex); //$NON-NLS-1$
    }
    return null;
  }

  /**
   * For Internet Explorer we need to make it a relative filename since it
   * always delivers the full filename.<br>
   * http://commons.apache.org/fileupload/faq.html#whole-path-from-IE
   * 
   * @param aFileItem
   *        The file item to get unified
   * @return the file name with fixed encoding and without path.
   */
  @Nonnull
  public static String getUnifiedFilename (@Nonnull final IFileItem aFileItem)
  {
    if (aFileItem == null)
    {
      throw new NullPointerException ("fileItem"); //$NON-NLS-1$
    }

    final String sFileName = aFileItem.getName ();
    if (StringHelper.hasNoText (sFileName))
    {
      // Happens if no file is selected in an input field
      return sFileName;
    }

    // Skip any path!
    final String ret = FileUtils.getSecureFile (new File (sFileName)).getName ();
    if (!sFileName.equals (ret))
    {
      // Log only if something changed
      s_aLogger.info ("Original file name: " + sFileName); //$NON-NLS-1$
      s_aLogger.info ("Unified file name:  " + ret); //$NON-NLS-1$
    }
    return ret;
  }
}
