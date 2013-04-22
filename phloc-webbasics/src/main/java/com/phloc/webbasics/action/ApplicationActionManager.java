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

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.scopes.singleton.ApplicationSingleton;
import com.phloc.web.servlet.response.UnifiedResponse;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * This class represents a per-application singleton {@link ActionInvoker}.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class ApplicationActionManager extends ApplicationSingleton implements IActionInvoker
{
  // Main container
  private final IActionInvoker m_aInvoker = new ActionInvoker ();

  @Deprecated
  @UsedViaReflection
  public ApplicationActionManager ()
  {}

  @Nonnull
  public static ApplicationActionManager getInstance ()
  {
    return getApplicationSingleton (ApplicationActionManager.class);
  }

  public void setCustomExceptionHandler (@Nullable final IActionExceptionHandler aExceptionHandler)
  {
    m_aInvoker.setCustomExceptionHandler (aExceptionHandler);
  }

  @Nullable
  public IActionExceptionHandler getCustomExceptionHandler ()
  {
    return m_aInvoker.getCustomExceptionHandler ();
  }

  public void addAction (@Nonnull final String sAction, @Nonnull final IActionExecutor aCallback)
  {
    m_aInvoker.addAction (sAction, aCallback);
  }

  @Nonnull
  public ESuccess executeAction (@Nullable final String sAction,
                                 @Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                 @Nonnull final UnifiedResponse aUnifiedResponse) throws Exception
  {
    return m_aInvoker.executeAction (sAction, aRequestScope, aUnifiedResponse);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, IActionExecutor> getAllActions ()
  {
    return m_aInvoker.getAllActions ();
  }

  public boolean containsAction (@Nullable final String sAction)
  {
    return m_aInvoker.containsAction (sAction);
  }

  @Nullable
  public IActionExecutor getActionExecutor (@Nullable final String sAction)
  {
    return m_aInvoker.getActionExecutor (sAction);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("container", m_aInvoker).toString ();
  }
}
