package com.phloc.web.smtp.listener;

import javax.annotation.Nonnull;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.log.LogUtils;

/**
 * An implementation of {@link TransportListener} that logs stuff to a logger.
 * 
 * @author philip
 */
public class LoggingTransportListener implements TransportListener
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggingConnectionListener.class);

  private final EErrorLevel m_eErrorLevel;

  public LoggingTransportListener ()
  {
    this (EErrorLevel.INFO);
  }

  public LoggingTransportListener (@Nonnull final EErrorLevel eErrorLevel)
  {
    if (eErrorLevel == null)
      throw new NullPointerException ("ErrorLevel");
    m_eErrorLevel = eErrorLevel;
  }

  public void messageDelivered (@Nonnull final TransportEvent aEvent)
  {
    LogUtils.log (s_aLogger, m_eErrorLevel, "Message delivered");
  }

  public void messageNotDelivered (@Nonnull final TransportEvent aEvent)
  {
    LogUtils.log (s_aLogger, m_eErrorLevel, "Message not delivered");
  }

  public void messagePartiallyDelivered (@Nonnull final TransportEvent aEvent)
  {
    LogUtils.log (s_aLogger, m_eErrorLevel, "Message partially delivered");
  }
}
