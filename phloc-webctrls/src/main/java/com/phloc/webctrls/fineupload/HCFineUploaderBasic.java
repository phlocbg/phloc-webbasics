package com.phloc.webctrls.fineupload;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.EHTMLElement;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.JSPackage;
import com.phloc.html.js.builder.JSVar;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.html.js.builder.jquery.JQuerySelector;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;

public class HCFineUploaderBasic implements IHCNodeBuilder
{
  private final FineUploaderBasic m_aUploader;
  private IHCElement <?> m_aButton;

  public HCFineUploaderBasic (@Nonnull final FineUploaderBasic aUploader)
  {
    if (aUploader == null)
      throw new NullPointerException ("uploader");
    m_aUploader = aUploader;
  }

  @Nonnull
  public HCFineUploaderBasic setButtonToUse (@Nullable final IHCElement <?> aButton)
  {
    m_aButton = aButton;
    return this;
  }

  @Nullable
  public IHCNode build ()
  {
    registerExternalResources ();
    final String sID = GlobalIDFactory.getNewStringID ();
    final HCNodeList ret = new HCNodeList ();
    ret.addChild (new HCDiv ().setID (sID));
    if (m_aButton != null)
    {
      // We have a special button to use
      ret.addChild (m_aButton);
      String sButtonID = m_aButton.getID ();
      if (StringHelper.hasNoText (sButtonID))
      {
        sButtonID = GlobalIDFactory.getNewStringID ();
        m_aButton.setID (sButtonID);
      }
      m_aUploader.setButtonElementID (sButtonID);
    }

    // Start building JS
    final JSPackage aPkg = new JSPackage ();
    final JSVar aCnt = aPkg.var ("nCnt" + sID, 0);

    // On submit, inc counter
    final JSAnonymousFunction aOnSubmit = new JSAnonymousFunction ();
    aOnSubmit.body ().incr (aCnt);
    // On cancel, dec counter
    final JSAnonymousFunction aOnCancel = new JSAnonymousFunction ();
    aOnCancel.body ().decr (aCnt);

    aPkg.add (JQuery.idRef (sID)
                    .invoke ("fineUploader")
                    .arg (m_aUploader.getJSON ())
                    .invoke ("on")
                    .arg ("submit")
                    .arg (aOnSubmit)
                    .invoke ("on")
                    .arg ("cancel")
                    .arg (aOnCancel));

    if (!m_aUploader.isAutoUpload ())
    {
      // Manually trigger upload when form is submitted

      // Get closest form to the input ID
      final JSVar aForm = aPkg.var ("f" + sID, JQuery.idRef (sID).closest ().arg (EHTMLElement.FORM.getElementName ()));

      final JSAnonymousFunction aOnClick = new JSAnonymousFunction ();

      // If no file was uploaded, process file normally
      aOnClick.body ()._if (aCnt.eq (0))._then ()._return (true);

      final JSVar aUpload = aOnClick.body ().var ("u" + sID, JQuery.idRef (sID));
      {
        // assign an "onComplete" handler to the fileuploader, that submits the
        // form, as soon, as all uploaded files are handled
        final JSAnonymousFunction aOnCompete = new JSAnonymousFunction ();
        final JSVar aInProgress = aOnCompete.body ().var ("nInProgress",
                                                          aUpload.invoke ("fineUploader").arg ("getInProgress"));
        aOnCompete.body ()._if (aInProgress.eq (0))._then ().add (aForm.invoke ("submit"));
        aOnClick.body ().add (aUpload.invoke ("on").arg ("complete").arg (aOnCompete));
      }
      // Start the uploading manually
      aOnClick.body ().invoke (aUpload, "fineUploader").arg ("uploadStoredFiles");
      aOnClick.body ()._return (false);

      // Find the first ":submit" element of the closest form of the passed
      // element and set the "click" handler
      aPkg.addStatement (JQuery.select (JQuerySelector.submit).arg (aForm).click ().arg (aOnClick));
    }
    ret.addChild (new HCScriptOnDocumentReady (aPkg));
    return ret;
  }

  public static final void registerExternalResources ()
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EFineUploaderJSPathProvider.FINEUPLOADER_311);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EFineUploaderCSSPathProvider.FINEUPLOADER_311);
  }
}
