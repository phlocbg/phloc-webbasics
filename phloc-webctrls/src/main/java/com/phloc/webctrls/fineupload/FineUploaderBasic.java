package com.phloc.webctrls.fineupload;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.js.IJSCodeProvider;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.json.impl.JSONObject;

/**
 * Wrapper for Fineuploader 3.0
 * 
 * @author philip
 */
public class FineUploaderBasic implements IJSCodeProvider
{
  public static final boolean DEFAULT_DEBUG = false;
  public static final boolean DEFAULT_MULTIPLE = true;
  public static final int DEFAULT_MAX_CONNECTIONS = 3;
  public static final boolean DEFAULT_DISABLE_CANCEL_FOR_FORM_UPLOADS = false;
  public static final boolean DEFAULT_AUTO_UPLOAD = true;

  public static final String DEFAULT_REQUEST_ENDPOINT = "/server/upload";
  public static final boolean DEFAULT_REQUEST_FORCE_MULTIPART = false;
  public static final String DEFAULT_REQUEST_INPUT_NAME = "qqfile";

  public static final int DEFAULT_VALIDATION_SIZE_LIMIT = 0;
  public static final int DEFAULT_VALIDATION_MIN_SIZE_LIMIT = 0;
  public static final boolean DEFAULT_VALIDATION_STOP_ON_FIRST_INVALID_FILE = true;

  public static final boolean DEFAULT_RETRY_ENABLE_AUTO = false;
  public static final int DEFAULT_RETRY_MAX_AUTO_ATTEMPTS = 3;
  public static final int DEFAULT_RETRY_AUTO_ATTEMPT_DELAY = 5;
  public static final String DEFAULT_RETRY_PREVENT_RETRY_RESPONSE_PROPERTY = "preventRetry";

  private boolean m_bDebug = DEFAULT_DEBUG;
  private String m_sButtonElementID;
  private boolean m_bMultiple = DEFAULT_MULTIPLE;
  private int m_nMaxConnections = DEFAULT_MAX_CONNECTIONS;
  private boolean m_bDisableCancelForFormUploads = DEFAULT_DISABLE_CANCEL_FOR_FORM_UPLOADS;
  private boolean m_bAutoUpload = DEFAULT_AUTO_UPLOAD;

  private String m_sRequestEndpoint = DEFAULT_REQUEST_ENDPOINT;
  private final Map <String, String> m_aRequestParams = new LinkedHashMap <String, String> ();
  private final Map <String, String> m_aRequestCustomHeaders = new LinkedHashMap <String, String> ();
  private boolean m_bRequestForceMultipart = DEFAULT_REQUEST_FORCE_MULTIPART;
  private String m_sRequestInputName = DEFAULT_REQUEST_INPUT_NAME;

  private final Set <String> m_aValidationAllowedExtensions = new LinkedHashSet <String> ();
  private int m_nValidationSizeLimit = DEFAULT_VALIDATION_SIZE_LIMIT;
  private int m_nValidationMinSizeLimit = DEFAULT_VALIDATION_MIN_SIZE_LIMIT;
  private boolean m_bValidationStopOnFirstInvalidFile = DEFAULT_VALIDATION_STOP_ON_FIRST_INVALID_FILE;

  private final boolean m_bRetryEnableAuto = DEFAULT_RETRY_ENABLE_AUTO;
  private final int m_nRetryMaxAutoAttempts = DEFAULT_RETRY_MAX_AUTO_ATTEMPTS;
  private final int m_nRetryAutoAttemptDelay = DEFAULT_RETRY_AUTO_ATTEMPT_DELAY;
  private final String m_sRetryPreventRetryResponseProperty = DEFAULT_RETRY_PREVENT_RETRY_RESPONSE_PROPERTY;

  /**
   * If enabled, this will result in log messages (such as server response)
   * being written to the javascript console. If your browser does not support
   * the [window.console
   * object](https://developer.mozilla.org/en-US/docs/DOM/console.log), the
   * value of this option is irrelevant.
   * 
   * @param bDebug
   *        New value
   * @return this
   */
  @Nonnull
  public FineUploaderBasic setDebug (final boolean bDebug)
  {
    m_bDebug = bDebug;
    return this;
  }

