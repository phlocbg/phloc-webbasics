package com.phloc.webbasics.security.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Test;

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
    final User aUser = new User ("id1", "me@example.org", "ABCDEF", "Philip", "Helger", Locale.GERMANY);
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
    final User aUser = new User ("id1", "me@example.org", "ABCDEF", "Philip", "Helger", Locale.GERMANY);

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
  }
}
