/**
 * Copyright (C) 2006-2013 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.webbasics.ajax;

import javax.annotation.Nonnull;

import com.phloc.commons.mime.CMimeType;
import com.phloc.html.EHTMLElement;
import com.phloc.html.hc.api.EHCLinkType;
import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.JSBlock;
import com.phloc.html.js.builder.JSConditional;
import com.phloc.html.js.builder.JSForIn;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.JSVar;
import com.phloc.html.js.builder.html.JSHtml;
import com.phloc.html.js.builder.jquery.JQuery;

/**
 * The default JQuery AJAX success to be used, that handles external JS/CSS
 * files and inline JS correctly.
 * 
 * @author Philip Helger
 */
@Deprecated
public class AjaxDefaultResponseJSSuccess extends JSAnonymousFunction
{
  public AjaxDefaultResponseJSSuccess (@Nonnull final JSAnonymousFunction aAnonFunc, final boolean bInvokeHandlerFirst)
  {
    this (aAnonFunc.invoke (), bInvokeHandlerFirst);
  }

  /**
   * Ctor
   * 
   * @param aHandler
   *        The main handler function for the "value" property. The first 3
   *        parameters are: data, textStatus and xhr
   * @param bInvokeHandlerFirst
   *        <code>true</code> if the handler should be invoked before the CSS/JS
   *        are included/evaluated, <code>false</code> if it should occur after
   *        the inclusion.
   */
  public AjaxDefaultResponseJSSuccess (@Nonnull final JSInvocation aHandler, final boolean bInvokeHandlerFirst)
  {
    if (aHandler == null)
      throw new NullPointerException ("handler");
    if (aHandler.hasArgs ())
      throw new IllegalArgumentException ("Handler may not have arguments assigned so far!");

    final JSVar aData = param ("a");
    final JSVar aTextStatus = param ("b");
    final JSVar aXHR = param ("c");
    final JSInvocation aHandlerInvocation = aHandler.arg (aData.ref (AjaxDefaultResponse.PROPERTY_VALUE))
                                                    .arg (aTextStatus)
                                                    .arg (aXHR);

    final JSBlock aBody = body ();

    // Overall success?
    final JSConditional aIf = aBody._if (aData.ref (AjaxDefaultResponse.PROPERTY_SUCCESS));
    final JSBlock aIfSuccess = aIf._then ();
    if (bInvokeHandlerFirst)
    {
      // Invoke the main handler first
      aIfSuccess.add (aHandlerInvocation);
    }
    // add all external JS
    {
      final JSBlock aIfExternalJS = aIfSuccess._if (aData.ref (AjaxDefaultResponse.PROPERTY_EXTERNAL_JS))._then ();
      final JSVar aFirstJS = aIfExternalJS.var ("fj", JSHtml.documentGetElementsByTagName (EHTMLElement.SCRIPT)
                                                            .component0 ());
      final JSVar aJS = new JSVar ("js");
      final JSForIn aJSLoop = new JSForIn (aJS, aData.ref (AjaxDefaultResponse.PROPERTY_EXTERNAL_JS));
      final JSVar aJSNode = aJSLoop.body ().var ("jsNode", JSHtml.documentCreateElement (EHTMLElement.SCRIPT));
      aJSLoop.body ().assign (aJSNode.ref ("type"), CMimeType.TEXT_JAVASCRIPT.getAsString ());
      aJSLoop.body ().assign (aJSNode.ref ("src"), aJS);
      aJSLoop.body ().assign (aJSNode.ref ("title"), "dynamicallyLoadedJS");
      aJSLoop.body ().invoke (aFirstJS.ref ("parentNode"), "insertBefore").arg (aJSNode).arg (aFirstJS);
      aIfExternalJS.add (aJSLoop);
    }
    // add all external CSS
    {
      final JSBlock aIfExternalCSS = aIfSuccess._if (aData.ref (AjaxDefaultResponse.PROPERTY_EXTERNAL_CSS))._then ();
      final JSVar aFirstCSS = aIfExternalCSS.var ("fc", JSHtml.documentGetElementsByTagName (EHTMLElement.LINK)
                                                              .component0 ());
      final JSVar aCSS = new JSVar ("css");
      final JSForIn aCSSLoop = new JSForIn (aCSS, aData.ref (AjaxDefaultResponse.PROPERTY_EXTERNAL_CSS));
      final JSVar aCSSNode = aCSSLoop.body ().var ("cssNode", JSHtml.documentCreateElement (EHTMLElement.LINK));
      aCSSLoop.body ().assign (aCSSNode.ref ("type"), CMimeType.TEXT_CSS.getAsString ());
      aCSSLoop.body ().assign (aCSSNode.ref ("rel"), EHCLinkType.STYLESHEET.getAttrValue ());
      aCSSLoop.body ().assign (aCSSNode.ref ("src"), aCSS);
      aCSSLoop.body ().assign (aCSSNode.ref ("title"), "dynamicallyLoadedCSS");
      aCSSLoop.body ().invoke (aFirstCSS.ref ("parentNode"), "insertBefore").arg (aCSSNode).arg (aFirstCSS);
      aIfExternalCSS.add (aCSSLoop);
    }
    // evaluate the inline script (if any)
    {
      aIfSuccess._if (aData.ref (AjaxDefaultResponse.PROPERTY_INLINE_JS))
                ._then ()
                .addStatement (JQuery.globalEval (aData.ref (AjaxDefaultResponse.PROPERTY_INLINE_JS)));
    }
    if (!bInvokeHandlerFirst)
    {
      // Invoke the main handler last
      aIfSuccess.add (aHandlerInvocation);
    }

    final JSBlock aIfError = aIf._else ();
    final JSVar aErrorMsg = aIfError.var ("msg", "Error invoking AJAX function!");
    final JSConditional aIfErrorMsg = aIfError._if (aData.ref (AjaxDefaultResponse.PROPERTY_ERRORMESSAGE));
    aIfErrorMsg._then ()
               .add (JSHtml.windowAlert (aErrorMsg.plus (" ")
                                                  .plus (aData.ref (AjaxDefaultResponse.PROPERTY_ERRORMESSAGE))));
    aIfErrorMsg._else ().add (JSHtml.windowAlert (aErrorMsg));
  }
}
