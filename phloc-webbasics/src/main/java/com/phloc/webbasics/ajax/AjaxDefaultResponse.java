/**
 * Copyright (C) 2006-2012 phloc systems
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

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.ISuccessIndicator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.json.IJSON;
import com.phloc.json.IJSONObject;
import com.phloc.json.impl.JSONObject;

@Immutable
public final class AjaxDefaultResponse implements ISuccessIndicator
{
  public static final String PROPERTY_SUCCESS = "success";
  public static final String PROPERTY_ERRORMESSAGE = "errormessage";
  public static final String PROPERTY_VALUE = "value";

  private final boolean m_bSuccess;
  private final String m_sErrorMessage;
  private final IJSONObject m_aSuccessValue;

  private AjaxDefaultResponse (final boolean bSuccess,
                               @Nullable final String sErrorMessage,
                               @Nullable final IJSONObject aSuccessValue)
  {
    m_bSuccess = bSuccess;
    m_sErrorMessage = sErrorMessage;
    m_aSuccessValue = aSuccessValue;
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

  @Nonnull
  public String getSerializedAsJSON (final boolean bIndentAndAlign)
  {
    final JSONObject aAssocArray = new JSONObject ();
    aAssocArray.setBooleanProperty (PROPERTY_SUCCESS, m_bSuccess);
    if (m_bSuccess)
      aAssocArray.setObjectProperty (PROPERTY_VALUE, m_aSuccessValue);
    else
      aAssocArray.setStringProperty (PROPERTY_ERRORMESSAGE, m_sErrorMessage);
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
           EqualsUtils.equals (m_aSuccessValue, rhs.m_aSuccessValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_bSuccess)
                                       .append (m_sErrorMessage)
                                       .append (m_aSuccessValue)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("success", m_bSuccess)
                                       .append ("errorMsg", m_sErrorMessage)
                                       .append ("successValue", m_aSuccessValue)
                                       .toString ();
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
}
