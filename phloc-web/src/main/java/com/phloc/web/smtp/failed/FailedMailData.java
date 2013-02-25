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
package com.phloc.web.smtp.failed;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import com.phloc.commons.email.IEmailAddress;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.type.ITypedObject;
import com.phloc.commons.type.ObjectType;
import com.phloc.datetime.PDTFactory;
import com.phloc.datetime.format.PDTToString;
import com.phloc.web.smtp.IEmailData;
import com.phloc.web.smtp.settings.ISMTPSettings;

@Immutable
public final class FailedMailData implements ITypedObject <String>
{
  public static final ObjectType TYPE_FAILEDMAIL = new ObjectType ("failedmail");

  private final String m_sID;
  private final LocalDateTime m_aErrorDT;
  private final ISMTPSettings m_aSettings;
  private final DateTime m_aOriginalSentDateTime;
  private final IEmailData m_aEmailData;
  private final Throwable m_aError;

  /**
   * Constructor for message unspecific error.
   * 
   * @param aSettings
   *        The mail settings for which the error occurs. Never
   *        <code>null</code>.
   * @param aError
   *        The exception that occurred. Never <code>null</code>.
   */
  public FailedMailData (@Nonnull final ISMTPSettings aSettings, @Nonnull final Throwable aError)
  {
    this (aSettings, null, aError);
  }

  /**
   * Constructor for message unspecific error.
   * 
   * @param aSettings
   *        The mail settings for which the error occurs. Never
   *        <code>null</code>.
   * @param aEmailData
   *        The message that failed to send. May be <code>null</code> if it is a
   *        mail-independent error.
   * @param aError
   *        The exception that occurred. Never <code>null</code>.
   */
  public FailedMailData (@Nonnull final ISMTPSettings aSettings,
                         @Nullable final IEmailData aEmailData,
                         @Nonnull final Throwable aError)
  {
    this (GlobalIDFactory.getNewPersistentStringID (),
          PDTFactory.getCurrentLocalDateTime (),
          aSettings,
          null,
          aEmailData,
          aError);
  }

  /**
   * Constructor for serialization only!
   * 
   * @param sID
   *        The ID of this object. Never <code>null</code>.
   * @param aErrorDT
   *        The date and time when the error occurred. Never <code>null</code>.
   * @param aSettings
   *        The mail settings for which the error occurs. Never
   *        <code>null</code>.
   * @param aOriginalSentDT
   *        The date and time when the message was originally sent. Never
   *        <code>null</code>.
   * @param aEmailData
   *        The message that failed to send. May be <code>null</code> if it is a
   *        mail-independent error.
   * @param aError
   *        The exception that occurred. Never <code>null</code>.
   */
  public FailedMailData (@Nonnull final String sID,
                         @Nonnull final LocalDateTime aErrorDT,
                         @Nonnull final ISMTPSettings aSettings,
                         @Nullable final DateTime aOriginalSentDT,
                         @Nullable final IEmailData aEmailData,
                         @Nonnull final Throwable aError)
  {
    if (sID == null)
      throw new NullPointerException ("id");
    if (aErrorDT == null)
      throw new NullPointerException ("errorDateTime");
    if (aSettings == null)
      throw new NullPointerException ("settings");
    if (aError == null)
      throw new NullPointerException ("error");
    m_sID = sID;
    m_aErrorDT = aErrorDT;
    m_aSettings = aSettings;
    m_aOriginalSentDateTime = aOriginalSentDT != null ? aOriginalSentDT
                                                     : (aEmailData != null ? aEmailData.getSentDate () : null);
    m_aEmailData = aEmailData;
    m_aError = aError;
  }

  @Nonnull
  public ObjectType getTypeID ()
  {
    return TYPE_FAILEDMAIL;
  }

  @Nonnull
  public String getID ()
  {
    return m_sID;
  }

  /**
   * @return The date and time when the error occurred.
   */
  @Nonnull
  public LocalDateTime getErrorDateTime ()
  {
    return m_aErrorDT;
  }

  /**
   * @return The original SMTP settings used for sending the mail.
   */
  @Nonnull
  public ISMTPSettings getSMTPSettings ()
  {
    return m_aSettings;
  }

  /**
   * @return The date and time when the message was originally sent.
   */
  @Nonnull
  public DateTime getOriginalSentDateTime ()
  {
    return m_aOriginalSentDateTime;
  }

  /**
   * @return The message object on which the error occurred. May be
   *         <code>null</code> if e.g. a general authentication problem
   *         occurred.
   */
  @Nullable
  public IEmailData getEmailData ()
  {
    return m_aEmailData;
  }

  @Nonnull
  public Throwable getError ()
  {
    return m_aError;
  }

  @Nonnull
  public String getErrorTimeDisplayText (@Nonnull final Locale aDisplayLocale)
  {
    return PDTToString.getAsString (m_aErrorDT, aDisplayLocale);
  }

  @Nonnull
  public String getSMTPServerDisplayText ()
  {
    String ret = m_aSettings.getHostName () + ":" + m_aSettings.getPort ();
    if (StringHelper.hasText (m_aSettings.getUserName ()))
    {
      ret += "[" + m_aSettings.getUserName ();
      if (StringHelper.hasText (m_aSettings.getPassword ()))
        ret += "/****";
      ret += ']';
    }
    return ret;
  }

  @Nonnull
  public String getSenderDisplayText ()
  {
    return m_aEmailData == null ? "" : m_aEmailData.getFrom ().getDisplayName ();
  }

  @Nonnull
  public String getRecipientDisplayText ()
  {
    final StringBuilder ret = new StringBuilder ();
    if (m_aEmailData != null)
      for (final IEmailAddress aEmailAddress : m_aEmailData.getTo ())
      {
        if (ret.length () > 0)
          ret.append ("; ");
        ret.append (aEmailAddress.getDisplayName ());
      }
    return ret.toString ();
  }

  @Nullable
  public String getMessageDisplayText ()
  {
    return m_aError.getMessage ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("id", m_sID)
                                       .append ("dateTime", m_aErrorDT)
                                       .append ("settings", m_aSettings)
                                       .appendIfNotNull ("originalSentDateTime", m_aOriginalSentDateTime)
                                       .appendIfNotNull ("mailData", m_aEmailData)
                                       .append ("error", m_aError)
                                       .toString ();
  }
}
