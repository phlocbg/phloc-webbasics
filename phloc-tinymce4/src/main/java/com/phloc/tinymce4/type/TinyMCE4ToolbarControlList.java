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

public class TinyMCE4ToolbarControlList implements ICloneable <TinyMCE4ToolbarControlList>
{
  private final List <ETinyMCE4ToolbarControl> m_aList;

  /**
   * Constructor
   */
  public TinyMCE4ToolbarControlList ()
  {
    m_aList = new ArrayList <ETinyMCE4ToolbarControl> ();
  }

  /**
   * Constructor
   *
   * @param aList
   *        Separators are denoted by <code>null</code> elements.
   */
  public TinyMCE4ToolbarControlList (@Nonnull final ETinyMCE4ToolbarControl... aList)
  {
    ValueEnforcer.notNull (aList, "List");
    m_aList = ContainerHelper.newList (aList);
  }

  /**
   * Constructor
   *
   * @param aList
   *        Separators are denoted by <code>null</code> elements.
   */
  public TinyMCE4ToolbarControlList (@Nonnull final List <ETinyMCE4ToolbarControl> aList)
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
  public TinyMCE4ToolbarControlList (@Nonnull final TinyMCE4ToolbarControlList aOther)
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
  public List <ETinyMCE4ToolbarControl> getAllControls ()
  {
    return ContainerHelper.newList (m_aList);
  }

  @Nonnull
  public TinyMCE4ToolbarControlList addSeparator ()
  {
    m_aList.add (null);
    return this;
  }

  @Nonnull
  public TinyMCE4ToolbarControlList addSeparator (@Nonnegative final int nIndex)
  {
    m_aList.add (nIndex, null);
    return this;
  }

  @Nonnull
  public TinyMCE4ToolbarControlList addControl (@Nonnull final ETinyMCE4ToolbarControl eControl)
  {
    ValueEnforcer.notNull (eControl, "Control");
    m_aList.add (eControl);
    return this;
  }

  @Nonnull
  public TinyMCE4ToolbarControlList addControl (@Nonnegative final int nIndex,
                                                @Nonnull final ETinyMCE4ToolbarControl eControl)
  {
    ValueEnforcer.notNull (eControl, "Control");
    m_aList.add (nIndex, eControl);
    return this;
  }

  @CheckForSigned
  public int getControlIndex (@Nonnull final ETinyMCE4ToolbarControl eControl)
  {
    ValueEnforcer.notNull (eControl, "Control");
    return m_aList.indexOf (eControl);
  }

  @Nonnull
  public EChange removeControl (@Nonnull final ETinyMCE4ToolbarControl eControl)
  {
    ValueEnforcer.notNull (eControl, "Control");
    return EChange.valueOf (m_aList.remove (eControl));
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
    for (final ETinyMCE4ToolbarControl eControl : m_aList)
    {
      if (aSB.length () > 0)
        aSB.append (' ');
      if (eControl == null)
        aSB.append ('|');
      else
        aSB.append (eControl.getValue ());
    }
    return aSB.toString ();
  }

  @Nonnull
  public TinyMCE4ToolbarControlList getClone ()
  {
    return new TinyMCE4ToolbarControlList (this);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("list", m_aList).toString ();
  }
}
