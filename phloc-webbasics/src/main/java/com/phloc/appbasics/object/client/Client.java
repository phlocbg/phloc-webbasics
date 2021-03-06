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
package com.phloc.appbasics.object.client;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.appbasics.object.AbstractObject;
import com.phloc.appbasics.object.CObject;
import com.phloc.appbasics.object.StubObject;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.type.ObjectType;

/**
 * Default implementation of {@link IClient}
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class Client extends AbstractObject implements IClient
{
  private String m_sDisplayName;

  /**
   * Constructor for new client
   * 
   * @param sID
   *        ID
   * @param sDisplayName
   *        display name
   */
  public Client (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sDisplayName)
  {
    this (StubObject.createForCurrentUserAndID (sID), sDisplayName);
  }

  Client (@Nonnull final StubObject aStubObject, @Nonnull @Nonempty final String sDisplayName)
  {
    super (aStubObject);
    setDisplayName (sDisplayName);
  }

  @Nonnull
  public ObjectType getTypeID ()
  {
    return CObject.OT_CLIENT;
  }

  public boolean isGlobalClient ()
  {
    return CObject.GLOBAL_CLIENT.equals (getID ());
  }

  @Nonnull
  @Nonempty
  public String getDisplayName ()
  {
    return m_sDisplayName;
  }

  @Nonnull
  public EChange setDisplayName (@Nonnull @Nonempty final String sDisplayName)
  {
    ValueEnforcer.notEmpty (sDisplayName, "DisplayName");

    if (sDisplayName.equals (m_sDisplayName))
      return EChange.UNCHANGED;
    m_sDisplayName = sDisplayName;
    return EChange.CHANGED;
  }

  @Nonnull
  @Nonempty
  public String getAsUIText (Locale aDisplayLocale)
  {
    return getID () + " - " + m_sDisplayName;
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("displayName", m_sDisplayName).toString ();
  }
}
