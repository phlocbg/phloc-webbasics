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
package com.phloc.web.useragent.uaprofile;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class represents a single UA profile.
 * 
 * @author Philip Helger
 */
@Immutable
public class UAProfile implements Serializable
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (UAProfile.class);
  public static final UAProfile EMPTY = new UAProfile ();

  private final String m_sProfileUrl;
  private final Map <Integer, UAProfileDiff> m_aProfileDiffData;

  private UAProfile ()
  {
    m_sProfileUrl = null;
    m_aProfileDiffData = null;
  }

  public UAProfile (@Nullable final List <String> aProfileUrls,
                    @Nullable final Map <Integer, UAProfileDiff> aProfileDiffData)
  {
    final int nUrls = ContainerHelper.getSize (aProfileUrls);
    final int nDiffs = ContainerHelper.getSize (aProfileDiffData);
    if (nUrls == 0 && nDiffs == 0)
      throw new IllegalArgumentException ("Neither profile nor diff data found!");

    if (nUrls > 1)
      s_aLogger.warn ("Found more than one profile URL: " + aProfileUrls);
    m_sProfileUrl = ContainerHelper.getFirstElement (aProfileUrls);
    m_aProfileDiffData = ContainerHelper.isEmpty (aProfileDiffData) ? null
                                                                   : new TreeMap <Integer, UAProfileDiff> (aProfileDiffData);
  }

  /**
   * @return The best match UA profile URL to use. May be <code>null</code> if
   *         only profile diffs are present.
   */
  @Nullable
  public String getProfileURL ()
  {
    return m_sProfileUrl;
  }

  @Nonnegative
  public int getDiffCount ()
  {
    return ContainerHelper.getSize (m_aProfileDiffData);
  }

  @Nullable
  @ReturnsImmutableObject
  public Map <Integer, UAProfileDiff> getProfileDiffData ()
  {
    return ContainerHelper.makeUnmodifiable (m_aProfileDiffData);
  }

  public boolean isSet ()
  {
    return m_sProfileUrl != null || m_aProfileDiffData != null;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof UAProfile))
      return false;
    final UAProfile rhs = (UAProfile) o;
    return EqualsUtils.equals (m_sProfileUrl, rhs.m_sProfileUrl) &&
           EqualsUtils.equals (m_aProfileDiffData, rhs.m_aProfileDiffData);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sProfileUrl).append (m_aProfileDiffData).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).appendIfNotNull ("profileUrl", m_sProfileUrl)
                                       .appendIfNotNull ("profileDiff", m_aProfileDiffData)
                                       .toString ();
  }
}
