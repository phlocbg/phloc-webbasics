/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.web.servlet.request;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.phloc.commons.url.SMap;
import com.phloc.web.http.EHTTPMethod;
import com.phloc.web.mock.MockHttpServletRequest;
import com.phloc.web.mock.MockServletContext;

/**
 * Test class for class {@link RequestHelper}.
 * 
 * @author Philip Helger, Boris Gregorcic
 */
public final class RequestHelperTest
{
  @Test
  public void testGetRequestURI ()
  {
    final MockServletContext aSC = new MockServletContext ();
    final MockHttpServletRequest r = new MockHttpServletRequest (aSC, EHTTPMethod.GET).setAllPaths ("/context/servlet/index.xyz?x=1");
    assertEquals ("/context/servlet/index.xyz", RequestHelper.getRequestURI (r));
    r.addParameters (new SMap ().add ("abc", "xyz"));
    assertEquals ("/context/servlet/index.xyz", RequestHelper.getRequestURI (r));
  }

  @Test
  public void testGetRequestString ()
  {
    assertEquals ("http://server:517/context/servlet/index.xyz?x=1",
                  RequestHelper.getFullServerNameAndPath ("http", "server", 517, "/context/servlet/index.xyz", "x=1"));
  }

  @Test
  public void testGetPathWithinServletContext ()
  {
    final String sContext = "/My App";
    final MockHttpServletRequest aRequest = new MockHttpServletRequest ();
    aRequest.setContextPath (sContext);
    aRequest.setRequestURI ("/My%20App/dms/foobar");
    assertEquals ("/dms/foobar", RequestHelper.getPathWithinServletContext (aRequest));
  }
}
