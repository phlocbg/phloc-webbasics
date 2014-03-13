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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.misc.SizeHelper;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.state.ISuccessIndicator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.impl.TextFormatter;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.json.IJSONObject;
import com.phloc.json.impl.JSONObject;
import com.phloc.web.fileupload.IFileItem;
import com.phloc.web.fileupload.IProgressListener;
import com.phloc.webscopes.singleton.SessionApplicationWebSingleton;

/**
 * The default file upload progress listener, used in every multi part request.
 * Always access it via {@link #getInstance()}.
 * 
 * @author Boris Gregorcic
 */
public final class FileUploadProgressListener extends SessionApplicationWebSingleton implements IProgressListener, ISuccessIndicator
{
  public static final String RESPONSE_ELEMENT_RESPONSE = "response";
  public static final String RESPONSE_ELEMENT_DESCRIPTION = "description";
  public static final String RESPONSE_ELEMENT_PERCENT_COMPLETE = "percent_complete";
  public static final String RESPONSE_ELEMENT_BYTES_READ = "bytes_read";
  public static final String RESPONSE_ELEMENT_FILENAME = "filename";
  public static final String RESPONSE_ELEMENT_ITEMINDEX = "itemindex";
  public static final String RESPONSE_ELEMENT_CONTENT_LENGTH = "content_length";
  public static final String RESPONSE_ELEMENT_ABORTED = "aborted";
  public static final String RESPONSE_ELEMENT_FAILURE = "failure";
  public static final String RESPONSE_ELEMENT_SUCCESS = "success";
  public static final String RESPONSE_ELEMENT_POSTPROCESSING = "postprocessing";
  public static final String RESPONSE_ELEMENT_FINISHED = "finished";
  public static final String RESPONSE_ELEMENT_TEXT = "text";
  public static final String RESPONSE_ELEMENT_CODE = "code";
  public static final String RESPONSE_ELEMENT_PARAMS = "params";

  private static final long serialVersionUID = 6500049544627732305L;

  private static final Logger s_aLogger = LoggerFactory.getLogger (FileUploadProgressListener.class);

  // Counter used for indicating progress after 100% was reached (by appending
  // "..." to progress message)
  private static final int ALTERNATING_COUNTER_SIZE = 3;

  // Maximum number of initialization tries before the upload is aborted
  private static final int MAX_INIT_COUNT = 25;

  private volatile long m_nBytesRead;
  private volatile long m_nContentLength;
  private volatile int m_nItemIndex;
  private int m_nInitCount;
  private String m_sFilename;
  private IHasDisplayText m_aError;
  private String [] m_aErrorArgs;
  private String m_sErrorCode;
  private IUploadPostProcessor m_aPostProcessor;
  private UploadPostProcessingResult m_aPostProcessingResult;
  private int m_nAlternatingCounter = 0;
  private boolean m_bUploadAborted = false;
  private boolean m_bUploadFinished = false;
  private boolean m_bPostProcessing = false;
  private ISimpleURL m_aAbortRedirect = null;
  private char m_cProcessingCharPlaceholder = ' ';
  private char m_cProcessingCharTaken = '.';

  @UsedViaReflection
  @Deprecated
  public FileUploadProgressListener ()
  {
    reset ();
  }

  @Nonnull
  public static FileUploadProgressListener getInstance ()
  {
    return getSessionApplicationSingleton (FileUploadProgressListener.class);
  }

  public void setPostProcessor (@Nullable final IUploadPostProcessor aPostProcessor)
  {
    m_aPostProcessor = aPostProcessor;
  }

  public void setPostProcessorResult (@Nullable final UploadPostProcessingResult aPostProcessingResult)
  {
    m_aPostProcessingResult = aPostProcessingResult;
    if (aPostProcessingResult == null || aPostProcessingResult.isFailure ())
    {
      setUploadFinished ();
    }
  }

