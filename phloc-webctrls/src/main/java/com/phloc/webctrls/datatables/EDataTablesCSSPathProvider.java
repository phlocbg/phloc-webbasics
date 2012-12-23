package com.phloc.webctrls.datatables;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.css.CSSFilenameHelper;
import com.phloc.html.resource.css.ICSSPathProvider;

/**
 * Contains default CSS paths within this library.
 * 
 * @author philip
 */
public enum EDataTablesCSSPathProvider implements ICSSPathProvider
{
  DATATABLES ("datatables/194/css/jquery.dataTables.css"),
  DATATABLES_THEMEROLLER ("datatables/194/css/jquery.dataTables_themeroller.css"),
  DEMO_TABLE ("datatables/194/css/demo_table.css"),
  DEMO_TABLE_JUI ("datatables/194/css/demo_table_jui.css");

  private final String m_sPath;

  private EDataTablesCSSPathProvider (@Nonnull @Nonempty final String sPath)
  {
    if (!CSSFilenameHelper.isCSSFilename (sPath))
      throw new IllegalArgumentException ("path");
    m_sPath = sPath;
  }

  @Nonnull
  @Nonempty
  public String getCSSItemPath (final boolean bRegular)
  {
    return bRegular ? m_sPath : CSSFilenameHelper.getMinifiedCSSFilename (m_sPath);
  }
}
