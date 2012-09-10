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
package com.phloc.webbasics.ajax;

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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
import com.phloc.scopes.web.domain.IRequestWebScope;
import com.phloc.scopes.web.servlet.AbstractScopeAwareHttpServlet;
import com.phloc.webbasics.CWebCharset;
import com.phloc.webbasics.web.RequestHelper;
import com.phloc.webbasics.web.RequestLogger;
import com.phloc.webbasics.web.ResponseHelper;

/**
 * Abstract implementation of a servlet that invokes AJAX functions.
 * 
 * @author philip
 */
public class DefaultAjaxServlet extends AbstractScopeAwareHttpServlet
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (DefaultAjaxServlet.class);
  private static final IStatisticsHandlerKeyedTimer s_aStatsTimer = StatisticsManager.getKeyedTimerHandler (DefaultAjaxServlet.class);
  private static final IStatisticsHandlerKeyedCounter s_aStatsCounterSuccess = StatisticsManager.getKeyedCounterHandler (DefaultAjaxServlet.class +
                                                                                                                         "$success");
  private static final IStatisticsHandlerKeyedCounter s_aStatsCounterError = StatisticsManager.getKeyedCounterHandler (DefaultAjaxServlet.class +
                                                                                                                       "$error");

  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
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

  private void _invokeAJAXFunction (@Nonnull final HttpServletRequest aHttpRequest,
                                    @Nonnull final HttpServletResponse aHttpResponse,
                                    @Nonnull final IRequestWebScope aRequestScope) throws IOException, ServletException
  {
    // get handler name from request (skipping the leading "/")
    final String sAjaxFunctionName = StringHelper.trimStart (RequestHelper.getPathWithinServlet (aHttpRequest), "/");

    try
    {
      if (StringHelper.hasNoText (sAjaxFunctionName))
      {
        // Just in case somebody tries to play around with our servlet...
        s_aLogger.warn ("No AJAX method name provided");
        aHttpResponse.setStatus (HttpServletResponse.SC_NOT_FOUND);
        ResponseHelper.modifyResponseForNoCaching (aHttpResponse);
        ResponseHelper.writeEmptyResponse (aHttpRequest, aHttpResponse);
      }
      else
        if (!isValidToInvokeActionFunction (sAjaxFunctionName))
        {
          // E.g. not valid for current p3 run-mode
          s_aLogger.warn ("Invoking the AJAX function '" + sAjaxFunctionName + "' is not valid in this context!");
          aHttpResponse.setStatus (HttpServletResponse.SC_NOT_ACCEPTABLE);
          ResponseHelper.modifyResponseForNoCaching (aHttpResponse);
          ResponseHelper.writeEmptyResponse (aHttpRequest, aHttpResponse);
        }
        else
        {
          // Start the timing
          final StopWatch aSW = new StopWatch (true);

          // E.g. for keep-alive
          final AjaxDefaultResponse aResult = AjaxManager.getInstance ().invokeFunction (sAjaxFunctionName,
                                                                                         aRequestScope);
          if (s_aLogger.isTraceEnabled ())
            s_aLogger.trace ("  AJAX Result: " + aResult);

          // Do not cache the result!
          ResponseHelper.modifyResponseForNoCaching (aHttpResponse);

          // response writer cannot handle null values!
          ResponseHelper.writeTextResponse (aHttpRequest,
                                            aHttpResponse,
                                            aResult.getSerializedAsJSON (GlobalDebug.isDebugMode ()),
                                            CMimeType.APPLICATION_JSON,
                                            CWebCharset.CHARSET_XML_OBJ);

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
        RequestLogger.logRequestComplete (aHttpRequest);
      }

      // Notify custom exception handler
      final IAjaxExceptionHandler aCustomExceptionHandler = getCustomExceptionHandler ();
      if (aCustomExceptionHandler != null)
        try
        {
          aCustomExceptionHandler.onAjaxException (t, sAjaxFunctionName, aHttpRequest, aHttpResponse);
        }
        catch (final Throwable t2)
        {
          s_aLogger.error ("Exception in custom AJAX exception handler of function '" + sAjaxFunctionName + "'", t2);
        }

      if (t instanceof IOException)
        throw (IOException) t;
      throw new ServletException ("Error invoking AJAX function: " + t.getMessage ());
    }
  }

  /**
   * Main request handling method. This indirection level is required for doing
   * additional request initialization methods.
   * 
   * @param aHttpRequest
   *        The HTTP request
   * @param aHttpResponse
   *        The HTTP response
   * @throws IOException
   */
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void handleRequest (@Nonnull final HttpServletRequest aHttpRequest,
                                @Nonnull final HttpServletResponse aHttpResponse,
                                @Nonnull final IRequestWebScope aRequestScope) throws ServletException, IOException
  {
    // Main invocation
    _invokeAJAXFunction (aHttpRequest, aHttpResponse, aRequestScope);
  }

  @Override
  protected final void onGet (@Nonnull final HttpServletRequest aHttpRequest,
                              @Nonnull final HttpServletResponse aHttpResponse,
                              @Nonnull final IRequestWebScope aRequestScope) throws ServletException, IOException
  {
    handleRequest (aHttpRequest, aHttpResponse, aRequestScope);
  }

  @Override
  protected final void onPost (@Nonnull final HttpServletRequest aHttpRequest,
                               @Nonnull final HttpServletResponse aHttpResponse,
                               @Nonnull final IRequestWebScope aRequestScope) throws ServletException, IOException
  {
    handleRequest (aHttpRequest, aHttpResponse, aRequestScope);
  }
}