  public void setUploadAborted (@Nullable final ISimpleURL aRedirect)
  {
    m_bUploadAborted = true;
    m_aAbortRedirect = aRedirect;
  }

  public void setUploadFinished ()
  {
    m_bUploadFinished = true;
  }

  public void setPostProcessing ()
  {
    m_bPostProcessing = true;
  }

  public void setCustomProcessingChars (final char cTaken, final char cPlaceholder)
  {
    m_cProcessingCharPlaceholder = cPlaceholder;
    m_cProcessingCharTaken = cTaken;
  }

  @Override
  public void update (final long nBytesRead, final long nContentLength, final int nItem)
  {
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("update (" + nItem + ": " + nBytesRead + " / " + nContentLength + ")");
    m_nBytesRead = nBytesRead;
    m_nContentLength = nContentLength;
    m_nItemIndex = nItem;
  }

  void setFileItem (final IFileItem aFileItem)
  {
    m_nBytesRead = 0L;
    m_nContentLength = 0L;
    m_sFilename = FileUploadHelper.getUnifiedFilename (aFileItem);
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("set file item (" + m_sFilename + ")");
    m_nInitCount = 0;
    m_aError = null;
    m_aErrorArgs = null;
    m_sErrorCode = null;
    m_aPostProcessingResult = null;
    m_aPostProcessor = null;
    m_bPostProcessing = false;
  }

  /**
   * CAn be used to set a custom file name. This is especially useful when the
   * file stored on the server has been renamed to avoid collisions and is
   * therefore different from the original file item name.
   * 
   * @param sFileName
   */
  public void setFileName (final String sFileName)
  {
    m_sFilename = FileUtils.getSecureFile (new File (sFileName)).getName ();
  }

  public String getFilename ()
  {
    return m_sFilename;
  }

  @Override
  public boolean isSuccess ()
  {
    return m_aError == null;
  }

  @Override
  public boolean isFailure ()
  {
    return m_aError != null;
  }

  public void setFailed (@Nonnull final String sErrorCode,
                         @Nonnull final IHasDisplayText aError,
                         @Nullable final String... aErrorArgs)
  {
    if (sErrorCode == null)
    {
      throw new NullPointerException ("sErrorCode");
    }
    if (aError == null)
    {
      throw new NullPointerException ("error");
    }
    m_sErrorCode = sErrorCode;
    m_aError = aError;
    m_aErrorArgs = ArrayHelper.getCopy (aErrorArgs);
    setUploadFinished ();
  }

  public void reset ()
  {
    m_nBytesRead = 0L;
    m_nContentLength = 0L;
    m_nItemIndex = 0;
    m_nInitCount = 0;
    m_sFilename = "";
    m_sErrorCode = null;
    m_aError = null;
    m_aErrorArgs = null;
    m_aPostProcessingResult = null;
    m_aPostProcessor = null;
    m_bPostProcessing = false;
    m_nAlternatingCounter = 0;
    m_bUploadAborted = false;
    m_bUploadFinished = false;
  }

  private void _increaseAlternatingCounter ()
  {
    m_nAlternatingCounter++;
    if (m_nAlternatingCounter > ALTERNATING_COUNTER_SIZE)
    {
      m_nAlternatingCounter = 0;
    }
  }

  private static IJSONObject _createMessage (final String sDefaultMessate,
                                             final String sMessageCode,
                                             final List <String> aParams)
  {
    final IJSONObject aMessage = new JSONObject ();
    aMessage.setStringProperty (RESPONSE_ELEMENT_TEXT, sDefaultMessate);
    aMessage.setStringProperty (RESPONSE_ELEMENT_CODE, sMessageCode);
    final IJSONObject aParamObject = new JSONObject ();
    if (aParams != null)
    {
      for (int i = 0; i < aParams.size (); i++)
      {
        aParamObject.setStringProperty (String.valueOf (i), aParams.get (i));
      }
    }
    aMessage.setObjectProperty (RESPONSE_ELEMENT_PARAMS, aParamObject);
    return aMessage;
  }

