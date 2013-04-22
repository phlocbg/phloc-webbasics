package com.phloc.web.smtp.listener;

import javax.annotation.Nonnull;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;

/**
 * An implementation of {@link TransportListener} that does nothing.
 * 
 * @author Philip Helger
 */
public class DoNothingTransportListener implements TransportListener
{
  @Override
  public void messageDelivered (@Nonnull final TransportEvent aEvent)
  {}

  @Override
  public void messageNotDelivered (@Nonnull final TransportEvent aEvent)
  {}

  @Override
  public void messagePartiallyDelivered (@Nonnull final TransportEvent aEvent)
  {}
}
