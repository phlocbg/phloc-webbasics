/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webbasics.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.webbasics.web.IRequestParamMap;
import com.phloc.webbasics.web.RequestParamMap;

public final class RequestParamMapTest
{
  @Test
  public void test ()
  {
    final Map <String, Object> aTestMap = new HashMap <String, Object> ();
    aTestMap.put ("a", "...");
    aTestMap.put ("page_name[de]", "deutscher name");
    aTestMap.put ("a", "...");
    aTestMap.put ("page_name[en]", "english name");
    aTestMap.put ("a", "...");
    aTestMap.put ("b", "...");
    aTestMap.put ("page_name[de]", "deutscher name");
    aTestMap.put ("c", "...");
    assertEquals (5, aTestMap.size ());

    final IRequestParamMap aMap = RequestParamMap.create (new MapBasedAttributeContainer (aTestMap));
    assertEquals (4, aMap.size ());

    // get page_name[de] and page_name[en]
    final IRequestParamMap aNames = aMap.getMap ("page_name");
    assertEquals (2, aNames.size ());
    final Map <String, String> aValueMap = aNames.getAsValueMap ();
    assertEquals (2, aValueMap.size ());
    assertEquals ("deutscher name", aValueMap.get ("de"));
    assertEquals ("english name", aValueMap.get ("en"));

    // non-existing key
    assertFalse (aMap.contains ("xxx"));
    assertNull (aMap.getMap ("xxx"));
    assertNull (aMap.getString ("xxx"));

    // nested non-existing key
    assertFalse (aMap.contains ("xxx", "yyy"));
    assertNull (aMap.getMap ("xxx", "yyy"));
    assertNull (aMap.getString ("xxx", "yyy"));

    // non-existing nested key
    assertFalse (aMap.contains ("pagename", "yyy"));
    assertNull (aMap.getMap ("pagename", "yyy"));
    assertNull (aMap.getString ("pagename", "yyy"));

    // getting nested key of a non-map
    assertFalse (aMap.contains ("a", "yyy"));
    assertNull (aMap.getMap ("a", "yyy"));
    assertNull (aMap.getString ("a", "yyy"));
  }
}
