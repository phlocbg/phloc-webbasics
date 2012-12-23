/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webctrls.tiptip;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.css.CSSFilenameHelper;
import com.phloc.html.resource.css.ICSSPathProvider;

/**
 * Contains default CSS paths within this library.
 * 
 * @author philip
 */
public enum ETipTipCSSPathProvider implements ICSSPathProvider
{
  TOOLTIP ("tiptip/tooltip.css"),
  TIPTIP_13 ("tiptip/13/jquery.tiptip.css");

  private final String m_sPath;

  private ETipTipCSSPathProvider (@Nonnull @Nonempty final String sPath)
  {
    if (!CSSFilenameHelper.isCSSFilename (sPath))
      throw new IllegalArgumentException ("path");
    m_sPath = sPath;
  }

  @Nonnull
  @Nonempty
  public String getCSSItemPath (final boolean bRegular)
  {
    return bRegular ? m_sPath : CSSFilenameHelper.getMinifiedCSSFilename (m_sPath);
  }
}
