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
package com.phloc.appbasics.app;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.menu.ApplicationMenuTree;
import com.phloc.appbasics.app.menu.GlobalMenuTree;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.scopes.nonweb.domain.IRequestScope;
import com.phloc.scopes.nonweb.mgr.ScopeManager;
import com.phloc.scopes.nonweb.singleton.GlobalSingleton;

/**
 * This class holds the per-request configuration settings.
 * <ul>
 * <li>Menu item to show</li>
 * <li>Display locale</li>
 * </ul>
 * 
 * @author philip
 */
public final class ApplicationRequestManager extends GlobalSingleton implements IRequestManager
{
  private static final class RM extends AbstractRequestManager
  {
    @Override
    @Nonnull
    protected IMenuTree getMenuTree ()
    {
      IMenuTree ret = ApplicationMenuTree.getInstance ();
      // XXX hack alert :(
      if (!ret.getRootItem ().hasChildren ())
        ret = GlobalMenuTree.getInstance ();
      return ret;
    }

    @Override
    @Nonnull
    protected ILocaleManager getLocaleManager ()
    {
      ILocaleManager ret = ApplicationLocaleManager.getInstance ();
      // XXX hack alert :(
      if (!ret.hasLocales ())
        ret = GlobalLocaleManager.getInstance ();
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

  private final IRequestManager m_aRM = new RM ();

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

  @Nullable
  public IMenuItemPage getSessionMenuItem ()
  {
    return m_aRM.getSessionMenuItem ();
  }

  @Nullable
  public IMenuItemPage getDefaultMenuItem ()
  {
    return m_aRM.getDefaultMenuItem ();
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
