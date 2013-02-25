/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.web.http;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Represents a base class for all QValue'd stuff
 * 
 * @author philip
 * @param <KEYTYPE>
 *        the key type for the map.
 */
public abstract class AbstractQValueList <KEYTYPE>
{
  // Maps something to quality
  protected final Map <KEYTYPE, QValue> m_aMap = new LinkedHashMap <KEYTYPE, QValue> ();

  public AbstractQValueList ()
  {}

  @Nonnull
  @ReturnsMutableCopy
  public Map <KEYTYPE, QValue> getAllQValues ()
  {
    return ContainerHelper.newMap (m_aMap);
  }

  @Nonnull
  @ReturnsMutableCopy
  public final Map <KEYTYPE, QValue> getAllQValuesLowerThan (final double dQuality)
  {
    final Map <KEYTYPE, QValue> ret = new HashMap <KEYTYPE, QValue> ();
    for (final Map.Entry <KEYTYPE, QValue> aEntry : m_aMap.entrySet ())
      if (aEntry.getValue ().getQuality () < dQuality)
        ret.put (aEntry.getKey (), aEntry.getValue ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final Map <KEYTYPE, QValue> getAllQValuesLowerOrEqual (final double dQuality)
  {
    final Map <KEYTYPE, QValue> ret = new HashMap <KEYTYPE, QValue> ();
    for (final Map.Entry <KEYTYPE, QValue> aEntry : m_aMap.entrySet ())
      if (aEntry.getValue ().getQuality () <= dQuality)
        ret.put (aEntry.getKey (), aEntry.getValue ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final Map <KEYTYPE, QValue> getAllQValuesGreaterThan (final double dQuality)
  {
    final Map <KEYTYPE, QValue> ret = new HashMap <KEYTYPE, QValue> ();
    for (final Map.Entry <KEYTYPE, QValue> aEntry : m_aMap.entrySet ())
      if (aEntry.getValue ().getQuality () > dQuality)
        ret.put (aEntry.getKey (), aEntry.getValue ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final Map <KEYTYPE, QValue> getAllQValuesGreaterOrEqual (final double dQuality)
  {
    final Map <KEYTYPE, QValue> ret = new HashMap <KEYTYPE, QValue> ();
    for (final Map.Entry <KEYTYPE, QValue> aEntry : m_aMap.entrySet ())
      if (aEntry.getValue ().getQuality () >= dQuality)
        ret.put (aEntry.getKey (), aEntry.getValue ());
    return ret;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("map", m_aMap).toString ();
  }
}
