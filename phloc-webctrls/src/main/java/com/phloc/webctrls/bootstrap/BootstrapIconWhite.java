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
package com.phloc.webctrls.bootstrap;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCI;
import com.phloc.webctrls.custom.IIcon;

public class BootstrapIconWhite implements IIcon
{
  private final EBootstrapIcon m_eSrcIcon;

  public BootstrapIconWhite (@Nonnull final EBootstrapIcon eSrcIcon)
  {
    if (eSrcIcon == null)
      throw new NullPointerException ("Icon");
    m_eSrcIcon = eSrcIcon;
  }

  @Nonnull
  public EBootstrapIcon getSrcIcon ()
  {
    return m_eSrcIcon;
  }

  @Nonnull
  @Nonempty
  public String getCSSClass ()
  {
    return m_eSrcIcon.getCSSClass ();
  }

  @Nonnull
  public IHCNode getAsNode ()
  {
    return new HCI ().addClasses (m_eSrcIcon, EBootstrapIcon.CSS_CLASS_ICON_WHITE);
  }

  @Nonnull
  public <T extends IHCElement <?>> T applyToNode (@Nonnull final T aElement)
  {
    aElement.addClasses (m_eSrcIcon, EBootstrapIcon.CSS_CLASS_ICON_WHITE);
    return aElement;
  }
}
