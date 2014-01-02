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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.webscopes.IWebScope;

/**
 * This enumeration defines all the possible web scopes including some utility
 * methods on it.
 * 
 * @author Philip Helger
 */
public enum EWebScope
{
  /** The global scope. */
  GLOBAL,
  /** The application scope. */
  APPLICATION,
  /** The session scope */
  SESSION,
  /** The session application scope */
  SESSION_APPLICATION,
  /** The request scope. */
  REQUEST;

  /**
   * @return The current {@link IWebScope} object for this enum. Aequivalent to
   *         <code>getScope(true)</code> and therefore never <code>null</code>.
   */
  @Nonnull
  public IWebScope getScope ()
  {
    return getScope (true);
  }

  /**
   * Get the current web scope object of this enum entry.
   * 
   * @param bCreateIfNotExisting
   *        if <code>true</code> the scope is created if it is not existing.
   * @return <code>null</code> if the scope is not existing yet and should not
   *         be created. Always non-<code>null</code> if the parameter is
   *         <code>true</code>.
   */
  @Nullable
  public IWebScope getScope (final boolean bCreateIfNotExisting)
  {
    return getScope (this, bCreateIfNotExisting);
  }

  /**
   * Resolve the currently matching web scope of the given {@link EWebScope}
   * value.
   * 
   * @param eWebScope
   *        The web scope to resolve to a real scope. May not be
   *        <code>null</code>.
   * @param bCreateIfNotExisting
   *        if <code>false</code> and the scope is not existing,
   *        <code>null</code> will be returned. This parameter is only used in
   *        application, session and session application scopes.
   * @return The matching IWebScope.
   * @throws IllegalArgumentException
   *         If an illegal enumeration value is passed.
   */
  @Nullable
  public static IWebScope getScope (@Nonnull final EWebScope eWebScope, final boolean bCreateIfNotExisting)
  {
    switch (eWebScope)
    {
      case GLOBAL:
        return WebScopeManager.getGlobalScope ();
      case APPLICATION:
        return WebScopeManager.getApplicationScope (bCreateIfNotExisting);
      case SESSION:
        return WebScopeManager.getSessionScope (bCreateIfNotExisting);
      case SESSION_APPLICATION:
        return WebScopeManager.getSessionApplicationScope (bCreateIfNotExisting);
      case REQUEST:
        return WebScopeManager.getRequestScope ();
      default:
        throw new IllegalArgumentException ("Unknown web scope: " + eWebScope);
    }
  }
}
