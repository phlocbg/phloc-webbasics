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
package com.phloc.appbasics.object.client;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.object.IObject;
import com.phloc.commons.annotations.Nonempty;

/**
 * Base interface for an object that is uniquely assigned to a client.
 * 
 * @author Philip Helger
 */
public interface IClientObject extends IObject, IHasClient
{
  /**
   * @return The client ID to which the object is assigned to. May neither be
   *         <code>null</code> nor empty.
   * @see #getClient()
   */
  @Nonnull
  @Nonempty
  String getClientID ();

  /**
   * @return The client to which the object is assigned to. May not be
   *         <code>null</code>.
   */
  @Nonnull
  IClient getClient ();

  /**
   * Check if the passed object has the same client ID as this object
   * 
   * @param aClientObject
   *        The object to check. May not be <code>null</code>.
   * @return <code>true</code> if this object and the passed object have the
   *         same client ID
   */
  boolean hasSameClientID (@Nonnull IClientObject aClientObject);

  /**
   * Check if the passed client ID has the same ID as this object
   * 
   * @param sClientID
   *        The client ID to check. May be <code>null</code>.
   * @return <code>true</code> if this object and the passed object have the
   *         same client ID
   */
  boolean hasSameClientID (@Nullable String sClientID);

  /**
   * Check if the passed client has the same ID as this object
   * 
   * @param aClient
   *        The client ID to check. May be <code>null</code>.
   * @return <code>true</code> if this object and the passed object have the
   *         same client ID
   */
  boolean hasSameClient (@Nullable IClient aClient);
}
