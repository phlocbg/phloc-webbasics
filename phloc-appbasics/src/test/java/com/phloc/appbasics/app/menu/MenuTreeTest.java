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
package com.phloc.appbasics.app.menu;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.phloc.appbasics.app.page.AbstractBasePage;
import com.phloc.appbasics.app.page.IBasePage;
import com.phloc.commons.filter.FilterFalse;
import com.phloc.scopes.nonweb.mock.AbstractScopeAwareTestCase;

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
    final IBasePage aPage = new AbstractBasePage ("id1")
    {};

    final MenuTree aTree = MenuTree.getInstance ();
    final IMenuItem aRoot1 = aTree.createRootItem ("root1", aPage)
                                  .setDisplayFilter (FilterFalse.<IMenuObject> getInstance ());
    assertNotNull (aRoot1);
    assertNotNull (aTree.createItem (aRoot1, aPage));
    assertNotNull (aTree.getItemWithID ("root1"));
  }
}
