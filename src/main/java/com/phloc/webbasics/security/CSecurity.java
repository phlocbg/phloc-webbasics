package com.phloc.webbasics.security;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.messagedigest.EMessageDigestAlgorithm;

/**
 * Constants for user handling
 * 
 * @author philip
 */
@Immutable
public final class CSecurity
{
  /** Hashing algorithm to use for user passwords */
  public static final EMessageDigestAlgorithm USER_PASSWORD_ALGO = EMessageDigestAlgorithm.SHA_512;

  // Default users
  public static final String USER_ADMINISTRATOR_ID = "admin";
  public static final String USER_ADMINISTRATOR_EMAIL = "admin@phloc.com";
  public static final String USER_ADMINISTRATOR_NAME = "Administrator";
  public static final String USER_ADMINISTRATOR_PASSWORD = "password";

  public static final String USER_USER_ID = "user";
  public static final String USER_USER_EMAIL = "user@phloc.com";
  public static final String USER_USER_NAME = "User";
  public static final String USER_USER_PASSWORD = "user";

  public static final String USER_GUEST_ID = "guest";
  public static final String USER_GUEST_EMAIL = "guest@phloc.com";
  public static final String USER_GUEST_NAME = "Guest";
  public static final String USER_GUEST_PASSWORD = "guest";

  // Default roles
  public static final String ROLE_ADMINISTRATOR_ID = "radmin";
  public static final String ROLE_ADMINISTRATOR_NAME = "Administrator";
  public static final String ROLE_USER_ID = "ruser";
  public static final String ROLE_USER_NAME = "User";

  // Default user groups
  public static final String USERGROUP_ADMINISTRATORS_ID = "ugadmin";
  public static final String USERGROUP_ADMINISTRATORS_NAME = "Administrators";
  public static final String USERGROUP_USERS_ID = "uguser";
  public static final String USERGROUP_USERS_NAME = "Users";
  public static final String USERGROUP_GUESTS_ID = "ugguest";
  public static final String USERGROUP_GUESTS_NAME = "Guests";

  private CSecurity ()
  {}
}
