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

import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.commons.collections.attrs.IAttributeContainer;
import com.phloc.commons.collections.attrs.IReadonlyAttributeContainer;
import com.phloc.commons.url.SimpleURL;
import com.phloc.html.hc.CHCParam;
import com.phloc.web.fileupload.IFileItem;
import com.phloc.web.servlet.request.IRequestParamMap;
import com.phloc.web.useragent.IUserAgent;
import com.phloc.web.useragent.browser.BrowserInfo;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

public interface ISimpleWebExecutionContext extends IReadonlyAttributeContainer
{
  /**
   * @return The current request scope. Never <code>null</code>.
   */
  @Nonnull
  IRequestWebScopeWithoutResponse getRequestScope ();

  /**
   * @return The current display locale. Base on the item of the request
   *         manager.
   */
  @Nonnull
  Locale getDisplayLocale ();

  /**
   * @return The current menu tree. Based on the menu tree of the request
   *         manager.
   */
  @Nonnull
  IMenuTree getMenuTree ();

  /**
   * Use {@link #getAttributeAsString(String)} instead
   * 
   * @param sName
   *        Attribute name
   * @return <code>null</code> if no such attribute exists,
   */
  @Deprecated
  String getAttr (@Nullable String sName);

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
  @Deprecated
  boolean hasAttr (@Nullable String sName, @Nullable String sValue);

  // The following 4 methods are copied from IRequestScope

  /**
   * Get a list of all attribute values with the same name.
   * 
   * @param sName
   *        The name of the attribute to query.
   * @return <code>null</code> if no such attribute value exists
   */
  @Nullable
  List <String> getAttributeValues (@Nullable String sName);

  /**
   * Get a list of all attribute values with the same name.
   * 
   * @param sName
   *        The name of the attribute to query.
   * @param aDefault
   *        The default value to be returned, if no such attribute is present.
   * @return <code>adefault</code> if no such attribute value exists
   */
  @Nullable
  List <String> getAttributeValues (@Nullable String sName, @Nullable List <String> aDefault);

  /**
   * Check if a attribute with the given name is present in the request and has
   * the specified value.
   * 
   * @param sName
   *        The name of the attribute to check
   * @param sDesiredValue
   *        The value to be matched
   * @return <code>true</code> if an attribute with the given name is present
   *         and has the desired value
   */
  boolean hasAttributeValue (@Nullable String sName, @Nullable String sDesiredValue);

  /**
   * Check if a attribute with the given name is present in the request and has
   * the specified value. If no such attribute is present, the passed default
   * value is returned.
   * 
   * @param sName
   *        The name of the attribute to check
   * @param sDesiredValue
   *        The value to be matched
   * @param bDefault
   *        the default value to be returned, if the specified attribute is not
   *        present
   * @return <code>true</code> if an attribute with the given name is present
   *         and has the desired value, <code>false</code> if the attribute is
   *         present but has a different value. If the attribute is not present,
   *         the default value is returned.
   */
  boolean hasAttributeValue (@Nullable String sName, @Nullable String sDesiredValue, boolean bDefault);

  /**
   * Get the value of the checkbox of the request parameter with the given name.
   * 
   * @param sName
   *        Request parameter name
   * @param bDefaultValue
   *        the default value to be returned, if no request attribute is present
   * @return The value of the passed request parameter
   */
  boolean getCheckBoxAttr (@Nullable String sName, boolean bDefaultValue);

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
  IFileItem getFileItem (@Nullable String sName);

  IRequestParamMap getRequestParamMap ();

  /**
   * @return The special request parameter value of
   *         {@link CHCParam#PARAM_ACTION}. May be <code>null</code>.
   */
  @Nullable
  String getAction ();

  boolean hasAction (@Nullable String sAction);

  /**
   * @return The special request parameter value of
   *         {@link CHCParam#PARAM_SUBACTION}. May be <code>null</code>.
   */
  @Nullable
  String getSubAction ();

  boolean hasSubAction (@Nullable String sSubAction);

  IUserAgent getUserAgent ();

  BrowserInfo getBrowserInfo ();

  /**
   * @return The custom attributes for this execution context. Never
   *         <code>null</code>.
   */
  IAttributeContainer getCustomAttrs ();

  /**
   * Get the URL of the specified menu it.
   * 
   * @param sMenuItemID
   *        The ID of the menu item to link to. May not be <code>null</code>.
   * @return The non-<code>null</code> URL to the specified menu item.
   */
  SimpleURL getLinkToMenuItem (String sMenuItemID);

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
  SimpleURL getLinkToMenuItem (String sMenuItemID, @Nullable Map <String, String> aParams);

}
