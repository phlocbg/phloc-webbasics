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

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;

import com.phloc.commons.annotations.OverrideOnDemand;

/**
 * Callbacks for the application server
 * 
 * @author philip
 */
public class WebAppListenerExtended extends WebAppListener
{
  /**
   * Set global system properties, after the content was initialized
   */
  @OverrideOnDemand
  protected void initSystemProperties ()
  {}

  /**
   * Register all application locales and set the default locale
   */
  @OverrideOnDemand
  protected void initApplicationLocales ()
  {}

  /**
   * Create all default user and user groups and roles
   */
  @OverrideOnDemand
  protected void initSecurity ()
  {}

  /**
   * Register all layout handler
   */
  @OverrideOnDemand
  protected void initLayout ()
  {}

  /**
   * Create all menu items
   */
  @OverrideOnDemand
  protected void initMenu ()
  {}

  /**
   * Register all ajax functions
   */
  @OverrideOnDemand
  protected void initAjax ()
  {}

  /**
   * Register all actions
   */
  @OverrideOnDemand
  protected void initActions ()
  {}

  /**
   * Init all things for which no special method is present
   */
  @OverrideOnDemand
  protected void initRest ()
  {}

  @Override
  protected final void afterContextInitialized (@Nonnull final ServletContext aSC)
  {
    // Global properties
    initSystemProperties ();

    // Register application locales
    initApplicationLocales ();

    // Init the security things
    initSecurity ();

    // Create the application layouts
    initLayout ();

    // Create all menu items
    initMenu ();

    // Register all Ajax functions here
    initAjax ();

    // Register all actions here
    initActions ();

    // All other things come last
    initRest ();
  }
}
