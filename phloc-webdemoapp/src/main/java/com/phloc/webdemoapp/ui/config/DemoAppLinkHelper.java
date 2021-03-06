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
package com.phloc.webdemoapp.ui.config;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webdemoapp.servlet.DemoAppConfigActionServlet;

/**
 * Several helper methods for consistently creating links.
 * 
 * @author Philip Helger
 */
@Immutable
public final class DemoAppLinkHelper
{
  private DemoAppLinkHelper ()
  {}

  @Nonnull
  public static ISimpleURL getActionURL (final String sAction)
  {
    return getActionURL (sAction, null);
  }

  @Nonnull
  public static ISimpleURL getActionURL (final String sAction, final Map <String, String> aParams)
  {
    return LinkUtils.getURLWithContext (DemoAppConfigActionServlet.SERVLET_DEFAULT_PATH + '/' + sAction, aParams);
  }

  @Nonnull
  public static ISimpleURL getStreamURL (@Nonnull final String sURL)
  {
    // We're using the default path!
    return LinkUtils.getStreamURL (sURL);
  }
}
