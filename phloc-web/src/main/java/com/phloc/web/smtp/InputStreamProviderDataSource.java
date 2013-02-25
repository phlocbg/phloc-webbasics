package com.phloc.web.smtp;

import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.string.ToStringGenerator;

public final class InputStreamProviderDataSource implements DataSource
{
  private final IInputStreamProvider m_aISS;
  private final String m_sFilename;
  private final String m_sContentType;

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
