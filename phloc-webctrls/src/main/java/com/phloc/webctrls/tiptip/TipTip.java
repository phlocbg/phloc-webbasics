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
package com.phloc.webctrls.tiptip;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;
import com.phloc.webctrls.custom.EDefaultIcon;

/**
 * Tooltip class using tiptip jQuery plugin.<br>
 * Source:
 * 
 * <pre>
 * http://code.drewwilson.com/entry/tiptip-jquery-plugin
 * </pre>
 * 
 * @author Philip Helger
 */
public final class TipTip implements IHCNodeBuilder
{
  /** Used for tooltip initialization as jquery selector! */
  public static final ICSSClassProvider CSS_CLASS_TOOLTIP = DefaultCSSClassProvider.create ("nocss_tooltip");
  public static final String DEFAULT_MAX_WIDTH = "45%";
  public static final int DEFAULT_EDGE_OFFSET = 5;

  private final String m_sContent;
  private ETipType m_eType = ETipType.INFO;
  private String m_sMaxWidth = DEFAULT_MAX_WIDTH;
  private int m_nEdgeOffset = DEFAULT_EDGE_OFFSET;

  public TipTip (@Nonnull @Nonempty final String sText)
  {
    if (StringHelper.hasNoText (sText))
      throw new IllegalArgumentException ("No text makes no sense!");

    m_sContent = sText;
  }

  public TipTip (@Nonnull @Nonempty final String sText, @Nonnull final ETipType eType)
  {
    this (sText);
    setType (eType);
  }

  @Nonnull
  @Nonempty
  public String getContent ()
  {
    return m_sContent;
  }

  @Nonnull
  public ETipType getType ()
  {
    return m_eType;
  }

  @Nonnull
  public TipTip setType (@Nonnull final ETipType eType)
  {
    if (eType == null)
      throw new NullPointerException ("type");
    m_eType = eType;
    return this;
  }

  @Nullable
  public String getMaxWidth ()
  {
    return m_sMaxWidth;
  }

  @Nonnull
  public TipTip setMaxWidth (@Nullable final String sMaxWidth)
  {
    m_sMaxWidth = sMaxWidth;
    return this;
  }

  public int getEdgeOffset ()
  {
    return m_nEdgeOffset;
  }

  @Nonnull
  public TipTip setEdgeOffset (@Nonnegative final int nEdgeOffset)
  {
    if (nEdgeOffset < 0)
      throw new IllegalArgumentException ("EdgeOffset");
    m_nEdgeOffset = nEdgeOffset;
    return this;
  }

  @Nullable
  public IHCNode build ()
  {
    registerExternalResources ();

    final String sID = GlobalIDFactory.getNewStringID ();
    final HCSpan aSpan = new HCSpan ().setID (sID).addClasses (m_eType, CSS_CLASS_TOOLTIP);
    if (false)
      aSpan.addChild (EDefaultIcon.INFO.getIcon ().getAsNode ());

    final JSAssocArray aOptions = new JSAssocArray ();
    aOptions.add ("content", m_sContent);
    if (StringHelper.hasText (m_sMaxWidth))
      aOptions.add ("maxWidth", m_sMaxWidth);
    if (m_nEdgeOffset > 0)
      aOptions.add ("edgeOffset", m_nEdgeOffset);
    final IHCNode aScript = new HCScriptOnDocumentReady (JQuery.idRef (sID).jqinvoke ("tipTip").arg (aOptions));

    return HCNodeList.create (aSpan, aScript);
  }

  /**
   * Registers all external resources (CSS or JS files) this control needs
   */
  public static void registerExternalResources ()
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (ETipTipJSPathProvider.TIPTIP_13);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (ETipTipCSSPathProvider.TIPTIP_13);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (ETipTipCSSPathProvider.TOOLTIP);
  }

  @Nonnull
  private static String _getAsString (@Nonnull final IHCNode aHCNode)
  {
    return HCSettings.getAsHTMLString (aHCNode);
  }

  @Nonnull
  public static TipTip create (@Nonnull @Nonempty final String sText)
  {
    return new TipTip (sText);
  }

  @Nonnull
  public static TipTip create (@Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    return create (aTextProvider.getText ());
  }

  @Nonnull
  public static TipTip create (@Nonnull final IHCNodeBuilder aNodeBuilder)
  {
    return create (aNodeBuilder.build ());
  }

  @Nonnull
  public static TipTip create (@Nonnull final IHCNodeBuilder... aNodeBuilders)
  {
    final StringBuilder aSB = new StringBuilder ();
    for (final IHCNodeBuilder aNodeBuilder : aNodeBuilders)
      aSB.append (_getAsString (aNodeBuilder.build ()));
    return create (aSB.toString ());
  }

  @Nonnull
  public static TipTip create (@Nonnull final IHCNode aNode)
  {
    return create (_getAsString (aNode));
  }

  @Nonnull
  public static TipTip create (@Nonnull final IHCNode... aNodes)
  {
    final StringBuilder aSB = new StringBuilder ();
    for (final IHCNode aHCNode : aNodes)
      aSB.append (_getAsString (aHCNode));
    return create (aSB.toString ());
  }

  @Nonnull
  public static TipTip create (@Nonnull final Iterable <? extends IHCNode> aNodes)
  {
    final StringBuilder aSB = new StringBuilder ();
    for (final IHCNode aHCNode : aNodes)
      aSB.append (_getAsString (aHCNode));
    return create (aSB.toString ());
  }
}
