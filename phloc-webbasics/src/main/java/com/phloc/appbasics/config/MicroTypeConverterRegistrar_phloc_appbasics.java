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
package com.phloc.appbasics.config;

import javax.annotation.Nonnull;

import com.phloc.appbasics.migration.SystemMigrationResult;
import com.phloc.appbasics.migration.SystemMigrationResultMicroTypeConverter;
import com.phloc.appbasics.security.role.Role;
import com.phloc.appbasics.security.role.RoleMicroTypeConverter;
import com.phloc.appbasics.security.user.User;
import com.phloc.appbasics.security.user.UserMicroTypeConverter;
import com.phloc.appbasics.security.usergroup.UserGroup;
import com.phloc.appbasics.security.usergroup.UserGroupMicroTypeConverter;
import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.microdom.convert.IMicroTypeConverterRegistrarSPI;
import com.phloc.commons.microdom.convert.IMicroTypeConverterRegistry;

/**
 * Special micro type converter for this project.
 * 
 * @author Philip Helger
 */
@IsSPIImplementation
public final class MicroTypeConverterRegistrar_phloc_appbasics implements IMicroTypeConverterRegistrarSPI
{
  public void registerMicroTypeConverter (@Nonnull final IMicroTypeConverterRegistry aRegistry)
  {
    aRegistry.registerMicroElementTypeConverter (SystemMigrationResult.class,
                                                 new SystemMigrationResultMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (Role.class, new RoleMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (User.class, new UserMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (UserGroup.class, new UserGroupMicroTypeConverter ());
  }
}
