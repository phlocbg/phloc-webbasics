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
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.mime.MimeType;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Represents a list of Accept HTTP header values
 * 
 * @author philip
 */
@NotThreadSafe
public final class AcceptMimeTypeList
{
  // Maps MIME types to quality
  private final Map <IMimeType, QValue> m_aMap = new LinkedHashMap <IMimeType, QValue> ();

  public AcceptMimeTypeList ()
  {}

  public void addMimeType (@Nonnull final IMimeType aMimeType, @Nonnegative final double dQuality)
  {
    if (aMimeType == null)
      throw new NullPointerException ("charset name is empty");
    m_aMap.put (aMimeType, new QValue (dQuality));
  }

  /**
   * Return the associated quality of the given MIME type using the fallback
   * mechanism.
   * 
   * @param aMimeType
   *        The charset name to query. May not be <code>null</code>.
   * @return The {@link QValue} of the mime type
   */
  @Nonnull
  public QValue getQValueOfMimeType (@Nonnull final IMimeType aMimeType)
  {
    if (aMimeType == null)
      throw new NullPointerException ("mimeType");
    QValue aQuality = m_aMap.get (aMimeType);
    if (aQuality == null)
    {
      // Check for "contenttype/*"
      aQuality = m_aMap.get (aMimeType.getContentType ().buildMimeType ("*"));
      if (aQuality == null)
      {
        // If not explicitly given, check for "*"
        aQuality = m_aMap.get (AcceptMimeTypeHandler.ANY_MIMETYPE);
        if (aQuality == null)
        {
          // Neither charset nor "*" nor "*/*" is present
          return QValue.MIN_QVALUE;
        }
      }
    }
    return aQuality;
  }

  public double getQualityOfMimeType (@Nonnull final String sMimeType)
  {
    return getQualityOfMimeType (MimeType.parseFromStringWithoutEncoding (sMimeType));
  }

  /**
   * Return the associated quality of the given MIME type using the fallback
   * mechanism.
   * 
   * @param aMimeType
   *        The charset name to query. May not be <code>null</code>.
   * @return 0 means not accepted, 1 means fully accepted.
   */
  public double getQualityOfMimeType (@Nonnull final IMimeType aMimeType)
  {
    return getQValueOfMimeType (aMimeType).getQuality ();
  }

  /**
   * Check if the passed MIME type is supported, incl. fallback handling
   * 
   * @param sMimeType
   *        The MIME type to check
   * @return <code>true</code> if it is supported, <code>false</code> if not
   */
  public boolean supportsMimeType (@Nonnull final String sMimeType)
  {
    return supportsMimeType (MimeType.parseFromStringWithoutEncoding (sMimeType));
  }

  /**
   * Check if the passed MIME type is supported, incl. fallback handling
   * 
   * @param aMimeType
   *        The MIME type to check
   * @return <code>true</code> if it is supported, <code>false</code> if not
   */
  public boolean supportsMimeType (@Nonnull final IMimeType aMimeType)
  {
    return getQValueOfMimeType (aMimeType).isAboveMinimumQuality ();
  }

  /**
   * Check if the passed MIME type is supported, without considering fallback
   * MIME types (xxx/*)
   * 
   * @param sMimeType
   *        The MIME type to check
   * @return <code>true</code> if it is supported, <code>false</code> if not
   */
  public boolean explicitlySupportsMimeType (@Nonnull final String sMimeType)
  {
    return explicitlySupportsMimeType (MimeType.parseFromStringWithoutEncoding (sMimeType));
  }

  /**
   * Check if the passed MIME type is supported, without considering fallback
   * MIME types (xxx/*)
   * 
   * @param aMimeType
   *        The MIME type to check
   * @return <code>true</code> if it is supported, <code>false</code> if not
   */
  public boolean explicitlySupportsMimeType (@Nonnull final IMimeType aMimeType)
  {
    final QValue aQuality = m_aMap.get (aMimeType);
    return aQuality != null && aQuality.isAboveMinimumQuality ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("map", m_aMap).toString ();
  }
}
