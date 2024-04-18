package com.phloc.web.smtp.transport;

import jakarta.mail.MessagingException;

import com.phloc.web.smtp.IEMailSendListener;
import com.phloc.web.smtp.IEmailData;

public class DoNothingEMailSendListener implements IEMailSendListener
{
  @Override
  public void onSuccess (final IEmailData aMessage)
  {
    // do nothing
  }

  @Override
  public void onError (final IEmailData aMessage, final MessagingException aEx)
  {
    // do nothing
  }
}
