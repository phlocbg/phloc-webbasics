package com.phloc.webctrls.bootstrap.ext;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.appbasics.app.menu.IMenuItem;
import com.phloc.appbasics.app.menu.IMenuObject;
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webctrls.bootstrap.BootstrapBreadcrumb;

@Immutable
public final class BootstrapBreadcrumbProvider
{
  private BootstrapBreadcrumbProvider ()
  {}

  @Nonnull
  public static BootstrapBreadcrumb createBreadCrumbs (@Nonnull final IMenuTree aMenuTree,
                                                       @Nonnull final Locale aDisplayLocale)
  {
    final BootstrapBreadcrumb aBreadcrumb = new BootstrapBreadcrumb ();
    final List <IMenuItem> aItems = new ArrayList <IMenuItem> ();
    IMenuItem aCurrent = ApplicationRequestManager.getInstance ().getRequestMenuItem ();
    while (aCurrent != null)
    {
      aItems.add (0, aCurrent);
      final DefaultTreeItemWithID <String, IMenuObject> aTreeItem = aMenuTree.getItemWithID (aCurrent.getID ());
      aCurrent = aTreeItem.isRootItem () ? null : (IMenuItem) aTreeItem.getParent ().getData ();
    }

    final int nItems = aItems.size ();
    if (nItems >= 0)
    {
      for (int i = 0; i < nItems; ++i)
      {
        final IMenuItem aItem = aItems.get (i);

        // add separator?
        if (i > 0)
          aBreadcrumb.addSeparator (" | ");

        // Create link on all but the last item
        if (i < nItems - 1)
          aBreadcrumb.addLink (LinkUtils.getLinkToMenuItem (aItem.getID ()), aItem.getDisplayText (aDisplayLocale));
        else
          aBreadcrumb.addActive (aItem.getDisplayText (aDisplayLocale));
      }
    }
    return aBreadcrumb;
  }
}
