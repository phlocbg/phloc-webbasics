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
package com.phloc.appbasics.security.audit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.dao.xml.AbstractXMLDAODataProvider;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.StringParser;
import com.phloc.datetime.PDTFactory;

final class AuditManagerXMLDAO extends AbstractXMLDAODataProvider
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AuditManagerXMLDAO.class);
  private static final String ELEMENT_ITEMS = "items";
  private static final String ELEMENT_ITEM = "item";
  private static final String ATTR_DT = "dt";
  private static final String ATTR_USER = "user";
  private static final String ATTR_TYPE = "type";
  /* initially was called "succes" by error */
  private static final String ATTR_SUCCESS = "success";

  private final AuditManager m_aMgr;

  public AuditManagerXMLDAO (@Nonnull final AuditManager aMgr)
  {
    if (aMgr == null)
      throw new NullPointerException ("auditMgr");
    m_aMgr = aMgr;
  }

  @Nonnull
  public EChange readXML (@Nullable final IMicroDocument aDoc)
  {
    // No clearing of existing items!!!

    if (aDoc != null)
      for (final IMicroElement eItem : aDoc.getDocumentElement ().getChildElements (ELEMENT_ITEM))
      {
        final String sDT = eItem.getAttribute (ATTR_DT);
        final Long aDT = StringParser.parseLongObj (sDT);
        if (aDT == null)
        {
          s_aLogger.warn ("Failed to parse date time '" + sDT + "'");
          continue;
        }

        final String sUserID = eItem.getAttribute (ATTR_USER);
        if (StringHelper.hasNoText (sUserID))
        {
          s_aLogger.warn ("Failed find user ID");
          continue;
        }

        final String sType = eItem.getAttribute (ATTR_TYPE);
        final EAuditActionType eType = EAuditActionType.getFromIDOrNull (sType);
        if (eType == null)
        {
          s_aLogger.warn ("Failed to parse change type '" + sType + "'");
          continue;
        }

        final String sSuccess = eItem.getAttribute (ATTR_SUCCESS);
        final ESuccess eSuccess = ESuccess.valueOf (StringParser.parseBool (sSuccess));

        final String sAction = eItem.getTextContent ();
        m_aMgr.internalAddItem (new AuditItem (PDTFactory.createDateTimeFromMillis (aDT.longValue ()),
                                               sUserID,
                                               eType,
                                               eSuccess,
                                               sAction));
      }
    // write-only :)
    return EChange.UNCHANGED;
  }

  public void fillXMLDocument (@Nonnull final IMicroDocument aDoc)
  {
    final IMicroElement eCIL = aDoc.appendElement (ELEMENT_ITEMS);
    for (final IAuditItem aAuditItem : m_aMgr.internalGetAllItems ())
    {
      final IMicroElement eItem = eCIL.appendElement (ELEMENT_ITEM);
      eItem.setAttribute (ATTR_DT, Long.toString (aAuditItem.getDateTime ().getMillis ()));
      eItem.setAttribute (ATTR_USER, aAuditItem.getUserID ());
      eItem.setAttribute (ATTR_TYPE, aAuditItem.getType ().getID ());
      eItem.setAttribute (ATTR_SUCCESS, Boolean.toString (aAuditItem.getSuccess ().isSuccess ()));
      eItem.appendText (aAuditItem.getAction ());
    }
  }
}
