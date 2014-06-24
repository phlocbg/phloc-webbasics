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

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.appbasics.app.menu.IMenuTree;
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
public class SimpleWebExecutionContext implements ISimpleWebExecutionContext
{
  private final IRequestWebScopeWithoutResponse m_aRequestScope;
  private final Locale m_aDisplayLocale;
  private final IMenuTree m_aMenuTree;
  private final MapBasedAttributeContainer m_aCustomAttrs = new MapBasedAttributeContainer ();
  @SuppressWarnings ("unused")
  private final String m_sRequestParamNameLocale;
  private final String m_sRequestParamNameMenuItem;

  public SimpleWebExecutionContext (@Nonnull final ISimpleWebExecutionContext aSWEC)
  {
    this (aSWEC.getRequestScope (), aSWEC.getDisplayLocale (), aSWEC.getMenuTree (), aSWEC.getCustomAttrs ());
  }

  public SimpleWebExecutionContext (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                    @Nonnull final Locale aDisplayLocale,
                                    @Nonnull final IMenuTree aMenuTree)
  {
    this (aRequestScope, aDisplayLocale, aMenuTree, (IReadonlyAttributeContainer) null);
  }

  public SimpleWebExecutionContext (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                    @Nonnull final Locale aDisplayLocale,
                                    @Nonnull final IMenuTree aMenuTree,
                                    @Nullable final IReadonlyAttributeContainer aCustomAttrs)
  {
    m_aRequestScope = ValueEnforcer.notNull (aRequestScope, "RequestScope");
    m_aDisplayLocale = ValueEnforcer.notNull (aDisplayLocale, "DisplayLocale");
    m_aMenuTree = ValueEnforcer.notNull (aMenuTree, "MenuTree");
    if (aCustomAttrs != null)
      m_aCustomAttrs.setAttributes (aCustomAttrs);

    final ApplicationRequestManager aARM = ApplicationRequestManager.getInstance ();
    m_sRequestParamNameLocale = aARM.getRequestParamNameLocale ();
    m_sRequestParamNameMenuItem = aARM.getRequestParamNameMenuItem ();
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

  @Nonnull
  public IMenuTree getMenuTree ()
  {
    return m_aMenuTree;
  }

  public boolean containsAttr (@Nullable final String sName)
  {
    return m_aRequestScope.containsAttribute (sName);
  }

  @Nullable
  public String getAttr (@Nullable final String sName)
  {
    return getAttr (sName, null);
  }

  @Nullable
  public String getAttr (@Nullable final String sName, @Nullable final String sDefault)
  {
    final String sScopeValue = m_aRequestScope.getAttributeAsString (sName, sDefault);
    return StringHelper.trim (sScopeValue);
  }

  @Nullable
  public List <String> getAttrs (@Nullable final String sName)
  {
    return m_aRequestScope.getAttributeValues (sName);
  }

  public boolean getBooleanAttr (@Nullable final String sName, final boolean bDefault)
  {
    return m_aRequestScope.getAttributeAsBoolean (sName, bDefault);
  }

  public int getIntAttr (@Nullable final String sName, final int nDefault)
  {
    return m_aRequestScope.getAttributeAsInt (sName, nDefault);
  }

  public long getLongAttr (@Nullable final String sName, final long nDefault)
  {
    return m_aRequestScope.getAttributeAsLong (sName, nDefault);
  }

  public double getDoubleAttr (@Nullable final String sName, final double dDefault)
  {
    return m_aRequestScope.getAttributeAsDouble (sName, dDefault);
  }

  @Nullable
  public <DATATYPE> DATATYPE getCastedAttr (@Nullable final String sName)
  {
    return getCastedAttr (sName, (DATATYPE) null);
  }

  @Nullable
  public <DATATYPE> DATATYPE getCastedAttr (@Nullable final String sName, @Nullable final DATATYPE aDefault)
  {
    return m_aRequestScope.getCastedAttribute (sName, aDefault);
  }

  public boolean hasAttr (@Nullable final String sName, final String sValue)
  {
    return EqualsUtils.equals (sValue, getAttr (sName));
  }

  public boolean getCheckBoxAttr (@Nullable final String sName, final boolean bDefaultValue)
  {
    return getCheckBoxAttrStatic (sName, bDefaultValue);
  }

  public static boolean getCheckBoxAttrStatic (@Nullable final String sName, final boolean bDefaultValue)
  {
    return StringHelper.hasNoText (sName) ? bDefaultValue : RequestFieldBoolean.getCheckBoxValue (sName, bDefaultValue);
  }

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

  @Nonnull
  @ReturnsMutableObject (reason = "Design")
  public IAttributeContainer getCustomAttrs ()
  {
    return m_aCustomAttrs;
  }

  @Nonnull
  public SimpleURL getLinkToMenuItem (@Nonnull final String sMenuItemID)
  {
    final String sPath = m_aRequestScope.encodeURL (m_aRequestScope.getFullContextAndServletPath ());
    return new SimpleURL (sPath).add (m_sRequestParamNameMenuItem, sMenuItemID);
  }

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
                                       .append ("menuTree", m_aMenuTree)
                                       .append ("customAttrs", m_aCustomAttrs)
                                       .toString ();
  }
}
