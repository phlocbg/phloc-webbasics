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
package com.phloc.web.supplementary.main;

import java.io.File;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.multimap.IMultiMapListBased;
import com.phloc.commons.collections.multimap.MultiHashMapArrayListBased;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.mime.MimeTypeDeterminator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.vendor.VendorInfo;
import com.phloc.datetime.PDTFactory;

public final class CreateMimeTypesFileNameMapForJavaxActivation
{
  /**
   * Create the mime.types file that is read by javax.activation. See class
   * javax.annotation.MimetypesFileTypeMap
   * 
   * @param args
   *        ignore
   * @throws Exception
   *         if anything goes wrong
   */
  public static void main (final String [] args) throws Exception
  {
    final String sDestPath = "src/main/resources/META-INF/mime.types";
    Writer w = null;
    try
    {
      // build map
      final IMultiMapListBased <String, String> aMap = new MultiHashMapArrayListBased <String, String> ();
      for (final Map.Entry <String, String> aEntry : MimeTypeDeterminator.getAllKnownMimeTypeFilenameMappings ()
                                                                         .entrySet ())
      {
        // Skip the one empty extension!
        if (aEntry.getKey ().length () > 0)
          aMap.putSingle (aEntry.getValue (), aEntry.getKey ());
      }

      // write file in format iso-8859-1!
      w = new PrintWriter (new File (sDestPath), CCharset.CHARSET_ISO_8859_1);

      // write header
      for (final String sLine : VendorInfo.getFileHeaderLines ())
        w.write ("# " + sLine + '\n');
      w.write ("#\n");
      w.write ("# Created on: " + PDTFactory.getCurrentDateTime ().toString () + "\n");
      w.write ("# Created by: " + CreateMimeTypesFileNameMapForJavaxActivation.class.getName () + "\n");
      w.write ("#\n");

      // write MIME type mapping
      for (final Map.Entry <String, List <String>> aEntry : ContainerHelper.getSortedByKey (aMap).entrySet ())
        w.write ("type=" + aEntry.getKey () + " exts=" + StringHelper.getImploded (",", aEntry.getValue ()) + "\n");

      // done
      w.flush ();
      w.close ();
      System.out.println ("Done creating " + sDestPath);
    }
    finally
    {
      StreamUtils.close (w);
    }
  }
}
