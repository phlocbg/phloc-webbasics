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
    aBT.setPlacement (EBootstrapTooltipPosition.BOTTOM, false);
    assertEquals ("$('#foo').tooltip({animation:false,html:true,placement:'bottom'});", aBT.jsAttach ().getJSCode ());
    aBT.setPlacement (EBootstrapTooltipPosition.BOTTOM, true);
    assertEquals ("$('#foo').tooltip({animation:false,html:true,placement:'bottom auto'});", aBT.jsAttach ()
                                                                                                .getJSCode ());
    aBT.setPlacement (EBootstrapTooltipPosition.TOP, true);
    assertEquals ("$('#foo').tooltip({animation:false,html:true,placement:'top auto'});", aBT.jsAttach ().getJSCode ());
    aBT.setPlacement (EBootstrapTooltipPosition.TOP, false);
    assertEquals ("$('#foo').tooltip({animation:false,html:true});", aBT.jsAttach ().getJSCode ());
    aBT.setTrigger (EBootstrapTooltipTrigger.HOVER);
    assertEquals ("$('#foo').tooltip({animation:false,html:true,trigger:'hover'});", aBT.jsAttach ().getJSCode ());
    aBT.setTrigger (EBootstrapTooltipTrigger.HOVER, EBootstrapTooltipTrigger.FOCUS);
    assertEquals ("$('#foo').tooltip({animation:false,html:true});", aBT.jsAttach ().getJSCode ());
    aBT.setTrigger ();
    assertEquals ("$('#foo').tooltip({animation:false,html:true});", aBT.jsAttach ().getJSCode ());
    aBT.setDelay (10);
    assertEquals ("$('#foo').tooltip({animation:false,html:true,delay:10});", aBT.jsAttach ().getJSCode ());
    aBT.setDelay (10, 20);
    assertEquals ("$('#foo').tooltip({animation:false,html:true,delay:{show:10,hide:20}});", aBT.jsAttach ()
                                                                                                .getJSCode ());
  }
}
