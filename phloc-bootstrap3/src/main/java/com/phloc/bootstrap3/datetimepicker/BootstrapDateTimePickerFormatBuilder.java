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
package com.phloc.bootstrap3.datetimepicker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import com.phloc.commons.cache.AbstractNotifyingCache;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ComparatorStringLongestFirst;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.datetime.format.PDTFromString;

public final class BootstrapDateTimePickerFormatBuilder implements IDateFormatBuilder
{
  private final List <Object> m_aList = new ArrayList <Object> ();

  public BootstrapDateTimePickerFormatBuilder ()
  {}

  @Nonnull
  public BootstrapDateTimePickerFormatBuilder append (@Nonnull final EDateTimePickerFormatToken eToken)
  {
    if (eToken == null)
      throw new NullPointerException ("token");
    m_aList.add (eToken);
    return this;
  }

  @Nonnull
  public BootstrapDateTimePickerFormatBuilder append (final char c)
  {
    m_aList.add (Character.valueOf (c));
    return this;
  }

  @Nonnull
  public List <Object> getAllInternalObjects ()
  {
    return ContainerHelper.newList (m_aList);
  }

  @Nonnull
  public String getJSFormatString ()
  {
    final StringBuilder aSB = new StringBuilder ();
    for (final Object o : m_aList)
      if (o instanceof EDateTimePickerFormatToken)
        aSB.append (((EDateTimePickerFormatToken) o).getJSToken ());
      else
        aSB.append (((Character) o).charValue ());
    return aSB.toString ();
  }

  @Nonnull
  public String getJavaFormatString ()
  {
    final StringBuilder aSB = new StringBuilder ();
    for (final Object o : m_aList)
      if (o instanceof EDateTimePickerFormatToken)
        aSB.append (((EDateTimePickerFormatToken) o).getJavaToken ());
      else
        aSB.append (((Character) o).charValue ());
    return aSB.toString ();
  }

  @Nonnull
  public LocalDate getDateFormatted (@Nullable final String sDate)
  {
    return PDTFromString.getLocalDateFromString (sDate, getJavaFormatString ());
  }

  @Nonnull
  public LocalTime getTimeFormatted (@Nullable final String sTime)
  {
    return PDTFromString.getLocalTimeFromString (sTime, getJavaFormatString ());
  }

  @Nonnull
  public LocalDateTime getLocalDateTimeFormatted (@Nullable final String sDateTime)
  {
    return PDTFromString.getLocalDateTimeFromString (sDateTime, getJavaFormatString ());
  }

  @Nonnull
  public DateTime getDateTimeFormatted (@Nullable final String sDateTime)
  {
    return PDTFromString.getDateTimeFromString (sDateTime, getJavaFormatString ());
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("list", m_aList).toString ();
  }

  private static final class Searcher
  {
    private String m_sRest;
    private final Map <String, EDateTimePickerFormatToken> m_aAllMatching = new HashMap <String, EDateTimePickerFormatToken> ();
    private final Comparator <String> m_aComp = new ComparatorStringLongestFirst ();

    public Searcher (@Nonnull final String sRest)
    {
      if (sRest == null)
        throw new NullPointerException ("rest");
      m_sRest = sRest;
    }

    public boolean hasMore ()
    {
      return m_sRest.length () > 0;
    }

    @Nullable
    public EDateTimePickerFormatToken getNextToken ()
    {
      m_aAllMatching.clear ();
      for (final EDateTimePickerFormatToken eToken : EDateTimePickerFormatToken.values ())
      {
        final String sJavaToken = eToken.getJavaToken ();
        if (m_sRest.startsWith (sJavaToken))
          m_aAllMatching.put (sJavaToken, eToken);
      }
      if (m_aAllMatching.isEmpty ())
        return null;

      Map.Entry <String, EDateTimePickerFormatToken> aEntry;
      if (m_aAllMatching.size () == 1)
        aEntry = ContainerHelper.getFirstElement (m_aAllMatching);
      else
        aEntry = ContainerHelper.getFirstElement (ContainerHelper.getSortedByKey (m_aAllMatching, m_aComp));
      m_sRest = m_sRest.substring (aEntry.getKey ().length ());
      return aEntry.getValue ();
    }

    public char getNextChar ()
    {
      final char ret = m_sRest.charAt (0);
      m_sRest = m_sRest.substring (1);
      return ret;
    }
  }

  private static final class PatternCache extends AbstractNotifyingCache <String, BootstrapDateTimePickerFormatBuilder>
  {
    public PatternCache ()
    {
      super ("BootstrapDateTimePickerFormatBuilder.PatternCache");
    }

    @Override
    @Nonnull
    protected BootstrapDateTimePickerFormatBuilder getValueToCache (@Nullable final String sJavaPattern)
    {
      if (sJavaPattern == null)
        throw new NullPointerException ("JavaPattern");
      // Do parsing
      final BootstrapDateTimePickerFormatBuilder aDFB = new BootstrapDateTimePickerFormatBuilder ();
      final Searcher aSearcher = new Searcher (sJavaPattern);
      while (aSearcher.hasMore ())
      {
        final EDateTimePickerFormatToken eToken = aSearcher.getNextToken ();
        if (eToken != null)
          aDFB.append (eToken);
        else
        {
          // It's not a token -> use a single char and check for the next token
          aDFB.append (aSearcher.getNextChar ());
        }
      }
      return aDFB;
    }
  }

  private static final PatternCache s_aCache = new PatternCache ();

  @Nonnull
  public static IDateFormatBuilder fromJavaPattern (@Nonnull final String sJavaPattern)
  {
    if (StringHelper.hasNoText (sJavaPattern))
      throw new IllegalArgumentException ("JavaPattern");
    return s_aCache.getFromCache (sJavaPattern);
  }
}
