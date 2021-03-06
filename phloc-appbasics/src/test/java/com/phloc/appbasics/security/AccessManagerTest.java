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

import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.phloc.appbasics.mock.AppBasicTestRule;
import com.phloc.appbasics.security.role.Role;

/**
 * Test class for class {@link Role}.
 * 
 * @author Philip Helger
 */
public final class AccessManagerTest
{
  @Rule
  public final TestRule m_aRule = new AppBasicTestRule ();

  @Test
  public void testStartup ()
  {
    final AccessManager aAM = AccessManager.getInstance ();
    assertNotNull (aAM);

    // Check default stuff
    assertNotNull (aAM.getRoleOfID (CSecurity.ROLE_ADMINISTRATOR_ID));
    assertNotNull (aAM.getRoleOfID (CSecurity.ROLE_USER_ID));

    assertNotNull (aAM.getUserOfID (CSecurity.USER_ADMINISTRATOR_ID));
    assertNotNull (aAM.getUserOfID (CSecurity.USER_USER_ID));
    assertNotNull (aAM.getUserOfID (CSecurity.USER_GUEST_ID));

    assertNotNull (aAM.getUserGroupOfID (CSecurity.USERGROUP_ADMINISTRATORS_ID));
    assertNotNull (aAM.getUserGroupOfID (CSecurity.USERGROUP_USERS_ID));
    assertNotNull (aAM.getUserGroupOfID (CSecurity.USERGROUP_GUESTS_ID));
  }
}
