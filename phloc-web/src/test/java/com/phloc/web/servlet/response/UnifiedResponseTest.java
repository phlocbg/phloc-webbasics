package com.phloc.web.servlet.response;

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
}
