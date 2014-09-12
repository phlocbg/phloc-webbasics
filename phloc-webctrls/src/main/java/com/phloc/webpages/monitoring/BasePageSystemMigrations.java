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
package com.phloc.webpages.monitoring;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.migration.SystemMigrationManager;
import com.phloc.appbasics.migration.SystemMigrationResult;
import com.phloc.appbasics.security.audit.IAuditItem;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.datetime.format.PDTToString;
import com.phloc.html.hc.IHCTable;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.page.IWebPageExecutionContext;
import com.phloc.webctrls.custom.toolbar.IButtonToolbar;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webctrls.datatables.comparator.ComparatorTableDateTime;
import com.phloc.webpages.AbstractWebPageExt;
import com.phloc.webpages.EWebPageText;

/**
 * Show system migration status.
 * 
 * @author Philip Helger
 */
public class BasePageSystemMigrations <WPECTYPE extends IWebPageExecutionContext> extends AbstractWebPageExt <WPECTYPE>
{
  @Translatable
  protected static enum EText implements IHasDisplayText
  {
    BUTTON_REFRESH ("Aktualisieren", "Refresh"),
    MSG_ID ("ID", "ID"),
    MSG_DATE ("Datum", "Date"),
    MSG_SUCCESS ("Erfolg?", "Success?"),
    MSG_ERRORMESSAGE ("Fehlermeldung", "Error message");

    @Nonnull
    private final TextProvider m_aTP;

    private EText (@Nonnull final String sDE, @Nonnull final String sEN)
    {
      m_aTP = TextProvider.create_DE_EN (sDE, sEN);
    }

    @Nullable
    public String getDisplayText (@Nonnull final Locale aContentLocale)
    {
      return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
    }
  }

  private transient final SystemMigrationManager m_aSystemMigrationMgr;

  public BasePageSystemMigrations (@Nonnull @Nonempty final String sID,
                                   @Nonnull final SystemMigrationManager aSystemMigrationMgr)
  {
    super (sID, EWebPageText.PAGE_NAME_MONITORING_SYSTEMMIGRATIONS.getAsMLT ());
    m_aSystemMigrationMgr = ValueEnforcer.notNull (aSystemMigrationMgr, "SystemMigrationMgr");
  }

  public BasePageSystemMigrations (@Nonnull @Nonempty final String sID,
                                   @Nonnull final String sName,
                                   @Nonnull final SystemMigrationManager aSystemMigrationMgr)
  {
    super (sID, sName);
    m_aSystemMigrationMgr = ValueEnforcer.notNull (aSystemMigrationMgr, "SystemMigrationMgr");
  }

  public BasePageSystemMigrations (@Nonnull @Nonempty final String sID,
                                   @Nonnull final String sName,
                                   @Nullable final String sDescription,
                                   @Nonnull final SystemMigrationManager aSystemMigrationMgr)
  {
    super (sID, sName, sDescription);
    m_aSystemMigrationMgr = ValueEnforcer.notNull (aSystemMigrationMgr, "SystemMigrationMgr");
  }

  public BasePageSystemMigrations (@Nonnull @Nonempty final String sID,
                                   @Nonnull final IReadonlyMultiLingualText aName,
                                   @Nullable final IReadonlyMultiLingualText aDescription,
                                   @Nonnull final SystemMigrationManager aSystemMigrationMgr)
  {
    super (sID, aName, aDescription);
    m_aSystemMigrationMgr = ValueEnforcer.notNull (aSystemMigrationMgr, "SystemMigrationMgr");
  }

  @Nonnull
  protected final SystemMigrationManager getSystemMigrationMgr ()
  {
    return m_aSystemMigrationMgr;
  }

  @Nonnull
  @OverrideOnDemand
  protected String getActionString (@Nonnull final IAuditItem aItem)
  {
    return aItem.getAction ();
  }

  @Override
  protected void fillContent (@Nonnull final WPECTYPE aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    // Refresh button
    final IButtonToolbar <?> aToolbar = getStyler ().createToolbar (aWPEC);
    aToolbar.addButton (EText.BUTTON_REFRESH.getDisplayText (aDisplayLocale), aWPEC.getSelfHref ());
    aNodeList.addChild (aToolbar);

    final IHCTable <?> aTable = getStyler ().createTable (new HCCol (200),
                                                          new HCCol (140),
                                                          new HCCol (60),
                                                          HCCol.star ()).setID (getID ());
    aTable.addHeaderRow ().addCells (EText.MSG_ID.getDisplayText (aDisplayLocale),
                                     EText.MSG_DATE.getDisplayText (aDisplayLocale),
                                     EText.MSG_SUCCESS.getDisplayText (aDisplayLocale),
                                     EText.MSG_ERRORMESSAGE.getDisplayText (aDisplayLocale));

    for (final SystemMigrationResult aItem : m_aSystemMigrationMgr.getAllMigrationResultsFlattened ())
    {
      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (aItem.getID ());
      aRow.addCell (PDTToString.getAsString (aItem.getExecutionDateTime (), aDisplayLocale));
      aRow.addCell (EWebBasicsText.getYesOrNo (aItem.isSuccess (), aDisplayLocale));
      aRow.addCell (aItem.getErrorMessage ());
    }
    aNodeList.addChild (aTable);

    final DataTables aDataTables = getStyler ().createDefaultDataTables (aWPEC, aTable);
    aDataTables.getOrCreateColumnOfTarget (1)
               .addClass (CSS_CLASS_RIGHT)
               .setComparator (new ComparatorTableDateTime (aDisplayLocale));
    aDataTables.getOrCreateColumnOfTarget (2).setDataSort (2, 1);
    aDataTables.getOrCreateColumnOfTarget (3).setDataSort (3, 2, 1);
    aDataTables.setInitialSorting (1, ESortOrder.DESCENDING);
    aNodeList.addChild (aDataTables);
  }
}
