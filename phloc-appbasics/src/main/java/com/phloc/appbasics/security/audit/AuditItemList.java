/**
 * Copyright (C) 2006-2012 phloc systems
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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.appbasics.security.login.ICurrentUserIDProvider;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Manages a list of {@link IAuditItem} objects.
 * 
 * @author philip
 */
@NotThreadSafe
final class AuditItemList
{
  private final List <IAuditItem> m_aItems = new ArrayList <IAuditItem> ();
  private final ICurrentUserIDProvider m_aUserIDProvider;

  public AuditItemList (@Nonnull final ICurrentUserIDProvider aUserIDProvider)
  {
    if (aUserIDProvider == null)
      throw new NullPointerException ("userIDProvider");
    m_aUserIDProvider = aUserIDProvider;
  }

  void internalKeepOnlyLast ()
  {
    final IAuditItem aLastItem = ContainerHelper.getLastElement (m_aItems);
    m_aItems.clear ();
    m_aItems.add (aLastItem);
  }

  void internalAddItem (@Nonnull final IAuditItem aItem)
  {
    if (aItem == null)
      throw new NullPointerException ("item");

    m_aItems.add (aItem);
  }

  @Nonnull
  public EChange createItem (@Nonnull final EAuditActionType eType,
                             @Nonnull final ESuccess eSuccess,
                             @Nonnull final String sAction)
  {
    final String sUserID = m_aUserIDProvider.getCurrentUserID ();
    internalAddItem (new AuditItem (sUserID != null ? sUserID : CAudit.GUEST_USERID, eType, eSuccess, sAction));
    return EChange.CHANGED;
  }

  @Nonnull
  @ReturnsImmutableObject
  public List <IAuditItem> getAllItems ()
  {
    return ContainerHelper.makeUnmodifiable (m_aItems);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof AuditItemList))
      return false;
    final AuditItemList rhs = (AuditItemList) o;
    return m_aItems.equals (rhs.m_aItems);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aItems).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("items", m_aItems).toString ();
  }
}
