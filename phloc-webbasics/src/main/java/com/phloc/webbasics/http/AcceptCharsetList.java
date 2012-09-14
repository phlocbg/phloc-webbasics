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

import java.util.Locale;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.string.StringHelper;

/**
 * Represents a list of Accept-Charset values as specified in the HTTP header
 * 
 * @author philip
 */
public final class AcceptCharsetList extends AbstractQValueList <String>
{
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
   * @return The associated {@link QValue}.
   */
  @Nonnull
  public QValue getQValueOfCharset (@Nonnull final String sCharset)
  {
    if (sCharset == null)
      throw new NullPointerException ("charset");

    // Find charset direct
    QValue aQuality = m_aMap.get (_unify (sCharset));
    if (aQuality == null)
    {
      // If not explicitly given, check for "*"
      aQuality = m_aMap.get (AcceptCharsetHandler.ANY_CHARSET);
      if (aQuality == null)
      {
        // Neither charset nor "*" is present
        return sCharset.equals (AcceptCharsetHandler.DEFAULT_CHARSET) ? QValue.MAX_QVALUE : QValue.MIN_QVALUE;
      }
    }
    return aQuality;
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
    return getQValueOfCharset (sCharset).getQuality ();
  }

  public boolean supportsCharset (@Nonnull final String sCharset)
  {
    return getQValueOfCharset (sCharset).isAboveMinimumQuality ();
  }

  public boolean explicitlySupportsCharset (@Nonnull final String sCharset)
  {
    if (sCharset == null)
      throw new NullPointerException ("charset");

    final QValue aQuality = m_aMap.get (_unify (sCharset));
    return aQuality != null && aQuality.isAboveMinimumQuality ();
  }
}
