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
package com.phloc.webdemoapp.app.menu.config;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuObject;
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.appbasics.app.menu.filter.AbstractMenuObjectFilter;
import com.phloc.appbasics.app.menu.filter.MenuItemFilterUserAssignedToUserGroup;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webbasics.app.page.system.PageShowChildren;
import com.phloc.webbasics.form.FormStateManager;
import com.phloc.webbasics.mgr.MetaSystemManager;
import com.phloc.webdemoapp.app.CDemoApp;
import com.phloc.webdemoapp.app.CDemoAppSecurity;
import com.phloc.webdemoapp.page.config.PageSavedStates;
import com.phloc.webpages.DefaultMenuConfigurator;

@Immutable
public final class MenuConfig
{
  private MenuConfig ()
  {}

  public static void init (@Nonnull final IMenuTree aMenuTree)
  {
    // We need this additional indirection layer, as the pages are initialized
    // statically!
    final MenuItemFilterUserAssignedToUserGroup aFilterSuperUser = new MenuItemFilterUserAssignedToUserGroup (CDemoAppSecurity.USERGROUPID_SUPERUSER);

    // Administrator
    {
      final IMenuItemPage aAdmin = aMenuTree.createRootItem (new PageShowChildren <WebPageExecutionContext> (CDemoAppMenuConfig.MENU_ADMIN,
                                                                                                             "Administration",
                                                                                                             aMenuTree))
                                            .setDisplayFilter (aFilterSuperUser);

      DefaultMenuConfigurator.addSecurityItems (aMenuTree, aAdmin, aFilterSuperUser, CDemoApp.DEFAULT_LOCALE);
      DefaultMenuConfigurator.addMonitoringItems (aMenuTree,
                                                  aAdmin,
                                                  aFilterSuperUser,
                                                  MetaSystemManager.getAuditMgr (),
                                                  MetaSystemManager.getFailedMailQueue (),
                                                  MetaSystemManager.getSystemMigrationMgr ());
      DefaultMenuConfigurator.addSysInfoItems (aMenuTree, aAdmin, aFilterSuperUser);
      DefaultMenuConfigurator.addDataItems (aMenuTree, aAdmin, aFilterSuperUser);
      DefaultMenuConfigurator.addSettingsItems (aMenuTree,
                                                aAdmin,
                                                aFilterSuperUser,
                                                MetaSystemManager.getSMTPSettingsMgr ());
    }

    // Saved states
    final AbstractMenuObjectFilter aFilterSavedStates = new AbstractMenuObjectFilter ()
    {
      public boolean matchesFilter (final IMenuObject aValue)
      {
        // Show always after a form state was once stored
        return FormStateManager.getInstance ().containedOnceAFormState ();
      }
    };
    aMenuTree.createRootSeparator ().setDisplayFilter (aFilterSavedStates);
    aMenuTree.createRootItem (new PageSavedStates (CDemoAppMenuConfig.MENU_SAVED_STATES, "Saved objects"))
             .setDisplayFilter (aFilterSavedStates);

    // Default menu item
    aMenuTree.setDefaultMenuItemID (CDemoAppMenuConfig.MENU_ADMIN);
  }
}
