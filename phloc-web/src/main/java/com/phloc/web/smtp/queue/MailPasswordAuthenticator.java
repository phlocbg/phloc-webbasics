package com.phloc.web.smtp.queue;

import javax.annotation.Nonnull;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import com.phloc.web.smtp.settings.ISMTPSettings;

final class MailPasswordAuthenticator extends Authenticator
{
  private final ISMTPSettings m_aSettings;

  public MailPasswordAuthenticator (@Nonnull final ISMTPSettings aSettings)
  {
    if (aSettings == null)
      throw new NullPointerException ("settings");
    m_aSettings = aSettings;
  }

  @Override
  protected PasswordAuthentication getPasswordAuthentication ()
  {
    return new PasswordAuthentication (m_aSettings.getUserName (), m_aSettings.getPassword ());
  }
}
