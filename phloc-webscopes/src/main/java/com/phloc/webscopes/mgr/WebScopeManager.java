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
package com.phloc.webscopes.mgr;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.DevelopersNote;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.scopes.nonweb.domain.IGlobalScope;
import com.phloc.scopes.nonweb.domain.IRequestScope;
import com.phloc.scopes.nonweb.domain.ISessionScope;
import com.phloc.scopes.nonweb.mgr.ScopeManager;
import com.phloc.scopes.nonweb.mgr.ScopeSessionManager;
import com.phloc.webscopes.MetaWebScopeFactory;
import com.phloc.webscopes.domain.IApplicationWebScope;
import com.phloc.webscopes.domain.IGlobalWebScope;
import com.phloc.webscopes.domain.IRequestWebScope;
import com.phloc.webscopes.domain.ISessionApplicationWebScope;
import com.phloc.webscopes.domain.ISessionWebScope;
import com.phloc.webscopes.impl.SessionWebScopeActivator;

/**
 * This is the main manager class for web scope handling.
 * 
 * @author philip
 */
@Immutable
public final class WebScopeManager
{
  // For backward compatibility passivation is disabled
  public static final boolean DEFAULT_SESSION_PASSIVATION_ALLOWED = false;
  private static final String SESSION_ATTR_SESSION_SCOPE_ACTIVATOR = "$phloc.sessionwebscope.activator";
  private static final Logger s_aLogger = LoggerFactory.getLogger (WebScopeManager.class);
  private static final AtomicBoolean s_aSessionPassivationAllowed = new AtomicBoolean (DEFAULT_SESSION_PASSIVATION_ALLOWED);

  private WebScopeManager ()
  {}

  // --- settings ---

  /**
   * @return <code>true</code> if session passivation is allowed. Default is
   *         {@link #DEFAULT_SESSION_PASSIVATION_ALLOWED}
   */
  public static boolean isSessionPassivationAllowed ()
  {
    return s_aSessionPassivationAllowed.get ();
  }

  /**
   * Allow or disallow session passivation
   * 
   * @param bSessionPassivationAllowed
   *        <code>true</code> to enable session passivation, <code>false</code>
   *        to disable it
   */
  public static void setSessionPassivationAllowed (final boolean bSessionPassivationAllowed)
  {
    s_aSessionPassivationAllowed.set (bSessionPassivationAllowed);

    // For passivation to work, the session scopes may not be invalidated at the
    // end of the global scope!
    final ScopeSessionManager aSSM = ScopeSessionManager.getInstance ();
    aSSM.setDestroyAllSessionsOnScopeEnd (!bSessionPassivationAllowed);
    aSSM.setEndAllSessionsOnScopeEnd (!bSessionPassivationAllowed);

    // Ensure that all session web scopes have the activator set or removed
    for (final ISessionWebScope aSessionWebScope : WebScopeSessionManager.getAllSessionWebScopes ())
    {
      final HttpSession aHttpSession = aSessionWebScope.getSession ();
      if (bSessionPassivationAllowed)
      {
        // Ensure the activator is present
        if (aHttpSession.getAttribute (SESSION_ATTR_SESSION_SCOPE_ACTIVATOR) == null)
          aHttpSession.setAttribute (SESSION_ATTR_SESSION_SCOPE_ACTIVATOR,
                                     new SessionWebScopeActivator (aSessionWebScope));
      }
      else
      {
        // Ensure the activator is not present
        aHttpSession.removeAttribute (SESSION_ATTR_SESSION_SCOPE_ACTIVATOR);
      }
    }
  }

  // --- global scope ---

  /**
   * To be called, when the global web scope is initialized. Most commonly this
   * is called from within
   * {@link javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)}
   * 
   * @param aServletContext
   *        The source servlet context to be used to retrieve the scope ID. May
   *        not be <code>null</code>
   * @return The created global web scope
   */
  @Nonnull
  public static IGlobalWebScope onGlobalBegin (@Nonnull final ServletContext aServletContext)
  {
    final IGlobalWebScope aGlobalScope = MetaWebScopeFactory.getWebScopeFactory ().createGlobalScope (aServletContext);
    ScopeManager.setGlobalScope (aGlobalScope);
    return aGlobalScope;
  }

