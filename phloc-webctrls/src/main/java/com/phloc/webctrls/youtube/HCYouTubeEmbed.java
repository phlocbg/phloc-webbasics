/**
 * Copyright (C) 2006-2012 phloc systems
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

import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.SMap;
import com.phloc.commons.url.SimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCEmbed;
import com.phloc.html.hc.html.HCObject;
import com.phloc.html.hc.html.HCParam;
import com.phloc.html.hc.impl.AbstractWrappedHCNode;

/**
 * Embeds YouTube videos!<br>
 * Example code: <code>new YouTubeEmbed (640, 385, "93RkWNK3BZc")</code>
 * 
 * @author philip
 */
public class HCYouTubeEmbed extends AbstractWrappedHCNode
{
  private static final String PREFIX = "http://www.youtube.com/v/";
  private final ISimpleURL m_aBaseURL;
  private final HCObject m_aObject;
  private final HCParam m_aParamMovie;
  private final HCParam m_aParamAllowFullScreen;
  private final HCEmbed m_aEmbed;
  private final SMap m_aURLParams = new SMap ();

  public HCYouTubeEmbed (final int nWidth, final int nHeight, final String sVideoID)
  {
    m_aBaseURL = new SimpleURL (PREFIX + sVideoID);
    m_aObject = new HCObject ();
    m_aObject.setWidth (nWidth);
    m_aObject.setHeight (nHeight);
    m_aParamMovie = m_aObject.addAndReturnChild (new HCParam ("movie"));
    m_aParamAllowFullScreen = m_aObject.addAndReturnChild (new HCParam ("allowFullScreen"));
    m_aObject.addChild (new HCParam ("allowscriptaccess").setValue ("always"));
    m_aEmbed = m_aObject.addAndReturnChild (new HCEmbed ());
    m_aEmbed.setType (CMimeType.APPLICATION_SHOCKWAVE_FLASH);
    m_aEmbed.setCustomAttr ("allowscriptaccess", "always");
    m_aEmbed.setWidth (nWidth);
    m_aEmbed.setHeight (nHeight);
    setShowFullScreen (true);
  }

  public void setShowFullScreen (final boolean bShow)
  {
    m_aURLParams.add ("fs", bShow ? "1" : "0");
    m_aParamAllowFullScreen.setValue (Boolean.toString (bShow));
    m_aEmbed.setCustomAttr ("allowfullscreen", Boolean.toString (bShow));
  }

  @Override
  protected IHCNode getContainedHCNode ()
  {
    // Build the correct URL based on the passed settings
    final ISimpleURL aURL = new SimpleURL (m_aBaseURL).addAll (m_aURLParams).add ("hl", "en_US");
    m_aParamMovie.setValue (aURL.getAsString ());
    m_aEmbed.setSrc (aURL);
    return m_aObject;
  }
}
