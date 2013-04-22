/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.webbasics.form.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link FormErrors}.
 * 
 * @author Philip Helger
 */
public final class FormErrorsTest
{
  @Test
  public void testBasic ()
  {
    final FormErrors aFEL = new FormErrors ();
    assertTrue (aFEL.isEmpty ());
    assertFalse (aFEL.hasErrorsOrWarnings ());
    assertNull (aFEL.getMostSevereErrorLevel ());

    aFEL.addFieldInfo ("f1", "info");
    assertFalse (aFEL.isEmpty ());
    assertFalse (aFEL.hasErrorsOrWarnings ());
    assertEquals (EFormErrorLevel.INFO, aFEL.getMostSevereErrorLevel ());

    aFEL.addFieldError ("f2", "error");
    assertFalse (aFEL.isEmpty ());
    assertTrue (aFEL.hasErrorsOrWarnings ());
    assertEquals (EFormErrorLevel.ERROR, aFEL.getMostSevereErrorLevel ());

    assertNotNull (aFEL.getListOfField ("f1"));
    assertFalse (aFEL.getListOfField ("f1").isEmpty ());
    assertNotNull (aFEL.getListOfField ("f1-gibtsned"));
    assertTrue (aFEL.getListOfField ("f1-gibtsned").isEmpty ());
  }
}
