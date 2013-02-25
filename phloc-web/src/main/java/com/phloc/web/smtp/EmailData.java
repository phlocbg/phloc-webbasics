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
package com.phloc.web.smtp;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.joda.time.DateTime;

import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.email.IEmailAddress;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.web.smtp.attachment.IEmailAttachments;

/**
 * Default implementation of the {@link IEmailData} interface.
 * 
 * @author philip
 */
@NotThreadSafe
public final class EmailData implements IEmailData
{
  private EEmailType m_eEmailType;
  private IEmailAddress m_aFrom;
  private final List <IEmailAddress> m_aReplyTo = new ArrayList <IEmailAddress> ();
  private final List <IEmailAddress> m_aTo = new ArrayList <IEmailAddress> ();
  private final List <IEmailAddress> m_aCc = new ArrayList <IEmailAddress> ();
  private final List <IEmailAddress> m_aBcc = new ArrayList <IEmailAddress> ();
  private DateTime m_aSentDate;
  private String m_sSubject;
  private String m_sBody;
  private IEmailAttachments m_aAttachments;

  public EmailData (@Nonnull final EEmailType eEmailType)
  {
    setEmailType (eEmailType);
  }

  @Nonnull
  public EEmailType getEmailType ()
  {
    return m_eEmailType;
  }

  public void setEmailType (@Nonnull final EEmailType eEmailType)
  {
    if (eEmailType == null)
      throw new NullPointerException ("emailType");
    m_eEmailType = eEmailType;
  }

  public void setFrom (@Nullable final IEmailAddress sFrom)
  {
    m_aFrom = sFrom;
  }

  @Nullable
  public IEmailAddress getFrom ()
  {
    return m_aFrom;
  }

  public void setReplyTo (@Nullable final IEmailAddress aReplyTo)
  {
    m_aReplyTo.clear ();
    if (aReplyTo != null)
      m_aReplyTo.add (aReplyTo);
  }

  public void setReplyTo (@Nullable final List <? extends IEmailAddress> aReplyTos)
  {
    m_aReplyTo.clear ();
    if (aReplyTos != null)
      for (final IEmailAddress aReplyTo : aReplyTos)
        if (aReplyTo != null)
          m_aReplyTo.add (aReplyTo);
  }

  @Nonnull
  @ReturnsImmutableObject
  public List <? extends IEmailAddress> getReplyTo ()
  {
    return ContainerHelper.makeUnmodifiable (m_aReplyTo);
  }

  @Nonnull
  private static InternetAddress [] _asArray (@Nonnull final List <IEmailAddress> aList, @Nullable final String sCharset) throws UnsupportedEncodingException,
                                                                                                                         AddressException
  {
    final InternetAddress [] ret = new InternetAddress [aList.size ()];
    int i = 0;
    for (final IEmailAddress aMA : aList)
      ret[i++] = InternetAddressUtils.getAsInternetAddress (aMA, sCharset);
    return ret;
  }

  @Nonnull
  public InternetAddress [] getReplyToArray (@Nullable final String sCharset) throws UnsupportedEncodingException,
                                                                             AddressException
  {
    return _asArray (m_aReplyTo, sCharset);
  }

  @Nonnegative
  public int getReplyToCount ()
  {
    return m_aReplyTo.size ();
  }

  public void setTo (@Nullable final IEmailAddress aTo)
  {
    m_aTo.clear ();
    if (aTo != null)
      m_aTo.add (aTo);
  }

  public void setTo (@Nullable final List <? extends IEmailAddress> aTos)
  {
    m_aTo.clear ();
    if (aTos != null)
      for (final IEmailAddress aTo : aTos)
        if (aTo != null)
          m_aTo.add (aTo);
  }

  @Nonnull
  @ReturnsImmutableObject
  public List <? extends IEmailAddress> getTo ()
  {
    return ContainerHelper.makeUnmodifiable (m_aTo);
  }

  @Nonnull
  public InternetAddress [] getToArray (@Nullable final String sCharset) throws UnsupportedEncodingException,
                                                                        AddressException
  {
    return _asArray (m_aTo, sCharset);
  }

  @Nonnegative
  public int getToCount ()
  {
    return m_aTo.size ();
  }

  public void setCc (@Nullable final IEmailAddress aCc)
  {
    m_aCc.clear ();
    if (aCc != null)
      m_aCc.add (aCc);
  }

  public void setCc (@Nullable final List <? extends IEmailAddress> aCcs)
  {
    m_aCc.clear ();
    if (aCcs != null)
      for (final IEmailAddress aCc : aCcs)
        if (aCc != null)
          m_aCc.add (aCc);
  }

  @Nonnull
  @ReturnsImmutableObject
  public List <? extends IEmailAddress> getCc ()
  {
    return ContainerHelper.makeUnmodifiable (m_aCc);
  }

