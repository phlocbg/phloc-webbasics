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
package com.phloc.appbasics.security.audit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.phloc.appbasics.mock.MockCurrentUserIDProvider;
import com.phloc.commons.callback.IThrowingRunnableWithParameter;
import com.phloc.commons.concurrent.ThreadUtils;
import com.phloc.commons.mutable.MutableInt;
import com.phloc.commons.type.ObjectType;

/**
 * Test class for class {@link AsynchronousAuditor}.
 * 
 * @author Philip Helger
 */
public class AsynchronousAuditorTest
{
  @Test
  public void testBasic ()
  {
    final ObjectType aOT = new ObjectType ("mock");
    final MutableInt aPerformCount = new MutableInt (0);
    final IThrowingRunnableWithParameter <List <IAuditItem>> aPerformer = new IThrowingRunnableWithParameter <List <IAuditItem>> ()
    {
      public void run (@Nonnull final List <IAuditItem> aItems) throws Exception
      {
        // Count number of items to be handled
        aPerformCount.inc (aItems.size ());
      }
    };
    final AsynchronousAuditor aAuditor = new AsynchronousAuditor (new MockCurrentUserIDProvider ("userid"), aPerformer);
    AuditUtils.setAuditor (aAuditor);
    AuditUtils.onAuditCreateSuccess (aOT);
    AuditUtils.onAuditCreateSuccess (aOT, "this", "is", Integer.valueOf (2), "a", "test");
    AuditUtils.onAuditModifySuccess (aOT, "this", "is", Integer.valueOf (2), "a", "test");
    AuditUtils.onAuditModifyFailure (aOT, "this", "is", Integer.valueOf (2), "a", "test");
    AuditUtils.onAuditDeleteSuccess (aOT, "this", "is", Integer.valueOf (2), "a", "test");
    AuditUtils.onAuditDeleteFailure (aOT, "this", "is", Integer.valueOf (2), "a", "test");
    AuditUtils.onAuditUndeleteSuccess (aOT, "this", "is", Integer.valueOf (2), "a", "test");
    ThreadUtils.sleep (10);
    AuditUtils.onAuditUndeleteFailure (aOT, "this", "is", Integer.valueOf (2), "a", "test");
    AuditUtils.onAuditExecuteSuccess ("spawn", "this", "is", Integer.valueOf (2), "a", "test");
    AuditUtils.onAuditExecuteFailure ("spawn", "this", "is", Integer.valueOf (2), "a", "test");
    AuditUtils.onAuditExecuteSuccess (aOT, "this", "is", Integer.valueOf (2), "a", "test");
    AuditUtils.onAuditExecuteFailure (aOT, "this", "is", Integer.valueOf (2), "a", "test");
    // Stop!
    assertTrue (aAuditor.stop ().isChanged ());
    assertFalse (aAuditor.stop ().isChanged ());

    // Ensure that all audit were performed
    assertEquals (12, aPerformCount.intValue ());

    // And now, after we stopped...
    try
    {
      AuditUtils.onAuditExecuteFailure (aOT, "this", "is", "a", "test");
      fail ();
    }
    catch (final IllegalStateException ex)
    {
      // ... no new objects can be queued!
    }

    // Ensure that nothing changed
    assertEquals (12, aPerformCount.intValue ());
  }
}
