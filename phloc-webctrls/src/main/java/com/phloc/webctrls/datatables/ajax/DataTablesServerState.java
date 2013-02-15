package com.phloc.webctrls.datatables.ajax;

import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.webctrls.datatables.DataTables;

/**
 * The current search and sort state of a {@link DataTables} object.
 * 
 * @author philip
 */
final class DataTablesServerState
{
  private final String m_sSearchText;
  private final boolean m_bSearchRegEx;
  private final RequestDataSortColumn [] m_aSortCols;

  public DataTablesServerState ()
  {
    this (null, false, new RequestDataSortColumn [0]);
  }

  public DataTablesServerState (@Nullable final String sSearchText,
                                final boolean bSearchRegEx,
                                @Nonnull final RequestDataSortColumn [] aSortCols)
  {
    if (aSortCols == null)
      throw new NullPointerException ("sortCols");
    m_sSearchText = StringHelper.hasNoText (sSearchText) ? null : sSearchText;
    m_bSearchRegEx = bSearchRegEx;
    m_aSortCols = aSortCols;
  }

  public boolean hasSearchText ()
  {
    return StringHelper.hasText (m_sSearchText);
  }

  @Nullable
  public String getSearchText ()
  {
    return m_sSearchText;
  }

  public boolean isSearchRegEx ()
  {
    return m_bSearchRegEx;
  }

  /**
   * @return Number of columns to sort on
   */
  public int getSortingCols ()
  {
    return m_aSortCols.length;
  }

  @Nonnull
  @ReturnsMutableCopy
  public RequestDataSortColumn [] getSortCols ()
  {
    return ArrayHelper.getCopy (m_aSortCols);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof DataTablesServerState))
      return false;
    final DataTablesServerState rhs = (DataTablesServerState) o;
    return EqualsUtils.equals (m_sSearchText, rhs.m_sSearchText) &&
           m_bSearchRegEx == rhs.m_bSearchRegEx &&
           Arrays.equals (m_aSortCols, rhs.m_aSortCols);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sSearchText)
                                       .append (m_bSearchRegEx)
                                       .append (m_aSortCols)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("searchText", m_sSearchText)
                                       .append ("searchRegEx", m_bSearchRegEx)
                                       .append ("sortCols", m_aSortCols)
                                       .toString ();
  }
}
