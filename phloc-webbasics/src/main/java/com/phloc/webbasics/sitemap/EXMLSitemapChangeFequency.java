package com.phloc.webbasics.sitemap;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;

public enum EXMLSitemapChangeFequency
{
  ALWAYS ("always"),
  HOURLY ("hourly"),
  DAILY ("daily"),
  WEEKLY ("weekly"),
  MONTHLY ("monthly"),
  YEARLY ("yearly"),
  NEVER ("never");

  private final String m_sText;

  private EXMLSitemapChangeFequency (@Nonnull @Nonempty final String sText)
  {
    m_sText = sText;
  }

  @Nonnull
  @Nonempty
  public String getText ()
  {
    return m_sText;
  }
}
