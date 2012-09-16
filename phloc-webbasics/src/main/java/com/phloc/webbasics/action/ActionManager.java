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
package com.phloc.webbasics.action;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;

/**
 * This class represents a singleton {@link ActionContainer}.
 * 
 * @author philip
 */
@ThreadSafe
public final class ActionManager
{
  // Singleton instance
  private static final ActionManager s_aInstance = new ActionManager ();

  // Main container
  private final ActionContainer m_aActionContainer = new ActionContainer ();

  private ActionManager ()
  {}

  @Nonnull
  public static ActionManager getInstance ()
  {
    return s_aInstance;
  }

  public void setCustomExceptionHandler (@Nullable final IActionExceptionHandler aExceptionHandler)
  {
    s_aInstance.setCustomExceptionHandler (aExceptionHandler);
  }

  @Nullable
  public IActionExceptionHandler getCustomExceptionHandler ()
  {
    return s_aInstance.getCustomExceptionHandler ();
  }

  public void addAction (@Nonnull final String sAction, @Nonnull final IActionExecutor aCallback)
  {
    m_aActionContainer.addAction (sAction, aCallback);
  }

  @Nonnull
  public ESuccess executeAction (@Nullable final String sAction,
                                 @Nonnull final IRequestWebScopeWithoutResponse aRequestScope) throws Exception
  {
    return m_aActionContainer.executeAction (sAction, aRequestScope);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, IActionExecutor> getAllActions ()
  {
    return m_aActionContainer.getAllActions ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("container", m_aActionContainer).toString ();
  }
}