  /**
   * The is the endpoint used by both the form and ajax uploader. In the case of
   * the form uploader, it is part of the form's action attribute value along
   * with all parameters. In the case of the ajax uploader, it is makes up part
   * of the URL of the XHR request (again, along with the parameters).
   * 
   * @param sRequestEndpoint
   *        The new action URL
   * @return this
   */
  @Nonnull
  public FineUploaderBasic setEndpoint (@Nonnull @Nonempty final String sRequestEndpoint)
  {
    if (StringHelper.hasNoText (sRequestEndpoint))
      throw new IllegalArgumentException ("requestEndpoint");
    m_sRequestEndpoint = sRequestEndpoint;
    return this;
  }

  /**
   * These parameters are sent with the request to the endpoint specified in the
   * action option.
   * 
   * @param aParams
   *        New parameters to be set.
   * @return this
   */
  @Nonnull
  public FineUploaderBasic setParams (@Nullable final Map <String, String> aParams)
  {
    m_aRequestParams.clear ();
    if (aParams != null)
      m_aRequestParams.putAll (aParams);
    return this;
  }

  /**
   * These parameters are sent with the request to the endpoint specified in the
   * action option.
   * 
   * @param aParams
   *        New parameters to be added.
   * @return this
   */
  @Nonnull
  public FineUploaderBasic addParams (@Nullable final Map <String, String> aParams)
  {
    if (aParams != null)
      m_aRequestParams.putAll (aParams);
    return this;
  }

  /**
   * These parameters are sent with the request to the endpoint specified in the
   * action option.
   * 
   * @param sKey
   *        Parameter name
   * @param sValue
   *        Parameter value
   * @return this
   */
  @Nonnull
  public FineUploaderBasic addParam (@Nonnull @Nonempty final String sKey, @Nonnull final String sValue)
  {
    if (StringHelper.hasNoText (sKey))
      throw new IllegalArgumentException ("key");
    if (sValue == null)
      throw new NullPointerException ("value");
    m_aRequestParams.put (sKey, sValue);
    return this;
  }

  /**
   * Additional headers sent along with the XHR POST request. Note that is
   * option is only relevant to the ajax/XHR uploader.
   * 
   * @param aCustomHeaders
   *        Custom headers to be set.
   * @return this
   */
  @Nonnull
  public FineUploaderBasic setCustomHeaders (@Nullable final Map <String, String> aCustomHeaders)
  {
    m_aRequestCustomHeaders.clear ();
    if (aCustomHeaders != null)
      m_aRequestCustomHeaders.putAll (aCustomHeaders);
    return this;
  }

  /**
   * Additional headers sent along with the XHR POST request. Note that is
   * option is only relevant to the ajax/XHR uploader.
   * 
   * @param aCustomHeaders
   *        Custom headers to be added.
   * @return this
   */
  @Nonnull
  public FineUploaderBasic addCustomHeaders (@Nullable final Map <String, String> aCustomHeaders)
  {
    if (aCustomHeaders != null)
      m_aRequestCustomHeaders.putAll (aCustomHeaders);
    return this;
  }

  /**
   * Additional headers sent along with the XHR POST request. Note that is
   * option is only relevant to the ajax/XHR uploader.
   * 
   * @param sKey
   *        Custom header name
   * @param sValue
   *        Custom header value
   * @return this
   */
  @Nonnull
  public FineUploaderBasic addCustomHeader (@Nonnull @Nonempty final String sKey, @Nonnull final String sValue)
  {
    if (StringHelper.hasNoText (sKey))
      throw new IllegalArgumentException ("key");
    if (sValue == null)
      throw new NullPointerException ("value");
    m_aRequestCustomHeaders.put (sKey, sValue);
    return this;
  }

