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
import javax.annotation.Nullable;

import com.phloc.webbasics.ajax.servlet.AbstractAjaxServlet;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Callback interface to handle thrown exception objects from the
 * {@link AbstractAjaxServlet}.
 * 
 * @author Philip Helger
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
   * @param aRequestScope
   *        The request scope. Never <code>null</code>.
   */
  void onAjaxException (@Nonnull Throwable t,
                        @Nullable String sAjaxFunctionName,
                        @Nonnull IRequestWebScopeWithoutResponse aRequestScope);
}
