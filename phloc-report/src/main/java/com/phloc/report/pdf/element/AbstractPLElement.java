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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.lang.CGStringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.report.pdf.render.PDPageContentStreamWithCache;
import com.phloc.report.pdf.render.RenderPreparationContext;
import com.phloc.report.pdf.render.RenderingContext;
import com.phloc.report.pdf.spec.BorderSpec;
import com.phloc.report.pdf.spec.BorderStyleSpec;
import com.phloc.report.pdf.spec.SizeSpec;

/**
 * Abstract layout element that supports rendering.
 * 
 * @author Philip Helger
 * @param <IMPLTYPE>
 *        The implementation type of this class.
 */
public abstract class AbstractPLElement <IMPLTYPE extends AbstractPLElement <IMPLTYPE>> extends AbstractPLBaseElement <IMPLTYPE>
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractPLElement.class);

  private boolean m_bPrepared = false;
  private SizeSpec m_aPreparedSize;

  public AbstractPLElement ()
  {}

  /**
   * Throw an exception, if this object is already prepared.
   * 
   * @throws IllegalStateException
   *         if already prepared
   */
  @Override
  protected final void checkNotPrepared ()
  {
    if (isPrepared ())
      throw new IllegalStateException ("This object was already prepared and can therefore not be modified!");
  }

  /**
   * @return <code>true</code> if this object was already prepared,
   *         <code>false</code> otherwise.
   */
  protected final boolean isPrepared ()
  {
    return m_bPrepared;
  }

  /**
   * The abstract method that must be implemented by all subclasses. It is
   * ensured that this method is called only once per instance!
   * 
   * @param aCtx
   *        Preparation context. Never <code>null</code>.
   * @return The size of the rendered element
   * @throws IOException
   *         on error
   */
  @Nonnull
  protected abstract SizeSpec onPrepare (@Nonnull final RenderPreparationContext aCtx) throws IOException;

  /**
   * Prepare this element once for rendering.
   * 
   * @param aCtx
   *        The preparation context
   * @return The net size of the rendered object without padding or margin. May
   *         not be <code>null</code>.
   * @throws IOException
   */
  @Nonnull
  public final SizeSpec prepare (@Nonnull final RenderPreparationContext aCtx) throws IOException
  {
    // Prepare only once!
    if (m_bPrepared)
    {
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Already prepared object " + CGStringHelper.getClassLocalName (getClass ()));
    }
    else
    {
      // Do prepare
      m_bPrepared = true;
      m_aPreparedSize = onPrepare (aCtx);
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Prepared object " + CGStringHelper.getClassLocalName (getClass ()));
    }
    return m_aPreparedSize;
  }

  /**
   * Abstract method to be implemented by subclasses.
   * 
   * @param aCtx
   *        Rendering context
   * @throws IOException
   */
  @Nonnegative
  protected abstract void onPerform (@Nonnull RenderingContext aCtx) throws IOException;

  /**
   * Second step: perform.
   * 
   * @param aCtx
   *        Rendering context
   * @throws IOException
   */
  @Nonnegative
  public final void perform (@Nonnull final RenderingContext aCtx) throws IOException
  {
    if (!m_bPrepared)
      throw new IllegalStateException ("Element " +
                                       CGStringHelper.getClassLocalName (getClass ()) +
                                       " was never prepared!");

    // Render border - debug: green
    {
      final PDPageContentStreamWithCache aContentStream = aCtx.getContentStream ();
      final float fLeft = aCtx.getStartLeft ();
      final float fTop = aCtx.getStartTop ();
      final float fWidth = m_aPreparedSize.getWidth () + getPadding ().getXSum ();
      final float fHeight = m_aPreparedSize.getHeight () + getPadding ().getYSum ();

      // Fill before border
      if (getFillColor () != null)
      {
        aContentStream.setNonStrokingColor (getFillColor ());
        aContentStream.fillRect (fLeft, fTop - fHeight, fWidth, fHeight);
      }

      BorderSpec aRealBorder = getBorder ();
      if (shouldApplyDebugBorder (aRealBorder, aCtx.isDebugMode ()))
        aRealBorder = new BorderSpec (new BorderStyleSpec (new Color (0, 255, 0)));
      if (aRealBorder.hasAnyBorder ())
        renderBorder (aContentStream, fLeft, fTop, fWidth, fHeight, aRealBorder);
    }

    // Main perform after border
    onPerform (aCtx);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("prepared", m_bPrepared)
                            .appendIfNotNull ("preparedSize", m_aPreparedSize)
                            .toString ();
  }
}
