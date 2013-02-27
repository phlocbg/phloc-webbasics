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
package com.phloc.webscopes.servlet;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.exceptions.InitializationException;
import com.phloc.commons.lang.CGStringHelper;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.IStatisticsHandlerTimer;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.timing.StopWatch;
import com.phloc.commons.xml.serialize.XMLWriterSettings;
import com.phloc.webscopes.domain.IRequestWebScope;

/**
 * A thin wrapper around an {@link HttpServlet} that encapsulates the correct
 * Scope handling before and after a request.<br>
 * It overrides all the protected "do*" methods from {@link HttpServlet} and
 * replaced them with protected "on*" methods that can be overridden. The "do*"
 * methods are final to avoid overriding the without the usage of scopes. The
 * default operations of the "on*" methods is to call the original "do*"
 * functionality from the parent class.
 * 
 * @author philip
 */
public abstract class AbstractScopeAwareHttpServlet extends HttpServlet
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractScopeAwareHttpServlet.class);
  private static final IStatisticsHandlerCounter s_aCounterRequests = StatisticsManager.getCounterHandler (AbstractScopeAwareHttpServlet.class.getName () +
                                                                                                           "$requests");
  private static final IStatisticsHandlerTimer s_aTimerHdlDelete = StatisticsManager.getTimerHandler (AbstractScopeAwareHttpServlet.class.getName () +
                                                                                                      "$DELETE");
  private static final IStatisticsHandlerTimer s_aTimerHdlGet = StatisticsManager.getTimerHandler (AbstractScopeAwareHttpServlet.class.getName () +
                                                                                                   "$GET");
  private static final IStatisticsHandlerTimer s_aTimerHdlHead = StatisticsManager.getTimerHandler (AbstractScopeAwareHttpServlet.class.getName () +
                                                                                                    "$HEAD");
  private static final IStatisticsHandlerTimer s_aTimerHdlOptions = StatisticsManager.getTimerHandler (AbstractScopeAwareHttpServlet.class.getName () +
                                                                                                       "$OPTIONS");
  private static final IStatisticsHandlerTimer s_aTimerHdlPost = StatisticsManager.getTimerHandler (AbstractScopeAwareHttpServlet.class.getName () +
                                                                                                    "$POST");
  private static final IStatisticsHandlerTimer s_aTimerHdlPut = StatisticsManager.getTimerHandler (AbstractScopeAwareHttpServlet.class.getName () +
                                                                                                   "$PUT");
  private static final IStatisticsHandlerTimer s_aTimerHdlTrace = StatisticsManager.getTimerHandler (AbstractScopeAwareHttpServlet.class.getName () +
                                                                                                     "$TRACE");

  private String m_sApplicationID;

  /**
   * @return The application ID for this servlet.
   */
  @OverrideOnDemand
  protected String getApplicationID ()
  {
    return CGStringHelper.getClassLocalName (getClass ());
  }

  /**
   * Add custom init code in overridden implementations of this method.
   * 
   * @throws ServletException
   *         to conform to the outer specifications
   */
  @OverrideOnDemand
  protected void onInit () throws ServletException
  {
    /* empty */
  }

  @Override
  public final void init () throws ServletException
  {
    super.init ();
    m_sApplicationID = getApplicationID ();
    if (StringHelper.hasNoText (m_sApplicationID))
      throw new InitializationException ("Failed retrieve a valid application ID!");
    onInit ();
  }

  /**
   * Add custom destruction code in overridden implementations of this method.
   */
  @OverrideOnDemand
  protected void onDestroy ()
  {
    /* empty */
  }

  @Override
  public final void destroy ()
  {
    onDestroy ();
    super.destroy ();
  }

  /*
   * This method is required to ensure that the HTTP response is correctly
   * encoded. Normally this is done via the charset filter, but if a
   * non-existing URL is accesses then the error redirect happens without the
   * charset filter ever called.
   */
  private static void _ensureResponseCharset (@Nonnull final HttpServletResponse aHttpResponse)
  {
    if (aHttpResponse.getCharacterEncoding () == null)
    {
      s_aLogger.info ("Setting response charset to " + XMLWriterSettings.DEFAULT_XML_CHARSET);
      aHttpResponse.setCharacterEncoding (XMLWriterSettings.DEFAULT_XML_CHARSET);
    }
  }

  /**
   * Called before every request, independent of the method
   * 
   * @param aHttpRequest
   *        The HTTP servlet request
   * @param aHttpResponse
   *        The HTTP servlet response
   * @return the created request scope
   */
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected RequestScopeInitializer beforeRequest (@Nonnull final HttpServletRequest aHttpRequest,
                                                   @Nonnull final HttpServletResponse aHttpResponse)
  {
    final RequestScopeInitializer aRequestScopeInitializer = RequestScopeInitializer.create (m_sApplicationID,
                                                                                             aHttpRequest,
                                                                                             aHttpResponse);
    _ensureResponseCharset (aHttpResponse);
    s_aCounterRequests.increment ();
    return aRequestScopeInitializer;
  }

  /**
   * Implement HTTP DELETE
   * 
   * @param aHttpRequest
   *        The original HTTP request. Never <code>null</code>.
   * @param aHttpResponse
   *        The original HTTP response. Never <code>null</code>.
   * @param aRequestScope
   *        The request scope to be used. Never <code>null</code>.
   * @throws ServletException
   *         In case of an error.
   * @throws IOException
   *         In case of an error.
   */
  @OverrideOnDemand
  protected void onDelete (@Nonnull final HttpServletRequest aHttpRequest,
                           @Nonnull final HttpServletResponse aHttpResponse,
                           @Nonnull final IRequestWebScope aRequestScope) throws ServletException, IOException
  {
    super.doDelete (aHttpRequest, aHttpResponse);
  }

  @Override
  protected final void doDelete (@Nonnull final HttpServletRequest aHttpRequest,
                                 @Nonnull final HttpServletResponse aHttpResponse) throws ServletException, IOException
  {
    final RequestScopeInitializer aRequestScopeInitializer = beforeRequest (aHttpRequest, aHttpResponse);
    final StopWatch aSW = new StopWatch (true);
    try
    {
      onDelete (aHttpRequest, aHttpResponse, aRequestScopeInitializer.getRequestScope ());
    }
    finally
    {
      s_aTimerHdlDelete.addTime (aSW.stopAndGetMillis ());
      aRequestScopeInitializer.destroyScope ();
    }
  }

  /**
   * Implement HTTP GET
   * 
   * @param aHttpRequest
   *        The original HTTP request. Never <code>null</code>.
   * @param aHttpResponse
   *        The original HTTP response. Never <code>null</code>.
   * @param aRequestScope
   *        The request scope to be used. Never <code>null</code>.
   * @throws ServletException
   *         In case of an error.
   * @throws IOException
   *         In case of an error.
   */
  @OverrideOnDemand
  protected void onGet (@Nonnull final HttpServletRequest aHttpRequest,
                        @Nonnull final HttpServletResponse aHttpResponse,
                        @Nonnull final IRequestWebScope aRequestScope) throws ServletException, IOException
  {
    super.doGet (aHttpRequest, aHttpResponse);
  }

  @Override
  protected final void doGet (@Nonnull final HttpServletRequest aHttpRequest,
                              @Nonnull final HttpServletResponse aHttpResponse) throws ServletException, IOException
  {
    final RequestScopeInitializer aRequestScopeInitializer = beforeRequest (aHttpRequest, aHttpResponse);
    final StopWatch aSW = new StopWatch (true);
    try
    {
      onGet (aHttpRequest, aHttpResponse, aRequestScopeInitializer.getRequestScope ());
    }
    finally
    {
      s_aTimerHdlGet.addTime (aSW.stopAndGetMillis ());
      aRequestScopeInitializer.destroyScope ();
    }
  }

  /**
   * Implement HTTP HEAD
   * 
   * @param aHttpRequest
   *        The original HTTP request. Never <code>null</code>.
   * @param aHttpResponse
   *        The original HTTP response. Never <code>null</code>.
   * @param aRequestScope
   *        The request scope to be used. Never <code>null</code>.
   * @throws ServletException
   *         In case of an error.
   * @throws IOException
   *         In case of an error.
   */
  @OverrideOnDemand
  protected void onHead (@Nonnull final HttpServletRequest aHttpRequest,
                         @Nonnull final HttpServletResponse aHttpResponse,
                         @Nonnull final IRequestWebScope aRequestScope) throws ServletException, IOException
  {
    super.doHead (aHttpRequest, aHttpResponse);
  }

  @Override
  protected final void doHead (@Nonnull final HttpServletRequest aHttpRequest,
                               @Nonnull final HttpServletResponse aHttpResponse) throws ServletException, IOException
  {
    final RequestScopeInitializer aRequestScopeInitializer = beforeRequest (aHttpRequest, aHttpResponse);
    final StopWatch aSW = new StopWatch (true);
    try
    {
      onHead (aHttpRequest, aHttpResponse, aRequestScopeInitializer.getRequestScope ());
    }
    finally
    {
      s_aTimerHdlHead.addTime (aSW.stopAndGetMillis ());
      aRequestScopeInitializer.destroyScope ();
    }
  }

  /**
   * Implement HTTP OPTIONS
   * 
   * @param aHttpRequest
   *        The original HTTP request. Never <code>null</code>.
   * @param aHttpResponse
   *        The original HTTP response. Never <code>null</code>.
   * @param aRequestScope
   *        The request scope to be used. Never <code>null</code>.
   * @throws ServletException
   *         In case of an error.
   * @throws IOException
   *         In case of an error.
   */
  @OverrideOnDemand
  protected void onOptions (@Nonnull final HttpServletRequest aHttpRequest,
                            @Nonnull final HttpServletResponse aHttpResponse,
                            @Nonnull final IRequestWebScope aRequestScope) throws ServletException, IOException
  {
    super.doOptions (aHttpRequest, aHttpResponse);
  }

  @Override
  protected final void doOptions (@Nonnull final HttpServletRequest aHttpRequest,
                                  @Nonnull final HttpServletResponse aHttpResponse) throws ServletException,
                                                                                   IOException
  {
    final RequestScopeInitializer aRequestScopeInitializer = beforeRequest (aHttpRequest, aHttpResponse);
    final StopWatch aSW = new StopWatch (true);
    try
    {
      onOptions (aHttpRequest, aHttpResponse, aRequestScopeInitializer.getRequestScope ());
    }
    finally
    {
      s_aTimerHdlOptions.addTime (aSW.stopAndGetMillis ());
      aRequestScopeInitializer.destroyScope ();
    }
  }

  /**
   * Implement HTTP POST
   * 
   * @param aHttpRequest
   *        The original HTTP request. Never <code>null</code>.
   * @param aHttpResponse
   *        The original HTTP response. Never <code>null</code>.
   * @param aRequestScope
   *        The request scope to be used. Never <code>null</code>.
   * @throws ServletException
   *         In case of an error.
   * @throws IOException
   *         In case of an error.
   */
  @OverrideOnDemand
  protected void onPost (@Nonnull final HttpServletRequest aHttpRequest,
                         @Nonnull final HttpServletResponse aHttpResponse,
                         @Nonnull final IRequestWebScope aRequestScope) throws ServletException, IOException
  {
    super.doPost (aHttpRequest, aHttpResponse);
  }

  @Override
  protected final void doPost (@Nonnull final HttpServletRequest aHttpRequest,
                               @Nonnull final HttpServletResponse aHttpResponse) throws ServletException, IOException
  {
    final RequestScopeInitializer aRequestScopeInitializer = beforeRequest (aHttpRequest, aHttpResponse);
    final StopWatch aSW = new StopWatch (true);
    try
    {
      onPost (aHttpRequest, aHttpResponse, aRequestScopeInitializer.getRequestScope ());
    }
    finally
    {
      s_aTimerHdlPost.addTime (aSW.stopAndGetMillis ());
      aRequestScopeInitializer.destroyScope ();
    }
  }

  /**
   * Implement HTTP PUT
   * 
   * @param aHttpRequest
   *        The original HTTP request. Never <code>null</code>.
   * @param aHttpResponse
   *        The original HTTP response. Never <code>null</code>.
   * @param aRequestScope
   *        The request scope to be used. Never <code>null</code>.
   * @throws ServletException
   *         In case of an error.
   * @throws IOException
   *         In case of an error.
   */
  @OverrideOnDemand
  protected void onPut (@Nonnull final HttpServletRequest aHttpRequest,
                        @Nonnull final HttpServletResponse aHttpResponse,
                        @Nonnull final IRequestWebScope aRequestScope) throws ServletException, IOException
  {
    super.doPut (aHttpRequest, aHttpResponse);
  }

  @Override
  protected final void doPut (@Nonnull final HttpServletRequest aHttpRequest,
                              @Nonnull final HttpServletResponse aHttpResponse) throws ServletException, IOException
  {
    final RequestScopeInitializer aRequestScopeInitializer = beforeRequest (aHttpRequest, aHttpResponse);
    final StopWatch aSW = new StopWatch (true);
    try
    {
      onPut (aHttpRequest, aHttpResponse, aRequestScopeInitializer.getRequestScope ());
    }
    finally
    {
      s_aTimerHdlPut.addTime (aSW.stopAndGetMillis ());
      aRequestScopeInitializer.destroyScope ();
    }
  }

  /**
   * Implement HTTP TRACE
   * 
   * @param aHttpRequest
   *        The original HTTP request. Never <code>null</code>.
   * @param aHttpResponse
   *        The original HTTP response. Never <code>null</code>.
   * @param aRequestScope
   *        The request scope to be used. Never <code>null</code>.
   * @throws ServletException
   *         In case of an error.
   * @throws IOException
   *         In case of an error.
   */
  @OverrideOnDemand
  protected void onTrace (@Nonnull final HttpServletRequest aHttpRequest,
                          @Nonnull final HttpServletResponse aHttpResponse,
                          @Nonnull final IRequestWebScope aRequestScope) throws ServletException, IOException
  {
    super.doTrace (aHttpRequest, aHttpResponse);
  }

  @Override
  protected final void doTrace (@Nonnull final HttpServletRequest aHttpRequest,
                                @Nonnull final HttpServletResponse aHttpResponse) throws ServletException, IOException
  {
    final RequestScopeInitializer aRequestScopeInitializer = beforeRequest (aHttpRequest, aHttpResponse);
    final StopWatch aSW = new StopWatch (true);
    try
    {
      onTrace (aHttpRequest, aHttpResponse, aRequestScopeInitializer.getRequestScope ());
    }
    finally
    {
      s_aTimerHdlTrace.addTime (aSW.stopAndGetMillis ());
      aRequestScopeInitializer.destroyScope ();
    }
  }
}
