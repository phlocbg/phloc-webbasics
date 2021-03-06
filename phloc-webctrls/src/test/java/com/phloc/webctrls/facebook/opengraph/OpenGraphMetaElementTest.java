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
package com.phloc.webctrls.facebook.opengraph;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.phloc.html.hc.conversion.HCSettings;

/**
 * Test class for class {@link OpenGraphMetaElement}.
 * 
 * @author Philip Helger
 */
public class OpenGraphMetaElementTest
{
  @Test
  public void testBasic ()
  {
    for (final EOpenGraphObjectProperty eMeta : EOpenGraphObjectProperty.values ())
    {
      final OpenGraphMetaElement aME = new OpenGraphMetaElement (eMeta, "test123");
      assertNotNull (aME.convertToNode (HCSettings.getConversionSettings (false)));
    }
  }
}
