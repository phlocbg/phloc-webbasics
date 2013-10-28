package com.phloc.tinymce4.supplementary.tools;

import java.io.File;
import java.util.Locale;

import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.io.file.filter.FileFilterDirectoryPublic;
import com.phloc.commons.io.file.iterate.FileSystemIterator;

public class MainCreateTinyMCEPluginEnum
{
  public static void main (final String [] args)
  {
    for (final File aFile : FileSystemIterator.create ("src/main/resources/tinymce/plugins",
                                                       FileFilterDirectoryPublic.getInstance ()))
    {
      final String sID = FilenameHelper.getBaseName (aFile);
      System.out.println (sID.toUpperCase (Locale.US) + " (\"" + sID + "\"),");
    }
  }
}
