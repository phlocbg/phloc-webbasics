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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.lang.ClassHelper;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.IStatisticsHandlerKeyedCounter;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.timing.StopWatch;
import com.phloc.html.js.marshal.JSMarshaller;
import com.phloc.scopes.nonweb.singleton.GlobalSingleton;
import com.phloc.scopes.web.domain.IRequestWebScope;

/**
 * The main AJAX service.
 * 
 * @author philip
 */
@ThreadSafe
public final class AjaxManager extends GlobalSingleton
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AjaxManager.class);
  private static final IStatisticsHandlerCounter s_aStatsGlobalInvoke = StatisticsManager.getCounterHandler (AjaxManager.class.getName () +
                                                                                                             "$invocations");
  private static final IStatisticsHandlerKeyedCounter s_aStatsFunctionInvoke = StatisticsManager.getKeyedCounterHandler (AjaxManager.class.getName () +
                                                                                                                         "$func");

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final Map <String, Class <? extends IAjaxHandler>> m_aMap = new HashMap <String, Class <? extends IAjaxHandler>> ();

  /**
   * Private constructor. Avoid outside instantiation
   */
  @Deprecated
  @UsedViaReflection
  public AjaxManager ()
  {}

  @Nonnull
  public static AjaxManager getInstance ()
  {
    return getGlobalSingleton (AjaxManager.class);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, Class <? extends IAjaxHandler>> getAllRegisteredHandlers ()
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
  public Class <? extends IAjaxHandler> getRegisteredHandler (@Nullable final String sFunctionName)
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

  /**
   * Add a global handler function that is used as a callback.
   * 
   * @param sFunctionName
   *        Name AJAX function to be invoked. May not be <code>null</code>.
   * @param aHandlerClass
   *        The class handling a special function. May not be <code>null</code>.
   */
  public void addHandlerFunction (@Nonnull final String sFunctionName,
                                  @Nonnull final Class <? extends IAjaxHandler> aHandlerClass)
  {
    if (!JSMarshaller.isJSIdentifier (sFunctionName))
      throw new IllegalArgumentException ("Invalid function name '" + sFunctionName + "' specified");
    if (aHandlerClass == null)
      throw new NullPointerException ("No handler class specified");
    if (!ClassHelper.isInstancableClass (aHandlerClass))
      throw new IllegalArgumentException ("The passed handler class is not instancable: " + aHandlerClass);

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_aMap.containsKey (sFunctionName))
        throw new IllegalArgumentException ("An AJAX function with the name '" +
                                            sFunctionName +
                                            "' is already registered");
      m_aMap.put (sFunctionName, aHandlerClass);
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Registered AJAX function '" + sFunctionName + "' with handler " + aHandlerClass);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Invoke the specified AJAX function.
   * 
   * @param sFunctionName
   *        the alias of the AJAX function to invoke. May not be
   *        <code>null</code>.
   * @param aRequestScope
   *        The request scope to be used for the function.
   * @return <code>null</code> if this is a void function, the JS string to
   *         serialize otherwise.
   * @throws Exception
   *         on reflection error
   * @throws IllegalArgumentException
   *         If no handler is registered for the specified function name.
   */
  @Nonnull
  public AjaxDefaultResponse invokeFunction (@Nonnull final String sFunctionName,
                                             @Nonnull final IRequestWebScope aRequestScope) throws Exception
  {
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Invoking AJAX function '" + sFunctionName + "'");

    final StopWatch aSW = new StopWatch (true);

    // Find the handler
    final Class <? extends IAjaxHandler> aHandlerClass = getRegisteredHandler (sFunctionName);
    if (aHandlerClass == null)
      throw new IllegalArgumentException ("Failed to find handler for AJAX function '" + sFunctionName + "'");

    // Global increment before invocation
    s_aStatsGlobalInvoke.increment ();

    // create handler instance (we ensured at registration, that a no-argument
    // ctor is present)
    final IAjaxHandler aHandlerObj = aHandlerClass.newInstance ();

    // Register all external resources, prior to handling the main request, as
    // the JS/CSS elements will be contained in the AjaxDefaultResponse in case
    // of success
    aHandlerObj.registerExternalResources ();

    // execute request
    final AjaxDefaultResponse aReturnValue = aHandlerObj.handleRequest (aRequestScope);

    // Increment statistics after successful call
    s_aStatsFunctionInvoke.increment (sFunctionName);

    // Extremely long running AJAX request?
    if (aSW.stopAndGetMillis () > CGlobal.MILLISECONDS_PER_SECOND)
      s_aLogger.warn ("Finished invoking AJAX function '" +
                      sFunctionName +
                      "' which took " +
                      aSW.getMillis () +
                      " milliseconds (which is too long)");
    return aReturnValue;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("map", m_aMap).toString ();
  }
}
