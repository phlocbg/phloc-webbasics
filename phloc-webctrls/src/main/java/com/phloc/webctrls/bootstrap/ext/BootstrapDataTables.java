package com.phloc.webctrls.bootstrap.ext;

import com.phloc.html.hc.html.AbstractHCBaseTable;
import com.phloc.webctrls.datatables.DataTables;

public class BootstrapDataTables extends DataTables
{
  public BootstrapDataTables (final AbstractHCBaseTable <?> aTable)
  {
    super (aTable);
    setDom ("<<'span6'l><'span6'f>r>t<<'span6'i><'span6'p>>");
  }
}
