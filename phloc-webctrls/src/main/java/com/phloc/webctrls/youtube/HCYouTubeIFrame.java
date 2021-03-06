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

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.SimpleURL;
import com.phloc.html.hc.html.HCIFrame;

/**
 * Embeds YouTube videos as an IFrame<br>
 * Example code: <code>new HCYouTubeIFrame (640, 385, "93RkWNK3BZc")</code>
 *
 * @author Philip Helger
 */
public class HCYouTubeIFrame extends HCIFrame
{
  private static final String PREFIX = "http://www.youtube.com/embed/";

  private final SimpleURL m_aVideoURL;

  public HCYouTubeIFrame (@Nonnull @Nonempty final String sVideoID)
  {
    m_aVideoURL = new SimpleURL (PREFIX + sVideoID);
    setSrc (m_aVideoURL);
  }

  public HCYouTubeIFrame (final int nWidth, final int nHeight, @Nonnull @Nonempty final String sVideoID)
  {
    this (sVideoID);
    setWidth (nWidth);
    setHeight (nHeight);
  }

  @Nonnull
  public ISimpleURL getVideoURL ()
  {
    return m_aVideoURL;
  }

  @Nonnull
  public HCYouTubeIFrame setAutoPlay (final boolean bAutoplay)
  {
    m_aVideoURL.remove ("autoplay");
    m_aVideoURL.add ("autoplay", bAutoplay ? "1" : "0");
    setSrc (m_aVideoURL);
    return this;
  }

  @Nonnull
  public HCYouTubeIFrame setAllowFullscreen (final boolean bAllowFullscreen)
  {
    m_aVideoURL.remove ("fs");
    m_aVideoURL.add ("fs", bAllowFullscreen ? "1" : "0");
    setSrc (m_aVideoURL);
    return this;
  }

  @Nonnull
  public HCYouTubeIFrame setShowRelated (final boolean bShowRelated)
  {
    m_aVideoURL.remove ("rel");
    m_aVideoURL.add ("rel", bShowRelated ? "1" : "0");
    setSrc (m_aVideoURL);
    return this;
  }

  @Nonnull
  public HCYouTubeIFrame setLanguage (@Nonnull final Locale aLocale)
  {
    ValueEnforcer.notNull (aLocale, "Locale");
    ValueEnforcer.notEmpty (aLocale.getLanguage (), "Locale.getLanguage");

    m_aVideoURL.remove ("hl");
    m_aVideoURL.add ("hl", aLocale.getLanguage ());
    setSrc (m_aVideoURL);
    return this;
  }
}
