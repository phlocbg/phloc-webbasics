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
package com.phloc.webctrls.datetime;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.locale.LocaleCache;
import com.phloc.commons.string.StringHelper;

public final class MainCreateCalendarTranslations
{
  private static boolean _isValid (final char c)
  {
    return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
  }

  @Nonnull
  private static String _mask (@Nonnull final String s)
  {
    final StringBuilder aSB = new StringBuilder ();
    for (final char c : s.toCharArray ())
      if (_isValid (c))
        aSB.append (c);
      else
        if (c < 256)
          aSB.append ("\\x").append (StringHelper.getHexStringLeadingZero (c, 2));
        else
          aSB.append ("\\u").append (StringHelper.getHexStringLeadingZero (c, 4));
    return aSB.toString ();
  }

  private static void _translate (final Locale aLocale)
  {
    final DateFormatSymbols aDFS = DateFormatSymbols.getInstance (aLocale);
    final Calendar aCal = Calendar.getInstance (aLocale);
    final SimpleDateFormat aDF = (SimpleDateFormat) DateFormat.getDateInstance (DateFormat.MEDIUM, aLocale);
    final SimpleDateFormat aTF = (SimpleDateFormat) DateFormat.getTimeInstance (DateFormat.MEDIUM, aLocale);

    final StringBuilder aSB = new StringBuilder ();

    {
      // Day names
      aSB.append ("Calendar._DN = new Array(");
      final String [] DN = aDFS.getWeekdays ();
      for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; ++i)
      {
        if (i > Calendar.SUNDAY)
          aSB.append (",");
        aSB.append ('"').append (_mask (DN[i])).append ('"');
      }
      aSB.append (",\"").append (_mask (DN[Calendar.SUNDAY])).append ("\");\n");
    }

    {
      // Short day names
      aSB.append ("Calendar._SDN = new Array(");
      final String [] SDN = aDFS.getShortWeekdays ();
      for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; ++i)
      {
        if (i > Calendar.SUNDAY)
          aSB.append (",");
        aSB.append ('"').append (_mask (SDN[i])).append ('"');
      }
      aSB.append (",\"").append (_mask (SDN[Calendar.SUNDAY])).append ("\");\n");
    }

    // First day of week (Sunday=0)
    aSB.append ("Calendar._FD = ").append (aCal.getFirstDayOfWeek () - 1).append (";\n");

    {
      // Month names
      aSB.append ("Calendar._MN = new Array(");
      final String [] MN = aDFS.getMonths ();
      for (int i = Calendar.JANUARY; i <= Calendar.DECEMBER; ++i)
      {
        if (i > Calendar.JANUARY)
          aSB.append (",");
        aSB.append ('"').append (_mask (MN[i])).append ('"');
      }
      aSB.append (");\n");
    }

    // Short month names
    aSB.append ("Calendar._SMN = new Array(");
    final String [] SMN = aDFS.getShortMonths ();
    for (int i = Calendar.JANUARY; i <= Calendar.DECEMBER; ++i)
    {
      if (i > Calendar.JANUARY)
        aSB.append (",");
      aSB.append ('"').append (_mask (SMN[i])).append ('"');
    }
    aSB.append (");\n");

    // Tooltips
    aSB.append ("Calendar._TT = {};\n");
    aSB.append ("Calendar._TT[\"WEEKEND\"] = \"0,6\";\n");
    aSB.append ("Calendar._TT[\"DEF_DATE_FORMAT\"] = \"")
       .append (DateFormatBuilder.fromJavaPattern (aDF.toPattern ()).getJSCalendarFormatString ())
       .append ("\";\n");
    aSB.append ("Calendar._TT[\"TT_DATE_FORMAT\"] = \"%A, %e. %B\";\n");
    aSB.append ("Calendar._TT[\"DEF_TIME_FORMAT\"] = \"")
       .append (DateFormatBuilder.fromJavaPattern (aTF.toPattern ()).getJSCalendarFormatString ())
       .append ("\";\n");

    System.out.println (aSB.toString ());
  }

  public static void main (final String [] args)
  {
    _translate (LocaleCache.getLocale ("cs", "cz"));
  }
}
