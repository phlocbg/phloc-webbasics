package com.phloc.web.smtp.listener;

import javax.annotation.Nonnull;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of {@link ConnectionListener} that logs stuff to a logger.
 * 
 * @author philip
 */
public class LoggingConnectionListener implements ConnectionListener
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggingConnectionListener.class);

  public void opened (@Nonnull final ConnectionEvent aEvent)
  {
    s_aLogger.info ("Connected to SMTP server");
  }

  public void disconnected (@Nonnull final ConnectionEvent aEvent)
  {
    s_aLogger.info ("Disconnected to SMTP server");
  }

  public void closed (@Nonnull final ConnectionEvent aEvent)
  {
    s_aLogger.info ("Closed connection to SMTP server");
  }
}
