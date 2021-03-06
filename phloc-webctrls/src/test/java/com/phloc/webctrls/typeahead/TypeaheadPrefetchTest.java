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

import com.phloc.commons.url.SimpleURL;
import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.html.JSHtml;

/**
 * Test class for class {@link TypeaheadPrefetch}.
 * 
 * @author Philip Helger
 */
public class TypeaheadPrefetchTest
{
  @Test
  public void testBasic ()
  {
    final TypeaheadPrefetch p = new TypeaheadPrefetch (new SimpleURL ("/a.json"));
    assertEquals ("{url:'\\/a.json'}", p.getAsJSObject ().getJSCode ());
    p.setTTL (3600);
    assertEquals ("{url:'\\/a.json',ttl:3600}", p.getAsJSObject ().getJSCode ());
    p.setFilter (new JSAnonymousFunction (JSHtml.windowAlert ("x")));
    assertEquals ("{url:'\\/a.json',ttl:3600,filter:function(){window.alert('x');}}", p.getAsJSObject ().getJSCode ());
    p.setTTL (TypeaheadPrefetch.DEFAULT_TTL);
    assertEquals ("{url:'\\/a.json',filter:function(){window.alert('x');}}", p.getAsJSObject ().getJSCode ());
  }
}
