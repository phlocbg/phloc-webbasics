package com.phloc.appbasics.security.password.constraint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link PasswordConstraintList}.
 * 
 * @author Philip Helger
 */
public final class PasswordConstraintListTest
{
  @Test
  public void testBasic ()
  {
    PasswordConstraintList aPCL = new PasswordConstraintList ();
    assertFalse (aPCL.hasConstraints ());
    assertEquals (0, aPCL.getConstraintCount ());
    assertTrue (aPCL.isPasswordValid (""));
    assertTrue (aPCL.isPasswordValid ("abc"));
    assertTrue (aPCL.isPasswordValid ("123456789"));
    assertTrue (aPCL.isPasswordValid ("abcdiekatzeliefimschnee"));
    assertTrue (aPCL.isPasswordValid ("abcdiekatzelief2014nochimmerimschnee"));

    aPCL = new PasswordConstraintList (new PasswordConstraintMinLength (3));
    assertTrue (aPCL.hasConstraints ());
    assertEquals (1, aPCL.getConstraintCount ());
    assertFalse (aPCL.isPasswordValid (""));
    assertTrue (aPCL.isPasswordValid ("abc"));
    assertTrue (aPCL.isPasswordValid ("123456789"));
    assertTrue (aPCL.isPasswordValid ("abcdiekatzeliefimschnee"));
    assertTrue (aPCL.isPasswordValid ("abcdiekatzelief2014nochimmerimschnee"));

    aPCL = new PasswordConstraintList (new PasswordConstraintMinLength (3), new PasswordConstraintMaxLength (9));
    assertTrue (aPCL.hasConstraints ());
    assertEquals (2, aPCL.getConstraintCount ());
    assertFalse (aPCL.isPasswordValid (""));
    assertTrue (aPCL.isPasswordValid ("abc"));
    assertTrue (aPCL.isPasswordValid ("123456789"));
    assertFalse (aPCL.isPasswordValid ("abcdiekatzeliefimschnee"));
    assertFalse (aPCL.isPasswordValid ("abcdiekatzelief2014nochimmerimschnee"));

    aPCL = new PasswordConstraintList (new PasswordConstraintMinLength (3), new PasswordConstraintMustContainDigit (1));
    assertTrue (aPCL.hasConstraints ());
    assertEquals (2, aPCL.getConstraintCount ());
    assertFalse (aPCL.isPasswordValid (""));
    assertFalse (aPCL.isPasswordValid ("abc"));
    assertTrue (aPCL.isPasswordValid ("123456789"));
    assertFalse (aPCL.isPasswordValid ("abcdiekatzeliefimschnee"));
    assertTrue (aPCL.isPasswordValid ("abcdiekatzelief2014nochimmerimschnee"));

    aPCL = new PasswordConstraintList (new PasswordConstraintMinLength (3),
                                       new PasswordConstraintMustContainDigit (1),
                                       new PasswordConstraintMustContainLetter (1));
    PhlocTestUtils.testGetClone (aPCL);
    assertTrue (aPCL.hasConstraints ());
    assertEquals (3, aPCL.getConstraintCount ());
    assertFalse (aPCL.isPasswordValid (""));
    assertFalse (aPCL.isPasswordValid ("abc"));
    assertFalse (aPCL.isPasswordValid ("123456789"));
    assertFalse (aPCL.isPasswordValid ("abcdiekatzeliefimschnee"));
    assertTrue (aPCL.isPasswordValid ("abcdiekatzelief2014nochimmerimschnee"));
  }
}
