/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.webbasics.action.servlet;

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.GuardedBy;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.state.EContinue;
import com.phloc.commons.stats.IStatisticsHandlerKeyedCounter;
import com.phloc.commons.stats.IStatisticsHandlerKeyedTimer;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.timing.StopWatch;
import com.phloc.web.servlet.request.RequestHelper;
import com.phloc.web.servlet.request.RequestLogger;
import com.phloc.web.servlet.response.UnifiedResponse;
import com.phloc.webbasics.action.IActionExceptionHandler;
import com.phloc.webbasics.action.IActionExecutor;
import com.phloc.webbasics.action.IActionInvoker;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webscopes.servlets.AbstractUnifiedResponseServlet;

/**
 * Abstract action handling servlet
 * 
 * @author Philip Helger
 */
public abstract class AbstractActionServlet extends AbstractUnifiedResponseServlet
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractActionServlet.class);
  private static final IStatisticsHandlerKeyedTimer s_aStatsTimer = StatisticsManager.getKeyedTimerHandler (AbstractActionServlet.class);
  private static final IStatisticsHandlerKeyedCounter s_aStatsCounterSuccess = StatisticsManager.getKeyedCounterHandler (AbstractActionServlet.class +
                                                                                                                         "$success");
  private static final IStatisticsHandlerKeyedCounter s_aStatsCounterError = StatisticsManager.getKeyedCounterHandler (AbstractActionServlet.class +
                                                                                                                       "$error");
  private static final String SCOPE_ATTR_ACTION_NAME = "$defaultactionservlet.actionname";
  private static final String SCOPE_ATTR_EXECUTOR = "$defaultactionservlet.executor";

  protected static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  @GuardedBy ("s_aRWLock")
  private static IActionExceptionHandler s_aCustomExceptionHandler;

  /**
   * Set a custom exception handler that is invoked in case an exception is
   * thrown. This exception handler is invoked additional to the regular
   * exception logging!
   * 
   * @param aCustomExceptionHandler
   *        The custom handler. May be <code>null</code> to indicate that no
   *        handler is needed.
   */
  public static void setCustomExceptionHandler (@Nullable final IActionExceptionHandler aCustomExceptionHandler)
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
  public static IActionExceptionHandler getCustomExceptionHandler ()
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
   * Check if it is valid in the current scope to invoke the passed Action.
   * 
   * @param sActionName
   *        The Action that was desired to be invoked.
   * @param aRequestScope
   *        The current request scope. Never <code>null</code>.
   * @return <code>true</code> if the Action may be invoked.
   */
  @OverrideOnDemand
  protected boolean isValidToInvokeAction (@Nonnull final String sActionName,
                                           @Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    return true;
  }

  /**
   * Get the action invoker matching the passed request
   * 
   * @param aRequestScope
   *        The request scope to use. May not be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  protected abstract IActionInvoker getActionInvoker (@Nonnull IRequestWebScopeWithoutResponse aRequestScope);

  @Override
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected EContinue initRequestState (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                        @Nonnull final UnifiedResponse aUnifiedResponse)
  {
    // cut the leading "/"
    String sActionName = RequestHelper.getPathWithinServlet (aRequestScope.getRequest ());
    if (StringHelper.startsWith (sActionName, '/'))
      sActionName = sActionName.substring (1);

    final IActionInvoker aInvoker = getActionInvoker (aRequestScope);
    final IActionExecutor aActionExecutor = aInvoker.getActionExecutor (sActionName);
    if (aActionExecutor == null)
    {
      s_aLogger.warn ("Unknown action '" + sActionName + "' provided!");

      // No such action
      aUnifiedResponse.setStatus (HttpServletResponse.SC_NOT_FOUND);
      return EContinue.BREAK;
    }

    // Call the initialization of the action executor
    aActionExecutor.initExecution (aRequestScope);

    // Remember in scope
    aRequestScope.setAttribute (SCOPE_ATTR_ACTION_NAME, sActionName);
    aRequestScope.setAttribute (SCOPE_ATTR_EXECUTOR, aActionExecutor);
    return EContinue.CONTINUE;
  }

  @Override
  protected void handleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                @Nonnull final UnifiedResponse aUnifiedResponse) throws ServletException, IOException
  {
    // Action is present
    final String sActionName = aRequestScope.getAttributeAsString (SCOPE_ATTR_ACTION_NAME);

    try
    {
      // Start the timing
      final StopWatch aSW = new StopWatch (true);

      if (!isValidToInvokeAction (sActionName, aRequestScope))
      {
        s_aLogger.warn ("Invoking the Action '" + sActionName + "' is not valid in this context!");
        aUnifiedResponse.setStatus (HttpServletResponse.SC_NOT_ACCEPTABLE);
      }
      else
      {
        // Handle the main action
        final IActionInvoker aInvoker = getActionInvoker (aRequestScope);
        if (aInvoker.executeAction (sActionName, aRequestScope, aUnifiedResponse).isSuccess ())
        {
          // Remember the time
          s_aStatsTimer.addTime (sActionName, aSW.stopAndGetMillis ());
          s_aStatsCounterSuccess.increment (sActionName);
        }
        else
        {
          // Error in execution
          aUnifiedResponse.setStatus (HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
      }
    }
    catch (final Throwable t)
    {
      s_aStatsCounterError.increment (sActionName);
      if (t instanceof IOException)
      {
        if (!StreamUtils.isKnownEOFException (t))
          s_aLogger.error ("Error writing result of Action '" + sActionName + "'", t);
      }
      else
      {
        s_aLogger.error ("Error invoking Action '" + sActionName + "'", t);
        if (GlobalDebug.isDebugMode ())
          RequestLogger.logRequestComplete (aRequestScope.getRequest ());
      }

      // Notify custom exception handler
      final IActionExceptionHandler aCustomExceptionHandler = getCustomExceptionHandler ();
      if (aCustomExceptionHandler != null)
        try
        {
          aCustomExceptionHandler.onActionExecutionException (t, sActionName, aRequestScope);
        }
        catch (final Throwable t2)
        {
          s_aLogger.error ("Exception in custom Action exception handler of function '" + sActionName + "'", t2);
        }

      // Re-throw
      if (t instanceof IOException)
        throw (IOException) t;
      if (t instanceof ServletException)
        throw (ServletException) t;
      throw new ServletException ("Error invoking Action '" + sActionName + "'", t);
    }
  }
}
