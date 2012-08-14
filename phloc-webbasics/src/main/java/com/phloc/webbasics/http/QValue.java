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

import java.io.Serializable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Represents the quality value of an HTTP Accept* header.
 * 
 * @author philip
 */
@Immutable
public final class QValue implements Comparable <QValue>, Serializable
{
  public static final double MIN_QUALITY = 0;
  public static final double MAX_QUALITY = 1;
  private static final Logger s_aLogger = LoggerFactory.getLogger (QValue.class);

  private final double m_dQuality;

  public QValue (@Nonnegative final double dQuality)
  {
    if (dQuality < MIN_QUALITY)
      s_aLogger.warn ("QValue is too small: " + dQuality);
    else
      if (dQuality > MAX_QUALITY)
        s_aLogger.warn ("QValue is too large: " + dQuality);
    m_dQuality = dQuality < MIN_QUALITY ? MIN_QUALITY : dQuality > MAX_QUALITY ? MAX_QUALITY : dQuality;
  }

  @Nonnegative
  public double getQuality ()
  {
    return m_dQuality;
  }

  public int compareTo (@Nonnull final QValue rhs)
  {
    return Double.compare (m_dQuality, rhs.m_dQuality);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof QValue))
      return false;
    final QValue rhs = (QValue) o;
    return EqualsUtils.equals (m_dQuality, rhs.m_dQuality);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_dQuality).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("quality", m_dQuality).toString ();
  }
}
