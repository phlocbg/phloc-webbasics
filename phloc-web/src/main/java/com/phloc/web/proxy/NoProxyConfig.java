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

import java.net.Proxy;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.string.ToStringGenerator;

/**
 * Class for indicating direct Internet access (no proxy needed).
 * 
 * @author Philip Helger
 */
@Immutable
public final class NoProxyConfig implements IProxyConfig
{
  private static final NoProxyConfig s_aInstance = new NoProxyConfig ();

  private NoProxyConfig ()
  {}

  @Nonnull
  public static NoProxyConfig getInstance ()
  {
    return s_aInstance;
  }

  public void activateGlobally ()
  {
    // Deactivate all other proxy configurations
    HttpProxyConfig.deactivateGlobally ();
    SocksProxyConfig.deactivateGlobally ();
    UseSystemProxyConfig.deactivateGlobally ();
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
    return Proxy.NO_PROXY;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
