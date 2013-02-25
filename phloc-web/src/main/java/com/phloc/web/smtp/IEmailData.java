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

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joda.time.DateTime;

import com.phloc.commons.email.IEmailAddress;
import com.phloc.web.smtp.attachment.IEmailAttachments;

/**
 * Contains all possible fields for mail sending
 * 
 * @author philip
 */
public interface IEmailData extends IReadonlyEmailData
{
  void setEmailType (@Nonnull EEmailType eType);

  void setFrom (@Nullable IEmailAddress aFrom);

  void setReplyTo (@Nullable IEmailAddress aReplyTo);

  void setReplyTo (@Nullable List <? extends IEmailAddress> aTo);

  void setTo (@Nullable IEmailAddress aTo);

  void setTo (@Nullable List <? extends IEmailAddress> aTo);

  void setCc (@Nullable IEmailAddress aCc);

  void setCc (@Nullable List <? extends IEmailAddress> aCc);

  void setBcc (@Nullable IEmailAddress aBcc);

  void setBcc (@Nullable List <? extends IEmailAddress> aBcc);

  void setSentDate (@Nullable DateTime aDate);

  void setSubject (@Nullable String sSubject);

  void setBody (@Nullable String sBody);

  @Nullable
  IEmailAttachments getAttachments ();

  /**
   * Specify a set of attachments to be send together with the mail. Pass
   * <code>null</code> to indicate that no attachments are desired (this is the
   * default).
   * 
   * @param aAttachments
   *        The attachments to be used. May be <code>null</code> or empty.
   */
  void setAttachments (@Nullable IEmailAttachments aAttachments);
}
