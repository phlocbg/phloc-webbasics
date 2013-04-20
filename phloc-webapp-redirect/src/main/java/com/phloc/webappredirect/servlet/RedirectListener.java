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
package com.phloc.webappredirect.servlet;

import java.net.URL;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.URLUtils;

/**
 * Servlet context listener
 * 
 * @author philip
 */
public final class RedirectListener implements ServletContextListener
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (RedirectListener.class);
  private static URL s_aURL;

  @Nonnull
  public static URL getTargetURL ()
  {
    return s_aURL;
  }

  @Override
  public void contextInitialized (final ServletContextEvent aSCE)
  {
    final ServletContext aSC = aSCE.getServletContext ();
    final String sTarget = StringHelper.trimEnd (aSC.getInitParameter ("target"), "/");
    if (StringHelper.hasNoText (sTarget))
      throw new IllegalStateException ("ServletContext init-parameter 'target' is missing or empty!");
    final URL aURL = URLUtils.getAsURL (sTarget);
    if (aURL == null)
      throw new IllegalStateException ("Failed to convert ServletContext init-parameter 'target' to a URL: '" +
                                       sTarget +
                                       "'");
    s_aURL = aURL;
    s_aLogger.info ("Redirect servlet listener initialized: " + s_aURL.toExternalForm ());
  }

  @Override
  public void contextDestroyed (final ServletContextEvent aSCE)
  {
    s_aLogger.info ("Redirect servlet listener destroyed: " + s_aURL.toExternalForm ());
  }
}
