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
package com.phloc.webdemoapp.ui;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.bootstrap3.button.BootstrapButtonToolbar;
import com.phloc.bootstrap3.form.BootstrapForm;
import com.phloc.bootstrap3.form.EBootstrapFormType;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.hc.html.HCEditPassword;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSPackage;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.layout.LayoutExecutionContext;
import com.phloc.webbasics.login.CLogin;
import com.phloc.webdemoapp.app.ajax.view.CDemoAppAjaxView;

@Immutable
public final class DemoAppAccessUI
{
  private DemoAppAccessUI ()
  {}

  @Nonnull
  public static BootstrapForm createViewLoginForm (@Nonnull final LayoutExecutionContext aLEC,
                                                   @Nullable final String sPreselectedUserName,
                                                   final boolean bFullUI)
  {
    final Locale aDisplayLocale = aLEC.getDisplayLocale ();

    // Use new IDs for both fields, in case the login stuff is displayed more
    // than once!
    final String sIDUserName = GlobalIDFactory.getNewStringID ();
    final String sIDPassword = GlobalIDFactory.getNewStringID ();
    final String sIDErrorField = GlobalIDFactory.getNewStringID ();

    final BootstrapForm aForm = new BootstrapForm (aLEC.getSelfHref (), bFullUI ? EBootstrapFormType.HORIZONTAL
                                                                                : EBootstrapFormType.DEFAULT);

    // User name field
    aForm.addFormGroup (EWebBasicsText.EMAIL_ADDRESS.getDisplayText (aDisplayLocale),
                        new HCEdit (CLogin.REQUEST_ATTR_USERID, sPreselectedUserName).setID (sIDUserName));

    // Password field
    aForm.addFormGroup (EWebBasicsText.LOGIN_FIELD_PASSWORD.getDisplayText (aDisplayLocale),
                        new HCEditPassword (CLogin.REQUEST_ATTR_PASSWORD).setID (sIDPassword));

    // Placeholder for error message
    aForm.addChild (new HCDiv ().setID (sIDErrorField));

    // Login button
    final BootstrapButtonToolbar aToolbar = aForm.addAndReturnChild (new BootstrapButtonToolbar ());
    final JSPackage aOnClick = new JSPackage ();
    aOnClick.add (DemoAppJS.viewLogin ()
                           .arg (CDemoAppAjaxView.LOGIN.getInvocationURI ())
                           .arg (new JSAssocArray ().add (CLogin.REQUEST_ATTR_USERID, JQuery.idRef (sIDUserName).val ())
                                                    .add (CLogin.REQUEST_ATTR_PASSWORD,
                                                          JQuery.idRef (sIDPassword).val ()))
                           .arg (sIDErrorField));
    aOnClick._return (false);
    aToolbar.addSubmitButton (EWebBasicsText.LOGIN_BUTTON_SUBMIT.getDisplayText (aDisplayLocale), aOnClick);
    return aForm;
  }
}
