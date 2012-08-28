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
package com.phloc.webbasics.ajax;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Callback interface to handle thrown exception objects from the
 * {@link DefaultAjaxServlet}.
 * 
 * @author philip
 */
public interface IAjaxExceptionHandler
{
  /**
   * Called when an exception of the specified type occurred
   * 
   * @param t
   *        The exception. Never <code>null</code>.
   * @param sAjaxFunctionName
   *        The AJAX function that should have been involved
   * @param aHttpRequest
   *        The HTTP request
   * @param aHttpResponse
   *        The HTTP response
   */
  void onAjaxException (@Nonnull Throwable t,
                        @Nullable String sAjaxFunctionName,
                        @Nonnull HttpServletRequest aHttpRequest,
                        @Nonnull HttpServletResponse aHttpResponse);
}
