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
package com.phloc.webdemoapp.app.init;

import javax.annotation.concurrent.Immutable;

import com.phloc.appbasics.security.AccessManager;
import com.phloc.appbasics.security.CSecurity;
import com.phloc.appbasics.security.role.RoleManager;
import com.phloc.appbasics.security.user.UserManager;
import com.phloc.appbasics.security.usergroup.UserGroupManager;
import com.phloc.webdemoapp.app.CDemoAppSecurity;

@Immutable
public final class DefaultSecurity
{
  private DefaultSecurity ()
  {}

  public static void init ()
  {
    // Call before accessing AccessManager!
    RoleManager.setCreateDefaults (false);
    UserManager.setCreateDefaults (false);
    UserGroupManager.setCreateDefaults (false);

    final AccessManager aAM = AccessManager.getInstance ();

    // Standard users
    if (!aAM.containsUserWithID (CSecurity.USER_ADMINISTRATOR_ID))
      aAM.createPredefinedUser (CSecurity.USER_ADMINISTRATOR_ID,
                                CSecurity.USER_ADMINISTRATOR_EMAIL,
                                CSecurity.USER_ADMINISTRATOR_EMAIL,
                                CSecurity.USER_ADMINISTRATOR_PASSWORD,
                                CSecurity.USER_ADMINISTRATOR_NAME,
                                null,
                                null,
                                null,
                                false);

    // Create all roles
    if (!aAM.containsRoleWithID (CDemoAppSecurity.ROLEID_CONFIG))
      aAM.createPredefinedRole (CDemoAppSecurity.ROLEID_CONFIG, "Config user");

    // User group Administrators
    if (!aAM.containsUserGroupWithID (CDemoAppSecurity.USERGROUPID_SUPERUSER))
    {
      aAM.createPredefinedUserGroup (CDemoAppSecurity.USERGROUPID_SUPERUSER, "Administrators");
      // Assign administrator user to UG administrators
      aAM.assignUserToUserGroup (CSecurity.USERGROUP_ADMINISTRATORS_ID, CSecurity.USER_ADMINISTRATOR_ID);
    }
    aAM.assignRoleToUserGroup (CSecurity.USERGROUP_ADMINISTRATORS_ID, CDemoAppSecurity.ROLEID_CONFIG);

    // User group Config users
    if (!aAM.containsUserGroupWithID (CDemoAppSecurity.USERGROUPID_CONFIG))
      aAM.createPredefinedUserGroup (CDemoAppSecurity.USERGROUPID_CONFIG, "Config user");
    aAM.assignRoleToUserGroup (CDemoAppSecurity.USERGROUPID_CONFIG, CDemoAppSecurity.ROLEID_CONFIG);
  }
}
