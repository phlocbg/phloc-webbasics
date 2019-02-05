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
package com.phloc.web.mock;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.SystemProperties;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.IReadableResourceProvider;
import com.phloc.commons.io.resourceprovider.DefaultResourceProvider;
import com.phloc.commons.mime.MimeTypeDeterminator;
import com.phloc.commons.string.StringHelper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

//ESCA-JAVA0116:
/**
 * Mock implementation of the {@link ServletContext} interface.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class MockServletContext implements ServletContext
{
  public static final int SERVLET_SPEC_MAJOR_VERSION = 2;
  public static final int SERVLET_SPEC_MINOR_VERSION = 5;
  public static final String DEFAULT_SERVLET_CONTEXT_NAME = "MockServletContext";
  public static final String DEFAULT_SERVLET_CONTEXT_PATH = "";
  private static final Logger s_aLogger = LoggerFactory.getLogger (MockServletContext.class);

  private final IReadableResourceProvider m_aResourceProvider;
  private final String m_sResourceBasePath;
  private String m_sContextPath = DEFAULT_SERVLET_CONTEXT_PATH;
  private final Map <String, ServletContext> m_aContexts = new HashMap <String, ServletContext> ();
  private final Properties m_aInitParameters = new Properties ();
  private final Map <String, Object> m_aAttributes = new HashMap <String, Object> ();
  private String m_sServletContextName = DEFAULT_SERVLET_CONTEXT_NAME;
  private final MockServletPool m_aServletPool;
  private boolean m_bInvalidated = false;

  /**
   * Create a new MockServletContext, using no base path and a
   * DefaultIResourceProvider (i.e. the classpath root as WAR root).
   */
  public MockServletContext ()
  {
    this (null, "", null, null);
  }

  /**
   * Create a new MockServletContext, using no base path and a
   * DefaultIResourceProvider (i.e. the classpath root as WAR root).
   *
   * @param aInitParams
   *        The init parameter
   */
  public MockServletContext (@Nullable final Map <String, String> aInitParams)
  {
    this (null, "", null, aInitParams);
  }

  /**
   * Create a new MockServletContext.
   *
   * @param sContextPath
   *        The context path to use. May be <code>null</code>.
   */
  public MockServletContext (@Nullable final String sContextPath)
  {
    this (sContextPath, "", null, null);
  }

  /**
   * Create a new MockServletContext.
   *
   * @param sContextPath
   *        Context path to use. May be <code>null</code>.
   * @param aInitParams
   *        The init parameter The context path to use
   */
  public MockServletContext (@Nullable final String sContextPath, @Nullable final Map <String, String> aInitParams)
  {
    this (sContextPath, "", null, aInitParams);
  }

  /**
   * Create a new MockServletContext.
   *
   * @param sContextPath
   *        The context path to use
   * @param sResourceBasePath
   *        the WAR root directory (should not end with a slash)
   */
  public MockServletContext (@Nullable final String sContextPath, @Nullable final String sResourceBasePath)
  {
    this (sContextPath, sResourceBasePath, null, null);
  }

  /**
   * Create a new MockServletContext.
   *
   * @param sContextPath
   *        The context path to use
   * @param sResourceBasePath
   *        the WAR root directory (should not end with a slash)
   * @param aResourceLoader
   *        the IReadableResourceProvider to use (or null for the default)
   * @param aInitParams
   *        A map containing initialization parameters
   */
  public MockServletContext (@Nullable final String sContextPath,
                             @Nullable final String sResourceBasePath,
                             @Nullable final IReadableResourceProvider aResourceLoader,
                             @Nullable final Map <String, String> aInitParams)
  {
    setContextPath (sContextPath);
    this.m_aResourceProvider = aResourceLoader != null ? aResourceLoader : new DefaultResourceProvider ();
    this.m_sResourceBasePath = sResourceBasePath != null ? sResourceBasePath : "";

    // Use JVM temp dir as ServletContext temp dir.
    final String sTempDir = SystemProperties.getTmpDir ();
    if (sTempDir != null)
      setAttribute ("tempdir", new File (sTempDir));

    if (aInitParams != null)
      for (final Map.Entry <String, String> aEntry : aInitParams.entrySet ())
        addInitParameter (aEntry.getKey (), aEntry.getValue ());

    // Invoke all event listeners
    final ServletContextEvent aSCE = new ServletContextEvent (this);
    for (final ServletContextListener aListener : MockHttpListener.getAllServletContextListeners ())
      aListener.contextInitialized (aSCE);

    this.m_aServletPool = new MockServletPool (this);
  }

  /**
   * Build a full resource location for the given path, prepending the resource
   * base path of this MockServletContext.
   *
   * @param sPath
   *        the path as specified
   * @return the full resource path
   */
  @Nonnull
  protected String getResourceLocation (@Nonnull final String sPath)
  {
    return StringHelper.startsWith (sPath, '/') ? this.m_sResourceBasePath + sPath
                                                : this.m_sResourceBasePath + "/" + sPath;
  }

  public final void setContextPath (@Nullable final String sContextPath)
  {
    if (sContextPath == null)
      this.m_sContextPath = "";
    else
      if (StringHelper.startsWith (sContextPath, '/'))
        this.m_sContextPath = sContextPath;
      else
        this.m_sContextPath = "/" + sContextPath;
  }

  /* This is a Servlet API 2.5 method. */
  @Override
  @Nonnull
  public String getContextPath ()
  {
    return this.m_sContextPath;
  }

  public void registerContext (@Nonnull final String sContextPath, @Nonnull final ServletContext aContext)
  {
    ValueEnforcer.notNull (sContextPath, "ContextPath");
    ValueEnforcer.notNull (aContext, "Context");
    this.m_aContexts.put (sContextPath, aContext);
  }

  @Override
  @Nullable
  public ServletContext getContext (@Nullable final String sContextPath)
  {
    if (this.m_sContextPath.equals (sContextPath))
      return this;
    return this.m_aContexts.get (sContextPath);
  }

  @Override
  @Nonnegative
  public int getMajorVersion ()
  {
    return SERVLET_SPEC_MAJOR_VERSION;
  }

  @Override
  @Nonnegative
  public int getMinorVersion ()
  {
    return SERVLET_SPEC_MINOR_VERSION;
  }

  @Override
  @Nullable
  public String getMimeType (@Nonnull final String sFilename)
  {
    return MimeTypeDeterminator.getMimeTypeFromFilename (sFilename);
  }

  @Override
  @UnsupportedOperation
  @Deprecated
  public Set <String> getResourcePaths (final String sPath)
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  @Nullable
  public URL getResource (@Nonnull final String sPath) throws MalformedURLException
  {
    final IReadableResource aResource = this.m_aResourceProvider.getReadableResource (getResourceLocation (sPath));
    if (!aResource.exists ())
      return null;
    return aResource.getAsURL ();
  }

  @Override
  @Nullable
  public InputStream getResourceAsStream (@Nonnull final String sPath)
  {
    final IReadableResource aResource = this.m_aResourceProvider.getReadableResource (getResourceLocation (sPath));
    if (!aResource.exists ())
      return null;
    return aResource.getInputStream ();
  }

  @Override
  @Nonnull
  public RequestDispatcher getRequestDispatcher (@Nonnull final String sPath)
  {
    if (!StringHelper.startsWith (sPath, '/'))
      throw new IllegalArgumentException ("RequestDispatcher path at ServletContext level must start with '/'");
    return new MockRequestDispatcher (sPath);
  }

  @Override
  @Nullable
  @Deprecated
  public RequestDispatcher getNamedDispatcher (@Nullable final String sPath)
  {
    return null;
  }

  @Override
  @Deprecated
  public Servlet getServlet (@Nullable final String sName)
  {
    return null;
  }

  @Override
  @Deprecated
  @Nonnull
  public Enumeration <Object> getServlets ()
  {
    return ContainerHelper.<Object> getEmptyEnumeration ();
  }

  @Override
  @Deprecated
  @Nonnull
  public Enumeration <Object> getServletNames ()
  {
    return ContainerHelper.<Object> getEmptyEnumeration ();
  }

  @Override
  public void log (@Nullable final String message)
  {
    s_aLogger.info (message);
  }

  @Override
  @Deprecated
  public void log (@Nullable final Exception ex, @Nullable final String sMessage)
  {
    s_aLogger.info (sMessage, ex);
  }

  @Override
  public void log (@Nullable final String sMessage, @Nullable final Throwable ex)
  {
    s_aLogger.info (sMessage, ex);
  }

  @Override
  @Nonnull
  @SuppressFBWarnings ("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
  public String getRealPath (@Nonnull final String sPath)
  {
    final IReadableResource aResource = this.m_aResourceProvider.getReadableResource (getResourceLocation (sPath));
    if (aResource == null)
      throw new IllegalStateException ("Failed to get real path of '" + sPath + "'");
    final File aFile = aResource.getAsFile ();
    if (aFile == null)
      throw new IllegalStateException ("Failed to convert resource " + aResource + " to a file");
    return aFile.getAbsolutePath ();
  }

  @Override
  @Nonnull
  @Nonempty
  public String getServerInfo ()
  {
    return "MockServletContext";
  }

  @Override
  @Nullable
  public String getInitParameter (@Nonnull final String sName)
  {
    ValueEnforcer.notNull (sName, "Name");
    return this.m_aInitParameters.getProperty (sName);
  }

  public final void addInitParameter (@Nonnull final String sName, @Nonnull final String sValue)
  {
    ValueEnforcer.notNull (sName, "Name");
    ValueEnforcer.notNull (sValue, "Value");
    this.m_aInitParameters.setProperty (sName, sValue);
  }

  @Override
  @Nonnull
  public Enumeration <Object> getInitParameterNames ()
  {
    return this.m_aInitParameters.keys ();
  }

  @Override
  @Nullable
  public Object getAttribute (@Nonnull final String sName)
  {
    ValueEnforcer.notNull (sName, "Name");
    return this.m_aAttributes.get (sName);
  }

  @Override
  @Nonnull
  public Enumeration <String> getAttributeNames ()
  {
    return ContainerHelper.getEnumeration (this.m_aAttributes.keySet ());
  }

  @Override
  public final void setAttribute (@Nonnull final String sName, @Nullable final Object aValue)
  {
    ValueEnforcer.notNull (sName, "Name");
    if (aValue != null)
      this.m_aAttributes.put (sName, aValue);
    else
      this.m_aAttributes.remove (sName);
  }

  @Override
  public void removeAttribute (@Nonnull final String sName)
  {
    ValueEnforcer.notNull (sName, "Name");
    this.m_aAttributes.remove (sName);
  }

  public void setServletContextName (@Nullable final String sServletContextName)
  {
    this.m_sServletContextName = sServletContextName;
  }

  @Override
  @Nullable
  public String getServletContextName ()
  {
    return this.m_sServletContextName;
  }

  /**
   * Create a new {@link MockServletConfig} object without servlet init
   * parameters.
   *
   * @param sServletName
   *        Name of the servlet. May neither be <code>null</code> nor empty.
   * @return A new {@link MockServletConfig} object for this servlet context.
   */
  @Nonnull
  public MockServletConfig createServletConfig (@Nonnull @Nonempty final String sServletName)
  {
    return createServletConfig (sServletName, null);
  }

  /**
   * Create a new {@link MockServletConfig} object.
   *
   * @param sServletName
   *        Name of the servlet. May neither be <code>null</code> nor empty.
   * @param aServletInitParams
   *        The map with all servlet init parameters. May be <code>null</code>
   *        or empty.
   * @return A new {@link MockServletConfig} object for this servlet context.
   */
  @Nonnull
  public MockServletConfig createServletConfig (@Nonnull @Nonempty final String sServletName,
                                                @Nullable final Map <String, String> aServletInitParams)
  {
    return new MockServletConfig (this, sServletName, aServletInitParams);
  }

  /**
   * @return The servlet pool for registering mock servlets.
   */
  @Nonnull
  public MockServletPool getServletPool ()
  {
    return this.m_aServletPool;
  }

  @Nullable
  public MockHttpServletResponse invoke (@Nonnull final HttpServletRequest aHttpRequest)
  {
    ValueEnforcer.notNull (aHttpRequest, "HttpRequest");

    // Find matching servlet
    final String sServletPath = aHttpRequest.getServletPath ();
    final Servlet aServlet = this.m_aServletPool.getServletOfPath (sServletPath);
    if (aServlet == null)
    {
      s_aLogger.error ("Found no servlet matching '" + sServletPath + "'");
      return null;
    }

    // Main invocation
    final MockHttpServletResponse ret = new MockHttpServletResponse ();
    try
    {
      aServlet.service (aHttpRequest, ret);
    }
    catch (final Throwable t)
    {
      throw new IllegalStateException ("Failed to invoke servlet " + aServlet + " for request " + aHttpRequest, t);
    }
    return ret;
  }

  public void invalidate ()
  {
    if (this.m_bInvalidated)
      throw new IllegalStateException ("Servlet context already invalidated!");
    this.m_bInvalidated = true;

    // Destroy all servlets
    this.m_aServletPool.invalidate ();

    // Call all HTTP listener
    final ServletContextEvent aSCE = new ServletContextEvent (this);
    for (final ServletContextListener aListener : MockHttpListener.getAllServletContextListeners ())
      aListener.contextDestroyed (aSCE);

    this.m_aAttributes.clear ();
  }
}
