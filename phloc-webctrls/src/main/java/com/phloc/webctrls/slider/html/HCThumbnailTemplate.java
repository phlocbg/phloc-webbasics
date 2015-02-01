package com.phloc.webctrls.slider.html;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;

public class HCThumbnailTemplate extends AbstractCustomHCElement <HCThumbnailTemplate>
{
  private static final long serialVersionUID = 2946934654146040749L;

  public HCThumbnailTemplate ()
  {
    super ("thumbnailtemplate"); //$NON-NLS-1$
  }

  @Override
  @OverrideOnDemand
  @Nonnull
  protected IMicroElement createElement (@Nonnull final IHCConversionSettingsToNode aConversionSettings)
  {
    final IMicroElement aElement = super.createElement (aConversionSettings);
    aElement.appendText ("");
    return aElement;

  }
}
