package com.phloc.webctrls.page;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.js.builder.JSArray;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSExpr;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.JSRef;
import com.phloc.webbasics.ajax.IAjaxFunction;

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
    return updateElementDirect (sFieldID, HCSettings.getAsHTMLString (aHCNode, GlobalDebug.isDebugMode ()));
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

  @Nonnull
  public static JSAssocArray createUpdateParam (@Nonnull @Nonempty final String sFieldID, @Nonnull final IHCNode aHCNode)
  {
    return new JSAssocArray ().add ("id", sFieldID).add ("html",
                                                         HCSettings.getAsHTMLString (aHCNode,
                                                                                     GlobalDebug.isDebugMode ()));
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
}
