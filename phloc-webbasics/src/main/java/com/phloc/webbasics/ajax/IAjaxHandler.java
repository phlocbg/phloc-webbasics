/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.webbasics.ajax;

import javax.annotation.Nonnull;

import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Base interface for a single AJAX function handler.
 *
 * @author Philip Helger
 */
public interface IAjaxHandler
{
  /**
   * Registers all external resources (CSS or JS files) needed by controls
   * potentially spawned by an AJAX request of this handler. This method is
   * called BEFORE {@link #handleRequest(IRequestWebScopeWithoutResponse)} is
   * invoked!
   */
  void registerExternalResources ();

  /**
   * Called to handle a specific request.
   *
   * @param aRequestScope
   *        the request scope values to be used
   * @return the result object. May never be <code>null</code>
   * @throws Exception
   *         Any exception if an error occurs.
   */
  @Nonnull
  IAjaxResponse handleRequest (@Nonnull IRequestWebScopeWithoutResponse aRequestScope) throws Exception;
}
