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
package com.phloc.webbasics.app.layout;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.hc.IHCNode;
import com.phloc.scopes.singleton.GlobalSingleton;

/**
 * This class handles the mapping of the area ID to a content provider.
 * 
 * @author Philip Helger
 */
@ThreadSafe
@Deprecated
public final class GlobalLayoutManager <LECTYPE extends LayoutExecutionContext> extends GlobalSingleton implements ILayoutManager <LECTYPE>
{
  private final LayoutManagerProxy <LECTYPE> m_aProxy = new LayoutManagerProxy <LECTYPE> ();

  @UsedViaReflection
  @Deprecated
  public GlobalLayoutManager ()
  {}

  @Nonnull
  public static <LECTYPE extends LayoutExecutionContext> GlobalLayoutManager <LECTYPE> getInstance ()
  {
    return getGlobalSingleton (GlobalLayoutManager.class);
  }

  public void registerAreaContentProvider (@Nonnull final String sAreaID,
                                           @Nonnull final ILayoutAreaContentProvider <LECTYPE> aContentProvider)
  {
    m_aProxy.registerAreaContentProvider (sAreaID, aContentProvider);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllAreaIDs ()
  {
    return m_aProxy.getAllAreaIDs ();
  }

  @Nullable
  public IHCNode getContentOfArea (@Nonnull final String sAreaID, @Nonnull final LECTYPE aLEC)
  {
    return m_aProxy.getContentOfArea (sAreaID, aLEC);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("proxy", m_aProxy).toString ();
  }
}
