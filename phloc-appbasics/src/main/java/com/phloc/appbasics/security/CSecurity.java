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
package com.phloc.appbasics.security;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.type.ObjectType;

// ESCA-JAVA0116:
/**
 * Constants for user handling
 * 
 * @author Philip Helger
 */
@Immutable
public final class CSecurity
{
  /** Bit field for undefined access. */
  public static final int UNDEFINED_ACCESS = 0x01;

  /** Bit field for granted access. */
  public static final int HAS_ACCESS = 0x02;

  /** Bit field for denied access. */
  public static final int HAS_NO_ACCESS = 0x04;

  /** Bit field for derived access (from user group). */
  public static final int INHERITED_ACCESS_FROM_USERGROUP = 0x80;

  /** Bit field for derived access (from parent object). */
  public static final int INHERITED_ACCESS_FROM_OBJECT = 0x78;

  /**
   * If no right settings are applied to a right object, does this mean that a
   * user has access, or not?
   */
  public static final boolean NO_RIGHT_SPECIFIED_MEANS_HAS_ACCESS = true;

  /**
   * If a user is not assigned to a single user group, does it mean he has
   * access?
   */
  public static final boolean USER_WITHOUT_USERGROUP_HAS_ACCESS = false;

  /**
   * This is relevant for hierarchical right objects only. If set to true,
   * rights set on parent objects will dominate rights set on parent user groups
   * for the current object
   */
  public static final boolean INHERIT_OBJECT_BEFORE_USERGROUP = true;

  /**
   * Define whether an explicit "forbidden" access right has higher precedence
   * than an explicit "allowed".
   */
  public static final boolean FORBIDDEN_HAS_HIGHER_PRECEDENCE = false;

  // Default right object facet names
  @Nonnull
  public static final String FACET_READ = "read";
  @Nonnull
  public static final String FACET_WRITE = "write";

  // Object types
  @Nonnull
  public static final ObjectType TYPE_USER = new ObjectType ("user");
  @Nonnull
  public static final ObjectType TYPE_USERGROUP = new ObjectType ("usergroup");
  @Nonnull
  public static final ObjectType TYPE_ROLE = new ObjectType ("role");

  // Default users
  @Nonnull
  public static final String USER_ADMINISTRATOR_ID = "admin";
  @Nonnull
  public static final String USER_ADMINISTRATOR_LOGIN = "Admin";
  @Nonnull
  public static final String USER_ADMINISTRATOR_EMAIL = "admin@phloc.com";
  @Nonnull
  public static final String USER_ADMINISTRATOR_NAME = "Administrator";
  @Nonnull
  public static final String USER_ADMINISTRATOR_PASSWORD = "password";

  @Nonnull
  public static final String USER_USER_ID = "user";
  @Nonnull
  public static final String USER_USER_LOGIN = "User";
  @Nonnull
  public static final String USER_USER_EMAIL = "user@phloc.com";
  @Nonnull
  public static final String USER_USER_NAME = "User";
  @Nonnull
  public static final String USER_USER_PASSWORD = "user";

  @Nonnull
  public static final String USER_GUEST_ID = "guest";
  @Nonnull
  public static final String USER_GUEST_LOGIN = "Guest";
  @Nonnull
  public static final String USER_GUEST_EMAIL = "guest@phloc.com";
  @Nonnull
  public static final String USER_GUEST_NAME = "Guest";
  @Nonnull
  public static final String USER_GUEST_PASSWORD = "guest";

  // Default roles
  @Nonnull
  public static final String ROLE_ADMINISTRATOR_ID = "radmin";
  @Nonnull
  public static final String ROLE_ADMINISTRATOR_NAME = "Administrator";
  @Nonnull
  public static final String ROLE_USER_ID = "ruser";
  @Nonnull
  public static final String ROLE_USER_NAME = "User";

  // Default user groups
  @Nonnull
  public static final String USERGROUP_ADMINISTRATORS_ID = "ugadmin";
  @Nonnull
  public static final String USERGROUP_ADMINISTRATORS_NAME = "Administrators";
  @Nonnull
  public static final String USERGROUP_USERS_ID = "uguser";
  @Nonnull
  public static final String USERGROUP_USERS_NAME = "Users";
  @Nonnull
  public static final String USERGROUP_GUESTS_ID = "ugguest";
  @Nonnull
  public static final String USERGROUP_GUESTS_NAME = "Guests";

  private CSecurity ()
  {}
}
