package com.phloc.webctrls.datatables;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.js.builder.JSArray;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.webctrls.datatable.EDataTableJSONKeyword;

public class DataTablesColumn
{
  public static final boolean DEFAULT_SEARCHABLE = true;
  public static final boolean DEFAULT_SORTABLE = true;
  public static final boolean DEFAULT_VISIBLE = true;

  private final JSArray m_aTargets;
  private boolean m_bSearchable = DEFAULT_SEARCHABLE;
  private boolean m_bSortable = DEFAULT_SORTABLE;
  private boolean m_bVisible = DEFAULT_VISIBLE;
  private ICSSClassProvider m_aCSSClassProvider;
  private String m_sName;
  private IDataTablesColumnType m_aType;
  private String m_sWidth;
  private int [] m_aDataSort;

  public DataTablesColumn (final int nTarget)
  {
    m_aTargets = new JSArray ();
    m_aTargets.add (nTarget);
  }

  public DataTablesColumn (@Nonnull @Nonempty final int... aTargets)
  {
    if (ArrayHelper.isEmpty (aTargets))
      throw new IllegalArgumentException ("targets may not be empty");
    m_aTargets = new JSArray ();
    for (final int nTarget : aTargets)
      m_aTargets.add (nTarget);
  }

  @Nonnull
  public DataTablesColumn setSearchable (final boolean bSearchable)
  {
    m_bSearchable = bSearchable;
    return this;
  }

  @Nonnull
  public DataTablesColumn setSortable (final boolean bSortable)
  {
    m_bSortable = bSortable;
    return this;
  }

  @Nonnull
  public DataTablesColumn setVisible (final boolean bVisible)
  {
    m_bVisible = bVisible;
    return this;
  }

  @Nonnull
  public DataTablesColumn setClass (@Nullable final ICSSClassProvider aCSSClassProvider)
  {
    m_aCSSClassProvider = aCSSClassProvider;
    return this;
  }

  @Nonnull
  public DataTablesColumn setName (@Nullable final String sName)
  {
    m_sName = sName;
    return this;
  }

  @Nonnull
  public DataTablesColumn setType (@Nullable final IDataTablesColumnType aType)
  {
    m_aType = aType;
    return this;
  }

  @Nonnull
  public DataTablesColumn setWidth (@Nullable final String sWidth)
  {
    m_sWidth = sWidth;
    return this;
  }

  /**
   * Set the column indices to sort, when this column is sorted
   * 
   * @param aDataSort
   *        The sorting column (incl. this column!)
   * @return this
   */
  @Nonnull
  public DataTablesColumn setDataSort (@Nullable final int... aDataSort)
  {
    m_aDataSort = aDataSort;
    return this;
  }

  @Nonnull
  public JSAssocArray getAsJS ()
  {
    final JSAssocArray ret = new JSAssocArray ();
    ret.add (EDataTableJSONKeyword.TARGETS.getName (), m_aTargets);
    if (m_bSearchable != DEFAULT_SEARCHABLE)
      ret.add (EDataTableJSONKeyword.SEARCHABLE.getName (), m_bSearchable);
    if (m_bSortable != DEFAULT_SORTABLE)
      ret.add (EDataTableJSONKeyword.SORTABLE.getName (), m_bSortable);
    if (m_bVisible != DEFAULT_VISIBLE)
      ret.add (EDataTableJSONKeyword.VISIBLE.getName (), m_bVisible);
    if (m_aCSSClassProvider != null)
      ret.add (EDataTableJSONKeyword.CLASS.getName (), m_aCSSClassProvider.getCSSClass ());
    if (StringHelper.hasText (m_sName))
      ret.add (EDataTableJSONKeyword.S_NAME.getName (), m_sName);
    if (m_aType != null)
      ret.add (EDataTableJSONKeyword.S_TYPE.getName (), m_aType.getName ());
    if (StringHelper.hasText (m_sWidth))
      ret.add (EDataTableJSONKeyword.WIDTH.getName (), m_sWidth);
    if (ArrayHelper.isNotEmpty (m_aDataSort))
    {
      final JSArray aArray = new JSArray ();
      for (final int nDataSort : m_aDataSort)
        aArray.add (nDataSort);
      ret.add (EDataTableJSONKeyword.DATASORT.getName (), aArray);
    }
    return ret;
  }
}
