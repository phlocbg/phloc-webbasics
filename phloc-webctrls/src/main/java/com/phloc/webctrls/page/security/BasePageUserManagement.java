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
package com.phloc.webctrls.page.security;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.security.AccessManager;
import com.phloc.appbasics.security.CSecurity;
import com.phloc.appbasics.security.role.IRole;
import com.phloc.appbasics.security.user.IUser;
import com.phloc.appbasics.security.user.password.PasswordUtils;
import com.phloc.appbasics.security.usergroup.IUserGroup;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.email.EmailAddressUtils;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.name.ComparatorHasName;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.name.IHasDisplayTextWithArgs;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.datetime.format.PDTToString;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.AbstractHCCell;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCCheckBox;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCEM;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.hc.html.HCEditPassword;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.form.RequestField;
import com.phloc.webbasics.form.RequestFieldBoolean;
import com.phloc.webbasics.form.validation.FormErrors;
import com.phloc.webctrls.bootstrap.BootstrapButtonToolbar;
import com.phloc.webctrls.bootstrap.BootstrapFormLabel;
import com.phloc.webctrls.bootstrap.BootstrapTabBox;
import com.phloc.webctrls.bootstrap.BootstrapTable;
import com.phloc.webctrls.bootstrap.EBootstrapIcon;
import com.phloc.webctrls.bootstrap.derived.BootstrapButtonToolbarAdvanced;
import com.phloc.webctrls.bootstrap.derived.BootstrapErrorBox;
import com.phloc.webctrls.bootstrap.derived.BootstrapSuccessBox;
import com.phloc.webctrls.bootstrap.derived.BootstrapTableForm;
import com.phloc.webctrls.bootstrap.derived.BootstrapTableFormView;
import com.phloc.webctrls.bootstrap.ext.BootstrapDataTables;
import com.phloc.webctrls.datatables.DataTablesColumn;
import com.phloc.webctrls.page.AbstractWebPageForm;
import com.phloc.webctrls.security.SecurityUI;
import com.phloc.webctrls.security.UserGroupForUserSelect;

public class BasePageUserManagement extends AbstractWebPageForm <IUser>
{
  @Translatable
  protected static enum EText implements IHasDisplayText, IHasDisplayTextWithArgs
  {
    CREATE_NEW_USER ("Neuen Benutzer anlegen", "Create new user"),
    TAB_ACTIVE ("Aktive Benutzer ({0})", "Active users ({0})"),
    TAB_DISABLED ("Deaktivierte Benutzer ({0})", "Disabled users ({0})"),
    TAB_DELETED ("Gelöschte Benutzer ({0})", "Deleted users ({0})"),
    HEADER_NAME ("Name", "Name"),
    HEADER_EMAIL ("E-Mail", "Email"),
    HEADER_USERGROUPS ("Benutzergruppen", "User groups"),
    HEADER_VALUE ("Wert", "Value"),
    TITLE_RESET_PASSWORD ("Passwort von ''{0}'' zurücksetzen", "Reset password of user ''{0}''"),
    NO_USERS_FOUND ("Keine Benutzer gefunden", "No users found"),
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
    ERROR_PASSWORDS_DONT_MATCH ("Die Passwörter stimmen nicht überein!", "Passwords don't match"),
    ERROR_NO_USERGROUP ("Es muss mindestens eine Benutzergruppe ausgewählt werden!", "At least one user group must be selected!"),
    ERROR_INVALID_USERGROUPS ("Mindestens eine der angegebenen Benutzergruppen ist ungültig!", "At least one selected user group is invalid!"),
    SUCCESS_RESET_PASSWORD ("Das neue Passwort vom Benutzer ''{0}'' wurde gespeichert!", "Successfully saved the new password of user ''{0}''!");

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

  public static final boolean DEFAULT_ENABLED = true;
  private static final String FIELD_FIRSTNAME = "firstname";
  private static final String FIELD_LASTNAME = "lastname";
  private static final String FIELD_EMAILADDRESS = "emailaddress";
  private static final String FIELD_PASSWORD = "password";
  private static final String FIELD_PASSWORD_CONFIRM = "passwordconf";
  private static final String FIELD_ENABLED = "enabled";
  private static final String FIELD_USERGROUPS = "usergroups";

  private static final String ACTION_RESET_PASSWORD = "resetpw";

