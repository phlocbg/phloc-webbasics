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

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.ReadonlySimpleURL;
import com.phloc.css.DefaultCSSClassProvider;
import com.phloc.css.ICSSClassProvider;
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
public abstract class AbstractPageWithHelp extends AbstractPage
{
  /** The CSS class to be applied to the help div */
  private static final ICSSClassProvider CSS_PAGE_HELP_ICON = DefaultCSSClassProvider.create ("page_help_icon");

  /** The name of the window where the help opens up */
  protected static final String HELP_WINDOW_NAME = "simplehelpwindow";

  public AbstractPageWithHelp (@Nonnull @Nonempty final String sID)
  {
    super (sID);
  }

  public AbstractPageWithHelp (@Nonnull @Nonempty final String sID, @Nonnull final IHasDisplayText aName)
  {
    super (sID, aName);
  }

  protected abstract void fillContent (@Nonnull HCNodeList aNodeList, @Nonnull final Locale aDisplayLocale);

  /**
   * @return <code>true</code> if help is available for this page,
   *         <code>false</code> otherwise.
   */
  @OverrideOnDemand
  protected boolean isHelpAvailable ()
  {
    return true;
  }

  /**
   * @param aDisplayLocale
   *        The current display locale
   * @return The Help URL for this page
   */
  @Nonnull
  @OverrideOnDemand
  protected ISimpleURL getHelpURL (@Nonnull final Locale aDisplayLocale)
  {
    return new ReadonlySimpleURL (LinkUtils.getContextAwareURI ("help/" +
                                                                getID () +
                                                                "?locale=" +
                                                                aDisplayLocale.toString ()));
  }

  @Nonnull
  @OverrideOnDemand
  protected IHCNode getHelpIconNode (@Nonnull final Locale aDisplayLocale)
  {
    final HCA aHelpNode = new HCA (getHelpURL (aDisplayLocale));
    aHelpNode.setTitle (EWebBasicsText.PAGE_HELP_TITLE.getDisplayTextWithArgs (aDisplayLocale,
                                                                               getDisplayText (aDisplayLocale)));
    aHelpNode.addChild (new HCSpan ().addClass (CSS_PAGE_HELP_ICON));
    aHelpNode.setTarget (new HCA_Target (HELP_WINDOW_NAME));
    return aHelpNode;
  }

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
