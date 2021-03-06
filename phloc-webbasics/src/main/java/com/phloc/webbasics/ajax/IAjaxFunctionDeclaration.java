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

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.name.IHasName;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Interface for all ajax function declarations
 *
 * @author Philip Helger
 */
public interface IAjaxFunctionDeclaration extends IHasName
{
  /**
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @return The URI where the AJAX function can be invoked. Neither
   *         <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  String getInvocationURI (@Nonnull IRequestWebScopeWithoutResponse aRequestScope);

  /**
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @param aParams
   *        An optional map with URL parameters to be used in the URL. May be
   *        <code>null</code> or empty.
   * @return The URI where the AJAX function can be invoked. Neither
   *         <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  String getInvocationURI (@Nonnull IRequestWebScopeWithoutResponse aRequestScope,
                           @Nullable Map <String, String> aParams);

  /**
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @return The URL where the AJAX function can be invoked. Never
   *         <code>null</code>.
   */
  @Nonnull
  ISimpleURL getInvocationURL (@Nonnull IRequestWebScopeWithoutResponse aRequestScope);

  /**
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @param aParams
   *        An optional map with URL parameters to be used in the URL. May be
   *        <code>null</code> or empty.
   * @return The URL where the AJAX function can be invoked. Never
   *         <code>null</code>.
   */
  @Nonnull
  ISimpleURL getInvocationURL (@Nonnull IRequestWebScopeWithoutResponse aRequestScope,
                               @Nullable Map <String, String> aParams);
}
