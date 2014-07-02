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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.appbasics.app.dao.impl.AbstractSimpleDAO;
import com.phloc.appbasics.app.dao.impl.DAOException;
import com.phloc.appbasics.security.audit.AuditUtils;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.callback.INonThrowingRunnable;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.multimap.IMultiMapListBased;
import com.phloc.commons.collections.multimap.MultiHashMapArrayListBased;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.type.ObjectType;

@ThreadSafe
public class SystemMigrationManager extends AbstractSimpleDAO
{
  public static final ObjectType OT_SYSTEM_MIGRATION_RESULT = new ObjectType ("systemmigrationresult");

  private static final String ELEMENT_SYSTEM_MIGRATION_RESULTS = "systemmigrationresults";
  private static final String ELEMENT_SYSTEM_MIGRATION_RESULT = "systemmigrationresult";

  private final IMultiMapListBased <String, SystemMigrationResult> m_aMap = new MultiHashMapArrayListBased <String, SystemMigrationResult> ();

  public SystemMigrationManager (@Nullable final String sFilename) throws DAOException
  {
    super (sFilename);
    initialRead ();
  }

  @Override
  protected EChange onRead (final IMicroDocument aDoc)
  {
    for (final IMicroElement eMigrationResult : aDoc.getDocumentElement ()
                                                    .getAllChildElements (ELEMENT_SYSTEM_MIGRATION_RESULT))
      internalAdd (MicroTypeConverter.convertToNative (eMigrationResult, SystemMigrationResult.class));
    return EChange.UNCHANGED;
  }

  @Override
  protected IMicroDocument createWriteData ()
  {
    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eRoot = aDoc.appendElement (ELEMENT_SYSTEM_MIGRATION_RESULTS);
    for (final List <SystemMigrationResult> aMigrationResults : ContainerHelper.getSortedByKey (m_aMap).values ())
      for (final SystemMigrationResult aMigrationResult : aMigrationResults)
        eRoot.appendChild (MicroTypeConverter.convertToMicroElement (aMigrationResult, ELEMENT_SYSTEM_MIGRATION_RESULT));
    return aDoc;
  }

  void internalAdd (@Nonnull final SystemMigrationResult aMigrationResult)
  {
    ValueEnforcer.notNull (aMigrationResult, "MigrationResult");

    final String sMigrationID = aMigrationResult.getID ();
    m_aMap.putSingle (sMigrationID, aMigrationResult);
  }

  public void addMigrationResult (@Nonnull final SystemMigrationResult aMigrationResult)
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

    AuditUtils.onAuditCreateSuccess (OT_SYSTEM_MIGRATION_RESULT,
                                     aMigrationResult.getID (),
                                     Boolean.valueOf (aMigrationResult.isSuccess ()));
  }

  /**
   * Mark the specified migration as success.
   * 
   * @param sMigrationID
   *        The migration ID to be added. May neither be <code>null</code> nor
   *        empty.
   */
  public void addMigrationResultSuccess (@Nonnull @Nonempty final String sMigrationID)
  {
    addMigrationResult (SystemMigrationResult.createSuccess (sMigrationID));
  }

  /**
   * Mark the specified migration as failed.
   * 
   * @param sMigrationID
   *        The migration ID to be added. May neither be <code>null</code> nor
   *        empty.
   * @param sErrorMsg
   *        The error message. May not be <code>null</code>.
   */
  public void addMigrationResultError (@Nonnull @Nonempty final String sMigrationID, @Nonnull final String sErrorMsg)
  {
    addMigrationResult (SystemMigrationResult.createFailure (sMigrationID, sErrorMsg));
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <SystemMigrationResult> getAllMigrationResults (@Nullable final String sMigrationID)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (m_aMap.get (sMigrationID));
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <SystemMigrationResult> getAllFailedMigrationResults (@Nullable final String sMigrationID)
  {
    final List <SystemMigrationResult> ret = new ArrayList <SystemMigrationResult> ();
    for (final SystemMigrationResult aMigrationResult : getAllMigrationResults (sMigrationID))
      if (aMigrationResult.isFailure ())
        ret.add (aMigrationResult);
    return ret;
  }

  public boolean wasMigrationExecutedSuccessfully (@Nullable final String sMigrationID)
  {
    final List <SystemMigrationResult> aResults = getAllMigrationResults (sMigrationID);
    for (final SystemMigrationResult aMigrationResult : aResults)
      if (aMigrationResult.isSuccess ())
        return true;
    return false;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <String> getAllMigrationIDs ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newSet (m_aMap.keySet ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Perform a migration if it was not performed yet. The performed callback may
   * not throw an error or return an error. All migrations executed with this
   * method will be handled as a success only.
   * 
   * @param sMigrationID
   *        The migration ID to handle. May neither be <code>null</code> nor
   *        empty.
   * @param aMigrationAction
   *        The action to be performed. May not be <code>null</code>.
   */
  public void performMigrationIfNecessary (@Nonnull @Nonempty final String sMigrationID,
                                           @Nonnull final INonThrowingRunnable aMigrationAction)
  {
    ValueEnforcer.notEmpty (sMigrationID, "MigrationID");
    ValueEnforcer.notNull (aMigrationAction, "MigrationAction");

    if (!wasMigrationExecutedSuccessfully (sMigrationID))
    {
      aMigrationAction.run ();
      addMigrationResultSuccess (sMigrationID);
    }
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("map", m_aMap).toString ();
  }
}
