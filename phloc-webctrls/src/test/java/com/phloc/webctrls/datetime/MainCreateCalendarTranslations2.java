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
package com.phloc.webctrls.datetime;

import java.io.File;
import java.util.Locale;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.file.SimpleFileIO;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.locale.LocaleCache;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.vendor.VendorInfo;

public final class MainCreateCalendarTranslations2
{
  private static String _mask (final String s)
  {
    // Replace inline "\n" with JS "\n"
    return RegExHelper.stringReplacePattern ("\\n", s, "\\\\n");
  }

  public static void main (final String [] args)
  {
    final StringBuilder aFileHeader = new StringBuilder ();
    for (final String sLine : VendorInfo.getFileHeaderLines ())
      if (StringHelper.hasText (sLine))
        aFileHeader.append ("// ").append (sLine).append ('\n');
      else
        aFileHeader.append ("//\n");
    aFileHeader.append ("// This file was generated by ")
               .append (MainCreateCalendarTranslations2.class.getName ())
               .append ("\n\n");
    final String sBaseTemplate = aFileHeader.toString () +
                                 StreamUtils.getAllBytesAsString (new ClassPathResource ("calendar/calendar-lang-template.js"),
                                                                  CCharset.CHARSET_UTF_8_OBJ);

    for (final String sLocale : new String [] { "hu_HU", "cs_CZ", "sk_SK", "de", "en" })
    {
      final Locale aLocale = LocaleCache.getLocale (sLocale);
      String sJSCode = sBaseTemplate;
      for (final EDateEditText e : EDateEditText.values ())
      {
        final String sKey = "%" + e.name () + "%";
        final boolean bIsString = e != EDateEditText.CALENDAR_FIRST_DAY_OF_WEEK;
        String sReplacement = e.getDisplayText (aLocale);
        if (sReplacement == null)
          throw new IllegalStateException ("Failed to resolve " + e.name () + " for locale " + sLocale);
        if (bIsString)
          sReplacement = "\"" + _mask (sReplacement) + "\"";
        sJSCode = sJSCode.replace (sKey, sReplacement);
      }

      SimpleFileIO.writeFile (new File ("src/main/resources/datetime/lang/calendar-" + aLocale.getLanguage () + ".js"),
                              sJSCode,
                              CCharset.CHARSET_UTF_8_OBJ);
    }
  }
}
