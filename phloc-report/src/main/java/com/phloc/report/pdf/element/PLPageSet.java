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
package com.phloc.report.pdf.element;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.report.pdf.render.ERenderingOption;
import com.phloc.report.pdf.render.PDPageContentStreamWithCache;
import com.phloc.report.pdf.render.RenderPreparationContext;
import com.phloc.report.pdf.render.RenderingContext;
import com.phloc.report.pdf.spec.BorderSpec;
import com.phloc.report.pdf.spec.BorderStyleSpec;
import com.phloc.report.pdf.spec.SizeSpec;

/**
 * Represents a single page layout as element
 * 
 * @author Philip Helger
 */
public class PLPageSet extends AbstractPLBaseElement <PLPageSet>
{
  public static final class PageSetPrepareResult
  {
    private float m_fHeaderHeight = Float.NaN;
    private final float [] m_aContentHeight;
    private float m_fFooterHeight = Float.NaN;
    private final List <List <AbstractPLElement <?>>> m_aPerPageElements = new ArrayList <List <AbstractPLElement <?>>> ();

    PageSetPrepareResult (@Nonnegative final int nElementCount)
    {
      m_aContentHeight = new float [nElementCount];
    }

    void setHeaderHeight (final float fHeaderHeight)
    {
      m_fHeaderHeight = fHeaderHeight;
    }

    public float getHeaderHeight ()
    {
      return m_fHeaderHeight;
    }

    void setContentHeight (@Nonnegative final int nIndex, final float fContentHeight)
    {
      m_aContentHeight[nIndex] = fContentHeight;
    }

    public float getContentHeight (@Nonnegative final int nIndex)
    {
      return m_aContentHeight[nIndex];
    }

    void setFooterHeight (final float fFooterHeight)
    {
      m_fFooterHeight = fFooterHeight;
    }

    public float getFooterHeight ()
    {
      return m_fFooterHeight;
    }

    void addPerPageElements (@Nonnull @Nonempty final List <AbstractPLElement <?>> aCurPageElements)
    {
      if (ContainerHelper.isEmpty (aCurPageElements))
        throw new IllegalArgumentException ("curPageElements");
      m_aPerPageElements.add (aCurPageElements);
    }

    @Nonnegative
    public int getPageCount ()
    {
      return m_aPerPageElements.size ();
    }

    @Nonnull
    @ReturnsMutableObject (reason = "speed")
    List <List <AbstractPLElement <?>>> directGetPerPageElements ()
    {
      return m_aPerPageElements;
    }
  }

  private final SizeSpec m_aPageSize;

  private AbstractPLElement <?> m_aPageHeader;
  private final List <AbstractPLElement <?>> m_aElements = new ArrayList <AbstractPLElement <?>> ();
  private AbstractPLElement <?> m_aPageFooter;

  public PLPageSet (@Nonnull final PDRectangle aPageRect)
  {
    this (SizeSpec.create (aPageRect));
  }

  public PLPageSet (@Nonnegative final float fWidth, @Nonnegative final float fHeight)
  {
    this (new SizeSpec (fWidth, fHeight));
  }

  public PLPageSet (@Nonnull final SizeSpec aPageSize)
  {
    if (aPageSize == null)
      throw new NullPointerException ("pageSize");
    m_aPageSize = aPageSize;
  }

  /**
   * @return The usable page width without the x-paddings and x-margins
   */
  @Nonnegative
  public float getAvailableWidth ()
  {
    return m_aPageSize.getWidth () - getMargin ().getXSum () - getPadding ().getXSum ();
  }

  /**
   * @return The usable page height without the y-paddings and y-margins
   */
  @Nonnegative
  public float getAvailableHeight ()
  {
    return m_aPageSize.getHeight () - getMargin ().getYSum () - getPadding ().getYSum ();
  }

  @Nullable
  public AbstractPLElement <?> getPageHeader ()
  {
    return m_aPageHeader;
  }

  @Nonnull
  public PLPageSet setPageHeader (@Nullable final AbstractPLElement <?> aPageHeader)
  {
    m_aPageHeader = aPageHeader;
    return this;
  }

  @Nonnull
  public List <? extends AbstractPLElement <?>> getAllElements ()
  {
    return ContainerHelper.newList (m_aElements);
  }

  @Nonnull
  public PLPageSet addElement (@Nonnull final AbstractPLElement <?> aElement)
  {
    if (aElement == null)
      throw new NullPointerException ("element");
    m_aElements.add (aElement);
    return this;
  }

  @Nullable
  public AbstractPLElement <?> getPageFooter ()
  {
    return m_aPageFooter;
  }

  @Nonnull
  public PLPageSet setPageFooter (@Nullable final AbstractPLElement <?> aPageFooter)
  {
    m_aPageFooter = aPageFooter;
    return this;
  }

  public float getYTop ()
  {
    return m_aPageSize.getHeight () - getMargin ().getTop () - getPadding ().getTop ();
  }

