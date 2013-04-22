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

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.appbasics.app.dao.impl.DAOException;
import com.phloc.appbasics.app.dao.xml.AbstractXMLDAO;
import com.phloc.appbasics.app.io.IHasFilename;
import com.phloc.appbasics.app.io.WebIO;
import com.phloc.appbasics.security.login.ICurrentUserIDProvider;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.callback.IThrowingRunnableWithParameter;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.datetime.io.PDTIOHelper;

/**
 * The class handles all audit items
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class AuditManager extends AbstractXMLDAO implements IAuditManager
{
  private static final class AuditHasFilename implements IHasFilename
  {
    private final String m_sBaseDir;

    AuditHasFilename (@Nullable final String sBaseDir)
    {
      m_sBaseDir = sBaseDir;
    }

    @Nullable
    public String getFilename ()
    {
      // No base dir -> in memory only
      if (StringHelper.hasNoText (m_sBaseDir))
        return null;
      return m_sBaseDir + PDTIOHelper.getCurrentDateForFilename () + ".xml";
    }

    @Override
    public boolean equals (final Object o)
    {
      if (o == this)
        return true;
      if (!(o instanceof AuditHasFilename))
        return false;
      final AuditHasFilename rhs = (AuditHasFilename) o;
      return EqualsUtils.equals (m_sBaseDir, rhs.m_sBaseDir);
    }

    @Override
    public int hashCode ()
    {
      return new HashCodeGenerator (this).append (m_sBaseDir).getHashCode ();
    }

    @Override
    public String toString ()
    {
      return new ToStringGenerator (this).append ("baseDir", m_sBaseDir).toString ();
    }
  }

  private final AuditItemList m_aItems = new AuditItemList ();
  private final AsynchronousAuditor m_aAuditor;

  public AuditManager (@Nullable final String sBaseDir, @Nonnull final ICurrentUserIDProvider aUserIDProvider) throws DAOException
  {
    super (new AuditHasFilename (sBaseDir));
    if (aUserIDProvider == null)
      throw new NullPointerException ("userIDProvider");

    // Ensure base path is present
    if (StringHelper.hasText (sBaseDir))
      WebIO.createDirRecursiveIfNotExisting (sBaseDir);

    final IThrowingRunnableWithParameter <List <IAuditItem>> aPerformer = new IThrowingRunnableWithParameter <List <IAuditItem>> ()
    {
      public void run (@Nonnull final List <IAuditItem> aAuditItems) throws Exception
      {
        if (!aAuditItems.isEmpty ())
        {
          m_aRWLock.writeLock ().lock ();
          try
          {
            for (final IAuditItem aItem : aAuditItems)
              m_aItems.internalAddItem (aItem);
          }
          finally
          {
            m_aRWLock.writeLock ().unlock ();
          }
          markAsChanged ();
        }
      }
    };

    m_aAuditor = new AsynchronousAuditor (aUserIDProvider, aPerformer);
    setXMLDataProvider (new AuditManagerXMLDAO (this));
    readFromFile ();
  }

  @Nonnull
  public IAuditor getAuditor ()
  {
    return m_aAuditor;
  }

  @Override
  protected void onFilenameChange ()
  {
    // Called within a write lock
    m_aItems.internalKeepOnlyLast ();
  }

  void internalAddItem (@Nonnull final IAuditItem aItem)
  {
    m_aItems.internalAddItem (aItem);
  }

  @Nonnull
  @ReturnsImmutableObject
  List <IAuditItem> internalGetAllItems ()
  {
    // Is sorted in itself
    return m_aItems.getAllItems ();
  }

  @Nonnull
  @ReturnsMutableCopy
  @Deprecated
  public List <IAuditItem> getAllAuditItems ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aItems.getAllItems ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IAuditItem> getLastAuditItems (@Nonnegative final int nMaxItems)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aItems.getLastItems (nMaxItems);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public void stop ()
  {
    // Do not call in write-lock as this call blocks until the auditor is
    // stopped
    m_aAuditor.stop ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final AuditManager rhs = (AuditManager) o;
    return m_aItems.equals (rhs.m_aItems);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aItems).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("items", m_aItems).toString ();
  }
}
