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
package com.phloc.schedule.longrun;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.joda.time.DateTime;

import com.phloc.appbasics.app.dao.xml.AbstractXMLDAODataProvider;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.StringParser;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.impl.ReadonlyMultiLingualText;
import com.phloc.commons.url.SimpleURL;
import com.phloc.schedule.longrun.LongRunningJobResult.EType;

@NotThreadSafe
final class LongRunningJobResultManagerXMLDAO extends AbstractXMLDAODataProvider
{
  private static final String ELEMENT_ROOT = "root";
  private static final String ELEMENT_JOBDATA = "jobdata";
  private static final String ATTR_ID = "id";
  private static final String ATTR_STARTDT = "startdt";
  private static final String ATTR_ENDDT = "enddt";
  private static final String ATTR_EXECSUCCESS = "execsuccess";
  private static final String ATTR_STARTINGUSERID = "startinguserid";
  private static final String ELEMENT_DESCRIPTION = "description";
  private static final String ELEMENT_RESULT = "result";
  private static final String ATTR_TYPE = "type";

  private final LongRunningJobResultManagerDAO m_aMgr;

  public LongRunningJobResultManagerXMLDAO (@Nonnull final LongRunningJobResultManagerDAO aMgr)
  {
    if (aMgr == null)
      throw new NullPointerException ("mgr");
    m_aMgr = aMgr;
  }

  @Nonnull
  public EChange readXML (@Nullable final IMicroDocument aDoc)
  {
    m_aMgr.internalClear ();

    if (aDoc != null)
      for (final IMicroElement eJobData : aDoc.getDocumentElement ().getAllChildElements (ELEMENT_JOBDATA))
      {
        final String sID = eJobData.getAttribute (ATTR_ID);
        final DateTime aStartDateTime = eJobData.getAttributeWithConversion (ATTR_STARTDT, DateTime.class);
        final DateTime aEndDateTime = eJobData.getAttributeWithConversion (ATTR_ENDDT, DateTime.class);
        final ESuccess eExecSuccess = ESuccess.valueOf (StringParser.parseBool (eJobData.getAttribute (ATTR_EXECSUCCESS)));
        final String sStartingUserID = eJobData.getAttribute (ATTR_STARTINGUSERID);
        final IReadonlyMultiLingualText aJobDescription = MicroTypeConverter.convertToNative (eJobData.getFirstChildElement (ELEMENT_DESCRIPTION),
                                                                                              ReadonlyMultiLingualText.class);
        final IMicroElement eResult = eJobData.getFirstChildElement (ELEMENT_RESULT);
        final EType eResultType = EType.getFromIDOrNull (eResult.getAttribute (ATTR_TYPE));
        final String sResultText = eResult.getTextContent ();
        LongRunningJobResult aResult;
        switch (eResultType)
        {
          case FILE:
            aResult = LongRunningJobResult.createFile (new File (sResultText));
            break;
          case XML:
            aResult = LongRunningJobResult.createXML (MicroReader.readMicroXML (sResultText));
            break;
          case TEXT:
            aResult = LongRunningJobResult.createText (sResultText);
            break;
          case LINK:
            aResult = LongRunningJobResult.createLink (new SimpleURL (sResultText));
            break;
          default:
            throw new IllegalStateException ("Unknown type!");
        }

        m_aMgr.internalAdd (new LongRunningJobData (sID,
                                                    aStartDateTime,
                                                    aEndDateTime,
                                                    eExecSuccess,
                                                    sStartingUserID,
                                                    aJobDescription,
                                                    aResult));
      }
    return EChange.UNCHANGED;
  }

  public void fillXMLDocument (@Nonnull final IMicroDocument aDoc)
  {
    final IMicroElement eRoot = aDoc.appendElement (ELEMENT_ROOT);
    for (final LongRunningJobData aJobData : m_aMgr.internalGetAllForSave ())
    {
      final IMicroElement eJobData = eRoot.appendElement (ELEMENT_JOBDATA);
      eJobData.setAttribute (ATTR_ID, aJobData.getID ());
      eJobData.setAttributeWithConversion (ATTR_STARTDT, aJobData.getStartDateTime ());
      eJobData.setAttributeWithConversion (ATTR_ENDDT, aJobData.getEndDateTime ());
      eJobData.setAttribute (ATTR_EXECSUCCESS, Boolean.toString (aJobData.getExecutionSuccess ().isSuccess ()));
      if (aJobData.getStartingUserID () != null)
        eJobData.setAttribute (ATTR_STARTINGUSERID, aJobData.getStartingUserID ());

      // Description
      eJobData.appendChild (MicroTypeConverter.convertToMicroElement (aJobData.getJobDescription (),
                                                                      ELEMENT_DESCRIPTION));

      // Result
      final IMicroElement eResult = eJobData.appendElement (ELEMENT_RESULT);
      eResult.setAttribute (ATTR_TYPE, aJobData.getResult ().getType ().getID ());
      eResult.appendText (aJobData.getResult ().getAsString ());
    }
  }
}
