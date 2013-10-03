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
package com.phloc.webctrls.colorbox;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.jquery.IJQuerySelector;
import com.phloc.html.js.builder.jquery.JQuerySelector;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;

/**
 * jQuery colorbox plugin from
 * 
 * <pre>
 * http://www.jacklmoore.com/colorbox
 * </pre>
 * 
 * @author Philip Helger
 */
public class HCColorBox implements IHCNodeBuilder
{
  public static final boolean DEFAULT_PHOTO = false;

  private final IHCElement <?> m_aElement;
  private final IJQuerySelector m_aSelector;
  private Locale m_aDisplayLocale;
  private boolean m_bPhoto = false;
  private String m_sMaxWidth;
  private String m_sMaxHeight;

  public HCColorBox (@Nonnull final IJQuerySelector aSelector)
  {
    if (aSelector == null)
      throw new NullPointerException ("selector");
    m_aElement = null;
    m_aSelector = aSelector;
  }

  public HCColorBox (@Nonnull final IHCElement <?> aElement)
  {
    if (aElement == null)
      throw new NullPointerException ("element");
    m_aElement = aElement;
    m_aSelector = null;
  }

  @Nullable
  public IHCElement <?> getElement ()
  {
    return m_aElement;
  }

  @Nullable
  public IJQuerySelector getSelector ()
  {
    return m_aSelector;
  }

  @Nullable
  public Locale getDisplayLocale ()
  {
    return m_aDisplayLocale;
  }

  @Nonnull
  public HCColorBox setDisplayLocale (@Nonnull final Locale aDisplayLocale)
  {
    m_aDisplayLocale = aDisplayLocale;
    return this;
  }

  public boolean isPhoto ()
  {
    return m_bPhoto;
  }

  @Nonnull
  public HCColorBox setPhoto (final boolean bPhoto)
  {
    m_bPhoto = bPhoto;
    return this;
  }

  @Nullable
  public String getMaxWidth ()
  {
    return m_sMaxWidth;
  }

  @Nonnull
  public HCColorBox setMaxWidth (@Nullable final String sMaxWidth)
  {
    m_sMaxWidth = sMaxWidth;
    return this;
  }

  @Nullable
  public String getMaxHeight ()
  {
    return m_sMaxHeight;
  }

  @Nonnull
  public HCColorBox setMaxHeight (@Nullable final String sMaxHeight)
  {
    m_sMaxHeight = sMaxHeight;
    return this;
  }

  @Nonnull
  public IHCNode build ()
  {
    IJQuerySelector aSelector = m_aSelector;
    if (aSelector == null)
    {
      // Ensure element has an ID
      String sID = m_aElement.getID ();
      if (StringHelper.hasNoText (sID))
      {
        sID = GlobalIDFactory.getNewStringID ();
        m_aElement.setID (sID);
      }
      aSelector = JQuerySelector.id (sID);
    }
    registerExternalResources ();

    // Build parameters
    final JSAssocArray aArgs = new JSAssocArray ();
    if (m_bPhoto != DEFAULT_PHOTO)
      aArgs.add ("photo", m_bPhoto);
    if (StringHelper.hasText (m_sMaxWidth))
      aArgs.add ("maxWidth", m_sMaxWidth);
    if (StringHelper.hasText (m_sMaxHeight))
      aArgs.add ("maxHeight", m_sMaxHeight);

    if (m_aDisplayLocale != null)
    {
      aArgs.add ("current", EColorBoxText.CURRENT.getDisplayText (m_aDisplayLocale));
      aArgs.add ("previous", EColorBoxText.PREVIOUS.getDisplayText (m_aDisplayLocale));
      aArgs.add ("next", EColorBoxText.NEXT.getDisplayText (m_aDisplayLocale));
      aArgs.add ("close", EColorBoxText.CLOSE.getDisplayText (m_aDisplayLocale));
      aArgs.add ("xhrError", EColorBoxText.XHR_ERROR.getDisplayText (m_aDisplayLocale));
      aArgs.add ("imgError", EColorBoxText.IMG_ERROR.getDisplayText (m_aDisplayLocale));
      aArgs.add ("slideshowStart", EColorBoxText.SLIDESHOW_START.getDisplayText (m_aDisplayLocale));
      aArgs.add ("slideshowStop", EColorBoxText.SLIDESHOW_STOP.getDisplayText (m_aDisplayLocale));
    }

    return HCNodeList.create (m_aElement,
                              new HCScriptOnDocumentReady (aSelector.invoke ().invoke ("colorbox").arg (aArgs)));
  }

  public static void registerExternalResources ()
  {
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EColorBoxCSSPathProvider.COLORBOX_1_4_31);
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EColorBoxJSPathProvider.COLORBOX_1_4_31);
  }
}
