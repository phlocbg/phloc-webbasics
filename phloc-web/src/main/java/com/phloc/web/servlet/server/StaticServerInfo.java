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
package com.phloc.web.servlet.server;

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.web.servlet.request.RequestHelper;

/**
 * This singleton instance represents default server information for locations
 * where no request context is available (e.g. in scheduled tasks)
 * 
 * @author Philip Helger
 */
@Immutable
public final class StaticServerInfo
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (StaticServerInfo.class);
  private static volatile StaticServerInfo s_aDefault;

  private final String m_sScheme;
  private final String m_sServerName;
  private final int m_nServerPort;
  private final String m_sContextPath;
  private final String m_sFullServerPath;
  private final String m_sFullServerAndContextPath;

  private StaticServerInfo (@Nonnull @Nonempty final String sScheme,
                            @Nonnull @Nonempty final String sServerName,
                            @Nonnegative final int nServerPort,
                            @Nonnull final String sContextPath)
  {
    if (StringHelper.hasNoText (sScheme))
      throw new IllegalArgumentException ("scheme");
    if (StringHelper.hasNoText (sServerName))
      throw new IllegalArgumentException ("serverName");
    // may be empty!!
    if (sContextPath == null)
      throw new NullPointerException ("contextPath");

    m_sScheme = sScheme;
    m_sServerName = sServerName;
    m_nServerPort = RequestHelper.getServerPortToUse (sScheme, nServerPort);
    m_sContextPath = sContextPath;

    m_sFullServerPath = RequestHelper.getFullServerName (sScheme, sServerName, nServerPort).toString ();
    m_sFullServerAndContextPath = m_sFullServerPath + sContextPath;
  }

  /**
   * @return The scheme. E.g. "http" or "https".
   */
  @Nonnull
  public String getScheme ()
  {
    return m_sScheme;
  }

  /**
   * @return The server name without scheme. E.g. "www.phloc.com"
   */
  @Nonnull
  public String getServerName ()
  {
    return m_sServerName;
  }

  /**
   * @return The server port we're running on. May be -1, if constructed from a
   *         URL and the passed scheme is neither "http" nor "https".
   */
  @CheckForSigned
  public int getServerPort ()
  {
    return m_nServerPort;
  }

  /**
   * @return <code>/context</code> or <code></code> (empty string)
   */
  @Nonnull
  public String getContextPath ()
  {
    return m_sContextPath;
  }

  /**
   * @return <code>scheme://server:port</code> or <code>scheme://server</code>
   *         if the default port was used
   */
  @Nonnull
  public String getFullServerPath ()
  {
    return m_sFullServerPath;
  }

  /**
   * @return <code>scheme://server:port/context</code> or
   *         <code>scheme://server:port</code> for the ROOT context.
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
  public static StaticServerInfo init (@Nonnull @Nonempty final String sScheme,
                                       @Nonnull @Nonempty final String sServerName,
                                       @Nonnegative final int nServerPort,
                                       @Nonnull final String sContextPath)
  {
    if (s_aDefault != null)
      throw new IllegalStateException ("Static server info already present!");

    final StaticServerInfo aDefault = new StaticServerInfo (sScheme, sServerName, nServerPort, sContextPath);
    s_aLogger.info ("Static server information set: " + aDefault.toString ());
    s_aDefault = aDefault;
    return aDefault;
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
