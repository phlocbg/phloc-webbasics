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
package com.phloc.web.smtp.attachment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroElement;

public final class EmailAttachmentListMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ELEMENT_ATTACHMENT = "attachment";

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aSource,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull final String sTagName)
  {
    final EmailAttachmentList aAttachmentList = (EmailAttachmentList) aSource;
    final IMicroElement eAttachmentList = new MicroElement (sNamespaceURI, sTagName);
    for (final IEmailAttachment aAttachment : aAttachmentList.directGetAllAttachments ())
      eAttachmentList.appendChild (MicroTypeConverter.convertToMicroElement (aAttachment,
                                                                             sNamespaceURI,
                                                                             ELEMENT_ATTACHMENT));
    return eAttachmentList;
  }

  @Nonnull
  public EmailAttachmentList convertToNative (@Nonnull final IMicroElement eAttachmentList)
  {
    final EmailAttachmentList ret = new EmailAttachmentList ();
    for (final IMicroElement eAttachment : eAttachmentList.getChildElements (ELEMENT_ATTACHMENT))
      ret.addAttachment (MicroTypeConverter.convertToNative (eAttachment, EmailAttachment.class));
    return ret;
  }
}
