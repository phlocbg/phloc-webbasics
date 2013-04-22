package com.phloc.web.smtp.listener;

import javax.annotation.Nonnull;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;

/**
 * An implementation of {@link ConnectionListener} that does nothing.
 * 
 * @author Philip Helger
 */
public class DoNothingConnectionListener implements ConnectionListener
{
  public void opened (@Nonnull final ConnectionEvent aEvent)
  {}

  public void disconnected (@Nonnull final ConnectionEvent aEvent)
  {}

  public void closed (@Nonnull final ConnectionEvent aEvent)
  {}
}
