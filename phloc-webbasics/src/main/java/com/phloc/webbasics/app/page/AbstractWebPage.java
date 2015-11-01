/**
 * Copyright (C) 2006-2015 phloc systems
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
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.state.EValidity;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.SMap;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCA_Target;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCH1;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.app.layout.ILayoutExecutionContext;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webscopes.mgr.WebScopeManager;

/**
 * Abstract base implementation for {@link IWebPage}.
 *
 * @author Philip Helger
 */
public abstract class AbstractWebPage <WPECTYPE extends IWebPageExecutionContext> extends AbstractPage implements IWebPage <WPECTYPE>
{
  /** The CSS class to be applied to the help div */
  public static final ICSSClassProvider CSS_PAGE_HELP_ICON = DefaultCSSClassProvider.create ("page_help_icon");

  private IWebPageIcon m_aIcon;

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
   * @param sName
   *        The constant (non-translatable) name of the page. May not be
   *        <code>null</code>.
   * @param sDescription
   *        The constant (non-translatable) description of the page. May be
   *        <code>null</code>.
   */
  public AbstractWebPage (@Nonnull @Nonempty final String sID,
                          @Nonnull final String sName,
                          @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
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

  /**
   * Constructor
   *
   * @param sID
   *        The unique page ID. May not be <code>null</code>.
   * @param aName
   *        The name of the page. May not be <code>null</code>.
   * @param aDescription
   *        Optional description of the page. May be <code>null</code>.
   */
  public AbstractWebPage (@Nonnull @Nonempty final String sID,
                          @Nonnull final IReadonlyMultiLingualText aName,
                          @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @Nullable
  public IWebPageIcon getIcon ()
  {
    return m_aIcon;
  }

  @Nonnull
  public AbstractWebPage <WPECTYPE> setIcon (@Nullable final IWebPageIcon aIcon)
  {
    m_aIcon = aIcon;
    return this;
  }

  @Nullable
  @OverrideOnDemand
  public String getHeaderText (@Nonnull final WPECTYPE aWPEC)
  {
    return getDisplayText (aWPEC.getDisplayLocale ());
  }

  @Nullable
  @OverrideOnDemand
  public IHCNode getHeaderNode (@Nonnull final WPECTYPE aWPEC)
  {
    final String sText = getHeaderText (aWPEC);
    if (StringHelper.hasNoText (sText))
      return null;
    return HCH1.create (sText);
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
  @Deprecated
  public HCForm createFormSelf ()
  {
    return new HCForm (LinkUtils.getSelfHref ());
  }

  /**
   * @return A form that links to the current page.
   */
  @Nonnull
  public HCForm createFormSelf (@Nonnull final ILayoutExecutionContext aLEC)
  {
    return new HCForm (aLEC.getSelfHref ());
  }

  /**
   * @return A file upload form that links to the current page.
   */
  @Nonnull
  @Deprecated
  public HCForm createFormFileUploadSelf ()
  {
    final HCForm aForm = createFormSelf ();
    aForm.setFileUploadEncType ();
    return aForm;
  }

  /**
   * @return A file upload form that links to the current page.
   */
  @Nonnull
  public HCForm createFormFileUploadSelf (@Nonnull final ILayoutExecutionContext aLEC)
  {
    final HCForm aForm = createFormSelf (aLEC);
    aForm.setFileUploadEncType ();
    return aForm;
  }

  /**
   * Check some pre-requisites. This is called as the very first action on each
   * page view.
   *
   * @param aWPEC
   *        The web page execution context
   * @return Never <code>null</code>.
   */
  @OverrideOnDemand
  @Nonnull
  protected EValidity isValidToDisplayPage (@Nonnull final WPECTYPE aWPEC)
  {
    return EValidity.VALID;
  }

  /**
   * This method is called before the main
   * {@link #fillContent(IWebPageExecutionContext)} method is called.
   */
  @OverrideOnDemand
  protected void beforeFillContent ()
  {}

  /**
   * Abstract method to be implemented by subclasses, that creates the main page
   * content, without the help icon.
   *
   * @param aWPEC
   *        The web page execution context. Never <code>null</code>.
   */
  protected abstract void fillContent (@Nonnull WPECTYPE aWPEC);

  /**
   * This method is called after the main
   * {@link #fillContent(IWebPageExecutionContext)} method is called.
   */
  @OverrideOnDemand
  protected void afterFillContent ()
  {}

  /**
   * Get the help URL of the current page
   *
   * @param aRequestScope
   *        The request web scope to be used. Required for cookie-less handling.
   *        May not be <code>null</code>.
   * @param aDisplayLocale
   *        The current display locale. Never <code>null</code>.
   * @return The help URL for this page. May not be <code>null</code>.
   */
  @Nonnull
  @OverrideOnDemand
  protected ISimpleURL getHelpURL (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                   @Nonnull final Locale aDisplayLocale)
  {
    return LinkUtils.getURLWithContext (aRequestScope,
                                        "help/" + getID (),
                                        new SMap ().add ("locale", aDisplayLocale.toString ()));
  }

  /**
   * Create the HC node to represent the help icon. This method is only called,
   * if help is available for this page. The created code looks like this by
   * default:<br>
   * <code>&lt;a href="<i>helpURL</i>" title="Show help for page <i>pageName</i>" target="simplehelpwindow"&gt;<br>
   * &lt;span class="page_help_icon"&gt;&lt;/span&gt;<br>
   * &lt;/a&gt;</code>
   *
   * @param aWPEC
   *        The web page execution context
   * @return The created help icon node. May be <code>null</code>.
   */
  @Nullable
  @OverrideOnDemand
  protected IHCNode getHelpIconNode (@Nonnull final WPECTYPE aWPEC)
  {
    final IRequestWebScopeWithoutResponse aRequestScope = aWPEC.getRequestScope ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    final HCA aHelpNode = new HCA (getHelpURL (aRequestScope, aDisplayLocale));
    final String sPageName = getDisplayText (aDisplayLocale);
    aHelpNode.setTitle (EWebBasicsText.PAGE_HELP_TITLE.getDisplayTextWithArgs (aDisplayLocale, sPageName));
    aHelpNode.addChild (new HCSpan ().addClass (CSS_PAGE_HELP_ICON));
    aHelpNode.setTarget (new HCA_Target (HELP_WINDOW_NAME));
    return aHelpNode;
  }

  /**
   * Check if is help is available for the current execution context
   *
   * @param aWPEC
   *        The web page execution context
   * @return <code>true</code> if help is available, <code>false</code> if not
   */
  @OverrideOnDemand
  public boolean isHelpAvailable (@Nonnull final WPECTYPE aWPEC)
  {
    return false;
  }

  /**
   * Overridable method to attach the help node to the page. This is called as
   * the last action.
   *
   * @param aWPEC
   *        The web page execution context. Never <code>null</code>.
   * @param aHelpNode
   *        The help node to be inserted. Never <code>null</code>.
   */
  @OverrideOnDemand
  protected void insertHelpNode (@Nonnull final WPECTYPE aWPEC, @Nonnull final IHCNode aHelpNode)
  {
    // Add the help icon as the first child of the resulting node list
    aWPEC.getNodeList ().addChild (0, aHelpNode);
  }

  /**
   * Default implementation calling the abstract
   * {@link #fillContent(IWebPageExecutionContext)} method and creating the help
   * node if desired.
   */
  public final void getContent (@Nonnull final WPECTYPE aWPEC)
  {
    if (isValidToDisplayPage (aWPEC).isValid ())
    {
      // "before"-callback
      beforeFillContent ();

      // Create the main page content
      fillContent (aWPEC);

      // "after"-callback
      afterFillContent ();
    }

    // Is help available for this page?
    if (isHelpAvailable (aWPEC))
    {
      final IHCNode aHelpNode = getHelpIconNode (aWPEC);
      if (aHelpNode != null)
        insertHelpNode (aWPEC, aHelpNode);
    }
  }
}
