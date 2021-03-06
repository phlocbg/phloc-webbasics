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
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.commons.lang.CGStringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Provides a common implementation of the {@link IAjaxHandler} interface for as
 * easy reuse as possible.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public abstract class AbstractAjaxHandler implements IAjaxHandler
{
  /**
   * The name of the request parameter used by jQuery to indicate "no cache".
   * Use this constant for parameter filtering.
   */
  public static final String REQUEST_PARAM_JQUERY_NO_CACHE = "_";

  public AbstractAjaxHandler ()
  {}

  @Override
  @OverrideOnDemand
  public void registerExternalResources ()
  {
    // empty default implementation
  }

  @OverrideOnDemand
  protected void modifyRequestParamMap (@Nonnull final MapBasedAttributeContainer aParams)
  {
    // Remove the jQuery timestamp parameter
    aParams.removeAttribute (REQUEST_PARAM_JQUERY_NO_CACHE);
  }

  /**
   * This method must be overridden by every handler
   * 
   * @param aRequestScope
   *        The current request scope. Never <code>null</code>.
   * @return the result object. May not be <code>null</code>
   * @throws Exception
   *         if something goes wrong
   */
  @OverrideOnDemand
  @Nonnull
  protected abstract IAjaxResponse mainHandleRequest (@Nonnull IRequestWebScopeWithoutResponse aRequestScope) throws Exception;

  @Override
  @Nonnull
  @SuppressFBWarnings ("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
  public final IAjaxResponse handleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope) throws Exception
  {
    // Main invocation
    final IAjaxResponse aResult = mainHandleRequest (aRequestScope);
    if (aResult == null)
      throw new IllegalStateException ("Invocation of " +
                                       CGStringHelper.getClassLocalName (getClass ()) +
                                       " returned null response!");

    // Return invocation result
    return aResult;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
