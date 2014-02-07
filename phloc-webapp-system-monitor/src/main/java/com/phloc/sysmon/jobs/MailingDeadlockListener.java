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
package com.phloc.sysmon.jobs;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.deadlock.IThreadDeadlockListener;
import com.phloc.commons.deadlock.ThreadDeadlockInfo;
import com.phloc.commons.lang.StackTraceHelper;
import com.phloc.web.smtp.EEmailType;
import com.phloc.web.smtp.IEmailData;
import com.phloc.web.smtp.impl.EmailData;
import com.phloc.webbasics.app.error.InternalErrorHandler;
import com.phloc.webbasics.app.error.ThreadDescriptor;
import com.phloc.webbasics.app.error.ThreadDescriptorList;
import com.phloc.webscopes.smtp.ScopedMailAPI;

/**
 * The deadlock detection listener that sends an email
 * 
 * @author Philip Helger
 */
public class MailingDeadlockListener implements IThreadDeadlockListener {
  private static final Logger s_aLogger = LoggerFactory.getLogger (MailingDeadlockListener.class);

  private static String _getAsString (@Nonnull final ThreadDeadlockInfo aTDI) {
    // Always ends with a newline char
    String sTI = aTDI.getThreadInfo ().toString ();
    final StackTraceElement [] aSTE = aTDI.getStackTrace ();
    if (aSTE != null)
      sTI += StackTraceHelper.getStackAsString (aSTE, false);
    return sTI;
  }

  public void onDeadlockDetected (@Nonnull @Nonempty final ThreadDeadlockInfo [] aDeadlockedThreads) {
    s_aLogger.warn ("Deadlock of " + ArrayHelper.getSize (aDeadlockedThreads) + " threads detected!");
    final StringBuilder aSB = new StringBuilder ();
    aSB.append (InternalErrorHandler.fillInternalErrorMetaData (null, null, null).getAsString ());
    for (final ThreadDeadlockInfo aTDI : aDeadlockedThreads)
      aSB.append ('\n').append (_getAsString (aTDI));

    aSB.append ("\n---------------------------------------------------------------\n")
       .append (ThreadDescriptor.createForCurrentThread (null).getAsString ())
       .append ("\n---------------------------------------------------------------\n")
       .append (ThreadDescriptorList.createWithAllThreads ().getAsString ())
       .append ("\n---------------------------------------------------------------");

    final IEmailData aEmailData = new EmailData (EEmailType.TEXT);
    aEmailData.setFrom (InternalErrorHandler.getSMTPSenderAddress ());
    aEmailData.setTo (InternalErrorHandler.getSMTPReceiverAddresses ());
    aEmailData.setSubject ("[system-monitor] Dead lock of " + aDeadlockedThreads.length + " threads detected");
    aEmailData.setBody (aSB.toString ());

    ScopedMailAPI.getInstance ().queueMail (InternalErrorHandler.getSMTPSettings (), aEmailData);
  }
}
