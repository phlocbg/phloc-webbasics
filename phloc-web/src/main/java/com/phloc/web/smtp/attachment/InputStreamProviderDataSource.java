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
import java.io.OutputStream;

import javax.activation.DataSource;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.mime.IMimeType;
import com.phloc.commons.string.ToStringGenerator;

/**
 * A special {@link DataSource} implementation based on data from
 * {@link IInputStreamProvider}.
 * 
 * @author Philip Helger
 */
public final class InputStreamProviderDataSource implements DataSource
{
  private final IInputStreamProvider m_aISS;
  private final String m_sFilename;
  private final String m_sContentType;

  public InputStreamProviderDataSource (@Nonnull final IInputStreamProvider aISS, @Nonnull final String sFilename)
  {
    this (aISS, sFilename, (String) null);
  }

  public InputStreamProviderDataSource (@Nonnull final IInputStreamProvider aISS,
                                        @Nonnull final String sFilename,
                                        @Nullable final IMimeType aContentType)
  {
    this (aISS, sFilename, aContentType == null ? null : aContentType.getAsString ());
  }

  public InputStreamProviderDataSource (@Nonnull final IInputStreamProvider aISS,
                                        @Nonnull final String sFilename,
                                        @Nullable final String sContentType)
  {
    if (aISS == null)
      throw new NullPointerException ("inputStreamProvider");
    if (sFilename == null)
      throw new NullPointerException ("filename");
    m_aISS = aISS;
    m_sFilename = sFilename;
    m_sContentType = sContentType;
  }

  @Nullable
  public InputStream getInputStream ()
  {
    return m_aISS.getInputStream ();
  }

  @UnsupportedOperation
  public OutputStream getOutputStream ()
  {
    throw new UnsupportedOperationException ("Read-only!");
  }

  @Nonnull
  public String getContentType ()
  {
    // Use octet stream if undefined
    return m_sContentType != null ? m_sContentType : CMimeType.APPLICATION_OCTET_STREAM.getAsString ();
  }

  @Nonnull
  public String getName ()
  {
    return m_sFilename;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ISS", m_aISS)
                                       .append ("filename", m_sFilename)
                                       .appendIfNotNull ("contentType", m_sContentType)
                                       .toString ();
  }
}