  @Nonnull
  public IJSONObject getStatusJSON (@Nonnull final Locale aDisplayLocale)
  {
    final IJSONObject aStatus = new JSONObject ();
    boolean bReset = false;
    aStatus.setIntegerProperty (RESPONSE_ELEMENT_ITEMINDEX, m_nItemIndex);
    aStatus.setStringProperty (RESPONSE_ELEMENT_FILENAME, m_sFilename);
    aStatus.setLongProperty (RESPONSE_ELEMENT_BYTES_READ, m_nBytesRead);
    aStatus.setLongProperty (RESPONSE_ELEMENT_CONTENT_LENGTH, m_nContentLength);
    aStatus.setBooleanProperty (RESPONSE_ELEMENT_POSTPROCESSING, m_bPostProcessing);

    if (m_bUploadAborted)
    {
      aStatus.setStringProperty (RESPONSE_ELEMENT_ABORTED,
                                 m_aAbortRedirect == null ? "" : m_aAbortRedirect.getAsStringWithEncodedParameters ());
      s_aLogger.error ("Reporting upload aborted!");
      bReset = true;
    }
    else
    {
      String sDescription = "";
      String sDescriptionCode = "";
      final List <String> aDescriptionParams = new ArrayList <String> ();

      // Check to see if we're done

      if (m_bUploadFinished || m_bPostProcessing)
      {
        if (m_aError != null)
        {
          _setError (aStatus, aDisplayLocale);
          bReset = true;
        }
        // check if we have a post processing tasks to do
        else
          if (m_aPostProcessor != null &&
              (!m_bUploadFinished && m_aPostProcessingResult == null || m_aPostProcessingResult.isFailure ()))
          {
            // post processing is triggered by UploadHandler, we just use it for
            // displaying the message...
            if (m_aPostProcessingResult == null)
            {
              // post processing still running
              sDescription = m_aPostProcessor.getProgressMessage ().getDisplayText (aDisplayLocale) +
                             StringHelper.getRepeated (m_cProcessingCharTaken, m_nAlternatingCounter) +
                             StringHelper.getRepeated (m_cProcessingCharPlaceholder, ALTERNATING_COUNTER_SIZE -
                                                                                     m_nAlternatingCounter);
              _increaseAlternatingCounter ();
            }
            else
            {
              // this must be the error case
              aStatus.setObjectProperty (RESPONSE_ELEMENT_FAILURE,
                                         _createMessage (m_aPostProcessingResult.getDisplayText (aDisplayLocale),
                                                         m_aPostProcessingResult.getResultCode (),
                                                         m_aPostProcessingResult.getResultArgs ()));
              s_aLogger.error ("Reporting upload failed due to post processing error!");
              bReset = true;
            }
          }
          else
          {
            // We're done -> marker node
            aStatus.setBooleanProperty (RESPONSE_ELEMENT_FINISHED, true);

            // Success message
            final StringBuilder aSuccessMsg = new StringBuilder ();
            String sSuccessCode = null;
            final List <String> aSuccessParams = new ArrayList <String> ();

            if (m_aPostProcessingResult != null && m_aPostProcessingResult.isSuccess ())
            {
              if (!m_aPostProcessingResult.isReplaceMainSuccessMessage ())
              {
                aSuccessMsg.append (EFileUploadText.PROGRESS_MSG_SUCCESS.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                 m_sFilename));
                sSuccessCode = EFileUploadText.PROGRESS_MSG_SUCCESS.getStringPropertyName ();
                aSuccessParams.add (m_sFilename);
              }
              final String sPostPorcessingMsg = m_aPostProcessingResult.getDisplayText (aDisplayLocale);
              if (StringHelper.hasText (sPostPorcessingMsg))
              {
                if (!m_aPostProcessingResult.isReplaceMainSuccessMessage ())
                {
                  aSuccessMsg.append ("\n");
                }
                sSuccessCode = m_aPostProcessingResult.getResultCode ();
                aSuccessParams.clear ();
                aSuccessParams.addAll (m_aPostProcessingResult.getResultArgs ());
                aSuccessMsg.append (sPostPorcessingMsg);
              }
            }
            else
            {
              aSuccessMsg.append (EFileUploadText.PROGRESS_MSG_SUCCESS.getDisplayTextWithArgs (aDisplayLocale,
                                                                                               m_sFilename));
              sSuccessCode = EFileUploadText.PROGRESS_MSG_SUCCESS.getStringPropertyName ();
              aSuccessParams.add (m_sFilename);
            }
            aStatus.setObjectProperty (RESPONSE_ELEMENT_SUCCESS,
                                       _createMessage (aSuccessMsg.toString (), sSuccessCode, aSuccessParams));

            // Description text
            sDescription = EFileUploadText.PROGRESS_DESC_DONE.getDisplayText (aDisplayLocale);
            sDescriptionCode = EFileUploadText.PROGRESS_DESC_DONE.getStringPropertyName ();

            // Reset status
            bReset = true;
          }
        // ESCA-JAVA0076: 100%
        aStatus.setLongProperty (RESPONSE_ELEMENT_PERCENT_COMPLETE, 100);
      }
      else
      {
        // Calculate the percent complete
        final long nPercentComplete = (m_nContentLength <= 0) ? 0 : ((100 * m_nBytesRead) / m_nContentLength);
        aStatus.setLongProperty (RESPONSE_ELEMENT_PERCENT_COMPLETE, nPercentComplete);
        if (m_nContentLength == 0 && nPercentComplete == 0)
        {
          m_nInitCount++;
          s_aLogger.info ("Initializing file upload [try " + m_nInitCount + "]"); //$NON-NLS-2$
          sDescription = EFileUploadText.PROGRESS_DESC_INIT.getDisplayText (aDisplayLocale);
          sDescriptionCode = EFileUploadText.PROGRESS_DESC_INIT.getStringPropertyName ();

          // Count the number of initializations, because in some case (e.g.
          // files > 4GB)
          // the browser will not transmit anything to the client
          // So to avoid having an endless poll, after a certain number of
          // "init" messages, the progress is stopped.
          if (m_nInitCount >= MAX_INIT_COUNT)
          {
            setFailed (EFileUploadText.ERROR_NO_MULTIPART_REQUEST.getStringPropertyName (),
                       EFileUploadText.ERROR_NO_MULTIPART_REQUEST,
                       Integer.toString (m_nInitCount));
          }
        }
        else
        {
          final boolean bUnknownSize = m_nContentLength == -1;

          final SizeHelper aSH = SizeHelper.getSizeHelperOfLocale (aDisplayLocale);
          final String sBytesRead = aSH.getAsMatching (m_nBytesRead, 2);
          final String sBytesTotal = bUnknownSize ? "" : aSH.getAsMatching (m_nContentLength, 2);
          if (bUnknownSize)
          {
            sDescription = EFileUploadText.PROGRESS_DESC_PROGRESS_UNKNOWN.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                  sBytesRead);
          }
          else
          {
            sDescription = EFileUploadText.PROGRESS_DESC_PROGRESS.getDisplayTextWithArgs (aDisplayLocale,
                                                                                          sBytesRead,
                                                                                          sBytesTotal);
          }
          sDescriptionCode = EFileUploadText.PROGRESS_DESC_PROGRESS.getStringPropertyName ();
          aDescriptionParams.add (sBytesRead);
          aDescriptionParams.add (sBytesTotal);
          aDescriptionParams.add (String.valueOf (nPercentComplete));
          m_nInitCount = 0;
        }
      }

