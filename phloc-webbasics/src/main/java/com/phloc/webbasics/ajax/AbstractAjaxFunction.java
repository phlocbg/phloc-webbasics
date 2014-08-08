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
package com.phloc.webbasics.ajax;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Abstract base implementation of {@link IAjaxFunction}
 *
 * @author Philip Helger
 */
@Immutable
public abstract class AbstractAjaxFunction implements IAjaxFunction
{
  private final String m_sFunctionName;

  public AbstractAjaxFunction (@Nonnull @Nonempty final String sFunctionName)
  {
    if (!AjaxInvoker.isValidFunctionName (sFunctionName))
      throw new IllegalArgumentException ("functionName");
    m_sFunctionName = sFunctionName;
  }

  @Nonnull
  @Nonempty
  public final String getName ()
  {
    return m_sFunctionName;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("functionName", m_sFunctionName).toString ();
  }
}
