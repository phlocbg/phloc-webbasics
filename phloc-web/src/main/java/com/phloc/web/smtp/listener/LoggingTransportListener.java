package com.phloc.web.smtp.listener;

import javax.annotation.Nonnull;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of {@link TransportListener} that logs stuff to a logger.
 * 
 * @author philip
 */
public class LoggingTransportListener implements TransportListener
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggingConnectionListener.class);

  public void messageDelivered (@Nonnull final TransportEvent aEvent)
  {
    s_aLogger.info ("Message delivered");
  }

  public void messageNotDelivered (@Nonnull final TransportEvent aEvent)
  {
    s_aLogger.info ("Message not delivered");
  }

  public void messagePartiallyDelivered (@Nonnull final TransportEvent aEvent)
  {
    s_aLogger.info ("Message partially delivered");
  }
}
