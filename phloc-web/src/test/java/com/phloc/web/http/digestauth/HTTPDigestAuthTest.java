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
package com.phloc.web.http.digestauth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.phloc.web.http.EHTTPMethod;

/**
 * Test class for class {@link HTTPDigestAuth}.
 * 
 * @author Philip Helger
 */
public final class HTTPDigestAuthTest
{
  @Test
  public void testGetDigestAuthValues ()
  {
    assertNull (HTTPDigestAuth.getDigestAuthClientCredentials ((String) null));
    assertNull (HTTPDigestAuth.getDigestAuthClientCredentials (""));
    assertNull (HTTPDigestAuth.getDigestAuthClientCredentials ("bla"));
    assertNull (HTTPDigestAuth.getDigestAuthClientCredentials ("bla foo"));
    assertNull (HTTPDigestAuth.getDigestAuthClientCredentials ("Basic"));
    assertNull (HTTPDigestAuth.getDigestAuthClientCredentials ("  Basic  "));
    assertNull (HTTPDigestAuth.getDigestAuthClientCredentials ("  Digest  username"));
    assertNull (HTTPDigestAuth.getDigestAuthClientCredentials ("  Digest  username="));
    assertNull (HTTPDigestAuth.getDigestAuthClientCredentials ("  Digest  username=ä"));
    assertNull (HTTPDigestAuth.getDigestAuthClientCredentials ("  Digest  username=abc x"));
    assertNull (HTTPDigestAuth.getDigestAuthClientCredentials ("  Digest  username=abc ,"));
    assertNull (HTTPDigestAuth.getDigestAuthClientCredentials ("  Digest  username=\""));
    assertNull (HTTPDigestAuth.getDigestAuthClientCredentials ("  Digest  username=\"abc"));
    assertNull (HTTPDigestAuth.getDigestAuthClientCredentials ("  Digest  username=\"abc\","));
    assertNull (HTTPDigestAuth.getDigestAuthClientCredentials ("  Digest  username=\"abc\" , "));
    final DigestAuthClientCredentials aUP = HTTPDigestAuth.getDigestAuthClientCredentials ("Digest username=\"Mufasa\",\r\n"
                                                                                           + "     realm=\"testrealm@host.com\",\r\n"
                                                                                           + "     nonce=\"dcd98b7102dd2f0e8b11d0f600bfb0c093\",\r\n"
                                                                                           + "     uri=\"/dir/index.html\",\r\n"
                                                                                           + "     qop=auth,\r\n"
                                                                                           + "     nc=00000001,\r\n"
                                                                                           + "     cnonce=\"0a4f113b\",\r\n"
                                                                                           + "     response=\"6629fae49393a05397450978507c4ef1\",\r\n"
                                                                                           + "     opaque=\"5ccc069c403ebaf9f0171e9517f40e41\"");
    assertNotNull (aUP);
    assertEquals ("Mufasa", aUP.getUserName ());
    assertEquals ("testrealm@host.com", aUP.getRealm ());
    assertEquals ("dcd98b7102dd2f0e8b11d0f600bfb0c093", aUP.getServerNonce ());
    assertEquals ("/dir/index.html", aUP.getDigestURI ());
    assertEquals ("auth", aUP.getMessageQOP ());
    assertEquals (1, aUP.getNonceCount ());
    assertEquals ("0a4f113b", aUP.getClientNonce ());
    assertEquals ("6629fae49393a05397450978507c4ef1", aUP.getResponse ());
    assertEquals ("5ccc069c403ebaf9f0171e9517f40e41", aUP.getOpaque ());
    assertNull (aUP.getAlgorithm ());
  }

  @Test
  public void testCreate ()
  {
    final DigestAuthClientCredentials aUP = HTTPDigestAuth.createDigestAuthClientCredentials (EHTTPMethod.GET,
                                                                                              "/dir/index.html",
                                                                                              "Mufasa",
                                                                                              "Circle Of Life",
                                                                                              "testrealm@host.com",
                                                                                              "dcd98b7102dd2f0e8b11d0f600bfb0c093",
                                                                                              null,
                                                                                              "0a4f113b",
                                                                                              "5ccc069c403ebaf9f0171e9517f40e41",
                                                                                              HTTPDigestAuth.QOP_AUTH,
                                                                                              1);
    assertNotNull (aUP);
    assertEquals ("6629fae49393a05397450978507c4ef1", aUP.getResponse ());
  }
}
