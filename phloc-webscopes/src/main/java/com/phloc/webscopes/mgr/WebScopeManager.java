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
package com.phloc.webscopes.mgr;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.DevelopersNote;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;
import com.phloc.scopes.domain.IApplicationScope;
import com.phloc.scopes.domain.IGlobalScope;
import com.phloc.scopes.domain.IRequestScope;
import com.phloc.scopes.domain.ISessionScope;
import com.phloc.scopes.mgr.ScopeManager;
import com.phloc.scopes.mgr.ScopeSessionManager;
import com.phloc.webscopes.MetaWebScopeFactory;
import com.phloc.webscopes.domain.IApplicationWebScope;
import com.phloc.webscopes.domain.IGlobalWebScope;
import com.phloc.webscopes.domain.IRequestWebScope;
import com.phloc.webscopes.domain.ISessionApplicationWebScope;
import com.phloc.webscopes.domain.ISessionWebScope;
import com.phloc.webscopes.session.SessionWebScopeActivator;

/**
 * This is the main manager class for web scope handling.
 * 
 * @author Boris Gregorcic
 */
@Immutable
public final class WebScopeManager
{
  // For backward compatibility passivation is disabled
  public static final boolean DEFAULT_SESSION_PASSIVATION_ALLOWED = false;
  private static final String SESSION_ATTR_SESSION_SCOPE_ACTIVATOR = "$phloc.sessionwebscope.activator"; //$NON-NLS-1$
  private static final Logger LOG = LoggerFactory.getLogger (WebScopeManager.class);
  private static final AtomicBoolean s_aSessionPassivationAllowed = new AtomicBoolean (DEFAULT_SESSION_PASSIVATION_ALLOWED);

  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  private static final Set <String> s_aSessionsInInvalidation = new HashSet <String> ();

  /**
   * Temporary session pointer, used to be able to point to a session that is
   * currently destroyed (necessary and available only inside the session
   * listener when reacting to the session destroyed event)
   */
  private static final ThreadLocal <String> CREATING_SESSION_ID = new ThreadLocal <String> ();
  private static final ThreadLocal <String> DYING_SESSION_ID = new ThreadLocal <String> ();

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

  @Nullable
  public static String getCreatingSessionID ()
  {
    return CREATING_SESSION_ID.get ();
  }

