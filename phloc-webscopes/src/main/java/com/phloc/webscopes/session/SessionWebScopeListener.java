/**
 * Copyright (C) 2006-2018 phloc systems
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
package com.phloc.webscopes.session;

import javax.annotation.Nonnull;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.webscopes.domain.ISessionWebScope;
import com.phloc.webscopes.mgr.WebScopeManager;

/**
 * A default implementation of a web scope aware {@link HttpSessionListener}
 * which takes care about request and session scope handling.<br>
 * <b>Note:</b> The benefit of using this as a base class for your listener
 * implementation is that the scope retrieval via the {@link WebScopeManager}
 * can also return the proper session scope during the destruction.
 * 
 * @author Boris Gregorcic
 */
public class SessionWebScopeListener implements HttpSessionListener
{
  @Override
  public final void sessionCreated (@Nonnull final HttpSessionEvent aEvent)
  {
    final HttpSession aSession = aEvent.getSession ();
    WebScopeManager.onDetectSessionStart (aSession);
    try
    {
      this.onBeforeSessionCreated (aSession);
    }
    finally
    {
      this.onAfterSessionCreated (WebScopeManager.onSessionBegin (aSession));
      WebScopeManager.onFinishedSessionStart (aSession);
    }
  }

  @OverrideOnDemand
  protected void onBeforeSessionCreated (@SuppressWarnings ("unused") final HttpSession aSession)
  {
    // implement on demand
  }

  @OverrideOnDemand
  protected void onAfterSessionCreated (@SuppressWarnings ("unused") final ISessionWebScope aScope)
  {
    // implement on demand
  }

  @Override
  public final void sessionDestroyed (@Nonnull final HttpSessionEvent aEvent)
  {
    final HttpSession aSession = aEvent.getSession ();
    WebScopeManager.onDetectSessionEnd (aSession);
    try
    {
      this.onBeforeSessionDestroyed (aSession);
    }
    finally
    {
      WebScopeManager.onSessionEnd (aSession);
      this.onAfterSessionDestroyed (aSession);
    }
  }

  @OverrideOnDemand
  protected void onBeforeSessionDestroyed (@SuppressWarnings ("unused") final HttpSession aSession)
  {
    // implement on demand
  }

  @OverrideOnDemand
  protected void onAfterSessionDestroyed (@SuppressWarnings ("unused") final HttpSession aSession)
  {
    // implement on demand
  }
}
