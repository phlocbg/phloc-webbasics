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
package com.phloc.webbasics.app.menu.ui;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.collections.NonBlockingStack;
import com.phloc.commons.tree.utils.walk.TreeWalkerDynamic;
import com.phloc.html.hc.html.HCUL;
import com.phloc.webbasics.app.menu.MenuTree;

public class MenuRenderer
{
  private final Locale m_aContentLocale;

  public MenuRenderer (@Nonnull final Locale aContentLocale)
  {
    if (aContentLocale == null)
      throw new NullPointerException ("contentLocale");
    m_aContentLocale = aContentLocale;
  }

  @Nonnull
  public HCUL createMenuHCNode ()
  {
    final NonBlockingStack <HCUL> aNodeStack = new NonBlockingStack <HCUL> (new HCUL ());
    TreeWalkerDynamic.walkTree (MenuTree.getInstance (), new MenuRendererCallback (aNodeStack, m_aContentLocale));
    if (aNodeStack.size () != 1)
      throw new IllegalStateException ("Stack is inconsistent: " + aNodeStack);

    // Return the remaining UL
    return aNodeStack.pop ();
  }
}
