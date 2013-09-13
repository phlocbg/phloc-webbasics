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
package com.phloc.bootstrap3.ext;

import javax.annotation.Nonnull;

import com.phloc.bootstrap3.EBootstrap3CSSPathProvider;
import com.phloc.bootstrap3.EBootstrap3JSPathProvider;
import com.phloc.bootstrap3.table.Bootstrap3Table;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webctrls.datatables.EDataTablesPaginationType;

public class Bootstrap3DataTables extends DataTables
{
  public static final String DEFAULT_DOM = "<<'col-md-6'l><'col-md-6'f>r>t<<'col-md-6'i><'col-md-6'p>>";

  public Bootstrap3DataTables (@Nonnull final Bootstrap3Table aTable)
  {
    super (aTable);
    aTable.setStriped (true).setHover (true);
    setDom (DEFAULT_DOM);
    setPaginationType (EDataTablesPaginationType.BOOTSTRAP);
    registerExternalResources ();
  }

  public static void registerExternalResources ()
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EBootstrap3JSPathProvider.BOOTSTRAP3_DATATABLES);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EBootstrap3CSSPathProvider.BOOTSTRAP3_DATATABLES);
  }
}
