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
package com.phloc.webbasics.app.page;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.page.AbstractPage;
import com.phloc.commons.annotations.DevelopersNote;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.state.EValidity;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.ReadonlySimpleURL;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCA_Target;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCForm_FileUpload;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webscopes.mgr.WebScopeManager;

/**
 * Abstract base implementation for {@link IWebPage}.
 * 
 * @author philip
 */
public abstract class AbstractWebPage extends AbstractPage implements IWebPage
{
  /** The CSS class to be applied to the help div */
  private static final ICSSClassProvider CSS_PAGE_HELP_ICON = DefaultCSSClassProvider.create ("page_help_icon");

  /**
   * Constructor
   * 
   * @param sID
   *        The unique page ID. May not be <code>null</code>.
   */
  public AbstractWebPage (@Nonnull @Nonempty final String sID)
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
  public AbstractWebPage (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
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
  public AbstractWebPage (@Nonnull @Nonempty final String sID, @Nonnull final IReadonlyMultiLingualText aName)
  {
    super (sID, aName);
  }

  @Nullable
  @OverrideOnDemand
  public String getHeaderText (@Nonnull final WebPageExecutionContext aWPEC)
  {
    return getDisplayText (aWPEC.getDisplayLocale ());
  }

  @Nonnull
  public static final IRequestWebScopeWithoutResponse getScope ()
  {
    return WebScopeManager.getRequestScope ();
  }

  /**
   * @return A form that links to the current page.
   */
  @Nonnull
  public static final HCForm createFormSelf ()
  {
    return new HCForm (LinkUtils.getSelfHref ());
  }

  /**
   * @return A file upload form that links to the current page.
   */
  @Nonnull
  public static final HCForm createFormFileUploadSelf ()
  {
    return new HCForm_FileUpload (LinkUtils.getSelfHref ());
  }

  /**
   * Check some pre-requisites
   * 
   * @param aWPEC
   *        The web page execution context
   * @return Never <code>null</code>.
   */
  @OverrideOnDemand
  @Nonnull
  protected EValidity isValidToDisplayPage (@Nonnull final WebPageExecutionContext aWPEC)
  {
    return EValidity.VALID;
  }

  /**
   * Abstract method to be implemented by subclasses, that creates the main page
   * content, without the help icon.
   * 
   * @param aWPEC
   *        The web page execution context. Never <code>null</code>.
   */
  protected abstract void fillContent (@Nonnull WebPageExecutionContext aWPEC);

  /**
   * Get the help URL of the current page
   * 
   * @param aDisplayLocale
   *        The current display locale. Never <code>null</code>.
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
   * @param aWPEC
   *        The web page execution context
   * @return The created help icon node.
   */
  @Nonnull
  @OverrideOnDemand
  protected IHCNode getHelpIconNode (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final HCA aHelpNode = new HCA (getHelpURL (aDisplayLocale));
    final String sPageName = getDisplayText (aDisplayLocale);
    aHelpNode.setTitle (EWebBasicsText.PAGE_HELP_TITLE.getDisplayTextWithArgs (aDisplayLocale, sPageName));
    aHelpNode.addChild (new HCSpan ().addClass (CSS_PAGE_HELP_ICON));
    aHelpNode.setTarget (new HCA_Target (HELP_WINDOW_NAME));
    return aHelpNode;
  }

  @Override
  @Deprecated
  @DevelopersNote ("Use the one with the WebPageExecutionContext instead")
  public final boolean isHelpAvailable ()
  {
    return super.isHelpAvailable ();
  }

  /**
   * Check if is help is available for the current execution context
   * 
   * @param aWPEC
   *        The web page execution context
   * @return <code>true</code> if help is available, <code>false</code> if not
   */
  @OverrideOnDemand
  public boolean isHelpAvailable (@Nonnull final WebPageExecutionContext aWPEC)
  {
    return false;
  }

  /**
   * Default implementation calling the abstract
   * {@link #fillContent(WebPageExecutionContext)} method and creating the help
   * node if desired.
   */
  @Nonnull
  public final void getContent (@Nonnull final WebPageExecutionContext aWPEC)
  {
    if (isValidToDisplayPage (aWPEC).isValid ())
    {
      // Create the main page content
      fillContent (aWPEC);
    }

    // Is help available for this page?
    if (isHelpAvailable (aWPEC))
    {
      // Add the help icon as the first child of the resulting node list
      aWPEC.getNodeList ().addChild (0, getHelpIconNode (aWPEC));
    }
  }
}
