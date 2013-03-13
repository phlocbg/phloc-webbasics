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
package com.phloc.web.http.basicauth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Test class for class {@link HTTPBasicAuth}.
 * 
 * @author philip
 */
public final class HTTPBasicAuthTest
{
  @Test
  public void testBasic ()
  {
    final BasicAuthCredentials aCredentials = new BasicAuthCredentials ("Alladin", "open sesame");
    final String sValue = HTTPBasicAuth.getRequestHeaderValue (aCredentials);
    assertNotNull (sValue);
    final BasicAuthCredentials aDecoded = HTTPBasicAuth.getBasicAuthCredentials (sValue);
    assertNotNull (aDecoded);
    assertEquals (aCredentials, aDecoded);
  }

  @Test
  public void testUserNameOnly ()
  {
    BasicAuthCredentials aCredentials = new BasicAuthCredentials ("Alladin");
    String sValue = HTTPBasicAuth.getRequestHeaderValue (aCredentials);
    assertNotNull (sValue);
    BasicAuthCredentials aDecoded = HTTPBasicAuth.getBasicAuthCredentials (sValue);
    assertNotNull (aDecoded);
    assertEquals (aCredentials, aDecoded);

    aCredentials = new BasicAuthCredentials ("Alladin", "");
    sValue = HTTPBasicAuth.getRequestHeaderValue (aCredentials);
    assertNotNull (sValue);
    aDecoded = HTTPBasicAuth.getBasicAuthCredentials (sValue);
    assertNotNull (aDecoded);
    assertEquals (aCredentials, aDecoded);
  }

  @Test
  public void testGetBasicAuthValues ()
  {
    assertNull (HTTPBasicAuth.getBasicAuthCredentials (null));
    assertNull (HTTPBasicAuth.getBasicAuthCredentials (""));
    assertNull (HTTPBasicAuth.getBasicAuthCredentials ("bla"));
    assertNull (HTTPBasicAuth.getBasicAuthCredentials ("bla foor"));
    assertNull (HTTPBasicAuth.getBasicAuthCredentials ("Basic"));
    assertNull (HTTPBasicAuth.getBasicAuthCredentials ("  Basic  "));
    // Base64 with blanks is OK!
    BasicAuthCredentials aUP = HTTPBasicAuth.getBasicAuthCredentials ("  Basic  QWxsYW  Rp   bjpvcG  VuIH Nlc2F tZQ   =  =   ");
    assertNotNull (aUP);
    assertEquals ("Alladin", aUP.getUserName ());
    assertEquals ("open sesame", aUP.getPassword ());

    aUP = HTTPBasicAuth.getBasicAuthCredentials ("  Basic  QWxsYWRpbjpvcGVuIHNlc2FtZQ==   ");
    assertNotNull (aUP);
    assertEquals ("Alladin", aUP.getUserName ());
    assertEquals ("open sesame", aUP.getPassword ());
  }
}
