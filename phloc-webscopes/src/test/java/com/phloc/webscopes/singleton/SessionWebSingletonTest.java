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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.webscopes.mock.AbstractWebScopeAwareTestCase;

/**
 * Test class for class {@link SessionWebSingleton}.<br>
 * Note: must reside here for Mock* stuff!
 * 
 * @author Philip Helger
 */
public final class SessionWebSingletonTest extends AbstractWebScopeAwareTestCase
{
  @Test
  public void testSerialize () throws Exception
  {
    assertTrue (SessionWebSingleton.getAllSessionSingletons ().isEmpty ());
    assertFalse (SessionWebSingleton.isSessionSingletonInstantiated (MockSessionWebSingleton.class));
    assertNull (SessionWebSingleton.getSessionSingletonIfInstantiated (MockSessionWebSingleton.class));

    final MockSessionWebSingleton a = MockSessionWebSingleton.getInstance ();
    assertTrue (SessionWebSingleton.isSessionSingletonInstantiated (MockSessionWebSingleton.class));
    assertSame (a, SessionWebSingleton.getSessionSingletonIfInstantiated (MockSessionWebSingleton.class));
    assertEquals (0, a.get ());
    a.inc ();
    assertEquals (1, a.get ());

    final MockSessionWebSingleton b = MockSessionWebSingleton.getInstance ();
    assertSame (a, b);

    PhlocTestUtils.testDefaultSerialization (a);
  }
}
