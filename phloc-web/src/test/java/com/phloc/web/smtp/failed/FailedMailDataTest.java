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
 * @author philip
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
                                                      true);
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
