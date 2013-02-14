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

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.json.IJSONObject;

@Immutable
public final class AjaxSimpleResponse implements IAjaxResponse
{
  private final IJSONObject m_aValue;

  public AjaxSimpleResponse (@Nullable final IJSONObject aValue)
  {
    m_aValue = aValue;
  }

  @Nonnull
  public String getSerializedAsJSON (final boolean bIndentAndAlign)
  {
    return m_aValue == null ? "" : m_aValue.getJSONString (bIndentAndAlign);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof AjaxSimpleResponse))
      return false;
    final AjaxSimpleResponse rhs = (AjaxSimpleResponse) o;
    return EqualsUtils.equals (m_aValue, rhs.m_aValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("value", m_aValue).toString ();
  }
}
