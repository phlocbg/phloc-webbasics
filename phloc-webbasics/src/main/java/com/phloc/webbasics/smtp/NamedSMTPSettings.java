package com.phloc.webbasics.smtp;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;
import com.phloc.web.smtp.impl.SMTPSettings;

public class NamedSMTPSettings
{
  private final String m_sName;
  private final SMTPSettings m_aSettings;

  public NamedSMTPSettings (@Nonnull @Nonempty final String sName, @Nonnull final SMTPSettings aSettings)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");
    if (aSettings == null)
      throw new NullPointerException ("settings");
    m_sName = sName;
    m_aSettings = aSettings;
  }

  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  @Nonnull
  public SMTPSettings getSettins ()
  {
    return m_aSettings;
  }
}
