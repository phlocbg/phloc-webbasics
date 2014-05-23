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
package com.phloc.fileupload.handler;

import java.io.File;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.io.file.FileIOError;
import com.phloc.commons.io.file.FileOperations;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.io.misc.SizeHelper;
import com.phloc.commons.lang.CGStringHelper;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.StringHelper;
import com.phloc.fileupload.EFileUploadText;
import com.phloc.fileupload.FileUploadHelper;
import com.phloc.fileupload.FileUploadProgressListener;
import com.phloc.fileupload.IUploadPostProcessor;
import com.phloc.fileupload.UploadPostProcessingResult;
import com.phloc.web.fileupload.FileUploadException;
import com.phloc.web.fileupload.IFileItem;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

@Immutable
public final class UploadHandler
{
  public static final String PARAM_UPLOAD_ID = "uploadID";

  private static final Logger s_aLogger = LoggerFactory.getLogger (UploadHandler.class);

  private UploadHandler ()
  {}

  /**
   * Called after successful external validation
   * 
   * @param aItem
   *        Uploaded file item
   * @param aContext
   *        The non-<code>null</code> Upload context
   * @return The file which has been written in on the server, or
   *         <code>null</code> in case of an error
   * @throws FileUploadException
   */
  @Nullable
  private static File _handleUploadFileItem (@Nonnull final IFileItem aItem, @Nonnull final UploadContext aContext) throws FileUploadException
  {
    final String sFieldName = aItem.getFieldName ();
    final String sSourceFileName = aItem.getName ();
    final String sContentType = aItem.getContentType ();
    final boolean bInMemory = aItem.isInMemory ();
    final long nSizeInBytes = aItem.getSize ();
    File aUploadedFile = null;

    s_aLogger.info ("Processing file item [field:" +
                    sFieldName +
                    ", file:" +
                    sSourceFileName +
                    ", contenttype:" +
                    sContentType +
                    ", inmemory:" +
                    bInMemory +
                    ", size:" +
                    nSizeInBytes +
                    (aContext.getPostProcessor () == null ? ""
                                                         : ", post processor " +
                                                           CGStringHelper.getClassLocalName (aContext.getPostProcessor ())) +
                    "]");

    // Validate the original filename
    final IUploadFilenameFilter aFilter = aContext.getFilenameFilter ();
    if (aFilter != null && !aFilter.matchesFilter (sSourceFileName))
    {
      FileUploadProgressListener.getInstance ().setFailed (aFilter.getErrorCode (),
                                                           aFilter.getErrorMessage (),
                                                           ArrayHelper.newArray (aFilter.getErrorArguments (),
                                                                                 String.class));
      final String sMsg = "Uploaded file '" + sSourceFileName + "' does not satisfy the active filename filter!"; //$NON-NLS-2$
      s_aLogger.warn (sMsg);
      throw new FileUploadException (sMsg);
    }

    final IUploadFileSizeFilter aSizeFilter = aContext.getFileSizeFilter ();
    if (aSizeFilter != null && !aSizeFilter.matchesFilter (Long.valueOf (nSizeInBytes)))
    {
      FileUploadProgressListener.getInstance ()
                                .setFailed (EFileUploadText.ERROR_FILESIZE_EXCEEDED.getStringPropertyName (),
                                            EFileUploadText.ERROR_FILESIZE_EXCEEDED,
                                            sSourceFileName,
                                            SizeHelper.getSizeHelperOfLocale (Locale.US)
                                                      .getAsMatching (aSizeFilter.getMaxBytes (), 2));
      final String sMsg = "Uploaded file '" + sSourceFileName + "' does not satisfy the maximum file size filter!"; //$NON-NLS-2$
      s_aLogger.warn (sMsg);
      throw new FileUploadException (sMsg);
    }

    // Size may be 0 if no file was selected
    if (nSizeInBytes > 0)
    {
      // Set the post processor only, if a file is present, to avoid endless
      // loops on "post processing file ..."
      final FileUploadProgressListener aProgListener = FileUploadProgressListener.getInstance ();
      final IUploadPostProcessor aPostProcessor = aContext.getPostProcessor ();
      if (aPostProcessor != null)
      {
        aProgListener.setPostProcessor (aPostProcessor);
      }
      // Ensure the file does not contain any harmful characters etc.
      final String sFileName = FileUploadHelper.getUnifiedFilename (aItem);
      String sTargetFileName = aContext.getTargetFileName ();
      if (StringHelper.hasNoText (sTargetFileName))
      {
        sTargetFileName = sFileName;
      }
      final String sTargetDir = FilenameHelper.getAbsoluteWithEnsuredParentDirectory (aContext.getUploadDirectory (),
                                                                                      FilenameHelper.getPath (sTargetFileName));

      // This is the main copy action
      aUploadedFile = FileUploadHelper.saveUploadedFile (aItem,
                                                         new File (sTargetDir),
                                                         FilenameHelper.getBaseName (sTargetFileName),
                                                         FilenameHelper.getExtension (sTargetFileName));

      if (aUploadedFile == null)
      {
        // Something went wrong in copying
        s_aLogger.error ("Upload failed.");
      }
      else
      {
        s_aLogger.info ("Upload completed successfully to destination " + aUploadedFile.getAbsolutePath ());
        if (aPostProcessor != null)
        {
          s_aLogger.info ("Starting post processing...");
          try
          {
            aProgListener.setPostProcessing ();
            final UploadPostProcessingResult aResult = aPostProcessor.performPostProcessing (aUploadedFile,
                                                                                             sFileName,
                                                                                             aContext.getProperties ());
            aProgListener.setPostProcessorResult (aResult);
          }
          catch (final RuntimeException ex)
          {
            // Catch any exception
            s_aLogger.error ("Internal error in post processing", ex);
            aProgListener.setPostProcessorResult (new UploadPostProcessingResult (ESuccess.FAILURE,
                                                                                  EFileUploadText.ERROR_POST_PROCESSING,
                                                                                  EFileUploadText.ERROR_POST_PROCESSING.getStringPropertyName (),
                                                                                  null));
          }
        }
      }
    }
    else
    {
      FileUploadProgressListener.getInstance ()
                                .setFailed (EFileUploadText.ERROR_NO_FILE_OR_EMPTY.getStringPropertyName (),
                                            EFileUploadText.ERROR_NO_FILE_OR_EMPTY);
      final String sMsg = "No file or empty file selected: '" + sSourceFileName + "'!"; //$NON-NLS-2$
      s_aLogger.warn (sMsg);
      throw new FileUploadException (sMsg);
    }
    return aUploadedFile;
  }

