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
package com.phloc.appbasics.object.accarea;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.object.client.IClientObject;

/**
 * Base interface for an object that is uniquely assigned to an accounting area.
 * 
 * @author Philip Helger
 */
public interface IAccountingAreaObject extends IClientObject, IHasAccountingArea
{
  /**
   * @return The accounting area ID to which the object is assigned to. May be
   *         <code>null</code> when using
   *         {@link AbstractAccountingAreaOptionalObject}.
   * @see #getAccountingArea()
   */
  @Nullable
  String getAccountingAreaID ();

  /**
   * @return The accounting area matching the given ID. May be <code>null</code>
   *         when using {@link AbstractAccountingAreaOptionalObject}.
   */
  @Nullable
  IAccountingArea getAccountingArea ();

  /**
   * Check if the passed object has the same client ID and the same accounting
   * area ID as this object
   * 
   * @param aAccountingAreaObject
   *        The object to check. May not be <code>null</code>.
   * @return <code>true</code> if this object and the passed object have the
   *         same client ID <b>and</b> the same accounting area ID
   */
  boolean hasSameClientAndAccountingAreaID (@Nonnull IAccountingAreaObject aAccountingAreaObject);
}
