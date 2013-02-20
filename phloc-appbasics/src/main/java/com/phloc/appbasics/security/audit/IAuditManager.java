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

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;

/**
 * Interface for a manager that can handle audit items.
 * 
 * @author philip
 */
public interface IAuditManager extends IAuditor
{
  /**
   * Create a new audit item.
   * 
   * @param eType
   *        The audit action type. May not be <code>null</code>.
   * @param eSuccess
   *        Was the action successful? May not be <code>null</code>.
   * @param sAction
   *        The performed action. May not be <code>null</code>.
   * @return {@link EChange}.
   */
  @Nonnull
  EChange createAuditItem (@Nonnull EAuditActionType eType, @Nonnull ESuccess eSuccess, @Nonnull String sAction);

  /**
   * @return All available audit items. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  @Deprecated
  List <IAuditItem> getAllAuditItems ();

  /**
   * @param nMaxItems
   *        The maximum number of items. Must be &gt; 0.
   * @return The n latest audit items. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <IAuditItem> getLastAuditItems (@Nonnegative int nMaxItems);
}
