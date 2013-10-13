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
package com.phloc.webbasics.servlet;

import java.io.File;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.io.WebFileIO;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.io.file.SimpleFileIO;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.stats.utils.StatisticsExporter;
import com.phloc.commons.xml.serialize.XMLWriterSettings;
import com.phloc.datetime.PDTFactory;
import com.phloc.datetime.io.PDTIOHelper;
import com.phloc.web.datetime.PDTWebDateUtils;

/**
 * Web application listener with statistics support
 * 
 * @author Philip Helger
 */
public class WebAppListenerWithStatistics extends WebAppListener
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (WebAppListenerWithStatistics.class);

  /**
   * @return <code>true</code> to write statistics on context shutdown,
   *         <code>false</code> to not do so
   */
  @OverrideOnDemand
  protected boolean isWriteStatisticsOnEnd ()
  {
    return true;
  }

  /**
   * @return The filename where the statistics should be written to.
   */
  @Nonnull
  @Nonempty
  protected String getStatisticsFilename ()
  {
    return "statistics/statistics_" + PDTIOHelper.getCurrentDateTimeForFilename () + ".xml";
  }

  @Override
  protected void handleStatistics ()
  {
    if (isWriteStatisticsOnEnd ())
    {
      // serialize statistics
      try
      {
        final File aDestPath = WebFileIO.getFile (getStatisticsFilename ());
        final IMicroDocument aDoc = StatisticsExporter.getAsXMLDocument ();
        aDoc.getDocumentElement ().setAttribute ("location", "shutdown");
        aDoc.getDocumentElement ().setAttribute ("datetime",
                                                 PDTWebDateUtils.getAsStringXSD (PDTFactory.getCurrentDateTime ()));
        SimpleFileIO.writeFile (aDestPath, MicroWriter.getXMLString (aDoc), XMLWriterSettings.DEFAULT_XML_CHARSET_OBJ);
      }
      catch (final Throwable t)
      {
        s_aLogger.error ("Failed to write statistics on context shutdown.", t);
      }
    }
  }
}
