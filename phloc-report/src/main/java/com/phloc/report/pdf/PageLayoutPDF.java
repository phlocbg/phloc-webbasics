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
package com.phloc.report.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.vendor.VendorInfo;
import com.phloc.report.pdf.element.PLPageSet;
import com.phloc.report.pdf.element.PLPageSet.PageSetPrepareResult;

public class PageLayoutPDF
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (PageLayoutPDF.class);

  private String m_sDocumentTitle;
  private String m_sDocumentKeywords;

  private boolean m_bDebug = false;
  private final List <PLPageSet> m_aPageSets = new ArrayList <PLPageSet> ();

  public PageLayoutPDF ()
  {}

  public boolean isDebug ()
  {
    return m_bDebug;
  }

  @Nonnull
  public PageLayoutPDF setDebug (final boolean bDebug)
  {
    m_bDebug = bDebug;
    return this;
  }

  @Nullable
  public String getDocumentTitle ()
  {
    return m_sDocumentTitle;
  }

  public void setDocumentTitle (@Nullable final String sDocumentTitle)
  {
    m_sDocumentTitle = sDocumentTitle;
  }

  @Nullable
  public String getDocumentKeywords ()
  {
    return m_sDocumentKeywords;
  }

  public void setDocumentKeywords (@Nullable final String sDocumentKeywords)
  {
    m_sDocumentKeywords = sDocumentKeywords;
  }

  @Nonnull
  public List <? extends PLPageSet> getAllPageSets ()
  {
    return ContainerHelper.newList (m_aPageSets);
  }

  public void addPageSet (@Nonnull final PLPageSet aPageSet)
  {
    if (aPageSet == null)
      throw new NullPointerException ("PageSet");
    m_aPageSets.add (aPageSet);
  }

  public void renderTo (@Nonnull @WillClose final OutputStream aOS) throws PDFCreationException
  {
    // create a new invoice pdf
    PDDocument aDoc = null;

    try
    {
      aDoc = new PDDocument ();

      // Set document properties
      {
        final PDDocumentInformation aProperties = new PDDocumentInformation ();
        aProperties.setAuthor (VendorInfo.getVendorName () + " " + VendorInfo.getVendorURLWithoutProtocol ());
        aProperties.setCreationDate (Calendar.getInstance ());
        aProperties.setCreator (VendorInfo.getVendorName ());
        if (StringHelper.hasText (m_sDocumentKeywords))
          aProperties.setKeywords (m_sDocumentKeywords);
        if (StringHelper.hasText (m_sDocumentTitle))
        {
          aProperties.setSubject (m_sDocumentTitle);
          aProperties.setTitle (m_sDocumentTitle);
        }
        // add the created properties
        aDoc.setDocumentInformation (aProperties);
      }

      // Prepare all page sets
      final PageSetPrepareResult [] aPRs = new PageSetPrepareResult [m_aPageSets.size ()];
      int nIndex = 0;
      int nTotalPageCount = 0;
      for (final PLPageSet aPageSet : m_aPageSets)
      {
        final PageSetPrepareResult aPR = aPageSet.prepareAllPages ();
        aPRs[nIndex++] = aPR;
        nTotalPageCount += aPR.getPageCount ();
      }

      // Start applying content
      nIndex = 0;
      int nTotalPageIndex = 0;
      for (final PLPageSet aPageSet : m_aPageSets)
      {
        final PageSetPrepareResult aPR = aPRs[nIndex++];
        aPageSet.renderAllPages (aPR, aDoc, m_bDebug, nTotalPageIndex, nTotalPageCount);
        // Inc afterwards
        nTotalPageIndex += aPR.getPageCount ();
      }

      // save document to output stream
      aDoc.save (aOS);

      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("PDF successfully created");
    }
    catch (final IOException ex)
    {
      throw new PDFCreationException ("IO Error", ex);
    }
    catch (final COSVisitorException ex)
    {
      throw new PDFCreationException ("Internal error", ex);
    }
    finally
    {
      // close document
      if (aDoc != null)
      {
        try
        {
          aDoc.close ();
        }
        catch (final IOException ex)
        {
          s_aLogger.error ("Failed to close PDF document", ex);
        }
      }

      // Necessary in case of an exception
      StreamUtils.close (aOS);
    }
  }
}
