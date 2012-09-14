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
package com.phloc.webbasics.http;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Contains a list of AcceptEncoding values as specified by the HTTP header
 * 
 * @author philip
 */
@NotThreadSafe
public final class AcceptEncodingList
{
  // Maps charset names to quality
  private final Map <String, QValue> m_aMap = new LinkedHashMap <String, QValue> ();

  public AcceptEncodingList ()
  {}

  @Nonnull
  private static String _unify (@Nonnull final String sEncoding)
  {
    return sEncoding.toLowerCase (Locale.US);
  }

  public void addEncoding (@Nonnull final String sEncoding, @Nonnegative final double dQuality)
  {
    if (StringHelper.hasNoText (sEncoding))
      throw new IllegalArgumentException ("encoding is empty");
    m_aMap.put (_unify (sEncoding), new QValue (dQuality));
  }

  /**
   * Return the associated quality of the given charset.
   * 
   * @param sEncoding
   *        The charset name to query. May not be <code>null</code>.
   * @return The matching {@link QValue} and never <code>null</code>.
   */
  @Nonnull
  public QValue getQValueOfEncoding (@Nonnull final String sEncoding)
  {
    if (sEncoding == null)
      throw new NullPointerException ("encoding");

    // Direct search encoding
    QValue aQuality = m_aMap.get (_unify (sEncoding));
    if (aQuality == null)
    {
      // If not explicitly given, check for "*"
      aQuality = m_aMap.get (AcceptEncodingHandler.ANY_ENCODING);
      if (aQuality == null)
      {
        // Neither encoding nor "*" is present
        // -> assume minimum quality
        return QValue.MIN_QVALUE;
      }
    }
    return aQuality;
  }

  /**
   * Return the associated quality of the given charset.
   * 
   * @param sEncoding
   *        The charset name to query. May not be <code>null</code>.
   * @return 0 means not accepted, 1 means fully accepted.
   */
  public double getQualityOfEncoding (@Nonnull final String sEncoding)
  {
    return getQValueOfEncoding (sEncoding).getQuality ();
  }

  public boolean supportsEncoding (@Nonnull final String sEncoding)
  {
    return getQValueOfEncoding (sEncoding).isAboveMinimumQuality ();
  }

  public boolean explicitlySupportsEncoding (@Nonnull final String sEncoding)
  {
    if (sEncoding == null)
      throw new NullPointerException ("encoding");

    final QValue aQuality = m_aMap.get (_unify (sEncoding));
    return aQuality != null && aQuality.isAboveMinimumQuality ();
  }

  public boolean supportsGZIP ()
  {
    return supportsEncoding (AcceptEncodingHandler.GZIP_ENCODING) ||
           supportsEncoding (AcceptEncodingHandler.X_GZIP_ENCODING);
  }

  /**
   * @return the accepted GZip encoding. May either be
   *         {@link AcceptEncodingHandler#GZIP_ENCODING} or
   *         {@link AcceptEncodingHandler#X_GZIP_ENCODING} or <code>null</code>
   */
  @Nullable
  public String getUsedGZIPEncoding ()
  {
    if (supportsEncoding (AcceptEncodingHandler.GZIP_ENCODING))
      return AcceptEncodingHandler.GZIP_ENCODING;
    if (supportsEncoding (AcceptEncodingHandler.X_GZIP_ENCODING))
      return AcceptEncodingHandler.X_GZIP_ENCODING;
    return null;
  }

  public boolean supportsDeflate ()
  {
    return supportsEncoding (AcceptEncodingHandler.DEFLATE_ENCODING);
  }

  /**
   * @return the accepted GZip encoding. May either be
   *         {@link AcceptEncodingHandler#DEFLATE_ENCODING} or <code>null</code>
   */
  @Nullable
  public String getUsedDeflateEncoding ()
  {
    if (supportsEncoding (AcceptEncodingHandler.DEFLATE_ENCODING))
      return AcceptEncodingHandler.DEFLATE_ENCODING;
    return null;
  }

  public boolean supportsCompress ()
  {
    return supportsEncoding (AcceptEncodingHandler.COMPRESS_ENCODING) ||
           supportsEncoding (AcceptEncodingHandler.X_COMPRESS_ENCODING);
  }

  /**
   * @return the accepted GZip encoding. May either be
   *         {@link AcceptEncodingHandler#COMPRESS_ENCODING} or
   *         {@link AcceptEncodingHandler#X_COMPRESS_ENCODING} or
   *         <code>null</code>
   */
  @Nullable
  public String getUsedCompressEncoding ()
  {
    if (supportsEncoding (AcceptEncodingHandler.COMPRESS_ENCODING))
      return AcceptEncodingHandler.COMPRESS_ENCODING;
    if (supportsEncoding (AcceptEncodingHandler.X_COMPRESS_ENCODING))
      return AcceptEncodingHandler.X_COMPRESS_ENCODING;
    return null;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("map", m_aMap).toString ();
  }
}
