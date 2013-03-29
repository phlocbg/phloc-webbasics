package com.phloc.web.smtp.listener;

import javax.annotation.Nonnull;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;

public class DoNothingConnectionListener implements ConnectionListener
{
  public void opened (@Nonnull final ConnectionEvent e)
  {}

  public void disconnected (@Nonnull final ConnectionEvent e)
  {}

  public void closed (@Nonnull final ConnectionEvent e)
  {}
}