  /**
   * @return <code>true</code> if a global scope is defined, <code>false</code>
   *         if none is defined
   */
  public static boolean isGlobalScopePresent ()
  {
    return ScopeManager.isGlobalScopePresent ();
  }

  /**
   * @return The global scope object or <code>null</code> if no global web scope
   *         is present.
   */
  @Nullable
  public static IGlobalWebScope getGlobalScopeOrNull ()
  {
    final IGlobalScope aGlobalScope = ScopeManager.getGlobalScopeOrNull ();
    try
    {
      return (IGlobalWebScope) aGlobalScope;
    }
    catch (final ClassCastException ex)
    {
      s_aLogger.warn ("Gobal scope object is not a global web scope: " + aGlobalScope);
      return null;
    }
  }

  /**
   * @return The global scope object and never <code>null</code>.
   * @throws IllegalStateException
   *         If no global web scope object is present
   */
  @Nonnull
  public static IGlobalWebScope getGlobalScope ()
  {
    final IGlobalWebScope aGlobalScope = getGlobalScopeOrNull ();
    if (aGlobalScope == null)
      throw new IllegalStateException ("No global web scope object has been set!");
    return aGlobalScope;
  }

  /**
   * To be called, when the global web scope is destroyed. Most commonly this is
   * called from within
   * {@link javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)}
   */
  public static void onGlobalEnd ()
  {
    ScopeManager.onGlobalEnd ();
  }

  // --- application scope ---

  /**
   * Get or create the current application scope using the application ID
   * present in the request scope.
   * 
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static IApplicationWebScope getApplicationScope ()
  {
    return (IApplicationWebScope) ScopeManager.getApplicationScope ();
  }

  /**
   * Get or create the current application scope using the application ID
   * present in the request scope.
   * 
   * @param bCreateIfNotExisting
   *        if <code>false</code> an no application scope is present, none will
   *        be created
   * @return <code>null</code> if bCreateIfNotExisting is <code>false</code> and
   *         no such scope is present
   */
  @Nullable
  public static IApplicationWebScope getApplicationScope (final boolean bCreateIfNotExisting)
  {
    return (IApplicationWebScope) ScopeManager.getApplicationScope (bCreateIfNotExisting);
  }

  /**
   * Get or create an application scope.
   * 
   * @param sApplicationID
   *        The ID of the application scope be retrieved or created. May neither
   *        be <code>null</code> nor empty.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static IApplicationWebScope getApplicationScope (@Nonnull @Nonempty final String sApplicationID)
  {
    return (IApplicationWebScope) ScopeManager.getApplicationScope (sApplicationID);
  }

  /**
   * Get or create an application scope.
   * 
   * @param sApplicationID
   *        The ID of the application scope be retrieved or created. May neither
   *        be <code>null</code> nor empty.
   * @param bCreateIfNotExisting
   *        if <code>false</code> an no application scope is present, none will
   *        be created
   * @return <code>null</code> if bCreateIfNotExisting is <code>false</code> and
   *         no such scope is present
   */
  @Nullable
  public static IApplicationWebScope getApplicationScope (@Nonnull @Nonempty final String sApplicationID,
                                                          final boolean bCreateIfNotExisting)
  {
    return (IApplicationWebScope) ScopeManager.getApplicationScope (sApplicationID, bCreateIfNotExisting);
  }

  // --- session scope ---

  /**
   * To be called, when a session web scope is initialized. Most commonly this
   * is called from within
   * {@link javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)}
   * 
   * @param aHttpSession
   *        The source session to base the scope on. May not be
   *        <code>null</code>
   * @return The created global session scope
   */
  @Nonnull
  public static ISessionWebScope onSessionBegin (@Nonnull final HttpSession aHttpSession)
  {
    final ISessionWebScope aSessionWebScope = MetaWebScopeFactory.getWebScopeFactory ().createSessionScope (aHttpSession);
    ScopeSessionManager.getInstance ().onScopeBegin (aSessionWebScope);
    if (isSessionPassivationAllowed ())
    {
      // Add the special session activator
      aHttpSession.setAttribute (SESSION_ATTR_SESSION_SCOPE_ACTIVATOR, new SessionWebScopeActivator (aSessionWebScope));
    }
    return aSessionWebScope;
  }

