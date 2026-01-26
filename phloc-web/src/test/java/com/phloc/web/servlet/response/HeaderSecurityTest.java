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
package com.phloc.web.servlet.response;

import org.junit.Assert;
import org.junit.Test;

import com.phloc.web.mock.MockHttpServletRequest;

/**
 * Test class for class {@link UnifiedResponse}.
 * 
 * @author Boris Gregorcic
 */
public class HeaderSecurityTest
{

  @Test
  public void testHeaders ()
  {
    new Thread ( () -> {
      try
      {
        final MockHttpServletRequest aRequest = new MockHttpServletRequest ();
        final UnifiedResponse aResponse = new UnifiedResponse (aRequest);
        aRequest.addHeader ("secretIN", "narf");
        aRequest.addHeader ("publicIN", "puit");
        aResponse.addCustomResponseHeader ("secretOUT", "zoot");
        aResponse.addCustomResponseHeader ("publicOUT", "fjord");
        final String sRequestInfo = aResponse.showRequestInfo ();
        Assert.assertTrue (sRequestInfo.contains ("Request Headers:"));
        Assert.assertTrue (sRequestInfo.contains ("secretIN"));
        Assert.assertTrue (sRequestInfo.contains ("narf"));
        Assert.assertTrue (sRequestInfo.contains ("publicIN"));
        Assert.assertTrue (sRequestInfo.contains ("puit"));

        Assert.assertTrue (sRequestInfo.contains ("Response Headers:"));
        Assert.assertTrue (sRequestInfo.contains ("secretOUT"));
        Assert.assertTrue (sRequestInfo.contains ("zoot"));
        Assert.assertTrue (sRequestInfo.contains ("publicOUT"));
        Assert.assertTrue (sRequestInfo.contains ("puit"));
      }
      finally
      {
        HeaderSecurity.clear ();
      }
    }).start ();
  }

  @Test
  public void testHeadersLogRequestGlobal ()
  {
    new Thread ( () -> {
      try
      {
        final MockHttpServletRequest aRequest = new MockHttpServletRequest ();
        final UnifiedResponse aResponse = new UnifiedResponse (aRequest);

        HeaderSecurity.disableLogRequestHeadersOnError (true);

        aRequest.addHeader ("secretIN", "narf");
        aRequest.addHeader ("publicIN", "puit");
        aResponse.addCustomResponseHeader ("secretOUT", "zoot");
        aResponse.addCustomResponseHeader ("publicOUT", "fjord");
        final String sRequestInfo = aResponse.showRequestInfo ();
        Assert.assertFalse (sRequestInfo.contains ("Request Headers:"));
        Assert.assertFalse (sRequestInfo.contains ("secretIN"));
        Assert.assertFalse (sRequestInfo.contains ("narf"));
        Assert.assertFalse (sRequestInfo.contains ("publicIN"));
        Assert.assertFalse (sRequestInfo.contains ("puit"));

        Assert.assertTrue (sRequestInfo.contains ("Response Headers:"));
        Assert.assertTrue (sRequestInfo.contains ("secretOUT"));
        Assert.assertTrue (sRequestInfo.contains ("zoot"));
        Assert.assertTrue (sRequestInfo.contains ("publicOUT"));
        Assert.assertTrue (sRequestInfo.contains ("puit"));
      }
      finally
      {
        HeaderSecurity.clear ();
      }
    }).start ();
  }

  @Test
  public void testHeadersLogRequestRequest ()
  {
    new Thread ( () -> {
      try
      {
        final MockHttpServletRequest aRequest = new MockHttpServletRequest ();
        final UnifiedResponse aResponse = new UnifiedResponse (aRequest);

        HeaderSecurity.disableLogRequestHeadersOnError (false);

        aRequest.addHeader ("secretIN", "narf");
        aRequest.addHeader ("publicIN", "puit");
        aResponse.addCustomResponseHeader ("secretOUT", "zoot");
        aResponse.addCustomResponseHeader ("publicOUT", "fjord");
        final String sRequestInfo = aResponse.showRequestInfo ();
        Assert.assertFalse (sRequestInfo.contains ("Request Headers:"));
        Assert.assertFalse (sRequestInfo.contains ("secretIN"));
        Assert.assertFalse (sRequestInfo.contains ("narf"));
        Assert.assertFalse (sRequestInfo.contains ("publicIN"));
        Assert.assertFalse (sRequestInfo.contains ("puit"));

        Assert.assertTrue (sRequestInfo.contains ("Response Headers:"));
        Assert.assertTrue (sRequestInfo.contains ("secretOUT"));
        Assert.assertTrue (sRequestInfo.contains ("zoot"));
        Assert.assertTrue (sRequestInfo.contains ("publicOUT"));
        Assert.assertTrue (sRequestInfo.contains ("puit"));
      }
      finally
      {
        HeaderSecurity.clear ();
      }
    }).start ();
  }

