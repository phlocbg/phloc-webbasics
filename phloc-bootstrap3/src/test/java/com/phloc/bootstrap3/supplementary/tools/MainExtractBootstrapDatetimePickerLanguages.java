package com.phloc.bootstrap3.supplementary.tools;

import java.io.File;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import com.phloc.commons.io.file.iterate.FileSystemIterator;
import com.phloc.commons.string.StringHelper;

public class MainExtractBootstrapDatetimePickerLanguages
{
  public static void main (final String [] args)
  {
    final Set <String> aAll = new TreeSet <String> ();
    for (final File aFile : new FileSystemIterator ("src\\main\\resources\\bootstrap\\datetimepicker\\locales"))
      if (aFile.getName ().endsWith (".js") && !aFile.getName ().endsWith (".min.js"))
      {
        final String sLocale = StringHelper.trimStartAndEnd (aFile.getName (), "bootstrap-datetimepicker.", ".js");
        aAll.add (sLocale);
      }
    for (final String s : aAll)
    {
      final String sLocale = s.replace ("-", "_");
      System.out.println (sLocale.toUpperCase (Locale.US) + " (\"" + sLocale + "\", \"" + s + "\"),");
    }
  }
}
