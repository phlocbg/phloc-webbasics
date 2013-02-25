package com.phloc.web.smtp;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.joda.time.DateTime;

import com.phloc.commons.email.IEmailAddress;

/**
 * Contains all possible fields for mail sending in a read-only fashion.
 * 
 * @author philip
 */
public interface IReadonlyEmailData
{
  /**
   * @return The type of the email - text or html.
   */
  @Nonnull
  EEmailType getEmailType ();

  /**
   * Get the sender mail address.
   * 
   * @return <code>null</code> if no sender is specified.
   */
  @Nullable
  IEmailAddress getFrom ();

  /**
   * Get the reply-to mail address.
   * 
   * @return never <code>null</code>
   */
  @Nonnull
  List <? extends IEmailAddress> getReplyTo ();

  @Nonnull
  InternetAddress [] getReplyToArray (@Nullable String sCharset) throws UnsupportedEncodingException, AddressException;

  @Nonnegative
  int getReplyToCount ();

  /**
   * Get a list of all TO-receivers.
   * 
   * @return Never <code>null</code>.
   */
  @Nonnull
  List <? extends IEmailAddress> getTo ();

  @Nonnull
  InternetAddress [] getToArray (@Nullable String sCharset) throws UnsupportedEncodingException, AddressException;

  @Nonnegative
  int getToCount ();

  /**
   * Get a list of all CC-receivers.
   * 
   * @return Never <code>null</code>.
   */
  @Nonnull
  List <? extends IEmailAddress> getCc ();

  @Nonnull
  InternetAddress [] getCcArray (@Nullable String sCharset) throws UnsupportedEncodingException, AddressException;

  @Nonnegative
  int getCcCount ();

  /**
   * Get a list of all BCC-receivers.
   * 
   * @return Never <code>null</code>.
   */
  @Nonnull
  List <? extends IEmailAddress> getBcc ();

  @Nonnull
  InternetAddress [] getBccArray (@Nullable String sCharset) throws UnsupportedEncodingException, AddressException;

  @Nonnegative
  int getBccCount ();

  /**
   * Get the date when the mail claims to be sent.
   * 
   * @return <code>null</code> if no such date is specified.
   */
  @Nullable
  DateTime getSentDate ();

  /**
   * Get the subject line.
   * 
   * @return <code>null</code> if no subject is specified.
   */
  @Nullable
  String getSubject ();

  /**
   * Get the mail content.
   * 
   * @return <code>null</code> if no content is specified.
   */
  @Nullable
  String getBody ();

  /**
   * Get an optional list of attachments. If attachments are present, the mail
   * is always created as a MIME message and never a simple text message.
   * 
   * @return A map from the filename to an {@link java.io.InputStream}-provider
   *         containing all desired attachments or <code>null</code>/an empty
   *         container if no attachments are required.
   */
  @Nullable
  IReadonlyEmailAttachments getAttachments ();
}
