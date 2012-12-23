package com.phloc.webctrls.tiptip;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.css.CSSFilenameHelper;
import com.phloc.html.resource.css.ICSSPathProvider;

/**
 * Contains default CSS paths within this library.
 * 
 * @author philip
 */
public enum ETipTipCSSPathProvider implements ICSSPathProvider
{
  TOOLTIP ("tiptip/tooltip.css"),
  TIPTIP_13 ("tiptip/13/jquery.tiptip.css");

  private final String m_sPath;

  private ETipTipCSSPathProvider (@Nonnull @Nonempty final String sPath)
  {
    if (!CSSFilenameHelper.isCSSFilename (sPath))
      throw new IllegalArgumentException ("path");
    m_sPath = sPath;
  }

  @Nonnull
  @Nonempty
  public String getCSSItemPath (final boolean bRegular)
  {
    return bRegular ? m_sPath : CSSFilenameHelper.getMinifiedCSSFilename (m_sPath);
  }
}
