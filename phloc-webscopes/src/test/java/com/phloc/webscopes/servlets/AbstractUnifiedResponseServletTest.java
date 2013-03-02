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
