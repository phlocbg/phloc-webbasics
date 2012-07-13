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

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.appbasics.app.page.IPage;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.filter.IFilter;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of the {@link IMenuItem} interface.
 * 
 * @author philip
 */
@NotThreadSafe
public final class MenuItem extends AbstractMenuObject implements IMenuItem
{
  private final IPage m_aPage;

  public MenuItem (@Nonnull @Nonempty final String sItemID, @Nonnull final IPage aPage)
  {
    super (sItemID);
    if (aPage == null)
      throw new NullPointerException ("page");
    m_aPage = aPage;
  }

  @Nonnull
  public MenuItem setDisplayFilter (@Nullable final IFilter <IMenuObject> aDisplayFilter)
  {
    m_aDisplayFilter = aDisplayFilter;
    return this;
  }

  @Nonnull
  public IPage getPage ()
  {
    return m_aPage;
  }

  @Nullable
  public String getDisplayText (final Locale aDisplayLocale)
  {
    return m_aPage.getDisplayText (aDisplayLocale);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final MenuItem rhs = (MenuItem) o;
    return m_aPage.getID ().equals (rhs.m_aPage.getID ());
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aPage).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("page", m_aPage).toString ();
  }
}