  /**
   * Call this method as the first action in your servlet, to ensure that the
   * files are technically handled, before your business logic takes places.
   * This method should only be invoked for POST calls.
   * 
   * @param aRequestScope
   *        Current request scope
   * @throws FileUploadException
   */
  public static void handleUpload (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope) throws FileUploadException
  {
    s_aLogger.info ("handleUpload");
    // get the upload ID that was sent via a hidden field from the upload frame
    final String sUploadID = aRequestScope.getAttributeAsString (PARAM_UPLOAD_ID);
    if (StringHelper.hasNoText (sUploadID))
    {
      FileUploadProgressListener.getInstance ().reset ();
      throw new FileUploadException ("Unable to retrieve upload ID for received request!");
    }

    // Check if we have the matching upload context
    final UploadContext aContext = UploadContextRegistry.getInstance ().getContext (sUploadID);
    if (aContext == null)
    {
      FileUploadProgressListener.getInstance ().reset ();
      throw new FileUploadException ("Unable to retrieve upload context for received request with ID '" +
                                     sUploadID +
                                     "'!");
    }
    // Ensure that the upload directory exists
    final File aUploadDir = aContext.getUploadDirectory ();
    if (!aUploadDir.exists ())
    {
      final FileIOError aErr = FileOperations.createDirRecursive (aUploadDir);
      if (aErr.isFailure ())
      {
        FileUploadProgressListener.getInstance ().reset ();
        throw new FileUploadException ("Failed to create upload directory " + aUploadDir + ": " + aErr.toString ()); //$NON-NLS-2$
      }
    }

    for (final IFileItem aFileItem : aRequestScope.getAllUploadedFileItemValues ())
    {
      final File aUploadedFile = _handleUploadFileItem (aFileItem, aContext);
      if (aUploadedFile != null)
      {
        FileUploadProgressListener.getInstance ()
                                  .setFileName (FilenameHelper.getRelativeToParentDirectory (aUploadedFile, aUploadDir));

      }
    }
    FileUploadProgressListener.getInstance ().setUploadFinished ();
  }
}
