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
package com.phloc.web.proxy;

import java.net.InetSocketAddress;
import java.net.Proxy;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.SystemProperties;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.web.port.DefaultNetworkPorts;

/**
 * SOCKS proxy configuration.
 * 
 * @author Philip Helger
 */
@Immutable
public final class SocksProxyConfig implements IProxyConfig
{
  public static final String SYSPROP_SOCKS_PROXY_HOST = "socksProxyHost";
  public static final String SYSPROP_SOCKS_PROXY_PORT = "socksProxyPort";
  public static final int DEFAULT_SOCKS_PROXY_PORT = 1080;
  private final String m_sHost;
  private final int m_nPort;

  /**
   * Create a SOCKS proxy config object based on the default port
   * {@link #DEFAULT_SOCKS_PROXY_PORT}.
   * 
   * @param sHost
   *        The SOCKS proxy host. May not be <code>null</code>.
   */
  public SocksProxyConfig (@Nonnull final String sHost)
  {
    this (sHost, DEFAULT_SOCKS_PROXY_PORT);
  }

  /**
   * Create a SOCKS proxy config object based on the given port.
   * 
   * @param sHost
   *        The SOCKS proxy host. May not be <code>null</code>.
   * @param nPort
   *        The port to use for communication. Must be &ge; 0.
   */
  public SocksProxyConfig (@Nonnull final String sHost, @Nonnegative final int nPort)
  {
    if (StringHelper.hasNoText (sHost))
      throw new IllegalArgumentException ("host may not empty");
    if (!DefaultNetworkPorts.isValidPort (nPort))
      throw new IllegalArgumentException ("The passed port is invalid");
    m_sHost = sHost;
    m_nPort = nPort;
  }

  @Nonnull
  public String getHost ()
  {
    return m_sHost;
  }

  @Nonnegative
  public int getPort ()
  {
    return m_nPort;
  }

  public void activateGlobally ()
  {
    // Deactivate other proxy configurations
    HttpProxyConfig.deactivateGlobally ();
    UseSystemProxyConfig.deactivateGlobally ();

    SystemProperties.setPropertyValue (SYSPROP_SOCKS_PROXY_HOST, m_sHost);
    SystemProperties.setPropertyValue (SYSPROP_SOCKS_PROXY_PORT, Integer.toString (m_nPort));
  }

  public static void deactivateGlobally ()
  {
    SystemProperties.removePropertyValue (SYSPROP_SOCKS_PROXY_HOST);
    SystemProperties.removePropertyValue (SYSPROP_SOCKS_PROXY_PORT);
  }

  /**
   * @deprecated Use {@link #getAsProxy()} instead
   */
  @Deprecated
  @Nonnull
  public Proxy asProxy ()
  {
    return getAsProxy ();
  }

  @Nonnull
  public Proxy getAsProxy ()
  {
    return new Proxy (Proxy.Type.SOCKS, new InetSocketAddress (m_sHost, m_nPort));
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("host", m_sHost).append ("port", m_nPort).toString ();
  }
}
