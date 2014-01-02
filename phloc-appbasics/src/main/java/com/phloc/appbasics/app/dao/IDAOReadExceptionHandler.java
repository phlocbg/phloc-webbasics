/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.appbasics.app.dao;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.io.IReadableResource;

/**
 * Callback interface to handle thrown exception objects on DAO reading.
 * 
 * @author Philip Helger
 */
public interface IDAOReadExceptionHandler
{
  /**
   * Called when an exception of the specified type occurred
   * 
   * @param t
   *        The exception. Never <code>null</code>.
   * @param bInit
   *        If <code>true</code>, it is an init action, else it is a read action
   * @param aRes
   *        The resource that failed. May be <code>null</code> if no file is
   *        defined
   */
  void onDAOReadException (@Nonnull Throwable t, boolean bInit, @Nullable IReadableResource aRes);
}
