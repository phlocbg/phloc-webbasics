package com.phloc.webbasics.app.html;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.mime.CMimeType;
import com.phloc.css.DefaultCSSClassProvider;
import com.phloc.css.ICSSClassProvider;
import com.phloc.css.media.CSSMediaList;
import com.phloc.css.media.ECSSMedium;
import com.phloc.html.CHTMLCharset;
import com.phloc.html.condcomment.ConditionalComment;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCBody;
import com.phloc.html.hc.html.HCHead;
import com.phloc.html.hc.html.HCHtml;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.meta.EStandardMetaElement;
import com.phloc.html.meta.MetaElement;
import com.phloc.html.resource.css.CSSExternal;
import com.phloc.html.resource.js.JSExternal;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.app.SimpleWebRequestManager;

/**
 * Main class for creating HTML output
 * 
 * @author philip
 */
public class HTMLCreationManager {
  private static final ICSSClassProvider CSS_CLASS_LAYOUT_AREA = DefaultCSSClassProvider.create ("layout_area");

  public HTMLCreationManager () {}

  /**
   * Fill the HTML HEAD element.
   * 
   * @param aHtml
   *        The HTML object to be filled.
   */
  private static void _createHead (@Nonnull final HCHtml aHtml) {
    final HCHead aHead = aHtml.getHead ();
    // Special meta tag
    aHead.addMetaElement (EStandardMetaElement.CONTENT_TYPE.getAsMetaElement (CMimeType.TEXT_XML.getAsStringWithEncoding (CHTMLCharset.CHARSET_HTML)));
    // Add all configured meta element
    for (final Map.Entry <String, String> aEntry : SimpleWebHTMLConfigManager.getInstance ()
                                                                             .getAllMetaTags ()
                                                                             .entrySet ())
      aHead.addMetaElement (new MetaElement (aEntry.getKey (), aEntry.getValue ()));
    // Add configured CSS
    for (final String sCSSFile : SimpleWebHTMLConfigManager.getInstance ().getAllCSSFiles ())
      aHead.addCSS (new CSSExternal (LinkUtils.makeAbsoluteSimpleURL (sCSSFile)));
    // Add configured print-only CSS
    for (final String sCSSPrintFile : SimpleWebHTMLConfigManager.getInstance ().getAllCSSPrintFiles ())
      aHead.addCSS (new CSSExternal (LinkUtils.makeAbsoluteSimpleURL (sCSSPrintFile),
                                     new CSSMediaList (ContainerHelper.newList (ECSSMedium.PRINT)),
                                     null));
    // Add configured IE-only CSS
    for (final String sCSSIEFile : SimpleWebHTMLConfigManager.getInstance ().getAllCSSIEFiles ())
      aHead.addCSS (new CSSExternal (LinkUtils.makeAbsoluteSimpleURL (sCSSIEFile),
                                     null,
                                     ConditionalComment.createForIE ()));
    // Add all configured JS
    for (final String sJSFile : SimpleWebHTMLConfigManager.getInstance ().getAllJSFiles ())
      aHead.addJS (new JSExternal (LinkUtils.makeAbsoluteSimpleURL (sJSFile)));
  }

  @OverrideOnDemand
  @Nonnull
  protected List <String> getAllAreaIDs () {
    return SimpleWebLayoutManager.getAllAreaIDs ();
  }

  @OverrideOnDemand
  @Nullable
  protected IHCNode getContentOfArea (@Nonnull final String sAreaID, @Nonnull final Locale aDisplayLocale) {
    return SimpleWebLayoutManager.getContentOfArea (sAreaID, aDisplayLocale);
  }

  @Nonnull
  public final HCHtml createPageHTML () {
    final HCHtml aHtml = new HCHtml ();
    final Locale aDisplayLocale = SimpleWebRequestManager.getRequestDisplayLocale ();

    // create the default layout and fill the areas
    final HCBody aBody = aHtml.getBody ();
    for (final String sAreaID : getAllAreaIDs ()) {
      final HCSpan aSpan = aBody.addAndReturnChild (new HCSpan ().addClass (CSS_CLASS_LAYOUT_AREA).setID (sAreaID));
      try {
        aSpan.addChild (getContentOfArea (sAreaID, aDisplayLocale));
      }
      catch (final Throwable t) {
        // send internal error mail here
        InternalErrorHandler.handleInternalError (aSpan, t);
      }
    }

    // build HTML header (after body for per-request stuff)
    _createHead (aHtml);

    return aHtml;
  }
}
