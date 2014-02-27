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
package com.phloc.bootstrap3.ext;

import javax.annotation.Nonnull;

import com.phloc.bootstrap3.EBootstrapCSSPathProvider;
import com.phloc.bootstrap3.EBootstrapJSPathProvider;
import com.phloc.bootstrap3.table.AbstractBootstrapTable;
import com.phloc.html.hc.IHCTable;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webctrls.datatables.EDataTablesPaginationType;

public class BootstrapDataTables extends DataTables
{
  public static final String DEFAULT_DOM = "<'row'<'col-xs-6'l><'col-xs-6'f>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>";

  @SuppressWarnings ("deprecation")
  public BootstrapDataTables (@Nonnull final IHCTable <?> aTable)
  {
    super (aTable);
    if (aTable instanceof AbstractBootstrapTable <?>)
    {
      ((AbstractBootstrapTable <?>) aTable).setStriped (true);
      ((AbstractBootstrapTable <?>) aTable).setHover (true);
    }
    setDom (DEFAULT_DOM);
    if (DataTables.USE_V19)
      setPaginationType (EDataTablesPaginationType.BOOTSTRAP);
  }

  @Override
  protected void onRegisterExternalResources ()
  {
    registerExternalResources ();
  }

  public static void registerExternalResources ()
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EBootstrapJSPathProvider.BOOTSTRAP3_DATATABLES);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EBootstrapCSSPathProvider.BOOTSTRAP3_DATATABLES);
  }
}
