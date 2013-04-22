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
package com.phloc.web.smtp.settings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Interface representing the basic SMTP settings required to login to a user.
 * 
 * @author Philip Helger
 */
public interface ISMTPSettings
{
  /** Use SSL by default? no :) */
  boolean DEFAULT_SSL_ENABLED = false;

  /**
   * @return The SMTP server host name
   */
  @Nonnull
  String getHostName ();

  /**
   * @return The SMTP server port to use. May be -1 to use the default port.
   */
  int getPort ();

  /**
   * @return The server user name. May be <code>null</code>.
   */
  @Nullable
  String getUserName ();

  /**
   * @return The server user's password. May be <code>null</code>.
   */
  @Nullable
  String getPassword ();

  /**
   * @return The mail encoding to be used. May be <code>null</code>.
   */
  @Nonnull
  String getCharset ();

  /**
   * @return <code>true</code> if SSL is enabled, <code>false</code> if SSL is
   *         disabled
   */
  boolean isSSLEnabled ();

  /**
   * @return <code>true</code> if the minimum number of fields are defined, that
   *         are required for sending.
   */
  boolean areRequiredFieldsSet ();
}
