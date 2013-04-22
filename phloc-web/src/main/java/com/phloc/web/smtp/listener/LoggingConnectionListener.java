package com.phloc.web.smtp.listener;

import javax.annotation.Nonnull;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.log.LogUtils;

/**
 * An implementation of {@link ConnectionListener} that logs stuff to a logger.
 * 
 * @author Philip Helger
 */
public class LoggingConnectionListener implements ConnectionListener
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggingConnectionListener.class);

  private final EErrorLevel m_eErrorLevel;

  public LoggingConnectionListener ()
  {
    this (EErrorLevel.INFO);
  }

  public LoggingConnectionListener (@Nonnull final EErrorLevel eErrorLevel)
  {
    if (eErrorLevel == null)
      throw new NullPointerException ("ErrorLevel");
    m_eErrorLevel = eErrorLevel;
  }

  public void opened (@Nonnull final ConnectionEvent aEvent)
  {
    LogUtils.log (s_aLogger, m_eErrorLevel, "Connected to SMTP server");
  }

  public void disconnected (@Nonnull final ConnectionEvent aEvent)
  {
    LogUtils.log (s_aLogger, m_eErrorLevel, "Disconnected to SMTP server");
  }

  public void closed (@Nonnull final ConnectionEvent aEvent)
  {
    LogUtils.log (s_aLogger, m_eErrorLevel, "Closed connection to SMTP server");
  }
}
