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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.ISuccessIndicator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.resource.css.ICSSPathProvider;
import com.phloc.html.resource.js.IJSPathProvider;
import com.phloc.json.IJSON;
import com.phloc.json.IJSONObject;
import com.phloc.json.impl.JSONObject;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;

@Immutable
public final class AjaxDefaultResponse implements ISuccessIndicator, IAjaxResponse
{
  /** Success property */
  public static final String PROPERTY_SUCCESS = "success";
  /** Error message property */
  public static final String PROPERTY_ERRORMESSAGE = "errormessage";
  /** Response value property */
  public static final String PROPERTY_VALUE = "value";
  /** Additional CSS files */
  public static final String PROPERTY_EXTERNAL_CSS = "externalcss";
  /** Additional JS files */
  public static final String PROPERTY_EXTERNAL_JS = "externaljs";
  /** Additional inline JS */
  public static final String PROPERTY_INLINE_JS = "inlinejs";

  private final boolean m_bSuccess;
  private final String m_sErrorMessage;
  private final IJSONObject m_aSuccessValue;
  private final List <String> m_aExternalCSSs = new ArrayList <String> ();
  private final List <String> m_aExternalJSs = new ArrayList <String> ();

  private AjaxDefaultResponse (final boolean bSuccess,
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
        m_aExternalCSSs.add (aCSSPath.getCSSItemPath (bRegularFiles));
      for (final IJSPathProvider aJSPath : PerRequestJSIncludes.getAllRegisteredJSIncludesForThisRequest ())
        m_aExternalJSs.add (aJSPath.getJSItemPath (bRegularFiles));
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
   */
  @Nullable
  public String getErrorMessage ()
  {
    return m_sErrorMessage;
  }

  /**
   * @return In case this is a success, this field contains the success object
   */
  @Nullable
  public IJSON getSuccessValue ()
  {
    return m_aSuccessValue;
  }

  /**
   * @return In case this is a success, this list contains all CSS files that
   *         were requested by elements created inside the handler. Never
   *         <code>null</code> but maybe empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllExternalCSSs ()
  {
    return ContainerHelper.newList (m_aExternalCSSs);
  }

  /**
   * @return In case this is a success, this list contains all JS files that
   *         were requested by elements created inside the handler. Never
   *         <code>null</code> but maybe empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllExternalJSs ()
  {
    return ContainerHelper.newList (m_aExternalJSs);
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
      aAssocArray.setStringListProperty (PROPERTY_EXTERNAL_CSS, m_aExternalCSSs);
      aAssocArray.setStringListProperty (PROPERTY_EXTERNAL_JS, m_aExternalJSs);
    }
    else
    {
      if (m_sErrorMessage != null)
        aAssocArray.setStringProperty (PROPERTY_ERRORMESSAGE, m_sErrorMessage);
    }
    return aAssocArray.getJSONString (bIndentAndAlign);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof AjaxDefaultResponse))
      return false;
    final AjaxDefaultResponse rhs = (AjaxDefaultResponse) o;
    return m_bSuccess == rhs.m_bSuccess &&
           EqualsUtils.equals (m_sErrorMessage, rhs.m_sErrorMessage) &&
           EqualsUtils.equals (m_aSuccessValue, rhs.m_aSuccessValue) &&
           m_aExternalCSSs.equals (rhs.m_aExternalCSSs) &&
           m_aExternalJSs.equals (rhs.m_aExternalJSs);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_bSuccess)
                                       .append (m_sErrorMessage)
                                       .append (m_aSuccessValue)
                                       .append (m_aExternalCSSs)
                                       .append (m_aExternalJSs)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("success", m_bSuccess)
                                       .append ("errorMsg", m_sErrorMessage)
                                       .append ("successValue", m_aSuccessValue)
                                       .append ("externalCSSs", m_aExternalCSSs)
                                       .append ("externalJSs", m_aExternalJSs)
                                       .toString ();
  }

  @Nonnull
  public static IAjaxResponse createSuccess ()
  {
    return createSuccess (null);
  }

  @Nonnull
  public static IAjaxResponse createSuccess (@Nullable final IJSONObject aSuccessValue)
  {
    return new AjaxDefaultResponse (true, null, aSuccessValue);
  }

  @Nonnull
  public static IAjaxResponse createError (@Nullable final String sErrorMessage)
  {
    return new AjaxDefaultResponse (false, sErrorMessage, null);
  }
}
