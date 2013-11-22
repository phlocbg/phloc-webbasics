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
package com.phloc.tinymce4.supplementary.tools;

import java.io.File;
import java.util.Locale;

import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.io.file.filter.FilenameFilterEndsWith;
import com.phloc.commons.io.file.iterate.FileSystemIterator;
import com.phloc.commons.locale.LocaleCache;

public class MainCreateTinyMCELanguageEnum
{
  public static void main (final String [] args)
  {
    // Last update: 2013-11-22
    for (final File aFile : FileSystemIterator.create ("src/main/resources/tinymce/langs",
                                                       new FilenameFilterEndsWith (".js")))
    {
      final String sID = FilenameHelper.getBaseName (aFile);
      if (!LocaleCache.containsLocale (sID))
      {
        System.out.println ("/* Note: this is not a valid Java locale! */");
      }
      System.out.println (sID.toUpperCase (Locale.US) + " (\"" + sID + "\"),");
    }
  }
}
