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
package com.phloc.webscopes.domain;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.ServletContext;

import com.phloc.scopes.nonweb.domain.IGlobalScope;
import com.phloc.webscopes.IWebScope;

/**
 * Interface for a global web scope.
 * 
 * @author philip
 */
public interface IGlobalWebScope extends IGlobalScope, IWebScope
{
  @Nullable
  IApplicationWebScope getApplicationScope (String sAppID, boolean bCreateIfNotExisting);

  /**
   * @return The underlying servlet context. Never <code>null</code>.
   */
  @Nonnull
  ServletContext getServletContext ();

  /**
   * Returns the context path of the web application.
   * <p>
   * The context path is the portion of the request URI that is used to select
   * the context of the request. The context path always comes first in a
   * request URI. The path starts with a "/" character but does not end with a
   * "/" character. For servlets in the default (root) context, this method
   * returns "".
   * <p>
   * It is possible that a servlet container may match a context by more than
   * one context path. In such cases the context path will return the actual
   * context path used by the request and it may differ from the path returned
   * by this method. The context path returned by this method should be
   * considered as the prime or preferred context path of the application.
   * 
   * @return The context path of the web application, or "" for the default
   *         (root) context
   */
  @Nonnull
  String getContextPath ();
}
