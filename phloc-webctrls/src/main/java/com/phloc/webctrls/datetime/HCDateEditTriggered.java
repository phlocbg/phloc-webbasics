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
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCRequestField;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.html.HCTable;
import com.phloc.html.hc.impl.HCEntityNode;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSExpr;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.form.RequestFieldDate;

/**
 * This class represents a wrapper around a date picker. It uses the external
 * "jscalendar 1.0" and consists of 3 elements. An edit field, an image to
 * launch the calendar and the JS script code block for the calendar
 * initialization. Note: each date element requires a unique ID since the
 * external calendar requires the ID.
 * 
 * @author Philip Helger
 */
public class HCDateEditTriggered extends HCTable
{
  private static final ICSSClassProvider CSS_CLASS_PDAF_CALENDAR_CTRL = DefaultCSSClassProvider.create ("phloc-calendar-ctrl");
  private static final ICSSClassProvider CSS_CLASS_PDAF_CALENDAR_CTRL_TRIGGER = DefaultCSSClassProvider.create ("phloc-calendar-ctrl-trigger");
  private static final ICSSClassProvider CSS_CLASS_PDAF_CALENDAR_CTRL_TRIGGER_OBJ = DefaultCSSClassProvider.create ("phloc-calendar-ctrl-trigger-obj");
  private static final String ID_SUFFIX_TRIGGER = "-trigger";

  @Nonnull
  private static HCScript _createJSCode (final boolean bShowTime, final String sID, final Locale aDisplayLocale)
  {
    final String sFormatString = DateFormatBuilder.fromJavaPattern (bShowTime
                                                                             ? PDTFormatPatterns.getDefaultPatternDateTime (aDisplayLocale)
                                                                             : PDTFormatPatterns.getDefaultPatternDate (aDisplayLocale))
                                                  .getJSCalendarFormatString ();

    return new HCScript (JSExpr.ref ("Calendar")
                               .invoke ("setup")
                               .arg (new JSAssocArray ().add ("inputField", sID)
                                                        .add ("ifFormat", sFormatString)
                                                        .add ("daFormat", sFormatString)
                                                        .add ("cache", true)
                                                        .add ("step", 1)
                                                        .add ("button", sID + ID_SUFFIX_TRIGGER)
                                                        .add ("showsTime", bShowTime)
                                                        .add ("align", "BL")));
  }

  public HCDateEditTriggered (final RequestFieldDate aRFD, final String sID, final boolean bShowTime)
  {
    this (aRFD, sID, bShowTime, aRFD.getDisplayLocale ());
  }

  public HCDateEditTriggered (final IHCRequestField aRF,
                              final String sID,
                              final boolean bShowTime,
                              final Locale aDisplayLocale)
  {
    addColumns (HCCol.star (), new HCCol (20));
    setCellPadding (0);
    setCellSpacing (0);
    final HCRow aRow = addBodyRow ();
    addClass (CSS_CLASS_PDAF_CALENDAR_CTRL);
    aRow.addCell (new HCEdit (aRF).setID (sID));

    final IHCElement <?> aImg = HCSpan.create (HCEntityNode.newNBSP ());
    aImg.setTitle (EWebBasicsText.OPEN_CALENDAR.getDisplayText (aDisplayLocale));
    aImg.setID (sID + ID_SUFFIX_TRIGGER);
    aImg.addClass (CSS_CLASS_PDAF_CALENDAR_CTRL_TRIGGER_OBJ);

    aRow.addAndReturnCell (aImg, _createJSCode (bShowTime, sID, aDisplayLocale))
        .addClass (CSS_CLASS_PDAF_CALENDAR_CTRL_TRIGGER);
    HCDateEdit.registerExternalResources (aDisplayLocale);
  }
}
