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
package com.phloc.webctrls.autonumeric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;

import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.js.IJSCodeProvider;
import com.phloc.webbasics.mock.WebBasicTestRule;

public final class HCAutoNumericTest
{
  @Rule
  public final WebBasicTestRule m_aRule = new WebBasicTestRule ();

  @Test
  public void testGetJS ()
  {
    final HCAutoNumeric a = new HCAutoNumeric ();
    final String sID = a.getID ();
    final HCNodeList aNL = a.build ();
    assertEquals (2, aNL.getChildCount ());
    assertTrue (aNL.getChildAtIndex (1) instanceof HCAutoNumericJS);
    IJSCodeProvider aJS = ((HCAutoNumericJS) aNL.getChildAtIndex (1)).getOnDocumentReadyCode ();
    assertEquals ("$('#" + sID + "').autoNumeric('init',{vMin:'-999999999'});", aJS.getJSCode ());

    a.setThousandSeparator ("");
    aJS = ((HCAutoNumericJS) a.build ().getChildAtIndex (1)).getOnDocumentReadyCode ();
    assertEquals ("$('#" + sID + "').autoNumeric('init',{aSep:'',vMin:'-999999999'});", aJS.getJSCode ());

    a.setInitialValue (5);
    aJS = ((HCAutoNumericJS) a.build ().getChildAtIndex (1)).getOnDocumentReadyCode ();
    assertEquals ("$('#" + sID + "').autoNumeric('init',{aSep:'',vMin:'-999999999'}).autoNumeric('set',5);",
                  aJS.getJSCode ());
  }
}
