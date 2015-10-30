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

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.ThreadSafe;

import org.quartz.Job;
import org.quartz.JobDataMap;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.state.ESuccess;
import com.phloc.scopes.mgr.ScopeManager;
import com.phloc.web.mock.MockHttpServletRequest;
import com.phloc.web.mock.MockHttpServletResponse;
import com.phloc.web.mock.OfflineHttpServletRequest;
import com.phloc.webscopes.mgr.WebScopeManager;

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
  public AbstractScopeAwareJob ()
  {}

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
  @OverrideOnDemand
  protected void beforeExecuteInScope (@Nonnull final JobDataMap aJobDataMap)
  {}

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
    // Scopes (ensure to create a new scope each time!)
    final String sApplicationScopeID = getApplicationScopeID (aJobDataMap);
    final MockHttpServletRequest aHttpRequest = createMockHttpServletRequest ();
    final MockHttpServletResponse aHttpResponse = createMockHttpServletResponse ();
    WebScopeManager.onRequestBegin (sApplicationScopeID, aHttpRequest, aHttpResponse);

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
  @OverrideOnDemand
  protected void afterExecuteInScope (@Nonnull final JobDataMap aJobDataMap, @Nonnull final ESuccess eExecSuccess)
  {}

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
