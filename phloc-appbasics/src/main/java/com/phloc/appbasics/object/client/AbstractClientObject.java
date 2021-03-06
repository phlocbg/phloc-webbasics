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
package com.phloc.appbasics.object.client;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.joda.time.DateTime;

import com.phloc.appbasics.object.AbstractBaseObject;
import com.phloc.appbasics.object.StubObject;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

@Immutable
public abstract class AbstractClientObject extends AbstractBaseObject implements IClientObject
{
  @Nonnull
  private final IClient m_aClient;
  private Integer m_aHashCode;

  protected AbstractClientObject (@Nonnull final IClientObject aBase)
  {
    super (aBase);
    m_aClient = aBase.getClient ();
    // Recalculate hash code, if this implementation class is different from the
    // passed implementation class
  }

  public AbstractClientObject (@Nonnull final IClient aClient, @Nonnull final StubObject aObject)
  {
    super (aObject);
    m_aClient = ValueEnforcer.notNull (aClient, "Client");
  }

  public AbstractClientObject (@Nonnull final IClient aClient,
                               @Nonnull @Nonempty final String sID,
                               @Nonnull final DateTime aCreationDT,
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
    m_aClient = ValueEnforcer.notNull (aClient, "Client");
  }

  @Nonnull
  @Nonempty
  public final String getClientID ()
  {
    return m_aClient.getID ();
  }

  @Nonnull
  public final IClient getClient ()
  {
    return m_aClient;
  }

  public final boolean hasSameClientID (@Nullable final IClientObject aClientObject)
  {
    return aClientObject != null && hasSameClientID (aClientObject.getClientID ());
  }

  public final boolean hasSameClientID (@Nullable final String sClientID)
  {
    return getClientID ().equals (sClientID);
  }

  public boolean hasSameClient (@Nullable final IClient aClient)
  {
    return m_aClient.equals (aClient);
  }

  @Override
  public final boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final AbstractClientObject rhs = (AbstractClientObject) o;
    return m_aClient.equals (rhs.m_aClient) && getID ().equals (rhs.getID ());
  }

  @Override
  public final int hashCode ()
  {
    Integer aObj = m_aHashCode;
    if (aObj == null)
    {
      aObj = new HashCodeGenerator (this).append (m_aClient).append (getID ()).getHashCodeObj ();
      m_aHashCode = aObj;
    }
    return aObj.intValue ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("client", m_aClient).toString ();
  }
}
