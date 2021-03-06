/**
 * Copyright (C) 2006-2015 phloc systems
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

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.ETriState;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.StringParser;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.html.HCCheckBox;
import com.phloc.html.request.IHCRequestFieldBoolean;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Special request field specially for check boxes with a fixed value.
 *
 * @author Philip Helger
 */
public class RequestFieldBoolean extends RequestField implements IHCRequestFieldBoolean
{
  private final boolean m_bDefaultValue;

  public RequestFieldBoolean (@Nonnull final RequestField aRF)
  {
    this (aRF.getFieldName (), aRF.getRequestValue (), CHCParam.VALUE_CHECKED.equals (aRF.getDefaultValue ()));
  }

  public RequestFieldBoolean (@Nonnull @Nonempty final String sFieldName, final boolean bDefaultValue)
  {
    this (sFieldName, getStringValue (bDefaultValue), bDefaultValue);
  }

  public RequestFieldBoolean (@Nonnull @Nonempty final String sFieldName,
                              @Nullable final String sFieldValue,
                              final boolean bDefaultValue)
  {
    super (sFieldName, sFieldValue);

    // The default value is immutable
    m_bDefaultValue = bDefaultValue;
  }

  /**
   * @param bValue
   *        the boolean value
   * @return The string parameter value to be used for the passed parameter.
   *         Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public static String getStringValue (final boolean bValue)
  {
    return bValue ? CHCParam.VALUE_CHECKED : CHCParam.VALUE_UNCHECKED;
  }

  /**
   * @return <code>true</code> if the check-box/radio button is checked or if no
   *         such request parameter is present and the fall-back is
   *         <code>true</code>, <code>false</code> otherwise.
   */
  public boolean isChecked ()
  {
    return isChecked (null);
  }

  /**
   * @return <code>true</code> if the check-box/radio button is checked or if no
   *         such request parameter is present and the fall-back is
   *         <code>true</code>, <code>false</code> otherwise.
   */
  public boolean isChecked (@Nullable final String sFieldValue)
  {
    return getCheckBoxValue (getFieldName (), sFieldValue, m_bDefaultValue);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final RequestFieldBoolean rhs = (RequestFieldBoolean) o;
    return m_bDefaultValue == rhs.m_bDefaultValue;
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_bDefaultValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("defaultValue", m_bDefaultValue).toString ();
  }

  @Nullable
  public static String getCheckBoxRequestValue (@Nonnull @Nonempty final String sFieldName,
                                                @Nullable final String sDefaultValue)
  {
    return getCheckBoxRequestValue (sFieldName, null, sDefaultValue);
  }

  @Nullable
  public static String getCheckBoxRequestValue (@Nonnull @Nonempty final String sFieldName,
                                                @Nullable final String sFieldValue,
                                                @Nullable final String sDefaultValue)
  {
    if (StringHelper.hasNoText (sFieldName))
      throw new IllegalArgumentException ("fieldName may not be empty!");

    final IRequestWebScopeWithoutResponse aScope = getScope ();
    if (StringHelper.hasText (sFieldValue))
    {
      final List <String> aValues = aScope.getAttributeValues (sFieldName);
      if (aValues == null)
        return sDefaultValue;
      return String.valueOf (aValues.contains (sFieldValue));
    }

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

  @Nonnull
  public static ETriState getCheckBoxState (@Nonnull @Nonempty final String sFieldName)
  {
    return getCheckBoxState (sFieldName, null);
  }

  @Nonnull
  public static ETriState getCheckBoxState (@Nonnull @Nonempty final String sFieldName,
                                            @Nullable final String sFieldValue)
  {
    final String sValue = getCheckBoxRequestValue (sFieldName, sFieldValue, null);
    return sValue == null ? ETriState.UNDEFINED : ETriState.valueOf (StringParser.parseBoolObj (sValue));
  }

  public static boolean getCheckBoxValue (@Nonnull @Nonempty final String sFieldName, final boolean bDefaultValue)
  {
    return getCheckBoxValue (sFieldName, null, bDefaultValue);
  }

  public static boolean getCheckBoxValue (@Nonnull @Nonempty final String sFieldName,
                                          @Nullable final String sFieldValue,
                                          final boolean bDefaultValue)
  {
    if (StringHelper.hasNoText (sFieldName))
      throw new IllegalArgumentException ("fieldName may not be empty!");

    return getCheckBoxState (sFieldName, sFieldValue).getAsBooleanValue (bDefaultValue);
  }
}
