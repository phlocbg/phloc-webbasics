package com.phloc.webpages.info;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webctrls.bootstrap.BootstrapTable;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webpages.AbstractWebPageExt;

public final class BasePageInfoEnvironmentVariables extends AbstractWebPageExt
{
  @Translatable
  protected static enum EText implements IHasDisplayText
  {
    MSG_HEADER_NAME ("Name", "Name"),
    MSG_HEADER_VALUE ("Wert", "Value");

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

  public BasePageInfoEnvironmentVariables (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sName)
  {
    super (sID, sName);
  }

  @Override
  protected void fillContent (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    final BootstrapTable aTable = new BootstrapTable (new HCCol (200), HCCol.star ()).setID (getID ());
    aTable.addHeaderRow ().addCells (EText.MSG_HEADER_NAME.getDisplayText (aDisplayLocale),
                                     EText.MSG_HEADER_VALUE.getDisplayText (aDisplayLocale));

    // For all environment variables
    for (final Map.Entry <String, String> aEntry : ContainerHelper.getSortedByKey (System.getenv ()).entrySet ())
    {
      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (aEntry.getKey ());
      aRow.addCell (aEntry.getValue ());
    }

    final DataTables aDataTables = createBootstrapDataTables (aTable, aDisplayLocale);
    aDataTables.setInitialSorting (0, ESortOrder.ASCENDING);
    aNodeList.addChild (aDataTables);
  }
}
