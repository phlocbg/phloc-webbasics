package com.phloc.webpages.info;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCH3;
import com.phloc.html.hc.htmlext.HCUtils;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.web.servlet.request.RequestHelper;
import com.phloc.web.servlet.request.RequestLogger;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webctrls.bootstrap.BootstrapTable;
import com.phloc.webpages.AbstractWebPageExt;

public final class BasePageInfoRequest extends AbstractWebPageExt
{
  @Translatable
  protected static enum EText implements IHasDisplayText
  {
    MSG_HTTP_HEADERS ("HTTP Header", "HTTP header"),
    MSG_PARAMETERS ("Request Parameter", "Request parameters"),
    MSG_PROPERTIES ("Request Eigenschaften", "Request properties"),
    MSG_NAME ("Name", "Name"),
    MSG_VALUE ("Wert", "Value");

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

  public BasePageInfoRequest (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  public BasePageInfoRequest (@Nonnull @Nonempty final String sID,
                              @Nonnull final String sName,
                              @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageInfoRequest (@Nonnull @Nonempty final String sID,
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

    final HttpServletRequest aHttpRequest = aWPEC.getRequestScope ().getRequest ();
    final int nFirstColWidth = 250;

    // HTTP headers
    aNodeList.addChild (HCH3.create (EText.MSG_HTTP_HEADERS.getDisplayText (aDisplayLocale)));
    BootstrapTable aTable = new BootstrapTable (new HCCol (nFirstColWidth), HCCol.star ()).setID (getID () + "$http");
    aTable.addHeaderRow ().addCells (EText.MSG_NAME.getDisplayText (aDisplayLocale),
                                     EText.MSG_VALUE.getDisplayText (aDisplayLocale));
    for (final Map.Entry <String, List <String>> aEntry : RequestHelper.getRequestHeaderMap (aHttpRequest))
    {
      aTable.addBodyRow ().addCell (aEntry.getKey ()).addCell (HCUtils.list2divList (aEntry.getValue ()));
    }
    aNodeList.addChild (aTable);

    // Request parameters
    aNodeList.addChild (HCH3.create (EText.MSG_PARAMETERS.getDisplayText (aDisplayLocale)));
    aTable = new BootstrapTable (new HCCol (nFirstColWidth), HCCol.star ()).setID (getID () + "$params");
    aTable.addHeaderRow ().addCells (EText.MSG_NAME.getDisplayText (aDisplayLocale),
                                     EText.MSG_VALUE.getDisplayText (aDisplayLocale));
    for (final Map.Entry <String, String> aEntry : RequestLogger.getRequestParameterMap (aHttpRequest).entrySet ())
    {
      aTable.addBodyRow ().addCells (aEntry.getKey (), aEntry.getValue ());
    }
    aNodeList.addChild (aTable);

    // Request properties
    aNodeList.addChild (HCH3.create (EText.MSG_PROPERTIES.getDisplayText (aDisplayLocale)));
    aTable = new BootstrapTable (new HCCol (nFirstColWidth), HCCol.star ()).setID (getID () + "$attrs");
    aTable.addHeaderRow ().addCells (EText.MSG_NAME.getDisplayText (aDisplayLocale),
                                     EText.MSG_VALUE.getDisplayText (aDisplayLocale));
    for (final Map.Entry <String, String> aEntry : RequestLogger.getRequestFieldMap (aHttpRequest).entrySet ())
    {
      aTable.addBodyRow ().addCells (aEntry.getKey (), aEntry.getValue ());
    }
    aNodeList.addChild (aTable);
  }
}
