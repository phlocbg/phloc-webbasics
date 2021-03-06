/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.webbasics.mock;

import java.util.Map;

import javax.annotation.Nullable;

import com.phloc.commons.url.SMap;
import com.phloc.web.mock.MockHttpListener;
import com.phloc.webbasics.servlet.WebAppListener;
import com.phloc.webscopes.mock.MockServletRequestListenerScopeAware;
import com.phloc.webscopes.mock.WebScopeTestRule;

/**
 * A JUnit test rule that is suitable for all webbasics projects
 * 
 * @author Philip Helger
 */
public class WebBasicTestRule extends WebScopeTestRule
{
  public WebBasicTestRule ()
  {
    this (new SMap ().add (WebAppListener.INIT_PARAMETER_NO_STARTUP_INFO, "true")
                     .add (WebAppListener.INIT_PARAMETER_NO_CHECK_FILE_ACCESS, "true"));
  }

  public WebBasicTestRule (@Nullable final Map <String, String> aServletContextInitParameters)
  {
    super (aServletContextInitParameters);
  }

  @Override
  protected void initListener ()
  {
    MockHttpListener.removeAllDefaultListeners ();
    MockHttpListener.addDefaultListener (new WebAppListener ());
    MockHttpListener.addDefaultListener (new MockServletRequestListenerScopeAware ());
    MockHttpListener.setToDefault ();
  }
}
