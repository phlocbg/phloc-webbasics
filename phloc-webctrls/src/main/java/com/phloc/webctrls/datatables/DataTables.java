package com.phloc.webctrls.datatables;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.AbstractHCBaseTable;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSPackage;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;
import com.phloc.webctrls.datatable.EDataTableJSONKeyword;

public class DataTables implements IHCNodeBuilder
{
  public static final boolean DEFAULT_PAGINATE = true;

  private final String m_sParentElementID;
  private boolean m_bPaginate = DEFAULT_PAGINATE;
  private DataTablesSorting m_aSorting;

  @Nonnull
  private static String _ensureID (@Nonnull final AbstractHCBaseTable <?> aTable)
  {
    String sID = aTable.getID ();
    if (StringHelper.hasNoText (sID))
    {
      sID = GlobalIDFactory.getNewStringID ();
      aTable.setID (sID);
    }
    return sID;
  }

  /**
   * Apply to an existing table. If the table does not have an ID yet, a new one
   * is created.
   * 
   * @param aTable
   *        The table to apply the data table to
   */
  public DataTables (@Nonnull final AbstractHCBaseTable <?> aTable)
  {
    this (_ensureID (aTable));
  }

  public DataTables (@Nonnull final String sParentElementID)
  {
    if (StringHelper.hasNoText (sParentElementID))
      throw new IllegalArgumentException ("ParentElementID");
    m_sParentElementID = sParentElementID;

    PerRequestJSIncludes.registerJSIncludeForThisRequest (EDataTablesJSPathProvider.DATATABLES);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EDataTablesCSSPathProvider.DATATABLES);
  }

  @Nonnull
  public DataTables setPaginate (final boolean bPaginate)
  {
    m_bPaginate = bPaginate;
    return this;
  }

  @Nonnull
  public DataTables setSorting (@Nullable final DataTablesSorting aSorting)
  {
    m_aSorting = aSorting;
    return this;
  }

  @Nullable
  public IHCNode build ()
  {
    // init parameters
    final JSAssocArray aParams = new JSAssocArray ();
    aParams.add (EDataTableJSONKeyword.PAGINATE.getName (), m_bPaginate);
    if (m_aSorting != null)
      aParams.add (EDataTableJSONKeyword.SORTING.getName (), m_aSorting.getAsJSON ());

    // main on document ready code
    final JSPackage aJSCode = new JSPackage ();
    aJSCode.var ("oTable", JQuery.idRef (m_sParentElementID).invoke ("dataTable").arg (aParams));
    return new HCScriptOnDocumentReady (aJSCode);
  }
}
