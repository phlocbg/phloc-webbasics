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

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.multimap.IMultiMapListBased;
import com.phloc.commons.collections.multimap.MultiHashMapArrayListBased;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.locale.ComparatorLocale;
import com.phloc.commons.locale.LocaleCache;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.html.hc.IHCCell;
import com.phloc.html.hc.IHCTable;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webctrls.famfam.EFamFamFlagIcon;
import com.phloc.webpages.AbstractWebPageExt;
import com.phloc.webpages.EWebPageText;

/**
 * Page with all available locales
 * 
 * @author Philip Helger
 */
public class BasePageSysInfoLanguages extends AbstractWebPageExt
{
  @Translatable
  protected static enum EText implements IHasDisplayText
  {
    MSG_ID ("ID", "ID"),
    MSG_NAME ("Name", "Name"),
    MSG_LOCALES ("Locales", "Locales");

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

  public BasePageSysInfoLanguages (@Nonnull @Nonempty final String sID)
  {
    super (sID, EWebPageText.PAGE_NAME_SYSINFO_LANGUAGES.getAsMLT ());
  }

  public BasePageSysInfoLanguages (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  public BasePageSysInfoLanguages (@Nonnull @Nonempty final String sID,
                                   @Nonnull final String sName,
                                   @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageSysInfoLanguages (@Nonnull @Nonempty final String sID,
                                   @Nonnull final IReadonlyMultiLingualText aName,
                                   @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @Override
  protected void fillContent (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    final IMultiMapListBased <String, Locale> aMapLanguageToLocale = new MultiHashMapArrayListBased <String, Locale> ();
    for (final Locale aLocale : LocaleCache.getAllLocales ())
    {
      final String sLanguage = aLocale.getLanguage ();
      if (sLanguage.length () > 0)
        aMapLanguageToLocale.putSingle (sLanguage, aLocale);
    }

    final IHCTable <?> aTable = getStyler ().createTable (new HCCol (100), new HCCol (200), HCCol.star ())
                                            .setID (getID ());
    aTable.addHeaderRow ().addCells (EText.MSG_ID.getDisplayText (aDisplayLocale),
                                     EText.MSG_NAME.getDisplayText (aDisplayLocale),
                                     EText.MSG_LOCALES.getDisplayText (aDisplayLocale));

    // For all environment variables
    for (final Map.Entry <String, List <Locale>> aEntry : aMapLanguageToLocale.entrySet ())
    {
      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (aEntry.getKey ());
      aRow.addCell (ContainerHelper.getFirstElement (aEntry.getValue ()).getDisplayLanguage (aDisplayLocale));

      final IHCCell <?> aCell = aRow.addCell ();
      for (final Locale aLocale : ContainerHelper.getSorted (aEntry.getValue (), new ComparatorLocale ()))
      {
        final HCDiv aDiv = new HCDiv ();
        final EFamFamFlagIcon eIcon = EFamFamFlagIcon.getFromIDOrNull (aLocale.getCountry ());
        if (eIcon != null)
        {
          aDiv.addChild (eIcon.getAsNode ());
          aDiv.addChild (" ");
        }
        aDiv.addChild (aLocale.toString ());
        if (aLocale.getCountry ().length () > 0)
          aDiv.addChild (" (" + aLocale.getDisplayCountry (aDisplayLocale) + ")");
        aCell.addChild (aDiv);
      }
    }
    aNodeList.addChild (aTable);

    final DataTables aDataTables = getStyler ().createDefaultDataTables (aTable, aDisplayLocale);
    aDataTables.getOrCreateColumnOfTarget (2).setSortable (false);
    aDataTables.setInitialSorting (0, ESortOrder.ASCENDING);
    aNodeList.addChild (aDataTables);
  }
}
