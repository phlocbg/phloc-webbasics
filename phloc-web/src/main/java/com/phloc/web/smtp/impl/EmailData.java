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
package com.phloc.web.smtp.impl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.joda.time.DateTime;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.commons.email.IEmailAddress;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.web.smtp.EEmailType;
import com.phloc.web.smtp.IEmailAttachmentList;
import com.phloc.web.smtp.IEmailData;
import com.phloc.web.smtp.InternetAddressUtils;

/**
 * Default implementation of the {@link IEmailData} interface. Note: the
 * attribute container may only contain String values, otherwise the
 * serialization and deserialization will result in different results!
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class EmailData extends MapBasedAttributeContainer implements IEmailData
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
  private IEmailAttachmentList m_aAttachments;
  private final Map <String, String> m_aHeaders = ContainerHelper.newMap ();
  private boolean m_bUseFailedMailQueue = true;

  public EmailData (@Nonnull final EEmailType eEmailType)
  {
    setEmailType (eEmailType);
  }

  @Override
  public IEmailData setHeader (@Nonnull @Nonempty final String sHeader, @Nonnull @Nonempty final String sValue)
  {
    this.m_aHeaders.put (sHeader, sValue);
    return this;
  }

  @Override
  public Map <String, String> getHeaders ()
  {
    return ContainerHelper.newMap (this.m_aHeaders);
  }

  @Override
  @Nonnull
  public final IEmailData setEmailType (@Nonnull final EEmailType eEmailType)
  {
    ValueEnforcer.notNull (eEmailType, "EmailType");
    this.m_eEmailType = eEmailType;
    return this;
  }

  @Override
  @Nonnull
  public EEmailType getEmailType ()
  {
    return this.m_eEmailType;
  }

  @Override
  @Nonnull
  public final IEmailData setFrom (@Nullable final IEmailAddress sFrom)
  {
    this.m_aFrom = sFrom;
    return this;
  }

  @Override
  @Nullable
  public IEmailAddress getFrom ()
  {
    return this.m_aFrom;
  }

  @Override
  @Nonnull
  public final IEmailData setReplyTo (@Nullable final IEmailAddress aReplyTo)
  {
    this.m_aReplyTo.clear ();
    if (aReplyTo != null)
      this.m_aReplyTo.add (aReplyTo);
    return this;
  }

  @Override
  @Nonnull
  public final IEmailData setReplyTo (@Nullable final IEmailAddress... aReplyTos)
  {
    this.m_aReplyTo.clear ();
    if (aReplyTos != null)
      for (final IEmailAddress aReplyTo : aReplyTos)
        if (aReplyTo != null)
          this.m_aReplyTo.add (aReplyTo);
    return this;
  }

  @Override
  @Nonnull
  public final IEmailData setReplyTo (@Nullable final List <? extends IEmailAddress> aReplyTos)
  {
    this.m_aReplyTo.clear ();
    if (aReplyTos != null)
      for (final IEmailAddress aReplyTo : aReplyTos)
        if (aReplyTo != null)
          this.m_aReplyTo.add (aReplyTo);
    return this;
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public List <? extends IEmailAddress> getReplyTo ()
  {
    return ContainerHelper.newList (this.m_aReplyTo);
  }

  @Nonnull
  @ReturnsMutableCopy
  private static InternetAddress [] _asArray (@Nonnull final List <IEmailAddress> aList,
                                              @Nullable final String sCharset) throws UnsupportedEncodingException,
                                                                               AddressException
  {
    final InternetAddress [] ret = new InternetAddress [aList.size ()];
    int i = 0;
    for (final IEmailAddress aMA : aList)
      ret[i++] = InternetAddressUtils.getAsInternetAddress (aMA, sCharset);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  private static InternetAddress [] _asArray (@Nonnull final List <IEmailAddress> aList,
                                              @Nullable final Charset aCharset) throws AddressException
  {
    try
    {
      return _asArray (aList, aCharset == null ? null : aCharset.name ());
    }
    catch (final UnsupportedEncodingException ex)
    {
      throw new IllegalStateException ("Charset " + aCharset + " is unknown!", ex);
    }
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  @Deprecated
  public InternetAddress [] getReplyToArray (@Nullable final String sCharset) throws UnsupportedEncodingException,
                                                                              AddressException
  {
    return _asArray (this.m_aReplyTo, sCharset);
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public InternetAddress [] getReplyToArray (@Nullable final Charset aCharset) throws AddressException
  {
    return _asArray (this.m_aReplyTo, aCharset);
  }

  @Override
  @Nonnegative
  public int getReplyToCount ()
  {
    return this.m_aReplyTo.size ();
  }

  @Override
  @Nonnull
  public final IEmailData setTo (@Nullable final IEmailAddress aTo)
  {
    this.m_aTo.clear ();
    if (aTo != null)
      this.m_aTo.add (aTo);
    return this;
  }

  @Override
  @Nonnull
  public final IEmailData setTo (@Nullable final IEmailAddress... aTos)
  {
    this.m_aTo.clear ();
    if (aTos != null)
      for (final IEmailAddress aTo : aTos)
        if (aTo != null)
          this.m_aTo.add (aTo);
    return this;
  }

  @Override
  @Nonnull
  public final IEmailData setTo (@Nullable final List <? extends IEmailAddress> aTos)
  {
    this.m_aTo.clear ();
    if (aTos != null)
      for (final IEmailAddress aTo : aTos)
        if (aTo != null)
          this.m_aTo.add (aTo);
    return this;
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public List <? extends IEmailAddress> getTo ()
  {
    return ContainerHelper.newList (this.m_aTo);
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  @Deprecated
  public InternetAddress [] getToArray (@Nullable final String sCharset) throws UnsupportedEncodingException,
                                                                         AddressException
  {
    return _asArray (this.m_aTo, sCharset);
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public InternetAddress [] getToArray (@Nullable final Charset aCharset) throws AddressException
  {
    return _asArray (this.m_aTo, aCharset);
  }

  @Override
  @Nonnegative
  public int getToCount ()
  {
    return this.m_aTo.size ();
  }

  @Override
  @Nonnull
  public final IEmailData setCc (@Nullable final IEmailAddress aCc)
  {
    this.m_aCc.clear ();
    if (aCc != null)
      this.m_aCc.add (aCc);
    return this;
  }

  @Override
  @Nonnull
  public final IEmailData setCc (@Nullable final IEmailAddress... aCcs)
  {
    this.m_aCc.clear ();
    if (aCcs != null)
      for (final IEmailAddress aCc : aCcs)
        if (aCc != null)
          this.m_aCc.add (aCc);
    return this;
  }

  @Override
  @Nonnull
  public final IEmailData setCc (@Nullable final List <? extends IEmailAddress> aCcs)
  {
    this.m_aCc.clear ();
    if (aCcs != null)
      for (final IEmailAddress aCc : aCcs)
        if (aCc != null)
          this.m_aCc.add (aCc);
    return this;
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public List <? extends IEmailAddress> getCc ()
  {
    return ContainerHelper.newList (this.m_aCc);
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  @Deprecated
  public InternetAddress [] getCcArray (@Nullable final String sCharset) throws UnsupportedEncodingException,
                                                                         AddressException
  {
    return _asArray (this.m_aCc, sCharset);
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public InternetAddress [] getCcArray (@Nullable final Charset aCharset) throws AddressException
  {
    return _asArray (this.m_aCc, aCharset);
  }

  @Override
  @Nonnegative
  public int getCcCount ()
  {
    return this.m_aCc.size ();
  }

  @Override
  @Nonnull
  public final IEmailData setBcc (@Nullable final IEmailAddress aBcc)
  {
    this.m_aBcc.clear ();
    if (aBcc != null)
      this.m_aBcc.add (aBcc);
    return this;
  }

  @Override
  @Nonnull
  public final IEmailData setBcc (@Nullable final IEmailAddress... aBccs)
  {
    this.m_aBcc.clear ();
    if (aBccs != null)
      for (final IEmailAddress aBcc : aBccs)
        if (aBcc != null)
          this.m_aBcc.add (aBcc);
    return this;
  }

  @Override
  @Nonnull
  public final IEmailData setBcc (@Nullable final List <? extends IEmailAddress> aBccs)
  {
    this.m_aBcc.clear ();
    if (aBccs != null)
      for (final IEmailAddress aBcc : aBccs)
        if (aBcc != null)
          this.m_aBcc.add (aBcc);
    return this;
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public List <? extends IEmailAddress> getBcc ()
  {
    return ContainerHelper.newList (this.m_aBcc);
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  @Deprecated
  public InternetAddress [] getBccArray (@Nullable final String sCharset) throws UnsupportedEncodingException,
                                                                          AddressException
  {
    return _asArray (this.m_aBcc, sCharset);
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public InternetAddress [] getBccArray (@Nullable final Charset aCharset) throws AddressException
  {
    return _asArray (this.m_aBcc, aCharset);
  }

  @Override
  @Nonnegative
  public int getBccCount ()
  {
    return this.m_aBcc.size ();
  }

  @Override
  @Nonnull
  public final IEmailData setSentDate (@Nullable final DateTime aSentDate)
  {
    this.m_aSentDate = aSentDate;
    return this;
  }

  @Override
  @Nullable
  public DateTime getSentDate ()
  {
    return this.m_aSentDate;
  }

  @Override
  @Nonnull
  public final IEmailData setSubject (@Nullable final String sSubject)
  {
    this.m_sSubject = sSubject;
    return this;
  }

  @Override
  @Nullable
  public String getSubject ()
  {
    return this.m_sSubject;
  }

  @Override
  @Nonnull
  public final IEmailData setBody (@Nullable final String sBody)
  {
    this.m_sBody = sBody;
    return this;
  }

  @Override
  @Nullable
  public String getBody ()
  {
    return this.m_sBody;
  }

  @Override
  @Nullable
  public IEmailAttachmentList getAttachments ()
  {
    return this.m_aAttachments;
  }

  @Override
  @Nonnegative
  public int getAttachmentCount ()
  {
    return this.m_aAttachments == null ? 0 : this.m_aAttachments.size ();
  }

  @Override
  @Nonnull
  public final IEmailData setAttachments (@Nullable final IEmailAttachmentList aAttachments)
  {
    if (aAttachments != null && !aAttachments.isEmpty ())
      this.m_aAttachments = aAttachments;
    else
      this.m_aAttachments = null;
    return this;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final EmailData rhs = (EmailData) o;
    return EqualsUtils.equals (this.m_aFrom, rhs.m_aFrom) &&
           EqualsUtils.equals (this.m_aReplyTo, rhs.m_aReplyTo) &&
           this.m_aTo.equals (rhs.m_aTo) &&
           this.m_aCc.equals (rhs.m_aCc) &&
           this.m_aBcc.equals (rhs.m_aBcc) &&
           EqualsUtils.equals (this.m_aSentDate, rhs.m_aSentDate) &&
           EqualsUtils.equals (this.m_sSubject, rhs.m_sSubject) &&
           EqualsUtils.equals (this.m_sBody, rhs.m_sBody) &&
           EqualsUtils.equals (this.m_aAttachments, rhs.m_aAttachments);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (this.m_aFrom)
                            .append (this.m_aReplyTo)
                            .append (this.m_aTo)
                            .append (this.m_aCc)
                            .append (this.m_aBcc)
                            .append (this.m_aSentDate)
                            .append (this.m_sSubject)
                            .append (this.m_sBody)
                            .append (this.m_aAttachments)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("from", this.m_aFrom)
                            .appendIfNotNull ("replyTo", this.m_aReplyTo)
                            .append ("to", this.m_aTo)
                            .appendIfNotNull ("cc", this.m_aCc)
                            .appendIfNotNull ("bcc", this.m_aBcc)
                            .append ("sendDate", this.m_aSentDate)
                            .append ("subject", this.m_sSubject)
                            .append ("body", this.m_sBody)
                            .appendIfNotNull ("attachments", this.m_aAttachments)
                            .toString ();
  }

  @Override
  public IEmailData setUseFailedMailQueue (final boolean bUseFailedMailQueue)
  {
    this.m_bUseFailedMailQueue = bUseFailedMailQueue;
    return this;
  }

  @Override
  public boolean isUseFailedMailQueue ()
  {
    return this.m_bUseFailedMailQueue;
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
   * @return the newly created email data
   */
  @Nonnull
  public static IEmailData createEmailData (@Nonnull final EEmailType eEmailType,
                                            @Nullable final IEmailAddress aSender,
                                            @Nullable final IEmailAddress aReceiver,
                                            @Nullable final String sSubject,
                                            @Nullable final String sBody,
                                            @Nullable final IEmailAttachmentList aAttachments)
  {
    return createEmailData (eEmailType, aSender, aReceiver, sSubject, sBody, aAttachments, true);
  }

  public static IEmailData createEmailData (@Nonnull final EEmailType eEmailType,
                                            @Nullable final IEmailAddress aSender,
                                            @Nullable final IEmailAddress aReceiver,
                                            @Nullable final String sSubject,
                                            @Nullable final String sBody,
                                            @Nullable final IEmailAttachmentList aAttachments,
                                            final boolean bUseFailedMailQueue)
  {
    final IEmailData aEmailData = new EmailData (eEmailType);
    aEmailData.setFrom (aSender);
    aEmailData.setTo (aReceiver);
    aEmailData.setSubject (sSubject);
    aEmailData.setBody (sBody);
    aEmailData.setAttachments (aAttachments);
    aEmailData.setUseFailedMailQueue (bUseFailedMailQueue);
    return aEmailData;
  }
}
