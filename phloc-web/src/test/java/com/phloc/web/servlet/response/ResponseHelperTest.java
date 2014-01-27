package com.phloc.web.servlet.response;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.phloc.web.mock.MockHttpServletRequest;

/**
 * Test class for class {@link ResponseHelper}.
 * 
 * @author Boris Gregorcic
 */
public class ResponseHelperTest
{
  @Test
  public void testAddRemoveCustomResponseHeader ()
  {
    final String sName = "FOO";
    final String sValue = "BAR";
    final String sValue2 = "NARF";
    final UnifiedResponse aResponse = new UnifiedResponse (new MockHttpServletRequest ());
    Assert.assertFalse (aResponse.getResponseHeaderMap ().containsHeaders (sName));
    {
      ResponseHelper.addCustomResponseHeader (aResponse, sName, sValue);
      Assert.assertTrue (aResponse.getResponseHeaderMap ().containsHeaders (sName));
      final List <String> aValues = aResponse.getResponseHeaderMap ().getHeaderValues (sName);
      Assert.assertEquals (1, aValues.size ());
      Assert.assertEquals (sValue, aValues.get (0));
    }
    {
      ResponseHelper.addCustomResponseHeader (aResponse, sName, sValue2);
      Assert.assertTrue (aResponse.getResponseHeaderMap ().containsHeaders (sName));
      final List <String> aValues = aResponse.getResponseHeaderMap ().getHeaderValues (sName);
      Assert.assertEquals (2, aValues.size ());
      Assert.assertEquals (sValue, aValues.get (0));
      Assert.assertEquals (sValue2, aValues.get (1));
    }
    {
      ResponseHelper.removeCustomResponseHeaders (aResponse, sName);
      Assert.assertFalse (aResponse.getResponseHeaderMap ().containsHeaders (sName));
    }
  }
}
