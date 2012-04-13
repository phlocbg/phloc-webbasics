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
package com.phloc.webbasics.login;

import javax.annotation.concurrent.Immutable;

import com.phloc.css.DefaultCSSClassProvider;
import com.phloc.css.ICSSClassProvider;

/**
 * Login constants
 * 
 * @author philip
 */
@Immutable
public final class CLogin
{
  public static final String LAYOUT_AREAID_LOGIN = "login";

  public static final ICSSClassProvider CSS_CLASS_LOGIN_APPLOGO = DefaultCSSClassProvider.create ("login_applogo");
  public static final ICSSClassProvider CSS_CLASS_LOGIN_HEADER = DefaultCSSClassProvider.create ("login_appheader");
  public static final ICSSClassProvider CSS_CLASS_LOGIN_ERRORMSG = DefaultCSSClassProvider.create ("login_errormsg");

  public static final String REQUEST_ATTR_USERID = "userid";
  public static final String REQUEST_ATTR_PASSWORD = "password";

  private CLogin ()
  {}
}