  @Nonnull
  public InternetAddress [] getCcArray (@Nullable final String sCharset) throws UnsupportedEncodingException,
                                                                        AddressException
  {
    return _asArray (m_aCc, sCharset);
  }

  @Nonnegative
  public int getCcCount ()
  {
    return m_aCc.size ();
  }

  public void setBcc (@Nullable final IEmailAddress aBcc)
  {
    m_aBcc.clear ();
    if (aBcc != null)
      m_aBcc.add (aBcc);
  }

  public void setBcc (@Nullable final List <? extends IEmailAddress> aBccs)
  {
    m_aBcc.clear ();
    if (aBccs != null)
      for (final IEmailAddress aBcc : aBccs)
        if (aBcc != null)
          m_aBcc.add (aBcc);
  }

  @Nonnull
  @ReturnsImmutableObject
  public List <? extends IEmailAddress> getBcc ()
  {
    return ContainerHelper.makeUnmodifiable (m_aBcc);
  }

  @Nonnull
  public InternetAddress [] getBccArray (@Nullable final String sCharset) throws UnsupportedEncodingException,
                                                                         AddressException
  {
    return _asArray (m_aBcc, sCharset);
  }

  @Nonnegative
  public int getBccCount ()
  {
    return m_aBcc.size ();
  }

  public void setSentDate (@Nullable final DateTime aSentDate)
  {
    m_aSentDate = aSentDate;
  }

  @Nullable
  public DateTime getSentDate ()
  {
    return m_aSentDate;
  }

  public void setSubject (@Nullable final String sSubject)
  {
    m_sSubject = sSubject;
  }

  @Nullable
  public String getSubject ()
  {
    return m_sSubject;
  }

  public void setBody (@Nullable final String sBody)
  {
    m_sBody = sBody;
  }

  @Nullable
  public String getBody ()
  {
    return m_sBody;
  }

  @Nullable
  public IEmailAttachments getAttachments ()
  {
    return m_aAttachments;
  }

  public void setAttachments (@Nullable final IEmailAttachments aAttachments)
  {
    if (aAttachments != null && aAttachments.size () > 0)
      m_aAttachments = aAttachments;
    else
      m_aAttachments = null;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof EmailData))
      return false;
    final EmailData rhs = (EmailData) o;
    return EqualsUtils.equals (m_aFrom, rhs.m_aFrom) &&
           EqualsUtils.equals (m_aReplyTo, rhs.m_aReplyTo) &&
           m_aTo.equals (rhs.m_aTo) &&
           m_aCc.equals (rhs.m_aCc) &&
           m_aBcc.equals (rhs.m_aBcc) &&
           EqualsUtils.equals (m_aSentDate, rhs.m_aSentDate) &&
           EqualsUtils.equals (m_sSubject, rhs.m_sSubject) &&
           EqualsUtils.equals (m_sBody, rhs.m_sBody) &&
           EqualsUtils.equals (m_aAttachments, rhs.m_aAttachments);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aFrom)
                                       .append (m_aReplyTo)
                                       .append (m_aTo)
                                       .append (m_aCc)
                                       .append (m_aBcc)
                                       .append (m_aSentDate)
                                       .append (m_sSubject)
                                       .append (m_sBody)
                                       .append (m_aAttachments)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("from", m_aFrom)
                                       .appendIfNotNull ("replyTo", m_aReplyTo)
                                       .append ("to", m_aTo)
                                       .appendIfNotNull ("cc", m_aCc)
                                       .appendIfNotNull ("bcc", m_aBcc)
                                       .append ("sendDate", m_aSentDate)
                                       .append ("subject", m_sSubject)
                                       .append ("body", m_sBody)
                                       .appendIfNotNull ("attachments", m_aAttachments)
                                       .toString ();
  }

  /**
   * Utility method for converting different fields to a single
   * {@link IEmailData}.
   * 
   * @param eEmailType
   *        The type of the email.
   * @param aSender
   *        The sender address.
   * @param aReceiver
   *        The receiver address.
   * @param sSubject
   *        The subject line.
   * @param sBody
   *        The mail body text.
   * @param aAttachments
   *        Any attachments to use. May be <code>null</code>.
   */
  @Nonnull
  public static IEmailData createEmailData (@Nonnull final EEmailType eEmailType,
                                            @Nullable final IEmailAddress aSender,
                                            @Nullable final IEmailAddress aReceiver,
                                            @Nullable final String sSubject,
                                            @Nullable final String sBody,
                                            @Nullable final IEmailAttachments aAttachments)
  {
    final IEmailData aEmailData = new EmailData (eEmailType);
    aEmailData.setFrom (aSender);
    aEmailData.setTo (aReceiver);
    aEmailData.setSubject (sSubject);
    aEmailData.setBody (sBody);
    aEmailData.setAttachments (aAttachments);
    return aEmailData;
  }
}