  @Nullable
  public static String getDyingSessionID ()
  {
    return DYING_SESSION_ID.get ();
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
        {
          aHttpSession.setAttribute (SESSION_ATTR_SESSION_SCOPE_ACTIVATOR,
                                     new SessionWebScopeActivator (aSessionWebScope));
        }
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
   * {@link jakarta.servlet.ServletContextListener#contextInitialized(jakarta.servlet.ServletContextEvent)}
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
      LOG.warn ("Gobal scope object is not a global web scope: " + String.valueOf (aGlobalScope), ex); //$NON-NLS-1$
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
    {
      throw new IllegalStateException ("No global web scope object has been set!"); //$NON-NLS-1$
    }
    return aGlobalScope;
  }

  /**
   * To be called, when the global web scope is destroyed. Most commonly this is
   * called from within
   * {@link jakarta.servlet.ServletContextListener#contextDestroyed(jakarta.servlet.ServletContextEvent)}
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
    final IApplicationWebScope aAppScope = getApplicationScope (true);
    if (aAppScope == null)
    {
      throw new IllegalStateException ("No application web scope object has been set!"); //$NON-NLS-1$
    }
    return aAppScope;
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
    final IApplicationScope aAppScope = ScopeManager.getApplicationScope (bCreateIfNotExisting);
    try
    {
      return (IApplicationWebScope) aAppScope;
    }
    catch (final ClassCastException ex)
    {
      LOG.warn ("Application scope object is not an application web scope: " + String.valueOf (aAppScope), ex); //$NON-NLS-1$
      return null;
    }
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
    final IApplicationWebScope aAppScope = getApplicationScope (sApplicationID, true);
    if (aAppScope == null)
    {
      throw new IllegalStateException ("No application web scope object for application ID '" + //$NON-NLS-1$
                                       sApplicationID +
                                       "' is present!"); //$NON-NLS-1$
    }
    return aAppScope;
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
    final IApplicationScope aAppScope = ScopeManager.getApplicationScope (sApplicationID, bCreateIfNotExisting);
    try
    {
      return (IApplicationWebScope) aAppScope;
    }
    catch (final ClassCastException ex)
    {
      LOG.warn ("Application scope object is not an application web scope: " + String.valueOf (aAppScope), ex); //$NON-NLS-1$
      return null;
    }
  }

  // --- session scope ---

  /**
   * To be called, when a session web scope is initialized. Most commonly this
   * is called from within
   * {@link jakarta.servlet.http.HttpSessionListener#sessionCreated(jakarta.servlet.http.HttpSessionEvent)}
   * 
   * @param aHttpSession
   *        The source session to base the scope on. May not be
   *        <code>null</code>
   * @return The created global session scope
   */
  @Nonnull
  public static ISessionWebScope onSessionBegin (@Nonnull final HttpSession aHttpSession)
  {
    final ISessionWebScope aSessionWebScope = MetaWebScopeFactory.getWebScopeFactory ()
                                                                 .createSessionScope (aHttpSession);
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
    ValueEnforcer.notNull (aHttpSession, "HttpSession"); //$NON-NLS-1$

    // Do we already have a session web scope for the session?
    final String sSessionID = aHttpSession.getId ();
    ISessionScope aSessionWebScope = ScopeSessionManager.getInstance ().getSessionScopeOfID (sSessionID);
    if (aSessionWebScope == null && bCreateIfNotExisting)
    {
      if (!bItsOkayToCreateANewScope)
      {
        if (!isRequestSessionValid ())
        {
          final String sMessage = "Not creating scope for already invalidated session!";//$NON-NLS-1$
          if (GlobalDebug.isDebugMode ())
          {
            LOG.error (sMessage, new Exception ("Illegal scope creation")); //$NON-NLS-1$
          }
          else
          {
            LOG.warn (sMessage);
          }
          return null;
        }
        // This can e.g. happen in tests, when there are no registered
        // listeners for session events!
        final String sMessage = "Creating a new session web scope for ID '" //$NON-NLS-1$
                                + sSessionID + "' but there should already be one!" //$NON-NLS-1$
                                + " Check your HttpSessionListener implementation." //$NON-NLS-1$
                                + " See com.phloc.scopes.web.servlet.WebScopeListener for an example.";//$NON-NLS-1$
        if (GlobalDebug.isDebugMode ())
        {
          LOG.error (sMessage, new Exception ("Illegal scope creation")); //$NON-NLS-1$
        }
        else
        {
          LOG.warn (sMessage);
        }
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
      throw new IllegalStateException ("Session scope object is not a web scope but: " + //$NON-NLS-1$
                                       String.valueOf (aSessionWebScope),
                                       ex);
    }
  }

  public static boolean isRequestSessionValid ()
  {
    final IRequestWebScope aRequestScope = getRequestScopeOrNull ();
    if (aRequestScope != null)
    {
      return aRequestScope.getRequest ().isRequestedSessionIdValid ();
    }
    return false;
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
    // If there is a scope currently set to 'dying', use that one (we are inside
    // a session listener implementation!)
    final String sDyingSessionID = DYING_SESSION_ID.get ();
    if (StringHelper.hasText (sDyingSessionID))
    {
      final ISessionWebScope aDyingSessionScope = WebScopeSessionManager.getSessionWebScopeOfID (sDyingSessionID);
      if (aDyingSessionScope != null)
      {
        return aDyingSessionScope;
      }
    }
    // Try to to resolve the current request scope
    final IRequestWebScope aRequestScope = getRequestScopeOrNull ();
    if (aRequestScope != null)
    {
      // Check if we have an HTTP session object
      final HttpSession aHttpSession = aRequestScope.getSession (bCreateIfNotExisting);
      if (aHttpSession != null)
      {
        return internalGetOrCreateSessionScope (aHttpSession, bCreateIfNotExisting, bItsOkayToCreateANewSession);
      }
    }
    else
    {
      // If we want a session scope, we expect the return value to be non-null!
      if (bCreateIfNotExisting)
      {
        throw new IllegalStateException ("No request scope is present, so no session scope can be retrieved!"); //$NON-NLS-1$
      }
    }
    return null;
  }

  /**
   * To be called when the session listener detects that a session was/is
   * created. This will mark the session and allow reusing the session scope in
   * the course of listener implementation without creating additional sessions
   * 
   * @param aHttpSession
   *        The HTTP session that was detected to have started
   */
  public static void onDetectSessionStart (@Nonnull final HttpSession aHttpSession)
  {
    if (aHttpSession == null)
    {
      throw new NullPointerException ("aHttpSession"); //$NON-NLS-1$
    }
    CREATING_SESSION_ID.set (aHttpSession.getId ());
    DYING_SESSION_ID.remove ();
  }

  public static void onFinishedSessionStart (@Nonnull final HttpSession aHttpSession)
  {
    if (aHttpSession == null)
    {
      throw new NullPointerException ("aHttpSession"); //$NON-NLS-1$
    }
    CREATING_SESSION_ID.remove ();
    DYING_SESSION_ID.remove ();
  }

  /**
   * To be called when the session listener detects that a session was/is
   * destroyed. This will mark the session and allow accessing the session scope
   * in the method {@link #onSessionEnd(HttpSession)}
   * 
   * @param aHttpSession
   *        The HTTP session that was detected to have ended
   */
  public static void onDetectSessionEnd (@Nonnull final HttpSession aHttpSession)
  {
    if (aHttpSession == null)
    {
      throw new NullPointerException ("aHttpSession"); //$NON-NLS-1$
    }
    DYING_SESSION_ID.set (aHttpSession.getId ());
    CREATING_SESSION_ID.remove ();
  }

  /**
   * To be called, when a session web scope is destroyed. Most commonly this is
   * called from within
   * {@link jakarta.servlet.http.HttpSessionListener#sessionDestroyed(jakarta.servlet.http.HttpSessionEvent)}
   * 
   * @param aHttpSession
   *        The source session to destroy the matching scope. May not be
   *        <code>null</code>
   */
  public static void onSessionEnd (@Nonnull final HttpSession aHttpSession)
  {
    if (aHttpSession == null)
    {
      throw new NullPointerException ("aHttpSession"); //$NON-NLS-1$
    }
    final ScopeSessionManager aSSM = ScopeSessionManager.getInstance ();
    final String sSessionID = aHttpSession.getId ();
    final ISessionScope aSessionScope = aSSM.getSessionScopeOfID (sSessionID);
    if (aSessionScope != null)
    {
      // Regular scope end
      aSSM.onScopeEnd (aSessionScope);
    }
    else
    {
      // Ensure session is invalidated anyhow, even if no session scope is
      // present.
      // Happens in Tomcat startup if sessions that where serialized in a
      // previous invocation are invalidated on Tomcat restart

      // Ensure that session.invalidate can not be called recursively
      boolean bCanInvalidateSession;
      s_aRWLock.writeLock ().lock ();
      try
      {
        bCanInvalidateSession = s_aSessionsInInvalidation.add (sSessionID);
      }
      finally
      {
        s_aRWLock.writeLock ().unlock ();
      }

      if (bCanInvalidateSession)
      {
        try
        {
          // this is actually fine when the scope is first ended and then
          // triggers the session invalidate, then there is no scope on session
          // invalidate ;-)
          aHttpSession.invalidate ();
        }
        catch (final IllegalStateException ex)
        {
          // session already invalidated
        }
        finally
        {
          // Remove from "in invalidation" list
          s_aRWLock.writeLock ().lock ();
          try
          {
            s_aSessionsInInvalidation.remove (sSessionID);
          }
          finally
          {
            s_aRWLock.writeLock ().unlock ();
          }
        }
      }
    }
    CREATING_SESSION_ID.remove ();
    DYING_SESSION_ID.remove ();
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
    return aSessionScope == null ? null
                                 : aSessionScope.getSessionApplicationScope (sApplicationID, bCreateIfNotExisting);
  }

  // --- request scopes ---

  @Nonnull
  public static IRequestWebScope onRequestBegin (@Nonnull final String sApplicationID,
                                                 @Nonnull final HttpServletRequest aHttpRequest,
                                                 @Nonnull final HttpServletResponse aHttpResponse)
  {
    final IRequestWebScope aRequestScope = MetaWebScopeFactory.getWebScopeFactory ()
                                                              .createRequestScope (aHttpRequest, aHttpResponse);
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
      LOG.warn ("Request scope object is not a request web scope: " + String.valueOf (aRequestScope), ex); //$NON-NLS-1$
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
    {
      throw new IllegalStateException ("No request web scope object has been set!"); //$NON-NLS-1$
    }
    return aRequestScope;
  }

  public static void onRequestEnd ()
  {
    ScopeManager.onRequestEnd ();
  }
}
