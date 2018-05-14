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
import com.phloc.css.CSSFilenameHelper;
import com.phloc.html.resource.css.ICSSPathProvider;

/**
 * Contains default CSS paths within this library.
 *
 * @author Philip Helger
 */
public enum EBootstrapCSSPathProvider implements ICSSPathProvider
{
 BOOTSTRAP_3_1_1 ("bootstrap/3.1.1/css/bootstrap.css"),
 BOOTSTRAP_THEME_3_1_1 ("bootstrap/3.1.1/css/bootstrap-theme.css"),
 BOOTSTRAP_3_2_0 ("bootstrap/3.2.0/css/bootstrap.css"),
 BOOTSTRAP_THEME_3_2_0 ("bootstrap/3.2.0/css/bootstrap-theme.css"),
 BOOTSTRAP_DATATABLES ("bootstrap/datatables/bootstrap3-datatables.css"),
 BOOTSTRAP_IE9 ("bootstrap/bootstrap3-ie9.css"),
 BOOTSTRAP_PHLOC ("bootstrap/bootstrap3-phloc.css");

  private final String m_sPath;

  private EBootstrapCSSPathProvider (@Nonnull @Nonempty final String sPath)
  {
    if (!CSSFilenameHelper.isCSSFilename (sPath))
      throw new IllegalArgumentException ("path");
    this.m_sPath = sPath;
  }

  @Override
  @Nonnull
  @Nonempty
  public String getCSSItemPath (final boolean bRegular)
  {
    return bRegular ? this.m_sPath : CSSFilenameHelper.getMinifiedCSSFilename (this.m_sPath);
  }

  @Override
  public boolean canBeBundled ()
  {
    return true;
  }
}
