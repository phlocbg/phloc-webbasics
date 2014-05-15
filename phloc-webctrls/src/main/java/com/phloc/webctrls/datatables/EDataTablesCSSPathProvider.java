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
package com.phloc.webctrls.datatables;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.css.CSSFilenameHelper;
import com.phloc.html.resource.css.ICSSPathProvider;

/**
 * Contains default CSS paths within this library.
 * 
 * @author Philip Helger
 */
public enum EDataTablesCSSPathProvider implements ICSSPathProvider
{
  DATATABLES_1_9 ("datatables/194/css/jquery.dataTables.css"),
  DATATABLES_THEMEROLLER_1_9 ("datatables/194/css/jquery.dataTables_themeroller.css"),
  DATATABLES_1_10 ("datatables/1.10/css/jquery.dataTables.css"),
  DATATABLES_THEMEROLLER_1_10 ("datatables/1.10/css/jquery.dataTables_themeroller.css"),
  EXTRAS_FIXED_HEADER ("datatables/FixedHeader-2.1.1/dataTables.fixedHeader.css"),
  EXTRAS_SCROLLER ("datatables/Scroller-1.2.1/dataTables.scroller.css");

  private final String m_sPath;

  private EDataTablesCSSPathProvider (@Nonnull @Nonempty final String sPath)
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
