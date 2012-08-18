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
package com.phloc.appbasics.security.audit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.type.ObjectType;

/**
 * Main interface for an auditing service.
 * 
 * @author philip
 */
public interface IAuditor
{
  /**
   * The creation of an object succeeded.
   * 
   * @param aObjectType
   *        The object type
   * @param aArgs
   *        Additional arguments
   */
  void onCreateSuccess (@Nonnull ObjectType aObjectType, @Nullable String... aArgs);

  /**
   * The creation of an object failed.
   * 
   * @param aObjectType
   *        The object type
   * @param aArgs
   *        Additional arguments
   */
  void onCreateFailure (@Nonnull ObjectType aObjectType, @Nullable String... aArgs);

  /**
   * The modification of an object succeeded.
   * 
   * @param aObjectType
   *        The object type
   * @param sWhat
   *        What was modified?
   * @param aArgs
   *        Additional arguments
   */
  void onModifySuccess (@Nonnull ObjectType aObjectType, @Nonnull String sWhat, @Nullable String... aArgs);

  /**
   * The modification of an object failed.
   * 
   * @param aObjectType
   *        The object type
   * @param sWhat
   *        What was modified?
   * @param aArgs
   *        Additional arguments
   */
  void onModifyFailure (@Nonnull ObjectType aObjectType, @Nonnull String sWhat, @Nullable String... aArgs);

  /**
   * The deletion of an object succeeded.
   * 
   * @param aObjectType
   *        The object type
   * @param aArgs
   *        Additional arguments
   */
  void onDeleteSuccess (@Nonnull ObjectType aObjectType, @Nullable String... aArgs);

  /**
   * The deletion of an object failed.
   * 
   * @param aObjectType
   *        The object type
   * @param aArgs
   *        Additional arguments
   */
  void onDeleteFailure (@Nonnull ObjectType aObjectType, @Nullable String... aArgs);

  /**
   * The execution of something succeeded.
   * 
   * @param sWhat
   *        What has been executed?
   * @param aArgs
   *        Additional arguments
   */
  void onExecuteSuccess (@Nonnull String sWhat, @Nullable String... aArgs);

  /**
   * The execution of something failed.
   * 
   * @param sWhat
   *        What has been executed?
   * @param aArgs
   *        Additional arguments
   */
  void onExecuteFailure (@Nonnull String sWhat, @Nullable String... aArgs);

  /**
   * The execution of something on an object succeeded.
   * 
   * @param aObjectType
   *        The object type
   * @param sWhat
   *        What has been executed?
   * @param aArgs
   *        Additional arguments
   */
  void onExecuteSuccess (@Nonnull ObjectType aObjectType, @Nonnull String sWhat, @Nullable String... aArgs);

  /**
   * The execution of something on an object failed.
   * 
   * @param aObjectType
   *        The object type
   * @param sWhat
   *        What has been executed?
   * @param aArgs
   *        Additional arguments
   */
  void onExecuteFailure (@Nonnull ObjectType aObjectType, @Nonnull String sWhat, @Nullable String... aArgs);
}
