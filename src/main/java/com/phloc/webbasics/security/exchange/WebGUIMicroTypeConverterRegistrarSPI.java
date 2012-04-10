package com.phloc.webbasics.security.exchange;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.microdom.convert.IMicroTypeConverterRegistrarSPI;
import com.phloc.commons.microdom.convert.IMicroTypeConverterRegistry;
import com.phloc.webbasics.security.role.Role;
import com.phloc.webbasics.security.role.RoleMicroTypeConverter;
import com.phloc.webbasics.security.user.User;
import com.phloc.webbasics.security.user.UserMicroTypeConverter;
import com.phloc.webbasics.security.usergroup.UserGroup;
import com.phloc.webbasics.security.usergroup.UserGroupMicroTypeConverter;

@IsSPIImplementation
public final class WebGUIMicroTypeConverterRegistrarSPI implements IMicroTypeConverterRegistrarSPI
{
  public void registerMicroTypeConverter (@Nonnull final IMicroTypeConverterRegistry aRegistry)
  {
    aRegistry.registerMicroElementTypeConverter (User.class, new UserMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (UserGroup.class, new UserGroupMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (Role.class, new RoleMicroTypeConverter ());
  }
}
