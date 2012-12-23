package com.phloc.webctrls.custom.table;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.hc.html.AbstractHCCell;
import com.phloc.html.hc.html.HCCol;
import com.phloc.types.EBaseType;

@Immutable
public class ColumnDefinition implements Serializable
{
  private final String m_sName;
  private final HCCol m_aCol;
  private final String m_sFieldID;
  private final Comparator <AbstractHCCell> m_aComparator;
  private String m_sToolTip = null;

  @Nullable
  protected static Comparator <AbstractHCCell> _getComparator (@Nullable final EBaseType eType, final Locale aLocale)
  {
    if (eType != null)
      switch (eType)
      {
        case DATE:
          return new ComparatorCellDate (aLocale);
        case DATETIME:
          return new ComparatorCellDateTime (aLocale);
        case DOUBLE:
          return new ComparatorCellDouble (aLocale);
        case INT:
          return new ComparatorCellInteger (aLocale);
        case TEXT:
          return new ComparatorCellString (aLocale);
        case TIME:
          return new ComparatorCellTime (aLocale);
      }
    return null;
  }

  public ColumnDefinition (@Nonnull final String sName,
                           @Nonnull final HCCol aCol,
                           @Nullable final String sFieldID,
                           @Nullable final EBaseType eType,
                           @Nonnull final Locale aDisplayLocale)
  {
    this (sName, aCol, sFieldID, _getComparator (eType, aDisplayLocale));
  }

  public ColumnDefinition (@Nonnull final String sName, @Nonnull final HCCol aCol)
  {
    this (sName, aCol, null, null);
  }

  public ColumnDefinition (@Nonnull final String sName, @Nonnull final HCCol aCol, @Nullable final String sFieldID)
  {
    this (sName, aCol, sFieldID, null);
  }

  public ColumnDefinition (@Nullable final String sName,
                           @Nonnull final HCCol aCol,
                           @Nullable final String sFieldID,
                           @Nullable final Comparator <AbstractHCCell> aComparator)
  {
    if (StringHelper.hasNoText (sFieldID) && aComparator != null)
      throw new IllegalArgumentException ("field ID cannot be empty for sortable columns");
    if (aCol == null)
      throw new NullPointerException ("col");
    m_sName = sName;
    m_aCol = aCol;
    m_sFieldID = sFieldID;
    m_aComparator = aComparator;
  }

  public boolean isFieldMapped ()
  {
    return m_sFieldID != null;
  }

  @Nonnull
  public String getName ()
  {
    return m_sName;
  }

  public void setToolTip (final String sToolTip)
  {
    m_sToolTip = sToolTip;
  }

  @Nullable
  public String getToolTip ()
  {
    return m_sToolTip;
  }

  @Nonnull
  public HCCol getCol ()
  {
    return m_aCol;
  }

  @Nullable
  public String getFieldID ()
  {
    return m_sFieldID;
  }

  @Nullable
  public Comparator <AbstractHCCell> getComparator ()
  {
    return m_aComparator;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("name", m_sName)
                                       .append ("column", m_aCol)
                                       .append ("fieldID", m_sFieldID)
                                       .append ("comparator", m_aComparator)
                                       .toString ();
  }
}
