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
package com.phloc.webbasics.web;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.state.EChange;

/**
 * Contains the settings for the {@link ResponseHelper} class.
 * 
 * @author philip
 */
@Immutable
public final class ResponseHelperSettings
{
  // Expires in at least 2 days (which is the minimum to be accepted for
  // real caching in Yahoo Guidelines)
  // Because of steady changes, use 1 hour
  public static final int DEFAULT_EXPIRATION_SECONDS = 1 * CGlobal.SECONDS_PER_HOUR;

  private static int s_nExpirationSeconds = DEFAULT_EXPIRATION_SECONDS;
  private static boolean s_bResponseCompressionEnabled = true;
  private static boolean s_bResponseGzipEnabled = true;
  private static boolean s_bResponseDeflateEnabled = true;

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
    if (s_bResponseCompressionEnabled == bResponseCompressionEnabled)
      return EChange.UNCHANGED;
    s_bResponseCompressionEnabled = bResponseCompressionEnabled;
    return EChange.CHANGED;
  }

  /**
   * @return <code>true</code> if overall compression is enabled,
   *         <code>false</code> if not
   */
  public static boolean isResponseCompressionEnabled ()
  {
    return s_bResponseCompressionEnabled;
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
    if (s_bResponseGzipEnabled == bResponseGzipEnabled)
      return EChange.UNCHANGED;
    s_bResponseGzipEnabled = bResponseGzipEnabled;
    return EChange.CHANGED;
  }

  /**
   * @return <code>true</code> if GZip compression is enabled,
   *         <code>false</code> if not
   */
  public static boolean isResponseGzipEnabled ()
  {
    return s_bResponseGzipEnabled;
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
    if (s_bResponseDeflateEnabled == bResponseDeflateEnabled)
      return EChange.UNCHANGED;
    s_bResponseDeflateEnabled = bResponseDeflateEnabled;
    return EChange.CHANGED;
  }

  /**
   * @return <code>true</code> if Deflate compression is enabled,
   *         <code>false</code> if not
   */
  public static boolean isResponseDeflateEnabled ()
  {
    return s_bResponseDeflateEnabled;
  }

  /**
   * Set the default expiration settings to be used for objects that should use
   * HTTP caching
   * 
   * @param nExpirationSeconds
   *        The number of seconds for which the response shdould be cache
   * @return {@link EChange}
   */
  @Nonnull
  public static EChange setExpirationSeconds (final int nExpirationSeconds)
  {
    if (s_nExpirationSeconds == nExpirationSeconds)
      return EChange.UNCHANGED;
    s_nExpirationSeconds = nExpirationSeconds;
    return EChange.CHANGED;
  }

  /**
   * @return The default expiration seconds for objects to be cached
   */
  public static int getExpirationSeconds ()
  {
    return s_nExpirationSeconds;
  }
}
