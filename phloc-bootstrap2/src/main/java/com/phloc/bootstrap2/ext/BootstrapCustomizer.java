/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.bootstrap2.ext;

import javax.annotation.Nonnull;

import com.phloc.bootstrap2.BootstrapDropDownMenu;
import com.phloc.commons.gfx.ScalableSize;
import com.phloc.commons.version.Version;
import com.phloc.css.ECSSUnit;
import com.phloc.css.property.CCSSProperties;
import com.phloc.html.EHTMLVersion;
import com.phloc.html.hc.IHCHasChildrenMutable;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.customize.HCEmptyCustomizer;
import com.phloc.html.hc.html.HCImg;

public class BootstrapCustomizer extends HCEmptyCustomizer
{
  private final Version m_aBootstrapVersion;

  public BootstrapCustomizer (@Nonnull final Version aBootstrapVersion)
  {
    if (aBootstrapVersion == null)
      throw new NullPointerException ("bootstrapVersion");
    this.m_aBootstrapVersion = aBootstrapVersion;
  }

  @Override
  public void customizeNode (@Nonnull final IHCHasChildrenMutable <?, ? super IHCNode> aParentElement,
                             @Nonnull final IHCNode aNode,
                             @Nonnull final EHTMLVersion eHTMLVersion)
  {
    if (aNode instanceof HCImg)
    {
      final HCImg aImg = (HCImg) aNode;
      final ScalableSize aExtent = aImg.getExtent ();
      // Workaround for IE if a CSS contains "width:auto" and/or "height:auto"
      // See e.g. https://github.com/twitter/bootstrap/issues/1899
      if (aExtent != null)
      {
        aImg.addStyles (CCSSProperties.WIDTH.newValue (ECSSUnit.px (aExtent.getWidth ())),
                        CCSSProperties.HEIGHT.newValue (ECSSUnit.px (aExtent.getHeight ())));
      }
    }
    else
      if (aNode instanceof BootstrapDropDownMenu)
      {
        EBootstrapWorkarounds.IPAD_DROPDOWN_FIX.appendIfApplicable (this.m_aBootstrapVersion, aParentElement);
      }
  }
}
