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
package com.phloc.web.dns;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.xbill.DNS.Address;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.UsedViaReflection;

/**
 * A simple DNS resolver, using the dnsjava library.
 * 
 * @author Philip Helger
 */
@Immutable
public final class DNSResolver
{
  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final DNSResolver s_aInstance = new DNSResolver ();

  private DNSResolver ()
  {}

  @Nullable
  public static InetAddress resolveByName (@Nonnull final String sHostName)
  {
    try
    {
      return Address.getByName (sHostName);
    }
    catch (final UnknownHostException ex)
    {
      return null;
    }
  }

  /**
   * JavaScript callback function! Do not rename!
   * 
   * @param sHostName
   *        The host name.
   * @return The resolved IP address as String or <code>null</code> if the host
   *         name could not be resolved.
   */
  @Nullable
  public static String dnsResolve (@Nonnull final String sHostName)
  {
    final InetAddress aAddress = resolveByName (sHostName);
    if (aAddress == null)
      return null;
    return new IPV4Addr (aAddress.getAddress ()).getAsString ();
  }

  /**
   * This method must be present for the proxy-auto configuration setup!
   * 
   * @return My IP address
   * @see #getMyIpAddress()
   */
  @Nonnull
  @Deprecated
  @UsedViaReflection
  public static String myIpAddress ()
  {
    return getMyIpAddress ();
  }

  @Nonnull
  public static String getMyIpAddress ()
  {
    try
    {
      final InetAddress aAddress = InetAddress.getLocalHost ();
      if (aAddress != null)
        return new IPV4Addr (aAddress.getAddress ()).getAsString ();
    }
    catch (final UnknownHostException ex)
    {
      // fall through
    }
    return "127.0.0.1";
  }
}
