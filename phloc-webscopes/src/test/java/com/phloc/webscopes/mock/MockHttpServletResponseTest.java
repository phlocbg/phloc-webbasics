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
package com.phloc.webscopes.mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.system.SystemHelper;
import com.phloc.web.mock.MockHttpServletResponse;

/**
 * Test class for class {@link MockHttpServletResponse}.
 * 
 * @author philip
 */
public final class MockHttpServletResponseTest
{
  private static final String TEST_STRING = "Test äöü";

  @Test
  public void testRequestResponse ()
  {
    // create a new Servlet context for testing
    final MockHttpServletResponse aResp = new MockHttpServletResponse ();
    assertEquals (CCharset.CHARSET_UTF_8, aResp.getCharacterEncoding ());
    aResp.getWriter ().write (TEST_STRING);
    assertFalse (aResp.isCommitted ());
    assertEquals (TEST_STRING, aResp.getContentAsString ());
    assertTrue (aResp.isCommitted ());

    // Start over
    aResp.setCommitted (false);
    aResp.reset ();
    assertNull (aResp.getCharacterEncoding ());
    assertFalse (aResp.isCommitted ());
    // Set character encoding before writing
    aResp.setCharacterEncoding (CCharset.CHARSET_ISO_8859_1);
    aResp.getWriter ().write (TEST_STRING);
    assertEquals (CCharset.CHARSET_ISO_8859_1, aResp.getCharacterEncoding ());
    assertEquals (TEST_STRING, aResp.getContentAsString ());

    // Start over again
    aResp.setCommitted (false);
    aResp.reset ();
    assertNull (aResp.getCharacterEncoding ());
    assertFalse (aResp.isCommitted ());
    // Write in the system charset
    aResp.getWriter ().write (TEST_STRING);
    // Set character encoding after writing
    aResp.setCharacterEncoding ("UTF-16");
    assertEquals ("UTF-16", aResp.getCharacterEncoding ());
    // It will fail in the selected charset
    assertFalse (TEST_STRING.equals (aResp.getContentAsString ()));
    // Retrieving in the system charset will succeed
    assertTrue (TEST_STRING.equals (aResp.getContentAsString (SystemHelper.getSystemCharsetName ())));
  }
}
