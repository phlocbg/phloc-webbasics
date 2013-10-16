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
package com.phloc.web.smtp.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.web.datasource.InputStreamProviderDataSource;
import com.phloc.web.smtp.EEmailAttachmentDisposition;
import com.phloc.web.smtp.IEmailAttachmentDataSource;

public class EmailAttachmentDataSource extends InputStreamProviderDataSource implements IEmailAttachmentDataSource
{
  private final EEmailAttachmentDisposition m_eDisposition;

  public EmailAttachmentDataSource (@Nonnull final IInputStreamProvider aISS,
                                    @Nonnull final String sFilename,
                                    @Nullable final String sContentType,
                                    @Nonnull final EEmailAttachmentDisposition eDisposition)
  {
    super (aISS, sFilename, sContentType);
    if (eDisposition == null)
      throw new NullPointerException ("disposition");
    m_eDisposition = eDisposition;
  }

  @Nonnull
  public EEmailAttachmentDisposition getDisposition ()
  {
    return m_eDisposition;
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("disposition", m_eDisposition).toString ();
  }
}
