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
package com.phloc.appbasics.migration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.appbasics.app.dao.impl.AbstractSimpleDAO;
import com.phloc.appbasics.app.dao.impl.DAOException;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.state.EChange;

@ThreadSafe
public class MigrationManager extends AbstractSimpleDAO
{
  private final Map <String, MigrationResult> m_aMap = new HashMap <String, MigrationResult> ();

  public MigrationManager (@Nullable final String sFilename) throws DAOException
  {
    super (sFilename);
    initialRead ();
  }

  @Override
  protected EChange onRead (final IMicroDocument aDoc)
  {
    // FIXME todo
    return EChange.UNCHANGED;
  }

  @Override
  protected IMicroDocument createWriteData ()
  {
    final IMicroDocument aDoc = new MicroDocument ();
    // FIXME todo
    return aDoc;
  }

  void internalAdd (@Nonnull final MigrationResult aMigrationResult)
  {
    ValueEnforcer.notNull (aMigrationResult, "MigrationResult");

    final String sMigrationID = aMigrationResult.getID ();
    // Overwrite is possible, in case a previous migration failed
    m_aMap.put (sMigrationID, aMigrationResult);
  }

  public void addMigrationResult (@Nonnull final MigrationResult aMigrationResult)
  {
    ValueEnforcer.notNull (aMigrationResult, "MigrationResult");

    m_aRWLock.writeLock ().lock ();
    try
    {
      internalAdd (aMigrationResult);
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  public void addMigrationResultSuccess (@Nonnull @Nonempty final String sMigrationID)
  {
    addMigrationResult (MigrationResult.createSuccess (sMigrationID));
  }

  public void addMigrationResultError (@Nonnull @Nonempty final String sMigrationID, @Nonnull final String sErrorMsg)
  {
    addMigrationResult (MigrationResult.createFailure (sMigrationID, sErrorMsg));
  }

  @Nullable
  public MigrationResult getMigrationResult (@Nullable final String sMigrationID)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aMap.get (sMigrationID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public boolean wasMigrationExecutedSuccessfully (@Nullable final String sMigrationID)
  {
    final MigrationResult aResult = getMigrationResult (sMigrationID);
    return aResult != null && aResult.isSuccess ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <MigrationResult> getAllMigrationResults ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (m_aMap.values ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }
}
