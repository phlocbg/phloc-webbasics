package com.phloc.webctrls.autosize;

import javax.annotation.Nonnull;

import com.phloc.html.EHTMLElement;
import com.phloc.html.EHTMLVersion;
import com.phloc.html.hc.IHCBaseNode;
import com.phloc.html.hc.IHCNodeWithChildren;
import com.phloc.html.hc.customize.HCEmptyCustomizer;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.hc.html.HCTextArea;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;

public class AutosizeCustomizer extends HCEmptyCustomizer
{
  @Override
  public void customizeNode (@Nonnull final IHCNodeWithChildren <?> aParentElement,
                             @Nonnull final IHCBaseNode aNode,
                             @Nonnull final EHTMLVersion eHTMLVersion)
  {
    if (aNode instanceof HCTextArea)
    {
      aParentElement.addChild (new HCScriptOnDocumentReady (JQuery.elementNameRef (EHTMLElement.TEXTAREA)
                                                                  .invoke ("autosize")));
      PerRequestJSIncludes.registerJSIncludeForThisRequest (EAutosizeJSPathProvider.AUTOSIZE_115);
    }
  }
}
