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
package com.phloc.web.http;

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
    final String sUsername = "Alladin";
    final String sPassword = "open sesame";
    final String sValue = HTTPBasicAuth.getRequestHeaderValue (sUsername, sPassword);
    assertNotNull (sValue);
    final String [] aDecoded = HTTPBasicAuth.getBasicAuthValues (sValue);
    assertNotNull (aDecoded);
    assertEquals (2, aDecoded.length);
    assertEquals (sUsername, aDecoded[0]);
    assertEquals (sPassword, aDecoded[1]);
  }

  @Test
  public void testPasswordOnly ()
  {
    final String sUsername = "Alladin";
    String sValue = HTTPBasicAuth.getRequestHeaderValue (sUsername, null);
    assertNotNull (sValue);
    String [] aDecoded = HTTPBasicAuth.getBasicAuthValues (sValue);
    assertNotNull (aDecoded);
    assertEquals (1, aDecoded.length);
    assertEquals (sUsername, aDecoded[0]);

    sValue = HTTPBasicAuth.getRequestHeaderValue (sUsername, "");
    assertNotNull (sValue);
    aDecoded = HTTPBasicAuth.getBasicAuthValues (sValue);
    assertNotNull (aDecoded);
    assertEquals (1, aDecoded.length);
    assertEquals (sUsername, aDecoded[0]);
  }

  @Test
  public void testGetBasicAuthValues ()
  {
    assertNull (HTTPBasicAuth.getBasicAuthValues (null));
    assertNull (HTTPBasicAuth.getBasicAuthValues (""));
    assertNull (HTTPBasicAuth.getBasicAuthValues ("bla"));
    assertNull (HTTPBasicAuth.getBasicAuthValues ("bla foor"));
    assertNull (HTTPBasicAuth.getBasicAuthValues ("Basic"));
    assertNull (HTTPBasicAuth.getBasicAuthValues ("  Basic  "));
    // Base64 with blanks is OK!
    String [] aUP = HTTPBasicAuth.getBasicAuthValues ("  Basic  QWxsYW  Rp   bjpvcG  VuIH Nlc2F tZQ   =  =   ");
    assertNotNull (aUP);
    assertEquals (2, aUP.length);
    assertEquals ("Alladin", aUP[0]);
    assertEquals ("open sesame", aUP[1]);

    aUP = HTTPBasicAuth.getBasicAuthValues ("  Basic  QWxsYWRpbjpvcGVuIHNlc2FtZQ==   ");
    assertNotNull (aUP);
    assertEquals (2, aUP.length);
    assertEquals ("Alladin", aUP[0]);
    assertEquals ("open sesame", aUP[1]);
  }
}
