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

import java.util.List;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.scopes.web.mgr.WebScopeManager;
import com.phloc.scopes.web.mock.MockHttpServletResponse;
import com.phloc.scopes.web.mock.OfflineHttpServletRequest;

/**
 * Callbacks for the application server
 * 
 * @author philip
 */
public abstract class WebAppListenerMultiApp extends WebAppListener
{
  @Nonnull
  @Nonempty
  protected abstract List <String> getAllApplicationIDs ();

  /**
   * Set global system properties, after the content was initialized
   */
  @OverrideOnDemand
  protected void initSystemProperties ()
  {}

  /**
   * Register all application locales and set the default locale
   * 
   * @param sApplicationID
   *        The application ID to init
   */
  @OverrideOnDemand
  protected void initLocales (@Nonnull @Nonempty final String sApplicationID)
  {}

  /**
   * Create all default user and user groups and roles
   * 
   * @param sApplicationID
   *        The application ID to init
   */
  @OverrideOnDemand
  protected void initSecurity (@Nonnull @Nonempty final String sApplicationID)
  {}

  /**
   * Register all layout handler
   * 
   * @param sApplicationID
   *        The application ID to init
   */
  @OverrideOnDemand
  protected void initLayout (@Nonnull @Nonempty final String sApplicationID)
  {}

  /**
   * Create all menu items
   * 
   * @param sApplicationID
   *        The application ID to init
   */
  @OverrideOnDemand
  protected void initMenu (@Nonnull @Nonempty final String sApplicationID)
  {}

  /**
   * Register all ajax functions
   * 
   * @param sApplicationID
   *        The application ID to init
   */
  @OverrideOnDemand
  protected void initAjax (@Nonnull @Nonempty final String sApplicationID)
  {}

  /**
   * Register all actions
   * 
   * @param sApplicationID
   *        The application ID to init
   */
  @OverrideOnDemand
  protected void initActions (@Nonnull @Nonempty final String sApplicationID)
  {}

  /**
   * Init all things for which no special method is present
   * 
   * @param sApplicationID
   *        The application ID to init
   */
  @OverrideOnDemand
  protected void initRest (@Nonnull @Nonempty final String sApplicationID)
  {}

  @Override
  protected final void afterContextInitialized (@Nonnull final ServletContext aSC)
  {
    // Global properties
    initSystemProperties ();

    final List <String> aAppIDs = getAllApplicationIDs ();
    if (ContainerHelper.isEmpty (aAppIDs))
      throw new IllegalStateException ("No application IDs provided!");

    for (final String sApplicationID : aAppIDs)
    {
      WebScopeManager.onRequestBegin (sApplicationID, new OfflineHttpServletRequest (), new MockHttpServletResponse ());
      try
      {
        // Register application locales
        initLocales (sApplicationID);

        // Init the security things
        initSecurity (sApplicationID);

        // Create the application layouts
        initLayout (sApplicationID);

        // Create all menu items
        initMenu (sApplicationID);

        // Register all Ajax functions here
        initAjax (sApplicationID);

        // Register all actions here
        initActions (sApplicationID);

        // All other things come last
        initRest (sApplicationID);
      }
      finally
      {
        WebScopeManager.onRequestEnd ();
      }
    }
  }
}
