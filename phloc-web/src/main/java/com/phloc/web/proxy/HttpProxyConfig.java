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
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.SystemProperties;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.web.port.DefaultNetworkPorts;

/**
 * HTTP proxy configuration.
 * 
 * @author philip
 */
@Immutable
public final class HttpProxyConfig implements IProxyConfig
{
  private final EHttpProxyType m_eProxyType;
  private final String m_sHost;
  private final int m_nPort;
  private final List <String> m_aNonProxyHosts = new ArrayList <String> ();

  public HttpProxyConfig (@Nonnull final EHttpProxyType eProxyType,
                          @Nonnull @Nonempty final String sHost,
                          @Nonnegative final int nPort)
  {
    this (eProxyType, sHost, nPort, null);
  }

  public HttpProxyConfig (@Nonnull final EHttpProxyType eProxyType,
                          @Nonnull @Nonempty final String sHost,
                          @Nonnegative final int nPort,
                          @Nullable final List <String> aNonProxyHosts)
  {
    if (eProxyType == null)
      throw new NullPointerException ("proxyType");
    if (StringHelper.hasNoText (sHost))
      throw new IllegalArgumentException ("host may not empty");
    if (!DefaultNetworkPorts.isValidPort (nPort))
      throw new IllegalArgumentException ("The passed port is invalid");
    m_eProxyType = eProxyType;
    m_sHost = sHost;
    m_nPort = nPort;
    if (aNonProxyHosts != null)
      for (final String sNonProxyHost : aNonProxyHosts)
        if (StringHelper.hasText (sNonProxyHost))
          m_aNonProxyHosts.add (sNonProxyHost);
  }

  @Nonnull
  public EHttpProxyType getType ()
  {
    return m_eProxyType;
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

  @Nonnull
  @ReturnsImmutableObject
  public List <String> getNonProxyHosts ()
  {
    return ContainerHelper.makeUnmodifiable (m_aNonProxyHosts);
  }

  public void activateGlobally ()
  {
    // Deactivate other proxy configurations
    SocksProxyConfig.deactivateGlobally ();
    UseSystemProxyConfig.deactivateGlobally ();

    SystemProperties.setPropertyValue (m_eProxyType.getPropertyNameProxyHost (), m_sHost);
    SystemProperties.setPropertyValue (m_eProxyType.getPropertyNameProxyPort (), Integer.toString (m_nPort));
    SystemProperties.setPropertyValue (m_eProxyType.getPropertyNameNoProxyHosts (),
                                       StringHelper.getImploded ("|", m_aNonProxyHosts));
  }

  public static void deactivateGlobally ()
  {
    for (final EHttpProxyType eProxyType : EHttpProxyType.values ())
    {
      SystemProperties.removePropertyValue (eProxyType.getPropertyNameProxyHost ());
      SystemProperties.removePropertyValue (eProxyType.getPropertyNameProxyPort ());
      SystemProperties.removePropertyValue (eProxyType.getPropertyNameNoProxyHosts ());
    }
  }

  @Nonnull
  public Proxy asProxy ()
  {
    return new Proxy (Proxy.Type.HTTP, new InetSocketAddress (m_sHost, m_nPort));
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("host", m_sHost)
                                       .append ("port", m_nPort)
                                       .append ("nonProxyHosts", m_aNonProxyHosts)
                                       .toString ();
  }
}
