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
package com.phloc.web.smtp;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import jakarta.mail.event.ConnectionListener;
import jakarta.mail.event.TransportListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.SystemProperties;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.LockedContainerHelper;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.web.smtp.transport.DoNothingEMailSendListener;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Global settings for the mail transport.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class EmailGlobalSettings
{
  public static final int DEFAULT_MAX_QUEUE_LENGTH = 500;
  public static final int DEFAULT_MAX_SEND_COUNT = 100;
  /** Don't use SSL by default */
  public static final boolean DEFAULT_USE_SSL = false;
  /** Don't use STARTTLS by default */
  public static final boolean DEFAULT_USE_STARTTLS = false;
  public static final long DEFAULT_CONNECT_TIMEOUT_MILLISECS = 5 * CGlobal.MILLISECONDS_PER_SECOND;
  public static final long DEFAULT_TIMEOUT_MILLISECS = 10 * CGlobal.MILLISECONDS_PER_SECOND;
  public static final boolean DEFAULT_REPORT_SUCCESS = false;

  private static final Logger LOG = LoggerFactory.getLogger (EmailGlobalSettings.class);
  private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock ();

  // Mail queue settings
  private static int s_nMaxMailQueueLen = DEFAULT_MAX_QUEUE_LENGTH;
  private static int s_nMaxMailSendCount = DEFAULT_MAX_SEND_COUNT;

  // SMTP connection settings
  private static boolean s_bUseSSL = DEFAULT_USE_SSL;
  private static boolean s_bUseSTARTTLS = DEFAULT_USE_STARTTLS;
  private static long s_nConnectionTimeoutMilliSecs = DEFAULT_CONNECT_TIMEOUT_MILLISECS;
  private static long s_nTimeoutMilliSecs = DEFAULT_TIMEOUT_MILLISECS;

  // Transport settings
  private static ConnectionListener s_aConnectionListener;
  private static TransportListener s_aTransportListener;
  private static IEmailDataTransportListener s_aEmailDataTransportListener;
  private static IEMailSendListener s_aEmailSendListener = new DoNothingEMailSendListener ();

  private static boolean s_bReportSuccess = DEFAULT_REPORT_SUCCESS;

  private static boolean s_bAllowNonVendorMailsInDebugOnLocalhost;

  private static List <String> s_aAllowedNonVendorDomains = ContainerHelper.newList ();

  private EmailGlobalSettings ()
  {}

  public static void setAllowNonVendorMailsInDebugOnLocalhost (final boolean bAllow)
  {
    s_bAllowNonVendorMailsInDebugOnLocalhost = bAllow;
  }

  public static boolean isAllowNonVendorMailsInDebugOnLocalhost ()
  {
    return s_bAllowNonVendorMailsInDebugOnLocalhost;
  }

  public static void setAllowedNonVendorDomains (final List <String> aAllowedNonVendorDomains)
  {
    LOCK.writeLock ().lock ();
    try
    {
      s_aAllowedNonVendorDomains.clear ();
      if (aAllowedNonVendorDomains != null)
      {
        s_aAllowedNonVendorDomains.addAll (aAllowedNonVendorDomains);
      }
    }
    finally
    {
      LOCK.writeLock ().unlock ();
    }
  }

  public static boolean isAllowedNonVendorDomain (final String sEmail)
  {
    LOCK.readLock ().lock ();
    try
    {
      for (final String sAllowedDomain : s_aAllowedNonVendorDomains)
      {
        if (StringHelper.endsWith (sEmail, '@' + sAllowedDomain))
        {
          return true;
        }
      }
      return false;
    }
    finally
    {
      LOCK.readLock ().unlock ();
    }
  }

  public static List <String> getAllowedNonVendorDomains ()
  {
    return LockedContainerHelper.getList (s_aAllowedNonVendorDomains, LOCK);
  }

  /**
   * Set mail queue settings. Changing these settings has no effect on existing
   * mail queues!
   * 
   * @param nMaxMailQueueLen
   *        The maximum number of mails that can be queued. Must be &gt; 0.
   * @param nMaxMailSendCount
   *        The maximum number of mails that are send out in one mail session.
   *        Must be &gt; 0 but &le; than {@link #getMaxMailQueueLength()}.
   * @return {@link EChange}.
   */
  @Nonnull
  public static EChange setMailQueueSize (@Nonnegative final int nMaxMailQueueLen,
                                          @Nonnegative final int nMaxMailSendCount)
  {
    ValueEnforcer.isGT0 (nMaxMailQueueLen, "MaxMailQueueLen");
    ValueEnforcer.isGT0 (nMaxMailSendCount, "MaxMailSendCount");
    if (nMaxMailSendCount > nMaxMailQueueLen)
      throw new IllegalArgumentException ("MaxMailQueueLen (" +
                                          nMaxMailQueueLen +
                                          ") must be >= than MaxMailSendCount (" +
                                          nMaxMailSendCount +
                                          ")");

    LOCK.writeLock ().lock ();
    try
    {
      if (nMaxMailQueueLen == s_nMaxMailQueueLen && nMaxMailSendCount == s_nMaxMailSendCount)
      {
        return EChange.UNCHANGED;
      }
      s_nMaxMailQueueLen = nMaxMailQueueLen;
      s_nMaxMailSendCount = nMaxMailSendCount;
      return EChange.CHANGED;
    }
    finally
    {
      LOCK.writeLock ().unlock ();
    }
  }

  /**
   * @return The maximum number of mails that can be queued. Always &gt; 0.
   */
  @Nonnegative
  public static int getMaxMailQueueLength ()
  {
    return s_nMaxMailQueueLen;
  }

  /**
   * @return The maximum number of mails that are send out in one mail session.
   *         Always &gt; 0 but &le; than {@link #getMaxMailQueueLength()}.
   */
  @Nonnegative
  public static int getMaxMailSendCount ()
  {
    return s_nMaxMailSendCount;
  }

  /**
   * Use SSL by default?
   * 
   * @param bUseSSL
   *        <code>true</code> to use it by default, <code>false</code> if not.
   * @return {@link EChange}
   */
  @Nonnull
  public static EChange setUseSSL (final boolean bUseSSL)
  {
    LOCK.writeLock ().lock ();
    try
    {
      if (s_bUseSSL == bUseSSL)
      {
        return EChange.UNCHANGED;
      }
      s_bUseSSL = bUseSSL;
      return EChange.CHANGED;
    }
    finally
    {
      LOCK.writeLock ().unlock ();
    }
  }

  /**
   * @return <code>true</code> to use SSL by default
   */
  public static boolean isUseSSL ()
  {
    return s_bUseSSL;
  }

  /**
   * Use STARTTLS by default?
   * 
   * @param bUseSTARTTLS
   *        <code>true</code> to use it by default, <code>false</code> if not.
   * @return {@link EChange}
   */
  @Nonnull
  public static EChange setUseSTARTTLS (final boolean bUseSTARTTLS)
  {
    LOCK.writeLock ().lock ();
    try
    {
      if (s_bUseSTARTTLS == bUseSTARTTLS)
      {
        return EChange.UNCHANGED;
      }
      s_bUseSTARTTLS = bUseSTARTTLS;
      return EChange.CHANGED;
    }
    finally
    {
      LOCK.writeLock ().unlock ();
    }
  }

  /**
   * @return <code>true</code> to use STARTTLS by default
   */
  public static boolean isUseSTARTTLS ()
  {
    return s_bUseSTARTTLS;
  }

  /**
   * Set the connection timeout in milliseconds. Values &le; 0 are interpreted
   * as indefinite timeout which is not recommended! Changing these settings has
   * no effect on existing mail queues!
   * 
   * @param nMilliSecs
   *        The milliseconds timeout
   * @return {@link EChange}
   */
  @Nonnull
  public static EChange setConnectionTimeoutMilliSecs (final long nMilliSecs)
  {
    LOCK.writeLock ().lock ();
    try
    {
      if (s_nConnectionTimeoutMilliSecs == nMilliSecs)
      {
        return EChange.UNCHANGED;
      }
      if (nMilliSecs <= 0)
      {
        LOG.warn ("You are setting an indefinite connection timeout for the mail transport api: {}", nMilliSecs);
      }
      s_nConnectionTimeoutMilliSecs = nMilliSecs;
      return EChange.CHANGED;
    }
    finally
    {
      LOCK.writeLock ().unlock ();
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
    return s_nConnectionTimeoutMilliSecs;
  }

  /**
   * Set the socket timeout in milliseconds. Values &le; 0 are interpreted as
   * indefinite timeout which is not recommended! Changing these settings has no
   * effect on existing mail queues!
   * 
   * @param nMilliSecs
   *        The milliseconds timeout
   * @return {@link EChange}
   */
  @Nonnull
  public static EChange setTimeoutMilliSecs (final long nMilliSecs)
  {
    LOCK.writeLock ().lock ();
    try
    {
      if (s_nTimeoutMilliSecs == nMilliSecs)
      {
        return EChange.UNCHANGED;
      }
      if (nMilliSecs <= 0)
      {
        LOG.warn ("You are setting an indefinite socket timeout for the mail transport api: {}", nMilliSecs);
      }
      s_nTimeoutMilliSecs = nMilliSecs;
      return EChange.CHANGED;
    }
    finally
    {
      LOCK.writeLock ().unlock ();
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
    return s_nTimeoutMilliSecs;
  }

  /**
   * Set a new mail connection listener. Changing these settings has no effect
   * on existing mail queues!
   * 
   * @param aConnectionListener
   *        The new connection listener to set. May be <code>null</code>.
   */
  public static void setConnectionListener (@Nullable final ConnectionListener aConnectionListener)
  {
    s_aConnectionListener = aConnectionListener;
  }

  /**
   * @return The default mail connection listener. May be <code>null</code>.
   */
  @Nullable
  public static ConnectionListener getConnectionListener ()
  {
    return s_aConnectionListener;
  }

  /**
   * Set a new mail transport listener. Changing these settings has no effect on
   * existing mail queues!
   * 
   * @param aTransportListener
   *        The new transport listener to set. May be <code>null</code>.
   */
  public static void setTransportListener (@Nullable final TransportListener aTransportListener)
  {
    s_aTransportListener = aTransportListener;
  }

  /**
   * @return The default mail transport listener. May be <code>null</code>.
   */
  @Nullable
  public static TransportListener getTransportListener ()
  {
    return s_aTransportListener;
  }

  /**
   * Set a new mail transport listener. Changing these settings has no effect on
   * existing mail queues!
   * 
   * @param aEmailDataTransportListener
   *        The new transport listener to set. May be <code>null</code>.
   */
  public static void setEmailDataTransportListener (@Nullable final IEmailDataTransportListener aEmailDataTransportListener)
  {
    s_aEmailDataTransportListener = aEmailDataTransportListener;
  }

  /**
   * @return The default mail transport listener. May be <code>null</code>.
   */
  @Nullable
  public static IEmailDataTransportListener getEmailDataTransportListener ()
  {
    return s_aEmailDataTransportListener;
  }

  public static void setEmailSendListener (@Nullable final IEMailSendListener aEmailSendListener)
  {
    s_aEmailSendListener = aEmailSendListener == null ? new DoNothingEMailSendListener () : aEmailSendListener;
  }

  @Nonnull
  public static IEMailSendListener getEmailSendListener ()
  {
    return s_aEmailSendListener;
  }

  public static void setReportSuccess (final boolean bReportSuccess)
  {
    s_bReportSuccess = bReportSuccess;
  }

  @Nullable
  public static boolean isReportSuccess ()
  {
    return s_bReportSuccess;
  }

  /**
   * Enable or disable jakarta.mail debugging. By default debugging is disabled.
   * 
   * @param bDebug
   *        <code>true</code> to enabled debugging, <code>false</code> to
   *        disable it.
   */
  @SuppressFBWarnings ("LG_LOST_LOGGER_DUE_TO_WEAK_REFERENCE")
  public static void enableJavaxMailDebugging (final boolean bDebug)
  {
    java.util.logging.Logger.getLogger ("com.sun.mail.smtp").setLevel (bDebug ? Level.FINEST : Level.INFO);
    java.util.logging.Logger.getLogger ("com.sun.mail.smtp.protocol").setLevel (bDebug ? Level.FINEST : Level.INFO);
    SystemProperties.setPropertyValue ("mail.socket.debug", Boolean.toString (bDebug));
    SystemProperties.setPropertyValue ("java.security.debug", bDebug ? "certpath" : null);
    SystemProperties.setPropertyValue ("javax.net.debug", bDebug ? "trustmanager" : null);
  }

  /**
   * @return <code>true</code> if jakarta.mail debugging is enabled,
   *         <code>false</code> if not.
   */
  public static boolean isJavaxMailDebuggingEnabled ()
  {
    return java.util.logging.Logger.getLogger ("com.sun.mail.smtp").getLevel ().equals (Level.FINEST);
  }
}
