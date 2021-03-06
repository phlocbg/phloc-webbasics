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
package com.phloc.appbasics.security.usergroup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.url.SMap;

/**
 * Test class for class {@link UserGroup}.
 * 
 * @author Philip Helger
 */
public final class UserGroupTest
{
  @Test
  public void testBasic ()
  {
    final UserGroup aUserGroup = new UserGroup ("id1", "User group 5");
    assertEquals ("id1", aUserGroup.getID ());
    assertEquals ("User group 5", aUserGroup.getName ());
  }

  @Test
  public void testMicroConversion ()
  {
    final UserGroup aUserGroup = new UserGroup ("id1", "User group 5", new SMap ("key", "value"));
    aUserGroup.assignUser ("user1");
    aUserGroup.assignUser ("user2");
    aUserGroup.assignRole ("role1");
    aUserGroup.assignRole ("role2");

    // To XML
    final IMicroElement aElement = MicroTypeConverter.convertToMicroElement (aUserGroup, "usergroup");
    assertNotNull (aElement);

    // From XML
    final UserGroup aUserGroup2 = MicroTypeConverter.convertToNative (aElement, UserGroup.class);
    assertNotNull (aUserGroup2);
    assertEquals ("id1", aUserGroup2.getID ());
    assertEquals ("User group 5", aUserGroup2.getName ());
    assertEquals (2, aUserGroup2.getAllContainedUserIDs ().size ());
    assertTrue (aUserGroup2.getAllContainedUserIDs ().contains ("user1"));
    assertTrue (aUserGroup2.getAllContainedUserIDs ().contains ("user2"));
    assertEquals (2, aUserGroup2.getAllContainedRoleIDs ().size ());
    assertTrue (aUserGroup2.getAllContainedRoleIDs ().contains ("role1"));
    assertTrue (aUserGroup2.getAllContainedRoleIDs ().contains ("role2"));
    assertEquals (1, aUserGroup2.getAttributeCount ());
    assertEquals ("value", aUserGroup2.getAttributeAsString ("key"));
  }
}
