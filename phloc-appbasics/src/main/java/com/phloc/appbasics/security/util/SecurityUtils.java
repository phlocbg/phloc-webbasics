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
package com.phloc.appbasics.security.util;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.appbasics.security.AccessManager;
import com.phloc.appbasics.security.login.LoggedInUserManager;
import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * Security utility methods
 * 
 * @author philip
 */
@Immutable
public final class SecurityUtils
{
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final SecurityUtils s_aInstance = new SecurityUtils ();

  private SecurityUtils ()
  {}

  public static boolean isCurrentUserAssignedToUserGroup (@Nullable final String sUserGroupID)
  {
    final String sUserID = LoggedInUserManager.getInstance ().getCurrentUserID ();
    if (sUserID == null)
    {
      // No user logged in
      return false;
    }
    return AccessManager.getInstance ().isUserAssignedToUserGroup (sUserGroupID, sUserID);
  }
}
