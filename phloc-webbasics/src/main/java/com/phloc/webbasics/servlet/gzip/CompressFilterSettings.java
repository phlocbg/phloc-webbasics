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
package com.phloc.webbasics.servlet.gzip;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;

/**
 * Contains the settings for the {@link CompressFilter} class.
 * 
 * @author philip
 */
@Immutable
public final class CompressFilterSettings
{
  private static boolean s_bResponseCompressionEnabled = true;
  private static boolean s_bResponseGzipEnabled = true;
  private static boolean s_bResponseDeflateEnabled = true;

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final CompressFilterSettings s_aInstance = new CompressFilterSettings ();

  private CompressFilterSettings ()
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
}
