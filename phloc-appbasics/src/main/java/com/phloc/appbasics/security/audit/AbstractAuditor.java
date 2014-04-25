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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.appbasics.security.login.ICurrentUserIDProvider;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.type.ObjectType;
import com.phloc.json2.impl.JsonArray;
import com.phloc.json2.impl.JsonObject;

/**
 * Abstract base class for interface {@link IAuditor}.
 * 
 * @author Philip Helger
 */
@Immutable
public abstract class AbstractAuditor implements IAuditor
{
  private final ICurrentUserIDProvider m_aUserIDProvider;

  public AbstractAuditor (@Nonnull final ICurrentUserIDProvider aUserIDProvider)
  {
    m_aUserIDProvider = ValueEnforcer.notNull (aUserIDProvider, "UserIDProvider");
  }

  /**
   * @return <code>true</code> to create a Json string, <code>false</code> to
   *         create a legacy comma separated list
   */
  @OverrideOnDemand
  protected boolean useJsonNotation ()
  {
    return true;
  }

  /**
   * Implement this method to handle the created audit items.
   * 
   * @param aAuditItem
   *        The audit item to handle. Never <code>null</code>.
   */
  protected abstract void handleAuditItem (@Nonnull IAuditItem aAuditItem);

  private void _createAuditItem (@Nonnull final EAuditActionType eType,
                                 @Nonnull final ESuccess eSuccess,
                                 @Nonnull final String sAction)
  {
    final AuditItem aAuditItem = new AuditItem (m_aUserIDProvider.getCurrentUserID (), eType, eSuccess, sAction);
    handleAuditItem (aAuditItem);
  }

  @Nonnull
  @OverrideOnDemand
  protected String createAuditString (@Nonnull final String sObjectType, @Nullable final Object [] aArgs)
  {
    if (useJsonNotation ())
    {
      // Get Json representation for easy evaluation afterwards
      final JsonArray aData = new JsonArray ();
      if (aArgs != null)
        for (final Object aArg : aArgs)
          aData.add (aArg);
      return new JsonObject ().add (sObjectType, aData).getAsString ();
    }

    // Use regular formatting
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

  public void onCreateSuccess (@Nonnull final ObjectType aObjectType, @Nullable final Object... aArgs)
  {
    ValueEnforcer.notNull (aObjectType, "ObjectType");

    _createAuditItem (EAuditActionType.CREATE,
                      ESuccess.SUCCESS,
                      createAuditString (aObjectType.getObjectTypeName (), aArgs));
  }

  public void onCreateFailure (@Nonnull final ObjectType aObjectType, @Nullable final Object... aArgs)
  {
    ValueEnforcer.notNull (aObjectType, "ObjectType");

    _createAuditItem (EAuditActionType.CREATE,
                      ESuccess.FAILURE,
                      createAuditString (aObjectType.getObjectTypeName (), aArgs));
  }

  public void onModifySuccess (@Nonnull final ObjectType aObjectType,
                               @Nonnull final String sWhat,
                               @Nullable final Object... aArgs)
  {
    ValueEnforcer.notNull (aObjectType, "ObjectType");

    _createAuditItem (EAuditActionType.MODIFY,
                      ESuccess.SUCCESS,
                      createAuditString (aObjectType.getObjectTypeName (),
                                         ArrayHelper.getConcatenated (sWhat, aArgs, Object.class)));
  }

  public void onModifyFailure (@Nonnull final ObjectType aObjectType,
                               @Nonnull final String sWhat,
                               @Nullable final Object... aArgs)
  {
    ValueEnforcer.notNull (aObjectType, "ObjectType");

    _createAuditItem (EAuditActionType.MODIFY,
                      ESuccess.FAILURE,
                      createAuditString (aObjectType.getObjectTypeName (),
                                         ArrayHelper.getConcatenated (sWhat, aArgs, Object.class)));
  }

  public void onDeleteSuccess (@Nonnull final ObjectType aObjectType, @Nullable final Object... aArgs)
  {
    ValueEnforcer.notNull (aObjectType, "ObjectType");

    _createAuditItem (EAuditActionType.DELETE,
                      ESuccess.SUCCESS,
                      createAuditString (aObjectType.getObjectTypeName (), aArgs));
  }

  public void onDeleteFailure (@Nonnull final ObjectType aObjectType, @Nullable final Object... aArgs)
  {
    ValueEnforcer.notNull (aObjectType, "ObjectType");

    _createAuditItem (EAuditActionType.DELETE,
                      ESuccess.FAILURE,
                      createAuditString (aObjectType.getObjectTypeName (), aArgs));
  }

  public void onUndeleteSuccess (@Nonnull final ObjectType aObjectType, @Nullable final Object... aArgs)
  {
    ValueEnforcer.notNull (aObjectType, "ObjectType");

    _createAuditItem (EAuditActionType.UNDELETE,
                      ESuccess.SUCCESS,
                      createAuditString (aObjectType.getObjectTypeName (), aArgs));
  }

  public void onUndeleteFailure (@Nonnull final ObjectType aObjectType, @Nullable final Object... aArgs)
  {
    ValueEnforcer.notNull (aObjectType, "ObjectType");

    _createAuditItem (EAuditActionType.UNDELETE,
                      ESuccess.FAILURE,
                      createAuditString (aObjectType.getObjectTypeName (), aArgs));
  }

  public void onExecuteSuccess (@Nonnull final String sWhat, @Nullable final Object... aArgs)
  {
    _createAuditItem (EAuditActionType.EXECUTE, ESuccess.SUCCESS, createAuditString (sWhat, aArgs));
  }

  public void onExecuteFailure (@Nonnull final String sWhat, @Nullable final Object... aArgs)
  {
    _createAuditItem (EAuditActionType.EXECUTE, ESuccess.FAILURE, createAuditString (sWhat, aArgs));
  }

  public void onExecuteSuccess (@Nonnull final ObjectType aObjectType,
                                @Nonnull final String sWhat,
                                @Nullable final Object... aArgs)
  {
    ValueEnforcer.notNull (aObjectType, "ObjectType");

    _createAuditItem (EAuditActionType.EXECUTE,
                      ESuccess.SUCCESS,
                      createAuditString (aObjectType.getObjectTypeName (),
                                         ArrayHelper.getConcatenated (sWhat, aArgs, Object.class)));
  }

  public void onExecuteFailure (@Nonnull final ObjectType aObjectType,
                                @Nonnull final String sWhat,
                                @Nullable final Object... aArgs)
  {
    ValueEnforcer.notNull (aObjectType, "ObjectType");

    _createAuditItem (EAuditActionType.EXECUTE,
                      ESuccess.FAILURE,
                      createAuditString (aObjectType.getObjectTypeName (),
                                         ArrayHelper.getConcatenated (sWhat, aArgs, Object.class)));
  }
}
