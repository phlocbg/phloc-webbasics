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
package com.phloc.webpages.security;

import java.util.Collection;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.security.AccessManager;
import com.phloc.appbasics.security.role.IRole;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.name.ComparatorHasName;
import com.phloc.html.hc.html.HCOption;
import com.phloc.webbasics.form.RequestField;
import com.phloc.webctrls.custom.HCExtSelect;

/**
 * Select roles a user group should be assigned to
 * 
 * @author Philip Helger
 */
public class RoleForUserGroupSelect extends HCExtSelect
{
  public RoleForUserGroupSelect (@Nonnull final RequestField aRF,
                                 @Nonnull final Locale aSortLocale,
                                 @Nullable final Collection <String> aSelectedRoles)
  {
    super (aRF);
    setMultiple (true);

    final Collection <? extends IRole> aAllRoles = AccessManager.getInstance ().getAllRoles ();
    setSize (Math.min (10, aAllRoles.size ()));
    for (final IRole aRole : ContainerHelper.getSorted (aAllRoles, new ComparatorHasName <IRole> (aSortLocale)))
    {
      final HCOption aOption = addOption (aRole.getID (), aRole.getName ());
      if (aSelectedRoles != null && aSelectedRoles.contains (aRole.getID ()))
        aOption.setSelected (true);
    }
  }
}
