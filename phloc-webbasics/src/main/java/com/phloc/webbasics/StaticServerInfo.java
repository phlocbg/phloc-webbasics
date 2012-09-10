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
package com.phloc.webbasics;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.string.ToStringGenerator;
import com.phloc.scopes.web.domain.IRequestWebScope;
import com.phloc.scopes.web.mgr.WebScopeManager;

/**
 * This singleton instance represents default server information for locations
 * where no request context is available (e.g. in scheduled tasks)
 * 
 * @author philip
 */
@Immutable
public final class StaticServerInfo
{
  private static volatile StaticServerInfo s_aDefault;

  private final String m_sScheme;
  private final String m_sServerName;
  private final int m_nServerPort;
  private final String m_sContextPath;
  private final String m_sFullServerPath;
  private final String m_sFullServerAndContextPath;

  private StaticServerInfo (@Nonnull final String sScheme,
                            @Nonnull final String sServerName,
                            @Nonnegative final int nServerPort,
                            @Nonnull final String sContextPath)
  {
    m_sScheme = sScheme;
    m_sServerName = sServerName;
    m_nServerPort = nServerPort;
    m_sContextPath = sContextPath;

    int nDefaultPort = CWeb.DEFAULT_PORT_HTTP;
    if ("https".equals (sScheme))
      nDefaultPort = CWeb.DEFAULT_PORT_HTTPS;

    final StringBuilder aSB = new StringBuilder ();
    aSB.append (sScheme).append ("://").append (sServerName);
    if (nServerPort != nDefaultPort)
    {
      // append non-standard port
      aSB.append (':').append (nServerPort);
    }
    m_sFullServerPath = aSB.toString ();
    m_sFullServerAndContextPath = m_sFullServerPath + sContextPath;
  }

  /**
   * @return The scheme without any trailing "://"
   */
  @Nonnull
  public String getScheme ()
  {
    return m_sScheme;
  }

  /**
   * @return The server name without scheme
   */
  @Nonnull
  public String getServerName ()
  {
    return m_sServerName;
  }

  /**
   * @return The server port we're running on
   */
  public int getServerPort ()
  {
    return m_nServerPort;
  }

  /**
   * @return /context
   */
  @Nonnull
  public String getContextPath ()
  {
    return m_sContextPath;
  }

  /**
   * @return http://server:port
   */
  @Nonnull
  public String getFullServerPath ()
  {
    return m_sFullServerPath;
  }

  /**
   * @return http://server:port/context
   */
  @Nonnull
  public String getFullContextPath ()
  {
    return m_sFullServerAndContextPath;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("scheme", m_sScheme)
                                       .append ("serverName", m_sServerName)
                                       .append ("serverPort", m_nServerPort)
                                       .append ("contextPath", m_sContextPath)
                                       .append ("fullServerPath", m_sFullServerPath)
                                       .append ("fullServerAndContextPath", m_sFullServerAndContextPath)
                                       .toString ();
  }

  @Nonnull
  public static StaticServerInfo initFromFirstRequest (@Nonnull final IRequestWebScope aRequestWebScope)
  {
    return init (aRequestWebScope.getScheme (),
                 aRequestWebScope.getServerName (),
                 aRequestWebScope.getServerPort (),
                 WebScopeManager.getGlobalScope ().getContextPath ());
  }

  @Nonnull
  public static StaticServerInfo init (@Nonnull final String sScheme,
                                       @Nonnull final String sServerName,
                                       @Nonnegative final int nServerPort,
                                       @Nonnull final String sContextPath)
  {
    if (s_aDefault != null)
      throw new IllegalStateException ("Static server info already present!");

    s_aDefault = new StaticServerInfo (sScheme, sServerName, nServerPort, sContextPath);
    return s_aDefault;
  }

  public static boolean isSet ()
  {
    return s_aDefault != null;
  }

  @Nonnull
  public static StaticServerInfo getInstance ()
  {
    final StaticServerInfo ret = s_aDefault;
    if (ret == null)
      throw new IllegalStateException ("No default web server info present!");
    return ret;
  }
}
