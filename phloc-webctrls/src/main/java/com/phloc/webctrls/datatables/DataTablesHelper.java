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
package com.phloc.webctrls.datatables;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;

/**
 * Some sanity functionality for {@link DataTables} objects.
 * 
 * @author Philip Helger
 */
@Immutable
public final class DataTablesHelper
{
  private DataTablesHelper ()
  {}

  public static void makeDataTablesScrollable (@Nonnull final DataTables aDataTables,
                                               @Nonnull final String sScrollHeight)
  {
    aDataTables.setScrollCollapse (true).setScrollY (sScrollHeight);
    // Activate the Scroller extra
    aDataTables.setDom (aDataTables.getDom () + "S");
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EDataTablesCSSPathProvider.EXTRAS_SCROLLER_110);
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EDataTablesJSPathProvider.EXTRAS_SCROLLER_110);
  }
}
