package com.phloc.webctrls.fineupload;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;

public class HCFineUploaderBasic implements IHCNodeBuilder
{
  private final FineUploaderBasic m_aUploader;

  public HCFineUploaderBasic (@Nonnull final FineUploaderBasic aUploader)
  {
    if (aUploader == null)
      throw new NullPointerException ("uploader");
    m_aUploader = aUploader;
  }

  @Nullable
  public IHCNode build ()
  {
    registerExternalResources ();
    final String sID = GlobalIDFactory.getNewStringID ();
    final HCNodeList ret = new HCNodeList ();
    ret.addChild (new HCDiv ().setID (sID));
    ret.addChild (new HCScriptOnDocumentReady (JQuery.idRef (sID).invoke ("fineUploader").arg (m_aUploader.getJSON ())));
    return ret;
  }

  public static final void registerExternalResources ()
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EFineUploaderJSPathProvider.FINEUPLOADER_311);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EFineUploaderCSSPathProvider.FINEUPLOADER_311);
  }
}
