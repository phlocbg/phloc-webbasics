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
package com.phloc.appbasics.errorhandling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link ErrorList}.
 * 
 * @author Philip Helger
 */
public final class ErrorListTest
{
  @Test
  public void testBasicGlobal ()
  {
    final ErrorList aList = new ErrorList ();
    assertTrue (aList.isEmpty ());
    assertEquals (0, aList.getItemCount ());
    assertFalse (aList.hasErrorsOrWarnings ());
    assertNull (aList.getMostSevereErrorLevel ());
    assertNotNull (aList.getAllItems ());
    assertEquals (0, aList.getAllItems ().size ());
    assertNotNull (aList.getAllItemTexts ());
    assertEquals (0, aList.getAllItemTexts ().size ());
    assertNotNull (aList.getListWithoutField ());
    assertEquals (0, aList.getListWithoutField ().getItemCount ());
    assertFalse (aList.hasEntryForField ("field1"));
    assertFalse (aList.hasEntryForField (null));
    assertEquals (0, aList.getListOfField ("field1").getItemCount ());
    assertEquals (0, aList.getListOfField ("field2").getItemCount ());
    assertEquals (0, aList.getListOfFields ("field1", "field2").getItemCount ());
    assertEquals (0, aList.getListOfFieldsStartingWith ("field").getItemCount ());
    assertEquals (0, aList.getListOfFieldsRegExp ("field\\d").getItemCount ());
    assertEquals (0, aList.getAllItemTextsOfField ("field1").size ());
    assertEquals (0, aList.getAllItemTextsOfField ("field2").size ());
    assertEquals (0, aList.getAllItemTextsOfFields ("field1", "field2").size ());
    assertEquals (0, aList.getAllItemTextsOfFieldsStartingWith ("field").size ());
    assertEquals (0, aList.getAllItemTextsOfFieldsRegExp ("field\\d").size ());
    assertEquals (0, aList.getStructuredByFieldName ().size ());

    aList.addInfo ("TestInfo");
    assertFalse (aList.isEmpty ());
    assertEquals (1, aList.getItemCount ());
    assertFalse (aList.hasErrorsOrWarnings ());
    assertEquals (EFormErrorLevel.INFO, aList.getMostSevereErrorLevel ());
    assertNotNull (aList.getAllItems ());
    assertEquals (1, aList.getAllItems ().size ());
    assertNotNull (aList.getAllItemTexts ());
    assertEquals (1, aList.getAllItemTexts ().size ());
    assertNotNull (aList.getListWithoutField ());
    assertEquals (1, aList.getListWithoutField ().getItemCount ());
    assertFalse (aList.hasEntryForField ("field1"));
    assertTrue (aList.hasEntryForField (null));
    assertEquals (0, aList.getListOfField ("field1").getItemCount ());
    assertEquals (0, aList.getListOfField ("field2").getItemCount ());
    assertEquals (0, aList.getListOfFields ("field1", "field2").getItemCount ());
    assertEquals (0, aList.getListOfFieldsStartingWith ("field").getItemCount ());
    assertEquals (0, aList.getListOfFieldsRegExp ("field\\d").getItemCount ());
    assertEquals (0, aList.getAllItemTextsOfField ("field1").size ());
    assertEquals (0, aList.getAllItemTextsOfField ("field2").size ());
    assertEquals (0, aList.getAllItemTextsOfFields ("field1", "field2").size ());
    assertEquals (0, aList.getAllItemTextsOfFieldsStartingWith ("field").size ());
    assertEquals (0, aList.getAllItemTextsOfFieldsRegExp ("field\\d").size ());
    assertEquals (1, aList.getStructuredByFieldName ().size ());

    aList.addError ("TestError");
    assertFalse (aList.isEmpty ());
    assertEquals (2, aList.getItemCount ());
    assertTrue (aList.hasErrorsOrWarnings ());
    assertEquals (EFormErrorLevel.ERROR, aList.getMostSevereErrorLevel ());
    assertNotNull (aList.getAllItems ());
    assertEquals (2, aList.getAllItems ().size ());
    assertNotNull (aList.getAllItemTexts ());
    assertEquals (2, aList.getAllItemTexts ().size ());
    assertNotNull (aList.getListWithoutField ());
    assertEquals (2, aList.getListWithoutField ().getItemCount ());
    assertFalse (aList.hasEntryForField ("field1"));
    assertTrue (aList.hasEntryForField (null));
    assertEquals (0, aList.getListOfField ("field1").getItemCount ());
    assertEquals (0, aList.getListOfField ("field2").getItemCount ());
    assertEquals (0, aList.getListOfFields ("field1", "field2").getItemCount ());
    assertEquals (0, aList.getListOfFieldsStartingWith ("field").getItemCount ());
    assertEquals (0, aList.getListOfFieldsRegExp ("field\\d").getItemCount ());
    assertEquals (0, aList.getAllItemTextsOfField ("field1").size ());
    assertEquals (0, aList.getAllItemTextsOfField ("field2").size ());
    assertEquals (0, aList.getAllItemTextsOfFields ("field1", "field2").size ());
    assertEquals (0, aList.getAllItemTextsOfFieldsStartingWith ("field").size ());
    assertEquals (0, aList.getAllItemTextsOfFieldsRegExp ("field\\d").size ());
    assertEquals (1, aList.getStructuredByFieldName ().size ());

    aList.addWarning ("TestWarning");
    assertFalse (aList.isEmpty ());
    assertEquals (3, aList.getItemCount ());
    assertTrue (aList.hasErrorsOrWarnings ());
    assertEquals (EFormErrorLevel.ERROR, aList.getMostSevereErrorLevel ());
    assertNotNull (aList.getAllItems ());
    assertEquals (3, aList.getAllItems ().size ());
    assertNotNull (aList.getAllItemTexts ());
    assertEquals (3, aList.getAllItemTexts ().size ());
    assertNotNull (aList.getListWithoutField ());
    assertEquals (3, aList.getListWithoutField ().getItemCount ());
    assertFalse (aList.hasEntryForField ("field1"));
    assertTrue (aList.hasEntryForField (null));
    assertEquals (0, aList.getListOfField ("field1").getItemCount ());
    assertEquals (0, aList.getListOfField ("field2").getItemCount ());
    assertEquals (0, aList.getListOfFields ("field1", "field2").getItemCount ());
    assertEquals (0, aList.getListOfFieldsStartingWith ("field").getItemCount ());
    assertEquals (0, aList.getListOfFieldsRegExp ("field\\d").getItemCount ());
    assertEquals (0, aList.getAllItemTextsOfField ("field1").size ());
    assertEquals (0, aList.getAllItemTextsOfField ("field2").size ());
    assertEquals (0, aList.getAllItemTextsOfFields ("field1", "field2").size ());
    assertEquals (0, aList.getAllItemTextsOfFieldsStartingWith ("field").size ());
    assertEquals (0, aList.getAllItemTextsOfFieldsRegExp ("field\\d").size ());
    assertEquals (1, aList.getStructuredByFieldName ().size ());
  }

