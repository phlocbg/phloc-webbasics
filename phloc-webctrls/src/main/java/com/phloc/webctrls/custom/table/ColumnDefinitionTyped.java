package com.phloc.webctrls.custom.table;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.hc.html.HCCol;

@Immutable
public final class ColumnDefinitionTyped extends ColumnDefinition
{
  private final Class <?> m_aType;

  public ColumnDefinitionTyped (@Nonnull final String sName,
                                @Nonnull final HCCol aCol,
                                @Nonnull final String sFieldID,
                                @Nonnull final Class <?> aType)
  {
    super (sName, aCol, sFieldID);
    if (StringHelper.hasNoText (sFieldID))
      throw new IllegalArgumentException ("field ID cannot be empty for typed columns");
    if (aType == null)
      throw new NullPointerException ("aType");
    m_aType = aType;
  }

  @Nonnull
  public Class <?> getType ()
  {
    return m_aType;
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("type", m_aType).toString ();
  }
}
