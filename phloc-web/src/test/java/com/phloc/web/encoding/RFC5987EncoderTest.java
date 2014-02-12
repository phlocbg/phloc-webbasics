package com.phloc.web.encoding;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class for class {@link RFC5987Encoder}.
 * 
 * @author Philip Helger
 */
public class RFC5987EncoderTest
{
  @Test
  public void testBasic ()
  {
    assertEquals ("abc", RFC5987Encoder.getRFC5987EncodedUTF8 ("abc"));
    assertEquals ("%20", RFC5987Encoder.getRFC5987EncodedUTF8 (" "));
    assertEquals ("a%20b%25c", RFC5987Encoder.getRFC5987EncodedUTF8 ("a b%c"));
  }
}
