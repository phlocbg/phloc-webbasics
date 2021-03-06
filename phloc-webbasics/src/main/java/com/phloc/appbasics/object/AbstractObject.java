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
package com.phloc.appbasics.object;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joda.time.DateTime;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;

/**
 * Abstract base implementation of {@link IObject} that handles everything
 * except {@link #getTypeID()}.
 * 
 * @author Philip Helger
 */
public abstract class AbstractObject extends AbstractBaseObject
{
  // Status vars
  private Integer m_aHashCode;

  public AbstractObject (@Nonnull final IObject aObject)
  {
    super (aObject);
  }

  public AbstractObject (@Nonnull @Nonempty final String sID,
                         @Nullable final DateTime aCreationDT,
                         @Nullable final String sCreationUserID,
                         @Nullable final DateTime aLastModificationDT,
                         @Nullable final String sLastModificationUserID,
                         @Nullable final DateTime aDeletionDT,
                         @Nullable final String sDeletionUserID)
  {
    super (sID,
           aCreationDT,
           sCreationUserID,
           aLastModificationDT,
           sLastModificationUserID,
           aDeletionDT,
           sDeletionUserID);
  }

  @Override
  public final boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final AbstractObject rhs = (AbstractObject) o;
    return getID ().equals (rhs.getID ());
  }

  @Override
  public final int hashCode ()
  {
    Integer aObj = m_aHashCode;
    if (aObj == null)
    {
      aObj = new HashCodeGenerator (this).append (getID ()).getHashCodeObj ();
      m_aHashCode = aObj;
    }
    return aObj.intValue ();
  }
}
