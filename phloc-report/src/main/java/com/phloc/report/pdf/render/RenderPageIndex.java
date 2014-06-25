/**
 * Copyright (C) 2014 phloc systems
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
package com.phloc.report.pdf.render;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.string.ToStringGenerator;

/**
 * This class describes the index of the current page.
 * 
 * @author Philip Helger
 */
public class RenderPageIndex
{
  public static final String PLACEHOLDER_PAGESET_INDEX = "${pageset-index}";
  public static final String PLACEHOLDER_PAGESET_PAGE_INDEX = "${pageset-page-index}";
  public static final String PLACEHOLDER_PAGESET_PAGE_NUMBER = "${pageset-page-number}";
  public static final String PLACEHOLDER_PAGESET_PAGE_COUNT = "${pageset-page-count}";
  public static final String PLACEHOLDER_TOTAL_PAGE_INDEX = "${total-page-index}";
  public static final String PLACEHOLDER_TOTAL_PAGE_NUMBER = "${total-page-number}";
  public static final String PLACEHOLDER_TOTAL_PAGE_COUNT = "${total-page-count}";

  private final int m_nPageSetIndex;
  private final int m_nPageSetPageIndex;
  private final int m_nPageSetPageCount;
  private final int m_nTotalPageIndex;
  private final int m_nTotalPageCount;

  public RenderPageIndex (@Nonnegative final int nPageSetIndex,
                          @Nonnegative final int nPageSetPageIndex,
                          @Nonnegative final int nPageSetPageCount,
                          @Nonnegative final int nTotalPageIndex,
                          @Nonnegative final int nTotalPageCount)

  {
    if (nPageSetIndex < 0)
      throw new IllegalArgumentException ("PageSetIndex");
    if (nPageSetPageIndex < 0)
      throw new IllegalArgumentException ("PageSetPageIndex");
    if (nPageSetPageCount < 0)
      throw new IllegalArgumentException ("PageSetPageCount");
    if (nTotalPageIndex < 0)
      throw new IllegalArgumentException ("TotalPageIndex");
    if (nTotalPageCount < 0)
      throw new IllegalArgumentException ("TotalPageCount");
    m_nPageSetIndex = nPageSetIndex;
    m_nPageSetPageIndex = nPageSetPageIndex;
    m_nPageSetPageCount = nPageSetPageCount;
    m_nTotalPageIndex = nTotalPageIndex;
    m_nTotalPageCount = nTotalPageCount;
  }

  /**
   * @return The index of the current page set. 0-based. Always &ge; 0.
   */
  @Nonnegative
  public int getPageSetIndex ()
  {
    return m_nPageSetIndex;
  }

  /**
   * @return The index of the page in the current page set. 0-based. Always &ge;
   *         0.
   */
  @Nonnegative
  public int getPageSetPageIndex ()
  {
    return m_nPageSetPageIndex;
  }

  /**
   * @return The number of the page in the current page set. 1-based. Always
   *         &ge; 1.
   */
  @Nonnegative
  public int getPageSetPageNumber ()
  {
    return m_nPageSetPageIndex + 1;
  }

  /**
   * @return The total number of pages in the current page set. Always &ge; 0.
   */
  @Nonnegative
  public int getPageSetPageCount ()
  {
    return m_nPageSetPageCount;
  }

  /**
   * @return The index of the page over all page sets. 0-based. Always &ge; 0.
   */
  @Nonnegative
  public int getTotalPageIndex ()
  {
    return m_nTotalPageIndex;
  }

  /**
   * @return The number of the page over all page sets. 1-based. Always &ge; 1.
   */
  @Nonnegative
  public int getTotalPageNumber ()
  {
    return m_nTotalPageIndex + 1;
  }

  /**
   * @return The overall number of pages. Always &ge; 0.
   */
  @Nonnegative
  public int getTotalPageCount ()
  {
    return m_nTotalPageCount;
  }

  public void setPlaceholdersInRenderingContext (@Nonnull final RenderingContext aRC)
  {
    aRC.setPlaceholder (PLACEHOLDER_PAGESET_INDEX, getPageSetIndex ());
    aRC.setPlaceholder (PLACEHOLDER_PAGESET_PAGE_INDEX, getPageSetPageIndex ());
    aRC.setPlaceholder (PLACEHOLDER_PAGESET_PAGE_NUMBER, getPageSetPageNumber ());
    aRC.setPlaceholder (PLACEHOLDER_PAGESET_PAGE_COUNT, getPageSetPageCount ());
    aRC.setPlaceholder (PLACEHOLDER_TOTAL_PAGE_INDEX, getTotalPageIndex ());
    aRC.setPlaceholder (PLACEHOLDER_TOTAL_PAGE_NUMBER, getTotalPageNumber ());
    aRC.setPlaceholder (PLACEHOLDER_TOTAL_PAGE_COUNT, getTotalPageCount ());
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("pageSetIndex", m_nPageSetIndex)
                                       .append ("pageSetPageIndex", m_nPageSetPageIndex)
                                       .append ("pageSetPageCount", m_nPageSetPageCount)
                                       .append ("tTotalPageIndex", m_nTotalPageIndex)
                                       .append ("totalPageCount", m_nTotalPageCount)
                                       .toString ();
  }
}
