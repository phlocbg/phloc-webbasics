package com.phloc.webbasics.security;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.phloc.webbasics.AbstractStorageAwareTestCase;
import com.phloc.webbasics.security.role.Role;

/**
 * Test class for class {@link Role}.
 * 
 * @author philip
 */
public final class AccessManagerTest extends AbstractStorageAwareTestCase
{
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
