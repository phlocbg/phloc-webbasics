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
package com.phloc.webbasics.action;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.ESuccess;
import com.phloc.web.servlet.response.UnifiedResponse;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

public interface IActionInvoker
{
  void setCustomExceptionHandler (@Nullable IActionExceptionHandler aExceptionHandler);

  @Nullable
  IActionExceptionHandler getCustomExceptionHandler ();

  void addAction (@Nonnull String sAction, @Nonnull IActionExecutor aActionExecutor);

  @Nonnull
  ESuccess executeAction (@Nullable String sActionName,
                          @Nonnull IRequestWebScopeWithoutResponse aRequestScope,
                          @Nonnull UnifiedResponse aUnifiedResponse) throws Exception;

  /**
   * @return A map from actionID to action executor. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  Map <String, IActionExecutor> getAllActions ();

  /**
   * Check whether an action with the given name is present
   * 
   * @param sName
   *        The name of the action to check. May be <code>null</code>.
   * @return <code>true</code> if an action with the given name is contained,
   *         <code>false</code> otherwise.
   */
  boolean containsAction (@Nullable String sName);

  /**
   * Get the executor associated with the given action.
   * 
   * @param sName
   *        The name of the action to check. May be <code>null</code>.
   * @return <code>null</code> if no such action exists.
   */
  @Nullable
  IActionExecutor getActionExecutor (@Nullable String sName);
}
