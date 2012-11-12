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

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.type.ObjectType;

/**
 * Simplify auditing calls.
 * 
 * @author philip
 */
@ThreadSafe
public final class AuditUtils
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AuditUtils.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  private static IAuditor s_aAuditor;

  private AuditUtils ()
  {}

  @Nonnull
  private static IAuditor _getAuditor ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      if (s_aAuditor == null)
        throw new IllegalStateException ("No auditor has been provided!");
      return s_aAuditor;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return <code>true</code> if an auditor is set, <code>false</code> if not.
   */
  public static boolean isAuditorSet ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aAuditor != null;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Set the global auditor to use.
   * 
   * @param aAuditor
   *        The auditor to be set. May be <code>null</code> to indicate that no
   *        global auditor is available.
   */
  public static void setAuditor (@Nullable final IAuditor aAuditor)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      if (s_aAuditor != null && aAuditor != null)
        s_aLogger.warn ("Overwriting auditor " + s_aAuditor + " with " + aAuditor);
      s_aAuditor = aAuditor;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  public static void onAuditCreateSuccess (@Nonnull final ObjectType aObjectType)
  {
    _getAuditor ().onCreateSuccess (aObjectType);
  }

  public static void onAuditCreateSuccess (@Nonnull final ObjectType aObjectType, @Nullable final String... aArgs)
  {
    _getAuditor ().onCreateSuccess (aObjectType, aArgs);
  }

  @SuppressWarnings ("unused")
  @Deprecated
  @UnsupportedOperation
  public static void onAuditCreateFailure (@Nonnull final ObjectType aObjectType)
  {
    throw new UnsupportedOperationException ();
  }

  public static void onAuditCreateFailure (@Nonnull final ObjectType aObjectType, @Nullable final String... aArgs)
  {
    _getAuditor ().onCreateFailure (aObjectType, aArgs);
  }

  @SuppressWarnings ("unused")
  @Deprecated
  @UnsupportedOperation
  public static void onAuditModifySuccess (@Nonnull final ObjectType aObjectType, @Nonnull final String sWhat)
  {
    throw new UnsupportedOperationException ();
  }

  public static void onAuditModifySuccess (@Nonnull final ObjectType aObjectType,
                                           @Nonnull final String sWhat,
                                           @Nullable final String... aArgs)
  {
    _getAuditor ().onModifySuccess (aObjectType, sWhat, aArgs);
  }

  @SuppressWarnings ("unused")
  @Deprecated
  @UnsupportedOperation
  public static void onAuditModifyFailure (@Nonnull final ObjectType aObjectType, @Nonnull final String sWhat)
  {
    throw new UnsupportedOperationException ();
  }

  public static void onAuditModifyFailure (@Nonnull final ObjectType aObjectType,
                                           @Nonnull final String sWhat,
                                           @Nullable final String... aArgs)
  {
    _getAuditor ().onModifyFailure (aObjectType, sWhat, aArgs);
  }

  @SuppressWarnings ("unused")
  @Deprecated
  @UnsupportedOperation
  public static void onAuditDeleteSuccess (@Nonnull final ObjectType aObjectType)
  {
    throw new UnsupportedOperationException ();
  }

  public static void onAuditDeleteSuccess (@Nonnull final ObjectType aObjectType, @Nullable final String... aArgs)
  {
    _getAuditor ().onDeleteSuccess (aObjectType, aArgs);
  }

  @SuppressWarnings ("unused")
  @Deprecated
  @UnsupportedOperation
  public static void onAuditDeleteFailure (@Nonnull final ObjectType aObjectType)
  {
    throw new UnsupportedOperationException ();
  }

  public static void onAuditDeleteFailure (@Nonnull final ObjectType aObjectType, @Nullable final String... aArgs)
  {
    _getAuditor ().onDeleteFailure (aObjectType, aArgs);
  }

  public static void onAuditExecuteSuccess (@Nonnull final String sWhat, @Nullable final String... aArgs)
  {
    _getAuditor ().onExecuteSuccess (sWhat, aArgs);
  }

  public static void onAuditExecuteFailure (@Nonnull final String sWhat, @Nullable final String... aArgs)
  {
    _getAuditor ().onExecuteFailure (sWhat, aArgs);
  }

  public static void onAuditExecuteSuccess (@Nonnull final ObjectType aObjectType,
                                            @Nonnull final String sWhat,
                                            @Nullable final String... aArgs)
  {
    _getAuditor ().onExecuteSuccess (aObjectType, sWhat, aArgs);
  }

  public static void onAuditExecuteFailure (@Nonnull final ObjectType aObjectType,
                                            @Nonnull final String sWhat,
                                            @Nullable final String... aArgs)
  {
    _getAuditor ().onExecuteFailure (aObjectType, sWhat, aArgs);
  }
}
