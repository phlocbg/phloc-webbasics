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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.lang.EnumHelper;
import com.phloc.commons.url.EURLProtocol;
import com.phloc.commons.url.IURLProtocol;
import com.phloc.web.port.DefaultNetworkPorts;

/**
 * Proxy type determination.<br>
 * Source: http://java.sun.com/javase/6/docs/technotes/guides/net/proxies.html
 * 
 * @author philip
 */
public enum EHttpProxyType implements IHasID <String>
{
  HTTP ("http", EURLProtocol.HTTP, DefaultNetworkPorts.TCP_80_www_http.getPort ()),
  HTTPS ("https", EURLProtocol.HTTPS, DefaultNetworkPorts.TCP_443_https.getPort ()),
  FTP ("ftp", EURLProtocol.FTP, DefaultNetworkPorts.TCP_80_www_http.getPort ());

  private final String m_sID;
  private final IURLProtocol m_aURLProtocol;
  private final int m_nDefaultPort;

  private EHttpProxyType (@Nonnull @Nonempty final String sID,
                          @Nonnull final IURLProtocol aURLProtocol,
                          @Nonnegative final int nDefaultPort)
  {
    m_sID = sID;
    m_aURLProtocol = aURLProtocol;
    m_nDefaultPort = nDefaultPort;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  public IURLProtocol getURLProtocol ()
  {
    return m_aURLProtocol;
  }

  @Nonnegative
  public int getDefaultPort ()
  {
    return m_nDefaultPort;
  }

  @Nonnull
  public String getPropertyNameProxyHost ()
  {
    return m_sID + ".proxyHost";
  }

  @Nonnull
  public String getPropertyNameProxyPort ()
  {
    return m_sID + ".proxyPort";
  }

  @Nonnull
  public String getPropertyNameNoProxyHosts ()
  {
    // HTTPS uses the http noProxyHosts property
    return this == HTTPS ? HTTP.getPropertyNameNoProxyHosts () : m_sID + ".noProxyHosts";
  }

  @Nullable
  public static EHttpProxyType getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EHttpProxyType.class, sID);
  }

  @Nullable
  public static EHttpProxyType getFromURLProtocolOrDefault (@Nullable final IURLProtocol aURLProtocol,
                                                            @Nullable final EHttpProxyType eDefault)
  {
    for (final EHttpProxyType eProxyType : values ())
      if (eProxyType.m_aURLProtocol.equals (aURLProtocol))
        return eProxyType;
    return eDefault;
  }
}
