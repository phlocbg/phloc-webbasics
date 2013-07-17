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
package com.phloc.appbasics.errorhandling;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Abstract base class for a list of form errors.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public abstract class AbstractFormErrorList <T extends IFormError> implements Iterable <T>
{
  protected final List <T> m_aItems = new ArrayList <T> ();

  /**
   * Add a new item.
   * 
   * @param aItem
   *        The item to be added. May not be <code>null</code>.
   * @deprecated Use {@link #addItem(IFormError)} instead
   */
  @Deprecated
  public final void add (@Nonnull final T aItem)
  {
    addItem (aItem);
  }

  /**
   * Add a new item.
   * 
   * @param aItem
   *        The item to be added. May not be <code>null</code>.
   */
  public final void addItem (@Nonnull final T aItem)
  {
    if (aItem == null)
      throw new NullPointerException ("item");
    m_aItems.add (aItem);
  }

  /**
   * @return <code>true</code> if no item is contained, <code>false</code> if at
   *         least one item is contained.
   */
  public final boolean isEmpty ()
  {
    return m_aItems.isEmpty ();
  }

  /**
   * @return The number of contained items. Always &ge; 0.
   */
  @Nonnegative
  public final int getItemCount ()
  {
    return m_aItems.size ();
  }

  public final boolean hasErrorsOrWarnings ()
  {
    for (final T aItem : m_aItems)
      if (aItem.getLevel ().isMoreOrEqualSevereThan (EFormErrorLevel.WARN))
        return true;
    return false;
  }

  @Nullable
  public final EFormErrorLevel getMostSevereErrorLevel ()
  {
    EFormErrorLevel ret = null;
    for (final T aError : m_aItems)
      if (ret == null || aError.getLevel ().isMoreSevereThan (ret))
        ret = aError.getLevel ();
    return ret;
  }

  /**
   * @return A non-<code>null</code> list of all contained texts, independent of
   *         the level.
   */
  @Nonnull
  @ReturnsMutableCopy
  public final List <String> getAllItemTexts ()
  {
    final List <String> ret = new ArrayList <String> ();
    for (final T aError : m_aItems)
      ret.add (aError.getErrorText ());
    return ret;
  }

  /**
   * @return A copy of all contained items. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public final List <T> getAllItems ()
  {
    return ContainerHelper.newList (m_aItems);
  }

  /**
   * @return An {@link Iterator} over all contained items. Never
   *         <code>null</code>.
   */
  @Nonnull
  public final Iterator <T> iterator ()
  {
    return m_aItems.iterator ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final AbstractFormErrorList <?> rhs = (AbstractFormErrorList <?>) o;
    return m_aItems.equals (rhs.m_aItems);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aItems).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("items", m_aItems).toString ();
  }
}
