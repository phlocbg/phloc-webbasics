/**
 * Copyright (C) 2006-2015 phloc systems
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.joda.time.DateTime;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.datetime.PDTFactory;

/**
 * Represents a single change item
 * 
 * @author Philip Helger
 */
@Immutable
final class AuditItem implements IAuditItem
{
  private final DateTime m_aDateTime;
  private final String m_sUserID;
  private final EAuditActionType m_eType;
  private final ESuccess m_eSuccess;
  private final String m_sAction;

  public AuditItem (@Nullable final String sUserID,
                    @Nonnull final EAuditActionType eType,
                    @Nonnull final ESuccess eSuccess,
                    @Nonnull final String sAction)
  {
    this (PDTFactory.getCurrentDateTime (), sUserID != null ? sUserID : CAudit.GUEST_USERID, eType, eSuccess, sAction);
  }

  AuditItem (@Nonnull final DateTime aDateTime,
             @Nonnull final String sUserID,
             @Nonnull final EAuditActionType eType,
             @Nonnull final ESuccess eSuccess,
             @Nonnull final String sAction)
  {
    m_aDateTime = ValueEnforcer.notNull (aDateTime, "DateTime");
    m_sUserID = ValueEnforcer.notEmpty (sUserID, "UserID");
    m_eType = ValueEnforcer.notNull (eType, "Type");
    m_eSuccess = ValueEnforcer.notNull (eSuccess, "Success");
    m_sAction = ValueEnforcer.notNull (sAction, "Action");
  }

  @Nonnull
  public DateTime getDateTime ()
  {
    return m_aDateTime;
  }

  @Nonnull
  public String getUserID ()
  {
    return m_sUserID;
  }

  public boolean isAnonymousUser ()
  {
    return CAudit.GUEST_USERID.equals (m_sUserID);
  }

  @Nonnull
  public EAuditActionType getType ()
  {
    return m_eType;
  }

  @Nonnull
  public ESuccess getSuccess ()
  {
    return m_eSuccess;
  }

  public boolean isSuccess ()
  {
    return m_eSuccess.isSuccess ();
  }

  public boolean isFailure ()
  {
    return m_eSuccess.isFailure ();
  }

  @Nonnull
  public String getAction ()
  {
    return m_sAction;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof AuditItem))
      return false;
    final AuditItem rhs = (AuditItem) o;
    return m_aDateTime.equals (rhs.m_aDateTime) &&
           m_sUserID.equals (rhs.m_sUserID) &&
           m_eType.equals (rhs.m_eType) &&
           m_eSuccess.equals (rhs.m_eSuccess) &&
           m_sAction.equals (rhs.m_sAction);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aDateTime)
                                       .append (m_sUserID)
                                       .append (m_eType)
                                       .append (m_eSuccess)
                                       .append (m_sAction)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("dateTime", m_aDateTime)
                                       .append ("userID", m_sUserID)
                                       .append ("type", m_eType)
                                       .append ("success", m_eSuccess)
                                       .append ("action", m_sAction)
                                       .toString ();
  }
}
