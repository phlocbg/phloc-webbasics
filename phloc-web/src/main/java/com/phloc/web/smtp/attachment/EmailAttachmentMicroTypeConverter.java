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

import com.phloc.commons.base64.Base64;
import com.phloc.commons.base64.Base64Helper;
import com.phloc.commons.io.streamprovider.ByteArrayInputStreamProvider;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroElement;

public final class EmailAttachmentMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ATTR_FILENAME = "filename";

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aSource,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull final String sTagName)
  {
    final IEmailAttachment aAttachment = (IEmailAttachment) aSource;
    final IMicroElement eAttachment = new MicroElement (sNamespaceURI, sTagName);
    eAttachment.setAttribute (ATTR_FILENAME, aAttachment.getFilename ());
    // Base64 encode
    eAttachment.appendText (Base64.encodeBytes (StreamUtils.getAllBytes (aAttachment.getInputStream ())));
    return eAttachment;
  }

  @Nonnull
  public EmailAttachment convertToNative (@Nonnull final IMicroElement eAttachment)
  {
    final String sFilename = eAttachment.getAttribute (ATTR_FILENAME);
    final byte [] aContent = Base64Helper.safeDecode (eAttachment.getTextContent ());
    return new EmailAttachment (sFilename, new ByteArrayInputStreamProvider (aContent));
  }
}
