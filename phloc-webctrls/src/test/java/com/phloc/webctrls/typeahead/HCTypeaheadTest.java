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

import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.html.JSHtml;
import com.phloc.html.js.builder.jquery.JQuery;

/**
 * Test class for class {@link HCTypeahead}.
 * 
 * @author Philip Helger
 */
public class HCTypeaheadTest
{
  @Test
  public void testBasic ()
  {
    final TypeaheadDatum d1 = new TypeaheadDatum ("v1", "a");
    final TypeaheadDataset d = new TypeaheadDataset ("test").setLocal (d1);
    final HCTypeahead t = new HCTypeahead (JQuery.idRef ("abc"));
    t.addDataset (d);
    assertEquals ("$('#abc').typeahead({name:'test',local:[{value:'v1',tokens:['a']}]});", t.getAsJSObject ()
                                                                                            .getJSCode ());
    t.setOnOpened (new JSAnonymousFunction (JSHtml.windowAlert ("o")));
    t.setOnAutoCompleted (new JSAnonymousFunction (JSHtml.windowAlert ("a")));
    assertEquals ("$('#abc').typeahead({name:'test',local:[{value:'v1',tokens:['a']}]})"
                  + ".on('typeahead:opened',function(){window.alert('o');})"
                  + ".on('typeahead:autocompleted',function(){window.alert('a');});", t.getAsJSObject ().getJSCode ());
  }
}