  @Test
  public void testHeadersLogResponseGlobal ()
  {
    new Thread ( () -> {
      try
      {
        final MockHttpServletRequest aRequest = new MockHttpServletRequest ();
        final UnifiedResponse aResponse = new UnifiedResponse (aRequest);

        HeaderSecurity.disableLogResponseHeadersOnError (true);

        aRequest.addHeader ("secretIN", "narf");
        aRequest.addHeader ("publicIN", "puit");
        aResponse.addCustomResponseHeader ("secretOUT", "zoot");
        aResponse.addCustomResponseHeader ("publicOUT", "fjord");
        final String sRequestInfo = aResponse.showRequestInfo ();
        Assert.assertTrue (sRequestInfo.contains ("Request Headers:"));
        Assert.assertTrue (sRequestInfo.contains ("secretIN"));
        Assert.assertTrue (sRequestInfo.contains ("narf"));
        Assert.assertTrue (sRequestInfo.contains ("publicIN"));
        Assert.assertTrue (sRequestInfo.contains ("puit"));

        Assert.assertFalse (sRequestInfo.contains ("Response Headers:"));
        Assert.assertFalse (sRequestInfo.contains ("secretOUT"));
        Assert.assertFalse (sRequestInfo.contains ("zoot"));
        Assert.assertFalse (sRequestInfo.contains ("publicOUT"));
        Assert.assertFalse (sRequestInfo.contains ("puit"));
      }
      finally
      {
        HeaderSecurity.clear ();
      }
    }).start ();
  }

  @Test
  public void testHeadersLogResponseRequest ()
  {
    new Thread ( () -> {
      try
      {
        final MockHttpServletRequest aRequest = new MockHttpServletRequest ();
        final UnifiedResponse aResponse = new UnifiedResponse (aRequest);

        HeaderSecurity.disableLogResponseHeadersOnError (false);

        aRequest.addHeader ("secretIN", "narf");
        aRequest.addHeader ("publicIN", "puit");
        aResponse.addCustomResponseHeader ("secretOUT", "zoot");
        aResponse.addCustomResponseHeader ("publicOUT", "fjord");
        final String sRequestInfo = aResponse.showRequestInfo ();
        Assert.assertTrue (sRequestInfo.contains ("Request Headers:"));
        Assert.assertTrue (sRequestInfo.contains ("secretIN"));
        Assert.assertTrue (sRequestInfo.contains ("narf"));
        Assert.assertTrue (sRequestInfo.contains ("publicIN"));
        Assert.assertTrue (sRequestInfo.contains ("puit"));

        Assert.assertFalse (sRequestInfo.contains ("Response Headers:"));
        Assert.assertFalse (sRequestInfo.contains ("secretOUT"));
        Assert.assertFalse (sRequestInfo.contains ("zoot"));
        Assert.assertFalse (sRequestInfo.contains ("publicOUT"));
        Assert.assertFalse (sRequestInfo.contains ("puit"));
      }
      finally
      {
        HeaderSecurity.clear ();
      }
    }).start ();
  }

