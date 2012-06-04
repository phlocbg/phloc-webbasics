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
package com.phloc.webbasics.ui.bootstrap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.conversion.IHCConversionSettings;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.impl.AbstractWrappedHCNode;

public class BootstrapContentRow extends AbstractWrappedHCNode
{
  private static final class SpannedNode implements Serializable
  {
    private final EBootstrapSpan m_eSpan;
    private final IHCNode m_aNode;

    public SpannedNode (@Nonnull final EBootstrapSpan eSpan, @Nullable final IHCNode aNode)
    {
      m_eSpan = eSpan;
      m_aNode = aNode;
    }

    @Nonnull
    public IHCNode getAsSpannedNode ()
    {
      return m_eSpan.getAsNode (m_aNode);
    }

    @Nonnull
    public int getSpanCount ()
    {
      return m_eSpan.getParts ();
    }
  }

  private static final Logger s_aLogger = LoggerFactory.getLogger (BootstrapContentRow.class);

  private HCDiv m_aRow;
  private final List <SpannedNode> m_aNodes = new ArrayList <SpannedNode> ();

  public BootstrapContentRow ()
  {}

  @Nonnull
  public BootstrapContentRow addColumn (@Nonnull final EBootstrapSpan eSpan, @Nullable final IHCNode aNode)
  {
    if (eSpan == null)
      throw new NullPointerException ("span");
    m_aNodes.add (new SpannedNode (eSpan, aNode));
    return this;
  }

  @Override
  protected void prepareBeforeGetAsNode (@Nonnull final IHCConversionSettings aConversionSettings)
  {
    m_aRow = new HCDiv ().addClass (CBootstrapCSS.ROW_FLUID);

    int nSpanCount = 0;
    for (final SpannedNode aSpannedNode : m_aNodes)
    {
      m_aRow.addChild (aSpannedNode.getAsSpannedNode ());
      nSpanCount += aSpannedNode.getSpanCount ();
    }
    if (nSpanCount != 12)
      s_aLogger.warn ("The overall spanning should be exactly 12 instead of " +
                      nSpanCount +
                      " for a consistent layout!");
  }

  @Override
  protected IHCNode getContainedHCNode ()
  {
    return m_aRow;
  }
}
