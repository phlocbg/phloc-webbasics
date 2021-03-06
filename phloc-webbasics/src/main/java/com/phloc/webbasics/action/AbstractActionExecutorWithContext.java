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
package com.phloc.webbasics.action;

import javax.annotation.Nonnull;

import com.phloc.web.servlet.response.UnifiedResponse;
import com.phloc.webbasics.app.layout.ILayoutExecutionContext;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

public abstract class AbstractActionExecutorWithContext <LECTYPE extends ILayoutExecutionContext> extends AbstractActionExecutor
{
  /**
   * Create the layout execution context
   * 
   * @param aRequestScope
   *        The request scope to use. Never <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  protected abstract LECTYPE createLayoutExecutionContext (@Nonnull IRequestWebScopeWithoutResponse aRequestScope);

  /**
   * This method must be overridden by every handler
   * 
   * @param aLEC
   *        The lyout execution context. Never <code>null</code>.
   * @param aUnifiedResponse
   *        The response to write to. Never <code>null</code>.
   * @throws Throwable
   *         in case of an error
   */
  @Nonnull
  protected abstract void mainExecute (@Nonnull LECTYPE aLEC, @Nonnull UnifiedResponse aUnifiedResponse) throws Throwable;

  public final void execute (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                             @Nonnull final UnifiedResponse aUnifiedResponse) throws Throwable
  {
    final LECTYPE aLEC = createLayoutExecutionContext (aRequestScope);
    if (aLEC == null)
      throw new IllegalStateException ("Failed to create layout execution context!");
    mainExecute (aLEC, aUnifiedResponse);
  }
}
