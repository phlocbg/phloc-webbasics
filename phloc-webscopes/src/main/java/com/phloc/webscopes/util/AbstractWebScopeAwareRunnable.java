/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.webscopes.util;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.callback.INonThrowingRunnable;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.web.mock.MockHttpServletResponse;
import com.phloc.web.mock.OfflineHttpServletRequest;
import com.phloc.webscopes.mgr.WebScopeManager;

/**
 * Abstract implementation of {@link Runnable} that handles WebScopes correctly.
 * 
 * @author Philip Helger
 */
public abstract class AbstractWebScopeAwareRunnable implements INonThrowingRunnable
{
  private final ServletContext m_aSC;
  private final String m_sApplicationID;

  public AbstractWebScopeAwareRunnable ()
  {
    this (WebScopeManager.getGlobalScope ().getServletContext (), WebScopeManager.getApplicationScope ().getID ());
  }

  public AbstractWebScopeAwareRunnable (@Nonnull final ServletContext aSC,
                                        @Nonnull @Nonempty final String sApplicationID)
  {
    this.m_aSC = ValueEnforcer.notNull (aSC, "ServletContext");
    this.m_sApplicationID = ValueEnforcer.notEmpty (sApplicationID, "ApplicationID");
  }

  @Nonnull
  public ServletContext getServletContext ()
  {
    return this.m_aSC;
  }

  @Nonnull
  @Nonempty
  public String getApplicationID ()
  {
    return this.m_sApplicationID;
  }

  /**
   * Implement your code in here.
   */
  protected abstract void scopedRun ();

  @Override
  public final void run ()
  {
    WebScopeManager.onRequestBegin (this.m_sApplicationID,
                                    new OfflineHttpServletRequest (this.m_aSC, false),
                                    new MockHttpServletResponse ());
    try
    {
      scopedRun ();
    }
    finally
    {
      WebScopeManager.onRequestEnd ();
    }
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("servletContext", this.m_aSC)
                                       .append ("applicationID", this.m_sApplicationID)
                                       .toString ();
  }
}
