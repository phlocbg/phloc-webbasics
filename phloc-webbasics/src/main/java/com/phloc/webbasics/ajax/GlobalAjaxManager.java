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

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.factory.IFactory;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webscopes.singleton.GlobalWebSingleton;

/**
 * A global AJAX manager.
 *
 * @author Philip Helger
 */
@ThreadSafe
public final class GlobalAjaxManager extends GlobalWebSingleton implements IAjaxInvoker
{
  private final AjaxInvoker m_aInvoker = new AjaxInvoker ();

  /**
   * Private constructor. Avoid outside instantiation
   */
  @Deprecated
  @UsedViaReflection
  public GlobalAjaxManager ()
  {}

  @Nonnull
  public static GlobalAjaxManager getInstance ()
  {
    return getGlobalSingleton (GlobalAjaxManager.class);
  }

  @Nullable
  public IAjaxBeforeExecutionHandler getBeforeExecutionHandler ()
  {
    return m_aInvoker.getBeforeExecutionHandler ();
  }

  public void setBeforeExecutionHandler (@Nullable final IAjaxBeforeExecutionHandler aBeforeExecutionHdl)
  {
    m_aInvoker.setBeforeExecutionHandler (aBeforeExecutionHdl);
  }

  @Nullable
  public IAjaxAfterExecutionHandler getAfterExecutionHandler ()
  {
    return m_aInvoker.getAfterExecutionHandler ();
  }

  public void setAfterExecutionHandler (@Nullable final IAjaxAfterExecutionHandler aAfterExecutionHdl)
  {
    m_aInvoker.setAfterExecutionHandler (aAfterExecutionHdl);
  }

  public long getLongRunningExecutionLimitTime ()
  {
    return m_aInvoker.getLongRunningExecutionLimitTime ();
  }

  public void setLongRunningExecutionLimitTime (final long nLongRunningExecutionLimitTime)
  {
    m_aInvoker.setLongRunningExecutionLimitTime (nLongRunningExecutionLimitTime);
  }

  @Nullable
  public IAjaxLongRunningExecutionHandler getLongRunningExecutionHandler ()
  {
    return m_aInvoker.getLongRunningExecutionHandler ();
  }

  public void setLongRunningExecutionHandler (@Nullable final IAjaxLongRunningExecutionHandler aLongRunningExecutionHdl)
  {
    m_aInvoker.setLongRunningExecutionHandler (aLongRunningExecutionHdl);
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

  public void addHandlerFunction (@Nonnull final IAjaxFunctionDeclaration aFunction,
                                  @Nonnull final Class <? extends IAjaxHandler> aClass)
  {
    m_aInvoker.addHandlerFunction (aFunction, aClass);
  }

  public void addHandlerFunction (@Nonnull final IAjaxFunctionDeclaration aFunction,
                                  @Nonnull final IFactory <? extends IAjaxHandler> aFactory)
  {
    m_aInvoker.addHandlerFunction (aFunction, aFactory);
  }

  public void addHandlerFunction (@Nonnull final String sFunctionName,
                                  @Nonnull final Class <? extends IAjaxHandler> aClass)
  {
    m_aInvoker.addHandlerFunction (sFunctionName, aClass);
  }

  public void addHandlerFunction (@Nonnull final String sFunctionName,
                                  @Nonnull final IFactory <? extends IAjaxHandler> aFactory)
  {
    m_aInvoker.addHandlerFunction (sFunctionName, aFactory);
  }

  @Nonnull
  public IAjaxResponse invokeFunction (@Nonnull final String sFunctionName,
                                       @Nonnull final IRequestWebScopeWithoutResponse aRequestWebScope) throws Exception
  {
    return m_aInvoker.invokeFunction (sFunctionName, aRequestWebScope);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("invoker", m_aInvoker).toString ();
  }
}
