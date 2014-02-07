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
package com.phloc.webbasics.app.error;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.io.WebFileIO;
import com.phloc.appbasics.security.login.LoggedInUserManager;
import com.phloc.commons.CGlobal;
import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.base64.Base64;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.email.IEmailAddress;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.io.file.SimpleFileIO;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.lang.StackTraceHelper;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.mutable.MutableInt;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.xml.serialize.XMLWriterSettings;
import com.phloc.css.ECSSUnit;
import com.phloc.css.property.CCSSProperties;
import com.phloc.css.propertyvalue.CCSSValue;
import com.phloc.datetime.PDTFactory;
import com.phloc.datetime.io.PDTIOHelper;
import com.phloc.html.hc.IHCNodeWithChildren;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCH1;
import com.phloc.html.hc.html.HCTextArea;
import com.phloc.html.hc.htmlext.HCUtils;
import com.phloc.web.datetime.PDTWebDateUtils;
import com.phloc.web.servlet.request.RequestLogger;
import com.phloc.web.smtp.EEmailType;
import com.phloc.web.smtp.IEmailAttachmentDataSource;
import com.phloc.web.smtp.IEmailAttachmentList;
import com.phloc.web.smtp.IReadonlyEmailAttachmentList;
import com.phloc.web.smtp.ISMTPSettings;
import com.phloc.web.smtp.impl.EmailData;
import com.phloc.web.smtp.transport.MailAPI;
import com.phloc.web.useragent.UserAgentDatabase;
import com.phloc.web.useragent.uaprofile.UAProfile;
import com.phloc.web.useragent.uaprofile.UAProfileDatabase;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webscopes.domain.ISessionWebScope;
import com.phloc.webscopes.mgr.WebScopeManager;
import com.phloc.webscopes.smtp.ScopedMailAPI;

