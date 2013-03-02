package com.phloc.web.mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.servlet.ServletContext;

import org.junit.Test;

import com.phloc.web.http.EHTTPMethod;

/**
 * Test class for class {@link MockHttpServletRequest}.
 * 
 * @author philip
 */
public final class MockHttpServletRequestTest
{
  @Test
  public void testSetPathsFromRequestURI ()
  {
    final String sContextPath = "/ctx";
    final ServletContext aSC = new MockServletContext (sContextPath);
    final MockHttpServletRequest c = new MockHttpServletRequest (aSC, EHTTPMethod.GET, sContextPath + "/servlet?x=y").setPathsFromRequestURI ();
    assertNull (c.getScheme ());
    assertNull (c.getServerName ());
    assertEquals (-1, c.getServerPort ());
    assertEquals (sContextPath, c.getContextPath ());
    assertEquals ("/servlet", c.getServletPath ());
    assertEquals ("", c.getPathInfo ());
    assertEquals (sContextPath + "/servlet", c.getRequestURI ());
    assertEquals ("x=y", c.getQueryString ());

    c.setRequestURI (sContextPath + "/servlet/path/in/servlet#anchor");
    c.setPathsFromRequestURI ();
    assertNull (c.getScheme ());
    assertNull (c.getServerName ());
    assertEquals (-1, c.getServerPort ());
    assertEquals (sContextPath, c.getContextPath ());
    assertEquals ("/servlet", c.getServletPath ());
    assertEquals ("/path/in/servlet", c.getPathInfo ());
    assertEquals (sContextPath + "/servlet/path/in/servlet", c.getRequestURI ());
    assertNull (c.getQueryString ());
  }
}
