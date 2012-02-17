/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webbasics.servlet;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.IStatisticsHandlerTimer;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.timing.StopWatch;
import com.phloc.commons.xml.serialize.AbstractXMLWriterSettings;
import com.phloc.webbasics.app.scope.RequestScope;
import com.phloc.webbasics.app.scope.BasicScopeManager;

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
      s_aLogger.info ("Setting response charset to " + AbstractXMLWriterSettings.DEFAULT_XML_CHARSET);
      aHttpResponse.setCharacterEncoding (AbstractXMLWriterSettings.DEFAULT_XML_CHARSET);
    }
  }

  /**
   * Called before every request, independent of the method
   * 
   * @param aHttpRequest
   *        The HTTP servlet request
   * @param aHttpResponse
   *        The HTTP servlet response
   */
  private void _beforeRequest (@Nonnull final HttpServletRequest aHttpRequest,
                               @Nonnull final HttpServletResponse aHttpResponse)
  {
    BasicScopeManager.onRequestBegin (new RequestScope (aHttpRequest, aHttpResponse));
    _ensureResponseCharset (aHttpResponse);
    s_aCounterRequests.increment ();
  }

  @OverrideOnDemand
  protected void onDelete (@Nonnull final HttpServletRequest aHttpRequest,
                           @Nonnull final HttpServletResponse aHttpResponse) throws ServletException, IOException
  {
    super.doDelete (aHttpRequest, aHttpResponse);
  }

  @Override
  protected final void doDelete (@Nonnull final HttpServletRequest aHttpRequest,
                                 @Nonnull final HttpServletResponse aHttpResponse) throws ServletException, IOException
  {
    _beforeRequest (aHttpRequest, aHttpResponse);
    final StopWatch aSW = new StopWatch (true);
    try
    {
      onDelete (aHttpRequest, aHttpResponse);
    }
    finally
    {
      s_aTimerHdlDelete.addTime (aSW.stopAndGetMillis ());
      BasicScopeManager.onRequestEnd ();
    }
  }

  @OverrideOnDemand
  protected void onGet (@Nonnull final HttpServletRequest aHttpRequest, @Nonnull final HttpServletResponse aHttpResponse) throws ServletException,
                                                                                                                         IOException
  {
    super.doGet (aHttpRequest, aHttpResponse);
  }

  @Override
  protected final void doGet (@Nonnull final HttpServletRequest aHttpRequest,
                              @Nonnull final HttpServletResponse aHttpResponse) throws ServletException, IOException
  {
    _beforeRequest (aHttpRequest, aHttpResponse);
    final StopWatch aSW = new StopWatch (true);
    try
    {
      onGet (aHttpRequest, aHttpResponse);
    }
    finally
    {
      s_aTimerHdlGet.addTime (aSW.stopAndGetMillis ());
      BasicScopeManager.onRequestEnd ();
    }
  }

  @OverrideOnDemand
  protected void onHead (@Nonnull final HttpServletRequest aHttpRequest,
                         @Nonnull final HttpServletResponse aHttpResponse) throws ServletException, IOException
  {
    super.doHead (aHttpRequest, aHttpResponse);
  }

  @Override
  protected final void doHead (@Nonnull final HttpServletRequest aHttpRequest,
                               @Nonnull final HttpServletResponse aHttpResponse) throws ServletException, IOException
  {
    _beforeRequest (aHttpRequest, aHttpResponse);
    final StopWatch aSW = new StopWatch (true);
    try
    {
      onHead (aHttpRequest, aHttpResponse);
    }
    finally
    {
      s_aTimerHdlHead.addTime (aSW.stopAndGetMillis ());
      BasicScopeManager.onRequestEnd ();
    }
  }

  @OverrideOnDemand
  protected void onOptions (@Nonnull final HttpServletRequest aHttpRequest,
                            @Nonnull final HttpServletResponse aHttpResponse) throws ServletException, IOException
  {
    super.doOptions (aHttpRequest, aHttpResponse);
  }

  @Override
  protected final void doOptions (@Nonnull final HttpServletRequest aHttpRequest,
                                  @Nonnull final HttpServletResponse aHttpResponse) throws ServletException,
                                                                                   IOException
  {
    _beforeRequest (aHttpRequest, aHttpResponse);
    final StopWatch aSW = new StopWatch (true);
    try
    {
      onOptions (aHttpRequest, aHttpResponse);
    }
    finally
    {
      s_aTimerHdlOptions.addTime (aSW.stopAndGetMillis ());
      BasicScopeManager.onRequestEnd ();
    }
  }

  @OverrideOnDemand
  protected void onPost (final HttpServletRequest aHttpRequest, final HttpServletResponse aHttpResponse) throws ServletException,
                                                                                                        IOException
  {
    super.doPost (aHttpRequest, aHttpResponse);
  }

  @Override
  protected final void doPost (@Nonnull final HttpServletRequest aHttpRequest,
                               @Nonnull final HttpServletResponse aHttpResponse) throws ServletException, IOException
  {
    _beforeRequest (aHttpRequest, aHttpResponse);
    final StopWatch aSW = new StopWatch (true);
    try
    {
      onPost (aHttpRequest, aHttpResponse);
    }
    finally
    {
      s_aTimerHdlPost.addTime (aSW.stopAndGetMillis ());
      BasicScopeManager.onRequestEnd ();
    }
  }

  @OverrideOnDemand
  protected void onPut (@Nonnull final HttpServletRequest aHttpRequest, @Nonnull final HttpServletResponse aHttpResponse) throws ServletException,
                                                                                                                         IOException
  {
    super.doPut (aHttpRequest, aHttpResponse);
  }

  @Override
  protected final void doPut (@Nonnull final HttpServletRequest aHttpRequest,
                              @Nonnull final HttpServletResponse aHttpResponse) throws ServletException, IOException
  {
    _beforeRequest (aHttpRequest, aHttpResponse);
    final StopWatch aSW = new StopWatch (true);
    try
    {
      onPut (aHttpRequest, aHttpResponse);
    }
    finally
    {
      s_aTimerHdlPut.addTime (aSW.stopAndGetMillis ());
      BasicScopeManager.onRequestEnd ();
    }
  }

  @OverrideOnDemand
  protected void onTrace (@Nonnull final HttpServletRequest aHttpRequest,
                          @Nonnull final HttpServletResponse aHttpResponse) throws ServletException, IOException
  {
    super.doTrace (aHttpRequest, aHttpResponse);
  }

  @Override
  protected final void doTrace (@Nonnull final HttpServletRequest aHttpRequest,
                                @Nonnull final HttpServletResponse aHttpResponse) throws ServletException, IOException
  {
    _beforeRequest (aHttpRequest, aHttpResponse);
    final StopWatch aSW = new StopWatch (true);
    try
    {
      onTrace (aHttpRequest, aHttpResponse);
    }
    finally
    {
      s_aTimerHdlTrace.addTime (aSW.stopAndGetMillis ());
      BasicScopeManager.onRequestEnd ();
    }
  }
}
