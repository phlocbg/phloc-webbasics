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
package com.phloc.webpages.security;

import java.util.Collection;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.security.AccessManager;
import com.phloc.appbasics.security.usergroup.IUserGroup;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.name.ComparatorHasName;
import com.phloc.html.hc.html.HCOption;
import com.phloc.webbasics.form.RequestField;
import com.phloc.webctrls.custom.HCExtSelect;

/**
 * Select user groups a user should be assigned to
 * 
 * @author Philip Helger
 */
public class UserGroupForUserSelect extends HCExtSelect
{
  public UserGroupForUserSelect (@Nonnull final RequestField aRF,
                                 @Nonnull final Locale aSortLocale,
                                 @Nullable final Collection <String> aSelectedUserGroups)
  {
    super (aRF);
    setMultiple (true);

    final Collection <? extends IUserGroup> aAllUserGroups = AccessManager.getInstance ().getAllUserGroups ();
    setSize (Math.min (10, aAllUserGroups.size ()));
    for (final IUserGroup aUserGroup : ContainerHelper.getSorted (aAllUserGroups,
                                                                  new ComparatorHasName <IUserGroup> (aSortLocale)))
    {
      final HCOption aOption = addOption (aUserGroup.getID (), aUserGroup.getName ());
      if (aSelectedUserGroups != null && aSelectedUserGroups.contains (aUserGroup.getID ()))
        aOption.setSelected (true);
    }
  }
}
