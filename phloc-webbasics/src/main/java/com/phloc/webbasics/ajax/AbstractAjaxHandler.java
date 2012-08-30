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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.lang.CGStringHelper;
import com.phloc.scopes.web.domain.IRequestWebScope;

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

  @OverrideOnDemand
  public void registerExternalResources ()
  {
    // empty default implementation
  }

  @OverrideOnDemand
  protected void modifyRequestParamMap (@Nonnull final Map <String, Object> aParams)
  {
    // Remove the jQuery timestamp parameter
    aParams.remove (AjaxManager.REQUEST_PARAM_JQUERY_NO_CACHE);
  }

  /**
   * This method must be overridden by every handler
   * 
   * @param aParams
   *        A mutable copy of the extracted request parameters. The values are
   *        either of type String, String[] or IFileItem.
   * @return the result object. May not be <code>null</code>
   * @throws Exception
   */
  @OverrideOnDemand
  @Nonnull
  protected abstract AjaxDefaultResponse mainHandleRequest (@Nonnull Map <String, Object> aParams) throws Exception;

  @Nonnull
  public final AjaxDefaultResponse handleRequest (@Nonnull final IRequestWebScope aRequestScope) throws Exception
  {
    // Get all request parameter values to use from the request scope, as the
    // request scope already differentiated between String, String[] and
    // IFileItem!
    final Map <String, Object> aParams = new HashMap <String, Object> ();
    for (final Object aKey : aRequestScope.getRequest ().getParameterMap ().keySet ())
    {
      final String sKey = (String) aKey;
      aParams.put (sKey, aRequestScope.getAttributeObject (sKey));
    }
    modifyRequestParamMap (aParams);

    // Main invocation
    final AjaxDefaultResponse aResult = mainHandleRequest (aParams);
    if (aResult == null)
      throw new IllegalStateException ("Invocation of " +
                                       CGStringHelper.getClassLocalName (getClass ()) +
                                       " returned null response!");

    // Return invocation result
    return aResult;
  }
}
