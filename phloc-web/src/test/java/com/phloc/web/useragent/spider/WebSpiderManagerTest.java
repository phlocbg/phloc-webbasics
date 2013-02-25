/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.web.useragent.spider;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.id.ComparatorHasIDString;
import com.phloc.commons.mock.AbstractPhlocTestCase;

/**
 * Test class for class {@link WebSpiderManager}.
 * 
 * @author philip
 */
public final class WebSpiderManagerTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll ()
  {
    for (final WebSpiderInfo aWSI : ContainerHelper.getSorted (WebSpiderManager.getInstance ().getAllKnownSpiders (),
                                                               new ComparatorHasIDString <WebSpiderInfo> ()))
    {
      assertNotNull (aWSI);
      assertNotNull (aWSI.getID ());
      // name and type may be null
    }
  }
}
