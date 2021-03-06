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
package com.phloc.webctrls.youtube;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.SMap;
import com.phloc.commons.url.SimpleURL;
import com.phloc.html.hc.html.HCEmbed;
import com.phloc.html.hc.html.HCObject;
import com.phloc.html.hc.html.HCParam;

/**
 * Embeds YouTube videos!<br>
 * Example code: <code>new YouTubeEmbed (640, 385, "93RkWNK3BZc")</code>
 * 
 * @author Philip Helger
 */
public class HCYouTubeEmbed extends HCObject
{
  private static final String PREFIX = "http://www.youtube.com/v/";

  public HCYouTubeEmbed (final int nWidth,
                         final int nHeight,
                         @Nonnull @Nonempty final String sVideoID,
                         final boolean bAllowFullScreen)
  {
    final ISimpleURL aBaseURL = new SimpleURL (PREFIX + sVideoID);
    setWidth (nWidth);
    setHeight (nHeight);
    final HCParam aParamMovie = addAndReturnChild (new HCParam ("movie"));
    final HCParam aParamAllowFullScreen = addAndReturnChild (new HCParam ("allowFullScreen"));
    aParamAllowFullScreen.setValue (Boolean.toString (bAllowFullScreen));
    addChild (new HCParam ("allowscriptaccess").setValue ("always"));
    final HCEmbed aEmbed = addAndReturnChild (new HCEmbed ());
    aEmbed.setType (CMimeType.APPLICATION_SHOCKWAVE_FLASH);
    aEmbed.setCustomAttr ("allowscriptaccess", "always");
    aEmbed.setWidth (nWidth);
    aEmbed.setHeight (nHeight);
    aEmbed.setCustomAttr ("allowfullscreen", Boolean.toString (bAllowFullScreen));

    // Build the correct URL based on the passed settings
    final SMap aURLParams = new SMap ("hl", "en_US");
    aURLParams.add ("fs", bAllowFullScreen ? "1" : "0");
    final ISimpleURL aURL = new SimpleURL (aBaseURL).addAll (aURLParams);
    aParamMovie.setValue (aURL.getAsString ());
    aEmbed.setSrc (aURL);
  }
}
