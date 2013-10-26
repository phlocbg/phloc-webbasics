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
package com.phloc.webbasics.ajax.servlet;

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.stats.IStatisticsHandlerKeyedCounter;
import com.phloc.commons.stats.IStatisticsHandlerKeyedTimer;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.timing.StopWatch;
import com.phloc.web.CWebCharset;
import com.phloc.web.servlet.request.RequestHelper;
import com.phloc.web.servlet.request.RequestLogger;
import com.phloc.web.servlet.response.UnifiedResponse;
import com.phloc.webbasics.ajax.IAjaxExceptionHandler;
import com.phloc.webbasics.ajax.IAjaxInvoker;
import com.phloc.webbasics.ajax.IAjaxResponse;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webscopes.servlets.AbstractUnifiedResponseServlet;

/**
 * Abstract implementation of a servlet that invokes AJAX functions.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public abstract class AbstractAjaxServlet extends AbstractUnifiedResponseServlet
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractAjaxServlet.class);
  private static final IStatisticsHandlerKeyedTimer s_aStatsTimer = StatisticsManager.getKeyedTimerHandler (AbstractAjaxServlet.class);
  private static final IStatisticsHandlerKeyedCounter s_aStatsCounterSuccess = StatisticsManager.getKeyedCounterHandler (AbstractAjaxServlet.class +
                                                                                                                         "$success");
  private static final IStatisticsHandlerKeyedCounter s_aStatsCounterError = StatisticsManager.getKeyedCounterHandler (AbstractAjaxServlet.class +
                                                                                                                       "$error");

  protected static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  @GuardedBy ("s_aRWLock")
  private static IAjaxExceptionHandler s_aCustomExceptionHandler;

  /**
   * Set a custom exception handler that is invoked in case an exception is
   * thrown. This exception handler is invoked additional to the regular
   * exception logging!
   * 
   * @param aCustomExceptionHandler
   *        The custom handler. May be <code>null</code> to indicate that no
   *        handler is needed.
   */
  public static void setCustomExceptionHandler (@Nullable final IAjaxExceptionHandler aCustomExceptionHandler)
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
   * @return The current custom exception handler or <code>null</code> if none
   *         is set.
   */
  @Nullable
  public static IAjaxExceptionHandler getCustomExceptionHandler ()
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
   * Check if it is valid in the current scope to invoke the passed AJAX
   * function.
   * 
   * @param sAjaxFunctionName
   *        The AJAX function that was desired to be invoked.
   * @return <code>true</code> if the AJAX function may be invoked.
   */
  @OverrideOnDemand
  protected boolean isValidToInvokeActionFunction (@Nonnull final String sAjaxFunctionName)
  {
    return true;
  }

  /**
   * Get the AJAX invoker matching the passed request
   * 
   * @param aRequestScope
   *        The request scope to use. May not be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  protected abstract IAjaxInvoker getAjaxInvoker (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope);

  @Override
  protected void handleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                @Nonnull final UnifiedResponse aUnifiedResponse) throws ServletException, IOException
  {
    // get handler name from request (skipping the leading "/")
    final String sAjaxFunctionName = StringHelper.trimStart (RequestHelper.getPathWithinServlet (aRequestScope.getRequest ()),
                                                             "/");

    try
    {
      if (StringHelper.hasNoText (sAjaxFunctionName))
      {
        // Just in case somebody tries to play around with our servlet...
        s_aLogger.warn ("No AJAX method name provided");
        aUnifiedResponse.setStatus (HttpServletResponse.SC_NOT_FOUND);
      }
      else
        if (!isValidToInvokeActionFunction (sAjaxFunctionName))
        {
          // E.g. not valid for current p3 run-mode
          s_aLogger.warn ("Invoking the AJAX function '" + sAjaxFunctionName + "' is not valid in this context!");
          aUnifiedResponse.setStatus (HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
        else
        {
          // Start the timing
          final StopWatch aSW = new StopWatch (true);

          // E.g. for keep-alive
          final IAjaxInvoker aInvoker = getAjaxInvoker (aRequestScope);
          final IAjaxResponse aResult = aInvoker.invokeFunction (sAjaxFunctionName, aRequestScope);
          if (s_aLogger.isTraceEnabled ())
            s_aLogger.trace ("  AJAX Result: " + aResult);

          // Do not cache the result!
          final String sResultJSON = aResult.getSerializedAsJSON (GlobalDebug.isDebugMode ());
          aUnifiedResponse.disableCaching ()
                          .setContentAndCharset (sResultJSON, CWebCharset.CHARSET_XML_OBJ)
                          .setMimeType (CMimeType.APPLICATION_JSON);

          // Remember the time
          s_aStatsTimer.addTime (sAjaxFunctionName, aSW.stopAndGetMillis ());
          s_aStatsCounterSuccess.increment (sAjaxFunctionName);
        }
    }
    catch (final Throwable t)
    {
      s_aStatsCounterError.increment (sAjaxFunctionName);
      if (t instanceof IOException)
      {
        if (!StreamUtils.isKnownEOFException (t))
          s_aLogger.error ("Error writing result of AJAX function '" + sAjaxFunctionName + "'", t);
      }
      else
      {
        s_aLogger.error ("Error invoking AJAX function '" + sAjaxFunctionName + "'", t);
        RequestLogger.logRequestComplete (aRequestScope.getRequest ());
      }

      // Notify custom exception handler
      final IAjaxExceptionHandler aCustomExceptionHandler = getCustomExceptionHandler ();
      if (aCustomExceptionHandler != null)
        try
        {
          aCustomExceptionHandler.onAjaxException (t, sAjaxFunctionName, aRequestScope);
        }
        catch (final Throwable t2)
        {
          s_aLogger.error ("Exception in custom AJAX exception handler of function '" + sAjaxFunctionName + "'", t2);
        }

      // Re-throw
      if (t instanceof IOException)
        throw (IOException) t;
      if (t instanceof ServletException)
        throw (ServletException) t;
      throw new ServletException ("Error invoking AJAX function", t);
    }
  }
}
