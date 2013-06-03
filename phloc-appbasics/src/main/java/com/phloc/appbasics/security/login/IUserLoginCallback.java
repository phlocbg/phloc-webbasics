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
package com.phloc.appbasics.security.login;

import javax.annotation.Nonnull;

/**
 * Callback interface when a user logs in.
 * 
 * @author Philip Helger
 */
public interface IUserLoginCallback
{
  /**
   * Called when a user is logged in.
   * 
   * @param aInfo
   *        The login info of the user that just logged in. Never
   *        <code>null</code>.
   */
  void onUserLogin (@Nonnull LoginInfo aInfo);
}