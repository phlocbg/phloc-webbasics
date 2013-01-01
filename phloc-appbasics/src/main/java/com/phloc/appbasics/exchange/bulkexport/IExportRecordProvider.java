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
package com.phloc.appbasics.exchange.bulkexport;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The main record provider.
 * 
 * @author philip
 */
public interface IExportRecordProvider
{
  /**
   * @return The optional header record. May be <code>null</code> if no header
   *         is required.
   */
  @Nullable
  IExportRecord getHeader ();

  /**
   * @return An iterator for all body records. Never <code>null</code>.
   */
  @Nonnull
  Iterator <? extends IExportRecord> getBodyRecords ();

  /**
   * @return The optional footer record. May be <code>null</code> if no footer
   *         is required.
   */
  @Nullable
  IExportRecord getFooter ();
}
