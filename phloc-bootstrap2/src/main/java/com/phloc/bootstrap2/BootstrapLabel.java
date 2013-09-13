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
package com.phloc.bootstrap2;

import javax.annotation.Nonnull;

import com.phloc.html.hc.html.HCButton;

/**
 * Represents a Bootstrap label
 * 
 * @author Philip Helger
 */
public class BootstrapLabel extends HCButton
{
  private EBootstrapLabelType m_eType = EBootstrapLabelType.DEFAULT;

  private void _init ()
  {
    addClass (CBootstrapCSS.LABEL);
  }

  public BootstrapLabel ()
  {
    super ();
    _init ();
  }

  @Nonnull
  public EBootstrapLabelType getLabelType ()
  {
    return m_eType;
  }

  @Nonnull
  public BootstrapLabel setType (@Nonnull final EBootstrapLabelType eType)
  {
    if (eType == null)
      throw new NullPointerException ("type");
    removeClass (m_eType);
    m_eType = eType;
    addClass (m_eType);
    return this;
  }
}
