package com.phloc.web.smtp.attachment;

import java.io.InputStream;

import javax.activation.DataSource;
import javax.activation.FileTypeMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.io.streamprovider.ByteArrayInputStreamProvider;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

public class EmailAttachment implements IEmailAttachment
{
  private final String m_sFilename;
  private final String m_sContentType;
  private final IInputStreamProvider m_aInputStreamProvider;

  public EmailAttachment (@Nonnull @Nonempty final String sFilename, @Nonnull final byte [] aContent)
  {
    this (sFilename, new ByteArrayInputStreamProvider (aContent));
  }

  public EmailAttachment (@Nonnull @Nonempty final String sFilename,
                          @Nonnull final IInputStreamProvider aInputStreamProvider)
  {
    if (StringHelper.hasNoText (sFilename))
      throw new IllegalArgumentException ("filename");
    if (aInputStreamProvider == null)
      throw new NullPointerException ("InputStreamProvider");
    m_sFilename = sFilename;
    m_sContentType = FileTypeMap.getDefaultFileTypeMap ().getContentType (sFilename);
    m_aInputStreamProvider = aInputStreamProvider;
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
  public String getContentType ()
  {
    return m_sContentType;
  }

  @Nonnull
  public DataSource getAsDataSource ()
  {
    return new InputStreamProviderDataSource (m_aInputStreamProvider, m_sFilename, m_sContentType);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("filename", m_sFilename)
                                       .append ("inputStreamProvider", m_aInputStreamProvider)
                                       .append ("contentType", m_sContentType)
                                       .toString ();
  }
}
