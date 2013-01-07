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
package com.phloc.webbasics.servlet;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;

import com.phloc.appbasics.app.ApplicationLocaleManager;
import com.phloc.appbasics.app.menu.ApplicationMenuTree;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.scopes.web.mgr.WebScopeManager;
import com.phloc.scopes.web.mock.MockHttpServletResponse;
import com.phloc.scopes.web.mock.OfflineHttpServletRequest;
import com.phloc.webbasics.action.ApplicationActionManager;
import com.phloc.webbasics.ajax.ApplicationAjaxManager;
import com.phloc.webbasics.app.init.IApplicationInitializer;
import com.phloc.webbasics.app.layout.ApplicationLayoutManager;

/**
 * Callbacks for the application server
 * 
 * @author philip
 */
public abstract class WebAppListenerMultiApp extends WebAppListener
{
  @Nonnull
  @Nonempty
  protected abstract Map <String, IApplicationInitializer> getAllInitializers ();

  /**
   * Set global system properties, after the content was initialized but before
   * the application specific init is started
   */
  @OverrideOnDemand
  protected void initGlobals ()
  {}

  @Override
  protected final void afterContextInitialized (@Nonnull final ServletContext aSC)
  {
    // Global properties
    initGlobals ();

    final Map <String, IApplicationInitializer> aIniter = getAllInitializers ();
    if (ContainerHelper.isEmpty (aIniter))
      throw new IllegalStateException ("No application initializers provided!");

    for (final Map.Entry <String, IApplicationInitializer> aEntry : aIniter.entrySet ())
    {
      final String sAppID = aEntry.getKey ();
      WebScopeManager.onRequestBegin (sAppID,
                                      new OfflineHttpServletRequest (aSC, false),
                                      new MockHttpServletResponse ());
      try
      {
        final IApplicationInitializer aInitializer = aEntry.getValue ();

        // Register application locales
        aInitializer.initLocales (ApplicationLocaleManager.getInstance ());

        // Create the application layouts
        aInitializer.initLayout (ApplicationLayoutManager.getInstance ());

        // Create all menu items
        aInitializer.initMenu (ApplicationMenuTree.getInstance ());

        // Register all Ajax functions here
        aInitializer.initAjax (ApplicationAjaxManager.getInstance ());

        // Register all actions here
        aInitializer.initActions (ApplicationActionManager.getInstance ());

        // All other things come last
        aInitializer.initRest ();
      }
      finally
      {
        WebScopeManager.onRequestEnd ();
      }
    }
  }
}
