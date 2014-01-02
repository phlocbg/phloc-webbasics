/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.webctrls.js;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.js.builder.IJSExpression;
import com.phloc.html.js.builder.JSArray;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSExpr;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.JSRef;
import com.phloc.webbasics.ajax.IAjaxFunction;

/**
 * Java JavaScript wrapper for form.js
 * 
 * @author Philip Helger
 */
@Immutable
public final class JSFormHelper
{
  private JSFormHelper ()
  {}

  @Nonnull
  public static JSRef getFormHelper ()
  {
    return JSExpr.ref ("FormHelper");
  }

  @Nonnull
  public static JSInvocation getAllFormValues (@Nonnull @Nonempty final String sFormID,
                                               @Nonnull @Nonempty final String sFieldPrefix)
  {
    return getFormHelper ().invoke ("getAllFormValues").arg (sFormID).arg (sFieldPrefix);
  }

  @Nonnull
  public static JSInvocation setAllFormValues (@Nonnull @Nonempty final String sFormID,
                                               @Nonnull final JSAssocArray aValues)
  {
    return getFormHelper ().invoke ("setAllFormValues").arg (sFormID).arg (aValues);
  }

  @Nonnull
  public static JSInvocation updateElementDirect (@Nonnull @Nonempty final String sFieldID,
                                                  @Nonnull final IHCNode aHCNode)
  {
    return updateElementDirect (sFieldID, HCSettings.getAsHTMLStringWithoutNamespaces (aHCNode));
  }

  @Nonnull
  public static JSInvocation updateElementDirect (@Nonnull @Nonempty final String sFieldID,
                                                  @Nonnull final String sHTMLCode)
  {
    return getFormHelper ().invoke ("updateElementDirect").arg (sFieldID).arg (sHTMLCode);
  }

  @Nonnull
  public static JSInvocation updateElementViaAjax (@Nonnull @Nonempty final String sFieldID,
                                                   @Nonnull final IAjaxFunction aUpdateCallURL)
  {
    return updateElementViaAjax (sFieldID, aUpdateCallURL.getInvocationURI ());
  }

  @Nonnull
  public static JSInvocation updateElementViaAjax (@Nonnull @Nonempty final String sFieldID,
                                                   @Nonnull final ISimpleURL aUpdateCallURL)
  {
    return updateElementViaAjax (sFieldID, aUpdateCallURL.getAsString ());
  }

  @Nonnull
  public static JSInvocation updateElementViaAjax (@Nonnull @Nonempty final String sFieldID,
                                                   @Nonnull final String sUpdateCallURI)
  {
    return getFormHelper ().invoke ("updateElementViaAjax").arg (sFieldID).arg (sUpdateCallURI);
  }

  // missing updateElements

  @Nonnull
  public static JSAssocArray createUpdateParam (@Nonnull @Nonempty final String sFieldID, @Nonnull final IHCNode aHCNode)
  {
    return new JSAssocArray ().add ("id", sFieldID).add ("html", HCSettings.getAsHTMLStringWithoutNamespaces (aHCNode));
  }

  @Nonnull
  public static JSAssocArray createUpdateParam (@Nonnull @Nonempty final String sFieldID,
                                                @Nonnull final IAjaxFunction aUpdateCallURL)
  {
    return new JSAssocArray ().add ("id", sFieldID).add ("url", aUpdateCallURL.getInvocationURI ());
  }

  @Nonnull
  public static JSInvocation saveFormData (@Nonnull @Nonempty final String sFormID,
                                           @Nonnull @Nonempty final String sFieldPrefix,
                                           @Nonnull @Nonempty final String sPageID,
                                           @Nonnull final IAjaxFunction aSaveCallURL,
                                           @Nonnull final JSArray aSuccessUpdates,
                                           @Nonnull final JSArray aErrorUpdates)
  {
    return getFormHelper ().invoke ("saveFormData")
                           .arg (sFormID)
                           .arg (sFieldPrefix)
                           .arg (sPageID)
                           .arg (aSaveCallURL.getInvocationURI ())
                           .arg (aSuccessUpdates)
                           .arg (aErrorUpdates);
  }

  /**
   * Set all options of a &lt;select&gt;
   * 
   * @param aSelector
   *        jQuery object
   * @param aValueList
   *        list of array[value,text] - nested array!
   * @return the invocation
   */
  @Nonnull
  public static JSInvocation setSelectOptions (@Nonnull final IJSExpression aSelector, @Nonnull final JSArray aValueList)
  {
    return getFormHelper ().invoke ("setSelectOptions").arg (aSelector).arg (aValueList);
  }
}
