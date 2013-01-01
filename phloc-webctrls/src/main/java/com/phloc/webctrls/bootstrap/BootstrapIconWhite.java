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
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCI;
import com.phloc.webctrls.custom.IIcon;

public class BootstrapIconWhite implements IIcon
{
  private final String m_sCSSClass;

  public BootstrapIconWhite (@Nonnull @Nonempty final String sCSSClass)
  {
    if (StringHelper.hasNoText (sCSSClass))
      throw new IllegalArgumentException ("CSSClass");
    m_sCSSClass = sCSSClass;
  }

  @Nonnull
  @Nonempty
  public String getCSSClass ()
  {
    return m_sCSSClass;
  }

  @Nonnull
  public IHCNode getAsNode ()
  {
    return new HCI ().addClasses (this, EBootstrapIcon.CSS_CLASS_ICON_WHITE);
  }

  @Nonnull
  public <T extends IHCElement <?>> T applyToNode (@Nonnull final T aElement)
  {
    aElement.addClasses (this, EBootstrapIcon.CSS_CLASS_ICON_WHITE);
    return aElement;
  }
}
