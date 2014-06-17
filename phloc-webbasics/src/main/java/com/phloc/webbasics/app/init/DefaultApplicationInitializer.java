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
package com.phloc.webbasics.app.init;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.ILocaleManager;
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.webbasics.action.IActionInvoker;
import com.phloc.webbasics.ajax.IAjaxInvoker;
import com.phloc.webbasics.app.layout.ILayoutManager;
import com.phloc.webbasics.app.layout.LayoutExecutionContext;

/**
 * Default implementation class of {@link IApplicationInitializer} doing
 * nothing. Use this as the base class for your implementation.
 * 
 * @author Philip Helger
 */
public class DefaultApplicationInitializer <LECTYPE extends LayoutExecutionContext> implements IApplicationInitializer <LECTYPE>
{
  public void initApplicationSettings ()
  {}

  public void initLocales (@Nonnull final ILocaleManager aLocaleMgr)
  {}

  public void initLayout (@Nonnull final ILayoutManager <LECTYPE> aLayoutMgr)
  {}

  public void initMenu (@Nonnull final IMenuTree aMenuTree)
  {}

  public void initAjax (@Nonnull final IAjaxInvoker aAjaxInvoker)
  {}

  public void initActions (@Nonnull final IActionInvoker aActionInvoker)
  {}

  public void initRest ()
  {}
}