  private final Locale m_aDefaultUserLocale;

  public BasePageUserManagement (@Nonnull @Nonempty final String sID,
                                 @Nonnull @Nonempty final String sName,
                                 @Nullable final Locale aDefaultUserLocale)
  {
    super (sID, sName);
    m_aDefaultUserLocale = aDefaultUserLocale;
  }

  @Override
  @Nullable
  protected IUser getSelectedObject (@Nullable final String sID)
  {
    return AccessManager.getInstance ().getUserOfID (sID);
  }

  private static boolean _canEdit (@Nonnull final IUser aUser)
  {
    // Deleted users and the Administrator cannot be edited
    return !aUser.isDeleted () && !aUser.getID ().equals (CSecurity.USER_ADMINISTRATOR_ID);
  }

  private static boolean _canResetPassword (@Nonnull final IUser aUser)
  {
    return !aUser.isDeleted ();
  }

  /**
   * @param aCurrentUser
   *        The user currently shown
   * @param aCustomAttrs
   *        The available custom attributes
   * @param aTable
   *        The table to be add custom information
   * @param aDisplayLocale
   *        The display locale to use
   * @return A set of all attribute names that were handled in this method or
   *         <code>null</code>.
   */
  @Nullable
  @OverrideOnDemand
  protected Set <String> showCustomAttrsOfSelectedObject (@Nonnull final IUser aCurrentUser,
                                                          @Nonnull @Nonempty final Map <String, String> aCustomAttrs,
                                                          @Nonnull final BootstrapTableFormView aTable,
                                                          @Nonnull final Locale aDisplayLocale)
  {
    return null;
  }

  @Override
  protected void showSelectedObject (final IUser aSelectedObject,
                                     final Locale aDisplayLocale,
                                     final HCNodeList aNodeList)
  {
    final AccessManager aMgr = AccessManager.getInstance ();
    final BootstrapTableFormView aTable = aNodeList.addAndReturnChild (new BootstrapTableFormView (new HCCol (170),
                                                                                                   HCCol.star ()));
    aTable.setSpanningHeaderContent (EText.HEADER_DETAILS.getDisplayTextWithArgs (aDisplayLocale,
                                                                                  aSelectedObject.getDisplayName ()));
    aTable.addItemRow (BootstrapFormLabel.create (EText.LABEL_CREATIONDATE.getDisplayText (aDisplayLocale)),
                       PDTToString.getAsString (aSelectedObject.getCreationDateTime (), aDisplayLocale));
    if (aSelectedObject.getLastModificationDateTime () != null)
      aTable.addItemRow (BootstrapFormLabel.create (EText.LABEL_LASTMODIFICATIONDATE.getDisplayText (aDisplayLocale)),
                         PDTToString.getAsString (aSelectedObject.getLastModificationDateTime (), aDisplayLocale));
    if (aSelectedObject.getDeletionDateTime () != null)
      aTable.addItemRow (BootstrapFormLabel.create (EText.LABEL_DELETIONDATE.getDisplayText (aDisplayLocale)),
                         PDTToString.getAsString (aSelectedObject.getDeletionDateTime (), aDisplayLocale));
    aTable.addItemRow (BootstrapFormLabel.create (EText.LABEL_FIRSTNAME.getDisplayText (aDisplayLocale)),
                       aSelectedObject.getFirstName ());
    aTable.addItemRow (BootstrapFormLabel.create (EText.LABEL_LASTNAME.getDisplayText (aDisplayLocale)),
                       aSelectedObject.getLastName ());
    aTable.addItemRow (BootstrapFormLabel.create (EText.LABEL_EMAIL.getDisplayText (aDisplayLocale)),
                       createEmailLink (aSelectedObject.getEmailAddress ()));
    aTable.addItemRow (BootstrapFormLabel.create (EText.LABEL_ENABLED.getDisplayText (aDisplayLocale)),
                       EWebBasicsText.getYesOrNo (aSelectedObject.isEnabled (), aDisplayLocale));
    aTable.addItemRow (BootstrapFormLabel.create (EText.LABEL_DELETED.getDisplayText (aDisplayLocale)),
                       EWebBasicsText.getYesOrNo (aSelectedObject.isDeleted (), aDisplayLocale));

    // user groups
    final Collection <IUserGroup> aUserGroups = aMgr.getAllUserGroupsWithAssignedUser (aSelectedObject.getID ());
    if (aUserGroups.isEmpty ())
    {
      aTable.addItemRow (BootstrapFormLabel.create (EText.LABEL_USERGROUPS_0.getDisplayText (aDisplayLocale)),
                         HCEM.create (EText.NONE_DEFINED.getDisplayText (aDisplayLocale)));
    }
    else
    {
      final HCNodeList aUserGroupUI = new HCNodeList ();
      for (final IUserGroup aUserGroupe : ContainerHelper.getSorted (aUserGroups,
                                                                     new ComparatorHasName <IUserGroup> (aDisplayLocale)))
        aUserGroupUI.addChild (HCDiv.create (aUserGroupe.getName ()));
      aTable.addItemRow (BootstrapFormLabel.create (EText.LABEL_USERGROUPS_N.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                     Integer.toString (aUserGroups.size ()))),
                         aUserGroupUI);
    }

