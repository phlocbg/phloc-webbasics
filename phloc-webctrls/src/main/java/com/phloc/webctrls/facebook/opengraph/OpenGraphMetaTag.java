package com.phloc.webctrls.facebook.opengraph;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;
import com.phloc.html.meta.MetaElement;

public class OpenGraphMetaTag extends MetaElement
{
  /** The namespace URI for OpenGraph elements */
  public static final String OPENGRAPH_NAMESPACE_URI = "http://ogp.me/ns#";

  public OpenGraphMetaTag (@Nonnull final EOpenGraphMetaTag eProperty, @Nullable final String sContent)
  {
    super (eProperty.getID (), false, sContent);
  }

  @Override
  @Nonnull
  @Nonempty
  @OverrideOnDemand
  protected String getNamespaceURI (@Nonnull final IHCConversionSettingsToNode aConversionSettings)
  {
    return OPENGRAPH_NAMESPACE_URI;
  }

  @Override
  @Nonnull
  @Nonempty
  @OverrideOnDemand
  protected String getNodeNameAttribute ()
  {
    return "property";
  }
}