  @Test
  public void testHeadersSensitiveGlobal ()
  {
    new Thread ( () -> {
      try
      {
        final MockHttpServletRequest aRequest = new MockHttpServletRequest ();
        final UnifiedResponse aResponse = new UnifiedResponse (aRequest);

        HeaderSecurity.addSensitiveHeader ("secretIN", true);
        HeaderSecurity.addSensitiveHeader ("secretOUT", true);

        aRequest.addHeader ("secretIN", "narf");
        aRequest.addHeader ("publicIN", "puit");
        aResponse.addCustomResponseHeader ("secretOUT", "zoot");
        aResponse.addCustomResponseHeader ("publicOUT", "fjord");
        final String sRequestInfo = aResponse.showRequestInfo ();
        Assert.assertTrue (sRequestInfo.contains ("Request Headers:"));
        Assert.assertTrue (sRequestInfo.contains ("secretIN"));
        Assert.assertFalse (sRequestInfo.contains ("narf"));
        Assert.assertTrue (sRequestInfo.contains ("publicIN"));
        Assert.assertTrue (sRequestInfo.contains ("puit"));

        Assert.assertTrue (sRequestInfo.contains ("Response Headers:"));
        Assert.assertTrue (sRequestInfo.contains ("secretOUT"));
        Assert.assertFalse (sRequestInfo.contains ("zoot"));
        Assert.assertTrue (sRequestInfo.contains ("publicOUT"));
        Assert.assertTrue (sRequestInfo.contains ("puit"));
      }
      finally
      {
        HeaderSecurity.clear ();
      }
    }).start ();
  }

  @Test
  public void testHeadersSensitiveRequest ()
  {

    new Thread ( () -> {
      try
      {
        HeaderSecurity.addSensitiveHeader ("secretIN", false);
        HeaderSecurity.addSensitiveHeader ("secretOUT", false);

        final MockHttpServletRequest aRequest = new MockHttpServletRequest ();
        final UnifiedResponse aResponse = new UnifiedResponse (aRequest);

        aRequest.addHeader ("secretIN", "narf");
        aRequest.addHeader ("publicIN", "puit");
        aResponse.addCustomResponseHeader ("secretOUT", "zoot");
        aResponse.addCustomResponseHeader ("publicOUT", "fjord");
        final String sRequestInfo = aResponse.showRequestInfo ();
        Assert.assertTrue (sRequestInfo.contains ("Request Headers:"));
        Assert.assertTrue (sRequestInfo.contains ("secretIN"));
        Assert.assertFalse (sRequestInfo.contains ("narf"));
        Assert.assertTrue (sRequestInfo.contains ("publicIN"));
        Assert.assertTrue (sRequestInfo.contains ("puit"));

        Assert.assertTrue (sRequestInfo.contains ("Response Headers:"));
        Assert.assertTrue (sRequestInfo.contains ("secretOUT"));
        Assert.assertFalse (sRequestInfo.contains ("zoot"));
        Assert.assertTrue (sRequestInfo.contains ("publicOUT"));
        Assert.assertTrue (sRequestInfo.contains ("puit"));
      }
      catch (final Exception aEx)
      {
        HeaderSecurity.clear ();
        Assert.fail ();
        // swallow to make sure next thread runs
      }
    }).start ();

    new Thread ( () -> {
      try
      {
        final MockHttpServletRequest aRequest = new MockHttpServletRequest ();
        final UnifiedResponse aResponse = new UnifiedResponse (aRequest);

        aRequest.addHeader ("secretIN", "narf");
        aRequest.addHeader ("publicIN", "puit");
        aResponse.addCustomResponseHeader ("secretOUT", "zoot");
        aResponse.addCustomResponseHeader ("publicOUT", "fjord");
        final String sRequestInfo = aResponse.showRequestInfo ();
        Assert.assertTrue (sRequestInfo.contains ("Request Headers:"));
        Assert.assertTrue (sRequestInfo.contains ("secretIN"));
        Assert.assertTrue (sRequestInfo.contains ("narf"));
        Assert.assertTrue (sRequestInfo.contains ("publicIN"));
        Assert.assertTrue (sRequestInfo.contains ("puit"));

        Assert.assertTrue (sRequestInfo.contains ("Response Headers:"));
        Assert.assertTrue (sRequestInfo.contains ("secretOUT"));
        Assert.assertTrue (sRequestInfo.contains ("zoot"));
        Assert.assertTrue (sRequestInfo.contains ("publicOUT"));
        Assert.assertTrue (sRequestInfo.contains ("puit"));
      }
      finally
      {
        HeaderSecurity.clear ();
      }
    }).start ();
  }

  @Test
  public void testLazyInit ()
  {
    new Thread ( () -> {
      try
      {
        Assert.assertTrue (HeaderSecurity.isLogRequestHeadersOnError ());
        Assert.assertTrue (HeaderSecurity.isLogResponseHeadersOnError ());
        Assert.assertFalse (HeaderSecurity.isSensitiveHeader ("foo"));
      }
      finally
      {
        HeaderSecurity.clear ();
      }
    }).start ();
  }

}
