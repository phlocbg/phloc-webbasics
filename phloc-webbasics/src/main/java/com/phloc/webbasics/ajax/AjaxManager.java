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

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.factory.IFactory;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.scopes.nonweb.singleton.GlobalSingleton;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;

/**
 * The main AJAX manager.
 * 
 * @author philip
 */
@ThreadSafe
public final class AjaxManager extends GlobalSingleton implements IAjaxInvoker
{
  private final AjaxInvoker m_aInvoker = new AjaxInvoker ();

  /**
   * Private constructor. Avoid outside instantiation
   */
  @Deprecated
  @UsedViaReflection
  public AjaxManager ()
  {}

  @Nonnull
  public static AjaxManager getInstance ()
  {
    return getGlobalSingleton (AjaxManager.class);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, IFactory <? extends IAjaxHandler>> getAllRegisteredHandlers ()
  {
    return m_aInvoker.getAllRegisteredHandlers ();
  }

  @Nullable
  public IFactory <? extends IAjaxHandler> getRegisteredHandler (@Nullable final String sFunctionName)
  {
    return m_aInvoker.getRegisteredHandler (sFunctionName);
  }

  public boolean isRegisteredFunction (@Nullable final String sFunctionName)
  {
    return m_aInvoker.isRegisteredFunction (sFunctionName);
  }

  public void addHandlerFunction (@Nonnull final IAjaxFunction aFunction,
                                  @Nonnull final IFactory <? extends IAjaxHandler> aFactory)
  {
    m_aInvoker.addHandlerFunction (aFunction, aFactory);
  }

  public void addHandlerFunction (@Nonnull final String sFunctionName,
                                  @Nonnull final IFactory <? extends IAjaxHandler> aFactory)
  {
    m_aInvoker.addHandlerFunction (sFunctionName, aFactory);
  }

  @Nonnull
  public AjaxDefaultResponse invokeFunction (@Nonnull final String sFunctionName,
                                             @Nonnull final IRequestWebScopeWithoutResponse aRequestWebScope) throws Exception
  {
    return m_aInvoker.invokeFunction (sFunctionName, aRequestWebScope);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("invoker", m_aInvoker).toString ();
  }

  @Deprecated
  public static boolean isValidFunctionName (@Nullable final String sFunctionName)
  {
    return AjaxInvoker.isValidFunctionName (sFunctionName);
  }
}
