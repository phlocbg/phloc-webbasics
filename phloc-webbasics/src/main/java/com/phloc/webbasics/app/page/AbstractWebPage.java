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

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.page.AbstractPage;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCForm_FileUpload;
import com.phloc.scopes.nonweb.domain.IRequestScope;
import com.phloc.scopes.web.mgr.WebScopeManager;
import com.phloc.webbasics.app.LinkUtils;

/**
 * Abstract base implementation for {@link IWebPage}.
 * 
 * @author philip
 */
public abstract class AbstractWebPage extends AbstractPage implements IWebPage
{
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
  public AbstractWebPage (@Nonnull @Nonempty final String sID, @Nonnull final IHasDisplayText aName)
  {
    super (sID, aName);
  }

  @Nonnull
  private static IRequestScope _getScope ()
  {
    return WebScopeManager.getRequestScope ();
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
    final String sScopeValue = _getScope ().getAttributeAsString (sName, sDefault);
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
    return _getScope ().getAttributeValues (sName);
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
    return _getScope ().getAttributeAsInt (sName, nDefault);
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
    return _getScope ().getAttributeAsDouble (sName, dDefault);
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
    return _getScope ().getCastedAttribute (sName, aDefault);
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
  protected static final boolean hasAttr (final String sName, final String sValue)
  {
    return EqualsUtils.equals (sValue, getAttr (sName));
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
}
