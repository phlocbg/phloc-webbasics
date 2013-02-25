package com.phloc.web.smtp.attachment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link EmailAttachment}.
 * 
 * @author philip
 */
public final class EmailAttachmentTest
{
  @Test
  public void testBasic ()
  {
    final EmailAttachment a = new EmailAttachment ("test.txt", "Inhalt".getBytes ());
    assertEquals ("test.txt", a.getFilename ());
    assertEquals ("text/plain", a.getContentType ());
    assertNotNull (a.getInputStreamProvider ());
    assertNotNull (a.getAsDataSource ());

    PhlocTestUtils.testMicroTypeConversion (a);
  }
}
