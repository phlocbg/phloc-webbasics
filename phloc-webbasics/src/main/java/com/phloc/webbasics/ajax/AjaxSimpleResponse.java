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
package com.phloc.webbasics.ajax;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.json.IJSON;

/**
 * A simple AJAX response, based on an {@link IJson} value.
 *
 * @author Philip Helger
 */
@Immutable
public class AjaxSimpleResponse implements IAjaxResponse
{
  private final boolean m_bSuccess;
  private final IJSON m_aValue;

  public AjaxSimpleResponse (final boolean bSuccess, @Nullable final IJSON aValue)
  {
    this.m_bSuccess = bSuccess;
    this.m_aValue = aValue;
  }

  @Override
  public boolean isSuccess ()
  {
    return this.m_bSuccess;
  }

  @Override
  public boolean isFailure ()
  {
    return !this.m_bSuccess;
  }

  @Nullable
  public IJSON getJson ()
  {
    return this.m_aValue;
  }

  @Override
  @Nonnull
  public String getSerializedAsJSON (final boolean bIndentAndAlign)
  {
    return this.m_aValue == null ? "" : this.m_aValue.getJSONString ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof AjaxSimpleResponse))
      return false;
    final AjaxSimpleResponse rhs = (AjaxSimpleResponse) o;
    return EqualsUtils.equals (this.m_aValue, rhs.m_aValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (this.m_aValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("value", this.m_aValue).toString ();
  }
}
