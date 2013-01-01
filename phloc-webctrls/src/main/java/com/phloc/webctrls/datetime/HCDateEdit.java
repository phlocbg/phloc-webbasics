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
package com.phloc.webctrls.datetime;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.datetime.format.PDTFormatPatterns;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.IHCRequestField;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSExpr;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;
import com.phloc.webbasics.form.RequestFieldDate;

/**
 * This class represents a wrapper around a date picker. It uses the external
 * "jscalendar 1.0" and consists of 2 internal elements.<br>
 * Note: each date element requires a unique ID since the external calendar
 * requires the ID.
 * 
 * @author philip
 */
public class HCDateEdit implements IHCNodeBuilder
{
  // dd.mm.yyyy
  public static final int DATE_DEFAULT_MAX_LENGTH = 2 + 1 + 2 + 1 + 4;

  private final HCEdit m_aEdit;
  private boolean m_bShowTime = false;
  private final Locale m_aLocale;

  public HCDateEdit (@Nonnull final RequestFieldDate aRFD)
  {
    this (aRFD.getFieldName (), aRFD.getRequestValue (), aRFD.getDisplayLocale ());
  }

  public HCDateEdit (@Nonnull final IHCRequestField aRF, @Nonnull final Locale aDisplayLocale)
  {
    this (aRF.getFieldName (), aRF.getRequestValue (), aDisplayLocale);
  }

  public HCDateEdit (final String sName, final String sValue, final Locale aDisplayLocale)
  {
    this (sName, sValue, sName, aDisplayLocale);
  }

  public HCDateEdit (final String sName, final String sValue, final String sID, final Locale aDisplayLocale)
  {
    m_aEdit = new HCEdit (sName, sValue);
    m_aEdit.setID (sID);
    m_aLocale = aDisplayLocale;
    registerExternalResources (aDisplayLocale);
  }

  @Nonnull
  public HCDateEdit setMaxLength (final int nMaxLength)
  {
    m_aEdit.setMaxLength (nMaxLength);
    return this;
  }

  @Nonnull
  public HCEdit getEdit ()
  {
    return m_aEdit;
  }

  @Nonnull
  public HCDateEdit setShowTime (final boolean bShowTime)
  {
    m_bShowTime = bShowTime;
    return this;
  }

  @Nonnull
  public HCNodeList build ()
  {
    final String sFormatString = DateFormatBuilder.fromJavaPattern (m_bShowTime
                                                                               ? PDTFormatPatterns.getDefaultPatternDateTime (m_aLocale)
                                                                               : PDTFormatPatterns.getDefaultPatternDate (m_aLocale))
                                                  .getJSCalendarFormatString ();

    final HCNodeList ret = new HCNodeList ();
    ret.addChild (m_aEdit);
    ret.addChild (new HCScript (JSExpr.ref ("Calendar")
                                      .invoke ("setup")
                                      .arg (new JSAssocArray ().add ("inputField", m_aEdit.getID ())
                                                               .add ("ifFormat", sFormatString)
                                                               .add ("daFormat", sFormatString)
                                                               .add ("eventName", "focus")
                                                               .add ("cache", true)
                                                               .add ("step", 1)
                                                               .add ("showsTime", m_bShowTime))));
    return ret;
  }

  public static void registerExternalResources (final Locale aContentLocale)
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EDateTimeJSPathProvider.CALENDAR);
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EDateTimeJSPathProvider.CALENDAR_LANGUAGE.getInstance (aContentLocale.getLanguage ()));
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EDateTimeJSPathProvider.CALENDAR_SETUP);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EDateTimeCSSPathProvider.CALENDAR);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EDateTimeCSSPathProvider.CALENDAR_THEME);
  }
}
