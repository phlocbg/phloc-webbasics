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
package com.phloc.webctrls.typeahead;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;

/**
 * Test class for class {@link TypeaheadDatum}.
 * 
 * @author Philip Helger
 */
public class TypeaheadDatumTest
{
  @Test
  public void testBasic ()
  {
    final TypeaheadDatum p = new TypeaheadDatum ("Value", "Token", "for", "this", "value");
    assertEquals ("Value", p.getValue ());
    assertEquals (ContainerHelper.newList ("Token", "for", "this", "value"), p.getAllTokens ());
    assertEquals ("{\"value\":\"Value\",\"tokens\":[\"Token\",\"for\",\"this\",\"value\"]}", p.getAsJson ()
                                                                                              .getAsString ());
    assertEquals ("{value:'Value',tokens:['Token','for','this','value']}", p.getAsJSObject ().getJSCode ());
  }
}
