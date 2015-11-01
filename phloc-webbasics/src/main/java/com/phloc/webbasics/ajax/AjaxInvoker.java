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
package com.phloc.webbasics.ajax;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.factory.FactoryNewInstance;
import com.phloc.commons.factory.IFactory;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.IStatisticsHandlerKeyedCounter;
import com.phloc.commons.stats.IStatisticsHandlerKeyedTimer;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.timing.StopWatch;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * The default implementation of {@link IAjaxInvoker}.
 *
 * @author Philip Helger
 */
@ThreadSafe
public class AjaxInvoker implements IAjaxInvoker
{
  /** Default milliseconds until an implementation is considered long running. */
  public static final long DEFAULT_LONG_RUNNING_EXECUTION_LIMIT_MS = CGlobal.MILLISECONDS_PER_SECOND;

  private static final Logger s_aLogger = LoggerFactory.getLogger (AjaxInvoker.class);
  private static final IStatisticsHandlerCounter s_aStatsGlobalInvoke = StatisticsManager.getCounterHandler (AjaxInvoker.class.getName () +
                                                                                                             "$invocations");
  private static final IStatisticsHandlerKeyedCounter s_aStatsFunctionInvoke = StatisticsManager.getKeyedCounterHandler (AjaxInvoker.class.getName () +
                                                                                                                         "$func");
  private static final IStatisticsHandlerKeyedTimer s_aStatsFunctionTimer = StatisticsManager.getKeyedTimerHandler (AjaxInvoker.class.getName () +
                                                                                                                    "$timer");

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  @GuardedBy ("m_aRWLock")
  private IAjaxBeforeExecutionHandler m_aBeforeExecutionHdl;
  @GuardedBy ("m_aRWLock")
  private IAjaxAfterExecutionHandler m_aAfterExecutionHdl;
  @GuardedBy ("m_aRWLock")
  private final Map <String, IFactory <? extends IAjaxHandler>> m_aMap = new HashMap <String, IFactory <? extends IAjaxHandler>> ();
  @GuardedBy ("m_aRWLock")
  private long m_nLongRunningExecutionLimitTime = DEFAULT_LONG_RUNNING_EXECUTION_LIMIT_MS;
  @GuardedBy ("m_aRWLock")
  private IAjaxLongRunningExecutionHandler m_aLongRunningExecutionHdl = new LoggingAjaxLongRunningExecutionHandler ();

  public AjaxInvoker ()
  {}

  public static boolean isValidFunctionName (@Nullable final String sFunctionName)
  {
    // All characters allowed should be valid in URLs without masking
    return StringHelper.hasText (sFunctionName) &&
           RegExHelper.stringMatchesPattern ("^[a-zA-Z0-9\\-_]+$", sFunctionName);
  }

  @Nullable
  public IAjaxBeforeExecutionHandler getBeforeExecutionHandler ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aBeforeExecutionHdl;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public void setBeforeExecutionHandler (@Nullable final IAjaxBeforeExecutionHandler aBeforeExecutionHdl)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aBeforeExecutionHdl = aBeforeExecutionHdl;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  public IAjaxAfterExecutionHandler getAfterExecutionHandler ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aAfterExecutionHdl;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public void setAfterExecutionHandler (@Nullable final IAjaxAfterExecutionHandler aAfterExecutionHdl)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aAfterExecutionHdl = aAfterExecutionHdl;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  public long getLongRunningExecutionLimitTime ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_nLongRunningExecutionLimitTime;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public void setLongRunningExecutionLimitTime (final long nLongRunningExecutionLimitTime)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      m_nLongRunningExecutionLimitTime = nLongRunningExecutionLimitTime;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  public IAjaxLongRunningExecutionHandler getLongRunningExecutionHandler ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aLongRunningExecutionHdl;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public void setLongRunningExecutionHandler (@Nullable final IAjaxLongRunningExecutionHandler aLongRunningExecutionHdl)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aLongRunningExecutionHdl = aLongRunningExecutionHdl;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, IFactory <? extends IAjaxHandler>> getAllRegisteredHandlers ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newMap (m_aMap);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public IFactory <? extends IAjaxHandler> getRegisteredHandler (@Nullable final String sFunctionName)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aMap.get (sFunctionName);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public boolean isRegisteredFunction (@Nullable final String sFunctionName)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aMap.containsKey (sFunctionName);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public void addHandlerFunction (@Nonnull final IAjaxFunctionDeclaration aFunction,
                                  @Nonnull final Class <? extends IAjaxHandler> aClass)
  {
    ValueEnforcer.notNull (aFunction, "Function");
    ValueEnforcer.notNull (aClass, "Class");
    addHandlerFunction (aFunction, FactoryNewInstance.create (aClass));
  }

