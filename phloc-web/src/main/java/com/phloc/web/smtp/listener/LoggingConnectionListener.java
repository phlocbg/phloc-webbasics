package com.phloc.web.smtp.listener;

import javax.annotation.Nonnull;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingConnectionListener implements ConnectionListener
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggingConnectionListener.class);

  public void opened (@Nonnull final ConnectionEvent e)
  {
    s_aLogger.info ("Connected to SMTP server");
  }

  public void disconnected (@Nonnull final ConnectionEvent e)
  {
    s_aLogger.info ("Disconnected to SMTP server");
  }

  public void closed (@Nonnull final ConnectionEvent e)
  {
    s_aLogger.info ("Closed connection to SMTP server");
  }
}
