package com.phloc.webctrls.bootstrap.ext;

import com.phloc.html.hc.html.AbstractHCBaseTable;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSPackage;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;
import com.phloc.webctrls.bootstrap.EBootstrapCSSPathProvider;
import com.phloc.webctrls.bootstrap.EBootstrapJSPathProvider;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webctrls.datatables.EDataTablesPaginationType;

public class BootstrapDataTables extends DataTables
{
  public BootstrapDataTables (final AbstractHCBaseTable <?> aTable)
  {
    super (aTable);
    setDom ("<<'span6'l><'span6'f>r>t<<'span6'i><'span6'p>>");
    setPaginationType (EDataTablesPaginationType.BOOTSTRAP);
    registerExternalResources ();
  }

  @Override
  protected void addCodeBeforeDataTables (final JSPackage aPackage)
  {
    aPackage.add (JQuery.extend ()
                        .arg (JQuery.fn ().ref ("dataTableExt").ref ("oStdClasses"))
                        .arg (new JSAssocArray ().add ("sSortAsc", "header headerSortDown")
                                                 .add ("sSortDesc", "header headerSortUp")
                                                 .add ("sSortable", "header")));
  }

  public static void registerExternalResources ()
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EBootstrapJSPathProvider.BOOTSTRAP_DATATABLES);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EBootstrapCSSPathProvider.BOOTSTRAP_DATATABLES);
  }
}
