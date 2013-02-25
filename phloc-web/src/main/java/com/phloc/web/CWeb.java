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
package com.phloc.web;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.web.port.DefaultNetworkPorts;

/**
 * Contains some global web constants
 * 
 * @author philip
 */
@Immutable
public final class CWeb
{
  /** Default FTP port */
  public static final int DEFAULT_PORT_FTP = DefaultNetworkPorts.TCP_21_ftp.getPort ();
  /** Default HTTP port */
  public static final int DEFAULT_PORT_HTTP = DefaultNetworkPorts.TCP_80_http.getPort ();
  /** Default HTTPS port */
  public static final int DEFAULT_PORT_HTTPS = DefaultNetworkPorts.TCP_443_https.getPort ();
  /** Default SMTP port */
  public static final int DEFAULT_PORT_SMTP = DefaultNetworkPorts.TCP_25_smtp.getPort ();

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final CWeb s_aInstance = new CWeb ();

  private CWeb ()
  {}
}