  public void addHandlerFunction (@Nonnull final IAjaxFunctionDeclaration aFunction,
                                  @Nonnull final IFactory <? extends IAjaxHandler> aFactory)
  {
    ValueEnforcer.notNull (aFunction, "Function");
    addHandlerFunction (aFunction.getName (), aFactory);
  }

  public void addHandlerFunction (@Nonnull final String sFunctionName,
                                  @Nonnull final Class <? extends IAjaxHandler> aClass)
  {
    ValueEnforcer.notNull (aClass, "Class");
    addHandlerFunction (sFunctionName, FactoryNewInstance.create (aClass));
  }

  public void addHandlerFunction (@Nonnull final String sFunctionName,
                                  @Nonnull final IFactory <? extends IAjaxHandler> aFactory)
  {
    if (!isValidFunctionName (sFunctionName))
      throw new IllegalArgumentException ("Invalid function name '" + sFunctionName + "' specified");
    ValueEnforcer.notNull (aFactory, "Factory");

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_aMap.containsKey (sFunctionName))
        throw new IllegalArgumentException ("An AJAX function with the name '" +
                                            sFunctionName +
                                            "' is already registered");
      m_aMap.put (sFunctionName, aFactory);
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Registered AJAX function '" + sFunctionName + "' with handler " + aFactory);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  public IAjaxResponse invokeFunction (@Nonnull final String sFunctionName,
                                       @Nonnull final IRequestWebScopeWithoutResponse aRequestWebScope) throws Exception
  {
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Invoking AJAX function '" + sFunctionName + "'");

    final StopWatch aSW = new StopWatch (true);

    // Find the handler
    final IFactory <? extends IAjaxHandler> aHandlerFactory = getRegisteredHandler (sFunctionName);
    if (aHandlerFactory == null)
      throw new IllegalArgumentException ("Failed to find handler for AJAX function '" + sFunctionName + "'");

    // Global increment before invocation
    s_aStatsGlobalInvoke.increment ();

    // create handler instance
    final IAjaxHandler aAjaxHandler = aHandlerFactory.create ();
    if (aAjaxHandler == null)
      throw new IllegalStateException ("Factory of '" + sFunctionName + "' created null-handler!");

    // Invoke before handler
    final IAjaxBeforeExecutionHandler aBeforeHdl = getBeforeExecutionHandler ();
    if (aBeforeHdl != null)
      try
      {
        aBeforeHdl.onBeforeExecution (this, sFunctionName, aRequestWebScope, aAjaxHandler);
      }
      catch (final Throwable t)
      {
        s_aLogger.error ("Error invoking AJAX before execution callback handler " + aBeforeHdl, t);
      }

    // Register all external resources, prior to handling the main request, as
    // the JS/CSS elements will be contained in the AjaxDefaultResponse in case
    // of success
    aAjaxHandler.registerExternalResources ();

    // Main handle request
    final IAjaxResponse aAjaxResponse = aAjaxHandler.handleRequest (aRequestWebScope);
    if (aAjaxResponse.isFailure ())
    {
      // Execution failed
      s_aLogger.warn ("Invoked AJAX function '" + sFunctionName + "' returned a failure: " + aAjaxResponse.toString ());
    }

    // Invoke after handler
    final IAjaxAfterExecutionHandler aAfterHdl = getAfterExecutionHandler ();
    if (aAfterHdl != null)
      try
      {
        aAfterHdl.onAfterExecution (this, sFunctionName, aRequestWebScope, aAjaxHandler, aAjaxResponse);
      }
      catch (final Throwable t)
      {
        s_aLogger.error ("Error invoking AJAX after execution callback handler " + aAfterHdl, t);
      }

    // Increment statistics after successful call
    s_aStatsFunctionInvoke.increment (sFunctionName);

    // Long running AJAX request?
    final long nExecutionMillis = aSW.stopAndGetMillis ();
    s_aStatsFunctionTimer.addTime (sFunctionName, nExecutionMillis);
    final long nLimitMS = getLongRunningExecutionLimitTime ();
    if (nLimitMS > 0 && nExecutionMillis > nLimitMS)
    {
      // Long running execution
      final IAjaxLongRunningExecutionHandler aLongRunningExecutionHdl = getLongRunningExecutionHandler ();
      if (aLongRunningExecutionHdl != null)
        try
        {
          aLongRunningExecutionHdl.onLongRunningExecution (this,
                                                           sFunctionName,
                                                           aRequestWebScope,
                                                           aAjaxHandler,
                                                           nExecutionMillis);
        }
        catch (final Throwable t)
        {
          s_aLogger.error ("Error invoking AJAX long running execution callback handler " + aLongRunningExecutionHdl, t);
        }
    }
    return aAjaxResponse;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("map", m_aMap)
                                       .append ("longRunningExecutionLimitTime", m_nLongRunningExecutionLimitTime)
                                       .appendIfNotNull ("longRunningExecutionHdl", m_aLongRunningExecutionHdl)
                                       .toString ();
  }
}
