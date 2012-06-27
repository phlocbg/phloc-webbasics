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
package com.phloc.webbasics.security.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.MicroTypeConverter;

/**
 * Test class for class {@link User}.
 * 
 * @author philip
 */
public final class UserTest
{
  @Test
  public void testBasic ()
  {
    final User aUser = new User ("id1", "MyName", "me@example.org", "ABCDEF", "Philip", "Helger", Locale.GERMANY, null);
    assertEquals ("id1", aUser.getID ());
    assertEquals ("me@example.org", aUser.getEmailAddress ());
    assertEquals ("ABCDEF", aUser.getPasswordHash ());
    assertEquals ("Philip", aUser.getFirstName ());
    assertEquals ("Helger", aUser.getLastName ());
    assertEquals (Locale.GERMANY, aUser.getDesiredLocale ());
  }

  @Test
  public void testMicroConversion ()
  {
    final User aUser = new User ("id1",
                                 "MyName",
                                 "me@example.org",
                                 "ABCDEF",
                                 "Philip",
                                 "Helger",
                                 Locale.GERMANY,
                                 ContainerHelper.newMap ("locale", "de_DE"));

    // To XML
    final IMicroElement aElement = MicroTypeConverter.convertToMicroElement (aUser, "user");
    assertNotNull (aElement);

    // From XML
    final User aUser2 = MicroTypeConverter.convertToNative (aElement, User.class);
    assertNotNull (aUser2);
    assertEquals ("id1", aUser2.getID ());
    assertEquals ("me@example.org", aUser2.getEmailAddress ());
    assertEquals ("ABCDEF", aUser2.getPasswordHash ());
    assertEquals ("Philip", aUser2.getFirstName ());
    assertEquals ("Helger", aUser2.getLastName ());
    assertEquals (Locale.GERMANY, aUser2.getDesiredLocale ());
    assertEquals (1, aUser2.getCustomAttrs ().size ());
    assertEquals ("de_DE", aUser2.getCustomAttrs ().get ("locale"));
  }
}
