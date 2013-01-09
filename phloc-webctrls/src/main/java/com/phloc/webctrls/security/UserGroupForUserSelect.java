package com.phloc.webctrls.security;

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
 * @author philip
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
