package com.phloc.web.smtp.attachment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;
import javax.activation.FileTypeMap;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This is the default implementation of the {@link IEmailAttachments}
 * interface.
 * 
 * @author philip
 */
@NotThreadSafe
public class EmailAttachments implements IEmailAttachments
{
  private final Map <String, IInputStreamProvider> m_aMap = new LinkedHashMap <String, IInputStreamProvider> ();

  public EmailAttachments ()
  {}

  @Nonnegative
  public int size ()
  {
    return m_aMap.size ();
  }

  public boolean isEmpty ()
  {
    return m_aMap.isEmpty ();
  }

  public void addAttachment (@Nonnull final String sFilename, @Nonnull final IInputStreamProvider aISS)
  {
    if (sFilename == null)
      throw new NullPointerException ("filename");
    if (aISS == null)
      throw new NullPointerException ("inputStreamProvider");
    m_aMap.put (sFilename, aISS);
  }

  @Nonnull
  public EChange removeAttachment (@Nullable final String sFilename)
  {
    return EChange.valueOf (m_aMap.remove (sFilename) != null);
  }

  public boolean containsAttachment (@Nullable final String sFilename)
  {
    return m_aMap.containsKey (sFilename);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllAttachmentFilenames ()
  {
    return ContainerHelper.newList (m_aMap.keySet ());
  }

  @Nonnull
  public List <DataSource> getAsDataSourceList ()
  {
    final List <DataSource> ret = new ArrayList <DataSource> ();
    for (final Map.Entry <String, IInputStreamProvider> aEntry : m_aMap.entrySet ())
    {
      final String sFilename = aEntry.getKey ();
      final String sContentType = FileTypeMap.getDefaultFileTypeMap ().getContentType (sFilename);
      ret.add (new InputStreamProviderDataSource (aEntry.getValue (), sFilename, sContentType));
    }
    return ret;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("map", m_aMap).toString ();
  }
}
