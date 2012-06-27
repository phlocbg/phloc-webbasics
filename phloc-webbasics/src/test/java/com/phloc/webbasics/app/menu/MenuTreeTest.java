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
package com.phloc.webbasics.app.menu;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.phloc.scopes.nonweb.mock.AbstractScopeAwareTestCase;
import com.phloc.webbasics.app.page.system.SystemPageNotFound;

/**
 * Test class for class {@link MenuTree}
 * 
 * @author philip
 */
public final class MenuTreeTest extends AbstractScopeAwareTestCase
{
  @Test
  public void testBasic ()
  {
    final MenuTree aTree = MenuTree.getInstance ();
    final IMenuItem aRoot1 = aTree.createRootItem ("root1", SystemPageNotFound.getInstance ())
                                  .setDisplayFilter (new AbstractMenuObjectFilter ()
                                  {
                                    public boolean matchesFilter (final IMenuObject aValue)
                                    {
                                      return false;
                                    }
                                  });
    assertNotNull (aRoot1);
    assertNotNull (aTree.createItem (aRoot1, SystemPageNotFound.getInstance ()));
    assertNotNull (aTree.getItemWithID ("root1"));
  }
}
