package com.phloc.web.http.basicauth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test class for class {@link BasicAuthResponseBuilder}.
 * 
 * @author philip
 */
public final class BasicAuthResponseBuilderTest
{
  @Test
  public void testBasic ()
  {
    final BasicAuthResponseBuilder b = new BasicAuthResponseBuilder ();
    try
    {
      // Mandatory realm not present
      b.build ();
      fail ();
    }
    catch (final IllegalStateException ex)
    {}
    b.setRealm ("xyz");
    assertEquals ("Basic realm=\"xyz\"", b.build ());
    b.setRealm ("anything");
    assertEquals ("Basic realm=\"anything\"", b.build ());
  }
}
