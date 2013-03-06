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

import org.junit.Test;

import com.phloc.appbasics.mock.MockCurrentUserIDProvider;
import com.phloc.commons.type.ObjectType;

/**
 * Test class for class {@link LoggingAuditor}.
 * 
 * @author philip
 */
public class LoggingAuditorTest
{
  @Test
  public void testBasic ()
  {
    final ObjectType aOT = new ObjectType ("mock");
    AuditUtils.setAuditor (new LoggingAuditor (new MockCurrentUserIDProvider ("userid")));
    AuditUtils.onAuditCreateSuccess (aOT);
    AuditUtils.onAuditCreateSuccess (aOT, "this", "is", "a", "test");
    AuditUtils.onAuditModifySuccess (aOT, "this", "is", "a", "test");
    AuditUtils.onAuditModifyFailure (aOT, "this", "is", "a", "test");
    AuditUtils.onAuditDeleteSuccess (aOT, "this", "is", "a", "test");
    AuditUtils.onAuditDeleteFailure (aOT, "this", "is", "a", "test");
    AuditUtils.onAuditUndeleteSuccess (aOT, "this", "is", "a", "test");
    AuditUtils.onAuditUndeleteFailure (aOT, "this", "is", "a", "test");
    AuditUtils.onAuditExecuteSuccess ("spawn", "this", "is", "a", "test");
    AuditUtils.onAuditExecuteFailure ("spawn", "this", "is", "a", "test");
    AuditUtils.onAuditExecuteSuccess (aOT, "this", "is", "a", "test");
    AuditUtils.onAuditExecuteFailure (aOT, "this", "is", "a", "test");
  }
}
