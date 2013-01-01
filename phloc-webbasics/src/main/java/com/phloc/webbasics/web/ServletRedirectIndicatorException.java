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
package com.phloc.webbasics.web;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.url.ISimpleURL;

public final class ServletRedirectIndicatorException extends RuntimeException
{
  private final ISimpleURL m_aURL;
  private final Map <String, Object> m_aRequestParams;

  public ServletRedirectIndicatorException (@Nonnull final ISimpleURL aURL)
  {
    this (aURL, null);
  }

  public ServletRedirectIndicatorException (@Nonnull final ISimpleURL aURL,
                                            @Nullable final Map <String, Object> aRequestParams)
  {
    if (aURL == null)
      throw new NullPointerException ("URL");
    m_aURL = aURL;
    m_aRequestParams = aRequestParams;
  }

  @Nonnull
  public ISimpleURL getURL ()
  {
    return m_aURL;
  }

  @Nullable
  public Map <String, Object> getRequestParams ()
  {
    return m_aRequestParams;
  }
}
