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

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet context listener
 * 
 * @author Philip Helger
 */
public final class RedirectListener implements ServletContextListener
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (RedirectListener.class);
  private static URL s_aURL = null;

  /**
   * @return The target URL as specified in the "target" init-parameter. Never
   *         <code>null</code>!
   */
  public static URL getTargetURL ()
  {
    if (s_aURL == null)
      throw new IllegalStateException ("This ServletContextListener was never initialized. Please check your web.xml file!");
    return s_aURL;
  }

  public static void initializeTargetURL (final String sTarget)
  {
    if (sTarget == null)
      throw new IllegalStateException ("ServletContext init-parameter 'target' is missing or empty!");

    String sRealTarget;
    if (sTarget.endsWith ("/"))
    {
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Cutting trailing slash from target URL '" + sTarget + "'");
      sRealTarget = sTarget.substring (0, sTarget.length () - 1);
    }
    else
      sRealTarget = sTarget;
    if (sRealTarget.length () == 0)
      throw new IllegalStateException ("ServletContext init-parameter 'target' is empty!");
    try
    {
      final URL aURL = new URL (sRealTarget);

      // Save in static field :)
      s_aURL = aURL;
    }
    catch (final MalformedURLException ex)
    {
      throw new IllegalStateException ("Failed to convert ServletContext init-parameter 'target' to a URL: '" +
                                       sRealTarget +
                                       "'");
    }
  }

  @Override
  public void contextInitialized (final ServletContextEvent aSCE)
  {
    final ServletContext aSC = aSCE.getServletContext ();
    final String sTarget = aSC.getInitParameter ("target");
    initializeTargetURL (sTarget);
    s_aLogger.info ("Redirect servlet listener initialized: " + s_aURL.toExternalForm ());
  }

  @Override
  public void contextDestroyed (final ServletContextEvent aSCE)
  {
    s_aLogger.info ("Redirect servlet listener destroyed: " + s_aURL.toExternalForm ());
  }
}
