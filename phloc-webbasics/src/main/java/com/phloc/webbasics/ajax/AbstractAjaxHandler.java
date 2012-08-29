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
package com.phloc.webbasics.ajax;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.lang.CGStringHelper;
import com.phloc.scopes.nonweb.domain.IRequestScope;

/**
 * Provides a common implementation of the {@link IAjaxHandler} interface for as
 * easy reuse as possible.
 * 
 * @author philip
 */
@NotThreadSafe
public abstract class AbstractAjaxHandler implements IAjaxHandler
{
  public AbstractAjaxHandler ()
  {}

  /**
   * This method must be overridden by every handler
   * 
   * @param aRequestScope
   * @return the result object. May not be <code>null</code>
   * @throws Exception
   */
  @OverrideOnDemand
  @Nonnull
  protected abstract AjaxDefaultResponse mainHandleRequest (@Nonnull final IRequestScope aRequestScope) throws Exception;

  @Nonnull
  public final AjaxDefaultResponse handleRequest (@Nonnull final IRequestScope aRequestScope) throws Exception
  {
    // Main invocation
    final AjaxDefaultResponse aResult = mainHandleRequest (aRequestScope);
    if (aResult == null)
      throw new IllegalStateException ("Invocation of " +
                                       CGStringHelper.getClassLocalName (getClass ()) +
                                       " returned null response!");

    // Return invocation result
    return aResult;
  }

  @OverrideOnDemand
  public void registerExternalResources ()
  {
    // empty default implementation
  }
}
