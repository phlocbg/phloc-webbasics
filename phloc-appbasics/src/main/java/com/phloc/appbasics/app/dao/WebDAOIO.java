package com.phloc.appbasics.app.dao;

import java.io.InputStream;
import java.nio.charset.Charset;

import javax.annotation.Nullable;

import com.phloc.appbasics.app.io.WebIO;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.state.ESuccess;

public class WebDAOIO
{
  public WebDAOIO ()
  {}

  /**
   * @return Overwrite this only if you know what you're doing.
   */
  @Nullable
  public InputStream openInputStream (@Nullable final String sFilename)
  {
    // we're not operating on a file!
    if (sFilename == null)
      return null;

    return getReadableResource (sFilename).getInputStream ();
  }

  public IReadableResource getReadableResource (final String sFilename)
  {
    return WebIO.getReadableResource (sFilename);
  }

  public void renameFile (final String sSrcFileName, final String sDstFileName)
  {
    if (WebIO.resourceExists (sSrcFileName))
    {
      // if there is already a backup file, delete it
      if (WebIO.resourceExists (sDstFileName) && WebIO.deleteFile (sDstFileName).isFailure ())
      {
        // Weird...
      }
      else
      {
        // and rename existing file to backup file
        WebIO.renameFile (sSrcFileName, sDstFileName);
      }
    }
  }

  public ESuccess saveFile (final String sFilename, final String sContent, final Charset aCharset)
  {
    return WebIO.saveFile (sFilename, sContent, aCharset);
  }
}
