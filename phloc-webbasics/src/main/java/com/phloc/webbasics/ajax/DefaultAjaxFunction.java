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

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Default AjaxFunction implementation assuming that the Ajax servlet is
 * listening on "/ajax/"
 *
 * @author Philip Helger
 */
@Immutable
public class DefaultAjaxFunction extends AbstractAjaxFunction
{
  public DefaultAjaxFunction (@Nonnull @Nonempty final String sFunctionName)
  {
    super (sFunctionName);
  }

  /**
   * @return The path to the AJAX servlet. Must start with a slash and end with
   *         a slash!
   */
  @Nonnull
  @Nonempty
  @OverrideOnDemand
  protected String getAjaxServletPath ()
  {
    return "/ajax/";
  }

  @Nonnull
  @Nonempty
  public final String getPathWithoutContext ()
  {
    return getAjaxServletPath () + getName ();
  }

  @Nonnull
  public String getInvocationURI (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    return LinkUtils.getURIWithContext (aRequestScope, getPathWithoutContext ());
  }

  @Nonnull
  @Nonempty
  public String getInvocationURI (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                  @Nullable final Map <String, String> aParams)
  {
    if (ContainerHelper.isEmpty (aParams))
    {
      // No need to convert to SimpleURL and back
      return getInvocationURI (aRequestScope);
    }

    return getInvocationURL (aRequestScope, aParams).getAsString ();
  }

  @Nonnull
  public ISimpleURL getInvocationURL (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    return getInvocationURL (aRequestScope, (Map <String, String>) null);
  }

  @Nonnull
  public ISimpleURL getInvocationURL (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                      @Nullable final Map <String, String> aParams)
  {
    return LinkUtils.getURLWithContext (aRequestScope, getPathWithoutContext (), aParams);
  }
}
