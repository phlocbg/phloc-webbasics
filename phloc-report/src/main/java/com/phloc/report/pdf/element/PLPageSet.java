/**
 * Copyright (C) 2013 phloc systems
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.lang.CGStringHelper;
import com.phloc.report.pdf.element.IPLSplittableElement.SplitResult;
import com.phloc.report.pdf.render.ERenderingElementType;
import com.phloc.report.pdf.render.IRenderingContextCustomizer;
import com.phloc.report.pdf.render.PDPageContentStreamWithCache;
import com.phloc.report.pdf.render.RenderPageIndex;
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
    private final List <PLElementWithHeight> m_aContentHeight = new ArrayList <PLElementWithHeight> ();
    private float m_fFooterHeight = Float.NaN;
    private final List <List <PLElementWithHeight>> m_aPerPageElements = new ArrayList <List <PLElementWithHeight>> ();

    PageSetPrepareResult ()
    {}

    /**
     * Set the page header height.
     * 
     * @param fHeaderHeight
     *        Height without padding or margin.
     */
    void setHeaderHeight (final float fHeaderHeight)
    {
      m_fHeaderHeight = fHeaderHeight;
    }

    /**
     * @return Page header height without padding or margin.
     */
    public float getHeaderHeight ()
    {
      return m_fHeaderHeight;
    }

    /**
     * @param aElement
     *        The element to be added. May not be <code>null</code>. The element
     *        height must be without padding or margin.
     */
    void addElement (@Nonnull final PLElementWithHeight aElement)
    {
      if (aElement == null)
        throw new NullPointerException ("element");
      m_aContentHeight.add (aElement);
    }

    /**
     * @return A list of all elements. Never <code>null</code>. The height of
     *         the contained elements is without padding or margin.
     */
    @Nonnull
    @ReturnsMutableCopy
    List <PLElementWithHeight> getAllElements ()
    {
      return ContainerHelper.newList (m_aContentHeight);
    }

    /**
     * Set the page footer height.
     * 
     * @param fFooterHeight
     *        Height without padding or margin.
     */
    void setFooterHeight (final float fFooterHeight)
    {
      m_fFooterHeight = fFooterHeight;
    }

    /**
     * @return Page footer height without padding or margin.
     */
    public float getFooterHeight ()
    {
      return m_fFooterHeight;
    }

    void addPerPageElements (@Nonnull @Nonempty final List <PLElementWithHeight> aCurPageElements)
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
    List <List <PLElementWithHeight>> directGetPerPageElements ()
    {
      return m_aPerPageElements;
    }
  }

  private static final Logger s_aLogger = LoggerFactory.getLogger (PLPageSet.class);

  private final SizeSpec m_aPageSize;
  private AbstractPLElement <?> m_aPageHeader;
  private final List <AbstractPLElement <?>> m_aElements = new ArrayList <AbstractPLElement <?>> ();
  private AbstractPLElement <?> m_aPageFooter;
  private IRenderingContextCustomizer m_aRCCustomizer;

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

  @Nullable
  public IRenderingContextCustomizer getRenderingContextCustomizer ()
  {
    return m_aRCCustomizer;
  }

  @Nonnull
  public PLPageSet setRenderingContextCustomizer (@Nullable final IRenderingContextCustomizer aRCCustomizer)
  {
    m_aRCCustomizer = aRCCustomizer;
    return this;
  }

  /**
   * @return The usable page width without the x-paddings and x-margins
   */
  @Nonnegative
  public float getAvailableWidth ()
  {
    return m_aPageSize.getWidth () - getMarginPlusPaddingXSum ();
  }

  /**
   * @return The usable page height without the y-paddings and y-margins
   */
  @Nonnegative
  public float getAvailableHeight ()
  {
    return m_aPageSize.getHeight () - getMarginPlusPaddingYSum ();
  }

  /**
   * @return The global page header. May be <code>null</code>.
   */
  @Nullable
  public AbstractPLElement <?> getPageHeader ()
  {
    return m_aPageHeader;
  }

  /**
   * Set the global page header
   * 
   * @param aPageHeader
   *        The global page header. May be <code>null</code>.
   * @return this
   */
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

  /**
   * @return The global page footer. May be <code>null</code>.
   */
  @Nullable
  public AbstractPLElement <?> getPageFooter ()
  {
    return m_aPageFooter;
  }

  /**
   * Set the global page footer
   * 
   * @param aPageFooter
   *        The global page footer. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public PLPageSet setPageFooter (@Nullable final AbstractPLElement <?> aPageFooter)
  {
    m_aPageFooter = aPageFooter;
    return this;
  }

  /**
   * @return The y-top of the page
   */
  public float getYTop ()
  {
    return m_aPageSize.getHeight () - getMargin ().getTop () - getPadding ().getTop ();
  }

  @Nonnull
  public PageSetPrepareResult prepareAllPages () throws IOException
  {
    // The result element
    final PageSetPrepareResult ret = new PageSetPrepareResult ();

    // Prepare page header
    if (m_aPageHeader != null)
    {
      // Page header does not care about page padding
      final RenderPreparationContext aRPC = new RenderPreparationContext (m_aPageSize.getWidth () -
                                                                              getMargin ().getXSum () -
                                                                              m_aPageHeader.getMarginPlusPaddingXSum (),
                                                                          getMargin ().getTop () -
                                                                              m_aPageHeader.getMarginPlusPaddingYSum ());
      final SizeSpec aElementSize = m_aPageHeader.prepare (aRPC);
      ret.setHeaderHeight (aElementSize.getHeight ());
    }

    // Prepare content elements
    for (final AbstractPLElement <?> aElement : m_aElements)
    {
      final RenderPreparationContext aRPC = new RenderPreparationContext (getAvailableWidth () -
                                                                              aElement.getMarginPlusPaddingXSum (),
                                                                          getAvailableHeight () -
                                                                              aElement.getMarginPlusPaddingYSum ());
      final SizeSpec aElementSize = aElement.prepare (aRPC);
      ret.addElement (new PLElementWithHeight (aElement, aElementSize.getHeight ()));
    }

    // Prepare footer
    if (m_aPageFooter != null)
    {
      // Page footer does not care about page padding
      final RenderPreparationContext aRPC = new RenderPreparationContext (m_aPageSize.getWidth () -
                                                                              getMargin ().getXSum () -
                                                                              m_aPageFooter.getMarginPlusPaddingXSum (),
                                                                          getMargin ().getBottom () -
                                                                              m_aPageFooter.getMarginPlusPaddingYSum ());
      final SizeSpec aElementSize = m_aPageFooter.prepare (aRPC);
      ret.setFooterHeight (aElementSize.getHeight ());
    }

    // Split into page pieces
    final float fYTop = getYTop ();
    final float fYLeast = getMargin ().getBottom () + getPadding ().getBottom ();

    {
      List <PLElementWithHeight> aCurPageElements = new ArrayList <PLElementWithHeight> ();

      // Start at the top
      float fCurY = fYTop;

      // Create a copy of the list, so that we can safely modify it
      final List <PLElementWithHeight> aElementsWithHeight = ret.getAllElements ();
      while (!aElementsWithHeight.isEmpty ())
      {
        // Use the first element
        final PLElementWithHeight aElementWithHeight = aElementsWithHeight.remove (0);
        final AbstractPLElement <?> aElement = aElementWithHeight.getElement ();
        final float fThisHeightFull = aElementWithHeight.getHeightFull ();
        if (fCurY - fThisHeightFull < fYLeast)
        {
          // Next page
          if (aCurPageElements.isEmpty ())
          {
            // one element too large for a page
            if (aElement instanceof IPLSplittableElement)
            {
              // split elements
              final float fAvailableHeight = fCurY - fYLeast - aElement.getMarginPlusPaddingYSum ();
              final SplitResult aSplitResult = ((IPLSplittableElement) aElement).splitElements (fAvailableHeight);
              if (aSplitResult != null)
              {
                // Re-add them to the list and try again
                aElementsWithHeight.add (0, aSplitResult.getFirstElement ());
                aElementsWithHeight.add (1, aSplitResult.getSecondElement ());

                if (s_aLogger.isDebugEnabled ())
                  s_aLogger.debug ("Split " +
                                   CGStringHelper.getClassLocalName (aElement) +
                                   " into pieces: " +
                                   aSplitResult.getFirstElement ().getHeight () +
                                   " and " +
                                   aSplitResult.getSecondElement ().getHeight ());
                continue;
              }
              s_aLogger.warn ("A single element does not fit onto a single page even though it is splittable!");
            }
            else
              s_aLogger.warn ("A single element does not fit onto a single page and is not splittable!");
          }
          else
          {
            // We found elements fitting onto a page
            ret.addPerPageElements (aCurPageElements);
            aCurPageElements = new ArrayList <PLElementWithHeight> ();
            fCurY = fYTop;

            // Re-add element and continue from start, so that splitting happens
            aElementsWithHeight.add (0, aElementWithHeight);
            continue;
          }
        }

        // Add element to current page
        aCurPageElements.add (aElementWithHeight);
        fCurY -= fThisHeightFull;
      }

      // Add elements to last page
      if (!aCurPageElements.isEmpty ())
        ret.addPerPageElements (aCurPageElements);
    }

    return ret;
  }

  /**
   * Render all pages of this layout to the specified PDDocument
   * 
   * @param aPrepareResult
   *        The preparation result. May not be <code>null</code>.
   * @param aDoc
   *        The PDDocument. May not be <code>null</code>.
   * @param bDebug
   *        <code>true</code> for debug output
   * @param nPageSetIndex
   *        Page set index. Always &ge; 0.
   * @param nTotalPageStartIndex
   *        Total page index. Always &ge; 0.
   * @param nTotalPageCount
   *        Total page count. Always &ge; 0.
   * @throws IOException
   */
  public void renderAllPages (@Nonnull final PageSetPrepareResult aPrepareResult,
                              @Nonnull final PDDocument aDoc,
                              final boolean bDebug,
                              @Nonnegative final int nPageSetIndex,
                              @Nonnegative final int nTotalPageStartIndex,
                              @Nonnegative final int nTotalPageCount) throws IOException
  {
    // Start at the left
    final float fXLeft = getMargin ().getLeft () + getPadding ().getLeft ();
    final float fYTop = getYTop ();

    final boolean bCompressPDF = !bDebug;
    int nPageIndex = 0;
    final int nPageCount = aPrepareResult.getPageCount ();
    for (final List <PLElementWithHeight> aPerPage : aPrepareResult.directGetPerPageElements ())
    {
      final RenderPageIndex aPageIndex = new RenderPageIndex (nPageSetIndex,
                                                              nPageIndex,
                                                              nPageCount,
                                                              nTotalPageStartIndex + nPageIndex,
                                                              nTotalPageCount);

      // Layout in memory
      final PDPage aPage = new PDPage (m_aPageSize.getAsRectangle ());
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
          final RenderingContext aRC = new RenderingContext (ERenderingElementType.PAGE_HEADER,
                                                             aContentStream,
                                                             bDebug,
                                                             getMargin ().getLeft () +
                                                                 m_aPageHeader.getMargin ().getLeft (),
                                                             m_aPageSize.getHeight () -
                                                                 m_aPageHeader.getMargin ().getTop (),
                                                             m_aPageSize.getWidth () -
                                                                 getMargin ().getXSum () -
                                                                 m_aPageHeader.getMargin ().getXSum (),
                                                             aPrepareResult.getHeaderHeight () +
                                                                 m_aPageHeader.getPadding ().getYSum ());
          aPageIndex.setPlaceholdersInRenderingContext (aRC);
          if (m_aRCCustomizer != null)
            m_aRCCustomizer.customizeRenderingContext (aRC);
          m_aPageHeader.perform (aRC);
        }

        float fCurY = fYTop;
        for (final PLElementWithHeight aElementWithHeight : aPerPage)
        {
          final AbstractPLElement <?> aElement = aElementWithHeight.getElement ();
          // Get element height
          final float fThisHeight = aElementWithHeight.getHeight ();
          final float fThisHeightWithPadding = fThisHeight + aElement.getPadding ().getYSum ();

          final RenderingContext aRC = new RenderingContext (ERenderingElementType.CONTENT_ELEMENT,
                                                             aContentStream,
                                                             bDebug,
                                                             fXLeft + aElement.getMargin ().getLeft (),
                                                             fCurY - aElement.getMargin ().getTop (),
                                                             getAvailableWidth () - aElement.getMargin ().getXSum (),
                                                             fThisHeightWithPadding);
          aPageIndex.setPlaceholdersInRenderingContext (aRC);
          if (m_aRCCustomizer != null)
            m_aRCCustomizer.customizeRenderingContext (aRC);
          aElement.perform (aRC);

          fCurY -= fThisHeightWithPadding + aElement.getMargin ().getYSum ();
        }

        if (m_aPageFooter != null)
        {
          // Page footer does not care about page padding
          // footer top-left
          final RenderingContext aRC = new RenderingContext (ERenderingElementType.PAGE_FOOTER,
                                                             aContentStream,
                                                             bDebug,
                                                             getMargin ().getLeft () +
                                                                 m_aPageFooter.getMargin ().getLeft (),
                                                             getMargin ().getBottom () -
                                                                 m_aPageFooter.getMargin ().getTop (),
                                                             m_aPageSize.getWidth () -
                                                                 getMargin ().getXSum () -
                                                                 m_aPageFooter.getMargin ().getXSum (),
                                                             aPrepareResult.getFooterHeight () +
                                                                 m_aPageFooter.getPadding ().getYSum ());
          aPageIndex.setPlaceholdersInRenderingContext (aRC);
          if (m_aRCCustomizer != null)
            m_aRCCustomizer.customizeRenderingContext (aRC);
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
