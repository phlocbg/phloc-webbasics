package com.phloc.bootstrap3.embed;

import javax.annotation.Nonnull;

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.html.hc.html.AbstractHCDiv;
import com.phloc.html.hc.html.HCEmbed;
import com.phloc.html.hc.html.HCIFrame;
import com.phloc.html.hc.html.HCObject;

/**
 * Bootstrap responsive embed. Available from Bootstrap 3.2.0 on. It works with
 * {@link HCObject}, {@link HCIFrame} and {@link HCEmbed}.
 *
 * @author Philip Helger
 */
public class BootstrapEmbed extends AbstractHCDiv <BootstrapEmbed>
{
  public BootstrapEmbed (@Nonnull final EBootstrapEmbedType eType, @Nonnull final HCObject aObject)
  {
    addClasses (CBootstrapCSS.EMBED_RESPONSIVE, eType);
    addChild (aObject.addClass (CBootstrapCSS.EMBED_RESPONSIVE_ITEM));
  }

  public BootstrapEmbed (@Nonnull final EBootstrapEmbedType eType, @Nonnull final HCIFrame aIFrame)
  {
    addClasses (CBootstrapCSS.EMBED_RESPONSIVE, eType);
    addChild (aIFrame.addClass (CBootstrapCSS.EMBED_RESPONSIVE_ITEM));
  }

  public BootstrapEmbed (@Nonnull final EBootstrapEmbedType eType, @Nonnull final HCEmbed aEmbed)
  {
    addClasses (CBootstrapCSS.EMBED_RESPONSIVE, eType);
    addChild (aEmbed.addClass (CBootstrapCSS.EMBED_RESPONSIVE_ITEM));
  }
}
