/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.fileupload.handler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.string.StringHelper;

public final class UploadConstantMimeTypeFilter implements IUploadMimeTypeFilter
{
  private static final long serialVersionUID = 5091055422688527769L;
  private final IMimeType m_aMimeType;
  private final String m_sErrorMessageType;

  public UploadConstantMimeTypeFilter (@Nonnull final IMimeType aMimeType,
                                       @Nonnull @Nonempty final String sErrorMessageType)
  {
    if (aMimeType == null)
    {
      throw new NullPointerException ("mimeType"); //$NON-NLS-1$
    }
    if (StringHelper.hasNoText (sErrorMessageType))
    {
      throw new IllegalArgumentException ("errorMessageType"); //$NON-NLS-1$
    }
    this.m_aMimeType = aMimeType;
    this.m_sErrorMessageType = sErrorMessageType;
  }

  public boolean matchesFilter (@Nullable final String sMimeType)
  {
    return this.m_aMimeType.getAsString ().equals (sMimeType);
  }

  @Nonnull
  @Nonempty
  public String getErrorMessageType ()
  {
    return this.m_sErrorMessageType;
  }
}
