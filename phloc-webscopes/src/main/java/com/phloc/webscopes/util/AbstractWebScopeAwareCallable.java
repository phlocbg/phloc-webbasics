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
package com.phloc.webscopes.util;

import java.util.concurrent.Callable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.ServletContext;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.callback.INonThrowingCallable;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.web.mock.MockHttpServletResponse;
import com.phloc.web.mock.OfflineHttpServletRequest;
import com.phloc.webscopes.mgr.WebScopeManager;

/**
 * Abstract implementation of {@link Callable} that handles WebScopes correctly.
 * 
 * @author Philip Helger
 * @param <DATATYPE>
 *        The return type of the function.
 */
public abstract class AbstractWebScopeAwareCallable <DATATYPE> implements INonThrowingCallable <DATATYPE>
{
  private final ServletContext m_aSC;
  private final String m_sApplicationID;

  public AbstractWebScopeAwareCallable ()
  {
    this (WebScopeManager.getGlobalScope ().getServletContext (), WebScopeManager.getApplicationScope ().getID ());
  }

  public AbstractWebScopeAwareCallable (@Nonnull final ServletContext aSC, @Nonnull @Nonempty final String sApplicationID)
  {
    if (aSC == null)
      throw new NullPointerException ("servletContext");
    if (StringHelper.hasNoText (sApplicationID))
      throw new IllegalArgumentException ("applicationID");

    m_aSC = aSC;
    m_sApplicationID = sApplicationID;
  }

  /**
   * Implement your code in here
   * 
   * @return The return value of the {@link #call()} method.
   */
  @Nullable
  protected abstract DATATYPE scopedRun ();

  @Nullable
  public final DATATYPE call ()
  {
    WebScopeManager.onRequestBegin (m_sApplicationID,
                                    new OfflineHttpServletRequest (m_aSC, false),
                                    new MockHttpServletResponse ());
    try
    {
      final DATATYPE ret = scopedRun ();
      return ret;
    }
    finally
    {
      WebScopeManager.onRequestEnd ();
    }
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("servletContext", m_aSC)
                                       .append ("applicationID", m_sApplicationID)
                                       .toString ();
  }
}
