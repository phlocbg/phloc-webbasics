/**
 * Copyright (C) 2006-2015 phloc systems
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
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of the {@link IMenuItemPage} interface.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class MenuItemPage extends AbstractMenuObject <MenuItemPage> implements IMenuItemPage
{
  private final IPage m_aPage;
  private final IHasDisplayText m_aDisplayText;

  public MenuItemPage (@Nonnull @Nonempty final String sItemID, @Nonnull final IPage aPage)
  {
    this (sItemID, aPage, aPage);
  }

  public MenuItemPage (@Nonnull @Nonempty final String sItemID,
                       @Nonnull final IPage aPage,
                       @Nonnull final IHasDisplayText aDisplayText)
  {
    super (sItemID);
    m_aPage = ValueEnforcer.notNull (aPage, "Page");
    m_aDisplayText = ValueEnforcer.notNull (aDisplayText, "DisplayText");
  }

  @Nonnull
  public IPage getPage ()
  {
    return m_aPage;
  }

  @Nullable
  public String getDisplayText (@Nonnull final Locale aDisplayLocale)
  {
    return m_aDisplayText.getDisplayText (aDisplayLocale);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final MenuItemPage rhs = (MenuItemPage) o;
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
