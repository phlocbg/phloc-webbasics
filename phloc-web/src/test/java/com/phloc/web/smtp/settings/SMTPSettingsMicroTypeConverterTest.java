package com.phloc.web.smtp.settings;

import org.junit.Test;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link SMTPSettingsMicroTypeConverter}.
 * 
 * @author philip
 */
public final class SMTPSettingsMicroTypeConverterTest
{
  @Test
  public void testConvert ()
  {
    final ISMTPSettings aSettings = new SMTPSettings ("mail.example.com",
                                                      19,
                                                      "anyuser",
                                                      "secret",
                                                      CCharset.CHARSET_UTF_8,
                                                      true);
    PhlocTestUtils.testMicroTypeConversion (aSettings);
  }
}
