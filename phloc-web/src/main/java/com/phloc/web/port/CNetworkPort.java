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
package com.phloc.web.port;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * Constants for network port handling.
 * 
 * @author Philip Helger
 */
@Immutable
public final class CNetworkPort
{
  /**
   * Invalid valid port number - should be the same as the default port number
   * in the URL class
   */
  public static final int INVALID_PORT_NUMBER = -1;
  /** Smallest valid port number */
  public static final int MINIMUM_PORT_NUMBER = 0;
  /** Largest valid port number */
  public static final int MAXIMUM_PORT_NUMBER = 65535;

  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final CNetworkPort s_aInstance = new CNetworkPort ();

  private CNetworkPort ()
  {}
}
