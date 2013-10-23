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
package com.phloc.webpages.sysinfo;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ComparatorString;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.htmlext.HCUtils;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.web.servlet.cookie.CookieHelper;
import com.phloc.web.servlet.request.RequestHelper;
import com.phloc.web.servlet.request.RequestLogger;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webctrls.custom.tabbox.ITabBox;
import com.phloc.webctrls.custom.table.IHCTableFormView;
import com.phloc.webpages.AbstractWebPageExt;
import com.phloc.webpages.EWebPageText;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Page with information on the current request
 * 
 * @author Philip Helger
 */
public class BasePageSysInfoRequest extends AbstractWebPageExt
{
  @Translatable
  protected static enum EText implements IHasDisplayText
  {
    MSG_HTTP_HEADERS ("HTTP Header", "HTTP header"),
    MSG_COOKIES ("Cookies", "Cookies"),
    MSG_PARAMETERS ("Request-Parameter", "Request parameters"),
    MSG_PROPERTIES ("Request-Eigenschaften", "Request properties"),
    MSG_ATTRIBUTES ("Request-Attribute", "Request attributes"),
    MSG_NAME ("Name", "Name"),
    MSG_VALUE ("Wert", "Value"),
    MSG_DETAILS ("Details", "Details");

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

  public BasePageSysInfoRequest (@Nonnull @Nonempty final String sID)
  {
    super (sID, EWebPageText.PAGE_NAME_SYSINFO_REQUEST.getAsMLT ());
  }

  public BasePageSysInfoRequest (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  public BasePageSysInfoRequest (@Nonnull @Nonempty final String sID,
                                 @Nonnull final String sName,
                                 @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageSysInfoRequest (@Nonnull @Nonempty final String sID,
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

    final IRequestWebScopeWithoutResponse aRequestScope = aWPEC.getRequestScope ();
    final HttpServletRequest aHttpRequest = aRequestScope.getRequest ();
    final int nFirstColWidth = 250;

    final ITabBox <?> aTabBox = getStyler ().createTabBox ();

    // HTTP headers
    {
      final IHCTableFormView <?> aTable = getStyler ().createTableFormView (new HCCol (nFirstColWidth), HCCol.star ());
      aTable.setID (getID () + "$http");
      aTable.addHeaderRow ().addCells (EText.MSG_NAME.getDisplayText (aDisplayLocale),
                                       EText.MSG_VALUE.getDisplayText (aDisplayLocale));
      for (final Map.Entry <String, List <String>> aEntry : ContainerHelper.getSortedByKey (RequestHelper.getRequestHeaderMap (aHttpRequest)
                                                                                                         .getAllHeaders (),
                                                                                            new ComparatorString (aDisplayLocale))
                                                                           .entrySet ())
      {
        aTable.addBodyRow ().addCell (aEntry.getKey ()).addCell (HCUtils.list2divList (aEntry.getValue ()));
      }
      aTabBox.addTab (EText.MSG_HTTP_HEADERS.getDisplayText (aDisplayLocale), aTable);
    }

    // Cookies
    {
      final IHCTableFormView <?> aTable = getStyler ().createTableFormView (new HCCol (), new HCCol (), new HCCol ());
      aTable.setID (getID () + "$cookies");
      aTable.addHeaderRow ().addCells (EText.MSG_NAME.getDisplayText (aDisplayLocale),
                                       EText.MSG_VALUE.getDisplayText (aDisplayLocale),
                                       EText.MSG_DETAILS.getDisplayText (aDisplayLocale));
      for (final Map.Entry <String, Cookie> aEntry : ContainerHelper.getSortedByKey (CookieHelper.getAllCookies (aHttpRequest),
                                                                                     new ComparatorString (aDisplayLocale))
                                                                    .entrySet ())
      {
        final Cookie aCookie = aEntry.getValue ();
        String sOther = "";
        if (StringHelper.hasText (aCookie.getPath ()))
          sOther += "[path: " + aCookie.getPath () + "]";
        if (StringHelper.hasText (aCookie.getDomain ()))
          sOther += "[domain: " + aCookie.getDomain () + "]";
        if (aCookie.getSecure ())
          sOther += "[secure]";
        sOther += "[maxage: " + aCookie.getMaxAge () + "]";

        aTable.addBodyRow ().addCell (aEntry.getKey ()).addCell (aCookie.getValue ()).addCell (sOther);
      }
      aTabBox.addTab (EText.MSG_COOKIES.getDisplayText (aDisplayLocale), aTable);
    }

    // Request parameters
    {
      final IHCTableFormView <?> aTable = getStyler ().createTableFormView (new HCCol (nFirstColWidth), HCCol.star ());
      aTable.setID (getID () + "$params");
      aTable.addHeaderRow ().addCells (EText.MSG_NAME.getDisplayText (aDisplayLocale),
                                       EText.MSG_VALUE.getDisplayText (aDisplayLocale));
      for (final Map.Entry <String, String> aEntry : RequestLogger.getRequestParameterMap (aHttpRequest).entrySet ())
      {
        aTable.addBodyRow ().addCell (aEntry.getKey ()).addCell (aEntry.getValue ());
      }
      aTabBox.addTab (EText.MSG_PARAMETERS.getDisplayText (aDisplayLocale), aTable);
    }

    // Request properties
    {
      final IHCTableFormView <?> aTable = getStyler ().createTableFormView (new HCCol (nFirstColWidth), HCCol.star ());
      aTable.setID (getID () + "$props");
      aTable.addHeaderRow ().addCells (EText.MSG_NAME.getDisplayText (aDisplayLocale),
                                       EText.MSG_VALUE.getDisplayText (aDisplayLocale));
      for (final Map.Entry <String, String> aEntry : RequestLogger.getRequestFieldMap (aHttpRequest).entrySet ())
      {
        aTable.addBodyRow ().addCell (aEntry.getKey ()).addCell (aEntry.getValue ());
      }
      aTabBox.addTab (EText.MSG_PROPERTIES.getDisplayText (aDisplayLocale), aTable);
    }

    // Request attributes
    {
      final IHCTableFormView <?> aTable = getStyler ().createTableFormView (new HCCol (nFirstColWidth), HCCol.star ());
      aTable.setID (getID () + "$attrs");
      aTable.addHeaderRow ().addCells (EText.MSG_NAME.getDisplayText (aDisplayLocale),
                                       EText.MSG_VALUE.getDisplayText (aDisplayLocale));
      for (final Map.Entry <String, Object> aEntry : ContainerHelper.getSortedByKey (aRequestScope.getAllAttributes (),
                                                                                     new ComparatorString (aDisplayLocale))
                                                                    .entrySet ())
      {
        aTable.addBodyRow ().addCell (aEntry.getKey ()).addCell (String.valueOf (aEntry.getValue ()));
      }
      aTabBox.addTab (EText.MSG_ATTRIBUTES.getDisplayText (aDisplayLocale), aTable);
    }
    aNodeList.addChild (aTabBox);
  }
}
