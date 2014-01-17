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
package com.phloc.webpages.security;

import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.security.AccessManager;
import com.phloc.appbasics.security.login.LoggedInUserManager;
import com.phloc.appbasics.security.password.GlobalPasswordSettings;
import com.phloc.appbasics.security.user.IUser;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.name.IHasDisplayTextWithArgs;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCEditPassword;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.validation.error.FormErrors;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webctrls.custom.ELabelType;
import com.phloc.webctrls.custom.table.IHCTableForm;
import com.phloc.webctrls.custom.toolbar.IButtonToolbar;
import com.phloc.webctrls.security.SecurityUI;
import com.phloc.webpages.AbstractWebPageExt;
import com.phloc.webpages.EWebPageText;

/**
 * Base page for changing the password of the currently logged in use.
 * 
 * @author Philip Helger
 */
public class BasePageChangePassword extends AbstractWebPageExt
{
  @Translatable
  protected static enum EText implements IHasDisplayText, IHasDisplayTextWithArgs
  {
    ERROR_NO_USER_PRESENT ("Es ist kein Benutzer angemeldet, daher kann auch das Passwort nicht geändert werden.", "Since no user is logged in no password change is possible."),
    TITLE ("Passwort von ''{0}'' ändern", "Change password of ''{0}''"),
    LABEL_PASSWORD ("Passwort", "Password"),
    LABEL_PASSWORD_CONFIRM ("Passwort (Bestätigung)", "Password (confirmation)"),
    ERROR_PASSWORDS_DONT_MATCH ("Die Passwörter stimmen nicht überein!", "Passwords don't match"),
    SUCCESS_CHANGE_PW ("Das Passwort wurde erfolgreich geändert!", "Sucessfully changed the password!");

    private final ITextProvider m_aTP;

    private EText (final String sDE, final String sEN)
    {
      m_aTP = TextProvider.create_DE_EN (sDE, sEN);
    }

    @Nullable
    public String getDisplayText (@Nonnull final Locale aContentLocale)
    {
      return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
    }

    @Nullable
    public String getDisplayTextWithArgs (@Nonnull final Locale aContentLocale, @Nullable final Object... aArgs)
    {
      return DefaultTextResolver.getTextWithArgs (this, m_aTP, aContentLocale, aArgs);
    }
  }

  public static final String FIELD_PASSWORD = "password";
  public static final String FIELD_PASSWORD_CONFIRM = "passwordconf";

  public BasePageChangePassword (@Nonnull @Nonempty final String sID)
  {
    super (sID, EWebPageText.PAGE_NAME_CHANGE_PASSWORD.getAsMLT ());
  }

  public BasePageChangePassword (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sName)
  {
    super (sID, sName);
  }

  public BasePageChangePassword (@Nonnull @Nonempty final String sID,
                                 @Nonnull final String sName,
                                 @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageChangePassword (@Nonnull @Nonempty final String sID,
                                 @Nonnull final IReadonlyMultiLingualText aName,
                                 @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @Override
  protected void fillContent (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    final IUser aCurrentUser = LoggedInUserManager.getInstance ().getCurrentUser ();
    if (aCurrentUser == null)
    {
      aNodeList.addChild (getStyler ().createErrorBox (EText.ERROR_NO_USER_PRESENT.getDisplayText (aDisplayLocale)));
    }
    else
    {
      final boolean bShowForm = true;
      final FormErrors aFormErrors = new FormErrors ();
      if (aWPEC.hasAction (ACTION_PERFORM))
      {
        final String sPlainTextPassword = aWPEC.getAttr (FIELD_PASSWORD);
        final String sPlainTextPasswordConfirm = aWPEC.getAttr (FIELD_PASSWORD_CONFIRM);

        final List <String> aPasswordErrors = GlobalPasswordSettings.getPasswordConstraintList ()
                                                                    .getInvalidPasswordDescriptions (sPlainTextPassword,
                                                                                                     aDisplayLocale);
        for (final String sPasswordError : aPasswordErrors)
          aFormErrors.addFieldError (FIELD_PASSWORD, sPasswordError);
        if (!EqualsUtils.equals (sPlainTextPassword, sPlainTextPasswordConfirm))
          aFormErrors.addFieldError (FIELD_PASSWORD_CONFIRM,
                                     EText.ERROR_PASSWORDS_DONT_MATCH.getDisplayText (aDisplayLocale));

        if (aFormErrors.isEmpty ())
        {
          AccessManager.getInstance ().setUserPassword (aCurrentUser.getID (), sPlainTextPassword);
          aNodeList.addChild (getStyler ().createSuccessBox (EText.SUCCESS_CHANGE_PW.getDisplayText (aDisplayLocale)));
        }
      }
      if (bShowForm)
      {
        // Show input form
        final boolean bHasAnyPasswordConstraint = GlobalPasswordSettings.getPasswordConstraintList ().hasConstraints ();
        final HCForm aForm = aNodeList.addAndReturnChild (createFormSelf ());
        final IHCTableForm <?> aTable = aForm.addAndReturnChild (getStyler ().createTableForm (new HCCol (200),
                                                                                               HCCol.star (),
                                                                                               new HCCol (20)));

        aTable.setSpanningHeaderContent (EText.TITLE.getDisplayTextWithArgs (aDisplayLocale,
                                                                             SecurityUI.getUserDisplayName (aCurrentUser,
                                                                                                            aDisplayLocale)));

        final String sPassword = EText.LABEL_PASSWORD.getDisplayText (aDisplayLocale);
        aTable.createItemRow ()
              .setLabel (sPassword, bHasAnyPasswordConstraint ? ELabelType.MANDATORY : ELabelType.OPTIONAL)
              .setCtrl (new HCEditPassword (FIELD_PASSWORD).setPlaceholder (sPassword))
              .setNote (SecurityUI.createPasswordConstraintTip (aDisplayLocale))
              .setErrorList (aFormErrors.getListOfField (FIELD_PASSWORD));

        final String sPasswordConfirm = EText.LABEL_PASSWORD_CONFIRM.getDisplayText (aDisplayLocale);
        aTable.createItemRow ()
              .setLabel (sPasswordConfirm, bHasAnyPasswordConstraint ? ELabelType.MANDATORY : ELabelType.OPTIONAL)
              .setCtrl (new HCEditPassword (FIELD_PASSWORD_CONFIRM).setPlaceholder (sPasswordConfirm))
              .setNote (SecurityUI.createPasswordConstraintTip (aDisplayLocale))
              .setErrorList (aFormErrors.getListOfField (FIELD_PASSWORD_CONFIRM));

        final IButtonToolbar <?> aToolbar = aForm.addAndReturnChild (getStyler ().createToolbar ());
        aToolbar.addHiddenField (CHCParam.PARAM_ACTION, CHCParam.ACTION_PERFORM);
        aToolbar.addSubmitButtonSave (aDisplayLocale);
      }
    }
  }
}
