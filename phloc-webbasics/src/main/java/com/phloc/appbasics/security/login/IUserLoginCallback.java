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
package com.phloc.appbasics.security.login;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;

/**
 * Callback interface when a user logs in.
 * 
 * @author Philip Helger
 */
public interface IUserLoginCallback
{
  /**
   * Called when a user is successfully logged in.
   * 
   * @param aInfo
   *        The login info of the user that just logged in. Never
   *        <code>null</code>.
   */
  void onUserLogin (@Nonnull LoginInfo aInfo);

  /**
   * Called when a user failed to logged in.
   * 
   * @param sUserID
   *        The ID of the user who tried to login. Never <code>null</code>.
   * @param eLoginResult
   *        The login result indicating the login error.
   */
  void onUserLoginError (@Nonnull @Nonempty String sUserID, @Nonnull ELoginResult eLoginResult);
}