  /**
   * Internal method which does the main logic for session web scope creation
   * 
   * @param aHttpSession
   *        The underlying HTTP session
   * @param bCreateIfNotExisting
   *        if <code>true</code> if a new session web scope is created, if none
   *        is present
   * @param bItsOkayToCreateANewScope
   *        if <code>true</code> no warning is emitted, if a new session scope
   *        must be created. This is e.g. used when renewing a session or when
   *        activating a previously passivated session.
   * @return <code>null</code> if no session scope is present, and
   *         bCreateIfNotExisting is false
   */
  @Nullable
  @DevelopersNote ("This is only for project-internal use!")
  public static ISessionWebScope internalGetOrCreateSessionScope (@Nonnull final HttpSession aHttpSession,
                                                                  final boolean bCreateIfNotExisting,
                                                                  final boolean bItsOkayToCreateANewScope)
  {
    if (aHttpSession == null)
      throw new NullPointerException ("httpSession");

    // Do we already have a session web scope for the session?
    final String sSessionID = aHttpSession.getId ();
    ISessionScope aSessionWebScope = ScopeSessionManager.getInstance ().getSessionScopeOfID (sSessionID);
    if (aSessionWebScope == null && bCreateIfNotExisting)
    {
      if (!bItsOkayToCreateANewScope)
      {
        // This can e.g. happen in tests, when there are no registered
        // listeners for session events!
        s_aLogger.warn ("Creating a new session web scope for ID '" +
                        sSessionID +
                        "' but there should already be one!" +
                        " Check your HttpSessionListener implementation." +
                        " See com.phloc.scopes.web.servlet.WebScopeListener for an example.");
      }

      // Create a new session scope
      aSessionWebScope = onSessionBegin (aHttpSession);
    }

    try
    {
      return (ISessionWebScope) aSessionWebScope;
    }
    catch (final ClassCastException ex)
    {
      throw new IllegalStateException ("Session scope object is not a web scope!");
    }
  }

  /**
   * Get or create a session scope based on the current request scope. This is
   * the same as calling
   * <code>getSessionScope({@link ScopeManager#DEFAULT_CREATE_SCOPE})</code>
   * 
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static ISessionWebScope getSessionScope ()
  {
    return getSessionScope (ScopeManager.DEFAULT_CREATE_SCOPE);
  }

  /**
   * Get the session scope from the current request scope.
   * 
   * @param bCreateIfNotExisting
   *        if <code>true</code> a new session scope (and a new HTTP session if
   *        required) is created if none is existing so far.
   * @return <code>null</code> if no session scope is present, and none should
   *         be created.
   */
  @Nullable
  public static ISessionWebScope getSessionScope (final boolean bCreateIfNotExisting)
  {
    return internalGetSessionScope (bCreateIfNotExisting, false);
  }

  /**
   * Get the session scope from the current request scope.
   * 
   * @param bCreateIfNotExisting
   *        if <code>true</code> a new session scope (and a new HTTP session if
   *        required) is created if none is existing so far.
   * @param bItsOkayToCreateANewSession
   *        if <code>true</code> no warning is emitted, if a new session scope
   *        must be created. This is e.g. used when renewing a session.
   * @return <code>null</code> if no session scope is present, and none should
   *         be created.
   */
  @Nullable
  @DevelopersNote ("This is only for project-internal use!")
  public static ISessionWebScope internalGetSessionScope (final boolean bCreateIfNotExisting,
                                                          final boolean bItsOkayToCreateANewSession)
  {
    // Try to to resolve the current request scope
    final IRequestWebScope aRequestScope = getRequestScopeOrNull ();
    if (aRequestScope != null)
    {
      // Check if we have an HTTP session object
      final HttpSession aHttpSession = aRequestScope.getSession (bCreateIfNotExisting);
      if (aHttpSession != null)
        return internalGetOrCreateSessionScope (aHttpSession, bCreateIfNotExisting, bItsOkayToCreateANewSession);
    }
    else
    {
      // If we want a session scope, we expect the return value to be non-null!
      if (bCreateIfNotExisting)
        throw new IllegalStateException ("No request scope is present, so no session scope can be retrieved!");
    }
    return null;
  }

