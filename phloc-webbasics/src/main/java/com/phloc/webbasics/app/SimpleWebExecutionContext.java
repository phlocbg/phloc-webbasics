/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.webbasics.app;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.collections.attrs.IAttributeContainer;
import com.phloc.commons.collections.attrs.IReadonlyAttributeContainer;
import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.url.SimpleURL;
import com.phloc.html.hc.CHCParam;
import com.phloc.web.fileupload.IFileItem;
import com.phloc.web.servlet.request.IRequestParamMap;
import com.phloc.web.servlet.request.RequestHelper;
import com.phloc.web.useragent.IUserAgent;
import com.phloc.web.useragent.UserAgentDatabase;
import com.phloc.web.useragent.browser.BrowserInfo;
import com.phloc.webbasics.form.RequestFieldBoolean;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * This object is instantiated per page view and contains the current request
 * scope, the display locale and a set of custom attributes.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class SimpleWebExecutionContext
{
  private final IRequestWebScopeWithoutResponse m_aRequestScope;
  private final Locale m_aDisplayLocale;
  private final MapBasedAttributeContainer m_aCustomAttrs = new MapBasedAttributeContainer ();

  public SimpleWebExecutionContext (@Nonnull final SimpleWebExecutionContext aSWEC)
  {
    this (aSWEC.getRequestScope (), aSWEC.getDisplayLocale (), aSWEC.getCustomAttrs ());
  }

  public SimpleWebExecutionContext (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                    @Nonnull final Locale aDisplayLocale)
  {
    this (aRequestScope, aDisplayLocale, (IReadonlyAttributeContainer) null);
  }

  public SimpleWebExecutionContext (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                    @Nonnull final Locale aDisplayLocale,
                                    @Nullable final IReadonlyAttributeContainer aCustomAttrs)
  {
    m_aRequestScope = ValueEnforcer.notNull (aRequestScope, "RequestScope");
    m_aDisplayLocale = ValueEnforcer.notNull (aDisplayLocale, "DisplayLocale");
    if (aCustomAttrs != null)
      m_aCustomAttrs.setAttributes (aCustomAttrs);
  }

  @Nonnull
  public IRequestWebScopeWithoutResponse getRequestScope ()
  {
    return m_aRequestScope;
  }

  @Nonnull
  public Locale getDisplayLocale ()
  {
    return m_aDisplayLocale;
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
  public boolean containsAttr (@Nullable final String sName)
  {
    return m_aRequestScope.containsAttribute (sName);
  }

  /**
   * Get the value of the request parameter with the given name.
   * 
   * @param sName
   *        Request parameter name
   * @return The value of the passed request parameter
   */
  @Nullable
  public String getAttr (@Nullable final String sName)
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
  public String getAttr (@Nullable final String sName, @Nullable final String sDefault)
  {
    final String sScopeValue = m_aRequestScope.getAttributeAsString (sName, sDefault);
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
  public List <String> getAttrs (@Nullable final String sName)
  {
    return m_aRequestScope.getAttributeValues (sName);
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
  public boolean getBooleanAttr (@Nullable final String sName, final boolean bDefault)
  {
    return m_aRequestScope.getAttributeAsBoolean (sName, bDefault);
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
  public int getIntAttr (@Nullable final String sName, final int nDefault)
  {
    return m_aRequestScope.getAttributeAsInt (sName, nDefault);
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
  public long getLongAttr (@Nullable final String sName, final long nDefault)
  {
    return m_aRequestScope.getAttributeAsLong (sName, nDefault);
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
  public double getDoubleAttr (@Nullable final String sName, final double dDefault)
  {
    return m_aRequestScope.getAttributeAsDouble (sName, dDefault);
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
  public <DATATYPE> DATATYPE getCastedAttr (@Nullable final String sName)
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
  public <DATATYPE> DATATYPE getCastedAttr (@Nullable final String sName, @Nullable final DATATYPE aDefault)
  {
    return m_aRequestScope.getCastedAttribute (sName, aDefault);
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
  public boolean hasAttr (@Nullable final String sName, final String sValue)
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
  public boolean getCheckBoxAttr (@Nullable final String sName, final boolean bDefaultValue)
  {
    return getCheckBoxAttrStatic (sName, bDefaultValue);
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
  public static boolean getCheckBoxAttrStatic (@Nullable final String sName, final boolean bDefaultValue)
  {
    return StringHelper.hasNoText (sName) ? bDefaultValue : RequestFieldBoolean.getCheckBoxValue (sName, bDefaultValue);
  }

  /**
   * Get the uploaded file with the specified request parameter.
   * 
   * @param sName
   *        The parameter name.
   * @return <code>null</code> if no such uploaded file is present.
   * @throws ClassCastException
   *         if the passed request parameter is not a file
   */
  @Nullable
  public IFileItem getFileItem (@Nullable final String sName)
  {
    return m_aRequestScope.getAttributeAsFileItem (sName);
  }

  @Nonnull
  public IRequestParamMap getRequestParamMap ()
  {
    return m_aRequestScope.getRequestParamMap ();
  }

  @Nullable
  public String getAction ()
  {
    return getAttr (CHCParam.PARAM_ACTION);
  }

  public boolean hasAction (@Nullable final String sAction)
  {
    return hasAttr (CHCParam.PARAM_ACTION, sAction);
  }

  public boolean hasSubAction (@Nullable final String sSubAction)
  {
    return hasAttr (CHCParam.PARAM_SUBACTION, sSubAction);
  }

  @Nonnull
  public IUserAgent getUserAgent ()
  {
    return UserAgentDatabase.getUserAgent (m_aRequestScope.getRequest ());
  }

  @Nonnull
  public BrowserInfo getBrowserInfo ()
  {
    return getUserAgent ().getBrowserInfo ();
  }

  /**
   * @return The custom attributes for this execution context. Never
   *         <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableObject (reason = "Design")
  public IAttributeContainer getCustomAttrs ()
  {
    return m_aCustomAttrs;
  }

  /**
   * Get the URL of the specified menu it.
   * 
   * @param sMenuItemID
   *        The ID of the menu item to link to. May not be <code>null</code>.
   * @return The non-<code>null</code> URL to the specified menu item.
   */
  @Nonnull
  public SimpleURL getLinkToMenuItem (@Nonnull final String sMenuItemID)
  {
    return LinkUtils.getLinkToMenuItem (m_aRequestScope, sMenuItemID);
  }

  /**
   * Get the URL of the specified menu it.
   * 
   * @param aParams
   *        The optional request parameters to be used. May be <code>null</code>
   *        or empty.
   * @param sMenuItemID
   *        The ID of the menu item to link to. May not be <code>null</code>.
   * @return The non-<code>null</code> URL to the specified menu item with the
   *         passed parameters.
   */
  @Nonnull
  public SimpleURL getLinkToMenuItem (@Nonnull final String sMenuItemID, @Nullable final Map <String, String> aParams)
  {
    return getLinkToMenuItem (sMenuItemID).addAll (aParams);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("requestURL", RequestHelper.getURI (m_aRequestScope.getRequest ()))
                                       .append ("displayLocale", m_aDisplayLocale)
                                       .append ("customAttrs", m_aCustomAttrs)
                                       .toString ();
  }
}
