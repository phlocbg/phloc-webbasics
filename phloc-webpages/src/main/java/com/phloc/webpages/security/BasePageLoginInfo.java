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
package com.phloc.webpages.security;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.security.login.LoggedInUserManager;
import com.phloc.appbasics.security.login.LoginInfo;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.name.IHasDisplayTextWithArgs;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webbasics.form.validation.FormErrors;
import com.phloc.webpages.AbstractWebPageForm;

public class BasePageLoginInfo extends AbstractWebPageForm <LoginInfo>
{
  @Translatable
  protected static enum EText implements IHasDisplayText, IHasDisplayTextWithArgs
  {
    BUTTON_CREATE_NEW_USER ("Neuen Benutzer anlegen", "Create new user"),
    TAB_ACTIVE ("Aktive Benutzer ({0})", "Active users ({0})"),
    TAB_DISABLED ("Deaktivierte Benutzer ({0})", "Disabled users ({0})"),
    TAB_DELETED ("Gelöschte Benutzer ({0})", "Deleted users ({0})"),
    HEADER_NAME ("Name", "Name"),
    HEADER_EMAIL ("E-Mail", "Email"),
    HEADER_USERGROUPS ("Benutzergruppen", "User groups"),
    HEADER_VALUE ("Wert", "Value"),
    TITLE_CREATE ("Neuen Benutzer anlegen", "Create new user"),
    TITLE_EDIT ("Benutzer ''{0}'' bearbeiten", "Edit user ''{0}''"),
    TITLE_RESET_PASSWORD ("Passwort von ''{0}'' zurücksetzen", "Reset password of user ''{0}''"),
    NONE_DEFINED ("keine definiert", "none defined"),
    HEADER_DETAILS ("Details von Benutzer {0}", "Details of user {0}"),
    LABEL_CREATIONDATE ("Angelegt am", "Created on"),
    LABEL_LASTMODIFICATIONDATE ("Letzte Änderung am", "Last modification on"),
    LABEL_DELETIONDATE ("Gelöscht am", "Deleted on"),
    LABEL_FIRSTNAME ("Vorname", "First name"),
    LABEL_LASTNAME ("Nachname", "Last name"),
    LABEL_EMAIL ("E-Mail-Adresse", "Email address"),
    LABEL_PASSWORD ("Passwort", "Password"),
    LABEL_PASSWORD_CONFIRM ("Passwort (Bestätigung)", "Password (confirmation)"),
    LABEL_ENABLED ("Aktiv?", "Enabled?"),
    LABEL_DELETED ("Gelöscht?", "Deleted?"),
    LABEL_USERGROUPS_0 ("Benutzergruppen", "User groups"),
    LABEL_USERGROUPS_N ("Benutzergruppen ({0})", "User groups ({0})"),
    LABEL_ROLES_0 ("Rollen", "Roles"),
    LABEL_ROLES_N ("Rollen ({0})", "Roles ({0})"),
    LABEL_ATTRIBUTES ("Attribute", "Attributes"),
    ERROR_LASTNAME_REQUIRED ("Es muss ein Nachname angegeben werden!", "A last name must be specified!"),
    ERROR_EMAIL_REQUIRED ("Es muss eine E-Mail-Adresse angegeben werden!", "An email address must be specified!"),
    ERROR_EMAIL_INVALID ("Es muss eine gültige E-Mail-Adresse angegeben werden!", "A valid email address must be specified!"),
    ERROR_EMAIL_IN_USE ("Ein anderer Benutzer mit dieser E-Mail-Adresse existiert bereits!", "Another user with this email address already exists!"),
    ERROR_PASSWORDS_DONT_MATCH ("Die Passwörter stimmen nicht überein!", "Passwords don't match"),
    ERROR_NO_USERGROUP ("Es muss mindestens eine Benutzergruppe ausgewählt werden!", "At least one user group must be selected!"),
    ERROR_INVALID_USERGROUPS ("Mindestens eine der angegebenen Benutzergruppen ist ungültig!", "At least one selected user group is invalid!"),
    SUCCESS_CREATE ("Der neue Benutzer wurde erfolgreich angelegt!", "Successfully created the new user!"),
    SUCCESS_EDIT ("Der Benutzer wurde erfolgreich bearbeitet!", "Sucessfully edited the user!"),
    SUCCESS_RESET_PASSWORD ("Das neue Passwort vom Benutzer ''{0}'' wurde gespeichert!", "Successfully saved the new password of user ''{0}''!"),
    FAILURE_CREATE ("Fehler beim Anlegen des Benutzers!", "Error creating the new user!");

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

  public BasePageLoginInfo (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sName)
  {
    super (sID, sName);
  }

  public BasePageLoginInfo (@Nonnull @Nonempty final String sID,
                            @Nonnull final String sName,
                            @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageLoginInfo (@Nonnull @Nonempty final String sID,
                            @Nonnull final IReadonlyMultiLingualText aName,
                            @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @Override
  @Nullable
  protected LoginInfo getSelectedObject (final WebPageExecutionContext aWPEC, @Nullable final String sID)
  {
    return LoggedInUserManager.getInstance ().getLoginInfo (sID);
  }

  @Override
  protected final boolean isEditAllowed (@Nullable final LoginInfo aLoginInfo)
  {
    return false;
  }

  @Override
  protected void showSelectedObject (@Nonnull final WebPageExecutionContext aWPEC,
                                     @Nonnull final LoginInfo aSelectedObject)
  {}

  @Override
  protected void validateAndSaveInputParameters (@Nonnull final WebPageExecutionContext aWPEC,
                                                 @Nullable final LoginInfo aSelectedObject,
                                                 @Nonnull final FormErrors aFormErrors,
                                                 final boolean bEdit)
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  protected void showInputForm (@Nonnull final WebPageExecutionContext aWPEC,
                                @Nullable final LoginInfo aSelectedObject,
                                @Nonnull final HCForm aForm,
                                final boolean bEdit,
                                final boolean bCopy,
                                @Nonnull final FormErrors aFormErrors)
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  protected void showListOfExistingObjects (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final HCNodeList aNodeList = aWPEC.getNodeList ();
  }
}
