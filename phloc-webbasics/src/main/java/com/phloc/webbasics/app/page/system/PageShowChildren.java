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
package com.phloc.webbasics.app.page.system;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.menu.IMenuItem;
import com.phloc.appbasics.app.menu.IMenuItemExternal;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuObject;
import com.phloc.appbasics.app.menu.IMenuSeparator;
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.NonBlockingStack;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.tree.utils.walk.TreeWalker;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCUL;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.app.page.AbstractWebPage;
import com.phloc.webbasics.app.page.IWebPageExecutionContext;

public class PageShowChildren <WPECTYPE extends IWebPageExecutionContext> extends AbstractWebPage <WPECTYPE>
{
  private static final class ShowChildrenCallback <WPECTYPE extends IWebPageExecutionContext> extends DefaultHierarchyWalkerCallback <DefaultTreeItemWithID <String, IMenuObject>>
  {
    private final WPECTYPE m_aWPEC;
    private final NonBlockingStack <HCUL> m_aStack;
    private final PageShowChildrenRenderer m_aRenderer;

    ShowChildrenCallback (@Nonnull final HCUL aUL,
                          @Nonnull final WPECTYPE aWPEC,
                          @Nonnull final PageShowChildrenRenderer aRenderer)
    {
      ValueEnforcer.notNull (aUL, "UL");
      ValueEnforcer.notNull (aWPEC, "WPEC");
      ValueEnforcer.notNull (aRenderer, "Renderer");
      m_aWPEC = aWPEC;
      m_aStack = new NonBlockingStack <HCUL> (aUL);
      m_aRenderer = aRenderer;
    }

    @Override
    public void onLevelDown ()
    {
      super.onLevelDown ();
      m_aStack.push (new HCUL ());
    }

    @Override
    public void onLevelUp ()
    {
      final HCUL aLastCreated = m_aStack.pop ();
      if (aLastCreated.hasChildren ())
      {
        // There were some LIs created
        final HCUL aParent = m_aStack.peek ();
        if (aParent.hasChildren ())
          aParent.getLastItem ().addChild (aLastCreated);
        else
        {
          // Replace top element with last created element
          m_aStack.pop ();
          m_aStack.push (aLastCreated);
        }
      }
      super.onLevelUp ();
    }

    @Override
    public void onItemBeforeChildren (@Nullable final DefaultTreeItemWithID <String, IMenuObject> aTreeItem)
    {
      final IMenuObject aMenuObj = aTreeItem == null ? null : aTreeItem.getData ();
      if (aMenuObj != null)
      {
        IHCNode aNode;
        if (aMenuObj instanceof IMenuSeparator)
          aNode = m_aRenderer.renderMenuSeparator (m_aWPEC, (IMenuSeparator) aMenuObj);
        else
          if (aMenuObj instanceof IMenuItemPage)
            aNode = m_aRenderer.renderMenuItemPage (m_aWPEC, (IMenuItemPage) aMenuObj);
          else
            if (aMenuObj instanceof IMenuItemExternal)
              aNode = m_aRenderer.renderMenuItemExternal (m_aWPEC, (IMenuItemExternal) aMenuObj);
            else
              throw new IllegalStateException ("Unsupported menu object type: " + aMenuObj);

        if (aNode != null)
          m_aStack.peek ().addItem (aNode);
      }
    }
  }

  private final IMenuTree m_aMenuTree;
  private final PageShowChildrenRenderer m_aRenderer;

  public PageShowChildren (@Nonnull @Nonempty final String sID,
                           @Nonnull final IReadonlyMultiLingualText aName,
                           @Nonnull final IMenuTree aMenuTree)
  {
    this (sID, aName, aMenuTree, new PageShowChildrenRenderer ());
  }

  public PageShowChildren (@Nonnull @Nonempty final String sID,
                           @Nonnull final IReadonlyMultiLingualText aName,
                           @Nonnull final IMenuTree aMenuTree,
                           @Nonnull final PageShowChildrenRenderer aRenderer)
  {
    super (sID, aName);
    m_aMenuTree = ValueEnforcer.notNull (aMenuTree, "MenuTree");
    m_aRenderer = ValueEnforcer.notNull (aRenderer, "Renderer");
  }

  public PageShowChildren (@Nonnull @Nonempty final String sID,
                           @Nonnull final String sName,
                           @Nonnull final IMenuTree aMenuTree)
  {
    this (sID, sName, aMenuTree, new PageShowChildrenRenderer ());
  }

  public PageShowChildren (@Nonnull @Nonempty final String sID,
                           @Nonnull final String sName,
                           @Nonnull final IMenuTree aMenuTree,
                           @Nonnull final PageShowChildrenRenderer aRenderer)
  {
    super (sID, sName);
    m_aMenuTree = ValueEnforcer.notNull (aMenuTree, "MenuTree");
    m_aRenderer = ValueEnforcer.notNull (aRenderer, "Renderer");
  }

  @Nonnull
  @OverrideOnDemand
  protected HCUL createRootUL ()
  {
    return new HCUL ();
  }

  @Override
  protected void fillContent (@Nonnull final WPECTYPE aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();

    final DefaultTreeItemWithID <String, IMenuObject> aMenuTreeItem = m_aMenuTree.getItemWithID (getID ());
    if (aMenuTreeItem != null && aMenuTreeItem.getData () instanceof IMenuItem)
    {
      final HCUL aUL = createRootUL ();
      TreeWalker.walkSubTree (aMenuTreeItem, new ShowChildrenCallback <WPECTYPE> (aUL, aWPEC, m_aRenderer));
      aNodeList.addChild (aUL);
    }
  }
}
