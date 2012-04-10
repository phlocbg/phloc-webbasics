package com.phloc.webbasics.security.usergroup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.MicroTypeConverter;

/**
 * Test class for class {@link UserGroup}.
 * 
 * @author philip
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
    final UserGroup aUserGroup = new UserGroup ("id1", "User group 5");
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
    assertEquals (2, aUserGroup.getAllContainedUserIDs ().size ());
    assertTrue (aUserGroup.getAllContainedUserIDs ().contains ("user1"));
    assertTrue (aUserGroup.getAllContainedUserIDs ().contains ("user2"));
    assertEquals (2, aUserGroup.getAllContainedRoleIDs ().size ());
    assertTrue (aUserGroup.getAllContainedRoleIDs ().contains ("role1"));
    assertTrue (aUserGroup.getAllContainedRoleIDs ().contains ("role2"));
  }
}
