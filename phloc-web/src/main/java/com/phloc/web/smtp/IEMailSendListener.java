package com.phloc.web.smtp;

import jakarta.mail.MessagingException;

public interface IEMailSendListener
{
  void onSuccess (IEmailData aMessage);

  void onError (IEmailData aMessage, MessagingException aEx);
}
