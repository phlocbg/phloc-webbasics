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
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.flags.FlagContainer;
import com.phloc.commons.filter.IFilter;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Implementation base class for menu items.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public abstract class AbstractMenuObject <IMPLTYPE extends AbstractMenuObject <IMPLTYPE>> extends FlagContainer implements IMenuObject
{
  private final String m_sID;
  private IFilter <IMenuObject> m_aDisplayFilter;

  public AbstractMenuObject (@Nonnull @Nonempty final String sID)
  {
    m_sID = ValueEnforcer.notEmpty (sID, "ID");
  }

  @Nonnull
  @Nonempty
  public final String getID ()
  {
    return m_sID;
  }

  @SuppressWarnings ("unchecked")
  @Nonnull
  protected final IMPLTYPE thisAsT ()
  {
    return (IMPLTYPE) this;
  }

  @Nonnull
  public final IMPLTYPE setDisplayFilter (@Nullable final IFilter <IMenuObject> aDisplayFilter)
  {
    m_aDisplayFilter = aDisplayFilter;
    return thisAsT ();
  }

  @Nullable
  public final IFilter <IMenuObject> getDisplayFilter ()
  {
    return m_aDisplayFilter;
  }

  public final boolean matchesDisplayFilter ()
  {
    return m_aDisplayFilter == null || m_aDisplayFilter.matchesFilter (this);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final AbstractMenuObject <?> rhs = (AbstractMenuObject <?>) o;
    return m_sID.equals (rhs.m_sID);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_sID).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("ID", m_sID)
                            .appendIfNotNull ("displayFilter", m_aDisplayFilter)
                            .toString ();
  }
}
