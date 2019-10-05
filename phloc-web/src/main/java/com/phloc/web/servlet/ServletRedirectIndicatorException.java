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
package com.phloc.web.servlet;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.SimpleURL;
import com.phloc.webscopes.domain.IRequestWebScope;
import com.phloc.webscopes.mgr.WebScopeManager;

public class ServletRedirectIndicatorException extends RuntimeException
{
  private static final long serialVersionUID = -5799532978827403454L;
  private final SimpleURL m_aURL;
  private final Map <String, Object> m_aRequestParams;

  public ServletRedirectIndicatorException (@Nonnull final ISimpleURL aURL)
  {
    this (aURL, (Map <String, Object>) null);
  }

  public ServletRedirectIndicatorException (@Nonnull final ISimpleURL aURL,
                                            @Nullable final Map <String, Object> aRequestParams)
  {
    this.m_aURL = new SimpleURL (ValueEnforcer.notNull (aURL, "URL"));
    final IRequestWebScope aRequest = WebScopeManager.getRequestScopeOrNull ();
    this.m_aRequestParams = ContainerHelper.newMap ();
    if (aRequest != null)
    {
      final ServletRedirectParameterStrategy aStrat = ServletRedirectParameterStrategy.getInstance ();
      for (final Map.Entry <String, Object> aExisting : aRequest.getAllAttributes ().entrySet ())
      {
        if (aStrat.isCopyOnRedirect (aExisting.getKey ()))
        {
          this.m_aRequestParams.put (aExisting.getKey (), aExisting.getValue ());
          this.m_aURL.add (aExisting.getKey (), String.valueOf (aExisting.getValue ()));
        }
      }
    }
    if (aRequestParams != null)
    {
      this.m_aRequestParams.putAll (aRequestParams);
    }
  }

  @Nonnull
  public ISimpleURL getURL ()
  {
    return this.m_aURL;
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, Object> getRequestParams ()
  {
    return ContainerHelper.newMap (this.m_aRequestParams);
  }
}
