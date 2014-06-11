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
package com.phloc.webpages.sysinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.changelog.ChangeLog;
import com.phloc.commons.changelog.ChangeLogEntry;
import com.phloc.commons.changelog.ChangeLogSerializer;
import com.phloc.commons.changelog.ComparatorChangeLogEntryDate;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.datetime.PDTFactory;
import com.phloc.datetime.format.PDTToString;
import com.phloc.html.hc.IHCTable;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webctrls.datatables.comparator.ComparatorTableDate;
import com.phloc.webpages.AbstractWebPageExt;
import com.phloc.webpages.EWebPageText;

/**
 * Page with all linked third party libraries
 * 
 * @author Philip Helger
 */
public class BasePageSysInfoChangeLogs extends AbstractWebPageExt
{
  @Translatable
  protected static enum EText implements IHasDisplayText
  {
    MSG_HEADER_DATE ("Datum", "Date"),
    MSG_HEADER_COMPONENT ("Komponente", "Component"),
    MSG_HEADER_CATEGORY ("Kategorie", "Category"),
    MSG_HEADER_CHANGE ("Änderung", "Change");

    private final ITextProvider m_aTP;

    private EText (final String sDE, final String sEN)
    {
      m_aTP = TextProvider.create_DE_EN (sDE, sEN);
    }

    @Nullable
    public String getDisplayText (@Nonnull final Locale aContentLocale)
    {
      return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
    }
  }

  private static List <ChangeLogEntry> s_aCache;

  public BasePageSysInfoChangeLogs (@Nonnull @Nonempty final String sID)
  {
    super (sID, EWebPageText.PAGE_NAME_SYSINFO_CHANGELOGS.getAsMLT ());
  }

  public BasePageSysInfoChangeLogs (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sName)
  {
    super (sID, sName);
  }

  public BasePageSysInfoChangeLogs (@Nonnull @Nonempty final String sID,
                                    @Nonnull final String sName,
                                    @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageSysInfoChangeLogs (@Nonnull @Nonempty final String sID,
                                    @Nonnull final IReadonlyMultiLingualText aName,
                                    @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @Nullable
  private static String _getText (@Nonnull final ChangeLogEntry aEntry, @Nonnull final Locale aDisplayLocale)
  {
    String ret = aEntry.getText (aDisplayLocale);
    if (StringHelper.hasNoText (ret))
      if (aEntry.getAllTexts ().size () == 1)
        ret = ContainerHelper.getFirstElement (aEntry.getAllTexts ().getMap ()).getValue ();
    return ret;
  }

  @Override
  protected void fillContent (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    if (s_aCache == null)
    {
      // Get all change logs
      List <ChangeLogEntry> aChangeLogEntries = new ArrayList <ChangeLogEntry> ();
      for (final ChangeLog aChangeLog : ContainerHelper.newList (ChangeLogSerializer.readAllChangeLogs ().values ()))
        aChangeLogEntries.addAll (aChangeLog.getAllEntries ());

      // Show at last the 500 newest entries
      aChangeLogEntries = ContainerHelper.getSortedInline (aChangeLogEntries,
                                                           new ComparatorChangeLogEntryDate (ESortOrder.DESCENDING));
      s_aCache = aChangeLogEntries.subList (0, Math.min (500, aChangeLogEntries.size ()));
    }

    // Create table
    final IHCTable <?> aTable = getStyler ().createTable (new HCCol (100),
                                                          new HCCol (150),
                                                          new HCCol (100),
                                                          HCCol.star ()).setID (getID ());
    aTable.addHeaderRow ().addCells (EText.MSG_HEADER_DATE.getDisplayText (aDisplayLocale),
                                     EText.MSG_HEADER_COMPONENT.getDisplayText (aDisplayLocale),
                                     EText.MSG_HEADER_CATEGORY.getDisplayText (aDisplayLocale),
                                     EText.MSG_HEADER_CHANGE.getDisplayText (aDisplayLocale));
    for (final ChangeLogEntry aEntry : s_aCache)
    {
      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (PDTToString.getAsString (PDTFactory.createLocalDate (aEntry.getDate ()), aDisplayLocale));
      aRow.addCell (aEntry.getChangeLog ().getComponent ());
      aRow.addCell (aEntry.getCategory ().getID () + " " + aEntry.getAction ().getID ());
      aRow.addCell (_getText (aEntry, aDisplayLocale));
    }
    aNodeList.addChild (aTable);

    final DataTables aDataTables = getStyler ().createDefaultDataTables (aWPEC, aTable);
    aDataTables.getOrCreateColumnOfTarget (0).setComparator (new ComparatorTableDate (aDisplayLocale));
    aDataTables.setInitialSorting (0, ESortOrder.DESCENDING);
    aNodeList.addChild (aDataTables);
  }
}
