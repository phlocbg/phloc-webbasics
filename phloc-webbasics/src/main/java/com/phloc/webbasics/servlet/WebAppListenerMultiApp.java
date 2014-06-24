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
package com.phloc.webbasics.servlet;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.ApplicationLocaleManager;
import com.phloc.appbasics.app.menu.ApplicationMenuTree;
import com.phloc.appbasics.security.password.GlobalPasswordSettings;
import com.phloc.appbasics.security.password.constraint.PasswordConstraintList;
import com.phloc.appbasics.security.password.constraint.PasswordConstraintMinLength;
import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.html.js.builder.JSPrinter;
import com.phloc.web.mock.MockHttpServletResponse;
import com.phloc.web.mock.OfflineHttpServletRequest;
import com.phloc.webbasics.action.ApplicationActionManager;
import com.phloc.webbasics.ajax.ApplicationAjaxManager;
import com.phloc.webbasics.app.init.IApplicationInitializer;
import com.phloc.webbasics.app.layout.ApplicationLayoutManager;
import com.phloc.webbasics.app.layout.ILayoutExecutionContext;
import com.phloc.webbasics.userdata.UserDataManager;
import com.phloc.webscopes.mgr.WebScopeManager;

/**
 * Callbacks for the application server
 * 
 * @author Philip Helger
 */
public abstract class WebAppListenerMultiApp <LECTYPE extends ILayoutExecutionContext> extends WebAppListenerWithStatistics
{
  public static final int DEFAULT_PASSWORD_MIN_LENGTH = 6;
  private static final Logger s_aLogger = LoggerFactory.getLogger (WebAppListenerMultiApp.class);

  @Nonnull
  @Nonempty
  protected abstract Map <String, IApplicationInitializer <LECTYPE>> getAllInitializers ();

  /**
   * Set global system properties, after the content was initialized but before
   * the application specific init is started
   */
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void initGlobals ()
  {
    // Enable when ready
    WebScopeManager.setSessionPassivationAllowed (false);

    // UDO to data directory
    UserDataManager.setServletContextIO (false);

    // Define the password constrains
    GlobalPasswordSettings.setPasswordConstraintList (new PasswordConstraintList (new PasswordConstraintMinLength (DEFAULT_PASSWORD_MIN_LENGTH)));

    // Global JS formatting stuff :(
    JSPrinter.setGenerateComments (false);
    JSPrinter.setGenerateTypeNames (false);
    JSPrinter.setIndentAndAlign (GlobalDebug.isDebugMode ());
  }

  @Override
  protected final void afterContextInitialized (@Nonnull final ServletContext aSC)
  {
    // Global properties
    initGlobals ();

    // Determine all initializers
    final Map <String, IApplicationInitializer <LECTYPE>> aIniter = getAllInitializers ();
    if (ContainerHelper.isEmpty (aIniter))
      throw new IllegalStateException ("No application initializers provided!");

    // Invoke all initializers
    for (final Map.Entry <String, IApplicationInitializer <LECTYPE>> aEntry : aIniter.entrySet ())
    {
      final String sAppID = aEntry.getKey ();
      WebScopeManager.onRequestBegin (sAppID,
                                      new OfflineHttpServletRequest (aSC, false),
                                      new MockHttpServletResponse ());
      try
      {
        final IApplicationInitializer <LECTYPE> aInitializer = aEntry.getValue ();

        // Set per-application settings
        aInitializer.initApplicationSettings ();

        // Register application locales
        aInitializer.initLocales (ApplicationLocaleManager.getInstance ());

        // Create the application layouts
        aInitializer.initLayout (ApplicationLayoutManager.<LECTYPE> getInstance ());

        // Create all menu items
        aInitializer.initMenu (ApplicationMenuTree.getTree ());

        // Register all Ajax functions here
        aInitializer.initAjax (ApplicationAjaxManager.getInstance ());

        // Register all actions here
        aInitializer.initActions (ApplicationActionManager.getInstance ());

        // All other things come last
        aInitializer.initRest ();
      }
      catch (final RuntimeException ex)
      {
        // Log so that the failed application can easily be determined.
        s_aLogger.error ("Failed to init application '" + sAppID + "': " + ex.getMessage ());

        // re-throw
        throw ex;
      }
      finally
      {
        WebScopeManager.onRequestEnd ();
      }
    }
  }
}
