/**
 * Copyright (C) 2006-2013 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.web.smtp.queue;

import java.io.UnsupportedEncodingException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.phloc.commons.exceptions.LoggedRuntimeException;
import com.phloc.commons.mime.CMimeType;
import com.phloc.web.smtp.IEmailData;
import com.phloc.web.smtp.InternetAddressUtils;
import com.phloc.web.smtp.attachment.IEmailAttachmentList;

/**
 * Fill a {@link MimeMessage} object with the data of an {@link IEmailData}
 * object.
 * 
 * @author philip
 */
@Immutable
public final class MailConverter
{
  private MailConverter ()
  {}

  private static void _fillMimeMessage (@Nonnull final MimeMessage aMIMEMessage,
                                        @Nonnull final IEmailData aMailData,
                                        @Nullable final String sCharset) throws MessagingException,
                                                                        UnsupportedEncodingException
  {
    if (aMailData.getFrom () != null)
      aMIMEMessage.setFrom (InternetAddressUtils.getAsInternetAddress (aMailData.getFrom (), sCharset));
    if (aMailData.getReplyTo () != null)
      aMIMEMessage.setReplyTo (aMailData.getReplyToArray (sCharset));
    aMIMEMessage.setRecipients (Message.RecipientType.TO, aMailData.getToArray (sCharset));
    aMIMEMessage.setRecipients (Message.RecipientType.CC, aMailData.getCcArray (sCharset));
    aMIMEMessage.setRecipients (Message.RecipientType.BCC, aMailData.getBccArray (sCharset));
    if (aMailData.getSentDate () != null)
      aMIMEMessage.setSentDate (aMailData.getSentDate ().toDate ());
    if (aMailData.getSubject () != null)
      if (sCharset != null)
        aMIMEMessage.setSubject (aMailData.getSubject (), sCharset);
      else
        aMIMEMessage.setSubject (aMailData.getSubject ());

    final MimeMultipart aMixedMultipart = new MimeMultipart ();

    // build text part
    {
      final MimeBodyPart aBodyPart = new MimeBodyPart ();
      if (aMailData.getEmailType ().isHTML ())
      {
        if (sCharset != null)
          aBodyPart.setContent (aMailData.getBody (), CMimeType.TEXT_HTML.getAsStringWithEncoding (sCharset));
        else
          aBodyPart.setContent (aMailData.getBody (), CMimeType.TEXT_HTML.getAsString ());
      }
      else
      {
        if (sCharset != null)
          aBodyPart.setText (aMailData.getBody (), sCharset);
        else
          aBodyPart.setText (aMailData.getBody ());
      }
      aMixedMultipart.addBodyPart (aBodyPart);
    }

    // Does the mail data contain attachments?
    final IEmailAttachmentList aAttachments = aMailData.getAttachments ();
    if (aAttachments != null)
      for (final DataSource aDS : aAttachments.getAsDataSourceList ())
      {
        final MimeBodyPart aAttachmentPart = new MimeBodyPart ();
        aAttachmentPart.setDisposition (Part.ATTACHMENT);
        aAttachmentPart.setFileName (aDS.getName ());
        aAttachmentPart.setDataHandler (new DataHandler (aDS));
        aMixedMultipart.addBodyPart (aAttachmentPart);
      }

    // set mixed multipart content
    aMIMEMessage.setContent (aMixedMultipart);
  }

  /**
   * Fill the {@link MimeMessage} object with the {@link IEmailData} elements.
   * 
   * @param aMimeMessage
   *        The javax.mail object to be filled.
   * @param aMailData
   *        The mail data object that contains all the source information to be
   *        send.
   * @param sCharset
   *        The character set to be used for sending.
   * @throws LoggedRuntimeException
   *         in case of an error
   */
  public static void fillMimeMesage (@Nonnull final MimeMessage aMimeMessage,
                                     @Nonnull final IEmailData aMailData,
                                     @Nullable final String sCharset)
  {
    try
    {
      _fillMimeMessage (aMimeMessage, aMailData, sCharset);
    }
    catch (final Exception ex)
    {
      throw LoggedRuntimeException.newException (ex);
    }
  }
}
