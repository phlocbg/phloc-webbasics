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
package com.phloc.appbasics.app.menu;

import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.phloc.appbasics.app.page.AbstractPage;
import com.phloc.appbasics.app.page.IPage;
import com.phloc.commons.filter.FilterFalse;
import com.phloc.commons.text.impl.ConstantTextProvider;
import com.phloc.commons.url.SimpleURL;
import com.phloc.scopes.mock.ScopeTestRule;

/**
 * Test class for class {@link GlobalMenuTree}
 * 
 * @author philip
 */
public final class MenuTreeTest
{
  @Rule
  public final TestRule m_aScopeRule = new ScopeTestRule ();

  @Test
  public void testBasic ()
  {
    final IPage aPage = new AbstractPage ("id1")
    {};

    final GlobalMenuTree aTree = GlobalMenuTree.getInstance ();
    final IMenuItemPage aRoot1 = aTree.createRootItem ("root1", aPage)
                                      .setDisplayFilter (FilterFalse.<IMenuObject> getInstance ());
    assertNotNull (aRoot1);
    assertNotNull (aTree.createItem (aRoot1, aPage));
    assertNotNull (aTree.createItem (aRoot1, "logout", new SimpleURL ("/logout"), new ConstantTextProvider ("Any")));
    assertNotNull (aTree.getItemWithID ("root1"));
    assertNotNull (aTree.getItemWithID ("logout"));
  }
}
