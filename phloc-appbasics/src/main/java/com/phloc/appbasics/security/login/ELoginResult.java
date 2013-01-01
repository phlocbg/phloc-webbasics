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

import com.phloc.commons.state.ISuccessIndicator;

/**
 * Represents the different login results.
 * 
 * @author philip
 */
public enum ELoginResult implements ISuccessIndicator
{
  /** Login was successfully */
  SUCCESS,
  /** No such user */
  USER_NOT_EXISTING,
  /** The provided password is invalid */
  INVALID_PASSWORD,
  /** The user was already logged in */
  USER_ALREADY_LOGGED_IN,
  /** Another user is already logged in this session */
  SESSION_ALREADY_HAS_USER;

  public boolean isSuccess ()
  {
    return this == SUCCESS;
  }

  public boolean isFailure ()
  {
    return this != SUCCESS;
  }
}
