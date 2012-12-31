/**
 * Copyright (C) 2006-2012 phloc systems
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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.collections.ContainerHelper;

/**
 * An implementation of {@link IExportRecordProvider} that uses a constant list
 * of records.
 * 
 * @author philip
 */
@Immutable
public class ConstantExportRecordProvider implements IExportRecordProvider
{
  private final IExportRecord m_aHeader;
  private final List <IExportRecord> m_aBody;
  private final IExportRecord m_aFooter;

  public ConstantExportRecordProvider (@Nonnull final Collection <? extends IExportRecord> aBody)
  {
    this (null, aBody, null);
  }

  public ConstantExportRecordProvider (@Nullable final IExportRecord aHeader,
                                       @Nonnull final Collection <? extends IExportRecord> aBody)
  {
    this (aHeader, aBody, null);
  }

  public ConstantExportRecordProvider (@Nullable final IExportRecord aHeader,
                                       @Nonnull final Collection <? extends IExportRecord> aBody,
                                       @Nullable final IExportRecord aFooter)
  {
    if (aBody == null)
      throw new NullPointerException ("body");
    m_aHeader = aHeader;
    m_aBody = ContainerHelper.newList (aBody);
    m_aFooter = aFooter;
  }

  @Nullable
  public IExportRecord getHeader ()
  {
    return m_aHeader;
  }

  @Nonnull
  public Iterator <? extends IExportRecord> getBodyRecords ()
  {
    return m_aBody.iterator ();
  }

  @Nullable
  public IExportRecord getFooter ()
  {
    return m_aFooter;
  }
}
