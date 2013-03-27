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
package com.phloc.webdemoapp.app;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.version.Version;
import com.phloc.webctrls.bootstrap.CBootstrap;

@Immutable
public final class CDemoAppUI
{
  public static final String APP_CONFIG_ID = "config";
  public static final String APP_VIEW_ID = "view";
  public static final Version BOOTSTRAP_VERSION = CBootstrap.BOOTSTRAP_VERSION_231;

  private CDemoAppUI ()
  {}
}
