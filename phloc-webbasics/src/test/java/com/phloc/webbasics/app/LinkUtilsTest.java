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
package com.phloc.webbasics.app;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.phloc.webscopes.mock.WebScopeTestRule;

/**
 * Test class for class {@link LinkUtils}.
 * 
 * @author Philip Helger
 */
public final class LinkUtilsTest
{
  @Rule
  public final TestRule m_aRules = new WebScopeTestRule ().setContextPath ("");

  @Test
  public void testStreamServletName ()
  {
    assertEquals (LinkUtils.DEFAULT_STREAM_SERVLET_NAME, LinkUtils.getStreamServletName ());
    LinkUtils.setStreamServletName ("abc");
    assertEquals ("/abc/x", LinkUtils.getStreamURL ("x").getAsString ());
    LinkUtils.setStreamServletName (LinkUtils.DEFAULT_STREAM_SERVLET_NAME);
    assertEquals (LinkUtils.DEFAULT_STREAM_SERVLET_NAME, LinkUtils.getStreamServletName ());
    assertEquals ("/" + LinkUtils.DEFAULT_STREAM_SERVLET_NAME + "/x", LinkUtils.getStreamURL ("x").getAsString ());
  }
}
