package com.phloc.webbasics.servlet;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;

import com.phloc.appbasics.app.menu.MenuTree;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.webbasics.action.ActionManager;
import com.phloc.webbasics.ajax.AjaxManager;
import com.phloc.webbasics.app.layout.LayoutManager;

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
   * 
   * @param aLayoutManager
   */
  @OverrideOnDemand
  protected void initLayout (@Nonnull final LayoutManager aLayoutManager)
  {}

  /**
   * Create all menu items
   * 
   * @param aMenu
   *        The menu tree to use
   */
  @OverrideOnDemand
  protected void initMenu (@Nonnull final MenuTree aMenu)
  {}

  /**
   * Register all ajax functions
   * 
   * @param aAjaxMgr
   *        The manager to register the functions at
   */
  @OverrideOnDemand
  protected void initAjax (@Nonnull final AjaxManager aAjaxMgr)
  {}

  /**
   * Register all actions
   * 
   * @param aActionMgr
   *        The manager to register the actions at
   */
  @OverrideOnDemand
  protected void initActions (@Nonnull final ActionManager aActionMgr)
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
    initLayout (LayoutManager.getInstance ());

    // Create all menu items
    initMenu (MenuTree.getInstance ());

    // Register all Ajax functions here
    initAjax (AjaxManager.getInstance ());

    // Register all actions here
    initActions (ActionManager.getInstance ());

    // All other things come last
    initRest ();
  }
}
