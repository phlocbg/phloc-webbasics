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
package com.phloc.webctrls.bootstrap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCDiv;

public class BootstrapRow implements IHCNodeBuilder
{
  public static final boolean DEFAULT_FLUID = true;
  public static final int BOOTSTRAP_COLUMN_COUNT = 12;

  @Immutable
  private static final class SpannedNode implements Serializable
  {
    private final int m_nSpanCount;
    private final IHCNode m_aNode;

    public SpannedNode (@Nonnull final EBootstrapSpan eSpan, @Nullable final String sContent)
    {
      if (eSpan == null)
        throw new NullPointerException ("span");
      m_nSpanCount = eSpan.getParts ();
      m_aNode = eSpan.getAsNode (sContent);
    }

    public SpannedNode (@Nonnull final EBootstrapSpan eSpan, @Nullable final IHCNode aContent)
    {
      if (eSpan == null)
        throw new NullPointerException ("span");
      m_nSpanCount = eSpan.getParts ();
      m_aNode = eSpan.getAsNode (aContent);
    }

    public SpannedNode (@Nonnull final EBootstrapSpan eSpan, @Nullable final IHCNode... aContent)
    {
      if (eSpan == null)
        throw new NullPointerException ("span");
      m_nSpanCount = eSpan.getParts ();
      m_aNode = eSpan.getAsNode (aContent);
    }

    public SpannedNode (@Nonnull final EBootstrapSpan eSpan, @Nullable final Iterable <? extends IHCNode> aContent)
    {
      if (eSpan == null)
        throw new NullPointerException ("span");
      m_nSpanCount = eSpan.getParts ();
      m_aNode = eSpan.getAsNode (aContent);
    }

    @Nonnegative
    public int getSpanCount ()
    {
      return m_nSpanCount;
    }

    @Nonnull
    public IHCNode getAsSpannedNode ()
    {
      return m_aNode;
    }
  }

  private static final Logger s_aLogger = LoggerFactory.getLogger (BootstrapRow.class);

  private final boolean m_bFluid;
  private final List <SpannedNode> m_aNodes = new ArrayList <SpannedNode> ();

  public BootstrapRow ()
  {
    this (DEFAULT_FLUID);
  }

  public BootstrapRow (final boolean bFluid)
  {
    m_bFluid = bFluid;
  }

  public boolean isFluid ()
  {
    return m_bFluid;
  }

  @Nonnull
  public BootstrapRow addColumn (@Nonnull final EBootstrapSpan eSpan)
  {
    return addColumn (eSpan, (IHCNode) null);
  }

  @Nonnull
  public BootstrapRow addColumn (@Nonnull final EBootstrapSpan eSpan, @Nullable final String sContent)
  {
    m_aNodes.add (new SpannedNode (eSpan, sContent));
    return this;
  }

  @Nonnull
  public BootstrapRow addColumn (@Nonnull final EBootstrapSpan eSpan, @Nullable final IHCNode aContent)
  {
    m_aNodes.add (new SpannedNode (eSpan, aContent));
    return this;
  }

  @Nonnull
  public BootstrapRow addColumn (@Nonnull final EBootstrapSpan eSpan, @Nullable final IHCNode... aContent)
  {
    m_aNodes.add (new SpannedNode (eSpan, aContent));
    return this;
  }

  @Nonnull
  public BootstrapRow addColumn (@Nonnull final EBootstrapSpan eSpan,
                                 @Nullable final Iterable <? extends IHCNode> aContent)
  {
    m_aNodes.add (new SpannedNode (eSpan, aContent));
    return this;
  }

  @Nonnull
  public HCDiv build ()
  {
    final HCDiv aRow = new HCDiv ().addClass (m_bFluid ? CBootstrapCSS.ROW_FLUID : CBootstrapCSS.ROW);

    int nSpanCount = 0;
    for (final SpannedNode aSpannedNode : m_aNodes)
    {
      aRow.addChild (aSpannedNode.getAsSpannedNode ());
      nSpanCount += aSpannedNode.getSpanCount ();
    }
    if (nSpanCount > BOOTSTRAP_COLUMN_COUNT)
      s_aLogger.warn ("The overall spanning should be less or equal than " +
                      BOOTSTRAP_COLUMN_COUNT +
                      " instead of " +
                      nSpanCount +
                      " for a consistent layout!");
    return aRow;
  }
}
