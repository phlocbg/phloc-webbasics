package com.phloc.webbasics.mock;

import java.io.File;

import javax.annotation.Nonnull;

import com.phloc.appbasics.mock.AppBasicTestRule;
import com.phloc.scopes.nonweb.mock.ScopeTestRule;
import com.phloc.scopes.web.mock.MockHttpListener;
import com.phloc.scopes.web.mock.MockServletRequestListener;
import com.phloc.scopes.web.mock.WebScopeTestRule;
import com.phloc.webbasics.servlet.WebAppListener;

public class WebBasicTestRule extends WebScopeTestRule
{
  private final File m_aStoragePath;

  public WebBasicTestRule ()
  {
    this (ScopeTestRule.STORAGE_PATH);
  }

  public WebBasicTestRule (@Nonnull final File aStoragePath)
  {
    if (aStoragePath == null)
      throw new NullPointerException ("storagePath");
    m_aStoragePath = aStoragePath;
  }

  @Nonnull
  public File getStoragePath ()
  {
    return m_aStoragePath;
  }

  @Override
  public void before ()
  {
    super.before ();
    initWebBasics (m_aStoragePath);
  }

  public static void initWebBasics (@Nonnull final File aStoragePath)
  {
    MockHttpListener.removeAllDefaultListeners ();
    MockHttpListener.addDefaultListener (new WebAppListener ());
    MockHttpListener.addDefaultListener (new MockServletRequestListener ());
    MockHttpListener.setToDefault ();

    AppBasicTestRule.initAppBasics (aStoragePath);
  }
}
