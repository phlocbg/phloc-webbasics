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

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.PresentForCodeCoverage;

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
   */
  public static void setResponseCompressionEnabled (final boolean bResponseCompressionEnabled)
  {
    s_bResponseCompressionEnabled = bResponseCompressionEnabled;
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
   */
  public static void setResponseGzipEnabled (final boolean bResponseGzipEnabled)
  {
    s_bResponseGzipEnabled = bResponseGzipEnabled;
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
   */
  public static void setResponseDeflateEnabled (final boolean bResponseDeflateEnabled)
  {
    s_bResponseDeflateEnabled = bResponseDeflateEnabled;
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
   */
  public static void setExpirationSeconds (final int nExpirationSeconds)
  {
    s_nExpirationSeconds = nExpirationSeconds;
  }

  /**
   * @return The default expiration seconds for objects to be cached
   */
  public static int getExpirationSeconds ()
  {
    return s_nExpirationSeconds;
  }
}
