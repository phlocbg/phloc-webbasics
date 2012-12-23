package com.phloc.webctrls.datatables;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.js.builder.JSArray;

public class DataTablesSorting
{
  public static final class SortColumn
  {
    private final int m_nIndex;
    private final ESortOrder m_eSortOrder;

    public SortColumn (@Nonnegative final int nIndex, @Nonnull final ESortOrder eSortOrder)
    {
      if (nIndex < 0)
        throw new IllegalArgumentException ("index");
      if (eSortOrder == null)
        throw new NullPointerException ("sortOrder");
      m_nIndex = nIndex;
      m_eSortOrder = eSortOrder;
    }

    @Nonnegative
    public int getIndex ()
    {
      return m_nIndex;
    }

    @Nonnull
    public ESortOrder getSortOrder ()
    {
      return m_eSortOrder;
    }

    @Nonnull
    public JSArray getAsJSON ()
    {
      final JSArray ret = new JSArray ();
      ret.add (m_nIndex);
      ret.add (m_eSortOrder.isAscending () ? "asc" : "desc");
      return ret;
    }

    @Override
    public String toString ()
    {
      return new ToStringGenerator (this).append ("index", m_nIndex).append ("sortOrder", m_eSortOrder).toString ();
    }
  }

  private final List <SortColumn> m_aSortColumns = new ArrayList <SortColumn> ();

  public DataTablesSorting ()
  {}

  @Nonnull
  public DataTablesSorting addColumn (@Nonnegative final int nIndex, @Nonnull final ESortOrder eSortOrder)
  {
    m_aSortColumns.add (new SortColumn (nIndex, eSortOrder));
    return this;
  }

  @Nonnull
  public JSArray getAsJSON ()
  {
    final JSArray ret = new JSArray ();
    for (final SortColumn aSortColumn : m_aSortColumns)
      ret.add (aSortColumn.getAsJSON ());
    return ret;
  }
}
