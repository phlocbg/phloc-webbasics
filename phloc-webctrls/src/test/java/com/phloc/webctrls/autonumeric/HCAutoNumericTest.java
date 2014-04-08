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
