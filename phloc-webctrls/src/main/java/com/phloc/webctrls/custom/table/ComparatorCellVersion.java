package com.phloc.webctrls.custom.table;

import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.compare.AbstractComparator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.version.Version;
import com.phloc.html.hc.html.AbstractHCCell;

/**
 * This comparator is responsible for sorting cells by {@link Version}
 * 
 * @author philip
 */
public class ComparatorCellVersion extends AbstractComparator <AbstractHCCell>
{
  private final String m_sCommonPrefix;
  private final String m_sCommonSuffix;

  public ComparatorCellVersion ()
  {
    this (null, null);
  }

  public ComparatorCellVersion (@Nullable final String sCommonPrefix, @Nullable final String sCommonSuffix)
  {
    m_sCommonPrefix = sCommonPrefix;
    m_sCommonSuffix = sCommonSuffix;
  }

  @OverrideOnDemand
  protected String getCellText (@Nullable final AbstractHCCell aCell)
  {
    if (aCell == null)
      return "";

    String sText = aCell.getPlainText ();

    // strip common prefix and suffix
    if (StringHelper.hasText (m_sCommonPrefix))
      sText = StringHelper.trimStart (sText, m_sCommonPrefix);
    if (StringHelper.hasText (m_sCommonSuffix))
      sText = StringHelper.trimEnd (sText, m_sCommonSuffix);

    return sText;
  }

  @Override
  protected final int mainCompare (final AbstractHCCell aCell1, final AbstractHCCell aCell2)
  {
    final String sText1 = getCellText (aCell1);
    final String sText2 = getCellText (aCell2);

    final Version aBD1 = new Version (sText1);
    final Version aBD2 = new Version (sText2);
    return aBD1.compareTo (aBD2);
  }
}
