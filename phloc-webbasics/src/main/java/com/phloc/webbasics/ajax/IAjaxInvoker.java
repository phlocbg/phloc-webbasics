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

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.factory.IFactory;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Base interface for an Ajax invoker. It has a set of {@link IAjaxHandler}
 * instances that it can invoke.
 *
 * @author Philip Helger
 */
public interface IAjaxInvoker
{
  @Nullable
  IAjaxBeforeExecutionHandler getBeforeExecutionHandler ();

  void setBeforeExecutionHandler (@Nullable IAjaxBeforeExecutionHandler aBeforeExecutionHdl);

  @Nullable
  IAjaxAfterExecutionHandler getAfterExecutionHandler ();

  void setAfterExecutionHandler (@Nullable IAjaxAfterExecutionHandler aAfterExecutionHdl);

  /**
   * @return The milliseconds after which an execution is considered long
   *         running.
   */
  long getLongRunningExecutionLimitTime ();

  /**
   * Set the milliseconds after which an execution is considered long running.
   *
   * @param nLongRunningExecutionLimitTime
   *        The milliseconds to use. Value &le; 0 are considered "no limit"
   */
  void setLongRunningExecutionLimitTime (long nLongRunningExecutionLimitTime);

  @Nullable
  IAjaxLongRunningExecutionHandler getLongRunningExecutionHandler ();

  void setLongRunningExecutionHandler (@Nullable IAjaxLongRunningExecutionHandler aLongRunningExecutionHdl);

  @Nonnull
  @ReturnsMutableCopy
  Map <String, IFactory <? extends IAjaxHandler>> getAllRegisteredHandlers ();

  @Nullable
  IFactory <? extends IAjaxHandler> getRegisteredHandler (@Nullable String sFunctionName);

  boolean isRegisteredFunction (@Nullable String sFunctionName);

  /**
   * Add a handler function that is used as a callback.
   *
   * @param aFunction
   *        The Ajax function declaration to be invoked. May not be
   *        <code>null</code>.
   * @param aClass
   *        The class to be instantiated each time the function is invoked. May
   *        not be <code>null</code>.
   */
  void addHandlerFunction (@Nonnull IAjaxFunctionDeclaration aFunction, @Nonnull Class <? extends IAjaxHandler> aClass);

  /**
   * Add a handler function that is used as a callback.
   *
   * @param aFunction
   *        The Ajax function declaration to be invoked. May not be
   *        <code>null</code>.
   * @param aFactory
   *        The factory creating the respective handler function. May not be
   *        <code>null</code>.
   */
  void addHandlerFunction (@Nonnull IAjaxFunctionDeclaration aFunction,
                           @Nonnull IFactory <? extends IAjaxHandler> aFactory);

  /**
   * Add a handler function that is used as a callback.
   *
   * @param sFunctionName
   *        Name AJAX function to be invoked. May not be <code>null</code>.
   * @param aClass
   *        The class to be instantiated each time the function is invoked. May
   *        not be <code>null</code>.
   */
  void addHandlerFunction (@Nonnull String sFunctionName, @Nonnull Class <? extends IAjaxHandler> aClass);

  /**
   * Add a handler function that is used as a callback.
   *
   * @param sFunctionName
   *        Name AJAX function to be invoked. May not be <code>null</code>.
   * @param aFactory
   *        The factory creating the respective handler function. May not be
   *        <code>null</code>.
   */
  void addHandlerFunction (@Nonnull String sFunctionName, @Nonnull IFactory <? extends IAjaxHandler> aFactory);

  /**
   * Invoke the specified AJAX function.
   *
   * @param sFunctionName
   *        the alias of the AJAX function to invoke. May not be
   *        <code>null</code>.
   * @param aRequestWebScope
   *        The request scope to be used for the function.
   * @return <code>null</code> if this is a void function, the JS string to
   *         serialize otherwise.
   * @throws Exception
   *         on reflection error
   * @throws IllegalArgumentException
   *         If no handler is registered for the specified function name.
   */
  @Nonnull
  IAjaxResponse invokeFunction (@Nonnull String sFunctionName, @Nonnull IRequestWebScopeWithoutResponse aRequestWebScope) throws Exception;
}
