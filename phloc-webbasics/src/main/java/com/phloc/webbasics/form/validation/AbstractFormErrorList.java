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
package com.phloc.webbasics.form.validation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Abstract base class for form error lists
 * 
 * @author philip
 */
@NotThreadSafe
public abstract class AbstractFormErrorList <T extends IFormError>
{
  protected final List <T> m_aItems = new ArrayList <T> ();

  public final void add (@Nonnull final T aError)
  {
    if (aError == null)
      throw new NullPointerException ("error");
    m_aItems.add (aError);
  }

  public final boolean isEmpty ()
  {
    return m_aItems.isEmpty ();
  }

  public final boolean hasErrorsOrWarnings ()
  {
    // Check per-field items
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
      if ((ret == null) || (aError.getLevel ().isMoreSevereThan (ret)))
        ret = aError.getLevel ();
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <String> getAllItemTexts ()
  {
    final List <String> ret = new ArrayList <String> ();
    for (final T aError : m_aItems)
      ret.add (aError.getErrorText ());
    return ret;
  }

  @Nonnull
  @ReturnsImmutableObject
  public final List <T> getAllItems ()
  {
    return ContainerHelper.makeUnmodifiable (m_aItems);
  }

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
