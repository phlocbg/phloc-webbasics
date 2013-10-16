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
package com.phloc.web.smtp.queue;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import javax.mail.event.ConnectionListener;
import javax.mail.event.TransportListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.SystemProperties;
import com.phloc.commons.state.EChange;

/**
 * Global settings for the mail transport.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class MailTransportSettings
{
  public static final long DEFAULT_CONNECT_TIMEOUT_MILLISECS = 5 * CGlobal.MILLISECONDS_PER_SECOND;
  public static final long DEFAULT_TIMEOUT_MILLISECS = 10 * CGlobal.MILLISECONDS_PER_SECOND;

  private static final Logger s_aLogger = LoggerFactory.getLogger (MailTransportSettings.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  @GuardedBy ("s_aRWLock")
  private static long s_nConnectionTimeoutMilliSecs = DEFAULT_CONNECT_TIMEOUT_MILLISECS;
  @GuardedBy ("s_aRWLock")
  private static long s_nTimeoutMilliSecs = DEFAULT_TIMEOUT_MILLISECS;
  @GuardedBy ("s_aRWLock")
  private static ConnectionListener s_aConnectionListener;
  @GuardedBy ("s_aRWLock")
  private static TransportListener s_aTransportListener;

  private MailTransportSettings ()
  {}

  /**
   * Set the connection timeout in milliseconds. Values &le; 0 are interpreted
   * as indefinite timeout which is not recommended!
   * 
   * @param nMilliSecs
   *        The milliseconds timeout
   * @return {@link EChange}
   */
  @Nonnull
  public static EChange setConnectionTimeoutMilliSecs (final long nMilliSecs)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      if (s_nConnectionTimeoutMilliSecs == nMilliSecs)
        return EChange.UNCHANGED;
      if (nMilliSecs <= 0)
        s_aLogger.warn ("You are setting an indefinite connection timeout for the mail transport api: " + nMilliSecs);
      else
        s_aLogger.info ("Connection timeout for the mail transport api is set to " + nMilliSecs + " milliseconds");
      s_nConnectionTimeoutMilliSecs = nMilliSecs;
      return EChange.CHANGED;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Get the connection timeout in milliseconds.
   * 
   * @return If the value is &le; 0 than there should be no connection timeout.
   */
  @CheckForSigned
  public static long getConnectionTimeoutMilliSecs ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_nConnectionTimeoutMilliSecs;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Set the socket timeout in milliseconds. Values &le; 0 are interpreted as
   * indefinite timeout which is not recommended!
   * 
   * @param nMilliSecs
   *        The milliseconds timeout
   * @return {@link EChange}
   */
  @Nonnull
  public static EChange setTimeoutMilliSecs (final long nMilliSecs)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      if (s_nTimeoutMilliSecs == nMilliSecs)
        return EChange.UNCHANGED;
      if (nMilliSecs <= 0)
        s_aLogger.warn ("You are setting an indefinite socket timeout for the mail transport api: " + nMilliSecs);
      else
        s_aLogger.info ("Socket timeout for the mail transport api is set to " + nMilliSecs + " milliseconds");
      s_nTimeoutMilliSecs = nMilliSecs;
      return EChange.CHANGED;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Get the socket timeout in milliseconds.
   * 
   * @return If the value is &le; 0 than there should be no connection timeout.
   */
  @CheckForSigned
  public static long getTimeoutMilliSecs ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_nTimeoutMilliSecs;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Set a new mail connection listener.
   * 
   * @param aConnectionListener
   *        The new connection listener to set. May be <code>null</code>.
   */
  public static void setConnectionListener (@Nullable final ConnectionListener aConnectionListener)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aConnectionListener = aConnectionListener;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return The default mail connection listener. May be <code>null</code>.
   */
  @Nullable
  public static ConnectionListener getConnectionListener ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aConnectionListener;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Set a new mail transport listener.
   * 
   * @param aTransportListener
   *        The new transport listener to set. May be <code>null</code>.
   */
  public static void setTransportListener (@Nullable final TransportListener aTransportListener)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aTransportListener = aTransportListener;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return The default mail transport listener. May be <code>null</code>.
   */
  @Nullable
  public static TransportListener getTransportListener ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aTransportListener;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Enable or disable javax.mail debugging. By default debugging is disabled.
   * 
   * @param bDebug
   *        <code>true</code> to enabled debugging, <code>false</code> to
   *        disable it.
   */
  public static void enableJavaxMailDebugging (final boolean bDebug)
  {
    java.util.logging.Logger.getLogger ("com.sun.mail.smtp").setLevel (bDebug ? Level.FINEST : Level.INFO);
    java.util.logging.Logger.getLogger ("com.sun.mail.smtp.protocol").setLevel (bDebug ? Level.FINEST : Level.INFO);
    SystemProperties.setPropertyValue ("mail.socket.debug", Boolean.toString (bDebug));
    SystemProperties.setPropertyValue ("java.security.debug", bDebug ? "certpath" : null);
    SystemProperties.setPropertyValue ("javax.net.debug", bDebug ? "trustmanager" : null);
  }

  /**
   * @return <code>true</code> if javax.mail debugging is enabled,
   *         <code>false</code> if not.
   */
  public static boolean isJavaxMailDebuggingEnabled ()
  {
    return java.util.logging.Logger.getLogger ("com.sun.mail.smtp").getLevel ().equals (Level.FINEST);
  }
}
