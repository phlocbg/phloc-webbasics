package com.phloc.bootstrap3.tooltip;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.phloc.html.js.builder.jquery.JQuerySelector;

/**
 * Test class for class {@link BootstrapTooltip}.
 * 
 * @author Philip Helger
 */
public final class BootstrapTooltipTest
{
  @Test
  public void testCodeGen ()
  {
    final BootstrapTooltip aBT = new BootstrapTooltip (JQuerySelector.id ("foo"));
    assertEquals ("$('#foo').tooltip({});", aBT.jsAttach ().getJSCode ());
    aBT.setAnimation (false);
    assertEquals ("$('#foo').tooltip({animation:false});", aBT.jsAttach ().getJSCode ());
    aBT.setHTML (true);
    assertEquals ("$('#foo').tooltip({animation:false,html:true});", aBT.jsAttach ().getJSCode ());
  }
}
