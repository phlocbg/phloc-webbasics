package com.phloc.appbasics.migration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.appbasics.app.dao.impl.DAOException;

public final class SystemMigrationManagerTest
{
  @Test
  public void testBasicSuccess () throws DAOException
  {
    final SystemMigrationManager aMgr = new SystemMigrationManager (null);
    assertTrue (aMgr.getAllMigrationIDs ().isEmpty ());
    assertFalse (aMgr.wasMigrationExecutedSuccessfully ("mig1"));
    assertTrue (aMgr.getAllMigrationResults ("mig1").isEmpty ());
    assertTrue (aMgr.getAllFailedMigrationResults ("mig1").isEmpty ());

    aMgr.addMigrationResult (SystemMigrationResult.createSuccess ("mig1"));
    assertEquals (1, aMgr.getAllMigrationIDs ().size ());
    assertTrue (aMgr.wasMigrationExecutedSuccessfully ("mig1"));
    assertEquals (1, aMgr.getAllMigrationResults ("mig1").size ());
    assertEquals (0, aMgr.getAllFailedMigrationResults ("mig1").size ());

    aMgr.addMigrationResult (SystemMigrationResult.createFailure ("mig2", "oops"));
    assertEquals (2, aMgr.getAllMigrationIDs ().size ());
    assertTrue (aMgr.wasMigrationExecutedSuccessfully ("mig1"));
    assertFalse (aMgr.wasMigrationExecutedSuccessfully ("mig2"));
    assertEquals (1, aMgr.getAllMigrationResults ("mig1").size ());
    assertEquals (0, aMgr.getAllFailedMigrationResults ("mig1").size ());
    assertEquals (1, aMgr.getAllMigrationResults ("mig2").size ());
    assertEquals (1, aMgr.getAllFailedMigrationResults ("mig2").size ());

    aMgr.addMigrationResult (SystemMigrationResult.createFailure ("mig2", "oops2"));
    assertEquals (2, aMgr.getAllMigrationIDs ().size ());
    assertTrue (aMgr.wasMigrationExecutedSuccessfully ("mig1"));
    assertFalse (aMgr.wasMigrationExecutedSuccessfully ("mig2"));
    assertEquals (1, aMgr.getAllMigrationResults ("mig1").size ());
    assertEquals (0, aMgr.getAllFailedMigrationResults ("mig1").size ());
    assertEquals (2, aMgr.getAllMigrationResults ("mig2").size ());
    assertEquals (2, aMgr.getAllFailedMigrationResults ("mig2").size ());

    aMgr.addMigrationResult (SystemMigrationResult.createSuccess ("mig2"));
    assertEquals (2, aMgr.getAllMigrationIDs ().size ());
    assertTrue (aMgr.wasMigrationExecutedSuccessfully ("mig1"));
    assertTrue (aMgr.wasMigrationExecutedSuccessfully ("mig2"));
    assertEquals (1, aMgr.getAllMigrationResults ("mig1").size ());
    assertEquals (0, aMgr.getAllFailedMigrationResults ("mig1").size ());
    assertEquals (3, aMgr.getAllMigrationResults ("mig2").size ());
    assertEquals (2, aMgr.getAllFailedMigrationResults ("mig2").size ());
  }
}
