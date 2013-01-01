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
package com.phloc.webctrls.bootstrap.ext;

import javax.annotation.Nonnull;

import com.phloc.commons.version.Version;
import com.phloc.html.EHTMLVersion;
import com.phloc.html.hc.IHCBaseNode;
import com.phloc.html.hc.IHCNodeWithChildren;
import com.phloc.html.hc.customize.HCDefaultCustomizer;
import com.phloc.webctrls.bootstrap.BootstrapDropDownMenu;

public class BootstrapCustomizer extends HCDefaultCustomizer
{
  private final Version m_aBootstrapVersion;

  public BootstrapCustomizer (@Nonnull final Version aBootstrapVersion)
  {
    if (aBootstrapVersion == null)
      throw new NullPointerException ("bootstrapVersion");
    m_aBootstrapVersion = aBootstrapVersion;
  }

  @Override
  public void customizeNode (@Nonnull final IHCNodeWithChildren <?> aParentElement,
                             @Nonnull final IHCBaseNode aNode,
                             @Nonnull final EHTMLVersion eHTMLVersion)
  {
    super.customizeNode (aParentElement, aNode, eHTMLVersion);

    if (aNode instanceof BootstrapDropDownMenu)
    {
      EBootstrapWorkarounds.IPAD_DROPDOWN_FIX.appendIfApplicable (m_aBootstrapVersion, aParentElement);
    }
  }
}
