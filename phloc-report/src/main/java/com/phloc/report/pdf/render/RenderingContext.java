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
package com.phloc.report.pdf.render;

import java.util.EnumMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class RenderingContext
{
  private final PDPageContentStreamWithCache m_aCS;
  private final boolean m_bDebugMode;
  private final float m_fStartLeft;
  private final float m_fStartTop;
  private final float m_fWidth;
  private final float m_fHeight;
  private final Map <ERenderingOption, String> m_aOptions = new EnumMap <ERenderingOption, String> (ERenderingOption.class);

  /**
   * @param aCtx
   *        Context to copy settings from
   * @param fStartLeft
   *        Absolute page x-start position with element x-margin but without
   *        element x-padding
   * @param fStartTop
   *        Absolute page y-start position with element y-margin but without
   *        element y-padding
   * @param fWidth
   *        width without margin but including padding of the surrounding
   *        element
   * @param fHeight
   *        width without margin but including padding of the surrounding
   *        element
   */
  public RenderingContext (@Nonnull final RenderingContext aCtx,
                           final float fStartLeft,
                           final float fStartTop,
                           final float fWidth,
                           final float fHeight)
  {
    this (aCtx.getContentStream (), aCtx.isDebugMode (), fStartLeft, fStartTop, fWidth, fHeight);
    m_aOptions.putAll (aCtx.m_aOptions);
  }

  /**
   * @param aCS
   *        Page content stream
   * @param bDebugMode
   *        debug mode?
   * @param fStartLeft
   *        Absolute page x-start position with element x-margin but without
   *        element x-padding
   * @param fStartTop
   *        Absolute page y-start position with element y-margin but without
   *        element y-padding
   * @param fWidth
   *        width without margin but including padding of the surrounding
   *        element
   * @param fHeight
   *        width without margin but including padding of the surrounding
   *        element
   */
  public RenderingContext (@Nonnull final PDPageContentStreamWithCache aCS,
                           final boolean bDebugMode,
                           final float fStartLeft,
                           final float fStartTop,
                           final float fWidth,
                           final float fHeight)
  {
    m_aCS = aCS;
    m_bDebugMode = bDebugMode;
    m_fStartLeft = fStartLeft;
    m_fStartTop = fStartTop;
    m_fWidth = fWidth;
    m_fHeight = fHeight;
  }

  @Nonnull
  public RenderingContext setOption (@Nonnull final ERenderingOption eOption, final int nValue)
  {
    return setOption (eOption, Integer.toString (nValue));
  }

  @Nonnull
  public RenderingContext setOption (@Nonnull final ERenderingOption eOption, @Nonnull final String sValue)
  {
    if (eOption == null)
      throw new NullPointerException ("option");
    if (sValue == null)
      throw new NullPointerException ("value");
    m_aOptions.put (eOption, sValue);
    return this;
  }

  @Nullable
  public String getOption (@Nonnull final ERenderingOption eOption)
  {
    if (eOption == null)
      throw new NullPointerException ("option");
    return m_aOptions.get (eOption);
  }

  @Nonnull
  public PDPageContentStreamWithCache getContentStream ()
  {
    return m_aCS;
  }

  public boolean isDebugMode ()
  {
    return m_bDebugMode;
  }

  /**
   * @return Absolute page x-start position with element x-margin but without
   *         element x-padding
   */
  public float getStartLeft ()
  {
    return m_fStartLeft;
  }

  /**
   * @return Absolute page y-start position with element y-margin but without
   *         element y-padding
   */
  public float getStartTop ()
  {
    return m_fStartTop;
  }

  /**
   * @return width without margin but including padding of the surrounding
   *         element
   */
  public float getWidth ()
  {
    return m_fWidth;
  }

  /**
   * @return width without margin but including padding of the surrounding
   *         element
   */
  public float getHeight ()
  {
    return m_fHeight;
  }
}
