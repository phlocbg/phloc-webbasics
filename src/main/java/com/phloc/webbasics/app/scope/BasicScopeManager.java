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
package com.phloc.webbasics.app.scope;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manage the available scopes.
 * 
 * @author philip
 */
public final class BasicScopeManager
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (BasicScopeManager.class);

  /** Global scope */
  private static IGlobalScope s_aGlobalScope;

  /** Request scope */
  private static final ThreadLocal <IRequestScope> s_aRequestScope = new ThreadLocal <IRequestScope> ();

  protected BasicScopeManager ()
  {}

  @Nonnull
  public static IGlobalScope getGlobalScope ()
  {
    if (s_aGlobalScope == null)
      throw new IllegalStateException ("No global scope object has been set!");
    return s_aGlobalScope;
  }

  @Nonnull
  public static IRequestScope getRequestScope ()
  {
    final IRequestScope aScope = s_aRequestScope.get ();
    if (aScope == null)
      throw new IllegalStateException ("The request context is not available. If you're running the unittests, inherit your test class from a scopeAwareTestCase.");
    return aScope;
  }

  @Nonnull
  public static ISessionScope getSessionScope ()
  {
    return getSessionScope (true);
  }

  @Nullable
  public static ISessionScope getSessionScope (final boolean bCreateIfNotExisting)
  {
    final IRequestScope aRequestScope = getRequestScope ();
    ISessionScope aSessionScope = aRequestScope.getCastedAttribute ("$session");
    if (aSessionScope == null && bCreateIfNotExisting)
    {
      aSessionScope = new SessionScope (aRequestScope.getRequest ().getSession ());
      aRequestScope.setAttribute ("$session", aSessionScope);
    }
    return aSessionScope;
  }

  /**
   * This method is used to set the initial offline context and the initial
   * global context.
   * 
   * @param aGlobalScope
   *        The global context to use. May not be null.
   */
  public static void onGlobalBegin (@Nonnull final IGlobalScope aGlobalScope)
  {
    if (aGlobalScope == null)
      throw new NullPointerException ("globalScope");
    if (s_aGlobalScope != null)
      throw new IllegalStateException ("Another global scope is already present");

    s_aGlobalScope = aGlobalScope;
    s_aLogger.info ("Global scope initialized!");
  }

  /**
   * This method initializes a request.
   */
  public static void onRequestBegin (@Nonnull final IRequestScope aRequestScope)
  {
    if (aRequestScope == null)
      throw new NullPointerException ("requestScope");
    if (s_aGlobalScope == null)
      throw new IllegalStateException ("No global context present! May be the global context listener is not installed?");

    // set request context
    s_aRequestScope.set (aRequestScope);
  }

  public static void onRequestEnd ()
  {
    final IRequestScope aScope = s_aRequestScope.get ();
    try
    {
      if (aScope != null)
        aScope.destroyScope ();
    }
    finally
    {
      s_aRequestScope.remove ();
    }
  }

  @OverridingMethodsMustInvokeSuper
  public static void onGlobalEnd ()
  {
    /**
     * This code removes all attributes set for the global context. This is
     * necessary, since the attributes would survive a Tomcat servlet context
     * reload if we don't kill them manually.<br>
     * Global scope variable may be null if onGlobalBegin() was never called!
     */
    if (s_aGlobalScope != null)
      s_aGlobalScope.destroyScope ();
    s_aGlobalScope = null;

    // done
    s_aLogger.info ("Global scope shut down!");
  }
}
