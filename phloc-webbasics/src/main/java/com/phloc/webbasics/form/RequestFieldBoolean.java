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
package com.phloc.webbasics.form;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringParser;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.IHCRequestFieldBoolean;
import com.phloc.html.hc.html.HCCheckBox;
import com.phloc.scopes.web.domain.IRequestWebScope;

/**
 * Special request field specially for check boxes with a fixed value.
 * 
 * @author philip
 */
public final class RequestFieldBoolean extends RequestField implements IHCRequestFieldBoolean
{
  private final boolean m_bDefaultValue;

  public RequestFieldBoolean (@Nonnull final RequestField aRF)
  {
    this (aRF.getFieldName (), CHCParam.VALUE_CHECKED.equals (aRF.getDefaultValue ()));
  }

  public RequestFieldBoolean (@Nonnull @Nonempty final String sFieldName, final boolean bDefaultValue)
  {
    super (sFieldName, getStringValue (bDefaultValue));

    // Save default value to parsing from string once we don't need it anymore
    // The default value is immutable
    m_bDefaultValue = bDefaultValue;
  }

  /**
   * @param bValue
   *        the boolean value
   * @return The string parameter value to be used for the passed parameter
   */
  @Nonnull
  public static String getStringValue (final boolean bValue)
  {
    return bValue ? CHCParam.VALUE_CHECKED : CHCParam.VALUE_UNCHECKED;
  }

  /**
   * @return <code>true</code> if the checkbox is checked or if no such request
   *         parameter is present and the fallback is <code>true</code>,
   *         <code>false</code> otherwise.
   */
  public boolean isChecked ()
  {
    return getCheckBoxValue (getFieldName (), m_bDefaultValue);
  }

  @Nullable
  public static String getCheckBoxRequestValue (@Nonnull @Nonempty final String sFieldName,
                                                @Nullable final String sDefaultValue)
  {
    final IRequestWebScope aScope = getScope ();

    // Is the checked value present?
    final String sRequestValue = aScope.getAttributeAsString (sFieldName);
    if (sRequestValue != null)
      return sRequestValue;

    // Check if the hidden parameter for "checkbox is contained in the request"
    // is present?
    if (aScope.containsAttribute (HCCheckBox.getHiddenFieldName (sFieldName)))
      return CHCParam.VALUE_UNCHECKED;

    // Neither nor - default!
    return sDefaultValue;
  }

  public static boolean getCheckBoxValue (@Nonnull @Nonempty final String sFieldName, final boolean bDefaultValue)
  {
    final String sValue = getCheckBoxRequestValue (sFieldName, null);
    return sValue == null ? bDefaultValue : StringParser.parseBool (sValue);
  }

  @Override
  public boolean equals (final Object o)
  {
    return super.equals (o);
  }

  @Override
  public int hashCode ()
  {
    return super.hashCode ();
  }
}