  /**
   * To be called, when a session web scope is destroyed. Most commonly this is
   * called from within
   * {@link javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)}
   * 
   * @param aHttpSession
   *        The source session to destroy the matching scope. May not be
   *        <code>null</code>
   */
  public static void onSessionEnd (@Nonnull final HttpSession aHttpSession)
  {
    if (aHttpSession == null)
      throw new NullPointerException ("httpSession");

    final ScopeSessionManager aSSM = ScopeSessionManager.getInstance ();
    final ISessionScope aSessionScope = aSSM.getSessionScopeOfID (aHttpSession.getId ());
    if (aSessionScope != null)
    {
      // Regular scope end
      aSSM.onScopeEnd (aSessionScope);
    }
    else
    {
      // Ensure session is invalidated anyhow, even if no session scope is
      // present.
      // Happens in Tomcat startup if sessions that where serialized in
      // a previous invocation are invalidated on Tomcat restart
      s_aLogger.warn ("Found no session scope but invalidating session '" + aHttpSession.getId () + "' anyway");
      try
      {
        aHttpSession.invalidate ();
      }
      catch (final IllegalStateException ex)
      {
        // session already invalidated
      }
    }
  }

  // --- session application scope ---

  @Nonnull
  public static ISessionApplicationWebScope getSessionApplicationScope ()
  {
    return getSessionApplicationScope (ScopeManager.DEFAULT_CREATE_SCOPE);
  }

  @Nullable
  public static ISessionApplicationWebScope getSessionApplicationScope (final boolean bCreateIfNotExisting)
  {
    return getSessionApplicationScope (ScopeManager.getRequestApplicationID (), bCreateIfNotExisting);
  }

  @Nonnull
  public static ISessionApplicationWebScope getSessionApplicationScope (@Nonnull @Nonempty final String sApplicationID)
  {
    return getSessionApplicationScope (sApplicationID, ScopeManager.DEFAULT_CREATE_SCOPE);
  }

  @Nullable
  public static ISessionApplicationWebScope getSessionApplicationScope (@Nonnull @Nonempty final String sApplicationID,
                                                                        final boolean bCreateIfNotExisting)
  {
    final ISessionWebScope aSessionScope = getSessionScope (bCreateIfNotExisting);
    return aSessionScope == null ? null : aSessionScope.getSessionApplicationScope (sApplicationID,
                                                                                    bCreateIfNotExisting);
  }

  // --- request scopes ---

  @Nonnull
  public static IRequestWebScope onRequestBegin (@Nonnull final String sApplicationID,
                                                 @Nonnull final HttpServletRequest aHttpRequest,
                                                 @Nonnull final HttpServletResponse aHttpResponse)
  {
    final IRequestWebScope aRequestScope = MetaWebScopeFactory.getWebScopeFactory ().createRequestScope (aHttpRequest,
                                                                                                      aHttpResponse);
    ScopeManager.setAndInitRequestScope (sApplicationID, aRequestScope);
    return aRequestScope;
  }

  @Nullable
  public static IRequestWebScope getRequestScopeOrNull ()
  {
    final IRequestScope aRequestScope = ScopeManager.getRequestScopeOrNull ();
    try
    {
      return (IRequestWebScope) aRequestScope;
    }
    catch (final ClassCastException ex)
    {
      s_aLogger.warn ("Request scope object is not a request web scope: " + aRequestScope);
      return null;
    }
  }

  public static boolean isRequestScopePresent ()
  {
    return ScopeManager.getRequestScopeOrNull () instanceof IRequestWebScope;
  }

  @Nonnull
  public static IRequestWebScope getRequestScope ()
  {
    final IRequestWebScope aRequestScope = getRequestScopeOrNull ();
    if (aRequestScope == null)
      throw new IllegalStateException ("No request web scope object has been set!");
    return aRequestScope;
  }

  public static void onRequestEnd ()
  {
    ScopeManager.onRequestEnd ();
  }
}
