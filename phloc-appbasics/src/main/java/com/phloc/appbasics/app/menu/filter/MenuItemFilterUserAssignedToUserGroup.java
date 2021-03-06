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
package com.phloc.appbasics.app.menu.filter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.menu.IMenuObject;
import com.phloc.appbasics.security.util.SecurityUtils;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This filter matches any menu item if a user is logged in and if the user is
 * assigned to the specified user group ID.
 *
 * @author Philip Helger
 */
public final class MenuItemFilterUserAssignedToUserGroup extends AbstractMenuObjectFilter
{
  private final String m_sUserGroupID;

  public MenuItemFilterUserAssignedToUserGroup (@Nonnull @Nonempty final String sUserGroupID)
  {
    m_sUserGroupID = ValueEnforcer.notEmpty (sUserGroupID, "UserGroupID");
  }

  @Nonnull
  @Nonempty
  public String getUserGroupID ()
  {
    return m_sUserGroupID;
  }

  public boolean matchesFilter (@Nullable final IMenuObject aValue)
  {
    return SecurityUtils.isCurrentUserAssignedToUserGroup (m_sUserGroupID);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("userGroupID", m_sUserGroupID).toString ();
  }
}
