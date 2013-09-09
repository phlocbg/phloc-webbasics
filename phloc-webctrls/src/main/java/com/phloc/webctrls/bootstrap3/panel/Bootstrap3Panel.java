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
package com.phloc.webctrls.bootstrap3.panel;

import javax.annotation.Nonnull;

import com.phloc.html.hc.html.AbstractHCDiv;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.webctrls.bootstrap3.CBootstrap3CSS;

/**
 * Wrapper for a Bootstrap3 panel.
 * 
 * @author Philip Helger
 */
public class Bootstrap3Panel extends AbstractHCDiv <Bootstrap3Panel>
{
  private final EBootstrap3PanelType m_eType;
  private HCDiv m_aHeader = null;
  private final HCDiv m_aBody;
  private HCDiv m_aFooter = null;

  public Bootstrap3Panel ()
  {
    this (EBootstrap3PanelType.DEFAULT);
  }

  /**
   * Create a new Panel element
   */
  public Bootstrap3Panel (@Nonnull final EBootstrap3PanelType eType)
  {
    if (eType == null)
      throw new NullPointerException ("Type");

    m_eType = eType;
    m_aBody = addAndReturnChild (new HCDiv ().addClasses (CBootstrap3CSS.PANEL, eType));
  }

  @Nonnull
  public EBootstrap3PanelType getType ()
  {
    return m_eType;
  }

  public boolean hasHeader ()
  {
    return m_aHeader != null;
  }

  @Nonnull
  public HCDiv getOrCreateHeader ()
  {
    if (m_aHeader == null)
    {
      m_aHeader = new HCDiv ().addClass (CBootstrap3CSS.PANEL_HEADING);
      addChild (0, m_aHeader);
    }
    return m_aHeader;
  }

  @Nonnull
  public HCDiv getBody ()
  {
    return m_aBody;
  }

  public boolean hasFooter ()
  {
    return m_aFooter != null;
  }

  @Nonnull
  public HCDiv getOrCreateFooter ()
  {
    if (m_aFooter == null)
    {
      m_aFooter = new HCDiv ().addClass (CBootstrap3CSS.PANEL_FOOTER);
      addChild (m_aFooter);
    }
    return m_aFooter;
  }
}
