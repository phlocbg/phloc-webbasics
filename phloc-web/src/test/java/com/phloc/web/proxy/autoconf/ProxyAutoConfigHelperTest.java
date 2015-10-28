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
package com.phloc.web.proxy.autoconf;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.web.proxy.IProxyConfig;

public final class ProxyAutoConfigHelperTest
{
  private static final String [] PAC_FILES = new String [] { "brz-proxy.pac",
                                                            "wikipedia-pac.js",
                                                            "returnproxy-complex.js",
                                                            "returnproxy-simple-with-loadbalancing.js",
                                                            "returnproxy-simple.js",
                                                            "ente.regione.emr.it.js" };

  @Test
  public void testFindProxyForURL ()
  {
    for (final String sFile : PAC_FILES)
    {
      final ProxyAutoConfigHelper aPACHelper = new ProxyAutoConfigHelper (new ClassPathResource ("proxyautoconf/pacfiles/" +
                                                                                                 sFile));
      assertNotNull (aPACHelper.findProxyForURL ("http://www.orf.at/index.html", "www.orf.at"));
    }
  }

  @Test
  public void testGetProxyListForURL ()
  {
    for (final String sFile : PAC_FILES)
    {
      final ProxyAutoConfigHelper aPACHelper = new ProxyAutoConfigHelper (new ClassPathResource ("proxyautoconf/pacfiles/" +
                                                                                                 sFile));
      final List <IProxyConfig> aPC = aPACHelper.getProxyListForURL ("http://www.orf.at/index.html", "www.orf.at");
      assertNotNull (aPC);
      assertFalse (aPC.isEmpty ());
    }
  }
}
