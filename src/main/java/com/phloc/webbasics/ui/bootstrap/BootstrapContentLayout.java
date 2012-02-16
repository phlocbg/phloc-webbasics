package com.phloc.webbasics.ui.bootstrap;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.conversion.HCConversionSettings;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCHR;
import com.phloc.html.hc.html5.HCFooter;
import com.phloc.html.hc.impl.AbstractWrappedHCNode;

public class BootstrapContentLayout extends AbstractWrappedHCNode
{
  private static final class SpannedNode
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

  private static final Logger s_aLogger = LoggerFactory.getLogger (BootstrapContentLayout.class);

  private HCDiv m_aContainer;
  private final List <SpannedNode> m_aNodes = new ArrayList <SpannedNode> ();
  private IHCNode m_aFooter;

  public BootstrapContentLayout ()
  {}

  @Nonnull
  public BootstrapContentLayout addColumn (@Nonnull final EBootstrapSpan eSpan, @Nonnull final IHCNode aNode)
  {
    if (eSpan == null)
      throw new NullPointerException ("span");
    m_aNodes.add (new SpannedNode (eSpan, aNode));
    return this;
  }

  @Nonnull
  public BootstrapContentLayout setFooter (@Nullable final IHCNode aFooter)
  {
    m_aFooter = aFooter;
    return this;
  }

  @Override
  protected void prepareBeforeGetAsNode (@Nonnull final HCConversionSettings aConversionSettings)
  {
    m_aContainer = new HCDiv ().addClass (CBootstrapCSS.CONTAINER_FLUID);
    if (!m_aNodes.isEmpty ())
    {
      final HCDiv aRow = m_aContainer.addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.ROW_FLUID));

      int nSpanCount = 0;
      for (final SpannedNode aSpannedNode : m_aNodes)
      {
        aRow.addChild (aSpannedNode.getAsSpannedNode ());
        nSpanCount += aSpannedNode.getSpanCount ();
      }
      if (nSpanCount != 12)
        s_aLogger.warn ("The overall spanning should be exactly 12 instead of " +
                        nSpanCount +
                        " for a consistent layout!");
    }

    if (m_aFooter != null)
      m_aContainer.addChildren (new HCHR (), new HCFooter (m_aFooter));
  }

  @Override
  protected IHCNode getContainedHCNode ()
  {
    return m_aContainer;
  }
}