  @Nonnull
  public PageSetPrepareResult prepareAllPages () throws IOException
  {
    final PageSetPrepareResult ret = new PageSetPrepareResult (m_aElements.size ());

    // Prepare page header
    if (m_aPageHeader != null)
    {
      // Page header does not care about page padding
      final RenderPreparationContext aRPC = new RenderPreparationContext (m_aPageSize.getWidth () -
                                                                              getMargin ().getXSum () -
                                                                              m_aPageHeader.getMargin ().getXSum () -
                                                                              m_aPageHeader.getPadding ().getXSum (),
                                                                          getMargin ().getTop () -
                                                                              m_aPageHeader.getMargin ().getYSum () -
                                                                              m_aPageHeader.getPadding ().getYSum ());
      ret.setHeaderHeight (m_aPageHeader.prepare (aRPC).getHeight ());
    }

    // Prepare content elements
    int nIndex = 0;
    for (final AbstractPLElement <?> aElement : m_aElements)
    {
      final RenderPreparationContext aRPC = new RenderPreparationContext (getAvailableWidth () -
                                                                              aElement.getMargin ().getXSum () -
                                                                              aElement.getPadding ().getXSum (),
                                                                          getAvailableHeight () -
                                                                              aElement.getMargin ().getYSum () -
                                                                              aElement.getPadding ().getYSum ());
      final float fHeight = aElement.prepare (aRPC).getHeight ();
      ret.setContentHeight (nIndex, fHeight);
      nIndex++;
    }

    // Prepare footer
    if (m_aPageFooter != null)
    {
      // Page footer does not care about page padding
      final RenderPreparationContext aRPC = new RenderPreparationContext (m_aPageSize.getWidth () -
                                                                              getMargin ().getXSum () -
                                                                              m_aPageFooter.getMargin ().getXSum () -
                                                                              m_aPageFooter.getPadding ().getXSum (),
                                                                          getMargin ().getBottom () -
                                                                              m_aPageFooter.getMargin ().getYSum () -
                                                                              m_aPageFooter.getPadding ().getYSum ());
      ret.setFooterHeight (m_aPageFooter.prepare (aRPC).getHeight ());
    }

    // Start rendering

    // Start at the top
    final float fYTop = getYTop ();
    final float fLeastY = getMargin ().getBottom () + getPadding ().getBottom ();

    // Split into page pieces
    {
      List <AbstractPLElement <?>> aCurPageElements = new ArrayList <AbstractPLElement <?>> ();
      int nElementIndex = 0;
      float fCurY = fYTop;
      for (final AbstractPLElement <?> aElement : m_aElements)
      {
        final float fThisHeight = ret.getContentHeight (nElementIndex);
        final float fThisHeightFull = fThisHeight +
                                      aElement.getPadding ().getYSum () +
                                      aElement.getMargin ().getYSum ();
        if (fCurY - fThisHeightFull < fLeastY)
        {
          // Next page
          if (aCurPageElements.isEmpty ())
          {
            // FIXME one element too large
          }
          else
          {
            ret.addPerPageElements (aCurPageElements);
            aCurPageElements = new ArrayList <AbstractPLElement <?>> ();
            fCurY = fYTop;
          }
        }
        aCurPageElements.add (aElement);
        fCurY -= fThisHeightFull;
        nElementIndex++;
      }

      if (!aCurPageElements.isEmpty ())
        ret.addPerPageElements (aCurPageElements);
    }

    return ret;
  }