    // roles
    final Set <IRole> aUserRoles = aMgr.getAllUserRoles (aSelectedObject.getID ());
    if (aUserRoles.isEmpty ())
    {
      aTable.addItemRow (BootstrapFormLabel.create (EText.LABEL_ROLES_0.getDisplayText (aDisplayLocale)),
                         HCEM.create (EText.NONE_DEFINED.getDisplayText (aDisplayLocale)));
    }
    else
    {
      final HCNodeList aRoleUI = new HCNodeList ();
      for (final IRole aRole : ContainerHelper.getSorted (aUserRoles, new ComparatorHasName <IRole> (aDisplayLocale)))
        aRoleUI.addChild (HCDiv.create (aRole.getName ()));
      aTable.addItemRow (BootstrapFormLabel.create (EText.LABEL_ROLES_N.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                Integer.toString (aUserRoles.size ()))),
                         aRoleUI);
    }

    // custom attributes
    final Map <String, String> aCustomAttrs = aSelectedObject.getCustomAttrs ();
    if (!aCustomAttrs.isEmpty ())
    {
      final Set <String> aHandledAttrs = showCustomAttrsOfSelectedObject (aSelectedObject,
                                                                          aCustomAttrs,
                                                                          aTable,
                                                                          aDisplayLocale);

      final BootstrapTable aAttrTable = new BootstrapTable (new HCCol (170), HCCol.star ());
      aAttrTable.addHeaderRow ().addCells (EText.HEADER_NAME.getDisplayText (aDisplayLocale),
                                           EText.HEADER_VALUE.getDisplayText (aDisplayLocale));
      for (final Map.Entry <String, String> aEntry : aCustomAttrs.entrySet ())
      {
        final String sName = aEntry.getKey ();
        final String sValue = aEntry.getValue ();
        if (aHandledAttrs == null || !aHandledAttrs.contains (sName))
          aAttrTable.addBodyRow ().addCells (sName, sValue);
      }
      if (aAttrTable.getBodyRowCount () > 0)
        aTable.addItemRow (BootstrapFormLabel.create (EText.LABEL_ATTRIBUTES.getDisplayText (aDisplayLocale)),
                           aAttrTable);
    }
  }

  @Override
  protected void validateAndSaveInputParameters (final FormErrors aFormErrors,
                                                 final Locale aDisplayLocale,
                                                 final HCNodeList aNodeList,
                                                 final boolean bEdit,
                                                 final IUser aSelectedObject)
  {
    final AccessManager aAccessMgr = AccessManager.getInstance ();
    final String sFirstName = getAttr (FIELD_FIRSTNAME);
    final String sLastName = getAttr (FIELD_LASTNAME);
    final String sEmailAddress = getAttr (FIELD_EMAILADDRESS);
    final String sPassword = getAttr (FIELD_PASSWORD);
    final String sPasswordConf = getAttr (FIELD_PASSWORD_CONFIRM);
    final boolean bEnabled = getCheckBoxAttr (FIELD_ENABLED, DEFAULT_ENABLED);
    final List <String> aUserGroupIDs = getAttrs (FIELD_USERGROUPS);

    if (StringHelper.hasNoText (sLastName))
      aFormErrors.addFieldError (FIELD_LASTNAME, "Es muss ein Nachname angegeben werden!");
    if (StringHelper.hasNoText (sEmailAddress))
      aFormErrors.addFieldError (FIELD_EMAILADDRESS, "Es muss eine E-Mail-Addresse angegeben werden!");
    else
      if (!EmailAddressUtils.isValid (sEmailAddress))
        aFormErrors.addFieldError (FIELD_EMAILADDRESS, "Es muss eine gültige E-Mail-Addresse angegeben werden!");
      else
      {
        final IUser aSameLoginUser = aAccessMgr.getUserOfLoginName (sEmailAddress);
        if (aSameLoginUser != null)
          if (!bEdit || !aSameLoginUser.equals (aSelectedObject))
            aFormErrors.addFieldError (FIELD_EMAILADDRESS,
                                       "Ein anderer Benutzer mit dieser E-Mail-Addresse ist bereits registriert!");
      }
    if (!bEdit)
    {
      final List <String> aPasswordErrors = PasswordUtils.getPasswordConstraints ()
                                                         .getInvalidPasswordDescriptions (sPassword, aDisplayLocale);
      for (final String sPasswordError : aPasswordErrors)
        aFormErrors.addFieldError (FIELD_PASSWORD, sPasswordError);
      if (!EqualsUtils.equals (sPassword, sPasswordConf))
        aFormErrors.addFieldError (FIELD_PASSWORD_CONFIRM,
                                   EText.ERROR_PASSWORDS_DONT_MATCH.getDisplayText (aDisplayLocale));
    }
    if (ContainerHelper.isEmpty (aUserGroupIDs))
      aFormErrors.addFieldError (FIELD_USERGROUPS, EText.ERROR_NO_USERGROUP.getDisplayText (aDisplayLocale));
    else
      if (!aAccessMgr.containsAllUserGroupsWithID (aUserGroupIDs))
        aFormErrors.addFieldError (FIELD_USERGROUPS, EText.ERROR_INVALID_USERGROUPS.getDisplayText (aDisplayLocale));

    if (aFormErrors.isEmpty ())
    {
      // All fields are valid -> save
      if (bEdit)
      {
        final String sUserID = aSelectedObject.getID ();

        // We're editing an existing object
        aAccessMgr.setUserData (sUserID,
                                sEmailAddress,
                                sEmailAddress,
                                sFirstName,
                                sLastName,
                                m_aDefaultUserLocale,
                                aSelectedObject.getCustomAttrs (),
                                !bEnabled);
        aNodeList.addChild (BootstrapSuccessBox.create ("Der Benutzer wurde erfolgreich bearbeitet!"));

        // assign to the matching internal user groups
        final Collection <String> aPrevUserGroupIDs = aAccessMgr.getAllUserGroupIDsWithAssignedUser (sUserID);
        // Create all missing assignments
        final Set <String> aUserGroupsToBeAssigned = ContainerHelper.getDifference (aUserGroupIDs, aPrevUserGroupIDs);
        for (final String sUserGroupID : aUserGroupsToBeAssigned)
          aAccessMgr.assignUserToUserGroup (sUserGroupID, sUserID);

        // Delete all old assignments
        final Set <String> aUserGroupsToBeUnassigned = ContainerHelper.getDifference (aPrevUserGroupIDs, aUserGroupIDs);
        for (final String sUserGroupID : aUserGroupsToBeUnassigned)
          aAccessMgr.unassignUserFromUserGroup (sUserGroupID, sUserID);
      }
      else
      {
        // We're creating a new object
        final IUser aNewUser = aAccessMgr.createNewUser (sEmailAddress,
                                                         sEmailAddress,
                                                         sPassword,
                                                         sFirstName,
                                                         sLastName,
                                                         m_aDefaultUserLocale,
                                                         null,
                                                         !bEnabled);
        if (aNewUser != null)
        {
          aNodeList.addChild (BootstrapSuccessBox.create ("Der neue Benutzer wurde erfolgreich angelegt!"));

          // assign to the matching internal user groups
          for (final String sUserGroupID : aUserGroupIDs)
            aAccessMgr.assignUserToUserGroup (sUserGroupID, aNewUser.getID ());
        }
        else
          aNodeList.addChild (BootstrapErrorBox.create ("Fehler beim Anlegen des Benutzers!"));
      }
    }
  }

  @Override
  protected void showInputForm (final IUser aSelectedObject,
                                final Locale aDisplayLocale,
                                final HCForm aForm,
                                final boolean bEdit,
                                final boolean bCopy,
                                final FormErrors aFormErrors)
  {
    if (bEdit && !_canEdit (aSelectedObject))
      throw new IllegalStateException ("Won't work!");

    final AccessManager aMgr = AccessManager.getInstance ();
    final BootstrapTableForm aTable = aForm.addAndReturnChild (new BootstrapTableForm (new HCCol (210), HCCol.star ()));
    aTable.setSpanningHeaderContent (bEdit ? "Benutzer '" + aSelectedObject.getDisplayName () + "' bearbeiten"
                                          : "Neuen Benutzer anlegen");
    // Use the country of the current client as the default
    aTable.addItemRow (BootstrapFormLabel.create (EText.LABEL_FIRSTNAME.getDisplayText (aDisplayLocale)),
                       new HCEdit (new RequestField (FIELD_FIRSTNAME,
                                                     aSelectedObject == null ? null : aSelectedObject.getFirstName ())),
                       aFormErrors.getListOfField (FIELD_FIRSTNAME));
    aTable.addItemRow (BootstrapFormLabel.createMandatory (EText.LABEL_LASTNAME.getDisplayText (aDisplayLocale)),
                       new HCEdit (new RequestField (FIELD_LASTNAME,
                                                     aSelectedObject == null ? null : aSelectedObject.getLastName ())),
                       aFormErrors.getListOfField (FIELD_LASTNAME));
    aTable.addItemRow (BootstrapFormLabel.createMandatory (EText.LABEL_EMAIL.getDisplayText (aDisplayLocale)),
                       new HCEdit (new RequestField (FIELD_EMAILADDRESS,
                                                     aSelectedObject == null ? null
                                                                            : aSelectedObject.getEmailAddress ())),
                       aFormErrors.getListOfField (FIELD_EMAILADDRESS));
    if (!bEdit)
    {
      // Password is only shown on creation of a new user
      aTable.addItemRow (BootstrapFormLabel.createMandatory (EText.LABEL_PASSWORD.getDisplayText (aDisplayLocale)),
                         HCNodeList.create (new HCEditPassword (FIELD_PASSWORD),
                                            SecurityUI.createPasswordConstraintTip (aDisplayLocale).build ()),
                         aFormErrors.getListOfField (FIELD_PASSWORD));
      aTable.addItemRow (BootstrapFormLabel.createMandatory (EText.LABEL_PASSWORD_CONFIRM.getDisplayText (aDisplayLocale)),
                         HCNodeList.create (new HCEditPassword (FIELD_PASSWORD_CONFIRM),
                                            SecurityUI.createPasswordConstraintTip (aDisplayLocale).build ()),
                         aFormErrors.getListOfField (FIELD_PASSWORD_CONFIRM));
    }
    aTable.addItemRow (BootstrapFormLabel.createMandatory (EText.LABEL_ENABLED.getDisplayText (aDisplayLocale)),
                       new HCCheckBox (new RequestFieldBoolean (FIELD_ENABLED,
                                                                aSelectedObject == null ? DEFAULT_ENABLED
                                                                                       : aSelectedObject.isEnabled ())),
                       aFormErrors.getListOfField (FIELD_ENABLED));
    final Collection <String> aUserGroupIDs = aSelectedObject == null
                                                                     ? getAttrs (FIELD_USERGROUPS)
                                                                     : aMgr.getAllUserGroupIDsWithAssignedUser (aSelectedObject.getID ());
    aTable.addItemRow (BootstrapFormLabel.createMandatory (EText.LABEL_USERGROUPS_0.getDisplayText (aDisplayLocale)),
                       new UserGroupForUserSelect (new RequestField (FIELD_USERGROUPS), aDisplayLocale, aUserGroupIDs),
                       aFormErrors.getListOfField (FIELD_USERGROUPS));
  }

  @Override
  protected boolean handleCustomActions (@Nullable final IUser aSelectedObject,
                                         @Nonnull final Locale aDisplayLocale,
                                         @Nonnull final HCNodeList aNodeList)
  {
    if (hasAction (ACTION_RESET_PASSWORD) && aSelectedObject != null)
    {
      if (!_canResetPassword (aSelectedObject))
        throw new IllegalStateException ("Won't work!");

      final boolean bShowForm = true;
      final FormErrors aFormErrors = new FormErrors ();
      if (hasSubAction (ACTION_PERFORM))
      {
        final String sPlainTextPassword = getAttr (FIELD_PASSWORD);
        final String sPlainTextPasswordConfirm = getAttr (FIELD_PASSWORD_CONFIRM);

        final List <String> aPasswordErrors = PasswordUtils.getPasswordConstraints ()
                                                           .getInvalidPasswordDescriptions (sPlainTextPassword,
                                                                                            aDisplayLocale);
        for (final String sPasswordError : aPasswordErrors)
          aFormErrors.addFieldError (FIELD_PASSWORD, sPasswordError);
        if (!EqualsUtils.equals (sPlainTextPassword, sPlainTextPasswordConfirm))
          aFormErrors.addFieldError (FIELD_PASSWORD_CONFIRM,
                                     EText.ERROR_PASSWORDS_DONT_MATCH.getDisplayText (aDisplayLocale));

        if (aFormErrors.isEmpty ())
        {
          AccessManager.getInstance ().setUserPassword (aSelectedObject.getID (), sPlainTextPassword);
          aNodeList.addChild (BootstrapSuccessBox.create (EText.SUCCESS_RESET_PASSWORD.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                               aSelectedObject.getDisplayName ())));
          return true;
        }
      }
      if (bShowForm)
      {
        // Show input form
        final HCForm aForm = aNodeList.addAndReturnChild (createFormSelf ());
        final BootstrapTableForm aTable = aForm.addAndReturnChild (new BootstrapTableForm (new HCCol (200),
                                                                                           HCCol.star ()));
        aTable.setSpanningHeaderContent (EText.TITLE_RESET_PASSWORD.getDisplayTextWithArgs (aDisplayLocale,
                                                                                            aSelectedObject.getDisplayName ()));
        final String sPassword = EText.LABEL_PASSWORD.getDisplayText (aDisplayLocale);
        aTable.addItemRow (BootstrapFormLabel.create (sPassword),
                           HCNodeList.create (new HCEditPassword (FIELD_PASSWORD).setPlaceholder (sPassword),
                                              SecurityUI.createPasswordConstraintTip (aDisplayLocale).build ()),
                           aFormErrors.getListOfField (FIELD_PASSWORD));
        final String sPasswordConfirm = EText.LABEL_PASSWORD_CONFIRM.getDisplayText (aDisplayLocale);
        aTable.addItemRow (BootstrapFormLabel.create (sPasswordConfirm),
                           HCNodeList.create (new HCEditPassword (FIELD_PASSWORD_CONFIRM).setPlaceholder (sPasswordConfirm),
                                              SecurityUI.createPasswordConstraintTip (aDisplayLocale).build ()),
                           aFormErrors.getListOfField (FIELD_PASSWORD_CONFIRM));

        final BootstrapButtonToolbar aToolbar = aForm.addAndReturnChild (new BootstrapButtonToolbar ());
        aToolbar.addHiddenField (CHCParam.PARAM_ACTION, ACTION_RESET_PASSWORD);
        aToolbar.addHiddenField (CHCParam.PARAM_OBJECT, aSelectedObject.getID ());
        aToolbar.addHiddenField (CHCParam.PARAM_SUBACTION, ACTION_PERFORM);
        aToolbar.addSubmitButtonSave (aDisplayLocale);
      }
      return false;
    }
    return true;
  }

  @Nonnull
  protected IHCNode getTabWithUsers (@Nonnull final Locale aDisplayLocale,
                                     @Nonnull final Collection <? extends IUser> aUsers,
                                     @Nonnull @Nonempty final String sTableID)
  {
    final AccessManager aMgr = AccessManager.getInstance ();
    // List existing
    final BootstrapTable aTable = new BootstrapTable (new HCCol (200),
                                                      HCCol.star (),
                                                      new HCCol (150),
                                                      createActionCol (1)).setID (sTableID);
    aTable.addHeaderRow ().addCells (EText.HEADER_NAME.getDisplayText (aDisplayLocale),
                                     EText.HEADER_EMAIL.getDisplayText (aDisplayLocale),
                                     EText.HEADER_USERGROUPS.getDisplayText (aDisplayLocale),
                                     EWebBasicsText.MSG_ACTIONS.getDisplayText (aDisplayLocale));

    for (final IUser aCurUser : aUsers)
    {
      final ISimpleURL aViewLink = createViewURL (aCurUser);

      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (new HCA (aViewLink).addChild (aCurUser.getDisplayName ()));
      aRow.addCell (new HCA (aViewLink).addChild (aCurUser.getEmailAddress ()));

      // User groups
      final Collection <IUserGroup> aUserGroups = aMgr.getAllUserGroupsWithAssignedUser (aCurUser.getID ());
      final StringBuilder aUserGroupsStr = new StringBuilder ();
      for (final IUserGroup aUserGroup : ContainerHelper.getSorted (aUserGroups,
                                                                    new ComparatorHasName <IUserGroup> (aDisplayLocale)))
      {
        if (aUserGroupsStr.length () > 0)
          aUserGroupsStr.append (", ");
        aUserGroupsStr.append (aUserGroup.getName ());
      }
      aRow.addCell (new HCA (aViewLink).addChild (aUserGroupsStr.toString ()));

      final AbstractHCCell aActionCell = aRow.addCell ();

      // Edit user
      if (_canEdit (aCurUser))
        aActionCell.addChild (createEditLink (aCurUser, aDisplayLocale));
      else
        aActionCell.addChild (createEmptyAction ());

      // Reset password of user
      if (_canResetPassword (aCurUser))
      {
        aActionCell.addChild (new HCA (LinkUtils.getSelfHref ()
                                                .add (CHCParam.PARAM_ACTION, ACTION_RESET_PASSWORD)
                                                .add (CHCParam.PARAM_OBJECT, aCurUser.getID ())).setTitle (EText.TITLE_RESET_PASSWORD.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                                                                              aCurUser.getDisplayName ()))
                                                                                                .addChild (EBootstrapIcon.LOCK.getAsNode ()));
      }
    }
    if (aUsers.isEmpty ())
      aTable.addSpanningBodyContent (EText.NO_USERS_FOUND.getDisplayText (aDisplayLocale));

    final HCNodeList aNodeList = new HCNodeList ();
    aNodeList.addChild (aTable);
    if (!aUsers.isEmpty ())
      aNodeList.addChild (new BootstrapDataTables (aTable).setDisplayLocale (aDisplayLocale)
                                                          .addColumn (new DataTablesColumn (3).setSortable (false))
                                                          .setInitialSorting (1, ESortOrder.ASCENDING));
    return aNodeList;
  }

  @Override
  protected void showListOfExistingObjects (final Locale aDisplayLocale, final HCNodeList aNodeList)
  {
    // Toolbar on top
    final BootstrapButtonToolbarAdvanced aToolbar = aNodeList.addAndReturnChild (new BootstrapButtonToolbarAdvanced ());
    aToolbar.addButtonNew (EText.CREATE_NEW_USER.getDisplayText (aDisplayLocale), createCreateURL ());

    final BootstrapTabBox aTabBox = new BootstrapTabBox ();

    final AccessManager aMgr = AccessManager.getInstance ();

    final Collection <? extends IUser> aActiveUsers = aMgr.getAllActiveUsers ();
    aTabBox.addTab (EText.TAB_ACTIVE.getDisplayTextWithArgs (aDisplayLocale, Integer.toString (aActiveUsers.size ())),
                    getTabWithUsers (aDisplayLocale, aActiveUsers, getID () + "1"));

    final Collection <? extends IUser> aDisabledUsers = aMgr.getAllDisabledUsers ();
    aTabBox.addTab (EText.TAB_DISABLED.getDisplayTextWithArgs (aDisplayLocale,
                                                               Integer.toString (aDisabledUsers.size ())),
                    getTabWithUsers (aDisplayLocale, aDisabledUsers, getID () + "2"));

    final Collection <? extends IUser> aDeletedUsers = aMgr.getAllDeletedUsers ();
    aTabBox.addTab (EText.TAB_DELETED.getDisplayTextWithArgs (aDisplayLocale, Integer.toString (aDeletedUsers.size ())),
                    getTabWithUsers (aDisplayLocale, aDeletedUsers, getID () + "3"));
    aNodeList.addChild (aTabBox);
  }
}
