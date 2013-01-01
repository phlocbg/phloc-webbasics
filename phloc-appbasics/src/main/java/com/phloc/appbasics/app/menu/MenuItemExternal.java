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

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.url.ISimpleURL;

/**
 * Default implementation of the {@link IMenuItemExternal} interface.
 * 
 * @author philip
 */
@NotThreadSafe
public final class MenuItemExternal extends AbstractMenuObject <MenuItemExternal> implements IMenuItemExternal
{
  private final ISimpleURL m_aURL;
  private final IHasDisplayText m_aDisplayText;

  public MenuItemExternal (@Nonnull @Nonempty final String sItemID,
                           @Nonnull final ISimpleURL aURL,
                           @Nonnull final IHasDisplayText aDisplayText)
  {
    super (sItemID);
    if (aURL == null)
      throw new NullPointerException ("URL");
    if (aDisplayText == null)
      throw new NullPointerException ("displayText");
    m_aURL = aURL;
    m_aDisplayText = aDisplayText;
  }

  @Nonnull
  public ISimpleURL getURL ()
  {
    return m_aURL;
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
    final MenuItemExternal rhs = (MenuItemExternal) o;
    return m_aURL.equals (rhs.m_aURL);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aURL).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("URL", m_aURL).toString ();
  }
}
