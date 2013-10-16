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

import java.io.InputStream;
import java.nio.charset.Charset;

import javax.activation.FileTypeMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.streamprovider.ByteArrayInputStreamProvider;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Base implementation for interface {@link IEmailAttachment}.
 * 
 * @author Philip Helger
 */
public class EmailAttachment implements IEmailAttachment
{
  public static final EEmailAttachmentDisposition DEFAULT_DISPOSITION = EEmailAttachmentDisposition.ATTACHMENT;

  private final String m_sFilename;
  private final IInputStreamProvider m_aInputStreamProvider;
  private final Charset m_aCharset;
  private final String m_sContentType;
  private final EEmailAttachmentDisposition m_eDisposition;

  public EmailAttachment (@Nonnull @Nonempty final String sFilename, @Nonnull final byte [] aContent)
  {
    this (sFilename, aContent, (Charset) null);
  }

  public EmailAttachment (@Nonnull @Nonempty final String sFilename,
                          @Nonnull final byte [] aContent,
                          @Nullable final Charset aCharset)
  {
    this (sFilename, aContent, aCharset, DEFAULT_DISPOSITION);
  }

  public EmailAttachment (@Nonnull @Nonempty final String sFilename,
                          @Nonnull final byte [] aContent,
                          @Nullable final Charset aCharset,
                          @Nonnull final EEmailAttachmentDisposition eDisposition)
  {
    this (sFilename, new ByteArrayInputStreamProvider (aContent), aCharset, eDisposition);
  }

  public EmailAttachment (@Nonnull @Nonempty final String sFilename,
                          @Nonnull final IInputStreamProvider aInputStreamProvider)
  {
    this (sFilename, aInputStreamProvider, (Charset) null);
  }

  public EmailAttachment (@Nonnull @Nonempty final String sFilename,
                          @Nonnull final IInputStreamProvider aInputStreamProvider,
                          @Nullable final Charset aCharset)
  {
    this (sFilename, aInputStreamProvider, aCharset, DEFAULT_DISPOSITION);
  }

  public EmailAttachment (@Nonnull @Nonempty final String sFilename,
                          @Nonnull final IInputStreamProvider aInputStreamProvider,
                          @Nullable final Charset aCharset,
                          @Nonnull final EEmailAttachmentDisposition eDisposition)
  {
    if (StringHelper.hasNoText (sFilename))
      throw new IllegalArgumentException ("filename");
    if (aInputStreamProvider == null)
      throw new NullPointerException ("InputStreamProvider");
    if (eDisposition == null)
      throw new NullPointerException ("Disposition");
    m_sFilename = sFilename;
    m_aInputStreamProvider = aInputStreamProvider;
    m_aCharset = aCharset;
    m_sContentType = FileTypeMap.getDefaultFileTypeMap ().getContentType (sFilename);
    m_eDisposition = eDisposition;
  }

  @Nonnull
  @Nonempty
  public String getFilename ()
  {
    return m_sFilename;
  }

  @Nonnull
  public IInputStreamProvider getInputStreamProvider ()
  {
    return m_aInputStreamProvider;
  }

  @Nonnull
  public InputStream getInputStream ()
  {
    return m_aInputStreamProvider.getInputStream ();
  }

  @Nullable
  public Charset getCharset ()
  {
    return m_aCharset;
  }

  @Nullable
  public String getContentType ()
  {
    return m_sContentType;
  }

  @Nonnull
  public EEmailAttachmentDisposition getDisposition ()
  {
    return m_eDisposition;
  }

  @Nonnull
  public EmailAttachmentDataSource getAsDataSource ()
  {
    return new EmailAttachmentDataSource (m_aInputStreamProvider, m_sFilename, m_sContentType, m_eDisposition);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final EmailAttachment rhs = (EmailAttachment) o;
    return m_sFilename.equals (rhs.m_sFilename) &&
    // Does not necessarily implement equals!
    // m_aInputStreamProvider.equals (rhs.m_aInputStreamProvider) &&
           EqualsUtils.equals (m_aCharset, rhs.m_aCharset) &&
           EqualsUtils.equals (m_sContentType, rhs.m_sContentType) &&
           m_eDisposition.equals (rhs.m_eDisposition);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sFilename)
    // Does not necessarily implement hashCode!
    // .append (m_aInputStreamProvider)
                                       .append (m_aCharset)
                                       .append (m_sContentType)
                                       .append (m_eDisposition)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("filename", m_sFilename)
                                       .append ("inputStreamProvider", m_aInputStreamProvider)
                                       .appendIfNotNull ("charset", m_aCharset)
                                       .append ("contentType", m_sContentType)
                                       .append ("disposition", m_eDisposition)
                                       .toString ();
  }
}
