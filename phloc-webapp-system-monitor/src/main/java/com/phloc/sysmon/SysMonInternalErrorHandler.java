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

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.appbasics.app.dao.impl.AbstractDAO;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.schedule.job.AbstractJob;
import com.phloc.schedule.job.IJobExceptionHandler;
import com.phloc.webbasics.action.servlet.AbstractActionServlet;
import com.phloc.webbasics.ajax.servlet.AbstractAjaxServlet;
import com.phloc.webbasics.app.error.AbstractErrorCallback;
import com.phloc.webbasics.app.error.InternalErrorHandler;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * The internal error handler
 * 
 * @author Philip Helger
 */
public final class SysMonInternalErrorHandler extends AbstractErrorCallback implements IJobExceptionHandler
{
  @Override
  protected void onError (@Nonnull final Throwable t,
                          @Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                          @Nonnull @Nonempty final String sErrorCode)
  {
    final Locale aDisplayLocale = ApplicationRequestManager.getInstance ().getRequestDisplayLocale ();
    InternalErrorHandler.handleInternalError (null, t, aRequestScope, sErrorCode, null, aDisplayLocale, true);
  }

  public void onScheduledJobException (@Nonnull final Throwable t,
                                       @Nonnull final String sJobClassName,
                                       final boolean bIsLongRunning)
  {
    onError (t, null, "Error executing" + (bIsLongRunning ? " long running" : "") + " job " + sJobClassName);
  }

  public static void doSetup ()
  {
    // Set global internal error handlers
    final SysMonInternalErrorHandler aIntErrHdl = new SysMonInternalErrorHandler ();
    AbstractAjaxServlet.setCustomExceptionHandler (aIntErrHdl);
    AbstractActionServlet.setCustomExceptionHandler (aIntErrHdl);
    AbstractDAO.setCustomExceptionHandlerRead (aIntErrHdl);
    AbstractDAO.setCustomExceptionHandlerWrite (aIntErrHdl);
    AbstractJob.setCustomExceptionHandler (aIntErrHdl);
    InternalErrorHandler.setSMTPSenderAddress (SysMonConfig.getEmailSender ());
    InternalErrorHandler.setSMTPReceiverAddresses (SysMonConfig.getEmailReceivers ());
    InternalErrorHandler.setSMTPSettings (SysMonConfig.getSMTPSettings ());
  }
}
