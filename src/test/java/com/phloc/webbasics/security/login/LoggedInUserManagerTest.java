/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webbasics.security.login;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.webbasics.AbstractStorageAwareTestCase;
import com.phloc.webbasics.security.CSecurity;

/**
 * Test class for class {@link LoggedInUserManager}.
 * 
 * @author philip
 */
public final class LoggedInUserManagerTest extends AbstractStorageAwareTestCase
{
  @Test
  public void testInit ()
  {
    LoggedInUserManager.getInstance ();
  }

  @Test
  public void testLoginLogout ()
  {
    final LoggedInUserManager aUM = LoggedInUserManager.getInstance ();
    // Check any non-present user
    assertFalse (aUM.isUserLoggedIn ("any"));
    assertEquals (ELoginResult.USER_NOT_EXISTING, aUM.loginUser ("bla@foo.com", "mypw"));
    assertNull (aUM.getCurrentUserID ());

    // Login user
    assertEquals (ELoginResult.SUCCESS,
                  aUM.loginUser (CSecurity.USER_ADMINISTRATOR_EMAIL, CSecurity.USER_ADMINISTRATOR_PASSWORD));
    assertTrue (aUM.isUserLoggedIn (CSecurity.USER_ADMINISTRATOR_ID));
    assertEquals (1, aUM.getLoggedInUserCount ());

    // Try to login another user in the same session
    assertEquals (ELoginResult.SESSION_ALREADY_HAS_USER,
                  aUM.loginUser (CSecurity.USER_USER_EMAIL, CSecurity.USER_USER_PASSWORD));
    assertTrue (aUM.isUserLoggedIn (CSecurity.USER_ADMINISTRATOR_ID));
    assertFalse (aUM.isUserLoggedIn (CSecurity.USER_USER_ID));
    assertEquals (1, aUM.getLoggedInUserCount ());

    // Check current user ID
    assertEquals (CSecurity.USER_ADMINISTRATOR_ID, aUM.getCurrentUserID ());

    // Logout non-logged in user
    assertTrue (aUM.logoutUser (CSecurity.USER_USER_ID).isUnchanged ());
    assertEquals (1, aUM.getLoggedInUserCount ());

    // Logout correct user
    assertTrue (aUM.logoutCurrentUser ().isChanged ());
    assertEquals (0, aUM.getLoggedInUserCount ());
    assertNull (aUM.getCurrentUserID ());
  }
}