  @Test
  public void testBasicField ()
  {
    final ErrorList aList = new ErrorList ();
    assertTrue (aList.isEmpty ());
    assertEquals (0, aList.getItemCount ());
    assertFalse (aList.hasErrorsOrWarnings ());
    assertNull (aList.getMostSevereErrorLevel ());
    assertNotNull (aList.getAllItems ());
    assertEquals (0, aList.getAllItems ().size ());
    assertNotNull (aList.getAllItemTexts ());
    assertEquals (0, aList.getAllItemTexts ().size ());
    assertNotNull (aList.getListWithoutField ());
    assertEquals (0, aList.getListWithoutField ().getItemCount ());
    assertFalse (aList.hasEntryForField ("field1"));
    assertFalse (aList.hasEntryForField ("field2"));
    assertFalse (aList.hasEntryForField (null));
    assertFalse (aList.hasEntryForField ("field1", EFormErrorLevel.ERROR));
    assertFalse (aList.hasEntryForField ("field2", EFormErrorLevel.ERROR));
    assertFalse (aList.hasEntryForField (null, EFormErrorLevel.ERROR));
    assertEquals (0, aList.getListOfField ("field1").getItemCount ());
    assertEquals (0, aList.getListOfField ("field2").getItemCount ());
    assertEquals (0, aList.getListOfFields ("field1", "field2").getItemCount ());
    assertEquals (0, aList.getListOfFieldsStartingWith ("field").getItemCount ());
    assertEquals (0, aList.getListOfFieldsRegExp ("field\\d").getItemCount ());
    assertEquals (0, aList.getAllItemTextsOfField ("field1").size ());
    assertEquals (0, aList.getAllItemTextsOfField ("field2").size ());
    assertEquals (0, aList.getAllItemTextsOfFields ("field1", "field2").size ());
    assertEquals (0, aList.getAllItemTextsOfFieldsStartingWith ("field").size ());
    assertEquals (0, aList.getAllItemTextsOfFieldsRegExp ("field\\d").size ());
    assertEquals (0, aList.getStructuredByFieldName ().size ());

    aList.addInfo ("field1", "TestInfo");
    assertFalse (aList.isEmpty ());
    assertEquals (1, aList.getItemCount ());
    assertFalse (aList.hasErrorsOrWarnings ());
    assertEquals (EFormErrorLevel.INFO, aList.getMostSevereErrorLevel ());
    assertNotNull (aList.getAllItems ());
    assertEquals (1, aList.getAllItems ().size ());
    assertNotNull (aList.getAllItemTexts ());
    assertEquals (1, aList.getAllItemTexts ().size ());
    assertNotNull (aList.getListWithoutField ());
    assertEquals (0, aList.getListWithoutField ().getItemCount ());
    assertTrue (aList.hasEntryForField ("field1"));
    assertFalse (aList.hasEntryForField ("field2"));
    assertFalse (aList.hasEntryForField (null));
    assertFalse (aList.hasEntryForField ("field1", EFormErrorLevel.ERROR));
    assertFalse (aList.hasEntryForField ("field2", EFormErrorLevel.ERROR));
    assertFalse (aList.hasEntryForField (null, EFormErrorLevel.ERROR));
    assertEquals (1, aList.getListOfField ("field1").getItemCount ());
    assertEquals (0, aList.getListOfField ("field2").getItemCount ());
    assertEquals (1, aList.getListOfFields ("field1", "field2").getItemCount ());
    assertEquals (1, aList.getListOfFieldsStartingWith ("field").getItemCount ());
    assertEquals (1, aList.getListOfFieldsRegExp ("field\\d").getItemCount ());
    assertEquals (1, aList.getAllItemTextsOfField ("field1").size ());
    assertEquals (0, aList.getAllItemTextsOfField ("field2").size ());
    assertEquals (1, aList.getAllItemTextsOfFields ("field1", "field2").size ());
    assertEquals (1, aList.getAllItemTextsOfFieldsStartingWith ("field").size ());
    assertEquals (1, aList.getAllItemTextsOfFieldsRegExp ("field\\d").size ());
    assertEquals (1, aList.getStructuredByFieldName ().size ());

    aList.addError ("field2", "TestError");
    assertFalse (aList.isEmpty ());
    assertEquals (2, aList.getItemCount ());
    assertTrue (aList.hasErrorsOrWarnings ());
    assertEquals (EFormErrorLevel.ERROR, aList.getMostSevereErrorLevel ());
    assertNotNull (aList.getAllItems ());
    assertEquals (2, aList.getAllItems ().size ());
    assertNotNull (aList.getAllItemTexts ());
    assertEquals (2, aList.getAllItemTexts ().size ());
    assertNotNull (aList.getListWithoutField ());
    assertEquals (0, aList.getListWithoutField ().getItemCount ());
    assertTrue (aList.hasEntryForField ("field1"));
    assertTrue (aList.hasEntryForField ("field2"));
    assertFalse (aList.hasEntryForField (null));
    assertFalse (aList.hasEntryForField ("field1", EFormErrorLevel.ERROR));
    assertTrue (aList.hasEntryForField ("field2", EFormErrorLevel.ERROR));
    assertFalse (aList.hasEntryForField (null, EFormErrorLevel.ERROR));
    assertEquals (1, aList.getListOfField ("field1").getItemCount ());
    assertEquals (1, aList.getListOfField ("field2").getItemCount ());
    assertEquals (2, aList.getListOfFields ("field1", "field2").getItemCount ());
    assertEquals (2, aList.getListOfFieldsStartingWith ("field").getItemCount ());
    assertEquals (2, aList.getListOfFieldsRegExp ("field\\d").getItemCount ());
    assertEquals (1, aList.getAllItemTextsOfField ("field1").size ());
    assertEquals (1, aList.getAllItemTextsOfField ("field2").size ());
    assertEquals (2, aList.getAllItemTextsOfFields ("field1", "field2").size ());
    assertEquals (2, aList.getAllItemTextsOfFieldsStartingWith ("field").size ());
    assertEquals (2, aList.getAllItemTextsOfFieldsRegExp ("field\\d").size ());
    assertEquals (2, aList.getStructuredByFieldName ().size ());

    aList.addWarning ("field1", "TestWarning");
    assertFalse (aList.isEmpty ());
    assertEquals (3, aList.getItemCount ());
    assertTrue (aList.hasErrorsOrWarnings ());
    assertEquals (EFormErrorLevel.ERROR, aList.getMostSevereErrorLevel ());
    assertNotNull (aList.getAllItems ());
    assertEquals (3, aList.getAllItems ().size ());
    assertNotNull (aList.getAllItemTexts ());
    assertEquals (3, aList.getAllItemTexts ().size ());
    assertNotNull (aList.getListWithoutField ());
    assertEquals (0, aList.getListWithoutField ().getItemCount ());
    assertTrue (aList.hasEntryForField ("field1"));
    assertTrue (aList.hasEntryForField ("field2"));
    assertFalse (aList.hasEntryForField (null));
    assertFalse (aList.hasEntryForField ("field1", EFormErrorLevel.ERROR));
    assertTrue (aList.hasEntryForField ("field2", EFormErrorLevel.ERROR));
    assertFalse (aList.hasEntryForField (null, EFormErrorLevel.ERROR));
    assertEquals (2, aList.getListOfField ("field1").getItemCount ());
    assertEquals (1, aList.getListOfField ("field2").getItemCount ());
    assertEquals (3, aList.getListOfFields ("field1", "field2").getItemCount ());
    assertEquals (3, aList.getListOfFieldsStartingWith ("field").getItemCount ());
    assertEquals (3, aList.getListOfFieldsRegExp ("field\\d").getItemCount ());
    assertEquals (2, aList.getAllItemTextsOfField ("field1").size ());
    assertEquals (1, aList.getAllItemTextsOfField ("field2").size ());
    assertEquals (3, aList.getAllItemTextsOfFields ("field1", "field2").size ());
    assertEquals (3, aList.getAllItemTextsOfFieldsStartingWith ("field").size ());
    assertEquals (3, aList.getAllItemTextsOfFieldsRegExp ("field\\d").size ());
    assertEquals (2, aList.getStructuredByFieldName ().size ());
  }
}
