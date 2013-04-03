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

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.webbasics.app.LinkUtils;

/**
 * Default AjaxFunction implementation assuming that the Ajax servlet is
 * listening on "/ajax/"
 * 
 * @author philip
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
  @Nonempty
  public String getInvocationURI ()
  {
    return LinkUtils.getURIWithContext (getPathWithoutContext ());
  }

  @Nonnull
  @Nonempty
  public ISimpleURL getInvocationURL ()
  {
    return getInvocationURL (null);
  }

  @Nonnull
  @Nonempty
  public ISimpleURL getInvocationURL (@Nullable final Map <String, String> aParams)
  {
    return LinkUtils.getURLWithContext (getPathWithoutContext (), aParams);
  }
}
