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
package com.phloc.appbasics.exchange.bulkexport;

import java.io.OutputStream;

import javax.annotation.Nonnull;
import javax.annotation.WillClose;

import com.phloc.commons.state.ESuccess;

/**
 * Generic interface for exporting records to an OutputStream.
 * 
 * @author Philip Helger
 */
public interface IExporter
{
  /**
   * Export all provided records to an output stream.
   * 
   * @param aProvider
   *        The provider for all records. May not be <code>null</code>.
   * @param aOS
   *        The output stream to write to. Will be closed automatically in any
   *        case. May not be <code>null</code>.
   * @return {@link ESuccess#SUCCESS} if at lease one record was provided and
   *         saving was successful.
   */
  @Nonnull
  ESuccess exportRecords (@Nonnull IExportRecordProvider aProvider, @Nonnull @WillClose OutputStream aOS);
}
