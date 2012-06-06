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
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.text.impl.ConstantTextProvider;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCForm_FileUpload;
import com.phloc.scopes.nonweb.domain.IRequestScope;
import com.phloc.scopes.web.mgr.WebScopeManager;
import com.phloc.webbasics.app.LinkUtils;

/**
 * Abstract base implementation for {@link IPage}.
 * 
 * @author philip
 */
public abstract class AbstractPage implements IPage
{
  private final String m_sID;
  private IHasDisplayText m_aName;

  /**
   * Constructor
   * 
   * @param sID
   *        The unique page ID. May not be <code>null</code>.
   */
  public AbstractPage (@Nonnull @Nonempty final String sID)
  {
    if (StringHelper.hasNoText (sID))
      throw new IllegalArgumentException ("ID");
    m_sID = sID;
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
  public AbstractPage (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    this (sID, new ConstantTextProvider (sName));
  }

  /**
   * Constructor
   * 
   * @param sID
   *        The unique page ID. May not be <code>null</code>.
   * @param aName
   *        The name of the page. May not be <code>null</code>.
   */
  public AbstractPage (@Nonnull @Nonempty final String sID, @Nonnull final IHasDisplayText aName)
  {
    this (sID);
    setName (aName);
  }

  /*
   * Get the unique page ID
   */
  @Nonnull
  @Nonempty
  public final String getID ()
  {
    return m_sID;
  }

  /**
   * Set the name of the page.
   * 
   * @param aName
   *        The multilingual name of the page. May not be <code>null</code>.
   */
  public final void setName (@Nonnull final IHasDisplayText aName)
  {
    if (aName == null)
      throw new NullPointerException ("name");
    m_aName = aName;
  }

  /*
   * Get the name of the page in the passed locale.
   */
  @Nullable
  public final String getDisplayText (@Nonnull final Locale aContentLocale)
  {
    return m_aName.getDisplayText (aContentLocale);
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
    return _getScope ().getAttributeAsString (sName);
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
    return _getScope ().getCastedAttribute (sName);
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

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ID", m_sID).append ("name", m_aName).toString ();
  }
}
