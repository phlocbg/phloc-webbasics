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

import org.junit.Test;

import com.phloc.appbasics.mock.MockCurrentUserIDProvider;
import com.phloc.commons.type.ObjectType;
import com.phloc.json.impl.JSONObject;

/**
 * Test class for class {@link LoggingAuditor}.
 * 
 * @author Philip Helger
 */
public class LoggingAuditorTest
{
  @Test
  public void testBasic ()
  {
    final ObjectType aOT = new ObjectType ("mock");
    AuditUtils.setAuditor (new LoggingAuditor (new MockCurrentUserIDProvider ("userid")));
    AuditUtils.onAuditCreateSuccess (aOT);
    AuditUtils.onAuditCreateSuccess (aOT, "this", "is", Integer.valueOf (2), "a", "test");
    AuditUtils.onAuditModifySuccess (aOT, "this", "is", Integer.valueOf (2), "a", "test");
    AuditUtils.onAuditModifyFailure (aOT, "this", "is", Integer.valueOf (2), "a", "test");
    AuditUtils.onAuditDeleteSuccess (aOT, "this", "is", Integer.valueOf (2), "a", "test");
    AuditUtils.onAuditDeleteFailure (aOT, "this", "is", Integer.valueOf (2), "a", "test");
    AuditUtils.onAuditUndeleteSuccess (aOT, "this", "is", Integer.valueOf (2), "a", "test");
    AuditUtils.onAuditUndeleteFailure (aOT, "this", "is", Integer.valueOf (2), "a", "test");
    AuditUtils.onAuditExecuteSuccess ("spawn", "this", "is", Integer.valueOf (2), "a", "test");
    AuditUtils.onAuditExecuteFailure ("spawn", "this", "is", Integer.valueOf (2), "a", "test");
    AuditUtils.onAuditExecuteSuccess (aOT, "this", "is", Integer.valueOf (2), "a", "test");
    AuditUtils.onAuditExecuteFailure (aOT, "this", "is", Integer.valueOf (2), "a", "test");
    JSONObject aJSON = new JSONObject();
    aJSON.set ("a",  "B");
    AuditUtils.onAuditExecuteFailure ("spawn", "this", "is", Integer.valueOf (2), "a", "test", aJSON, 18.34, false, Boolean.FALSE);
  }
}
