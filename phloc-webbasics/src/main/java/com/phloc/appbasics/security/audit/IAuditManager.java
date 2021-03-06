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
package com.phloc.appbasics.security.audit;

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * Interface for a manager that can handle audit items.
 * 
 * @author Philip Helger
 */
public interface IAuditManager
{
  /**
   * @return The underlying auditor. Never <code>null</code>.
   */
  @Nonnull
  IAuditor getAuditor ();

  /**
   * @param nMaxItems
   *        The maximum number of items. Must be &gt; 0.
   * @return The n latest audit items. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <IAuditItem> getLastAuditItems (@Nonnegative int nMaxItems);

  /**
   * Stop taking new audits. Call this upon shutdown for correct cleanup!
   * Consecutive calls to this method have no further effect.
   */
  void stop ();
}