  /**
   * Specify an element to use as the "select files" button. Note that this may
   * <b>NOT</b> be a &lt;button>, otherwise it will not work in Internet
   * Explorer. Please see issue #33 for details.
   * 
   * @param sButtonElementID
   *        Element ID of the button
   * @return this
   */
  @Nonnull
  public FineUploaderBasic setButtonElementID (@Nullable final String sButtonElementID)
  {
    m_sButtonElementID = sButtonElementID;
    return this;
  }

  /**
   * Set to false puts the uploader into what is best described as 'single-file
   * upload mode'. See the [demo](http://fineuploader.com) for an example.
   * 
   * @param bMultiple
   *        <code>true</code> for multiple, <code>false</code> for single
   * @return this
   */
  @Nonnull
  public FineUploaderBasic setMultiple (final boolean bMultiple)
  {
    m_bMultiple = bMultiple;
    return this;
  }

  /**
   * Maximum allowable concurrent uploads.
   * 
   * @param nMaxConnections
   *        Maximum number. Must be &gt; 0.
   * @return this
   */
  @Nonnull
  public FineUploaderBasic setMaxConnections (@Nonnegative final int nMaxConnections)
  {
    if (nMaxConnections <= 0)
      throw new IllegalArgumentException ("maxConnections may not be negative!");
    m_nMaxConnections = nMaxConnections;
    return this;
  }

  /**
   * If true, the cancel link does not appear next to files when the form
   * uploader is used. This may be desired since it may not be possible to
   * interrupt a form-based upload in some cases.
   * 
   * @param bDisableCancelForFormUploads
   *        disable?
   * @return this
   */
  @Nonnull
  public FineUploaderBasic setDisableCancelForFormUploads (final boolean bDisableCancelForFormUploads)
  {
    m_bDisableCancelForFormUploads = bDisableCancelForFormUploads;
    return this;
  }

  /**
   * Set to false if you want to be able to begin uploading selected/queued
   * files later, by calling uploadStoredFiles().
   * 
   * @param bAutoUpload
   *        <code>false</code> to disable auto upload
   * @return this
   */
  @Nonnull
  public FineUploaderBasic setAutoUpload (final boolean bAutoUpload)
  {
    m_bAutoUpload = bAutoUpload;
    return this;
  }

  /**
   * This may be helpful if you want to restrict uploaded files to specific file
   * types. Note that this validation option is only enforced by examining the
   * extension of uploaded file names. For a more complete verification of the
   * file type, you should use, for example, magic byte file identification on
   * the server side and return {"success": false} in the response if the file
   * type is not on your whitelist.
   * 
   * @param aAllowedExtensions
   *        The allowed extensions to be set.
   * @return this
   */
  public FineUploaderBasic setAllowedExtensions (@Nullable final Set <String> aAllowedExtensions)
  {
    m_aValidationAllowedExtensions.clear ();
    if (aAllowedExtensions != null)
      m_aValidationAllowedExtensions.addAll (aAllowedExtensions);
    return this;
  }

  /**
   * This may be helpful if you want to restrict uploaded files to specific file
   * types. Note that this validation option is only enforced by examining the
   * extension of uploaded file names. For a more complete verification of the
   * file type, you should use, for example, magic byte file identification on
   * the server side and return {"success": false} in the response if the file
   * type is not on your whitelist.
   * 
   * @param aAllowedExtensions
   *        The allowed extensions to be added.
   * @return this
   */
  public FineUploaderBasic addAllowedExtensions (@Nullable final Set <String> aAllowedExtensions)
  {
    if (aAllowedExtensions != null)
      m_aValidationAllowedExtensions.addAll (aAllowedExtensions);
    return this;
  }

  /**
   * This may be helpful if you want to restrict uploaded files to specific file
   * types. Note that this validation option is only enforced by examining the
   * extension of uploaded file names. For a more complete verification of the
   * file type, you should use, for example, magic byte file identification on
   * the server side and return {"success": false} in the response if the file
   * type is not on your whitelist.
   * 
   * @param sAllowedExtension
   *        The allowed extension to be added. E.g. ("jpeg", "jpg", "gif")
   * @return this
   */
  public FineUploaderBasic addAllowedExtension (@Nonnull @Nonempty final String sAllowedExtension)
  {
    if (StringHelper.hasNoText (sAllowedExtension))
      throw new IllegalArgumentException ("allowedExtension");
    m_aValidationAllowedExtensions.add (sAllowedExtension);
    return this;
  }

