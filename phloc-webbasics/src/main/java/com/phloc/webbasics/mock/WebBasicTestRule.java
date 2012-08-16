package com.phloc.webbasics.mock;

import com.phloc.appbasics.mock.AppBasicTestRule;
import com.phloc.scopes.web.mock.MockHttpListener;
import com.phloc.scopes.web.mock.MockServletRequestListener;
import com.phloc.scopes.web.mock.WebScopeTestRule;
import com.phloc.webbasics.servlet.WebAppListener;

public class WebBasicTestRule extends WebScopeTestRule
{
  @Override
  public void before () throws Throwable
  {
    super.before ();
    initWebBasics ();
  }

  public static void initWebBasics ()
  {
    MockHttpListener.removeAllDefaultListeners ();
    MockHttpListener.addDefaultListener (new WebAppListener ());
    MockHttpListener.addDefaultListener (new MockServletRequestListener ());
    MockHttpListener.setToDefault ();

    AppBasicTestRule.initAppBasics ();
  }
}
