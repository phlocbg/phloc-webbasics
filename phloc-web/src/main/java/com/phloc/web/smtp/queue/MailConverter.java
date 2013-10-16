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

import java.nio.charset.Charset;

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
import com.phloc.commons.mime.MimeType;
import com.phloc.web.smtp.IEmailData;
import com.phloc.web.smtp.InternetAddressUtils;
import com.phloc.web.smtp.attachment.IEmailAttachmentList;

/**
 * Fill a {@link MimeMessage} object with the data of an {@link IEmailData}
 * object.
 * 
 * @author Philip Helger
 */
@Immutable
public final class MailConverter
{
  private MailConverter ()
  {}

  private static void _setSubject (@Nonnull final MimeMessage aMIMEMessage,
                                   @Nonnull final String sSubject,
                                   @Nonnull final Charset aCharset)
  {
    try
    {
      aMIMEMessage.setSubject (sSubject, aCharset.name ());
    }
    catch (final MessagingException ex)
    {
      throw new IllegalStateException ("Charset " + aCharset + " is unknown!", ex);
    }
  }

  private static void _setText (@Nonnull final MimeBodyPart aMIMEBody,
                                @Nonnull final String sText,
                                @Nonnull final Charset aCharset)
  {
    try
    {
      aMIMEBody.setText (sText, aCharset.name ());
    }
    catch (final MessagingException ex)
    {
      throw new IllegalStateException ("Charset " + aCharset + " is unknown!", ex);
    }
  }

  private static void _fillMimeMessage (@Nonnull final MimeMessage aMIMEMessage,
                                        @Nonnull final IEmailData aMailData,
                                        @Nullable final Charset aCharset) throws MessagingException
  {
    if (aMailData.getFrom () != null)
      aMIMEMessage.setFrom (InternetAddressUtils.getAsInternetAddress (aMailData.getFrom (), aCharset));
    if (aMailData.getReplyTo () != null)
      aMIMEMessage.setReplyTo (aMailData.getReplyToArray (aCharset));
    aMIMEMessage.setRecipients (Message.RecipientType.TO, aMailData.getToArray (aCharset));
    aMIMEMessage.setRecipients (Message.RecipientType.CC, aMailData.getCcArray (aCharset));
    aMIMEMessage.setRecipients (Message.RecipientType.BCC, aMailData.getBccArray (aCharset));
    if (aMailData.getSentDate () != null)
      aMIMEMessage.setSentDate (aMailData.getSentDate ().toDate ());
    if (aMailData.getSubject () != null)
      if (aCharset != null)
        _setSubject (aMIMEMessage, aMailData.getSubject (), aCharset);
      else
        aMIMEMessage.setSubject (aMailData.getSubject ());

    final MimeMultipart aMixedMultipart = new MimeMultipart ();

    // build text part
    {
      final MimeBodyPart aBodyPart = new MimeBodyPart ();
      if (aMailData.getEmailType ().isHTML ())
      {
        if (aCharset != null)
        {
          aBodyPart.setContent (aMailData.getBody (),
                                new MimeType (CMimeType.TEXT_HTML).addParameter (CMimeType.PARAMETER_NAME_CHARSET,
                                                                                 aCharset.name ()).getAsString ());
        }
        else
          aBodyPart.setContent (aMailData.getBody (), CMimeType.TEXT_HTML.getAsString ());
      }
      else
      {
        if (aCharset != null)
          _setText (aBodyPart, aMailData.getBody (), aCharset);
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
   * @param aCharset
   *        The character set to be used for sending.
   * @throws LoggedRuntimeException
   *         in case of an error
   */
  public static void fillMimeMesage (@Nonnull final MimeMessage aMimeMessage,
                                     @Nonnull final IEmailData aMailData,
                                     @Nullable final Charset aCharset)
  {
    try
    {
      _fillMimeMessage (aMimeMessage, aMailData, aCharset);
    }
    catch (final Exception ex)
    {
      throw LoggedRuntimeException.newException (ex);
    }
  }
}