  /**
   * Maximum allowable size, in bytes, for a file.
   * 
   * @param nSizeLimit
   *        Size limit. 0 == unlimited
   * @return this
   */
  @Nonnull
  public FineUploaderBasic setSizeLimit (@Nonnegative final int nSizeLimit)
  {
    if (nSizeLimit < 0)
      throw new IllegalArgumentException ("sizeLimit may not be negative!");
    m_nValidationSizeLimit = nSizeLimit;
    return this;
  }

  /**
   * Minimum allowable size, in bytes, for a file.
   * 
   * @param nMinSizeLimit
   *        Minimum size limit. 0 == unlimited
   * @return this
   */
  @Nonnull
  public FineUploaderBasic setMinSizeLimit (@Nonnegative final int nMinSizeLimit)
  {
    if (nMinSizeLimit < 0)
      throw new IllegalArgumentException ("minSizeLimit may not be negative!");
    m_nValidationMinSizeLimit = nMinSizeLimit;
    return this;
  }

  /**
   * If true, when submitting multiple files, once a file is determined to be
   * invalid, the upload process will terminate. If false, all valid files will
   * be uploaded. Note: One downside to a false value can be seen if the default
   * showMessage implementation is not overriden. In this case, an alert dialog
   * will appear for each invalid file in the batch, and the upload process will
   * not continue until the dialog is dismissed. If this is bothersome, simply
   * override showMessage with a desirable implementation. 3.0 will likely have
   * a showMessage default implementation that does not use the alert function.
   * 
   * @param bStopOnFirstInvalidFile
   *        <code>false</code> to not stop
   * @return this
   */
  @Nonnull
  public FineUploaderBasic setStopOnFirstInvalidFile (final boolean bStopOnFirstInvalidFile)
  {
    m_bValidationStopOnFirstInvalidFile = bStopOnFirstInvalidFile;
    return this;
  }

  /**
   * This usually only useful with the ajax uploader, which sends the name of
   * the file as a parameter, using a key name equal to the value of this
   * options. In the case of the form uploader, this is simply the value of the
   * name attribute of the file's associated input element.
   * 
   * @param sInputName
   *        The input name
   * @return this
   */
  @Nonnull
  public FineUploaderBasic setInputName (@Nonnull @Nonempty final String sInputName)
  {
    m_sRequestInputName = sInputName;
    return this;
  }

  /**
   * While form-based uploads will always be multipart requests, this forces XHR
   * uploads to send files using multipart requests as well.
   * 
   * @param bForceMultipart
   *        <code>true</code> to force
   * @return this
   */
  @Nonnull
  public FineUploaderBasic setForceMultipart (final boolean bForceMultipart)
  {
    m_bRequestForceMultipart = bForceMultipart;
    return this;
  }

