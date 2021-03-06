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
package com.phloc.appbasics.security.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;

import com.phloc.appbasics.security.password.GlobalPasswordSettings;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.datetime.PDTFactory;

/**
 * Test class for class {@link User}.
 * 
 * @author Philip Helger
 */
public final class UserTest
{
  @Test
  public void testBasic ()
  {
    final User aUser = new User ("id1",
                                 PDTFactory.getCurrentDateTime (),
                                 null,
                                 null,
                                 "MyName",
                                 "me@example.org",
                                 GlobalPasswordSettings.createUserDefaultPasswordHash ("ABCDEF"),
                                 "Philip",
                                 "Helger",
                                 Locale.GERMANY,
                                 PDTFactory.getCurrentDateTime (),
                                 0,
                                 0,
                                 null,
                                 false,
                                 false);
    assertEquals ("id1", aUser.getID ());
    assertEquals ("me@example.org", aUser.getEmailAddress ());
    assertNotNull (aUser.getPasswordHash ());
    assertEquals ("Philip", aUser.getFirstName ());
    assertEquals ("Helger", aUser.getLastName ());
    assertEquals (Locale.GERMANY, aUser.getDesiredLocale ());
    assertEquals (0, aUser.getLoginCount ());
    assertEquals (0, aUser.getConsecutiveFailedLoginCount ());
    assertNotNull (aUser.getCreationDateTime ());
    assertNull (aUser.getLastModificationDateTime ());
    assertNull (aUser.getDeletionDateTime ());
    assertFalse (aUser.isDeleted ());
    assertFalse (aUser.isDisabled ());
    assertTrue (aUser.isEnabled ());
  }

  @Test
  public void testBasic2 ()
  {
    final User aUser = new User ("id1",
                                 PDTFactory.getCurrentDateTime (),
                                 null,
                                 null,
                                 "MyName",
                                 null,
                                 GlobalPasswordSettings.createUserDefaultPasswordHash ("ABCDEF"),
                                 null,
                                 null,
                                 Locale.GERMANY,
                                 PDTFactory.getCurrentDateTime (),
                                 0,
                                 0,
                                 null,
                                 false,
                                 false);
    assertEquals ("id1", aUser.getID ());
    assertNull (aUser.getEmailAddress ());
    assertNotNull (aUser.getPasswordHash ());
    assertNull (aUser.getFirstName ());
    assertNull (aUser.getLastName ());
    assertEquals (Locale.GERMANY, aUser.getDesiredLocale ());
    assertEquals (0, aUser.getLoginCount ());
    assertEquals (0, aUser.getConsecutiveFailedLoginCount ());
    assertNotNull (aUser.getCreationDateTime ());
    assertNull (aUser.getLastModificationDateTime ());
    assertNull (aUser.getDeletionDateTime ());
    assertFalse (aUser.isDeleted ());
    assertFalse (aUser.isDisabled ());
    assertTrue (aUser.isEnabled ());

    // To XML
    final IMicroElement aElement = MicroTypeConverter.convertToMicroElement (aUser, "user");
    assertNotNull (aElement);

    // From XML
    final User aUser2 = MicroTypeConverter.convertToNative (aElement, User.class);
    assertNotNull (aUser2);
    assertEquals (aUser, aUser2);
  }

  @Test
  public void testMicroConversion ()
  {
    final User aUser = new User ("id1",
                                 PDTFactory.getCurrentDateTime (),
                                 null,
                                 null,
                                 "MyName",
                                 "me@example.org",
                                 GlobalPasswordSettings.createUserDefaultPasswordHash ("ABCDEF"),
                                 "Philip",
                                 "Helger",
                                 Locale.GERMANY,
                                 PDTFactory.getCurrentDateTime (),
                                 0,
                                 0,
                                 ContainerHelper.newMap ("locale", "de_DE"),
                                 false,
                                 false);

    // To XML
    final IMicroElement aElement = MicroTypeConverter.convertToMicroElement (aUser, "user");
    assertNotNull (aElement);

    // From XML
    final User aUser2 = MicroTypeConverter.convertToNative (aElement, User.class);
    assertNotNull (aUser2);
    assertEquals ("id1", aUser2.getID ());
    assertEquals ("me@example.org", aUser2.getEmailAddress ());
    assertNotNull (aUser2.getPasswordHash ());
    assertEquals ("Philip", aUser2.getFirstName ());
    assertEquals ("Helger", aUser2.getLastName ());
    assertEquals (Locale.GERMANY, aUser2.getDesiredLocale ());
    assertEquals (1, aUser2.getAttributeCount ());
    assertEquals ("de_DE", aUser2.getAttributeAsString ("locale"));
  }
}
