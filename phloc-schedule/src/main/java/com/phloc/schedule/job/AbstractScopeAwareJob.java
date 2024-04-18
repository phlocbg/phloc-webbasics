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
package com.phloc.schedule.job;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.ThreadSafe;
import jakarta.servlet.http.HttpSession;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.state.ESuccess;
import com.phloc.scopes.mgr.ScopeManager;
import com.phloc.web.mock.MockHttpServletRequest;
import com.phloc.web.mock.MockHttpServletResponse;
import com.phloc.web.mock.OfflineHttpServletRequest;
import com.phloc.webscopes.domain.IRequestWebScope;
import com.phloc.webscopes.impl.RequestWebScope;
import com.phloc.webscopes.mgr.WebScopeManager;
import com.phloc.webscopes.util.ScopeHelper;

/**
 * Abstract {@link Job} implementation that handles request scopes correctly.
 * This is required, because each scheduled job runs in its own thread so that
 * no default {@link ScopeManager} information would be available.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public abstract class AbstractScopeAwareJob extends AbstractJob
{
  private static final Logger LOG = LoggerFactory.getLogger (AbstractScopeAwareJob.class);
  public static final String PROPERTY_REQUEST_SCOPE = "request.scope"; //$NON-NLS-1$
  public static final String PROPERTY_HTTP_SESSION = "request.session"; //$NON-NLS-1$

  public AbstractScopeAwareJob ()
  {}

  /**
   * @return A new map for setting up the job data. If a request scope is
   *         present, it will be stored along with the session in the map so it
   *         can be restored automatically on job execution.
   */
  @Nonnull
  public static Map <String, Object> createScopeAwareJobDataMap ()
  {
    final Map <String, Object> aMap = ContainerHelper.newMap ();
    final RequestWebScope aRequestScope = (RequestWebScope) WebScopeManager.getRequestScopeOrNull ();
    if (aRequestScope != null)
    {
      aMap.put (PROPERTY_REQUEST_SCOPE, aRequestScope);
      aMap.put (PROPERTY_HTTP_SESSION, aRequestScope.getRequest ().getSession ());
    }
    return aMap;
  }

  /**
   * @param aJobDataMap
   *        The current job data map. Never <code>null</code>.
   * @return The application scope ID to be used. May not be <code>null</code>.
   */
  @Nonnull
  protected abstract String getApplicationScopeID (@Nonnull final JobDataMap aJobDataMap);

  /**
   * @return The dummy HTTP request to be used for executing this job. By
   *         default an {@link OfflineHttpServletRequest} is created.
   */
  @SuppressWarnings ("static-method")
  @Nonnull
  @OverrideOnDemand
  protected MockHttpServletRequest createMockHttpServletRequest ()
  {
    return new OfflineHttpServletRequest (WebScopeManager.getGlobalScope ().getServletContext (), false);
  }

  /**
   * @return The dummy HTTP response to be used for executing this job. By
   *         default a {@link MockHttpServletResponse} is created.
   */
  @SuppressWarnings ("static-method")
  @Nonnull
  @OverrideOnDemand
  protected MockHttpServletResponse createMockHttpServletResponse ()
  {
    return new MockHttpServletResponse ();
  }

  /**
   * Called before the job gets executed. This method is called after the scopes
   * are initialized!
   * 
   * @param aJobDataMap
   *        The current job data map. Never <code>null</code>.
   */
  @SuppressWarnings ("unused")
  @OverrideOnDemand
  protected void beforeExecuteInScope (@Nonnull final JobDataMap aJobDataMap)
  {
    // override on demand
  }

  /**
   * Called before the job gets executed. This method is called before the
   * scopes are initialized!
   * 
   * @param aJobDataMap
   *        The current job data map. Never <code>null</code>.
   */
  @Override
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void beforeExecute (@Nonnull final JobDataMap aJobDataMap)
  {
    final String sApplicationScopeID = getApplicationScopeID (aJobDataMap);

    final IRequestWebScope aScope = (IRequestWebScope) aJobDataMap.get (PROPERTY_REQUEST_SCOPE);

    if (aScope == null)
    {
      // Scopes (ensure to create a new scope each time!)
      try
      {
        WebScopeManager.onRequestBegin (sApplicationScopeID,
                                        new OfflineHttpServletRequest (WebScopeManager.getGlobalScope ()
                                                                                      .getServletContext (),
                                                                       false),
                                        new MockHttpServletResponse ());
      }
      catch (final Exception aEx)
      {
        LOG.error ("Catched exception recreating scope", aEx); //$NON-NLS-1$
      }
    }
    else
    {
      final HttpSession aSession = (HttpSession) aJobDataMap.get (PROPERTY_HTTP_SESSION);
      ScopeHelper.setupMockRequestOnDemand (sApplicationScopeID, aSession);
    }

    // Invoke callback
    beforeExecuteInScope (aJobDataMap);
  }

  /**
   * Called after the job gets executed. This method is called before the scopes
   * are destroyed.
   * 
   * @param aJobDataMap
   *        The current job data map. Never <code>null</code>.
   * @param eExecSuccess
   *        The execution success state. Never <code>null</code>.
   */
  @SuppressWarnings ("unused")
  @OverrideOnDemand
  protected void afterExecuteInScope (@Nonnull final JobDataMap aJobDataMap, @Nonnull final ESuccess eExecSuccess)
  {
    // override on demand
  }

  /**
   * Called after the job gets executed. This method is called after the scopes
   * are destroyed.
   * 
   * @param aJobDataMap
   *        The current job data map. Never <code>null</code>.
   * @param eExecSuccess
   *        The execution success state. Never <code>null</code>.
   */
  @Override
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void afterExecute (@Nonnull final JobDataMap aJobDataMap, @Nonnull final ESuccess eExecSuccess)
  {
    try
    {
      // Invoke callback
      afterExecuteInScope (aJobDataMap, eExecSuccess);
    }
    finally
    {
      // Close request scope
      WebScopeManager.onRequestEnd ();
    }
  }
}
