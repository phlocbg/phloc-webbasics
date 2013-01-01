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
package com.phloc.webbasics.useragent;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.collections.pair.IReadonlyPair;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class wraps the basic elements of a user agent string.
 * 
 * @author philip
 */
@NotThreadSafe
final class UserAgentElementList
{
  private final List <Object> m_aList = new ArrayList <Object> ();

  public void add (@Nonnull final IReadonlyPair <String, String> aPair)
  {
    if (aPair == null)
      throw new NullPointerException ("pair");
    m_aList.add (aPair);
  }

  public void add (@Nonnull final String sValue)
  {
    if (sValue == null)
      throw new NullPointerException ("value");
    m_aList.add (sValue);
  }

  public void add (@Nonnull final List <String> aItems)
  {
    if (aItems == null)
      throw new NullPointerException ("items");
    m_aList.add (aItems);
  }

  @Nullable
  public String getPairValue (@Nullable final String sKey)
  {
    for (final Object aObj : m_aList)
      if (aObj instanceof IReadonlyPair <?, ?>)
        if (((IReadonlyPair <?, ?>) aObj).getFirst ().equals (sKey))
          return (String) ((IReadonlyPair <?, ?>) aObj).getSecond ();
    return null;
  }

  /**
   * Search for a list item starting with the specified String.<br>
   * Example: any/version (list1; list2) any2/version2<br>
   * -> This method is for searching for "list1" and "list2"
   * 
   * @param sPrefix
   *        The search string
   * @return The list item string
   */
  @Nullable
  public String getListItemStartingWith (@Nullable final String sPrefix)
  {
    for (final Object aObj : m_aList)
      if (aObj instanceof List <?>)
        for (final Object aListItem : (List <?>) aObj)
          if (((String) aListItem).startsWith (sPrefix))
            return (String) aListItem;
    return null;
  }

  public boolean containsString (@Nullable final String sString)
  {
    for (final Object aObj : m_aList)
      if (aObj instanceof String)
        if (((String) aObj).equals (sString))
          return true;
    return false;
  }

  @Nullable
  public String getStringValueFollowing (@Nullable final String sString)
  {
    int nIdx1 = -1;
    int nIdx = 0;
    for (final Object aObj : m_aList)
      if (aObj instanceof String)
      {
        if (nIdx1 >= 0 && nIdx == nIdx1 + 1)
          return (String) aObj;
        if (((String) aObj).equals (sString))
          nIdx1 = nIdx;
        nIdx++;
      }
    return null;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("items", m_aList).toString ();
  }
}
