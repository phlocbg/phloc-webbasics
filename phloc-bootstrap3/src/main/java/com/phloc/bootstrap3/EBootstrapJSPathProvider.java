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
package com.phloc.bootstrap3;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.resource.js.IJSPathProvider;
import com.phloc.html.resource.js.JSFilenameHelper;

public enum EBootstrapJSPathProvider implements IJSPathProvider
{
  BOOTSTRAP_3_1_1 ("bootstrap/3.1.1/js/bootstrap.js"),
  BOOTSTRAP_3_2_0 ("bootstrap/3.2.0/js/bootstrap.js"),
  BOOTSTRAP_DATATABLES ("bootstrap/datatables/bootstrap3-datatables.js"),
  BOOTSTRAP_PHLOC ("bootstrap/bootstrap3-phloc.js");

  private final String m_sPath;

  private EBootstrapJSPathProvider (@Nonnull @Nonempty final String sPath)
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
