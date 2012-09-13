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
package com.phloc.webctrls.facebook;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.SimpleURL;
import com.phloc.css.ECSSUnit;
import com.phloc.css.property.CCSSProperties;
import com.phloc.css.propertyvalue.CCSSValue;
import com.phloc.html.hc.api.EHCScrolling;
import com.phloc.html.hc.html.HCIFrame;

/**
 * Small control for liking the current page on Facebook.<br>
 * Source: http://developers.facebook.com/docs/reference/plugins/like
 * 
 * @author philip
 */
public final class HCFacebookLike extends HCIFrame
{
  public static final int DEFAULT_IFRAME_WIDTH = 450;
  public static final int DEFAULT_IFRAME_HEIGHT = 80;

  public HCFacebookLike (@Nonnull final ISimpleURL aLikeURL)
  {
    this (aLikeURL, DEFAULT_IFRAME_WIDTH, DEFAULT_IFRAME_HEIGHT, true);
  }

  public HCFacebookLike (@Nonnull final ISimpleURL aLikeURL,
                       @Nonnegative final int nWidth,
                       @Nonnegative final int nHeight,
                       final boolean bShowFaces)
  {
    final SimpleURL aURL = new SimpleURL ("http://www.facebook.com/plugins/like.php");
    aURL.add ("href", aLikeURL.getAsStringWithEncodedParameters ());
    aURL.add ("layout", "standard");
    aURL.add ("show_faces", Boolean.toString (bShowFaces));
    aURL.add ("action", "like");
    aURL.add ("colorscheme", "light");
    aURL.add ("width", nWidth);
    aURL.add ("height", nHeight);

    setSrc (aURL);
    setScrolling (EHCScrolling.NO);
    setFrameBorder (false);
    addStyle (CCSSProperties.BORDER.newValue (CCSSValue.NONE));
    addStyle (CCSSProperties.OVERFLOW.newValue (CCSSValue.HIDDEN));
    addStyle (CCSSProperties.WIDTH.newValue (ECSSUnit.px (nWidth)));
    addStyle (CCSSProperties.HEIGHT.newValue (ECSSUnit.px (nHeight)));
    setCustomAttr ("allowTransparency", "true");
  }
}
