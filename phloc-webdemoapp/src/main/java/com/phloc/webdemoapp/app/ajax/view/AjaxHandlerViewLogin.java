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
package com.phloc.webdemoapp.app.ajax.view;

import java.util.Locale;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.ApplicationRequestManager;
import com.phloc.appbasics.security.login.ELoginResult;
import com.phloc.appbasics.security.login.LoggedInUserManager;
import com.phloc.bootstrap3.alert.Bootstrap3ErrorBox;
import com.phloc.commons.GlobalDebug;
import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.json2.impl.JsonObject;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.ajax.AbstractAjaxHandler;
import com.phloc.webbasics.ajax.AjaxDefaultResponse;
import com.phloc.webbasics.ajax.IAjaxResponse;
import com.phloc.webbasics.login.CLogin;
import com.phloc.webdemoapp.app.CDemoAppSecurity;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

public final class AjaxHandlerViewLogin extends AbstractAjaxHandler
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AjaxHandlerViewLogin.class);
  private static final String JSON_LOGGEDIN = "loggedin";
  private static final String JSON_HTML = "html";

  @Override
  @Nonnull
  protected IAjaxResponse mainHandleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                             @Nonnull final MapBasedAttributeContainer aParams) throws Exception
  {
    final String sLoginName = aParams.getAttributeAsString (CLogin.REQUEST_ATTR_USERID);
    final String sPassword = aParams.getAttributeAsString (CLogin.REQUEST_ATTR_PASSWORD);

    // Main login
    final ELoginResult eLoginResult = LoggedInUserManager.getInstance ()
                                                         .loginUser (sLoginName,
                                                                     sPassword,
                                                                     CDemoAppSecurity.REQUIRED_ROLE_IDS_VIEW);
    if (eLoginResult.isSuccess ())
      return AjaxDefaultResponse.createSuccess (new JsonObject ().add (JSON_LOGGEDIN, true));

    // Get the rendered content of the menu area
    if (GlobalDebug.isDebugMode ())
      s_aLogger.warn ("Login of '" + sLoginName + "' failed because " + eLoginResult);

    final Locale aDisplayLocale = ApplicationRequestManager.getInstance ().getRequestDisplayLocale ();
    final IHCNode aRoot = Bootstrap3ErrorBox.create (EWebBasicsText.LOGIN_ERROR_MSG.getDisplayText (aDisplayLocale) +
                                                     " " +
                                                     eLoginResult.getDisplayText (aDisplayLocale));

    // Set as result property
    return AjaxDefaultResponse.createSuccess (new JsonObject ().add (JSON_LOGGEDIN, false)
                                                               .add (JSON_HTML, HCSettings.getAsHTMLString (aRoot)));
  }
}
