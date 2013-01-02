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
package com.phloc.webbasics.action;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.state.EContinue;
import com.phloc.commons.string.StringHelper;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webbasics.servlet.AbstractUnifiedResponseServlet;
import com.phloc.webbasics.web.RequestHelper;
import com.phloc.webbasics.web.UnifiedResponse;

/**
 * Abstract action handling servlet
 * 
 * @author philip
 */
public class GlobalActionServlet extends AbstractUnifiedResponseServlet
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (GlobalActionServlet.class);
  private static final String SCOPE_ATTR_ACTION_NAME = "$defaultactionservlet.actionname";
  private static final String SCOPE_ATTR_EXECUTOR = "$defaultactionservlet.executor";

  /**
   * Get the action invoker matching the passed request
   * 
   * @param aRequestScope
   *        The request scope to use. May not be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  protected IActionInvoker getActionInvoker (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    return GlobalActionManager.getInstance ();
  }

  @Override
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected EContinue initRequestState (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                        @Nonnull final UnifiedResponse aUnifiedResponse)
  {
    // cut the leading "/"
    String sAction = RequestHelper.getPathWithinServlet (aRequestScope.getRequest ());
    if (StringHelper.startsWith (sAction, '/'))
      sAction = sAction.substring (1);

    final IActionExecutor aActionExecutor = getActionInvoker (aRequestScope).getActionExecutor (sAction);
    if (aActionExecutor == null)
    {
      s_aLogger.warn ("Unknown action '" + sAction + "' provided!");

      // No such action
      aUnifiedResponse.setStatus (HttpServletResponse.SC_NOT_FOUND);
      return EContinue.BREAK;
    }

    // Call the initialization of the action executor
    aActionExecutor.initExecution (aRequestScope);

    // Remember in scope
    aRequestScope.setAttribute (SCOPE_ATTR_ACTION_NAME, sAction);
    aRequestScope.setAttribute (SCOPE_ATTR_EXECUTOR, aActionExecutor);
    return EContinue.CONTINUE;
  }

  @Override
  protected void handleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                @Nonnull final UnifiedResponse aUnifiedResponse) throws Exception
  {
    // Action is present
    final String sAction = aRequestScope.getAttributeAsString (SCOPE_ATTR_ACTION_NAME);

    // Handle the main action
    if (getActionInvoker (aRequestScope).executeAction (sAction, aRequestScope, aUnifiedResponse).isFailure ())
    {
      // Error in execution
      aUnifiedResponse.setStatus (HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}
