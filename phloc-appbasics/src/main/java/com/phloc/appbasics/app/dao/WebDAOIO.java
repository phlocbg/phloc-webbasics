package com.phloc.appbasics.app.dao;

import java.io.InputStream;
import java.nio.charset.Charset;

import javax.annotation.Nullable;

import com.phloc.appbasics.app.io.WebIO;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.state.ISuccessIndicator;

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

    return WebIO.getInputStream (sFilename, false);
  }

  public IReadableResource getReadableResource (final String sFilename)
  {
    return WebIO.getReadableResource (sFilename);
  }

  public boolean resourceExists (final String sFilename)
  {
    return WebIO.resourceExists (sFilename);
  }

  public ISuccessIndicator renameFile (final String sSrcFileName, final String sDstFileName)
  {
    return WebIO.renameFile (sSrcFileName, sDstFileName);
  }

  public ESuccess saveFile (final String sFilename, final String sContent, final Charset aCharset)
  {
    return WebIO.saveFile (sFilename, sContent, aCharset);
  }

  public ISuccessIndicator deleteFile (final String sFilename)
  {
    return WebIO.deleteFile (sFilename);
  }
}
