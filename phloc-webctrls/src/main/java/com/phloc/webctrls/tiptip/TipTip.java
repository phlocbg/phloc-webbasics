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
package com.phloc.webctrls.tiptip;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCHasChildrenMutable;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.IHCNodeWithChildren;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.html.AbstractHCSpan;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;

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
public final class TipTip extends AbstractHCSpan <TipTip>
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
    addClass (CSS_CLASS_TOOLTIP);
    m_sContent = ValueEnforcer.notEmpty (sText, "Text");
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
    if (m_eType != null)
      removeClass (m_eType);
    m_eType = ValueEnforcer.notNull (eType, "Type");
    if (m_eType != null)
      addClass (m_eType);
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

  @Nonnull
  @ReturnsMutableCopy
  public JSAssocArray getJSOptions ()
  {
    final JSAssocArray aOptions = new JSAssocArray ();
    aOptions.add ("content", m_sContent);
    if (StringHelper.hasText (m_sMaxWidth))
      aOptions.add ("maxWidth", m_sMaxWidth);
    if (m_nEdgeOffset > 0)
      aOptions.add ("edgeOffset", m_nEdgeOffset);

    return aOptions;
  }

  @Override
  public void onAdded (@Nonnegative final int nIndex, @Nonnull final IHCHasChildrenMutable <?, ?> aParent)
  {
    // Register resources
    PerRequestJSIncludes.registerJSIncludeForThisRequest (ETipTipJSPathProvider.TIPTIP_13);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (ETipTipCSSPathProvider.TIPTIP_13);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (ETipTipCSSPathProvider.TOOLTIP);

    // Add special JS code
    ((IHCNodeWithChildren <?>) aParent).addChild (new HCScriptOnDocumentReady (JQuery.idRef (this)
                                                                                     .jqinvoke ("tipTip")
                                                                                     .arg (getJSOptions ())));
  }

  @Override
  public void onRemoved (@Nonnegative final int nIndex, @Nonnull final IHCHasChildrenMutable <?, ?> aParent)
  {
    // Remove the JS that is now on that index
    aParent.removeChild (nIndex);
  }

  @Nonnull
  private static String _getAsString (@Nonnull final IHCNode aHCNode)
  {
    return HCSettings.getAsHTMLStringWithoutNamespaces (aHCNode);
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