      if (m_aError != null)
      {
        _setError (aStatus, aDisplayLocale);
        bReset = true;
      }
      // Finally the description element
      aStatus.setObjectProperty (RESPONSE_ELEMENT_DESCRIPTION,
                                 _createMessage (sDescription, sDescriptionCode, aDescriptionParams));
    }
    if (bReset)
    {
      reset ();
    }
    return aStatus;
  }

  private void _setError (final IJSONObject aStatus, final Locale aDisplayLocale)
  {
    // an error happened
    String sError = m_aError.getDisplayText (aDisplayLocale);
    if (!ArrayHelper.isEmpty (m_aErrorArgs))
    {
      // ESCA-JAVA0082: needed here
      sError = TextFormatter.getFormattedText (sError, (Object []) m_aErrorArgs);
    }
    aStatus.setObjectProperty (RESPONSE_ELEMENT_FAILURE,
                               _createMessage (sError, m_sErrorCode, ContainerHelper.newList (m_aErrorArgs)));
    s_aLogger.error ("Reporting upload failed due to upload error!");
  }

  @Nonnull
  public IMicroDocument getStatusDoc (@Nonnull final Locale aDisplayLocale)
  {
    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eResponse = aDoc.appendElement (RESPONSE_ELEMENT_RESPONSE);
    eResponse.appendElement (RESPONSE_ELEMENT_ITEMINDEX).appendText (Integer.toString (m_nItemIndex));
    eResponse.appendElement (RESPONSE_ELEMENT_FILENAME).appendText (m_sFilename);
    eResponse.appendElement (RESPONSE_ELEMENT_BYTES_READ).appendText (Long.toString (m_nBytesRead));
    eResponse.appendElement (RESPONSE_ELEMENT_CONTENT_LENGTH).appendText (Long.toString (m_nContentLength));
    eResponse.appendElement (RESPONSE_ELEMENT_POSTPROCESSING).appendText (String.valueOf (m_bPostProcessing));
    if (m_bUploadAborted)
    {
      eResponse.appendElement (RESPONSE_ELEMENT_ABORTED)
               .appendText (m_aAbortRedirect == null ? "" : m_aAbortRedirect.getAsStringWithEncodedParameters ());
      s_aLogger.error ("Reporting upload aborted!");
      reset ();
    }
    else
    {
      String sDescription = "";

      // Check to see if we're done
      if (m_nBytesRead == m_nContentLength && m_nContentLength > 0)
      {
        if (m_aError != null)
        {
          // an error happened
          String sError = m_aError.getDisplayText (aDisplayLocale);
          if (!ArrayHelper.isEmpty (m_aErrorArgs))
          {
            sError = TextFormatter.getFormattedText (sError, (Object) m_aErrorArgs);
          }
          eResponse.appendElement (RESPONSE_ELEMENT_FAILURE).appendText (sError);
          s_aLogger.error ("Reporting upload failed due to upload error!");
          reset ();
        }
        // check if we have a post processing tasks to do
        if (m_aPostProcessor != null && (m_aPostProcessingResult == null || m_aPostProcessingResult.isFailure ()))
        {
          // post processing is triggered by UploadHandler, we just use it for
          // displaying the message...
          if (m_aPostProcessingResult == null)
          {
            // post processing still running
            sDescription = m_aPostProcessor.getProgressMessage ().getDisplayText (aDisplayLocale) +
                           StringHelper.getRepeated ('.', m_nAlternatingCounter);
            _increaseAlternatingCounter ();
          }
          else
          {
            // this must be the error case
            eResponse.appendElement (RESPONSE_ELEMENT_FAILURE)
                     .appendText (m_aPostProcessingResult.getDisplayText (aDisplayLocale));
            s_aLogger.error ("Reporting upload failed due to post processing error!");
            reset ();
          }
        }
        else
        {
          // We're done -> marker node
          eResponse.appendElement (RESPONSE_ELEMENT_FINISHED);

          // Success message
          final IMicroElement eSuccess = eResponse.appendElement (RESPONSE_ELEMENT_SUCCESS);

          if (m_aPostProcessingResult != null && m_aPostProcessingResult.isSuccess ())
          {
            if (!m_aPostProcessingResult.isReplaceMainSuccessMessage ())
            {
              eSuccess.appendText (EFileUploadText.PROGRESS_MSG_SUCCESS.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                m_sFilename));
            }
            final String sPostPorcessingMsg = m_aPostProcessingResult.getDisplayText (aDisplayLocale);
            if (StringHelper.hasText (sPostPorcessingMsg))
            {
              if (!m_aPostProcessingResult.isReplaceMainSuccessMessage ())
              {
                eSuccess.appendText ("\n");
              }
              eSuccess.appendText (sPostPorcessingMsg);
            }
          }
          else
          {
            eSuccess.appendText (EFileUploadText.PROGRESS_MSG_SUCCESS.getDisplayTextWithArgs (aDisplayLocale,
                                                                                              m_sFilename));
          }
          // Description text
          sDescription = EFileUploadText.PROGRESS_DESC_DONE.getDisplayText (aDisplayLocale);

          s_aLogger.info ("Reporting upload finished successfully!");
          // Reset status
          reset ();
        }
        eResponse.appendElement (RESPONSE_ELEMENT_PERCENT_COMPLETE).appendText ("100");
      }
      else
      {
        // Calculate the percent complete
        final long nPercentComplete = (m_nContentLength <= 0) ? 0 : ((100 * m_nBytesRead) / m_nContentLength);
        eResponse.appendElement (RESPONSE_ELEMENT_PERCENT_COMPLETE).appendText (Long.toString (nPercentComplete));
        if (m_nContentLength == 0 && nPercentComplete == 0)
        {
          m_nInitCount++;
          s_aLogger.info ("Initializing file upload [try " + m_nInitCount + "]"); //$NON-NLS-2$
          sDescription = EFileUploadText.PROGRESS_DESC_INIT.getDisplayText (aDisplayLocale);

          // Count the number of initializations, because in some case (e.g.
          // files > 4GB)
          // the browser will not transmit anything to the client
          // So to avoid having an endless poll, after a certain number of
          // "init" messages, the progress is stopped.
          if (m_nInitCount >= MAX_INIT_COUNT)
          {
            setFailed (EFileUploadText.ERROR_NO_MULTIPART_REQUEST.getStringPropertyName (),
                       EFileUploadText.ERROR_NO_MULTIPART_REQUEST,
                       Integer.toString (m_nInitCount));
          }
        }
        else
        {
          if (m_nContentLength == -1)
          {
            final SizeHelper aSH = SizeHelper.getSizeHelperOfLocale (aDisplayLocale);
            sDescription = EFileUploadText.PROGRESS_DESC_PROGRESS_UNKNOWN.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                  aSH.getAsMatching (m_nBytesRead,
                                                                                                                     2));
          }
          else
          {
            final SizeHelper aSH = SizeHelper.getSizeHelperOfLocale (aDisplayLocale);
            sDescription = EFileUploadText.PROGRESS_DESC_PROGRESS.getDisplayTextWithArgs (aDisplayLocale,
                                                                                          aSH.getAsMatching (m_nBytesRead,
                                                                                                             2),
                                                                                          aSH.getAsMatching (m_nContentLength,
                                                                                                             2));
          }
          m_nInitCount = 0;
        }
      }

      if (m_aError != null)
      {
        // We had an error :(
        String sError = m_aError.getDisplayText (aDisplayLocale);
        if (!ArrayHelper.isEmpty (m_aErrorArgs))
        {
          sError = TextFormatter.getFormattedText (sError, (Object) m_aErrorArgs);
        }
        eResponse.appendElement (RESPONSE_ELEMENT_FAILURE).appendText (sError);
        sDescription = "";
        s_aLogger.error ("Reporting upload failed: " + sError);
        reset ();
      }
      // Finally the description element
      eResponse.appendElement (RESPONSE_ELEMENT_DESCRIPTION).appendText (sDescription);
    }
    return aDoc;
  }
}
