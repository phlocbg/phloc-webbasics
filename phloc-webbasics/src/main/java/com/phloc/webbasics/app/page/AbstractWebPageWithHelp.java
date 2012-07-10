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
package com.phloc.webbasics.app.page;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.page.AbstractBasePageWithHelp;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.ReadonlySimpleURL;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCA_Target;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.LinkUtils;

/**
 * Base class for a page that supports help.
 * 
 * @author philip
 */
public abstract class AbstractWebPageWithHelp extends AbstractWebPage
{
  /** The CSS class to be applied to the help div */
  private static final ICSSClassProvider CSS_PAGE_HELP_ICON = DefaultCSSClassProvider.create ("page_help_icon");

  /** The name of the window where the help opens up */
  protected static final String HELP_WINDOW_NAME = AbstractBasePageWithHelp.HELP_WINDOW_NAME;

  /**
   * Constructor
   * 
   * @param sID
   *        The unique page ID. May not be <code>null</code>.
   */
  public AbstractWebPageWithHelp (@Nonnull @Nonempty final String sID)
  {
    super (sID);
  }

  /**
   * Constructor
   * 
   * @param sID
   *        The unique page ID. May not be <code>null</code>.
   * @param sName
   *        The constant (non-translatable) name of the page. May not be
   *        <code>null</code>.
   */
  public AbstractWebPageWithHelp (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  /**
   * Constructor
   * 
   * @param sID
   *        The unique page ID. May not be <code>null</code>.
   * @param aName
   *        The name of the page. May not be <code>null</code>.
   */
  public AbstractWebPageWithHelp (@Nonnull @Nonempty final String sID, @Nonnull final IHasDisplayText aName)
  {
    super (sID, aName);
  }

  /**
   * Abstract method to be implemented by subclasses, that creates the main page
   * content, without the help icon.
   * 
   * @param aNodeList
   *        The node list where the created nodes should be placed. Never
   *        <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to be used for rendering the current page.
   */
  protected abstract void fillContent (@Nonnull HCNodeList aNodeList, @Nonnull final Locale aDisplayLocale);

  /**
   * Determine whether help is available for this page. The default
   * implementation returns always <code>true</code>.
   * 
   * @return <code>true</code> if help is available for this page,
   *         <code>false</code> otherwise.
   */
  @OverrideOnDemand
  protected boolean isHelpAvailable ()
  {
    return true;
  }

  /**
   * Get the help URL of the current page
   * 
   * @param aDisplayLocale
   *        The current display locale
   * @return The help URL for this page. May not be <code>null</code>.
   */
  @Nonnull
  @OverrideOnDemand
  protected ISimpleURL getHelpURL (@Nonnull final Locale aDisplayLocale)
  {
    return new ReadonlySimpleURL (LinkUtils.getURIWithContext ("help/" +
                                                               getID () +
                                                               "?locale=" +
                                                               aDisplayLocale.toString ()));
  }

  /**
   * Create the HC node to represent the help icon. This method is only called,
   * if help is available for this page. The created code looks like this by
   * default:<br>
   * <code>&lt;a href="<i>helpURL</i>" title="Show help for page <i>pageName</i>" target="simplehelpwindow"><br>
   * &lt;span class="page_help_icon">&lt;/span><br>
   * &lt;/a></code>
   * 
   * @param aDisplayLocale
   *        The display locale of the page
   * @return The created help icon node.
   */
  @Nonnull
  @OverrideOnDemand
  protected IHCNode getHelpIconNode (@Nonnull final Locale aDisplayLocale)
  {
    final HCA aHelpNode = new HCA (getHelpURL (aDisplayLocale));
    final String sPageName = getDisplayText (aDisplayLocale);
    aHelpNode.setTitle (EWebBasicsText.PAGE_HELP_TITLE.getDisplayTextWithArgs (aDisplayLocale, sPageName));
    aHelpNode.addChild (new HCSpan ().addClass (CSS_PAGE_HELP_ICON));
    aHelpNode.setTarget (new HCA_Target (HELP_WINDOW_NAME));
    return aHelpNode;
  }

  /**
   * Default implementation calling the abstract
   * {@link #fillContent(HCNodeList, Locale)} method and creating the help node
   * if desired.
   */
  @Nonnull
  public final IHCNode getContent (@Nonnull final Locale aDisplayLocale)
  {
    final HCNodeList aNodeList = new HCNodeList ();

    // Create the main page content
    fillContent (aNodeList, aDisplayLocale);

    // Is help available for this page?
    if (isHelpAvailable ())
    {
      // Add the help icon as the first child of the resulting node list
      aNodeList.addChild (0, getHelpIconNode (aDisplayLocale));
    }
    return aNodeList;
  }
}
