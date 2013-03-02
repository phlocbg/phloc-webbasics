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
package com.phloc.web.servlet.request;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.phloc.commons.url.SMap;
import com.phloc.web.http.EHTTPMethod;
import com.phloc.web.mock.MockHttpServletRequest;

/**
 * Test class for class {@link RequestHelper}.
 * 
 * @author philip
 */
public final class RequestHelperTest
{
  @Test
  public void testGetRequestURI ()
  {
    final MockHttpServletRequest r = new MockHttpServletRequest (EHTTPMethod.GET, "/context/servlet/index.xyz?x=1");
    r.addParameters (new SMap ().add ("abc", "xyz"));
    assertEquals ("/context/servlet/index.xyz?x=1", RequestHelper.getRequestURI (r));
  }

  @Test
  public void testGetRequestString ()
  {
    assertEquals ("http://server:517/context/servlet/index.xyz?x=1",
                  RequestHelper.getFullServerNameAndPath ("http", "server", 517, "/context/servlet/index.xyz", "x=1"));
  }
}
