package com.phloc.webpages.info;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.datetime.PDTFactory;
import com.phloc.datetime.config.PDTConfig;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webctrls.bootstrap.BootstrapTable;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webpages.AbstractWebPageExt;

public class BasePageInfoTimeZones extends AbstractWebPageExt
{
  @Translatable
  protected static enum EText implements IHasDisplayText
  {
    MSG_CURRENT_TIMEZONE ("Eingestellte Zeitzone: ", "Time zone set: "),
    MSG_ID ("ID", "ID"),
    MSG_NAME ("Name", "Name"),
    MSG_SHORTNAME ("Kurzer Name", "Short name"),
    MSG_OFFSET ("Abweichung", "Offset"),
    MSG_DEFAULT_OFFSET ("Std. Abweichung?", "Def offset?"),
    MSG_FIXED ("Konstant?", "Fixed?");

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

  public BasePageInfoTimeZones (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sName)
  {
    super (sID, sName);
  }

  public BasePageInfoTimeZones (@Nonnull @Nonempty final String sID,
                                @Nonnull final String sName,
                                @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageInfoTimeZones (@Nonnull @Nonempty final String sID,
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

    final long nNow = PDTFactory.getCurrentMillis ();

    // Get default time zone
    final DateTimeZone aCurrentDTZ = PDTConfig.getDefaultDateTimeZone ();

    final BootstrapTable aTable = new BootstrapTable (new HCCol (100),
                                                      new HCCol (200),
                                                      new HCCol (120),
                                                      new HCCol (100),
                                                      new HCCol (100),
                                                      HCCol.star ()).setID (getID ());
    aTable.setSpanningHeaderContent (EText.MSG_CURRENT_TIMEZONE.getDisplayText (aDisplayLocale) +
                                     aCurrentDTZ.getID () +
                                     ' ' +
                                     aCurrentDTZ.getName (nNow));
    aTable.addHeaderRow ().addCells (EText.MSG_ID.getDisplayText (aDisplayLocale),
                                     EText.MSG_NAME.getDisplayText (aDisplayLocale),
                                     EText.MSG_SHORTNAME.getDisplayText (aDisplayLocale),
                                     EText.MSG_OFFSET.getDisplayText (aDisplayLocale),
                                     EText.MSG_DEFAULT_OFFSET.getDisplayText (aDisplayLocale),
                                     EText.MSG_FIXED.getDisplayText (aDisplayLocale));
    for (final String sID : DateTimeZone.getAvailableIDs ())
    {
      final DateTimeZone aDTZ = DateTimeZone.forID (sID);
      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (sID);
      aRow.addCell (aDTZ.getName (nNow, aDisplayLocale));
      aRow.addCell (aDTZ.getShortName (nNow, aDisplayLocale));
      aRow.addCell (new Duration (aDTZ.getOffset (nNow)).toString ());
      aRow.addCell (EWebBasicsText.getYesOrNo (aDTZ.isStandardOffset (nNow), aDisplayLocale));
      aRow.addCell (EWebBasicsText.getYesOrNo (aDTZ.isFixed (), aDisplayLocale));
    }
    aNodeList.addChild (aTable);

    final DataTables aDataTables = createBootstrapDataTables (aTable, aDisplayLocale);
    aDataTables.setInitialSorting (0, ESortOrder.ASCENDING);
    aNodeList.addChild (aDataTables);
  }
}