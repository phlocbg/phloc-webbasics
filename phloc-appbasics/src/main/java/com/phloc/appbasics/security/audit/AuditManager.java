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

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.appbasics.app.dao.AbstractXMLDAO;
import com.phloc.appbasics.app.dao.DAOException;
import com.phloc.appbasics.app.io.IHasFilename;
import com.phloc.appbasics.app.io.WebIO;
import com.phloc.appbasics.security.login.ICurrentUserIDProvider;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.type.ObjectType;
import com.phloc.datetime.io.PDTIOHelper;

/**
 * The class handles all audit items
 * 
 * @author philip
 */
@ThreadSafe
public final class AuditManager extends AbstractXMLDAO implements IAuditor
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

  private final AuditItemList m_aItems;

  public AuditManager (@Nullable final String sBaseDir, @Nonnull final ICurrentUserIDProvider aUserIDProvider) throws DAOException
  {
    super (new AuditHasFilename (sBaseDir));

    // Ensure base path is present
    if (StringHelper.hasText (sBaseDir) && !WebIO.resourceExists (sBaseDir))
      WebIO.mkDir (sBaseDir, true);

    m_aItems = new AuditItemList (aUserIDProvider);
    setXMLDataProvider (new AuditManagerXMLDAO (this));
    readFromFile ();
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
  public EChange createAuditItem (@Nonnull final EAuditActionType eType,
                                  @Nonnull final ESuccess eSuccess,
                                  @Nonnull final String sAction)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_aItems.createItem (eType, eSuccess, sAction).isUnchanged ())
        return EChange.UNCHANGED;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    markAsChanged ();
    return EChange.CHANGED;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IAuditItem> getAllAuditItems ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (m_aItems.getAllItems ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  private static String _createAuditString (@Nonnull final String sObjectType, @Nullable final String [] aArgs)
  {
    if (ArrayHelper.isEmpty (aArgs))
      return sObjectType;
    final StringBuilder aSB = new StringBuilder (sObjectType).append ('(');
    for (int i = 0; i < aArgs.length; ++i)
    {
      if (i > 0)
        aSB.append (',');
      aSB.append (aArgs[i]);
    }
    return aSB.append (')').toString ();
  }

  @Nonnull
  private static String _createAuditStringWithWhat (@Nonnull final String sObjectType,
                                                    @Nonnull final String sWhat,
                                                    @Nullable final String [] aArgs)
  {
    final StringBuilder aSB = new StringBuilder (sObjectType).append ('(').append (sWhat);
    if (aArgs != null)
      for (final String sArg : aArgs)
        aSB.append (',').append (sArg);
    return aSB.append (')').toString ();
  }

  public void onCreateSuccess (@Nonnull final ObjectType aObjectType, @Nullable final String... aArgs)
  {
    createAuditItem (EAuditActionType.CREATE,
                     ESuccess.SUCCESS,
                     _createAuditString (aObjectType.getObjectTypeName (), aArgs));
  }

  public void onCreateFailure (@Nonnull final ObjectType aObjectType, @Nullable final String... aArgs)
  {
    createAuditItem (EAuditActionType.CREATE,
                     ESuccess.FAILURE,
                     _createAuditString (aObjectType.getObjectTypeName (), aArgs));
  }

  public void onModifySuccess (@Nonnull final ObjectType aObjectType,
                               final String sWhat,
                               @Nullable final String... aArgs)
  {
    createAuditItem (EAuditActionType.MODIFY,
                     ESuccess.SUCCESS,
                     _createAuditStringWithWhat (aObjectType.getObjectTypeName (), sWhat, aArgs));
  }

  public void onModifyFailure (@Nonnull final ObjectType aObjectType,
                               final String sWhat,
                               @Nullable final String... aArgs)
  {
    createAuditItem (EAuditActionType.MODIFY,
                     ESuccess.FAILURE,
                     _createAuditStringWithWhat (aObjectType.getObjectTypeName (), sWhat, aArgs));
  }

  public void onDeleteSuccess (@Nonnull final ObjectType aObjectType, @Nullable final String... aArgs)
  {
    createAuditItem (EAuditActionType.DELETE,
                     ESuccess.SUCCESS,
                     _createAuditString (aObjectType.getObjectTypeName (), aArgs));
  }

  public void onDeleteFailure (@Nonnull final ObjectType aObjectType, @Nullable final String... aArgs)
  {
    createAuditItem (EAuditActionType.DELETE,
                     ESuccess.FAILURE,
                     _createAuditString (aObjectType.getObjectTypeName (), aArgs));
  }

  public void onExecuteSuccess (@Nonnull final String sWhat, @Nullable final String... aArgs)
  {
    createAuditItem (EAuditActionType.EXECUTE, ESuccess.SUCCESS, _createAuditString (sWhat, aArgs));
  }

  public void onExecuteFailure (@Nonnull final String sWhat, @Nullable final String... aArgs)
  {
    createAuditItem (EAuditActionType.EXECUTE, ESuccess.FAILURE, _createAuditString (sWhat, aArgs));
  }

  public void onExecuteSuccess (@Nonnull final ObjectType aObjectType,
                                @Nonnull final String sWhat,
                                @Nullable final String... aArgs)
  {
    createAuditItem (EAuditActionType.EXECUTE,
                     ESuccess.SUCCESS,
                     _createAuditStringWithWhat (aObjectType.getObjectTypeName (), sWhat, aArgs));
  }

  public void onExecuteFailure (@Nonnull final ObjectType aObjectType,
                                @Nonnull final String sWhat,
                                @Nullable final String... aArgs)
  {
    createAuditItem (EAuditActionType.EXECUTE,
                     ESuccess.FAILURE,
                     _createAuditStringWithWhat (aObjectType.getObjectTypeName (), sWhat, aArgs));
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
