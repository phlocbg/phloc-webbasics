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
package com.phloc.appbasics.security.audit;

import javax.annotation.Nonnull;

import org.joda.time.DateTime;

import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.state.ESuccess;

/**
 * Base interface for a single audit item
 * 
 * @author philip
 */
@MustImplementEqualsAndHashcode
public interface IAuditItem
{
  /**
   * @return The date and time when the audit item was created
   */
  @Nonnull
  DateTime getDateTime ();

  /**
   * @return The user who triggered the audit item.
   */
  @Nonnull
  String getUserID ();

  /**
   * @return <code>true</code> if the user ID equals {@link CAudit#GUEST_USERID}
   */
  boolean isAnonymousUser ();

  /**
   * @return The audit action type.
   */
  @Nonnull
  EAuditActionType getType ();

  /**
   * @return Success or error?
   */
  @Nonnull
  ESuccess getSuccess ();

  /**
   * @return The performed action in the syntax "action(param1,param2,...)"
   */
  @Nonnull
  String getAction ();
}
