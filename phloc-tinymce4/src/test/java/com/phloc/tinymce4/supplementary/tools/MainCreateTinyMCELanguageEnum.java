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
