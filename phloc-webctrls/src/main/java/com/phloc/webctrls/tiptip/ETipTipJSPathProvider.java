package com.phloc.webctrls.tiptip;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.resource.js.IJSPathProvider;
import com.phloc.html.resource.js.JSFilenameHelper;

public enum ETipTipJSPathProvider implements IJSPathProvider
{
  TIPTIP_13 ("tiptip/13/jquery.tiptip.js");

  private final String m_sPath;

  private ETipTipJSPathProvider (@Nonnull @Nonempty final String sPath)
  {
    if (!JSFilenameHelper.isJSFilename (sPath))
      throw new IllegalArgumentException ("path");
    m_sPath = sPath;
  }

  @Nonnull
  @Nonempty
  public String getJSItemPath (final boolean bRegular)
  {
    return bRegular ? m_sPath : JSFilenameHelper.getMinifiedJSPath (m_sPath);
  }

  public boolean canBeBundled ()
  {
    return true;
  }
}
