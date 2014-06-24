package com.phloc.webbasics.app;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.collections.attrs.IAttributeContainer;
import com.phloc.commons.url.SimpleURL;
import com.phloc.web.fileupload.IFileItem;
import com.phloc.web.servlet.request.IRequestParamMap;
import com.phloc.web.useragent.IUserAgent;
import com.phloc.web.useragent.browser.BrowserInfo;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

public interface ISimpleWebExecutionContext
{
  /**
   * @return The current request scope. Never <code>null</code>.
   */
  @Nonnull
  IRequestWebScopeWithoutResponse getRequestScope ();

  /**
   * @return The current display locale.
   */
  @Nonnull
  Locale getDisplayLocale ();

  /**
   * Check if the request parameter with the given name is present (independent
   * of the value).
   * 
   * @param sName
   *        Request parameter name
   * @return <code>true</code> of the attribute is present, <code>false</code>
   *         if not
   */
  boolean containsAttr (@Nullable String sName);

  /**
   * Get the value of the request parameter with the given name.
   * 
   * @param sName
   *        Request parameter name
   * @return The value of the passed request parameter
   */
  @Nullable
  String getAttr (@Nullable String sName);

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
  String getAttr (@Nullable String sName, @Nullable String sDefault);

  /**
   * Get the value list of the request parameter with the given name.
   * 
   * @param sName
   *        Request parameter name
   * @return The value list of the passed request parameter
   */
  @Nullable
  List <String> getAttrs (@Nullable String sName);

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
  boolean getBooleanAttr (@Nullable String sName, boolean bDefault);

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
  int getIntAttr (@Nullable String sName, int nDefault);

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
  long getLongAttr (@Nullable String sName, long nDefault);

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
  double getDoubleAttr (@Nullable String sName, double dDefault);

  /**
   * Get the value of the request parameter with the given name casted to the
   * specified data type.
   * 
   * @param sName
   *        Request parameter name
   * @return The value of the passed request parameter
   */
  @Nullable
  <DATATYPE> DATATYPE getCastedAttr (@Nullable String sName);

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
  <DATATYPE> DATATYPE getCastedAttr (@Nullable String sName, @Nullable DATATYPE aDefault);

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
  boolean hasAttr (@Nullable String sName, String sValue);

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

  @Nullable
  String getAction ();

  boolean hasAction (@Nullable String sAction);

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
