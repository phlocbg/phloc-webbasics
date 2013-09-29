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
package com.phloc.web.smtp.failed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.idfactory.MemoryIntIDFactory;
import com.phloc.commons.mock.MockException;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.web.smtp.settings.ISMTPSettings;
import com.phloc.web.smtp.settings.SMTPSettings;

/**
 * Test class for class {@link FailedMailData}.
 * 
 * @author Philip Helger
 */
public final class FailedMailDataTest
{
  static
  {
    GlobalIDFactory.setPersistentIntIDFactory (new MemoryIntIDFactory ());
  }

  @Test
  public void testBasic ()
  {
    final ISMTPSettings aSettings = new SMTPSettings ("mail.example.com",
                                                      19,
                                                      "anyuser",
                                                      "secret",
                                                      CCharset.CHARSET_UTF_8,
                                                      true,
                                                      false);
    final Throwable aError = new MockException ("Test error");

    final FailedMailData aFMD = new FailedMailData (aSettings, aError);
    assertNotNull (aFMD.getID ());
    assertNotNull (aFMD.getErrorDateTime ());
    assertEquals (aSettings, aFMD.getSMTPSettings ());
    assertNull (aFMD.getOriginalSentDateTime ());
    assertNull (aFMD.getEmailData ());
    assertNotNull (aFMD.getError ());

    PhlocTestUtils.testMicroTypeConversion (aFMD);
  }
}
