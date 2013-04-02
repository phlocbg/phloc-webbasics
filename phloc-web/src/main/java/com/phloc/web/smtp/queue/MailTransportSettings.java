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

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.mail.event.ConnectionListener;
import javax.mail.event.TransportListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.state.EChange;

/**
 * Settings for the mail transport.
 * 
 * @author philip
 */
@ThreadSafe
public final class MailTransportSettings
{
  public static final long DEFAULT_CONNECT_TIMEOUT_MILLISECS = 5 * CGlobal.MILLISECONDS_PER_SECOND;
  public static final long DEFAULT_TIMEOUT_MILLISECS = 10 * CGlobal.MILLISECONDS_PER_SECOND;

  private static final Logger s_aLogger = LoggerFactory.getLogger (MailTransportSettings.class);
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  private static long s_nConnectTimeoutMilliSecs = DEFAULT_CONNECT_TIMEOUT_MILLISECS;
  private static long s_nTimeoutMilliSecs = DEFAULT_TIMEOUT_MILLISECS;
  private static ConnectionListener s_aConnectionListener = null;
  private static TransportListener s_aTransportListener = null;

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
  public static EChange setConnectTimeoutMilliSecs (final long nMilliSecs)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      if (s_nConnectTimeoutMilliSecs == nMilliSecs)
        return EChange.UNCHANGED;
      if (nMilliSecs <= 0)
        s_aLogger.warn ("You are setting an indefinite connection timeout for the mail transport api: " + nMilliSecs);
      else
        s_aLogger.info ("Connection timeout for the mail transport api is set to " + nMilliSecs + " milliseconds");
      s_nConnectTimeoutMilliSecs = nMilliSecs;
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
  public static long getConnectTimeoutMilliSecs ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_nConnectTimeoutMilliSecs;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Set the socket timeout in milliseconds. Values &le; 0 are interpreted as
   * indefinite timeout which is not recommeneded!
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
}
