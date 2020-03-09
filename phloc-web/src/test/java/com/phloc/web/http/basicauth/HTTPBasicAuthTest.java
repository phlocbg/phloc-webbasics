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
package com.phloc.web.http.basicauth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;

/**
 * Test class for class {@link HTTPBasicAuth}.
 * 
 * @author Boris Gregorcic
 */
public final class HTTPBasicAuthTest
{
  @Test
  public void testBasic ()
  {
    final BasicAuthClientCredentials aCredentials = new BasicAuthClientCredentials ("Alladin", "open sesame"); //$NON-NLS-1$ //$NON-NLS-2$
    final String sValue = aCredentials.getRequestValue ();
    assertNotNull (sValue);
    final BasicAuthClientCredentials aDecoded = HTTPBasicAuth.getBasicAuthClientCredentials (sValue);
    assertNotNull (aDecoded);
    assertEquals (aCredentials, aDecoded);
  }

  @Test
  public void testUserNameOnly ()
  {
    BasicAuthClientCredentials aCredentials = new BasicAuthClientCredentials ("Alladin"); //$NON-NLS-1$
    String sValue = aCredentials.getRequestValue ();
    assertNotNull (sValue);
    BasicAuthClientCredentials aDecoded = HTTPBasicAuth.getBasicAuthClientCredentials (sValue);
    assertNotNull (aDecoded);
    assertEquals (aCredentials, aDecoded);

    aCredentials = new BasicAuthClientCredentials ("Alladin", ""); //$NON-NLS-1$ //$NON-NLS-2$
    sValue = aCredentials.getRequestValue ();
    assertNotNull (sValue);
    aDecoded = HTTPBasicAuth.getBasicAuthClientCredentials (sValue);
    assertNotNull (aDecoded);
    assertEquals (aCredentials, aDecoded);
  }

  @Test
  public void testGetBasicAuthValues ()
  {
    assertNull (HTTPBasicAuth.getBasicAuthClientCredentials ((String) null));
    assertNull (HTTPBasicAuth.getBasicAuthClientCredentials ("")); //$NON-NLS-1$
    assertNull (HTTPBasicAuth.getBasicAuthClientCredentials ("bla")); //$NON-NLS-1$
    assertNull (HTTPBasicAuth.getBasicAuthClientCredentials ("bla foor")); //$NON-NLS-1$
    assertNull (HTTPBasicAuth.getBasicAuthClientCredentials ("Basic")); //$NON-NLS-1$
    assertNull (HTTPBasicAuth.getBasicAuthClientCredentials ("  Basic  ")); //$NON-NLS-1$
    // Base64 with blanks is OK!
    BasicAuthClientCredentials aUP = HTTPBasicAuth.getBasicAuthClientCredentials ("  Basic  QWxsYW  Rp   bjpvcG  VuIH Nlc2F tZQ   =  =   "); //$NON-NLS-1$
    assertNotNull (aUP);
    assertEquals ("Alladin", aUP.getUserName ()); //$NON-NLS-1$
    assertEquals ("open sesame", aUP.getPassword ()); //$NON-NLS-1$

    aUP = HTTPBasicAuth.getBasicAuthClientCredentials ("  Basic  QWxsYWRpbjpvcGVuIHNlc2FtZQ==   "); //$NON-NLS-1$
    assertNotNull (aUP);
    assertEquals ("Alladin", aUP.getUserName ()); //$NON-NLS-1$
    assertEquals ("open sesame", aUP.getPassword ()); //$NON-NLS-1$
  }

  @Test
  public void testGetBasicAuthEncoding ()
  {
    try
    {
      final String sUserName = "Üser§"; //$NON-NLS-1$
      final String sPassword = "Pässwörd"; //$NON-NLS-1$
      final BasicAuthClientCredentials aBA = new BasicAuthClientCredentials (sUserName, sPassword);
      final String sISO = aBA.getRequestValue ();
      final String sUTF8 = aBA.getRequestValue (CCharset.CHARSET_UTF_8_OBJ);

      assertEquals (aBA, HTTPBasicAuth.getBasicAuthClientCredentials (sISO));
      assertNotEquals (aBA, HTTPBasicAuth.getBasicAuthClientCredentials (sUTF8));

      HTTPBasicAuth.setCustomCharset (CCharset.CHARSET_UTF_8_OBJ);
      assertEquals (aBA, HTTPBasicAuth.getBasicAuthClientCredentials (sUTF8));
      assertNotEquals (aBA, HTTPBasicAuth.getBasicAuthClientCredentials (sISO));
    }
    finally
    {
      HTTPBasicAuth.setCustomCharset (null);
    }
  }
}
