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
import com.phloc.html.resource.js.IJSPathProvider;
import com.phloc.html.resource.js.JSFilenameHelper;

public enum EDataTablesJSPathProvider implements IJSPathProvider
{
  DATATABLES_1_10 ("datatables/1.10/js/jquery.dataTables.js"),
  EXTRAS_FIXED_HEADER ("datatables/FixedHeader-2.1.1/dataTables.fixedHeader.js"),
  EXTRAS_SCROLLER ("datatables/Scroller-1.2.1/dataTables.scroller.js");

  private final String m_sPath;

  private EDataTablesJSPathProvider (@Nonnull @Nonempty final String sPath)
  {
    if (!JSFilenameHelper.isJSFilename (sPath))
      throw new IllegalArgumentException ("path");
    m_sPath = sPath;
  }

  @Nonnull
  @Nonempty
  public String getJSItemPath (final boolean bRegular)
  {
    return bRegular ? m_sPath : JSFilenameHelper.getMinifiedJSPath (m_sPath);
  }

  public boolean canBeBundled ()
  {
    return true;
  }
}
