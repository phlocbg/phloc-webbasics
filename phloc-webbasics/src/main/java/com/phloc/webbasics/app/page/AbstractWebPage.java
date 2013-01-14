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

import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.page.AbstractPage;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.state.EValidity;
import com.phloc.commons.string.StringHelper;
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
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
import com.phloc.scopes.web.mgr.WebScopeManager;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.form.RequestFieldBoolean;
import com.phloc.webbasics.web.IRequestParamMap;
import com.phloc.webbasics.web.RequestHelper;

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
  public String getHeaderText (@Nonnull final Locale aContentLocale)
  {
    return getDisplayText (aContentLocale);
  }

  @Nonnull
  protected static final IRequestWebScopeWithoutResponse getScope ()
  {
    return WebScopeManager.getRequestScope ();
  }

  /**
   * Check if the request parameter with the given name is present (independent
   * of the value).
   * 
   * @param sName
   *        Request parameter name
   * @return <code>true</code> of the attribute is present, <code>false</code>
   *         if not
   */
  protected static final boolean containsAttr (@Nullable final String sName)
  {
    return getScope ().containsAttribute (sName);
  }

  /**
   * Get the value of the request parameter with the given name.
   * 
   * @param sName
   *        Request parameter name
   * @return The value of the passed request parameter
   */
  @Nullable
  protected static final String getAttr (@Nullable final String sName)
  {
    return getAttr (sName, null);
  }

  /**
   * Get the value of the request parameter with the given name.
   * 
   * @param sName
   *        Request parameter name
   * @param sDefault
   *        The default value to be used, if no request attribute is present
   * @return The value of the passed request parameter or the default value
   */
  @Nullable
  protected static final String getAttr (@Nullable final String sName, @Nullable final String sDefault)
  {
    final String sScopeValue = getScope ().getAttributeAsString (sName, sDefault);
    return StringHelper.trim (sScopeValue);
  }

  /**
   * Get the value list of the request parameter with the given name.
   * 
   * @param sName
   *        Request parameter name
   * @return The value list of the passed request parameter
   */
  @Nullable
  protected static final List <String> getAttrs (@Nullable final String sName)
  {
    return getScope ().getAttributeValues (sName);
  }

  /**
   * Get the value of the request parameter with the given name as a boolean.
   * 
   * @param sName
   *        The attribute values.
   * @param bDefault
   *        The default value to be returned if either no such parameter is
   *        present or if the parameter value cannot be safely converted to an
   *        boolean value.
   * @return The boolean representation of the parameter value or the default
   *         value.
   */
  protected static final boolean getBooleanAttr (@Nullable final String sName, final boolean bDefault)
  {
    return getScope ().getAttributeAsBoolean (sName, bDefault);
  }

  /**
   * Get the value of the request parameter with the given name as an integer.
   * 
   * @param sName
   *        The attribute values.
   * @param nDefault
   *        The default value to be returned if either no such parameter is
   *        present or if the parameter value cannot be safely converted to an
   *        integer value.
   * @return The integer representation of the parameter value or the default
   *         value.
   */
  protected static final int getIntAttr (@Nullable final String sName, final int nDefault)
  {
    return getScope ().getAttributeAsInt (sName, nDefault);
  }

  /**
   * Get the value of the request parameter with the given name as a long.
   * 
   * @param sName
   *        The attribute values.
   * @param nDefault
   *        The default value to be returned if either no such parameter is
   *        present or if the parameter value cannot be safely converted to an
   *        long value.
   * @return The long representation of the parameter value or the default
   *         value.
   */
  protected static final long getLongAttr (@Nullable final String sName, final long nDefault)
  {
    return getScope ().getAttributeAsLong (sName, nDefault);
  }

  /**
   * Get the value of the request parameter with the given name as a double.
   * 
   * @param sName
   *        The attribute values.
   * @param dDefault
   *        The default value to be returned if either no such parameter is
   *        present or if the parameter value cannot be safely converted to an
   *        integer value.
   * @return The integer representation of the parameter value or the default
   *         value.
   */
  protected static final double getDoubleAttr (@Nullable final String sName, final double dDefault)
  {
    return getScope ().getAttributeAsDouble (sName, dDefault);
  }

  /**
   * Get the value of the request parameter with the given name casted to the
   * specified data type.
   * 
   * @param sName
   *        Request parameter name
   * @return The value of the passed request parameter
   */
  @Nullable
  protected static final <DATATYPE> DATATYPE getCastedAttr (@Nullable final String sName)
  {
    return getCastedAttr (sName, (DATATYPE) null);
  }

  /**
   * Get the value of the request parameter with the given name casted to the
   * specified data type.
   * 
   * @param sName
   *        Request parameter name
   * @param aDefault
   *        The default value to be returned, if no such parameter is present
   * @return The value of the passed request parameter
   */
  @Nullable
  protected static final <DATATYPE> DATATYPE getCastedAttr (@Nullable final String sName,
                                                            @Nullable final DATATYPE aDefault)
  {
    return getScope ().getCastedAttribute (sName, aDefault);
  }

  /**
   * Check if a request parameter with the given value is present.
   * 
   * @param sName
   *        The name of the request parameter.
   * @param sValue
   *        The expected value of the request parameter.
   * @return <code>true</code> if the request parameter is present and has the
   *         expected value - <code>false</code> otherwise.
   */
  protected static final boolean hasAttr (@Nullable final String sName, final String sValue)
  {
    return EqualsUtils.equals (sValue, getAttr (sName));
  }

  /**
   * Get the value of the checkbox of the request parameter with the given name.
   * 
   * @param sName
   *        Request parameter name
   * @param bDefaultValue
   *        the default value to be returned, if no request attribute is present
   * @return The value of the passed request parameter
   */
  protected static final boolean getCheckBoxAttr (@Nullable final String sName, final boolean bDefaultValue)
  {
    return StringHelper.hasNoText (sName) ? bDefaultValue : RequestFieldBoolean.getCheckBoxValue (sName, bDefaultValue);
  }

  @Nonnull
  protected static IRequestParamMap getRequestParamMap ()
  {
    return RequestHelper.getRequestParamMap (getScope ());
  }

  /**
   * @return A form that links to the current page.
   */
  @Nonnull
  protected static final HCForm createFormSelf ()
  {
    return new HCForm (LinkUtils.getSelfHref ());
  }

  /**
   * @return A file upload form that links to the current page.
   */
  @Nonnull
  protected static final HCForm createFormFileUploadSelf ()
  {
    return new HCForm_FileUpload (LinkUtils.getSelfHref ());
  }

  /**
   * Check some pre-requisites
   * 
   * @param aDisplayLocale
   *        Display locale. Never <code>null</code>.
   * @param aNodeList
   *        Node list to be filled. Never <code>null</code>.
   * @return Never <code>null</code>.
   */
  @OverrideOnDemand
  @Nonnull
  protected EValidity isValidToDisplayPage (@Nonnull final Locale aDisplayLocale, @Nonnull final HCNodeList aNodeList)
  {
    return EValidity.VALID;
  }

  /**
   * Abstract method to be implemented by subclasses, that creates the main page
   * content, without the help icon.
   * 
   * @param aDisplayLocale
   *        The display locale to be used for rendering the current page. Never
   *        <code>null</code>.
   * @param aNodeList
   *        The node list where the created nodes should be placed. Never
   *        <code>null</code>.
   */
  protected abstract void fillContent (@Nonnull Locale aDisplayLocale, @Nonnull HCNodeList aNodeList);

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
   * {@link #fillContent(Locale,HCNodeList)} method and creating the help node
   * if desired.
   */
  @Nonnull
  public final IHCNode getContent (@Nonnull final Locale aContentLocale)
  {
    final HCNodeList aNodeList = new HCNodeList ();

    if (isValidToDisplayPage (aContentLocale, aNodeList).isValid ())
    {
      // Create the main page content
      fillContent (aContentLocale, aNodeList);
    }

    // Is help available for this page?
    if (isHelpAvailable ())
    {
      // Add the help icon as the first child of the resulting node list
      aNodeList.addChild (0, getHelpIconNode (aContentLocale));
    }
    return aNodeList;
  }
}
