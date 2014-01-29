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
package com.phloc.report.pdf.element;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;

import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.report.pdf.render.PDPageContentStreamWithCache;
import com.phloc.report.pdf.render.PreparationContext;
import com.phloc.report.pdf.render.RenderingContext;
import com.phloc.report.pdf.spec.SizeSpec;

/**
 * Render an image
 * 
 * @author Philip Helger
 */
public class PLImage extends AbstractPLElement <PLImage>
{
  private final BufferedImage m_aImage;
  private final IInputStreamProvider m_aJpeg;
  private float m_fWidth;
  private float m_fHeight;

  public PLImage (@Nonnull final BufferedImage aImage)
  {
    this (aImage, aImage.getWidth (), aImage.getHeight ());
  }

  public PLImage (@Nonnull final BufferedImage aImage, @Nonnegative final float fWidth, @Nonnegative final float fHeight)
  {
    if (aImage == null)
      throw new NullPointerException ("Image");
    if (fWidth <= 0)
      throw new IllegalArgumentException ("Width invalid: " + fWidth);
    if (fHeight <= 0)
      throw new IllegalArgumentException ("Height invalid: " + fHeight);

    m_aImage = aImage;
    m_aJpeg = null;
    m_fWidth = fWidth;
    m_fHeight = fHeight;
  }

  public PLImage (@Nonnull final IInputStreamProvider aJpeg)
  {
    if (aJpeg == null)
      throw new NullPointerException ("Image");

    m_aImage = null;
    m_aJpeg = aJpeg;
    m_fWidth = -1;
    m_fHeight = -1;
  }

  public PLImage (@Nonnull final IInputStreamProvider aImage,
                  @Nonnegative final float fWidth,
                  @Nonnegative final float fHeight)
  {
    if (aImage == null)
      throw new NullPointerException ("Image");
    if (fWidth <= 0)
      throw new IllegalArgumentException ("Width invalid: " + fWidth);
    if (fHeight <= 0)
      throw new IllegalArgumentException ("Height invalid: " + fHeight);

    m_aImage = null;
    m_aJpeg = aImage;
    m_fWidth = fWidth;
    m_fHeight = fHeight;
  }

  @Nullable
  public BufferedImage getImage ()
  {
    return m_aImage;
  }

  @Nullable
  public IInputStreamProvider getJpeg ()
  {
    return m_aJpeg;
  }

  public float getWidth ()
  {
    return m_fWidth;
  }

  public float getHeight ()
  {
    return m_fHeight;
  }

  @Override
  protected SizeSpec onPrepare (@Nonnull final PreparationContext aCtx) throws IOException
  {
    return new SizeSpec (aCtx.getAvailableWidth (), m_fHeight);
  }

  @Override
  protected void onPerform (@Nonnull final RenderingContext aCtx) throws IOException
  {
    final PDPageContentStreamWithCache aContentStream = aCtx.getContentStream ();
    PDJpeg aImage;
    if (m_aJpeg != null)
    {
      aImage = new PDJpeg (aCtx.getDocument (), m_aJpeg.getInputStream ());
    }
    else
    {
      aImage = new PDJpeg (aCtx.getDocument (), m_aImage);
    }
    if (m_fWidth < 0)
    {
      m_fWidth = aImage.getWidth ();
      m_fHeight = aImage.getHeight ();
    }
    aContentStream.drawXObject (aImage,
                                aCtx.getStartLeft () + getPadding ().getLeft (),
                                aCtx.getStartTop () - getPadding ().getTop (),
                                m_fWidth,
                                m_fHeight);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("image", m_aImage)
                            .append ("width", m_fWidth)
                            .append ("height", m_fHeight)
                            .toString ();
  }
}
