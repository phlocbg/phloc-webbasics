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
package com.phloc.appbasics.app;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.menu.ApplicationMenuTree;
import com.phloc.appbasics.app.menu.GlobalMenuTree;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.scopes.domain.IRequestScope;
import com.phloc.scopes.mgr.ScopeManager;
import com.phloc.scopes.singleton.GlobalSingleton;

/**
 * This class holds the per-request configuration settings.
 * <ul>
 * <li>Menu item to show</li>
 * <li>Display locale</li>
 * </ul>
 *
 * @author Philip Helger
 */
@SuppressWarnings ("deprecation")
public final class ApplicationRequestManager extends GlobalSingleton implements IRequestManager
{
  private static final class RequestManagerImpl extends AbstractRequestManager
  {
    private static final Logger s_aLogger = LoggerFactory.getLogger (RequestManagerImpl.class);
    private static final AtomicBoolean s_bIssuedWarningMenu = new AtomicBoolean (false);
    private static final AtomicBoolean s_bIssuedWarningLocale = new AtomicBoolean (false);

    @Override
    @Nonnull
    public IMenuTree getMenuTree ()
    {
      IMenuTree ret = ApplicationMenuTree.getTree ();
      // XXX hack alert :(
      if (!ret.getRootItem ().hasChildren ())
      {
        ret = GlobalMenuTree.getTree ();
        if (s_bIssuedWarningMenu.compareAndSet (false, true))
          s_aLogger.warn ("Please use the ApplicationMenuTree and not the GlobalMenuTree!");
      }
      return ret;
    }

    @Override
    @Nonnull
    protected ILocaleManager getLocaleManager ()
    {
      ILocaleManager ret = ApplicationLocaleManager.getInstance ();
      // XXX hack alert :(
      if (!ret.hasLocales ())
      {
        ret = GlobalLocaleManager.getInstance ();
        if (s_bIssuedWarningLocale.compareAndSet (false, true))
          s_aLogger.warn ("Please use the ApplicationLocaleManager and not the GlobalLocaleManager!");
      }
      return ret;
    }

    @Override
    @Nonnull
    @Nonempty
    protected String getSessionAttrMenuItem ()
    {
      return "$phloc-menuitem-" + ScopeManager.getRequestApplicationID ();
    }

    @Override
    @Nonnull
    @Nonempty
    protected String getSessionAttrLocale ()
    {
      return "$phloc-displaylocale-" + ScopeManager.getRequestApplicationID ();
    }
  }

  private final IRequestManager m_aRM = new RequestManagerImpl ();

  @Deprecated
  @UsedViaReflection
  public ApplicationRequestManager ()
  {}

  public static ApplicationRequestManager getInstance ()
  {
    return getGlobalSingleton (ApplicationRequestManager.class);
  }

  public void onRequestBegin (@Nonnull final IRequestScope aRequestScope)
  {
    m_aRM.onRequestBegin (aRequestScope);
  }

  @Nonnull
  @Nonempty
  public final String getRequestParamMenuItem ()
  {
    return m_aRM.getRequestMenuItemID ();
  }

  public final void setRequestParamMenuItem (@Nonnull @Nonempty final String sRequestParamMenuItem)
  {
    m_aRM.setRequestParamMenuItem (sRequestParamMenuItem);
  }

  @Nonnull
  @Nonempty
  public final String getRequestParamDisplayLocale ()
  {
    return m_aRM.getRequestParamDisplayLocale ();
  }

  public final void setRequestParamDisplayLocale (@Nonnull @Nonempty final String sRequestParamDisplayLocale)
  {
    m_aRM.setRequestParamDisplayLocale (sRequestParamDisplayLocale);
  }

  @Nullable
  public IMenuTree getMenuTree ()
  {
    return m_aRM.getMenuTree ();
  }

  @Nullable
  public IMenuItemPage getSessionMenuItem ()
  {
    return m_aRM.getSessionMenuItem ();
  }

  @Nonnull
  public IMenuItemPage getRequestMenuItem ()
  {
    return m_aRM.getRequestMenuItem ();
  }

  @Nonnull
  public String getRequestMenuItemID ()
  {
    return m_aRM.getRequestMenuItemID ();
  }

  @Nonnull
  public Locale getRequestDisplayLocale ()
  {
    return m_aRM.getRequestDisplayLocale ();
  }

  @Nonnull
  public Locale getRequestDisplayCountry ()
  {
    return m_aRM.getRequestDisplayCountry ();
  }
}
