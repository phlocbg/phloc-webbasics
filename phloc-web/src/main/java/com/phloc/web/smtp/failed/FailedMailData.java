/**
 * Copyright (C) 2006-2015 phloc systems
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

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.email.IEmailAddress;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.type.ITypedObject;
import com.phloc.commons.type.ObjectType;
import com.phloc.datetime.PDTFactory;
import com.phloc.datetime.format.PDTToString;
import com.phloc.web.smtp.IEmailData;
import com.phloc.web.smtp.ISMTPSettings;

@Immutable
public final class FailedMailData implements ITypedObject <String>, Serializable
{
  private static final long serialVersionUID = 1897012959398052936L;

  public static final ObjectType TYPE_FAILEDMAIL = new ObjectType ("failedmail");

  private final String m_sID;
  private final LocalDateTime m_aErrorDT;
  private final ISMTPSettings m_aSettings;
  private final DateTime m_aOriginalSentDateTime;
  private final IEmailData m_aEmailData;
  private final Throwable m_aError;
  private int m_nError;

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
   * Constructor for message specific error.
   * 
   * @param aSettings
   *        The mail settings for which the error occurs. Never
   *        <code>null</code>.
   * @param aEmailData
   *        The message that failed to send. May not be <code>null</code> in
   *        practice.
   */
  public FailedMailData (@Nonnull final ISMTPSettings aSettings, @Nullable final IEmailData aEmailData)
  {
    this (aSettings, aEmailData, (Throwable) null);
  }

  /**
   * Constructor for message specific error.
   * 
   * @param aSettings
   *        The mail settings for which the error occurs. Never
   *        <code>null</code>.
   * @param aEmailData
   *        The message that failed to send. May be <code>null</code> if it is a
   *        mail-independent error.
   * @param aError
   *        The exception that occurred. May be <code>null</code>.
   */
  public FailedMailData (@Nonnull final ISMTPSettings aSettings,
                         @Nullable final IEmailData aEmailData,
                         @Nullable final Throwable aError)
  {
    this (GlobalIDFactory.getNewPersistentStringID (),
          PDTFactory.getCurrentLocalDateTime (),
          aSettings,
          aEmailData == null ? null : aEmailData.getSentDate (),
          aEmailData,
          aError);
  }

  public FailedMailData (@Nonnull final String sID,
                         @Nonnull final LocalDateTime aErrorDT,
                         @Nonnull final ISMTPSettings aSettings,
                         @Nullable final DateTime aOriginalSentDT,
                         @Nullable final IEmailData aEmailData,
                         @Nullable final Throwable aError)
  {
    this (sID, aErrorDT, aSettings, aOriginalSentDT, aEmailData, aError, 0);
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
   *        The exception that occurred. May be <code>null</code>.
   * @param nErrorCode
   *        the error code returned by the SMTP
   */
  public FailedMailData (@Nonnull final String sID,
                         @Nonnull final LocalDateTime aErrorDT,
                         @Nonnull final ISMTPSettings aSettings,
                         @Nullable final DateTime aOriginalSentDT,
                         @Nullable final IEmailData aEmailData,
                         @Nullable final Throwable aError,
                         final int nErrorCode)
  {
    this.m_sID = ValueEnforcer.notNull (sID, "ID");
    this.m_aErrorDT = ValueEnforcer.notNull (aErrorDT, "ErrorDT");
    this.m_aSettings = ValueEnforcer.notNull (aSettings, "Settings");
    this.m_aOriginalSentDateTime = aOriginalSentDT != null ? aOriginalSentDT
                                                           : (aEmailData != null ? aEmailData.getSentDate () : null);
    this.m_aEmailData = aEmailData;
    this.m_aError = aError;
    this.m_nError = nErrorCode;
  }

  @Override
  @Nonnull
  public ObjectType getTypeID ()
  {
    return TYPE_FAILEDMAIL;
  }

  @Override
  @Nonnull
  public String getID ()
  {
    return this.m_sID;
  }

  /**
   * @return The date and time when the error occurred.
   */
  @Nonnull
  public LocalDateTime getErrorDateTime ()
  {
    return this.m_aErrorDT;
  }

  /**
   * @return The original SMTP settings used for sending the mail.
   */
  @Nonnull
  public ISMTPSettings getSMTPSettings ()
  {
    return this.m_aSettings;
  }

  /**
   * @return The date and time when the message was originally sent.
   */
  @Nonnull
  public DateTime getOriginalSentDateTime ()
  {
    return this.m_aOriginalSentDateTime;
  }

  /**
   * @return The message object on which the error occurred. May be
   *         <code>null</code> if e.g. a general authentication problem
   *         occurred.
   */
  @Nullable
  public IEmailData getEmailData ()
  {
    return this.m_aEmailData;
  }

  @Nullable
  public Throwable getError ()
  {
    return this.m_aError;
  }

  public int getErrorCode ()
  {
    return this.m_nError;
  }

  public void setErrorCode (final int nErrorCode)
  {
    this.m_nError = nErrorCode;
  }

  @Nonnull
  public String getErrorTimeDisplayText (@Nonnull final Locale aDisplayLocale)
  {
    return PDTToString.getAsString (this.m_aErrorDT, aDisplayLocale);
  }

  @Nonnull
  public String getSMTPServerDisplayText ()
  {
    String ret = this.m_aSettings.getHostName () + ":" + this.m_aSettings.getPort ();
    if (StringHelper.hasText (this.m_aSettings.getUserName ()))
    {
      ret += "[" + this.m_aSettings.getUserName ();
      if (StringHelper.hasText (this.m_aSettings.getPassword ()))
        ret += "/****";
      ret += ']';
    }
    return ret;
  }

  @Nonnull
  public String getSenderDisplayText ()
  {
    return this.m_aEmailData == null ? "" : this.m_aEmailData.getFrom ().getDisplayName ();
  }

  @Nonnull
  public String getRecipientDisplayText ()
  {
    final StringBuilder ret = new StringBuilder ();
    if (this.m_aEmailData != null)
      for (final IEmailAddress aEmailAddress : this.m_aEmailData.getTo ())
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
    return this.m_aError == null ? null : this.m_aError.getMessage ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof FailedMailData))
      return false;
    final FailedMailData rhs = (FailedMailData) o;
    return this.m_sID.equals (rhs.m_sID) &&
           this.m_aErrorDT.equals (rhs.m_aErrorDT) &&
           this.m_aSettings.equals (rhs.m_aSettings) &&
           EqualsUtils.equals (this.m_aOriginalSentDateTime, rhs.m_aOriginalSentDateTime) &&
           EqualsUtils.equals (this.m_aEmailData, rhs.m_aEmailData) &&
           EqualsUtils.equals (getMessageDisplayText (), rhs.getMessageDisplayText ());
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (this.m_sID)
                                       .append (this.m_aErrorDT)
                                       .append (this.m_aSettings)
                                       .append (this.m_aOriginalSentDateTime)
                                       .append (this.m_aEmailData)
                                       .append (getMessageDisplayText ())
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("id", this.m_sID)
                                       .append ("errorDateTime", this.m_aErrorDT)
                                       .append ("settings", this.m_aSettings)
                                       .appendIfNotNull ("originalSentDateTime", this.m_aOriginalSentDateTime)
                                       .appendIfNotNull ("emailData", this.m_aEmailData)
                                       .appendIfNotNull ("error", this.m_aError)
                                       .toString ();
  }
}
