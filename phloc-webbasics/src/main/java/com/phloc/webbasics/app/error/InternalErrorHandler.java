/**
 * Copyright (C) 2006-2013 phloc systems
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

import java.util.Locale;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.lang.StackTraceHelper;
import com.phloc.css.ECSSUnit;
import com.phloc.css.property.CCSSProperties;
import com.phloc.css.propertyvalue.CCSSValue;
import com.phloc.html.hc.IHCNodeWithChildren;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCH1;
import com.phloc.html.hc.html.HCTextArea;
import com.phloc.html.hc.htmlext.HCUtils;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * A handler for internal errors that occur while building a UI page
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class InternalErrorHandler
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (InternalErrorHandler.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  private static IInternalErrorCallback s_aCustomExceptionHandler;

  private InternalErrorHandler ()
  {}

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

  /**
   * Default handling for an internal error
   * 
   * @param aParent
   *        The parent list to append the nodes to
   * @param t
   *        The exception that occurred. May not be <code>null</code>.
   * @param aRequestScope
   *        The request scope in which the error occurred. May not be
   *        <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to use for the texts.
   * @return The created unique error ID
   */
  @Nonnull
  @Nonempty
  public static String handleInternalError (@Nonnull final IHCNodeWithChildren <?> aParent,
                                            @Nonnull final Throwable t,
                                            @Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                            @Nonnull final Locale aDisplayLocale)
  {
    return handleInternalError (aParent, t, aRequestScope, aDisplayLocale, true);
  }

  /**
   * Default handling for an internal error
   * 
   * @param aParent
   *        The parent list to append the nodes to
   * @param t
   *        The exception that occurred. May not be <code>null</code>.
   * @param aRequestScope
   *        The request scope in which the error occurred. May not be
   *        <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to use for the texts.
   * @param bInvokeCustomExceptionHandler
   *        <code>true</code> to invoke the custom exception handler (if any is
   *        present), <code>false</code> to not do so.
   * @return The created unique error ID
   */
  @Nonnull
  @Nonempty
  public static String handleInternalError (@Nonnull final IHCNodeWithChildren <?> aParent,
                                            @Nonnull final Throwable t,
                                            @Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                            @Nonnull final Locale aDisplayLocale,
                                            final boolean bInvokeCustomExceptionHandler)
  {
    final String sErrorID = createNewInternalErrorID ();

    // Log the error, to ensure the data is persisted!
    s_aLogger.error ("handleInternalError " + sErrorID, t);

    aParent.addChild (new HCH1 ().addChild (EWebBasicsText.INTERNAL_ERROR_TITLE.getDisplayText (aDisplayLocale)));
    aParent.addChild (new HCDiv ().addChildren (HCUtils.nl2brList (EWebBasicsText.INTERNAL_ERROR_DESCRIPTION.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                                                     sErrorID))));

    if (GlobalDebug.isDebugMode ())
    {
      // Get error stack trace
      final String sStackTrace = StackTraceHelper.getStackAsString (t, false);

      final HCTextArea aStackTrace = new HCTextArea ("callstack").setValue (sStackTrace)
                                                                 .setRows (20)
                                                                 .addStyle (CCSSProperties.WIDTH.newValue (ECSSUnit.perc (98)))
                                                                 .addStyle (CCSSProperties.FONT_SIZE.newValue (ECSSUnit.pt (10)))
                                                                 .addStyle (CCSSProperties.FONT_FAMILY.newValue (CCSSValue.FONT_MONOSPACE));

      aParent.addChild (aStackTrace);

      // In case an unexpected error occurs in the UnitTest, make the test fail!
      if (StackTraceHelper.containsUnitTestElement (t.getStackTrace ()))
        throw new IllegalStateException ("Error executing unit test", t);
    }

    if (bInvokeCustomExceptionHandler)
    {
      // Invoke custom exception handler (if present)
      final IInternalErrorCallback aCustomExceptionHandler = getCustomExceptionHandler ();
      if (aCustomExceptionHandler != null)
        try
        {
          aCustomExceptionHandler.onInternalError (t, aRequestScope, sErrorID, aDisplayLocale);
        }
        catch (final Throwable t2)
        {
          s_aLogger.error ("Internal error in custom exception handler " + aCustomExceptionHandler, t2);
        }
    }

    return sErrorID;
  }
}
