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
package com.phloc.sysmon;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;

import org.slf4j.bridge.SLF4JBridgeHandler;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.exceptions.InitializationException;
import com.phloc.sysmon.jobs.CheckDeadlockJob;
import com.phloc.sysmon.jobs.CheckDiskUsableSpaceJob;
import com.phloc.sysmon.mock.DummyDeadLock;
import com.phloc.webbasics.app.error.InternalErrorHandler;
import com.phloc.webbasics.servlet.WebAppListener;
import com.phloc.webbasics.smtp.FailedMailResendJob;

/**
 * Initialization
 * 
 * @author Philip Helger
 */
public final class SystemMonitorWebAppListener extends WebAppListener {
  @Override
  protected String getInitParameterTrace (@Nonnull final ServletContext aSC) {
    return SysMonConfig.getGlobalTrace ();
  }

  @Override
  protected String getInitParameterDebug (@Nonnull final ServletContext aSC) {
    return SysMonConfig.getGlobalDebug ();
  }

  @Override
  protected String getInitParameterProduction (@Nonnull final ServletContext aSC) {
    return SysMonConfig.getGlobalProduction ();
  }

  @Override
  protected String getDataPath (@Nonnull final ServletContext aSC) {
    return SysMonConfig.getDataPath ();
  }

  @Override
  protected boolean shouldCheckFileAccess (@Nonnull final ServletContext aSC) {
    return false;
  }

  @Override
  protected void afterContextInitialized (@Nonnull final ServletContext aSC) {
    // JUL to SLF4J
    SLF4JBridgeHandler.removeHandlersForRootLogger ();
    SLF4JBridgeHandler.install ();

    SysMonInternalErrorHandler.doSetup ();

    if (InternalErrorHandler.getSMTPSenderAddress () == null)
      throw new InitializationException ("No Email sender is configured! Please check the config file.");
    if (ContainerHelper.isEmpty (InternalErrorHandler.getSMTPReceiverAddresses ()))
      throw new InitializationException ("No Email receiver is configured! Please check the config file.");
    if (InternalErrorHandler.getSMTPSettings () == null ||
        !InternalErrorHandler.getSMTPSettings ().areRequiredFieldsSet ())
      throw new InitializationException ("SMTP configuration is invalid! Please check the config file.");

    // Managers etc.
    CheckDiskUsableSpaceJob.schedule ();
    CheckDeadlockJob.schedule ();
    FailedMailResendJob.scheduleMe (CSystemMonitor.APP_ID, 10);

    if (false)
      DummyDeadLock.triggerDummyDeadlock (5);
  }
}
