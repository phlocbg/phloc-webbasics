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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.request.IHCRequestField;
import com.phloc.webscopes.util.SessionBackedRequestFieldData;

public class SessionBackedRequestField extends SessionBackedRequestFieldData implements IHCRequestField
{
  /**
   * Create a new request field that has no default value
   * 
   * @param sFieldName
   *        The field name to use. May neither be <code>null</code> nor empty.
   */
  public SessionBackedRequestField (@Nonnull @Nonempty final String sFieldName)
  {
    super (sFieldName);
  }

  /**
   * Default constructor.
   * 
   * @param sFieldName
   *        The field name to use. May neither be <code>null</code> nor empty.
   * @param sDefaultValue
   *        The default value to use, if no value is present in the request
   *        scope.
   */
  public SessionBackedRequestField (@Nonnull @Nonempty final String sFieldName, @Nullable final String sDefaultValue)
  {
    super (sFieldName, sDefaultValue);
  }

  /**
   * Helper constructor using an int instead of a String.
   * 
   * @param sFieldName
   *        The field name to use. May neither be <code>null</code> nor empty.
   * @param nDefaultValue
   *        The default value to be used. Is converted to a String
   */
  public SessionBackedRequestField (@Nonnull @Nonempty final String sFieldName, final int nDefaultValue)
  {
    super (sFieldName, nDefaultValue);
  }
}
