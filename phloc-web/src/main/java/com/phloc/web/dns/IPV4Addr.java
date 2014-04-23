/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.web.dns;

import java.net.InetAddress;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.IHasStringRepresentation;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.StringParser;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Representation of a single IP V4 address.
 * 
 * @author Philip Helger
 */
@Immutable
public class IPV4Addr implements IHasStringRepresentation
{
  private final int [] m_aIP = new int [4];

  private static int _validatePart (@Nonnegative final int n)
  {
    return ValueEnforcer.isBetweenInclusive (n, "IP part", 0, 255);
  }

  private static int _validatePart (@Nullable final String s)
  {
    return _validatePart (StringParser.parseInt (s, CGlobal.ILLEGAL_UINT));
  }

  public IPV4Addr (final InetAddress aAddress)
  {
    this (aAddress.getAddress ());
  }

  public IPV4Addr (final byte [] aAddressBytes)
  {
    this (aAddressBytes[0] & 0xff, aAddressBytes[1] & 0xff, aAddressBytes[2] & 0xff, aAddressBytes[3] & 0xff);
  }

  /**
   * Constructor that creates an IP address from the 4 numbers.
   * 
   * @param n1
   *        first number
   * @param n2
   *        second number
   * @param n3
   *        third number
   * @param n4
   *        fourth number
   */
  public IPV4Addr (@Nonnegative final int n1,
                   @Nonnegative final int n2,
                   @Nonnegative final int n3,
                   @Nonnegative final int n4)
  {
    m_aIP[0] = _validatePart (n1);
    m_aIP[1] = _validatePart (n2);
    m_aIP[2] = _validatePart (n3);
    m_aIP[3] = _validatePart (n4);
  }

  /**
   * @param sText
   *        The text interpretation of an IP address like "10.0.0.1".
   */
  public IPV4Addr (@Nonnull final String sText)
  {
    ValueEnforcer.notNull (sText, "Text");
    final String [] aParts = StringHelper.getExplodedArray ('.', sText);
    if (aParts.length != 4)
      throw new IllegalArgumentException ("Expected exactly 4 parts");
    for (int i = 0; i < 4; ++i)
      m_aIP[i] = _validatePart (aParts[i]);
  }

  @Nonnull
  @ReturnsMutableCopy
  public int [] getNumberParts ()
  {
    return ArrayHelper.getCopy (m_aIP);
  }

  /**
   * @return The ID address as a usual string (e.g. "10.0.0.1")
   */
  @Nonnull
  public String getAsString ()
  {
    return new StringBuilder (15).append (m_aIP[0])
                                 .append ('.')
                                 .append (m_aIP[1])
                                 .append ('.')
                                 .append (m_aIP[2])
                                 .append ('.')
                                 .append (m_aIP[3])
                                 .toString ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof IPV4Addr))
      return false;
    final IPV4Addr rhs = (IPV4Addr) o;
    return EqualsUtils.equals (m_aIP, rhs.m_aIP);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aIP).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ip", m_aIP).toString ();
  }
}
