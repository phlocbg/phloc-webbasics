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

import com.phloc.appbasics.security.audit.IAuditItem;
import com.phloc.appbasics.security.audit.IAuditManager;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.datetime.format.PDTToString;
import com.phloc.html.hc.IHCTable;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webctrls.custom.toolbar.IButtonToolbar;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webctrls.datatables.comparator.ComparatorTableDateTime;
import com.phloc.webctrls.security.SecurityUI;
import com.phloc.webpages.AbstractWebPageExt;
import com.phloc.webpages.EWebPageText;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Show audit items.
 * 
 * @author Philip Helger
 */
public class BasePageAudit extends AbstractWebPageExt
{
  @Translatable
  protected static enum EText implements IHasDisplayText
  {
    BUTTON_REFRESH ("Aktualisieren", "Refresh"),
    MSG_DATE ("Datum", "Date"),
    MSG_USER ("Benutzer", "User"),
    MSG_TYPE ("Typ", "Type"),
    MSG_SUCCESS ("Erfolg?", "Success?"),
    MSG_ACTION ("Aktion", "Action");

    @Nonnull
    private final ITextProvider m_aTP;

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

  private final IAuditManager m_aAuditManager;

  public BasePageAudit (@Nonnull @Nonempty final String sID, @Nonnull final IAuditManager aAuditManager)
  {
    super (sID, EWebPageText.PAGE_NAME_MONITORING_AUDIT.getAsMLT ());
    m_aAuditManager = ValueEnforcer.notNull (aAuditManager, "AuditManager");
  }

  public BasePageAudit (@Nonnull @Nonempty final String sID,
                        @Nonnull final String sName,
                        @Nonnull final IAuditManager aAuditManager)
  {
    super (sID, sName);
    m_aAuditManager = ValueEnforcer.notNull (aAuditManager, "AuditManager");
  }

  public BasePageAudit (@Nonnull @Nonempty final String sID,
                        @Nonnull final String sName,
                        @Nullable final String sDescription,
                        @Nonnull final IAuditManager aAuditManager)
  {
    super (sID, sName, sDescription);
    m_aAuditManager = ValueEnforcer.notNull (aAuditManager, "AuditManager");
  }

  public BasePageAudit (@Nonnull @Nonempty final String sID,
                        @Nonnull final IReadonlyMultiLingualText aName,
                        @Nullable final IReadonlyMultiLingualText aDescription,
                        @Nonnull final IAuditManager aAuditManager)
  {
    super (sID, aName, aDescription);
    m_aAuditManager = ValueEnforcer.notNull (aAuditManager, "AuditManager");
  }

  @Nonnull
  @OverrideOnDemand
  protected String getActionString (@Nonnull final IAuditItem aItem)
  {
    return aItem.getAction ();
  }

  @Override
  protected void fillContent (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final IRequestWebScopeWithoutResponse aRequestScope = aWPEC.getRequestScope ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    // Refresh button
    final IButtonToolbar <?> aToolbar = getStyler ().createToolbar (aRequestScope);
    aToolbar.addButton (EText.BUTTON_REFRESH.getDisplayText (aDisplayLocale), aWPEC.getSelfHref ());
    aNodeList.addChild (aToolbar);

    final IHCTable <?> aTable = getStyler ().createTable (new HCCol (140),
                                                          new HCCol (120),
                                                          new HCCol (60),
                                                          new HCCol (60),
                                                          HCCol.star ()).setID (getID ());
    aTable.addHeaderRow ().addCells (EText.MSG_DATE.getDisplayText (aDisplayLocale),
                                     EText.MSG_USER.getDisplayText (aDisplayLocale),
                                     EText.MSG_TYPE.getDisplayText (aDisplayLocale),
                                     EText.MSG_SUCCESS.getDisplayText (aDisplayLocale),
                                     EText.MSG_ACTION.getDisplayText (aDisplayLocale));

    for (final IAuditItem aItem : m_aAuditManager.getLastAuditItems (250))
    {
      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (PDTToString.getAsString (aItem.getDateTime (), aDisplayLocale));
      aRow.addCell (SecurityUI.getUserDisplayName (aItem.getUserID (), aDisplayLocale));
      aRow.addCell (aItem.getType ().getID ());
      aRow.addCell (EWebBasicsText.getYesOrNo (aItem.getSuccess ().isSuccess (), aDisplayLocale));
      aRow.addCell (getActionString (aItem));
    }
    aNodeList.addChild (aTable);

    final DataTables aDataTables = getStyler ().createDefaultDataTables (aWPEC, aTable);
    aDataTables.getOrCreateColumnOfTarget (0)
               .addClass (CSS_CLASS_RIGHT)
               .setComparator (new ComparatorTableDateTime (aDisplayLocale));
    aDataTables.getOrCreateColumnOfTarget (4).addClass (CSS_CLASS_ACTION_COL).setSortable (false);
    aDataTables.setInitialSorting (0, ESortOrder.DESCENDING);
    aNodeList.addChild (aDataTables);
  }
}
