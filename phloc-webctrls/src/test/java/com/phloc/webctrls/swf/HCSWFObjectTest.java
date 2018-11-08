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
package com.phloc.webctrls.swf;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.url.SimpleURL;
import com.phloc.html.hc.conversion.HCSettings;

/**
 * Test class for class {@link HCSWFObject}.
 * 
 * @author Philip Helger
 */
public final class HCSWFObjectTest
{
  @Test
  public void testCodeGen ()
  {
    final HCSWFObject x = new HCSWFObject ();
    x.setID ("div1");
    x.setSWFURL (new SimpleURL ("a.swf"));
    x.setWidth (500).setHeight (300);
    x.setRequiredSWFVersion ("9.0.0");
    x.addFlashVar ("flash1", "Wert");
    final IMicroNode aNode = x.convertToNode (HCSettings.getConversionSettings (false));
    assertNotNull (aNode);
  }
}
