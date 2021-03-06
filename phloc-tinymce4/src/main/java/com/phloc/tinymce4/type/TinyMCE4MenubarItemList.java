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
package com.phloc.tinymce4.type;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.ICloneable;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

public class TinyMCE4MenubarItemList implements ICloneable <TinyMCE4MenubarItemList>
{
  private final List <ETinyMCE4MenuItem> m_aList;

  /**
   * Constructor
   */
  public TinyMCE4MenubarItemList ()
  {
    m_aList = new ArrayList <ETinyMCE4MenuItem> ();
  }

  /**
   * Constructor
   *
   * @param aList
   *        Separators are denoted by <code>null</code> elements.
   */
  public TinyMCE4MenubarItemList (@Nonnull final ETinyMCE4MenuItem... aList)
  {
    ValueEnforcer.notNull (aList, "List");
    m_aList = ContainerHelper.newList (aList);
  }

  /**
   * Constructor
   *
   * @param aList
   *        May not contain <code>null</code> elements.
   */
  public TinyMCE4MenubarItemList (@Nonnull final List <ETinyMCE4MenuItem> aList)
  {
    ValueEnforcer.notNull (aList, "List");
    m_aList = ContainerHelper.newList (aList);
  }

  /**
   * Other
   *
   * @param aOther
   *        Source object to copy from. May not be <code>null</code>.
   */
  public TinyMCE4MenubarItemList (@Nonnull final TinyMCE4MenubarItemList aOther)
  {
    ValueEnforcer.notNull (aOther, "Other");
    m_aList = ContainerHelper.newList (aOther.m_aList);
  }

  /**
   * @return The default toolbar. Separators are denoted by <code>null</code>
   *         elements. See the respective theme.js file.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <ETinyMCE4MenuItem> getAllMenuItems ()
  {
    return ContainerHelper.newList (m_aList);
  }

  @Nonnull
  public TinyMCE4MenubarItemList addMenuItem (@Nonnull final ETinyMCE4MenuItem eMenuItem)
  {
    ValueEnforcer.notNull (eMenuItem, "MenuItem");
    m_aList.add (eMenuItem);
    return this;
  }

  @Nonnull
  public TinyMCE4MenubarItemList addMenuItem (@Nonnegative final int nIndex, @Nonnull final ETinyMCE4MenuItem eMenuItem)
  {
    ValueEnforcer.notNull (eMenuItem, "MenuItem");
    m_aList.add (nIndex, eMenuItem);
    return this;
  }

  @CheckForSigned
  public int getMenuItemIndex (@Nonnull final ETinyMCE4MenuItem eMenuItem)
  {
    ValueEnforcer.notNull (eMenuItem, "MenuItem");
    return m_aList.indexOf (eMenuItem);
  }

  @Nonnull
  public EChange removeMenuItem (@Nonnull final ETinyMCE4MenuItem eMenuItem)
  {
    ValueEnforcer.notNull (eMenuItem, "MenuItem");
    return EChange.valueOf (m_aList.remove (eMenuItem));
  }

  @Nonnull
  public EChange removeAtIndex (@Nonnegative final int nIndex)
  {
    return ContainerHelper.removeElementAtIndex (m_aList, nIndex);
  }

  @Nonnull
  public EChange removeAll ()
  {
    if (m_aList.isEmpty ())
      return EChange.UNCHANGED;
    m_aList.clear ();
    return EChange.CHANGED;
  }

  @Nonnull
  public String getAsOptionString ()
  {
    final StringBuilder aSB = new StringBuilder ();
    for (final ETinyMCE4MenuItem eMenuItem : m_aList)
    {
      if (aSB.length () > 0)
        aSB.append (' ');
      aSB.append (eMenuItem.getValue ());
    }
    return aSB.toString ();
  }

  @Nonnull
  public TinyMCE4MenubarItemList getClone ()
  {
    return new TinyMCE4MenubarItemList (this);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("list", m_aList).toString ();
  }
}
