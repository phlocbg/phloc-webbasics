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
package com.phloc.webdemoapp.app.mgr;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.dao.impl.DAOException;
import com.phloc.appbasics.security.audit.AuditManager;
import com.phloc.appbasics.security.audit.AuditUtils;
import com.phloc.appbasics.security.login.LoggedInUserManager;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.exceptions.InitializationException;
import com.phloc.scopes.singleton.GlobalSingleton;

public final class MetaManager extends GlobalSingleton
{
  public static final String DIRECTORY_AUDITS = "audits/";

  private AuditManager m_aAuditMgr;

  @Deprecated
  @UsedViaReflection
  public MetaManager ()
  {}

  @Override
  protected void onAfterInstantiation ()
  {
    try
    {
      m_aAuditMgr = new AuditManager (DIRECTORY_AUDITS, LoggedInUserManager.getInstance ());
      AuditUtils.setAuditor (m_aAuditMgr.getAuditor ());
    }
    catch (final DAOException ex)
    {
      throw new InitializationException ("Failed to init managers", ex);
    }
  }

  @Override
  protected void onDestroy ()
  {
    if (m_aAuditMgr != null)
    {
      AuditUtils.setDefaultAuditor ();
      m_aAuditMgr.stop ();
    }
  }

  @Nonnull
  public static MetaManager getInstance ()
  {
    return getGlobalSingleton (MetaManager.class);
  }

  @Nonnull
  public static AuditManager getAuditMgr ()
  {
    return getInstance ().m_aAuditMgr;
  }
}
