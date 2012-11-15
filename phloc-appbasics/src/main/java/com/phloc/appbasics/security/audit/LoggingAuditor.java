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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.type.ObjectType;

/**
 * An implementation of {@link IAuditor} using SLF4J logging.
 * 
 * @author philip
 */
public final class LoggingAuditor implements IAuditor
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggingAuditor.class);
  private static final LoggingAuditor s_aInstance = new LoggingAuditor ();

  private LoggingAuditor ()
  {}

  @Nonnull
  public static LoggingAuditor getInstance ()
  {
    return s_aInstance;
  }

  public void onCreateSuccess (@Nonnull final ObjectType aObjectType, @Nullable final String... aArgs)
  {
    s_aLogger.info ("create.success(" + aObjectType.getObjectTypeName () + "," + ContainerHelper.newList (aArgs) + ")");
  }

  public void onCreateFailure (@Nonnull final ObjectType aObjectType, @Nullable final String... aArgs)
  {
    s_aLogger.info ("create.failure(" + aObjectType.getObjectTypeName () + "," + ContainerHelper.newList (aArgs) + ")");
  }

  public void onModifySuccess (@Nonnull final ObjectType aObjectType,
                               final String sWhat,
                               @Nullable final String... aArgs)
  {
    s_aLogger.info ("modify.success(" +
                    aObjectType.getObjectTypeName () +
                    "," +
                    sWhat +
                    "," +
                    ContainerHelper.newList (aArgs) +
                    ")");
  }

  public void onModifyFailure (@Nonnull final ObjectType aObjectType,
                               final String sWhat,
                               @Nullable final String... aArgs)
  {
    s_aLogger.info ("modify.failure(" +
                    aObjectType.getObjectTypeName () +
                    "," +
                    sWhat +
                    "," +
                    ContainerHelper.newList (aArgs) +
                    ")");
  }

  public void onDeleteSuccess (@Nonnull final ObjectType aObjectType, @Nullable final String... aArgs)
  {
    s_aLogger.info ("delete.success(" + aObjectType.getObjectTypeName () + "," + ContainerHelper.newList (aArgs) + ")");
  }

  public void onDeleteFailure (@Nonnull final ObjectType aObjectType, @Nullable final String... aArgs)
  {
    s_aLogger.info ("delete.failure(" + aObjectType.getObjectTypeName () + "," + ContainerHelper.newList (aArgs) + ")");
  }

  public void onUndeleteSuccess (@Nonnull final ObjectType aObjectType, @Nullable final String... aArgs)
  {
    s_aLogger.info ("undelete.success(" +
                    aObjectType.getObjectTypeName () +
                    "," +
                    ContainerHelper.newList (aArgs) +
                    ")");
  }

  public void onUndeleteFailure (@Nonnull final ObjectType aObjectType, @Nullable final String... aArgs)
  {
    s_aLogger.info ("undelete.failure(" +
                    aObjectType.getObjectTypeName () +
                    "," +
                    ContainerHelper.newList (aArgs) +
                    ")");
  }

  public void onExecuteSuccess (final String sWhat, @Nullable final String... aArgs)
  {
    s_aLogger.info ("execute.success(" + sWhat + "," + ContainerHelper.newList (aArgs) + ")");
  }

  public void onExecuteFailure (final String sWhat, @Nullable final String... aArgs)
  {
    s_aLogger.info ("execute.failure(" + sWhat + "," + ContainerHelper.newList (aArgs) + ")");
  }

  public void onExecuteSuccess (@Nonnull final ObjectType aObjectType,
                                final String sWhat,
                                @Nullable final String... aArgs)
  {
    s_aLogger.info ("execute.success(" +
                    aObjectType.getObjectTypeName () +
                    "," +
                    sWhat +
                    "," +
                    ContainerHelper.newList (aArgs) +
                    ")");
  }

  public void onExecuteFailure (@Nonnull final ObjectType aObjectType,
                                final String sWhat,
                                @Nullable final String... aArgs)
  {
    s_aLogger.info ("execute.failure(" +
                    aObjectType.getObjectTypeName () +
                    "," +
                    sWhat +
                    "," +
                    ContainerHelper.newList (aArgs) +
                    ")");
  }
}
