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
package com.phloc.webbasics.action;

import javax.annotation.Nonnull;

import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Callback interface to handle thrown exception objects on action execution.
 * 
 * @author Philip Helger
 */
public interface IActionExceptionHandler
{
  /**
   * Called when an exception in action execution occurs
   * 
   * @param t
   *        The exception. Never <code>null</code>.
   * @param sActionName
   *        The action that should be executed
   * @param aRequestScope
   *        The request scope. Never <code>null</code>.
   */
  void onActionExecutionException (@Nonnull Throwable t,
                                   @Nonnull String sActionName,
                                   @Nonnull IRequestWebScopeWithoutResponse aRequestScope);
}
