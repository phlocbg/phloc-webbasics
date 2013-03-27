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
package com.phloc.webdemoapp.ui;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.hc.html.HCEditPassword;
import com.phloc.html.hc.html.HCFieldSet;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCLabel;
import com.phloc.html.hc.html.HCLegend;
import com.phloc.html.js.EJSEvent;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSExpr;
import com.phloc.html.js.builder.JSPackage;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.login.CLogin;
import com.phloc.webctrls.bootstrap.BootstrapButton_Submit;
import com.phloc.webctrls.bootstrap.derived.BootstrapToolbar;
import com.phloc.webdemoapp.app.ajax.view.CDemoAppAjaxView;

@Immutable
public final class DemoAppAccessUI
{
  private DemoAppAccessUI ()
  {}

  @Nonnull
  public static HCForm createViewLoginForm (@Nonnull final Locale aDisplayLocale,
                                            @Nullable final String sPreselectedUserName,
                                            final boolean bFullUI)
  {
    // Use new IDs for both fields, in case the login stuff is displayed more
    // than once!
    final String sIDUserName = GlobalIDFactory.getNewStringID ();
    final String sIDPassword = GlobalIDFactory.getNewStringID ();
    final String sIDErrorField = GlobalIDFactory.getNewStringID ();

    final String sTextUserName = EWebBasicsText.EMAIL_ADDRESS.getDisplayText (aDisplayLocale);
    final String sTextPassword = EWebBasicsText.LOGIN_FIELD_PASSWORD.getDisplayText (aDisplayLocale);

    final HCForm aForm = new HCForm (LinkUtils.getSelfHref ());
    final HCFieldSet aFieldSet = aForm.addAndReturnChild (new HCFieldSet ());
    if (bFullUI)
    {
      aForm.addClass (CDemoAppCSS.CSS_CLASS_FORM_GROUPED);
      aFieldSet.addChild (new HCLegend ().addChild ("Login"));
    }

    // User name field
    aFieldSet.addChild (new HCLabel ().setFor (sIDUserName).addChild (sTextUserName));
    aFieldSet.addChild (new HCEdit (CLogin.REQUEST_ATTR_USERID, sPreselectedUserName).setID (sIDUserName)
                                                                                     .setPlaceholder (sTextUserName));

    // Password field
    aFieldSet.addChild (new HCLabel ().setFor (sIDPassword).addChild (sTextPassword));
    aFieldSet.addChild (new HCEditPassword (CLogin.REQUEST_ATTR_PASSWORD).setID (sIDPassword)
                                                                         .setPlaceholder (sTextPassword));

    // Placeholder for error message
    aFieldSet.addChild (new HCDiv ().setID (sIDErrorField));

    // Login button
    final BootstrapToolbar aToolbar = aFieldSet.addAndReturnChild (new BootstrapToolbar ());
    final JSPackage aOnClick = new JSPackage ();
    aOnClick.invoke (JSExpr.ref ("DemoApp"), "viewLogin")
            .arg (CDemoAppAjaxView.VIEW_LOGIN.getInvocationURI ())
            .arg (new JSAssocArray ().add (CLogin.REQUEST_ATTR_USERID, JQuery.idRef (sIDUserName).val ())
                                     .add (CLogin.REQUEST_ATTR_PASSWORD, JQuery.idRef (sIDPassword).val ()))
            .arg (sIDErrorField);
    aOnClick._return (false);
    aToolbar.addChild (new BootstrapButton_Submit ().addChild (EWebBasicsText.LOGIN_BUTTON_SUBMIT.getDisplayText (aDisplayLocale))
                                                    .setEventHandler (EJSEvent.ONCLICK, aOnClick));
    return aForm;
  }
}