/**
 * A handler for internal errors
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class InternalErrorHandler
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (InternalErrorHandler.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  private static ISMTPSettings s_aSMTPSettings = null;
  private static IEmailAddress s_aSenderAddress = null;
  private static List <IEmailAddress> s_aReceiverAddresses = null;
  private static IInternalErrorCallback s_aCustomExceptionHandler;
  private static final Map <String, MutableInt> s_aIntErrCache = new HashMap <String, MutableInt> ();

  private InternalErrorHandler ()
  {}

  public static void setSMTPSettings (@Nullable final ISMTPSettings aSMTPSettings)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aSMTPSettings = aSMTPSettings;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  public static ISMTPSettings getSMTPSettings ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aSMTPSettings;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  public static void setSMTPSenderAddress (@Nullable final IEmailAddress aSenderAddress)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aSenderAddress = aSenderAddress;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  public static IEmailAddress getSMTPSenderAddress ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aSenderAddress;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  public static void setSMTPReceiverAddress (@Nullable final IEmailAddress aReceiverAddress)
  {
    setSMTPReceiverAddresses (aReceiverAddress == null ? null : ContainerHelper.newList (aReceiverAddress));
  }

  public static void setSMTPReceiverAddresses (@Nullable final List <? extends IEmailAddress> aReceiverAddresses)
  {
    if (aReceiverAddresses != null && ContainerHelper.containsAnyNullElement (aReceiverAddresses))
      throw new IllegalArgumentException ("The list of receiver addresses may not contain any null element!");

    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aReceiverAddresses = ContainerHelper.newList (aReceiverAddresses);
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  public static void setSMTPReceiverAddresses (@Nullable final IEmailAddress... aReceiverAddresses)
  {
    if (aReceiverAddresses != null && ArrayHelper.containsAnyNullElement (aReceiverAddresses))
      throw new IllegalArgumentException ("The array of receiver addresses may not contain any null element!");

    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aReceiverAddresses = ContainerHelper.newList (aReceiverAddresses);
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public static List <IEmailAddress> getSMTPReceiverAddresses ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (s_aReceiverAddresses);
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return The current custom exception handler or <code>null</code> if none
   *         is set.
   */
  @Nullable
  public static IInternalErrorCallback getCustomExceptionHandler ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aCustomExceptionHandler;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Set the custom exception handler.
   * 
   * @param aCustomExceptionHandler
   *        The exception handler to be used. May be <code>null</code> to
   *        indicate none.
   */
  public static void setCustomExceptionHandler (@Nullable final IInternalErrorCallback aCustomExceptionHandler)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aCustomExceptionHandler = aCustomExceptionHandler;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Create a new unique error ID.
   * 
   * @return This is either a new persistent int ID or a non-persistent ID
   *         together with the timestamp. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public static String createNewErrorID ()
  {
    try
    {
      return Integer.toString (GlobalIDFactory.getNewPersistentIntID ());
    }
    catch (final IllegalStateException ex)
    {
      // happens when no persistent ID factory is present
      return "t" + GlobalIDFactory.getNewIntID () + "_" + System.currentTimeMillis ();
    }
  }

  /**
   * Create a new unique internal error ID.
   * 
   * @return The created error ID. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public static String createNewInternalErrorID ()
  {
    return "internal-error-" + createNewErrorID ();
  }

  @Nonnull
  @Nonempty
  private static String _getAsString (@Nonnull final Throwable t)
  {
    return t.getMessage () + " -- " + t.getClass ().getName ();
  }

  private static void _sendInternalErrorMailToVendor (@Nullable final String sErrorNumber,
                                                      @Nonnull final InternalErrorData aMetaData,
                                                      @Nonnull final ThreadDescriptor aCurrentDescriptor,
                                                      @Nonnull final ThreadDescriptorList aOtherThreads,
                                                      @Nullable final IEmailAttachmentList aEmailAttachments)
  {
    int nOccurranceCount = 1;
    final String sThrowableStackTrace = aCurrentDescriptor.getStackTrace ();
    if (StringHelper.hasText (sThrowableStackTrace))
    {
      // Check if an internal error was already sent for this stack trace
      final MutableInt aMI = s_aIntErrCache.get (sThrowableStackTrace);
      if (aMI != null)
      {
        // This stack trace was already found!
        aMI.inc ();

        // Send only every 100th invocation!
        nOccurranceCount = aMI.intValue ();
        if ((nOccurranceCount % 100) != 0)
        {
          s_aLogger.warn ("Not sending internal error mail, because this error occurred " + nOccurranceCount + " times");
          return;
        }
      }
      else
        s_aIntErrCache.put (sThrowableStackTrace, new MutableInt (1));
    }

    final IEmailAddress aSender = getSMTPSenderAddress ();
    final List <IEmailAddress> aReceiver = getSMTPReceiverAddresses ();
    final ISMTPSettings aSMTPSettings = getSMTPSettings ();

    if (aSender != null && !aReceiver.isEmpty () && aSMTPSettings != null)
    {
      final String sMailSubject = StringHelper.getConcatenatedOnDemand ("Internal error", ' ', sErrorNumber);

      // Main error thread dump
      final String sMailBody = aMetaData.getAsString () +
                               "\n---------------------------------------------------------------\n" +
                               aCurrentDescriptor.getAsString () +
                               "\n---------------------------------------------------------------\n" +
                               aOtherThreads.getAsString () +
                               "\n---------------------------------------------------------------";

      final EmailData aEmailData = new EmailData (EEmailType.TEXT);
      aEmailData.setFrom (aSender);
      aEmailData.setTo (aReceiver);
      aEmailData.setSubject (sMailSubject);
      aEmailData.setBody (sMailBody);
      aEmailData.setAttachments (aEmailAttachments);

      try
      {
        // Try default error communication data
        ScopedMailAPI.getInstance ().queueMail (aSMTPSettings, aEmailData);
      }
      catch (final Throwable t2)
      {
        // E.g. if no scopes are present
        s_aLogger.warn ("Failed to send via scoped MailAPI: " + _getAsString (t2));
        MailAPI.queueMail (aSMTPSettings, aEmailData);
      }
    }
    else
      s_aLogger.warn ("Not sending internal error mail, because required fields are not set!");
  }

  private static void _saveInternalErrorToXML (@Nullable final String sErrorNumber,
                                               @Nonnull final InternalErrorData aMetaData,
                                               @Nonnull final ThreadDescriptor aCurrentDescriptor,
                                               @Nonnull final ThreadDescriptorList aOtherThreads,
                                               @Nullable final IReadonlyEmailAttachmentList aEmailAttachments)
  {
    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eRoot = aDoc.appendElement ("internalerror");
    if (StringHelper.hasText (sErrorNumber))
      eRoot.setAttribute ("errornumber", sErrorNumber);
    eRoot.appendChild (aMetaData.getAsMicroNode ());
    eRoot.appendChild (aCurrentDescriptor.getAsMicroNode ());
    eRoot.appendChild (aOtherThreads.getAsMicroNode ());
    if (aEmailAttachments != null)
    {
      final List <IEmailAttachmentDataSource> aAttachments = aEmailAttachments.getAsDataSourceList ();
      if (ContainerHelper.isNotEmpty (aAttachments))
      {
        final IMicroElement eAttachments = eRoot.appendElement ("attachments");
        for (final IEmailAttachmentDataSource aDS : aAttachments)
        {
          final IMicroElement eAttachment = eAttachments.appendElement ("attachment");
          eAttachment.setAttribute ("name", aDS.getName ());
          eAttachment.setAttribute ("contenttype", aDS.getContentType ());
          try
          {
            eAttachment.appendText (Base64.encodeBytes (StreamUtils.getAllBytes (aDS.getInputStream ())));
          }
          catch (final Exception ex)
          {
            s_aLogger.error ("Failed to get content of attachment '" + aDS.getName () + "'", ex);
            eAttachment.setAttribute ("contentsavefailure", "true");
          }
        }
      }
    }

    // Start saving
    final String sFilename = StringHelper.getConcatenatedOnDemand (PDTIOHelper.getCurrentDateTimeForFilename (),
                                                                   "-",
                                                                   sErrorNumber) + ".xml";
    SimpleFileIO.writeFile (WebFileIO.getFile ("internal-errors/" + PDTFactory.getCurrentYear () + "/" + sFilename),
                            MicroWriter.getXMLString (aDoc),
                            XMLWriterSettings.DEFAULT_XML_CHARSET_OBJ);
  }

  @Nonnull
  public static InternalErrorData fillInternalErrorMetaData (@Nullable final IRequestWebScopeWithoutResponse aProvidedRequestScope,
                                                             @Nullable final String sErrorNumber,
                                                             @Nullable final String sCustomData)
  {
    final InternalErrorData aMetaData = new InternalErrorData ();

    // Date and time
    try
    {
      aMetaData.addField ("Time", PDTWebDateUtils.getAsStringXSD (PDTFactory.getCurrentDateTime ()));
    }
    catch (final Throwable t2)
    {
      aMetaData.addField ("Time", "System.currentTimeMillis=" + Long.toString (System.currentTimeMillis ()));
    }

    // Error number
    if (sErrorNumber != null)
      aMetaData.addField ("Error number", sErrorNumber);

    IRequestWebScopeWithoutResponse aRequestScope = aProvidedRequestScope;
    if (aRequestScope == null)
      try
      {
        aRequestScope = WebScopeManager.getRequestScope ();
      }
      catch (final Throwable t2)
      {
        // Happens if no scope is available (or what so ever)
        s_aLogger.warn ("Failed to get request scope: " + _getAsString (t2));
      }
    if (aRequestScope != null)
    {
      if (!aRequestScope.isValid ())
        aMetaData.addField ("Request scope", "!!!Present but invalid!!!");

      try
      {
        aMetaData.addField ("Request URL", aRequestScope.getURL ());
      }
      catch (final Throwable t2)
      {
        // fall-through - happens in a weird case
        aMetaData.addField ("Request URL", t2);
      }

      aMetaData.addField ("User agent", UserAgentDatabase.getUserAgent (aRequestScope.getRequest ()).getAsString ());

      try
      {
        aMetaData.addField ("Remote IP address", aRequestScope.getRemoteAddr ());
      }
      catch (final Throwable t2)
      {
        // fall-through - happens in a weird case
        aMetaData.addField ("Remote IP address", t2);
      }

      // Mobile browser?
      final UAProfile aProfile = UAProfileDatabase.getUAProfile (aRequestScope.getRequest ());
      if (!aProfile.equals (UAProfile.EMPTY))
        aMetaData.addField ("UAProfile", aProfile.toString ());
    }
    else
    {
      aMetaData.addField ("Request scope", "!!!Not present!!!");
    }

    ISessionWebScope aSessionScope = null;
    try
    {
      aSessionScope = WebScopeManager.getSessionScope (false);
    }
    catch (final Throwable t2)
    {
      // Happens if no scope is available (or what so ever)
      s_aLogger.warn ("Failed to get request scope: " + _getAsString (t2));
    }
    if (aSessionScope != null)
      aMetaData.addField ("SessionID", aSessionScope.getID ());

    try
    {
      aMetaData.addField ("User", LoggedInUserManager.getInstance ().getCurrentUserID ());
    }
    catch (final Throwable t2)
    {
      // Happens if no scope is available (or what so ever)
      aMetaData.addField ("User", t2);
    }

    // Custom data must always be the last field before the stack separator!
    if (sCustomData != null)
      aMetaData.addField ("Custom data", sCustomData);

    if (aRequestScope != null)
    {
      final HttpServletRequest aHttpRequest = aRequestScope.getRequest ();
      if (aHttpRequest != null)
      {
        try
        {
          for (final Map.Entry <String, String> aEntry : RequestLogger.getRequestFieldMap (aHttpRequest).entrySet ())
            aMetaData.addRequestField (aEntry.getKey (), aEntry.getValue ());
        }
        catch (final Throwable t2)
        {
          s_aLogger.error ("Failed to get request fields from " + aHttpRequest, t2);
        }
        try
        {
          for (final Map.Entry <String, String> aEntry : RequestLogger.getHTTPHeaderMap (aHttpRequest).entrySet ())
            aMetaData.addRequestHeader (aEntry.getKey (), aEntry.getValue ());
        }
        catch (final Throwable t2)
        {
          s_aLogger.error ("Failed to get request headers from " + aHttpRequest, t2);
        }
        try
        {
          for (final Map.Entry <String, String> aEntry : RequestLogger.getRequestParameterMap (aHttpRequest)
                                                                      .entrySet ())
            aMetaData.addRequestParameter (aEntry.getKey (), aEntry.getValue ());
        }
        catch (final Throwable t2)
        {
          s_aLogger.error ("Failed to get request parameters from " + aHttpRequest, t2);
        }

        try
        {
          final Cookie [] aCookies = aHttpRequest.getCookies ();
          if (aCookies != null)
            for (final Cookie aCookie : aCookies)
              aMetaData.addRequestCookie (aCookie.getName (), aCookie.getValue ());
        }
        catch (final Throwable t2)
        {
          s_aLogger.error ("Failed to get request cookies from " + aHttpRequest, t2);
        }
      }
    }
    return aMetaData;
  }

  public static void sendInternalErrorMailToVendor (@Nullable final Throwable t,
                                                    @Nullable final IRequestWebScopeWithoutResponse aRequestScope,
                                                    @Nullable final String sErrorNumber,
                                                    @Nullable final String sCustomData,
                                                    @Nullable final IEmailAttachmentList aEmailAttachments)
  {
    final InternalErrorData aMetaData = fillInternalErrorMetaData (aRequestScope, sErrorNumber, sCustomData);

    // Get descriptor for crashed thread
    final ThreadDescriptor aCurrentDescriptor = ThreadDescriptor.createForCurrentThread (t);

    // Get all other thread descriptors
    final ThreadDescriptorList aOtherThreads = ThreadDescriptorList.createWithAllThreads ();

    // Main mail sending
    _sendInternalErrorMailToVendor (sErrorNumber, aMetaData, aCurrentDescriptor, aOtherThreads, aEmailAttachments);

    // Save as XML too
    _saveInternalErrorToXML (sErrorNumber, aMetaData, aCurrentDescriptor, aOtherThreads, aEmailAttachments);
  }

  /**
   * Default handling for an internal error
   * 
   * @param aParent
   *        The parent list to append the nodes to. May be <code>null</code>.
   * @param t
   *        The exception that occurred. May be <code>null</code>.
   * @param aRequestScope
   *        The request scope in which the error occurred. May be
   *        <code>null</code>.
   * @param sCustomData
   *        Custom data to be put into the mail content. May be
   *        <code>null</code>.
   * @param aEmailAttachments
   *        Email attachments to be added. May be <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to use for the texts. May be <code>null</code> in
   *        which case it defaults to {@link CGlobal#DEFAULT_LOCALE}.
   * @param bInvokeCustomExceptionHandler
   *        <code>true</code> to invoke the custom exception handler (if any is
   *        present), <code>false</code> to not do so.
   * @return The created unique error ID
   */
  @Nonnull
  @Nonempty
  public static String handleInternalError (@Nullable final IHCNodeWithChildren <?> aParent,
                                            @Nullable final Throwable t,
                                            @Nullable final IRequestWebScopeWithoutResponse aRequestScope,
                                            @Nullable final String sCustomData,
                                            @Nullable final IEmailAttachmentList aEmailAttachments,
                                            @Nullable final Locale aDisplayLocale,
                                            final boolean bInvokeCustomExceptionHandler)
  {
    final Locale aRealDisplayLocale = aDisplayLocale != null ? aDisplayLocale : CGlobal.DEFAULT_LOCALE;

    final String sErrorID = createNewInternalErrorID ();

    // Log the error, to ensure the data is persisted!
    s_aLogger.error ("handleInternalError " + sErrorID, t);

    if (aParent != null)
    {
      aParent.addChild (new HCH1 ().addChild (EWebBasicsText.INTERNAL_ERROR_TITLE.getDisplayText (aRealDisplayLocale)));
      aParent.addChild (new HCDiv ().addChildren (HCUtils.nl2brList (EWebBasicsText.INTERNAL_ERROR_DESCRIPTION.getDisplayTextWithArgs (aRealDisplayLocale,
                                                                                                                                       sErrorID))));
    }

    if (GlobalDebug.isDebugMode ())
    {
      if (aParent != null)
      {
        // Get error stack trace
        final String sStackTrace = StackTraceHelper.getStackAsString (t, false);

        final HCTextArea aStackTrace = new HCTextArea ("callstack").setValue (sStackTrace)
                                                                   .setRows (20)
                                                                   .addStyle (CCSSProperties.WIDTH.newValue (ECSSUnit.perc (98)))
                                                                   .addStyle (CCSSProperties.FONT_SIZE.newValue (ECSSUnit.pt (10)))
                                                                   .addStyle (CCSSProperties.FONT_FAMILY.newValue (CCSSValue.FONT_MONOSPACE));

        aParent.addChild (aStackTrace);
      }

      // In case an unexpected error occurs in the UnitTest, make the test fail!
      if (t != null && StackTraceHelper.containsUnitTestElement (t.getStackTrace ()))
        throw new IllegalStateException ("Error executing unit test", t);
    }
    else
    {
      sendInternalErrorMailToVendor (t, aRequestScope, sErrorID, sCustomData, aEmailAttachments);
    }

    if (bInvokeCustomExceptionHandler)
    {
      // Invoke custom exception handler (if present)
      final IInternalErrorCallback aCustomExceptionHandler = getCustomExceptionHandler ();
      if (aCustomExceptionHandler != null)
        try
        {
          aCustomExceptionHandler.onInternalError (t, aRequestScope, sErrorID, aRealDisplayLocale);
        }
        catch (final Throwable t2)
        {
          s_aLogger.error ("Internal error in custom exception handler " + aCustomExceptionHandler, t2);
        }
    }

    return sErrorID;
  }
}
