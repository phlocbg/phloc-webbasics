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
package com.phloc.tinymce4.supplementary.tools;

import java.util.Locale;

import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.StringHelper;

public class MainCreateTinyMCEToolbarControls
{
  public static void main (final String [] args)
  {
    // Paste table from http://www.tinymce.com/wiki.php/Controls into this
    // string
    // Last update: 2013-11-22
    final String s = "core  newdocument bold italic underline strikethrough alignleft aligncenter alignright alignjustify styleselect formatselect fontselect fontsizeselect cut copy paste bullist numlist outdent indent blockquote undo redo removeformat subscript superscript\r\n"
                     + "hr  hr\r\n"
                     + "link  link unlink\r\n"
                     + "image   image\r\n"
                     + "charmap   charmap\r\n"
                     + "paste   pastetext\r\n"
                     + "print   print\r\n"
                     + "preview   preview\r\n"
                     + "anchor  anchor\r\n"
                     + "pagebreak   pagebreak\r\n"
                     + "spellchecker  spellchecker\r\n"
                     + "searchreplace   searchreplace\r\n"
                     + "visualblocks  visualblocks\r\n"
                     + "visualchars   visualchars\r\n"
                     + "code  code\r\n"
                     + "fullscreen  fullscreen\r\n"
                     + "insertdatetime  inserttime\r\n"
                     + "media   media\r\n"
                     + "nonbreaking   nonbreaking\r\n"
                     + "save  save cancel\r\n"
                     + "table   table\r\n"
                     + "directionality  ltr rtl\r\n"
                     + "emoticons   emoticons\r\n"
                     + "template  template\r\n"
                     + "textcolor   forecolor backcolor";
    for (final String sLine : StringHelper.getExploded ("\r\n", s))
    {
      final String [] aParts = RegExHelper.getSplitToArray (sLine, "\\s+");
      String sPlugin = aParts[0].toUpperCase (Locale.US);
      if ("CORE".equals (sPlugin))
        sPlugin = null;
      else
        sPlugin = "ETinyMCE4Plugin." + sPlugin;
      for (int i = 1; i < aParts.length; ++i)
      {
        final String sControl = aParts[i];
        System.out.println (sControl.toUpperCase (Locale.US) + " (\"" + sControl + "\", " + sPlugin + "),");
      }
    }
  }
}