  /**
   * Render all pages of this layout to the specified PDDocument
   * 
   * @param aPR
   *        The preparation result. May not be <code>null</code>.
   * @param aDoc
   *        The PDDocument. May not be <code>null</code>.
   * @param bDebug
   *        <code>true</code> for debug output
   * @param nTotalPageIndex
   *        Total page index. Always &ge; 0.
   * @param nTotalPageCount
   *        Total page count. Always &ge; 0.
   * @throws IOException
   */
  public void renderAllPages (@Nonnull final PageSetPrepareResult aPR,
                              @Nonnull final PDDocument aDoc,
                              final boolean bDebug,
                              @Nonnegative final int nTotalPageIndex,
                              @Nonnegative final int nTotalPageCount) throws IOException
  {
    // Start at the left
    final float fXLeft = getMargin ().getLeft () + getPadding ().getLeft ();
    final float fYTop = getYTop ();

    final boolean bCompressPDF = !bDebug;
    int nPageIndex = 0;
    int nElementIndex = 0;
    final int nPageCount = aPR.getPageCount ();
    for (final List <AbstractPLElement <?>> aPerPage : aPR.directGetPerPageElements ())
    {
      // Layout in memory
      final PDPage aPage = new PDPage (new PDRectangle (m_aPageSize.getWidth (), m_aPageSize.getHeight ()));
      aDoc.addPage (aPage);
      final PDPageContentStreamWithCache aContentStream = new PDPageContentStreamWithCache (aDoc,
                                                                                            aPage,
                                                                                            false,
                                                                                            bCompressPDF);
      try
      {
        // Page rect before content - debug: red
        {
          final float fLeft = getMargin ().getLeft ();
          final float fTop = m_aPageSize.getHeight () - getMargin ().getTop ();
          final float fWidth = m_aPageSize.getWidth () - getMargin ().getXSum ();
          final float fHeight = m_aPageSize.getHeight () - getMargin ().getYSum ();

          // Fill before border
          if (getFillColor () != null)
          {
            aContentStream.setNonStrokingColor (getFillColor ());
            aContentStream.fillRect (fLeft, fTop - fHeight, fWidth, fHeight);
          }

          BorderSpec aRealBorder = getBorder ();
          if (shouldApplyDebugBorder (aRealBorder, bDebug))
            aRealBorder = new BorderSpec (new BorderStyleSpec (new Color (255, 0, 0)));
          if (aRealBorder.hasAnyBorder ())
            renderBorder (aContentStream, fLeft, fTop, fWidth, fHeight, aRealBorder);
        }

        // Start with the page rect
        if (m_aPageHeader != null)
        {
          // Page header does not care about page padding
          // header top-left
          final RenderingContext aRC = new RenderingContext (aContentStream,
                                                             bDebug,
                                                             getMargin ().getLeft () +
                                                                 m_aPageHeader.getMargin ().getLeft (),
                                                             m_aPageSize.getHeight () -
                                                                 m_aPageHeader.getMargin ().getTop (),
                                                             m_aPageSize.getWidth () -
                                                                 getMargin ().getXSum () -
                                                                 m_aPageHeader.getMargin ().getXSum (),
                                                             aPR.getHeaderHeight () +
                                                                 m_aPageHeader.getPadding ().getYSum ());
          aRC.setOption (ERenderingOption.PAGESET_PAGENUM_CURRENT, nPageIndex + 1);
          aRC.setOption (ERenderingOption.PAGESET_PAGENUM_TOTAL, nPageCount);
          aRC.setOption (ERenderingOption.TOTAL_PAGENUM_CURRENT, nTotalPageIndex + nPageIndex + 1);
          aRC.setOption (ERenderingOption.TOTAL_PAGENUM_TOTAL, nTotalPageCount);
          m_aPageHeader.perform (aRC);
        }

        float fCurY = fYTop;
        for (final AbstractPLElement <?> aElement : aPerPage)
        {
          // Get element height
          final float fThisHeight = aPR.getContentHeight (nElementIndex);
          final float fThisHeightWithPadding = fThisHeight + aElement.getPadding ().getYSum ();

          final RenderingContext aRC = new RenderingContext (aContentStream,
                                                             bDebug,
                                                             fXLeft + aElement.getMargin ().getLeft (),
                                                             fCurY - aElement.getMargin ().getTop (),
                                                             getAvailableWidth () - aElement.getMargin ().getXSum (),
                                                             fThisHeightWithPadding);
          aRC.setOption (ERenderingOption.PAGESET_PAGENUM_CURRENT, nPageIndex + 1);
          aRC.setOption (ERenderingOption.PAGESET_PAGENUM_TOTAL, nPageCount);
          aRC.setOption (ERenderingOption.TOTAL_PAGENUM_CURRENT, nTotalPageIndex + nPageIndex + 1);
          aRC.setOption (ERenderingOption.TOTAL_PAGENUM_TOTAL, nTotalPageCount);
          aElement.perform (aRC);

          fCurY -= fThisHeightWithPadding + aElement.getMargin ().getYSum ();
          nElementIndex++;
        }

        if (m_aPageFooter != null)
        {
          // Page footer does not care about page padding
          // footer top-left
          final RenderingContext aRC = new RenderingContext (aContentStream,
                                                             bDebug,
                                                             getMargin ().getLeft () +
                                                                 m_aPageFooter.getMargin ().getLeft (),
                                                             getMargin ().getBottom () -
                                                                 m_aPageFooter.getMargin ().getTop (),
                                                             m_aPageSize.getWidth () -
                                                                 getMargin ().getXSum () -
                                                                 m_aPageFooter.getMargin ().getXSum (),
                                                             aPR.getFooterHeight () +
                                                                 m_aPageFooter.getPadding ().getYSum ());
          aRC.setOption (ERenderingOption.PAGESET_PAGENUM_CURRENT, nPageIndex + 1);
          aRC.setOption (ERenderingOption.PAGESET_PAGENUM_TOTAL, nPageCount);
          aRC.setOption (ERenderingOption.TOTAL_PAGENUM_CURRENT, nTotalPageIndex + nPageIndex + 1);
          aRC.setOption (ERenderingOption.TOTAL_PAGENUM_TOTAL, nTotalPageCount);
          m_aPageFooter.perform (aRC);
        }
      }
      finally
      {
        aContentStream.close ();
      }
      ++nPageIndex;
    }
  }
}
