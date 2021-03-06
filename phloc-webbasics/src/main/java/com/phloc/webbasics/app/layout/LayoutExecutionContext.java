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
package com.phloc.webbasics.app.layout;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.appbasics.app.IRequestManager;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.url.SimpleURL;
import com.phloc.webbasics.app.ISimpleWebExecutionContext;
import com.phloc.webbasics.app.SimpleWebExecutionContext;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * This object is instantiated per page view and contains the current request
 * scope, the display locale, the selected menu item and a set of custom
 * attributes. In addition to the base class {@link SimpleWebExecutionContext}
 * the selected menu item is added.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class LayoutExecutionContext extends SimpleWebExecutionContext implements ILayoutExecutionContext
{
  private final IMenuItemPage m_aSelectedMenuItem;

  public LayoutExecutionContext (@Nonnull final ILayoutExecutionContext aLEC)
  {
    this (aLEC, aLEC.getSelectedMenuItem ());
  }

  public LayoutExecutionContext (@Nonnull final ISimpleWebExecutionContext aSWEC,
                                 @Nonnull final IMenuItemPage aSelectedMenuItem)
  {
    super (aSWEC);
    m_aSelectedMenuItem = ValueEnforcer.notNull (aSelectedMenuItem, "SelectedMenuItem");
  }

  @Nonnull
  public IMenuItemPage getSelectedMenuItem ()
  {
    return m_aSelectedMenuItem;
  }

  @Nonnull
  @Nonempty
  public String getSelectedMenuItemID ()
  {
    return m_aSelectedMenuItem.getID ();
  }

  @Nonnull
  public SimpleURL getSelfHref ()
  {
    return getLinkToMenuItem (m_aSelectedMenuItem.getID ());
  }

  @Nonnull
  public SimpleURL getSelfHref (@Nullable final Map <String, String> aParams)
  {
    return getLinkToMenuItem (m_aSelectedMenuItem.getID (), aParams);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("selectedMenuItem", m_aSelectedMenuItem)
                            .toString ();
  }

  @Nonnull
  public static LayoutExecutionContext createForAjaxOrAction (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    final IRequestManager aRequestMgr = ApplicationRequestManager.getInstance ();
    // Get the locale from the session
    final Locale aDisplayLocale = aRequestMgr.getRequestDisplayLocale ();
    final IMenuTree aMenuTree = aRequestMgr.getMenuTree ();
    // Since no menu item is selected, use the default menu item
    return new LayoutExecutionContext (new SimpleWebExecutionContext (aRequestScope, aDisplayLocale, aMenuTree),
                                       aMenuTree.getDefaultMenuItem ());
  }
}
