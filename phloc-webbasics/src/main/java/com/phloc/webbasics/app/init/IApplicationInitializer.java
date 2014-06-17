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
 * Base interface for an application-specific initializer. The methods are
 * called in the correct order they are required for dependencies.
 * 
 * @author Philip Helger
 */
public interface IApplicationInitializer <LECTYPE extends LayoutExecutionContext>
{
  /**
   * Init all application specific settings. This method is called before all
   * named methods afterwards.
   */
  void initApplicationSettings ();

  /**
   * Init all application locales
   * 
   * @param aLocaleMgr
   *        Locale manager to use
   */
  void initLocales (@Nonnull ILocaleManager aLocaleMgr);

  /**
   * Register all layout handler
   * 
   * @param aLayoutMgr
   *        The layout manager to use
   */
  void initLayout (@Nonnull ILayoutManager <LECTYPE> aLayoutMgr);

  /**
   * Create all menu items
   * 
   * @param aMenuTree
   *        The menu tree to init
   */
  void initMenu (@Nonnull IMenuTree aMenuTree);

  /**
   * Register all ajax functions
   * 
   * @param aAjaxInvoker
   *        The ajax invoker to use
   */
  void initAjax (@Nonnull IAjaxInvoker aAjaxInvoker);

  /**
   * Register all actions
   * 
   * @param aActionInvoker
   *        The action invoker to use
   */
  void initActions (@Nonnull IActionInvoker aActionInvoker);

  /**
   * Init all things for which no special method is present after the predefined
   * methods.
   */
  void initRest ();
}
