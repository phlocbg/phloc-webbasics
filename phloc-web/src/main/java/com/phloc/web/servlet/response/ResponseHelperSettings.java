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
package com.phloc.web.servlet.response;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.state.EChange;
import com.phloc.datetime.PDTFactory;
import com.phloc.web.datetime.PDTWebDateUtils;

/**
 * Contains the settings for the {@link ResponseHelper} class.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class ResponseHelperSettings
{
  /** A special date that never expires */
  public static final LocalDateTime EXPIRES_NEVER_DATETIME = PDTFactory.createLocalDateTime (1995,
                                                                                             DateTimeConstants.MAY,
                                                                                             6,
                                                                                             12,
                                                                                             0,
                                                                                             0);

  /** The string representation of never expires date */
  public static final String EXPIRES_NEVER_STRING = PDTWebDateUtils.getAsStringRFC822 (EXPIRES_NEVER_DATETIME);

  /**
   * Expires in at least 2 days (which is the minimum to be accepted for real
   * caching in Yahoo Guidelines).
   */
  public static final int DEFAULT_EXPIRATION_SECONDS = 7 * CGlobal.SECONDS_PER_DAY;

  public static final boolean DEFAULT_RESPONSE_COMPRESSION_ENABLED = true;
  public static final boolean DEFAULT_RESPONSE_GZIP_ENABLED = true;
  public static final boolean DEFAULT_RESPONSE_DERFLATE_ENABLED = true;
  public static final boolean DEFAULT_FORCE_FILENAME_RFC5987 = false;

  private static final Logger s_aLogger = LoggerFactory.getLogger (ResponseHelperSettings.class);

  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  private static int s_nExpirationSeconds = DEFAULT_EXPIRATION_SECONDS;
  private static boolean s_bResponseCompressionEnabled = DEFAULT_RESPONSE_COMPRESSION_ENABLED;
  private static boolean s_bResponseGzipEnabled = DEFAULT_RESPONSE_GZIP_ENABLED;
  private static boolean s_bResponseDeflateEnabled = DEFAULT_RESPONSE_DERFLATE_ENABLED;
  private static boolean s_bForceFilenameRFC5987 = DEFAULT_FORCE_FILENAME_RFC5987;

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final ResponseHelperSettings s_aInstance = new ResponseHelperSettings ();

  private ResponseHelperSettings ()
  {}

  /**
   * Enable or disable the overall compression.
   * 
   * @param bResponseCompressionEnabled
   *        <code>true</code> to enable it, <code>false</code> to disable it
   * @return {@link EChange}
   */
  @Nonnull
  public static EChange setResponseCompressionEnabled (final boolean bResponseCompressionEnabled)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      if (s_bResponseCompressionEnabled == bResponseCompressionEnabled)
        return EChange.UNCHANGED;
      s_bResponseCompressionEnabled = bResponseCompressionEnabled;
      s_aLogger.info ("ResponseHelper responseCompressionEnabled=" + bResponseCompressionEnabled);
      return EChange.CHANGED;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return <code>true</code> if overall compression is enabled,
   *         <code>false</code> if not
   */
  public static boolean isResponseCompressionEnabled ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_bResponseCompressionEnabled;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Enable or disable Gzip compression. This only has an effect if
   * {@link #isResponseCompressionEnabled()} is <code>true</code>
   * 
   * @param bResponseGzipEnabled
   *        <code>true</code> to enable it, <code>false</code> to disable it
   * @return {@link EChange}
   */
  @Nonnull
  public static EChange setResponseGzipEnabled (final boolean bResponseGzipEnabled)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      if (s_bResponseGzipEnabled == bResponseGzipEnabled)
        return EChange.UNCHANGED;
      s_bResponseGzipEnabled = bResponseGzipEnabled;
      s_aLogger.info ("ResponseHelper responseGzipEnabled=" + bResponseGzipEnabled);
      return EChange.CHANGED;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return <code>true</code> if GZip compression is enabled,
   *         <code>false</code> if not
   */
  public static boolean isResponseGzipEnabled ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_bResponseGzipEnabled;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Enable or disable Deflate compression. This only has an effect if
   * {@link #isResponseCompressionEnabled()} is <code>true</code>
   * 
   * @param bResponseDeflateEnabled
   *        <code>true</code> to enable it, <code>false</code> to disable it
   * @return {@link EChange}
   */
  @Nonnull
  public static EChange setResponseDeflateEnabled (final boolean bResponseDeflateEnabled)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      if (s_bResponseDeflateEnabled == bResponseDeflateEnabled)
        return EChange.UNCHANGED;
      s_bResponseDeflateEnabled = bResponseDeflateEnabled;
      s_aLogger.info ("ResponseHelper responseDeflateEnabled=" + bResponseDeflateEnabled);
      return EChange.CHANGED;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return <code>true</code> if Deflate compression is enabled,
   *         <code>false</code> if not
   */
  public static boolean isResponseDeflateEnabled ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_bResponseDeflateEnabled;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Set all parameters at once as an atomic transaction
   * 
   * @param bResponseCompressionEnabled
   *        <code>true</code> to overall enable the usage
   * @param bResponseGzipEnabled
   *        <code>true</code> to enable GZip if compression is enabled
   * @param bResponseDeflateEnabled
   *        <code>true</code> to enable Deflate if compression is enabled
   * @return {@link EChange}
   */
  @Nonnull
  public static EChange setAll (final boolean bResponseCompressionEnabled,
                                final boolean bResponseGzipEnabled,
                                final boolean bResponseDeflateEnabled)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      EChange eChange = EChange.UNCHANGED;
      if (s_bResponseCompressionEnabled != bResponseCompressionEnabled)
      {
        s_bResponseCompressionEnabled = bResponseCompressionEnabled;
        eChange = EChange.CHANGED;
        s_aLogger.info ("ResponseHelper responseCompressEnabled=" + bResponseCompressionEnabled);
      }
      if (s_bResponseGzipEnabled != bResponseGzipEnabled)
      {
        s_bResponseGzipEnabled = bResponseGzipEnabled;
        eChange = EChange.CHANGED;
        s_aLogger.info ("ResponseHelper responseGzipEnabled=" + bResponseGzipEnabled);
      }
      if (s_bResponseDeflateEnabled != bResponseDeflateEnabled)
      {
        s_bResponseDeflateEnabled = bResponseDeflateEnabled;
        eChange = EChange.CHANGED;
        s_aLogger.info ("ResponseHelper responseDeflateEnabled=" + bResponseDeflateEnabled);
      }
      return eChange;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Set the default expiration settings to be used for objects that should use
   * HTTP caching
   * 
   * @param nExpirationSeconds
   *        The number of seconds for which the response should be cached
   * @return {@link EChange}
   */
  @Nonnull
  public static EChange setExpirationSeconds (final int nExpirationSeconds)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      if (s_nExpirationSeconds == nExpirationSeconds)
        return EChange.UNCHANGED;
      s_nExpirationSeconds = nExpirationSeconds;
      s_aLogger.info ("ResponseHelper expirationSeconds=" + nExpirationSeconds);
      return EChange.CHANGED;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return The default expiration seconds for objects to be cached
   */
  public static int getExpirationSeconds ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_nExpirationSeconds;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  public static EChange setForceFilenameRFC5987 (final boolean bForceFilenameRFC5987)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      if (s_bForceFilenameRFC5987 == bForceFilenameRFC5987)
        return EChange.UNCHANGED;
      s_bForceFilenameRFC5987 = bForceFilenameRFC5987;
      s_aLogger.info ("ResponseHelper ForceFilenameRFC5987=" + bForceFilenameRFC5987); //$NON-NLS-1$
      return EChange.CHANGED;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Option for workaround of content disposition problem with tomcat
   * (https://bz.apache.org/bugzilla/show_bug.cgi?id=66512)
   * 
   * @return <code>true</code> if the filename will be using the same value as
   *         the corresponding RFC5987 filename* on content disposition
   *         <code>false</code> if not
   */
  public static boolean isForceFilenameRFC5987 ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_bForceFilenameRFC5987;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }
}
