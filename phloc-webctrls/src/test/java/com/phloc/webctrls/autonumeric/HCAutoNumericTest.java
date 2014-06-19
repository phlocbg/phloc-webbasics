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
package com.phloc.webctrls.autonumeric;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;

import com.phloc.commons.CGlobal;
import com.phloc.html.EHTMLVersion;
import com.phloc.html.hc.conversion.HCConversionSettings;
import com.phloc.webbasics.form.RequestField;
import com.phloc.webbasics.mock.WebBasicTestRule;

public final class HCAutoNumericTest
{
  private static final String CRLF = CGlobal.LINE_SEPARATOR;

  @Rule
  public final WebBasicTestRule m_aRule = new WebBasicTestRule ();

  @Test
  public void testGetJS ()
  {
    final HCAutoNumeric a = new HCAutoNumeric (new RequestField ("dummy"), Locale.GERMANY);
    final String sID = a.getID ();
    assertEquals ("<input id=\"" +
                  sID +
                  "\" class=\"auto-numeric-edit\" name=\"dummy\" type=\"text\" value=\"\" />" +
                  CRLF, a.getAsHTMLString (new HCConversionSettings (EHTMLVersion.HTML5)));

    a.setThousandSeparator ("");
    assertEquals ("<input id=\"" +
                  sID +
                  "\" class=\"auto-numeric-edit\" name=\"dummy\" type=\"text\" value=\"\" />" +
                  CRLF, a.getAsHTMLString (new HCConversionSettings (EHTMLVersion.HTML5)));
  }
}
