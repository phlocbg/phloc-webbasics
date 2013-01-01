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
package com.phloc.appbasics.app.dao.xml;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.appbasics.app.dao.IDAOIO;
import com.phloc.appbasics.app.dao.impl.DefaultDAO;
import com.phloc.appbasics.app.io.ConstantHasFilename;
import com.phloc.appbasics.app.io.IHasFilename;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.state.EChange;

/**
 * This class represents an abstract version of a common DAO. This kind of DAO
 * is based upon an XML file that is read on startup.
 * 
 * @author philip
 */
@ThreadSafe
public abstract class AbstractXMLDAO extends DefaultDAO
{
  public AbstractXMLDAO (@Nullable final String sFilename)
  {
    this (new ConstantHasFilename (sFilename));
  }

  public AbstractXMLDAO (@Nonnull final IHasFilename aFilenameProvider)
  {
    this (aFilenameProvider, DEFAULT_BACKUP_COUNT);
  }

  public AbstractXMLDAO (@Nonnull final IHasFilename aFilenameProvider, @Nonnegative final int nBackupCount)
  {
    super (aFilenameProvider, new DefaultDAODataProviderXML (), nBackupCount);
  }

  public AbstractXMLDAO (@Nonnull final IHasFilename aFilenameProvider,
                         @Nonnegative final int nBackupCount,
                         @Nonnull final IDAOIO aDAOIO)
  {
    super (aFilenameProvider, new DefaultDAODataProviderXML (), nBackupCount, aDAOIO);
  }

  protected final void setXMLDataProvider (@Nonnull final IXMLDAODataProvider aXMLDataProvider)
  {
    ((DefaultDAODataProviderXML) getDataProvider ()).setXMLDataProvider (aXMLDataProvider);
  }

  @OverrideOnDemand
  @Nonnull
  public EChange initForFirstTimeUsage ()
  {
    return EChange.UNCHANGED;
  }
}
