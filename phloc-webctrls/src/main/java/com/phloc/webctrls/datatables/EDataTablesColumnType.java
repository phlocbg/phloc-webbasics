package com.phloc.webctrls.datatables;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;

public enum EDataTablesColumnType implements IDataTablesColumnType
{
  STRING ("string"),
  NUMERIC ("numeric"),
  DATE ("date"),
  HTML ("html");

  private final String m_sName;

  private EDataTablesColumnType (@Nonnull @Nonempty final String sName)
  {
    m_sName = sName;
  }

  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }
}
