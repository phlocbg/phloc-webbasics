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
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.ISuccessIndicator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.hc.utils.AbstractHCSpecialNodes;
import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.JSBlock;
import com.phloc.html.js.builder.JSVar;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.html.resource.css.ICSSPathProvider;
import com.phloc.html.resource.js.IJSPathProvider;
import com.phloc.json.IJSON;
import com.phloc.json.IJSONObject;
import com.phloc.json.impl.JSONObject;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;

@Immutable
public class AjaxDefaultResponse extends AbstractHCSpecialNodes <AjaxDefaultResponse> implements ISuccessIndicator, IAjaxResponse
{
  /** Success property */
  public static final String PROPERTY_SUCCESS = "success";
  /**
   * Response value property - only in case of success - contains the response
   * data as object
   */
  public static final String PROPERTY_VALUE = "value";
  /**
   * Additional CSS files - only in case of success - contains a list of strings
   */
  public static final String PROPERTY_EXTERNAL_CSS = "externalcss";
  /** Additional JS files - only in case of success - contains a list of strings */
  public static final String PROPERTY_EXTERNAL_JS = "externaljs";
  /** Additional inline JS - only in case of success - contains a string */
  public static final String PROPERTY_INLINE_JS = "inlinejs";
  /** Error message property - only in case of error */
  public static final String PROPERTY_ERRORMESSAGE = "errormessage";

  private final boolean m_bSuccess;
  private final String m_sErrorMessage;
  private final IJSONObject m_aSuccessValue;

  protected AjaxDefaultResponse (final boolean bSuccess,
                                 @Nullable final String sErrorMessage,
                                 @Nullable final IJSONObject aSuccessValue)
  {
    m_bSuccess = bSuccess;
    m_sErrorMessage = sErrorMessage;
    m_aSuccessValue = aSuccessValue;
    if (bSuccess)
    {
      // Grab per-request CSS/JS only in success case!
      final boolean bRegularFiles = GlobalDebug.isDebugMode ();
      for (final ICSSPathProvider aCSSPath : PerRequestCSSIncludes.getAllRegisteredCSSIncludesForThisRequest ())
        addExternalCSS (aCSSPath.getCSSItemPath (bRegularFiles));
      for (final IJSPathProvider aJSPath : PerRequestJSIncludes.getAllRegisteredJSIncludesForThisRequest ())
        addExternalJS (aJSPath.getJSItemPath (bRegularFiles));
    }
  }

  public boolean isSuccess ()
  {
    return m_bSuccess;
  }

  public boolean isFailure ()
  {
    return !m_bSuccess;
  }

  /**
   * @return In case this is a failure, this field contains the error message.
   *         May be <code>null</code>.
   */
  @Nullable
  public String getErrorMessage ()
  {
    return m_sErrorMessage;
  }

  /**
   * @return In case this is a success, this field contains the success object.
   *         May be <code>null</code>.
   */
  @Nullable
  public IJSON getSuccessValue ()
  {
    return m_aSuccessValue;
  }

  @Nonnull
  public String getSerializedAsJSON (final boolean bIndentAndAlign)
  {
    final JSONObject aAssocArray = new JSONObject ();
    aAssocArray.setBooleanProperty (PROPERTY_SUCCESS, m_bSuccess);
    if (m_bSuccess)
    {
      if (m_aSuccessValue != null)
        aAssocArray.setObjectProperty (PROPERTY_VALUE, m_aSuccessValue);
      if (hasExternalCSSs ())
        aAssocArray.setStringListProperty (PROPERTY_EXTERNAL_CSS, getAllExternalCSSs ());
      if (hasExternalJSs ())
        aAssocArray.setStringListProperty (PROPERTY_EXTERNAL_JS, getAllExternalJSs ());
      if (hasInlineJS ())
        aAssocArray.setStringProperty (PROPERTY_INLINE_JS, getInlineJS ().getJSCode ());
    }
    else
    {
      aAssocArray.setStringProperty (PROPERTY_ERRORMESSAGE, m_sErrorMessage != null ? m_sErrorMessage : "");
    }
    return aAssocArray.getJSONString (bIndentAndAlign);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final AjaxDefaultResponse rhs = (AjaxDefaultResponse) o;
    return m_bSuccess == rhs.m_bSuccess &&
           EqualsUtils.equals (m_sErrorMessage, rhs.m_sErrorMessage) &&
           EqualsUtils.equals (m_aSuccessValue, rhs.m_aSuccessValue);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (m_bSuccess)
                            .append (m_sErrorMessage)
                            .append (m_aSuccessValue)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("success", m_bSuccess)
                            .append ("errorMsg", m_sErrorMessage)
                            .append ("successValue", m_aSuccessValue)
                            .toString ();
  }

  @Nonnull
  public static AjaxDefaultResponse createSuccess ()
  {
    return createSuccess (null);
  }

  @Nonnull
  public static AjaxDefaultResponse createSuccess (@Nullable final IJSONObject aSuccessValue)
  {
    return new AjaxDefaultResponse (true, null, aSuccessValue);
  }

  @Nonnull
  public static AjaxDefaultResponse createError (@Nullable final String sErrorMessage)
  {
    return new AjaxDefaultResponse (false, sErrorMessage, null);
  }

  @Nonnull
  public static JSAnonymousFunction createDefaultSuccessFunction (@Nonnull final JSAnonymousFunction aHandler)
  {
    if (aHandler == null)
      throw new NullPointerException ("handler");
    if (aHandler.getParamCount () < 3)
      throw new IllegalArgumentException ("Success function must have at least 3 arguments");

    final JSAnonymousFunction ret = new JSAnonymousFunction ();
    final JSVar aData = ret.param ("a");
    final JSVar aStatus = ret.param ("b");
    final JSVar aXHR = ret.param ("c");
    final JSBlock aBody = ret.body ();

    // Overall success?
    final JSBlock aIfSuccess = aBody._if (aData.ref (PROPERTY_SUCCESS))._then ();
    // Invoke the main handler
    aIfSuccess.invoke (aHandler).arg (aData).arg (aStatus).arg (aXHR);
    // evaluate the inline script (if any)
    aIfSuccess._if (aData.ref (PROPERTY_INLINE_JS))
              ._then ()
              .addStatement (JQuery.globalEval (aData.ref (PROPERTY_INLINE_JS)));
    return ret;
  }
}
