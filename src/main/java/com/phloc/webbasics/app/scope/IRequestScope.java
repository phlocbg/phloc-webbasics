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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phloc.commons.annotations.Nonempty;

/**
 * Request scope.
 * 
 * @author philip
 */
public interface IRequestScope extends IScope
{
  /**
   * Get the underlying HTTP servlet request. Important: do not use it to access
   * the attributes within the session. Use only the scope API for this, so that
   * the synchronization is consistent!
   * 
   * @return The underlying HTTP servlet request. Never <code>null</code>.
   */
  @Nonnull
  HttpServletRequest getRequest ();

  /**
   * @return The underlying HTTP servlet response. Never <code>null</code>.
   */
  @Nonnull
  HttpServletResponse getResponse ();

  /**
   * @return The user agent of this request. Extracted from header fields of the
   *         HTTP servlet request.
   */
  @Nullable
  String getUserAgent ();

  /**
   * @return The full context path of the request. E.g.
   *         <code>http://server.com:8080/context</code>. Never ends with a "/".
   */
  @Nonnull
  @Nonempty
  String getFullContextPath ();
}
