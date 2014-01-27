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
package com.phloc.webbasics.mgr;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.dao.impl.DAOException;
import com.phloc.appbasics.security.audit.AuditManager;
import com.phloc.appbasics.security.audit.AuditUtils;
import com.phloc.appbasics.security.lock.ObjectLockManager;
import com.phloc.appbasics.security.login.LoggedInUserManager;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.exceptions.InitializationException;
import com.phloc.scopes.singleton.GlobalSingleton;
import com.phloc.web.smtp.failed.FailedMailQueue;
import com.phloc.webbasics.smtp.FailedMailQueueWithDAO;
import com.phloc.webbasics.smtp.NamedSMTPSettingsManager;
import com.phloc.webscopes.smtp.ScopedMailAPI;

public final class MetaSystemManager extends GlobalSingleton
{
  public static final String DIRECTORY_AUDITS = "audits/";
  public static final String SMTP_SETTINGS_XML = "smtpsettings.xml";
  public static final String FAILED_MAILS_XML = "failedmails.xml";

  private static final Logger s_aLogger = LoggerFactory.getLogger (MetaSystemManager.class);

  private AuditManager m_aAuditMgr;
  private NamedSMTPSettingsManager m_aSMTPSettingsMgr;
  private FailedMailQueueWithDAO m_aFailedMailQueue;

  @Deprecated
  @UsedViaReflection
  public MetaSystemManager ()
  {}

  @Override
  protected void onAfterInstantiation ()
  {
    try
    {
      m_aAuditMgr = new AuditManager (DIRECTORY_AUDITS, LoggedInUserManager.getInstance ());
      AuditUtils.setAuditor (m_aAuditMgr.getAuditor ());

      m_aSMTPSettingsMgr = new NamedSMTPSettingsManager (SMTP_SETTINGS_XML);

      m_aFailedMailQueue = new FailedMailQueueWithDAO (FAILED_MAILS_XML);
      ScopedMailAPI.getInstance ().setFailedMailQueue (m_aFailedMailQueue);

      s_aLogger.info ("MetaSystemManager was initialized");
    }
    catch (final DAOException ex)
    {
      throw new InitializationException ("Failed to init managers", ex);
    }
  }

  @Override
  protected void onDestroy ()
  {
    if (m_aFailedMailQueue != null)
    {
      // Set to default queue
      ScopedMailAPI.getInstance ().setFailedMailQueue (new FailedMailQueue ());
    }

    if (m_aAuditMgr != null)
    {
      AuditUtils.setDefaultAuditor ();
      m_aAuditMgr.stop ();
    }
  }

  @Nonnull
  public static MetaSystemManager getInstance ()
  {
    return getGlobalSingleton (MetaSystemManager.class);
  }

  @Nonnull
  public static AuditManager getAuditMgr ()
  {
    return getInstance ().m_aAuditMgr;
  }

  @Nonnull
  public static NamedSMTPSettingsManager getSMTPSettingsMgr ()
  {
    return getInstance ().m_aSMTPSettingsMgr;
  }

  @Nonnull
  public static FailedMailQueueWithDAO getFailedMailQueue ()
  {
    return getInstance ().m_aFailedMailQueue;
  }

  @Nonnull
  public static ObjectLockManager getLockManager ()
  {
    return ObjectLockManager.getInstance ();
  }
}
