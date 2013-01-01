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
package com.phloc.webbasics;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * Contains some global web constants
 * 
 * @author philip
 */
@Immutable
public final class CWeb
{
  public static final int DEFAULT_PORT_HTTP = 80;
  public static final int DEFAULT_PORT_HTTPS = 443;

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final CWeb s_aInstance = new CWeb ();

  private CWeb ()
  {}
}
