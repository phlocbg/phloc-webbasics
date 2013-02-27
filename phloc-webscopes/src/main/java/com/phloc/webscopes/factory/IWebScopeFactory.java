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
package com.phloc.webscopes.factory;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.webscopes.domain.IApplicationWebScope;
import com.phloc.webscopes.domain.IGlobalWebScope;
import com.phloc.webscopes.domain.IRequestWebScope;
import com.phloc.webscopes.domain.ISessionApplicationWebScope;
import com.phloc.webscopes.domain.ISessionWebScope;

/**
 * Interface for a web scope factory.
 * 
 * @author philip
 */
public interface IWebScopeFactory
{
  /**
   * Create a new global web scope.
   * 
   * @param aServletContext
   *        The servlet context to use
   * @return Never <code>null</code>.
   */
  @Nonnull
  IGlobalWebScope createGlobalScope (@Nonnull ServletContext aServletContext);

  /**
   * Create a new application scope
   * 
   * @param sScopeID
   *        The scope ID to use
   * @return Never <code>null</code>.
   */
  @Nonnull
  IApplicationWebScope createApplicationScope (@Nonnull @Nonempty String sScopeID);

  /**
   * Create a new session scope
   * 
   * @param aHttpSession
   *        The underlying HTTP session. May not be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  ISessionWebScope createSessionScope (@Nonnull HttpSession aHttpSession);

  /**
   * Create a new session application scope
   * 
   * @param sScopeID
   *        The scope ID to use
   * @return Never <code>null</code>.
   */
  @Nonnull
  ISessionApplicationWebScope createSessionApplicationScope (@Nonnull @Nonempty String sScopeID);

  /**
   * Create a new request scope
   * 
   * @param aHttpRequest
   *        The HTTP servlet request. May not be <code>null</code>.
   * @param aHttpResponse
   *        The HTTP servlet response. May not be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  IRequestWebScope createRequestScope (@Nonnull HttpServletRequest aHttpRequest,
                                       @Nonnull HttpServletResponse aHttpResponse);
}
