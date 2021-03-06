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
package com.phloc.webctrls.google;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.hc.html.HCScript.EMode;

/**
 * Test class for class {@link HCGoogleAnalytics}
 * 
 * @author Philip Helger
 */
public final class HCGoogleAnalyticsTest
{
  @Test
  public void testBasic1 ()
  {
    HCScript.setDefaultMode (EMode.PLAIN_TEXT_NO_ESCAPE);
    final HCGoogleAnalytics aGA = new HCGoogleAnalytics ("abc", false);
    assertEquals ("<script type=\"text/javascript\" xmlns=\"http://www.w3.org/1999/xhtml\">"
                      + "var _gaq=(_gaq||[]);"
                      + "_gaq.push(['_setAccount','abc']);"
                      + "_gaq.push(['_trackPageview']);"
                      + "_gaq.push(['_trackPageLoadTime']);"
                      + "(function(){"
                      + "var ga=document.createElement('script');"
                      + "ga.type='text\\/javascript';"
                      + "ga.async=true;"
                      + "ga.src=((('https:'==window.location.protocol)?'https:\\/\\/ssl':'http:\\/\\/www')+'.google-analytics.com\\/ga.js');"
                      + "var s=document.getElementsByTagName('script')[0];"
                      + "s.parentNode.insertBefore(ga,s);"
                      + "})();"
                      + "</script>",
                  HCSettings.getAsHTMLString (aGA, false));
    HCScript.setDefaultMode (HCScript.DEFAULT_MODE);
  }
}
