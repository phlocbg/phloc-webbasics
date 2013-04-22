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
package com.phloc.webscopes.singleton;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.phloc.webscopes.mock.AbstractWebScopeAwareTestCase;

/**
 * Test class for class {@link GlobalWebSingleton}.
 * 
 * @author Philip Helger
 */
public final class GlobalWebSingletonTest extends AbstractWebScopeAwareTestCase
{
  @BeforeClass
  public static void beforeClass ()
  {
    assertEquals (0, MockGlobalWebSingleton.s_nCtorCount);
    assertEquals (0, MockGlobalWebSingleton.s_nDtorCount);
  }

  @AfterClass
  public static void afterClass ()
  {
    assertEquals (1, MockGlobalWebSingleton.s_nCtorCount);
    assertEquals (1, MockGlobalWebSingleton.s_nDtorCount);
  }

  @Test
  public void testAll ()
  {
    assertTrue (GlobalWebSingleton.getAllGlobalSingletons ().isEmpty ());
    assertFalse (GlobalWebSingleton.isGlobalSingletonInstantiated (MockGlobalWebSingleton.class));
    assertNull (GlobalWebSingleton.getGlobalSingletonIfInstantiated (MockGlobalWebSingleton.class));

    final MockGlobalWebSingleton a = MockGlobalWebSingleton.getInstance ();
    assertTrue (GlobalWebSingleton.isGlobalSingletonInstantiated (MockGlobalWebSingleton.class));
    assertSame (a, GlobalWebSingleton.getGlobalSingletonIfInstantiated (MockGlobalWebSingleton.class));
    assertNotNull (a);

    final MockGlobalWebSingleton b = MockGlobalWebSingleton.getInstance ();
    assertSame (a, b);
  }
}
