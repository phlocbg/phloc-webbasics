package com.phloc.webscopes.servlets;

import javax.servlet.http.HttpServletRequest;

import org.junit.Rule;
import org.junit.Test;

import com.phloc.web.http.EHTTPMethod;
import com.phloc.web.mock.MockHttpServletRequest;
import com.phloc.webscopes.mock.WebScopeTestRule;

/**
 * Test class for class {@link AbstractUnifiedResponseServlet}.
 * 
 * @author philip
 */
public final class AbstractUnifiedResponseServletTest
{
  @Rule
  public WebScopeTestRule m_aRule = new WebScopeTestRule ();

  @Test
  public void testBasic ()
  {
    m_aRule.getServletPool ().registerServlet (MockUnifiedResponseServlet.class, "/mock/*", "MockServlet", null);

    final HttpServletRequest aRequest = new MockHttpServletRequest (m_aRule.getServletContext (), EHTTPMethod.GET, "");
    // m_aRule.invoke (new Mock
  }
}
