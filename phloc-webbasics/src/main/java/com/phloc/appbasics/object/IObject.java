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
import org.joda.time.LocalDateTime;

import com.phloc.appbasics.datetime.IHasCreationInfo;
import com.phloc.appbasics.datetime.IHasDeletionInfo;
import com.phloc.appbasics.datetime.IHasLastModificationInfo;
import com.phloc.commons.type.ITypedObject;

/**
 * Base interface for all objects
 * 
 * @author Philip Helger
 */
public interface IObject extends ITypedObject <String>, IHasCreationInfo, IHasLastModificationInfo, IHasDeletionInfo
{
  /**
   * @return The <code>null</code>-able creation date time of the object
   */
  @Nullable
  DateTime getCreationDateTime ();

  /**
   * @return The user ID who created the object. May be <code>null</code> in
   *         case of migrated content.
   */
  @Nullable
  String getCreationUserID ();

  /**
   * @return <code>true</code> if this object is deleted, <code>false</code> if
   *         not.
   */
  boolean isDeleted ();

  /**
   * Check if the object was deleted at the specified date time. This is true,
   * if the deletion time is &le; than the specified date time.
   * 
   * @param aDT
   *        The time to check for deletion. May not be <code>null</code>.
   * @return <code>true</code> if this object was deleted, <code>false</code> if
   *         not.
   */
  boolean isDeleted (@Nonnull DateTime aDT);

  /**
   * Check if the object was deleted at the specified local date time. This is
   * true, if the deletion time is &le; than the specified local date time.
   * 
   * @param aDT
   *        The time to check for deletion. May not be <code>null</code>.
   * @return <code>true</code> if this object was deleted, <code>false</code> if
   *         not.
   */
  boolean isDeleted (@Nonnull LocalDateTime aDT);
}
