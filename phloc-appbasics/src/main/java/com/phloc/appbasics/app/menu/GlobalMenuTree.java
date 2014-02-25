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
package com.phloc.appbasics.app.menu;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.scopes.singleton.GlobalSingleton;

/**
 * Represents the global menu tree
 * 
 * @author Philip Helger
 */
@Deprecated
public final class GlobalMenuTree extends GlobalSingleton
{
  private final IMenuTree m_aTree = new MenuTree ();

  @UsedViaReflection
  @Deprecated
  public GlobalMenuTree ()
  {}

  @Nonnull
  public static GlobalMenuTree getInstance ()
  {
    return getGlobalSingleton (GlobalMenuTree.class);
  }

  @Nonnull
  public static IMenuTree getTree ()
  {
    return getInstance ().m_aTree;
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("tree", m_aTree).toString ();
  }
}
