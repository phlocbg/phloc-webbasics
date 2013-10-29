package com.phloc.web.smtp;

import javax.annotation.Nonnull;
import javax.mail.event.TransportEvent;

/**
 * An interface similar to javax.mail.event.TransportListener but specific
 * relations to out internal object types {@link ISMTPSettings} and
 * {@link IEmailData}.
 * 
 * @author Philip Helger
 */
public interface IEmailDataTransportListener
{
  /**
   * Invoked when a Message is successfully delivered.
   * 
   * @param aSMTPSettings
   *        The SMTP settings used for this message. Never <code>null</code>.
   * @param aEmailData
   *        The data that was sent. Never <code>null</code>.
   * @param aEvent
   *        TransportEvent. Never <code>null</code>.
   */
  void messageDelivered (@Nonnull ISMTPSettings aSMTPSettings,
                         @Nonnull IEmailData aEmailData,
                         @Nonnull TransportEvent aEvent);

  /**
   * Invoked when a Message is not delivered.
   * 
   * @param aSMTPSettings
   *        The SMTP settings used for this message. Never <code>null</code>.
   * @param aEmailData
   *        The data that was not sent. Never <code>null</code>.
   * @param aEvent
   *        TransportEvent. Never <code>null</code>.
   * @see TransportEvent
   */
  void messageNotDelivered (@Nonnull ISMTPSettings aSMTPSettings,
                            @Nonnull IEmailData aEmailData,
                            @Nonnull TransportEvent aEvent);

  /**
   * Invoked when a Message is partially delivered.
   * 
   * @param aSMTPSettings
   *        The SMTP settings used for this message. Never <code>null</code>.
   * @param aEmailData
   *        The data that was partially sent. Never <code>null</code>.
   * @param aEvent
   *        TransportEvent. Never <code>null</code>.
   * @see TransportEvent
   */
  void messagePartiallyDelivered (@Nonnull ISMTPSettings aSMTPSettings,
                                  @Nonnull IEmailData aEmailData,
                                  @Nonnull TransportEvent aEvent);
}
