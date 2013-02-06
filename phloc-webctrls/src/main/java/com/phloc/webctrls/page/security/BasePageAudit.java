/**
 * Copyright (C) 2012-2013 Philip Helger <ph@phloc.com>
 * All Rights Reserved
 *
 * This file is part of the Ecoware Online Shop.
 *
 * Proprietary and confidential.
 *
 * It can not be copied and/or distributed without the
 * express permission of Philip Helger.
 *
 * Unauthorized copying of this file, via any medium is
 * strictly prohibited.
 */
package com.phloc.webctrls.page.security;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.appbasics.security.audit.ComparatorAuditItemDateTime;
import com.phloc.appbasics.security.audit.IAuditItem;
import com.phloc.appbasics.security.audit.IAuditManager;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.datetime.format.PDTToString;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.html.HCStrong;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webctrls.bootstrap.BootstrapTable;
import com.phloc.webctrls.bootstrap.ext.BootstrapDataTables;
import com.phloc.webctrls.datatables.DataTablesColumn;
import com.phloc.webctrls.page.AbstractWebPageExt;
import com.phloc.webctrls.security.SecurityUI;

public class BasePageAudit extends AbstractWebPageExt
{
  private final IAuditManager m_aAuditManager;

  public BasePageAudit (@Nonnull @Nonempty final String sID,
                        @Nonnull final String sName,
                        @Nonnull final IAuditManager aAuditManager)
  {
    super (sID, sName);
    if (aAuditManager == null)
      throw new NullPointerException ("auditManager");
    m_aAuditManager = aAuditManager;
  }

  @Override
  protected void fillContent (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final BootstrapTable aTable = new BootstrapTable (new HCCol (180),
                                                      new HCCol (120),
                                                      new HCCol (60),
                                                      new HCCol (60),
                                                      HCCol.star ()).setID (getID ());
    aTable.addHeaderRow ().addCells ("Datum", "Benutzer", "Typ", "Erfolg?", "Aktion");

    for (final IAuditItem aItem : ContainerHelper.getSorted (m_aAuditManager.getAllAuditItems (),
                                                             new ComparatorAuditItemDateTime (ESortOrder.DESCENDING)))
    {
      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (PDTToString.getAsString (aItem.getDateTime (), aDisplayLocale));
      aRow.addCell (SecurityUI.getUserDisplayName (aItem.getUserID ()));
      aRow.addCell (aItem.getType ().getID ());
      aRow.addCell (EWebBasicsText.getYesOrNo (aItem.getSuccess ().isSuccess (), aDisplayLocale));
      aRow.addCell (aItem.getAction ());

      if (aTable.getBodyRowCount () >= 1000)
        break;
    }

    if (aTable.getBodyRowCount () == 0)
      aTable.addBodyRow ()
            .addAndReturnCell (HCStrong.create ("Keine Audit-Einträge gefunden!"))
            .setColspan (aTable.getColumnCount ());

    aNodeList.addChild (aTable);
    aNodeList.addChild (new BootstrapDataTables (aTable).setDisplayLocale (aDisplayLocale)
                                                        .addColumn (new DataTablesColumn (4).setSortable (false))
                                                        .setInitialSorting (0, ESortOrder.DESCENDING));
  }
}
