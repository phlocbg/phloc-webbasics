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

import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.locale.LocaleCache;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.html.hc.IHCTable;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.masterdata.currency.ECurrency;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webctrls.datatables.comparator.ComparatorTableInteger;
import com.phloc.webpages.AbstractWebPageExt;
import com.phloc.webpages.EWebPageText;

/**
 * Page with all currencies
 * 
 * @author Philip Helger
 */
public class BasePageSysInfoCurrencies extends AbstractWebPageExt
{
  @Translatable
  protected static enum EText implements IHasDisplayText
  {
    MSG_LOCALE ("Locale", "Locale"),
    MSG_CODE ("Code", "Code"),
    MSG_NAME ("Name", "Name"),
    MSG_SYMBOL ("Symbol", "Symbol"),
    MSG_DEFAULT_FRACTION_DIGITS ("Nachkommastellen", "Fraction digits"),
    MSG_EXAMPLE ("Beispiel", "Example");

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

  public BasePageSysInfoCurrencies (@Nonnull @Nonempty final String sID)
  {
    super (sID, EWebPageText.PAGE_NAME_SYSINFO_CURRENCIES.getAsMLT ());
  }

  public BasePageSysInfoCurrencies (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sName)
  {
    super (sID, sName);
  }

  public BasePageSysInfoCurrencies (@Nonnull @Nonempty final String sID,
                                    @Nonnull final String sName,
                                    @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageSysInfoCurrencies (@Nonnull @Nonempty final String sID,
                                    @Nonnull final IReadonlyMultiLingualText aName,
                                    @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @Nonnull
  @ReturnsMutableCopy
  private static Map <Locale, Currency> _getAllCurrencies ()
  {
    final Map <Locale, Currency> ret = new HashMap <Locale, Currency> ();
    for (final Locale aLocale : LocaleCache.getAllLocales ())
    {
      try
      {
        final Currency aCurrency = Currency.getInstance (aLocale);
        if (aCurrency != null)
          ret.put (aLocale, aCurrency);
      }
      catch (final Exception exc)
      {
        // Locale not found
      }
    }

    return ret;
  }

  @Override
  protected void fillContent (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    final IHCTable <?> aTable = getStyler ().createTable (HCCol.star (),
                                                          HCCol.star (),
                                                          HCCol.star (),
                                                          HCCol.star (),
                                                          HCCol.star (),
                                                          HCCol.star ()).setID (getID ());
    aTable.addHeaderRow ().addCells (EText.MSG_LOCALE.getDisplayText (aDisplayLocale),
                                     EText.MSG_CODE.getDisplayText (aDisplayLocale),
                                     EText.MSG_NAME.getDisplayText (aDisplayLocale),
                                     EText.MSG_SYMBOL.getDisplayText (aDisplayLocale),
                                     EText.MSG_DEFAULT_FRACTION_DIGITS.getDisplayText (aDisplayLocale),
                                     EText.MSG_EXAMPLE.getDisplayText (aDisplayLocale));
    for (final Map.Entry <Locale, Currency> aEntry : _getAllCurrencies ().entrySet ())
    {
      final Locale aLocale = aEntry.getKey ();
      final Currency aCurrency = aEntry.getValue ();
      final ECurrency eCurrency = ECurrency.getFromIDOrNull (aCurrency.getCurrencyCode ());

      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (aLocale.toString ());
      aRow.addCell (aCurrency.getCurrencyCode ());
      aRow.addCell (eCurrency == null ? null : eCurrency.getDisplayText (aDisplayLocale));
      aRow.addCell (aCurrency.getSymbol (aDisplayLocale));
      aRow.addCell (Integer.toString (aCurrency.getDefaultFractionDigits ()));
      aRow.addCell (NumberFormat.getCurrencyInstance (aLocale).format (12.3456));
    }
    aNodeList.addChild (aTable);

    final DataTables aDataTables = getStyler ().createDefaultDataTables (aTable, aDisplayLocale);
    aDataTables.getOrCreateColumnOfTarget (1).setDataSort (1, 0);
    aDataTables.getOrCreateColumnOfTarget (4)
               .addClass (CSS_CLASS_RIGHT)
               .setComparator (new ComparatorTableInteger (aDisplayLocale));
    aDataTables.setInitialSorting (1, ESortOrder.ASCENDING);
    aNodeList.addChild (aDataTables);
  }
}
