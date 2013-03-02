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
package com.phloc.web.mock;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.string.StringHelper;

/**
 * A pool for registered servlets inside a {@link MockServletContext}.
 * 
 * @author philip
 */
@NotThreadSafe
public final class MockServletPool
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (MockServletPool.class);

  private final MockServletContext m_aSC;
  // Map from path to servlet
  private final Map <String, Servlet> m_aServlets = new LinkedHashMap <String, Servlet> ();
  // All servlet names
  private final Set <String> m_aServletNames = new HashSet <String> ();
  private boolean m_bInvalidated = false;

  public MockServletPool (@Nonnull final MockServletContext aSC)
  {
    if (aSC == null)
      throw new NullPointerException ("servletContext");
    m_aSC = aSC;
  }

  /**
   * Register a new servlet without servlet init parameters
   * 
   * @param aServletClass
   *        The class of the servlet to be registered. May not be
   *        <code>null</code>.
   * @param sServletPath
   *        The path where the servlet should listen to requests. May neither be
   *        <code>null</code> nor empty.
   * @param sServletName
   *        The name of the servlet. May neither be <code>null</code> nor empty.
   */
  public void registerServlet (@Nonnull final Class <? extends Servlet> aServletClass,
                               @Nonnull @Nonempty final String sServletPath,
                               @Nonnull @Nonempty final String sServletName)
  {
    registerServlet (aServletClass, sServletPath, sServletName, (Map <String, String>) null);
  }

  /**
   * Register a new servlet
   * 
   * @param aServletClass
   *        The class of the servlet to be registered. May not be
   *        <code>null</code>.
   * @param sServletPath
   *        The path where the servlet should listen to requests. May neither be
   *        <code>null</code> nor empty.
   * @param sServletName
   *        The name of the servlet. May neither be <code>null</code> nor empty.
   * @param aServletInitParams
   *        An optional map of servlet init parameters. May be <code>null</code>
   *        or empty.
   */
  public void registerServlet (@Nonnull final Class <? extends Servlet> aServletClass,
                               @Nonnull @Nonempty final String sServletPath,
                               @Nonnull @Nonempty final String sServletName,
                               @Nullable final Map <String, String> aServletInitParams)
  {
    if (aServletClass == null)
      throw new NullPointerException ("servletClass");
    if (StringHelper.hasNoText (sServletPath))
      throw new IllegalArgumentException ("servletPath");

    // Check path uniqueness
    final Servlet aServletOfSamePath = m_aServlets.get (sServletPath);
    if (aServletOfSamePath == null)
      throw new IllegalArgumentException ("Another servlet with the path '" +
                                          sServletPath +
                                          "' is already registered: " +
                                          aServletOfSamePath);

    // Check name uniqueness
    if (m_aServletNames.contains (sServletName))
      throw new IllegalArgumentException ("Another servlet with the name '" + sServletName + "' is already registered");

    // Instantiate servlet
    final Servlet aServlet = GenericReflection.newInstance (aServletClass);
    if (aServlet == null)
      throw new IllegalArgumentException ("Failed to instantiate servlet class " + aServletClass);

    final ServletConfig aServletConfig = m_aSC.createServletConfig (sServletName, aServletInitParams);
    try
    {
      aServlet.init (aServletConfig);
    }
    catch (final ServletException ex)
    {
      throw new IllegalStateException ("Failed to init servlet " +
                                       aServlet +
                                       " with configuration  " +
                                       aServletConfig +
                                       " for path '" +
                                       sServletPath +
                                       "'");
    }
    m_aServlets.put (sServletPath, aServlet);
    m_aServletNames.add (sServletName);
  }

  public void invalidate ()
  {
    if (m_bInvalidated)
      throw new IllegalArgumentException ("Servlet pool already invalidated!");
    m_bInvalidated = true;

    // Destroy all servlets
    for (final Servlet aServlet : m_aServlets.values ())
      try
      {
        aServlet.destroy ();
      }
      catch (final Throwable t)
      {
        s_aLogger.error ("Failed to destroy servlet " + aServlet, t);
      }

    m_aServlets.clear ();
  }
}
