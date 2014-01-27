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
package com.phloc.webbasics.action.servlet;

import javax.annotation.Nonnull;

import com.phloc.webbasics.action.GlobalActionManager;
import com.phloc.webbasics.action.IActionInvoker;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Action handling servlet using {@link GlobalActionManager}.
 * 
 * @author Philip Helger
 */
public class GlobalActionServlet extends AbstractActionServlet
{
  @Override
  @Nonnull
  protected final IActionInvoker getActionInvoker (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    return GlobalActionManager.getInstance ();
  }
}
