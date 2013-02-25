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
package com.phloc.webbasics.action;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.stats.IStatisticsHandlerKeyedTimer;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.timing.StopWatch;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
import com.phloc.web.servlet.response.UnifiedResponse;

/**
 * This class maps action names to callback objects.
 * 
 * @author philip
 */
@ThreadSafe
public final class ActionInvoker implements IActionInvoker
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ActionInvoker.class);
  private static final IStatisticsHandlerKeyedTimer s_aTimer = StatisticsManager.getKeyedTimerHandler (ActionInvoker.class);

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final Map <String, IActionExecutor> m_aMap = new HashMap <String, IActionExecutor> ();
  private IActionExceptionHandler m_aExceptionHandler;

  public void setCustomExceptionHandler (@Nullable final IActionExceptionHandler aExceptionHandler)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aExceptionHandler = aExceptionHandler;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  public IActionExceptionHandler getCustomExceptionHandler ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aExceptionHandler;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public void addAction (@Nonnull final String sAction, @Nonnull final IActionExecutor aActionExecutor)
  {
    if (StringHelper.hasNoText (sAction))
      throw new IllegalArgumentException ("action");
    if (aActionExecutor == null)
      throw new NullPointerException ("callback");

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_aMap.containsKey (sAction))
        throw new IllegalArgumentException ("Action '" + sAction + "' is already contained!");

      m_aMap.put (sAction, aActionExecutor);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  public ESuccess executeAction (@Nullable final String sActionName,
                                 @Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                 @Nonnull final UnifiedResponse aUnifiedResponse) throws Exception
  {
    // Find the executor
    IActionExecutor aActionExecutor;
    m_aRWLock.readLock ().lock ();
    try
    {
      aActionExecutor = m_aMap.get (sActionName);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }

    if (aActionExecutor == null)
    {
      // No executor found
      s_aLogger.warn ("Failed to resolve action '" + sActionName + "'");
      return ESuccess.FAILURE;
    }

    // For actions caching is not an option, because it is dynamic content
    aUnifiedResponse.disableCaching ();

    try
    {
      final StopWatch aSW = new StopWatch (true);
      aActionExecutor.execute (aRequestScope, aUnifiedResponse);
      s_aTimer.addTime (sActionName, aSW.stopAndGetMillis ());
      return ESuccess.SUCCESS;
    }
    catch (final Exception ex)
    {
      if (!StreamUtils.isKnownEOFException (ex))
        s_aLogger.error ("Failed to execute action '" + sActionName + "'", ex);

      // Invoke custom exception handler (if any)
      final IActionExceptionHandler aCustomExceptionHandler = getCustomExceptionHandler ();
      if (aCustomExceptionHandler != null)
        try
        {
          aCustomExceptionHandler.onActionExecutionException (ex, sActionName);
        }
        catch (final Throwable t2)
        {
          s_aLogger.error ("Error in custom exception handler for action '" + sActionName + "'", t2);
        }

      throw ex;
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, IActionExecutor> getAllActions ()
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

  public boolean containsAction (@Nullable final String sName)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aMap.containsKey (sName);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public IActionExecutor getActionExecutor (@Nullable final String sName)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aMap.get (sName);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("map", m_aMap).toString ();
  }
}
