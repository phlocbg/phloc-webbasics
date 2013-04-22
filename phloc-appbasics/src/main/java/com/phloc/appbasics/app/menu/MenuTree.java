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
package com.phloc.appbasics.app.menu;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.page.IPage;
import com.phloc.commons.callback.INonThrowingRunnableWithParameter;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.tree.withid.unique.DefaultTreeWithGlobalUniqueID;
import com.phloc.commons.url.ISimpleURL;

/**
 * Represents a single menu tree
 * 
 * @author Philip Helger
 */
public class MenuTree extends DefaultTreeWithGlobalUniqueID <String, IMenuObject> implements IMenuTree
{
  private final MenuTreeProxy m_aProxy;

  public MenuTree ()
  {
    m_aProxy = new MenuTreeProxy (this);
  }

  @Nonnull
  public IMenuSeparator createRootSeparator ()
  {
    return m_aProxy.createRootSeparator ();
  }

  @Nonnull
  public IMenuSeparator createSeparator (@Nonnull final String sParentID)
  {
    return m_aProxy.createSeparator (sParentID);
  }

  @Nonnull
  public IMenuSeparator createSeparator (@Nonnull final IMenuItem aParent)
  {
    return m_aProxy.createSeparator (aParent);
  }

  @Nonnull
  public IMenuItemPage createRootItem (@Nonnull final String sItemID, @Nonnull final IPage aPage)
  {
    return m_aProxy.createRootItem (sItemID, aPage);
  }

  @Nonnull
  public IMenuItemPage createRootItem (@Nonnull final IPage aPage)
  {
    return m_aProxy.createRootItem (aPage);
  }

  @Nonnull
  public IMenuItemPage createItem (@Nonnull final String sParentID,
                                   @Nonnull final String sItemID,
                                   @Nonnull final IPage aPage)
  {
    return m_aProxy.createItem (sParentID, sItemID, aPage);
  }

  @Nonnull
  public IMenuItemPage createItem (@Nonnull final String sParentID, @Nonnull final IPage aPage)
  {
    return m_aProxy.createItem (sParentID, aPage);
  }

  @Nonnull
  public IMenuItemPage createItem (@Nonnull final IMenuItem aParent, @Nonnull final IPage aPage)
  {
    return m_aProxy.createItem (aParent, aPage);
  }

  @Nonnull
  public IMenuItemExternal createRootItem (@Nonnull final String sItemID,
                                           @Nonnull final ISimpleURL aURL,
                                           @Nonnull final IHasDisplayText aName)
  {
    return m_aProxy.createRootItem (sItemID, aURL, aName);
  }

  @Nonnull
  public IMenuItemExternal createItem (@Nonnull final IMenuItem aParent,
                                       @Nonnull final String sItemID,
                                       @Nonnull final ISimpleURL aURL,
                                       @Nonnull final IHasDisplayText aName)
  {
    return m_aProxy.createItem (aParent, sItemID, aURL, aName);
  }

  @Nonnull
  public IMenuItemExternal createItem (@Nonnull final String sParentID,
                                       @Nonnull final String sItemID,
                                       @Nonnull final ISimpleURL aURL,
                                       @Nonnull final IHasDisplayText aName)
  {
    return m_aProxy.createItem (sParentID, sItemID, aURL, aName);
  }

  public void setDefaultMenuItemID (@Nullable final String sDefaultMenuItem)
  {
    m_aProxy.setDefaultMenuItemID (sDefaultMenuItem);
  }

  @Nullable
  public String getDefaultMenuItemID ()
  {
    return m_aProxy.getDefaultMenuItemID ();
  }

  @Nullable
  public IMenuItem getDefaultMenuItem ()
  {
    return m_aProxy.getDefaultMenuItem ();
  }

  @Nullable
  public IMenuObject getMenuObjectOfID (@Nullable final String sID)
  {
    return m_aProxy.getMenuObjectOfID (sID);
  }

  public void iterateAllMenuObjects (@Nonnull final INonThrowingRunnableWithParameter <IMenuObject> aCallback)
  {
    m_aProxy.iterateAllMenuObjects (aCallback);
  }

  @Override
  public boolean equals (final Object o)
  {
    return super.equals (o);
  }

  @Override
  public int hashCode ()
  {
    return super.hashCode ();
  }
}
