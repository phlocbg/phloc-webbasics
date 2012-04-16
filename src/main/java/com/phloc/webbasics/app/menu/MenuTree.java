/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webbasics.app.menu;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.compare.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;
import com.phloc.commons.tree.withid.unique.DefaultTreeWithGlobalUniqueID;
import com.phloc.webbasics.app.page.IPage;

/**
 * Represents the menu tree
 * 
 * @author philip
 */
public final class MenuTree extends DefaultTreeWithGlobalUniqueID <String, IMenuObject>
{
  private static final MenuTree s_aInstance = new MenuTree ();
  private String m_sDefaultMenuItem;

  private MenuTree ()
  {}

  @Nonnull
  public static MenuTree getInstance ()
  {
    return s_aInstance;
  }

  private static void _createChildItem (@Nonnull final DefaultTreeItemWithID <String, IMenuObject> aParentItem,
                                        @Nonnull final IMenuObject aMenuObject)
  {
    aParentItem.createChildItem (aMenuObject.getID (), aMenuObject);
  }

  public void createRootSeparator ()
  {
    _createChildItem (getRootItem (), new MenuSeparator ());
  }

  public void createSeparator (@Nonnull final String sParentID)
  {
    _createChildItem (getItemWithID (sParentID), new MenuSeparator ());
  }

  public void createRootItem (@Nonnull final String sItemID, @Nonnull final IPage aPage)
  {
    _createChildItem (getRootItem (), new MenuItem (sItemID, aPage));
  }

  public void createRootItem (@Nonnull final IPage aPage)
  {
    createRootItem (aPage.getID (), aPage);
  }

  public void createItem (@Nonnull final String sParentID, @Nonnull final String sItemID, @Nonnull final IPage aPage)
  {
    _createChildItem (getItemWithID (sParentID), new MenuItem (sItemID, aPage));
  }

  public void createItem (@Nonnull final String sParentID, @Nonnull final IPage aPage)
  {
    createItem (sParentID, aPage.getID (), aPage);
  }

  public void setDefaultMenuItem (@Nullable final String sDefaultMenuItem)
  {
    m_sDefaultMenuItem = sDefaultMenuItem;
  }

  @Nullable
  public IMenuItem getDefaultMenuItem ()
  {
    if (m_sDefaultMenuItem != null)
    {
      final DefaultTreeItemWithID <String, IMenuObject> aDefaultMenuItem = getItemWithID (m_sDefaultMenuItem);
      if (aDefaultMenuItem != null && aDefaultMenuItem.getData () instanceof IMenuItem)
        return (IMenuItem) aDefaultMenuItem.getData ();
    }
    return null;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final MenuTree rhs = (MenuTree) o;
    return EqualsUtils.nullSafeEquals (m_sDefaultMenuItem, rhs.m_sDefaultMenuItem);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_sDefaultMenuItem).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("defaultMenuItem", m_sDefaultMenuItem).toString ();
  }
}
