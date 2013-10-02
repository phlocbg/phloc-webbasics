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
package com.phloc.bootstrap2.ext;

import javax.annotation.Nonnull;

import com.phloc.bootstrap2.BootstrapTable;
import com.phloc.bootstrap2.EBootstrapCSSPathProvider;
import com.phloc.bootstrap2.EBootstrapJSPathProvider;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSPackage;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.html.js.builder.jquery.JQueryProperty;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webctrls.datatables.EDataTablesCSSPathProvider;
import com.phloc.webctrls.datatables.EDataTablesPaginationType;

public class BootstrapDataTables extends DataTables
{
  public static final String DEFAULT_DOM = "<<'span6'l><'span6'f>r>t<<'span6'i><'span6'p>>";

  public BootstrapDataTables (@Nonnull final BootstrapTable aTable)
  {
    super (aTable);
    aTable.setStriped (true).setHover (true);
    setDom (DEFAULT_DOM);
    setPaginationType (EDataTablesPaginationType.BOOTSTRAP);
    registerExternalResources ();
  }

  @Override
  protected void addCodeBeforeDataTables (final JSPackage aPackage)
  {
    if (false)
      aPackage.add (JQuery.extend ()
                          .arg (JQueryProperty.fn ().ref ("dataTableExt").ref ("oStdClasses"))
                          .arg (new JSAssocArray ().add ("sSortAsc", "icon icon-arrow-down")
                                                   .add ("sSortDesc", "icon icon-arrow-up")
                                                   .add ("sSortable", "header")));
  }

  public static void registerExternalResources ()
  {
    if (false)
      PerRequestCSSIncludes.unregisterCSSIncludeFromThisRequest (EDataTablesCSSPathProvider.DATATABLES_194);
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EBootstrapJSPathProvider.BOOTSTRAP_DATATABLES);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EBootstrapCSSPathProvider.BOOTSTRAP_DATATABLES);
  }
}