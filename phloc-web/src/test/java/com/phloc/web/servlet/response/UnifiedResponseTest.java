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
package com.phloc.web.servlet.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.phloc.web.http.CHTTPHeader;
import com.phloc.web.mock.MockHttpServletRequest;

/**
 * Test class for class {@link UnifiedResponse}.
 * 
 * @author Boris Gregorcic
 */
public class UnifiedResponseTest
{
  @Test
  public void testSetStrictTransportSecurity ()
  {
    final UnifiedResponse aResponse = new UnifiedResponse (new MockHttpServletRequest ());
    Assert.assertFalse (aResponse.getResponseHeaderMap ().containsHeaders (CHTTPHeader.STRICT_TRANSPORT_SECURITY));
    final int nMaxAgeSeconds = 60000;
    final boolean bIncludeSubdomains = true;
    aResponse.setStrictTransportSecurity (nMaxAgeSeconds, bIncludeSubdomains);
    Assert.assertTrue (aResponse.getResponseHeaderMap ().containsHeaders (CHTTPHeader.STRICT_TRANSPORT_SECURITY));
    final List <String> aValues = aResponse.getResponseHeaderMap ()
                                           .getHeaderValues (CHTTPHeader.STRICT_TRANSPORT_SECURITY);
    Assert.assertEquals (1, aValues.size ());
    Assert.assertEquals ("max-age=" + nMaxAgeSeconds + ";" + CHTTPHeader.VALUE_INCLUDE_SUBDMOAINS, aValues.get (0));
  }

  @Test
  public void testSetAllowMimeSniffing ()
  {
    final UnifiedResponse aResponse = new UnifiedResponse (new MockHttpServletRequest ());
    Assert.assertFalse (aResponse.getResponseHeaderMap ().containsHeaders (CHTTPHeader.X_CONTENT_TYPE_OPTIONS));
    aResponse.setAllowMimeSniffing (true);
    Assert.assertFalse (aResponse.getResponseHeaderMap ().containsHeaders (CHTTPHeader.X_CONTENT_TYPE_OPTIONS));
    aResponse.setAllowMimeSniffing (false);
    Assert.assertTrue (aResponse.getResponseHeaderMap ().containsHeaders (CHTTPHeader.X_CONTENT_TYPE_OPTIONS));
    final List <String> aValues = aResponse.getResponseHeaderMap ()
                                           .getHeaderValues (CHTTPHeader.X_CONTENT_TYPE_OPTIONS);
    Assert.assertEquals (1, aValues.size ());
    Assert.assertEquals (CHTTPHeader.VALUE_NOSNIFF, aValues.get (0));
  }

  @Test
  public void testAddRemoveCustomResponseHeader ()
  {
    final String sName = "FOO";
    final String sValue = "BAR";
    final String sValue2 = "NARF";
    final UnifiedResponse aResponse = new UnifiedResponse (new MockHttpServletRequest ());
    assertFalse (aResponse.getResponseHeaderMap ().containsHeaders (sName));
    {
      aResponse.addCustomResponseHeader (sName, sValue);
      assertTrue (aResponse.getResponseHeaderMap ().containsHeaders (sName));
      final List <String> aValues = aResponse.getResponseHeaderMap ().getHeaderValues (sName);
      assertEquals (1, aValues.size ());
      assertEquals (sValue, aValues.get (0));
    }
    {
      aResponse.addCustomResponseHeader (sName, sValue2);
      assertTrue (aResponse.getResponseHeaderMap ().containsHeaders (sName));
      final List <String> aValues = aResponse.getResponseHeaderMap ().getHeaderValues (sName);
      assertEquals (2, aValues.size ());
      assertEquals (sValue, aValues.get (0));
      assertEquals (sValue2, aValues.get (1));
    }
    {
      aResponse.removeCustomResponseHeaders (sName);
      assertFalse (aResponse.getResponseHeaderMap ().containsHeaders (sName));
    }
  }
}