  @Nonnull
  public JSONObject getJSON ()
  {
    final JSONObject ret = new JSONObject ();
    if (m_bDebug != DEFAULT_DEBUG)
      ret.setBooleanProperty ("debug", m_bDebug);
    if (StringHelper.hasText (m_sButtonElementID))
      ret.setFunctionPrebuildProperty ("button", JQuery.idRef (m_sButtonElementID).component (0).getJSCode ());
    if (m_bMultiple != DEFAULT_MULTIPLE)
      ret.setBooleanProperty ("multiple", m_bMultiple);
    if (m_nMaxConnections != DEFAULT_MAX_CONNECTIONS)
      ret.setIntegerProperty ("maxConnections", m_nMaxConnections);
    if (m_bDisableCancelForFormUploads != DEFAULT_DISABLE_CANCEL_FOR_FORM_UPLOADS)
      ret.setBooleanProperty ("disableCancelForFormUploads", m_bDisableCancelForFormUploads);
    if (m_bAutoUpload != DEFAULT_AUTO_UPLOAD)
      ret.setBooleanProperty ("autoUpload", m_bAutoUpload);

    // request
    {
      final JSONObject aRequest = new JSONObject ();
      if (!m_sRequestEndpoint.equals (DEFAULT_REQUEST_ENDPOINT))
        aRequest.setStringProperty ("endpoint", m_sRequestEndpoint);
      if (!m_aRequestParams.isEmpty ())
      {
        final JSONObject aParams = new JSONObject ();
        for (final Map.Entry <String, String> aEntry : m_aRequestParams.entrySet ())
          aParams.setStringProperty (aEntry.getKey (), aEntry.getValue ());
        aRequest.setObjectProperty ("params", aParams);
      }
      if (!m_aRequestCustomHeaders.isEmpty ())
      {
        final JSONObject aCustomHeaders = new JSONObject ();
        for (final Map.Entry <String, String> aEntry : m_aRequestCustomHeaders.entrySet ())
          aCustomHeaders.setStringProperty (aEntry.getKey (), aEntry.getValue ());
        aRequest.setObjectProperty ("customHeaders", aCustomHeaders);
      }
      if (m_bRequestForceMultipart != DEFAULT_REQUEST_FORCE_MULTIPART)
        aRequest.setBooleanProperty ("forceMultipart", m_bRequestForceMultipart);
      if (!m_sRequestInputName.equals (DEFAULT_REQUEST_INPUT_NAME))
        ret.setStringProperty ("inputName", m_sRequestInputName);

      if (!aRequest.isEmpty ())
        ret.setObjectProperty ("request", aRequest);
    }

    // validation
    {
      final JSONObject aValidation = new JSONObject ();
      if (!m_aValidationAllowedExtensions.isEmpty ())
        aValidation.setStringListProperty ("allowedExtensions", m_aValidationAllowedExtensions);
      if (m_nValidationSizeLimit != DEFAULT_VALIDATION_SIZE_LIMIT)
        aValidation.setIntegerProperty ("sizeLimit", m_nValidationSizeLimit);
      if (m_nValidationMinSizeLimit != DEFAULT_VALIDATION_MIN_SIZE_LIMIT)
        aValidation.setIntegerProperty ("minSizeLimit", m_nValidationMinSizeLimit);
      if (m_bValidationStopOnFirstInvalidFile != DEFAULT_VALIDATION_STOP_ON_FIRST_INVALID_FILE)
        aValidation.setBooleanProperty ("stopOnFirstInvalidFile", m_bValidationStopOnFirstInvalidFile);
      if (!aValidation.isEmpty ())
        ret.setObjectProperty ("validation", aValidation);
    }

    // retry
    {
      final JSONObject aRetry = new JSONObject ();
      if (m_bRetryEnableAuto != DEFAULT_RETRY_ENABLE_AUTO)
        aRetry.setBooleanProperty ("enableAuto", m_bRetryEnableAuto);
      if (m_nRetryMaxAutoAttempts != DEFAULT_RETRY_MAX_AUTO_ATTEMPTS)
        aRetry.setIntegerProperty ("maxAutoAttempts", m_nRetryMaxAutoAttempts);
      if (m_nRetryAutoAttemptDelay != DEFAULT_RETRY_AUTO_ATTEMPT_DELAY)
        aRetry.setIntegerProperty ("autoAttemptDelay", m_nRetryAutoAttemptDelay);
      if (!DEFAULT_RETRY_PREVENT_RETRY_RESPONSE_PROPERTY.equals (m_sRetryPreventRetryResponseProperty))
        aRetry.setStringProperty ("preventRetryResponseProperty", m_sRetryPreventRetryResponseProperty);
      if (!aRetry.isEmpty ())
        ret.setObjectProperty ("retry", aRetry);
    }

    return ret;
  }

  public String getJSCode ()
  {
    return new JSInvocation ("new qq.FileUploader").arg (getJSON ()).getJSCode ();
  }
}
