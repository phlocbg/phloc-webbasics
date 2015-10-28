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
package com.phloc.web.smtp.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link ReadonlySMTPSettingsMicroTypeConverter}.
 * 
 * @author Philip Helger
 */
public final class ReadonlySMTPSettingsTest
{
  @Test
  public void testConvert ()
  {
    ReadonlySMTPSettings aSettings = new ReadonlySMTPSettings ("mail.example.com",
                                                               19,
                                                               "anyuser",
                                                               "secret",
                                                               CCharset.CHARSET_UTF_8,
                                                               true);
    assertEquals ("mail.example.com", aSettings.getHostName ());
    assertEquals (19, aSettings.getPort ());
    assertEquals ("anyuser", aSettings.getUserName ());
    assertEquals ("secret", aSettings.getPassword ());
    assertTrue (aSettings.isSSLEnabled ());
    assertFalse (aSettings.isSTARTTLSEnabled ());
    PhlocTestUtils.testMicroTypeConversion (aSettings);

    aSettings = new ReadonlySMTPSettings ("mail.example.com",
                                          19,
                                          "anyuser",
                                          "secret",
                                          CCharset.CHARSET_UTF_8,
                                          true,
                                          true,
                                          5000,
                                          1200);
    assertEquals ("mail.example.com", aSettings.getHostName ());
    assertEquals (19, aSettings.getPort ());
    assertEquals ("anyuser", aSettings.getUserName ());
    assertEquals ("secret", aSettings.getPassword ());
    assertTrue (aSettings.isSSLEnabled ());
    assertTrue (aSettings.isSTARTTLSEnabled ());
    assertEquals (5000, aSettings.getConnectionTimeoutMilliSecs ());
    assertEquals (1200, aSettings.getTimeoutMilliSecs ());
    PhlocTestUtils.testMicroTypeConversion (aSettings);

    aSettings = new ReadonlySMTPSettings ("mail.example.com");
    assertEquals ("mail.example.com", aSettings.getHostName ());
    assertEquals (-1, aSettings.getPort ());
    assertNull (aSettings.getUserName ());
    assertNull (aSettings.getPassword ());
    assertFalse (aSettings.isSSLEnabled ());
    assertFalse (aSettings.isSTARTTLSEnabled ());
    PhlocTestUtils.testMicroTypeConversion (aSettings);
  }
}
