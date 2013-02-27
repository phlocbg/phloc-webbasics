package com.phloc.webbasics.app.page.system;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.menu.IMenuItemExternal;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuSeparator;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.webbasics.app.LinkUtils;

/**
 * The default renderer used for {@link PageShowChildren}.
 * 
 * @author philip
 */
public class PageShowChildrenRenderer implements Serializable
{
  public PageShowChildrenRenderer ()
  {}

  /**
   * Render a menu separator
   * 
   * @param aMenuSeparator
   *        The menu separator. Never <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to use. Never <code>null</code>.
   * @return The rendered representation or <code>null</code>
   */
  @Nullable
  @OverrideOnDemand
  protected IHCNode renderMenuSeparator (@Nonnull final IMenuSeparator aMenuSeparator,
                                         @Nonnull final Locale aDisplayLocale)
  {
    return null;
  }

  /**
   * Render a menu item to an internal page
   * 
   * @param aMenuItemPage
   *        The menu item. Never <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to use. Never <code>null</code>.
   * @return The rendered representation or <code>null</code>
   */
  @Nullable
  @OverrideOnDemand
  protected IHCNode renderMenuItemPage (@Nonnull final IMenuItemPage aMenuItemPage, @Nonnull final Locale aDisplayLocale)
  {
    return new HCA (LinkUtils.getLinkToMenuItem (aMenuItemPage.getID ())).addChild (aMenuItemPage.getDisplayText (aDisplayLocale));
  }

  /**
   * Render a menu item to an external page
   * 
   * @param aMenuItemExternal
   *        The menu item. Never <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to use. Never <code>null</code>.
   * @return The rendered representation or <code>null</code>
   */
  @Nullable
  @OverrideOnDemand
  protected IHCNode renderMenuItemExternal (@Nonnull final IMenuItemExternal aMenuItemExternal,
                                            @Nonnull final Locale aDisplayLocale)
  {
    return new HCA (aMenuItemExternal.getURL ()).addChild (aMenuItemExternal.getDisplayText (aDisplayLocale));
  }
}
