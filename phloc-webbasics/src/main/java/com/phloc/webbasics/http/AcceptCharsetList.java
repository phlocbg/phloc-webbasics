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

import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Represents a list of AcceptCharset values as specified in the HTTP header
 * 
 * @author philip
 */
public final class AcceptCharsetList
{
  // Maps charset names to quality
  private final Map <String, QValue> m_aMap = new LinkedHashMap <String, QValue> ();

  public AcceptCharsetList ()
  {}

  @Nonnull
  private static String _unify (@Nonnull final String sCharset)
  {
    return sCharset.toLowerCase (Locale.US);
  }

  public void addCharset (@Nonnull final String sCharset, @Nonnegative final double dQuality)
  {
    if (StringHelper.hasNoText (sCharset))
      throw new IllegalArgumentException ("charset name is empty");
    m_aMap.put (_unify (sCharset), new QValue (dQuality));
  }

  /**
   * Return the associated quality of the given charset.
   * 
   * @param sCharset
   *        The charset name to query. May not be <code>null</code>.
   * @return 0 means not accepted, 1 means fully accepted.
   */
  public double getQualityOfCharset (@Nonnull final String sCharset)
  {
    if (sCharset == null)
      throw new NullPointerException ("charset");

    QValue aQuality = m_aMap.get (_unify (sCharset));
    if (aQuality == null)
    {
      // If not explicitly given, check for "*"
      aQuality = m_aMap.get (AcceptCharsetHandler.ANY_CHARSET);
      if (aQuality == null)
      {
        // Neither charset nor "*" is present
        return sCharset.equals (AcceptCharsetHandler.DEFAULT_CHARSET) ? QValue.MAX_QUALITY : QValue.MIN_QUALITY;
      }
    }
    return aQuality.getQuality ();
  }

  public boolean supportsCharset (@Nonnull final String sCharset)
  {
    return getQualityOfCharset (sCharset) > QValue.MIN_QUALITY;
  }

  public boolean explicitlySupportsCharset (@Nonnull final String sCharset)
  {
    if (sCharset == null)
      throw new NullPointerException ("charset");

    final QValue aQuality = m_aMap.get (_unify (sCharset));
    return aQuality != null && aQuality.getQuality () > QValue.MIN_QUALITY;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("map", m_aMap).toString ();
  }
}
