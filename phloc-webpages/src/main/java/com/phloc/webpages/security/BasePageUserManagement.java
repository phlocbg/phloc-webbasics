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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.security.AccessManager;
import com.phloc.appbasics.security.password.GlobalPasswordSettings;
import com.phloc.appbasics.security.role.IRole;
import com.phloc.appbasics.security.user.IUser;
import com.phloc.appbasics.security.usergroup.IUserGroup;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.email.EmailAddressUtils;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.name.ComparatorHasName;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.name.IHasDisplayTextWithArgs;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.datetime.format.PDTToString;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.IHCCell;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCTable;
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
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.validation.error.FormErrors;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webbasics.form.RequestField;
import com.phloc.webbasics.form.RequestFieldBoolean;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webctrls.custom.ELabelType;
import com.phloc.webctrls.custom.tabbox.ITabBox;
import com.phloc.webctrls.custom.table.IHCTableForm;
import com.phloc.webctrls.custom.table.IHCTableFormView;
import com.phloc.webctrls.custom.toolbar.IButtonToolbar;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webctrls.security.SecurityUI;
import com.phloc.webpages.AbstractWebPageForm;
import com.phloc.webpages.EWebPageText;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class BasePageUserManagement extends AbstractWebPageForm <IUser>
{
  @Translatable
  protected static enum EText implements IHasDisplayText, IHasDisplayTextWithArgs
  {
    BUTTON_CREATE_NEW_USER ("Neuen Benutzer anlegen", "Create new user"),
    TAB_ACTIVE ("Aktive Benutzer ({0})", "Active users ({0})"),
    TAB_DISABLED ("Deaktivierte Benutzer ({0})", "Disabled users ({0})"),
    TAB_DELETED ("Gelöschte Benutzer ({0})", "Deleted users ({0})"),
    HEADER_NAME ("Name", "Name"),
    HEADER_LOGINNAME ("Benutzername", "User name"),
    HEADER_EMAIL ("E-Mail", "Email"),
    HEADER_USERGROUPS ("Benutzergruppen", "User groups"),
    HEADER_VALUE ("Wert", "Value"),
    TITLE_CREATE ("Neuen Benutzer anlegen", "Create new user"),
    TITLE_EDIT ("Benutzer ''{0}'' bearbeiten", "Edit user ''{0}''"),
    TITLE_RESET_PASSWORD ("Passwort von ''{0}'' zurücksetzen", "Reset password of user ''{0}''"),
    NONE_DEFINED ("keine definiert", "none defined"),
    HEADER_DETAILS ("Details von Benutzer ''{0}''", "Details of user ''{0}''"),
    LABEL_CREATIONDATE ("Angelegt am", "Created on"),
    LABEL_LASTMODIFICATIONDATE ("Letzte Änderung am", "Last modification on"),
    LABEL_DELETIONDATE ("Gelöscht am", "Deleted on"),
    LABEL_LOGINNAME ("Benutzername", "User name"),
    LABEL_FIRSTNAME ("Vorname", "First name"),
    LABEL_LASTNAME ("Nachname", "Last name"),
    LABEL_EMAIL ("E-Mail-Adresse", "Email address"),
    LABEL_PASSWORD ("Passwort", "Password"),
    LABEL_PASSWORD_CONFIRM ("Passwort (Bestätigung)", "Password (confirmation)"),
    LABEL_ENABLED ("Aktiv?", "Enabled?"),
    LABEL_DELETED ("Gelöscht?", "Deleted?"),
    LABEL_LAST_LOGIN ("Letzter Login", "Last login"),
    LABEL_LAST_LOGIN_NEVER ("noch nie", "never"),
    LABEL_LOGIN_COUNT ("Login-Anzahl", "Login count"),
    LABEL_CONSECUTIVE_FAILED_LOGIN_COUNT ("Fehlgeschlagene Logins", "Failed logins"),
    LABEL_USERGROUPS_0 ("Benutzergruppen", "User groups"),
    LABEL_USERGROUPS_N ("Benutzergruppen ({0})", "User groups ({0})"),
    LABEL_ROLES_0 ("Rollen", "Roles"),
    LABEL_ROLES_N ("Rollen ({0})", "Roles ({0})"),
    LABEL_ATTRIBUTES ("Attribute", "Attributes"),
    ERROR_LOGINNAME_REQUIRED ("Es muss ein Benutzername angegeben werden!", "A user name must be specified!"),
    ERROR_LASTNAME_REQUIRED ("Es muss ein Nachname angegeben werden!", "A last name must be specified!"),
    ERROR_EMAIL_REQUIRED ("Es muss eine E-Mail-Adresse angegeben werden!", "An email address must be specified!"),
    ERROR_EMAIL_INVALID ("Es muss eine gültige E-Mail-Adresse angegeben werden!", "A valid email address must be specified!"),
    ERROR_EMAIL_IN_USE ("Ein anderer Benutzer mit dieser E-Mail-Adresse existiert bereits!", "Another user with this email address already exists!"),
    ERROR_PASSWORDS_DONT_MATCH ("Die Passwörter stimmen nicht überein!", "Passwords don't match"),
    ERROR_NO_USERGROUP ("Es muss mindestens eine Benutzergruppe ausgewählt werden!", "At least one user group must be selected!"),
    ERROR_INVALID_USERGROUPS ("Mindestens eine der angegebenen Benutzergruppen ist ungültig!", "At least one selected user group is invalid!"),
    SUCCESS_CREATE ("Der neue Benutzer wurde erfolgreich angelegt!", "Successfully created the new user!"),
    SUCCESS_EDIT ("Der Benutzer wurde erfolgreich bearbeitet!", "Sucessfully edited the user!"),
    FAILURE_CREATE ("Fehler beim Anlegen des Benutzers!", "Error creating the new user!"),
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
  public static final String FIELD_FIRSTNAME = "firstname";
  public static final String FIELD_LASTNAME = "lastname";
  public static final String FIELD_LOGINNAME = "loginname";
  public static final String FIELD_EMAILADDRESS = "emailaddress";
  public static final String FIELD_PASSWORD = "password";
  public static final String FIELD_PASSWORD_CONFIRM = "passwordconf";
  public static final String FIELD_ENABLED = "enabled";
  public static final String FIELD_USERGROUPS = "usergroups";

  public static final String ACTION_RESET_PASSWORD = "resetpw";

  private Locale m_aDefaultUserLocale;

  public BasePageUserManagement (@Nonnull @Nonempty final String sID)
  {
    super (sID, EWebPageText.PAGE_NAME_SECURITY_USERS.getAsMLT ());
  }

  public BasePageUserManagement (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sName)
  {
    super (sID, sName);
  }

  public BasePageUserManagement (@Nonnull @Nonempty final String sID,
                                 @Nonnull final String sName,
                                 @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageUserManagement (@Nonnull @Nonempty final String sID,
                                 @Nonnull final IReadonlyMultiLingualText aName,
                                 @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @Nonnull
  public BasePageUserManagement setDefaultUserLocale (@Nullable final Locale aDefaultUserLocale)
  {
    m_aDefaultUserLocale = aDefaultUserLocale;
    return this;
  }

  /**
   * Override this method to determine if the email address should be used as
   * login name or not.
   * 
   * @return <code>true</code> by default
   */
  @OverrideOnDemand
  protected boolean useEmailAddressAsLoginName ()
  {
    return true;
  }

  @OverrideOnDemand
  protected boolean isLastNameMandatory ()
  {
    return true;
  }

  @OverrideOnDemand
  protected boolean isEmailMandatory ()
  {
    return true;
  }

  @Override
  @Nullable
  protected IUser getSelectedObject (final WebPageExecutionContext aWPEC, @Nullable final String sID)
  {
    return AccessManager.getInstance ().getUserOfID (sID);
  }

  @Override
  protected final boolean isEditAllowed (@Nullable final IUser aUser)
  {
    return SecurityUIHelper.canBeEdited (aUser);
  }

  /**
   * Check if the password of a user can be reset or not. Currently the
   * passwords of all not deleted users can be reset.
   * 
   * @param aUser
   *        The user to check. May be <code>null</code>.
   * @return <code>true</code> if the password can be reset, <code>false</code>
   *         if not.
   */
  protected final boolean canResetPassword (@Nonnull final IUser aUser)
  {
    return SecurityUIHelper.canResetPassword (aUser);
  }

  /**
   * Callback for manually extracting custom attributes. This method is called
   * independently if custom attributes are present or not.
   * 
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
                                                          @Nonnull final Map <String, ?> aCustomAttrs,
                                                          @Nonnull final IHCTableFormView <?> aTable,
                                                          @Nonnull final Locale aDisplayLocale)
  {
    return null;
  }

  /**
   * @param aTable
   *        Table to be filled.
   * @param aSelectedObject
   *        Current user.
   * @param aDisplayLocale
   *        Display locale to use.
   */
  @OverrideOnDemand
  protected void onShowSelectedObjectTableStart (@Nonnull final IHCTableFormView <?> aTable,
                                                 @Nonnull final IUser aSelectedObject,
                                                 @Nonnull final Locale aDisplayLocale)
  {
    aTable.createItemRow ()
          .setLabel (EText.LABEL_CREATIONDATE.getDisplayText (aDisplayLocale))
          .setCtrl (PDTToString.getAsString (aSelectedObject.getCreationDateTime (), aDisplayLocale));
    if (aSelectedObject.getLastModificationDateTime () != null)
      aTable.createItemRow ()
            .setLabel (EText.LABEL_LASTMODIFICATIONDATE.getDisplayText (aDisplayLocale))
            .setCtrl (PDTToString.getAsString (aSelectedObject.getLastModificationDateTime (), aDisplayLocale));
    if (aSelectedObject.getDeletionDateTime () != null)
      aTable.createItemRow ()
            .setLabel (EText.LABEL_DELETIONDATE.getDisplayText (aDisplayLocale))
            .setCtrl (PDTToString.getAsString (aSelectedObject.getDeletionDateTime (), aDisplayLocale));
  }

  /**
   * @param aTable
   *        Table to be filled.
   * @param aSelectedObject
   *        Current user.
   * @param aDisplayLocale
   *        Display locale to use.
   */
  @OverrideOnDemand
  protected void onShowSelectedObjectTableEnd (@Nonnull final IHCTableFormView <?> aTable,
                                               @Nonnull final IUser aSelectedObject,
                                               @Nonnull final Locale aDisplayLocale)
  {}

  @Override
  @SuppressFBWarnings ("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
  protected void showSelectedObject (@Nonnull final WebPageExecutionContext aWPEC, final IUser aSelectedObject)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final AccessManager aMgr = AccessManager.getInstance ();
    final IHCTableFormView <?> aTable = aNodeList.addAndReturnChild (getStyler ().createTableFormView (new HCCol (170),
                                                                                                       HCCol.star ()));
    aTable.setSpanningHeaderContent (EText.HEADER_DETAILS.getDisplayTextWithArgs (aDisplayLocale,
                                                                                  aSelectedObject.getDisplayName ()));
    onShowSelectedObjectTableStart (aTable, aSelectedObject, aDisplayLocale);
    if (!useEmailAddressAsLoginName ())
    {
      aTable.createItemRow ()
            .setLabel (EText.LABEL_LOGINNAME.getDisplayText (aDisplayLocale))
            .setCtrl (aSelectedObject.getLoginName ());
    }
    aTable.createItemRow ()
          .setLabel (EText.LABEL_FIRSTNAME.getDisplayText (aDisplayLocale))
          .setCtrl (aSelectedObject.getFirstName ());
    aTable.createItemRow ()
          .setLabel (EText.LABEL_LASTNAME.getDisplayText (aDisplayLocale))
          .setCtrl (aSelectedObject.getLastName ());
    aTable.createItemRow ()
          .setLabel (EText.LABEL_EMAIL.getDisplayText (aDisplayLocale))
          .setCtrl (getStyler ().createEmailLink (aSelectedObject.getEmailAddress ()));
    aTable.createItemRow ()
          .setLabel (EText.LABEL_ENABLED.getDisplayText (aDisplayLocale))
          .setCtrl (EWebBasicsText.getYesOrNo (aSelectedObject.isEnabled (), aDisplayLocale));
    aTable.createItemRow ()
          .setLabel (EText.LABEL_DELETED.getDisplayText (aDisplayLocale))
          .setCtrl (EWebBasicsText.getYesOrNo (aSelectedObject.isDeleted (), aDisplayLocale));
    aTable.createItemRow ()
          .setLabel (EText.LABEL_LAST_LOGIN.getDisplayText (aDisplayLocale))
          .setCtrl (aSelectedObject.getLastLoginDateTime () != null ? new HCTextNode (PDTToString.getAsString (aSelectedObject.getLastLoginDateTime (),
                                                                                                               aDisplayLocale))
                                                                   : HCEM.create (EText.LABEL_LAST_LOGIN_NEVER.getDisplayText (aDisplayLocale)));
    aTable.createItemRow ()
          .setLabel (EText.LABEL_LOGIN_COUNT.getDisplayText (aDisplayLocale))
          .setCtrl (Integer.toString (aSelectedObject.getLoginCount ()));
    aTable.createItemRow ()
          .setLabel (EText.LABEL_CONSECUTIVE_FAILED_LOGIN_COUNT.getDisplayText (aDisplayLocale))
          .setCtrl (Integer.toString (aSelectedObject.getConsecutiveFailedLoginCount ()));

    // user groups
    final Collection <IUserGroup> aUserGroups = aMgr.getAllUserGroupsWithAssignedUser (aSelectedObject.getID ());
    if (aUserGroups.isEmpty ())
    {
      aTable.createItemRow ()
            .setLabel (EText.LABEL_USERGROUPS_0.getDisplayText (aDisplayLocale))
            .setCtrl (HCEM.create (EText.NONE_DEFINED.getDisplayText (aDisplayLocale)));
    }
    else
    {
      final HCNodeList aUserGroupUI = new HCNodeList ();
      for (final IUserGroup aUserGroup : ContainerHelper.getSorted (aUserGroups,
                                                                    new ComparatorHasName <IUserGroup> (aDisplayLocale)))
        aUserGroupUI.addChild (HCDiv.create (aUserGroup.getName ()));
      aTable.createItemRow ()
            .setLabel (EText.LABEL_USERGROUPS_N.getDisplayTextWithArgs (aDisplayLocale,
                                                                        Integer.toString (aUserGroups.size ())))
            .setCtrl (aUserGroupUI);
    }

    // roles
    final Set <IRole> aUserRoles = aMgr.getAllUserRoles (aSelectedObject.getID ());
    if (aUserRoles.isEmpty ())
    {
      aTable.createItemRow ()
            .setLabel (EText.LABEL_ROLES_0.getDisplayText (aDisplayLocale))
            .setCtrl (HCEM.create (EText.NONE_DEFINED.getDisplayText (aDisplayLocale)));
    }
    else
    {
      final HCNodeList aRoleUI = new HCNodeList ();
      for (final IRole aRole : ContainerHelper.getSorted (aUserRoles, new ComparatorHasName <IRole> (aDisplayLocale)))
        aRoleUI.addChild (HCDiv.create (aRole.getName ()));
      aTable.createItemRow ()
            .setLabel (EText.LABEL_ROLES_N.getDisplayTextWithArgs (aDisplayLocale,
                                                                   Integer.toString (aUserRoles.size ())))
            .setCtrl (aRoleUI);
    }

    // custom attributes
    final Map <String, Object> aCustomAttrs = aSelectedObject.getAllAttributes ();

    // Callback
    final Set <String> aHandledAttrs = showCustomAttrsOfSelectedObject (aSelectedObject,
                                                                        aCustomAttrs,
                                                                        aTable,
                                                                        aDisplayLocale);

    if (!aCustomAttrs.isEmpty ())
    {
      final IHCTable <?> aAttrTable = getStyler ().createTable (new HCCol (170), HCCol.star ());
      aAttrTable.addHeaderRow ().addCells (EText.HEADER_NAME.getDisplayText (aDisplayLocale),
                                           EText.HEADER_VALUE.getDisplayText (aDisplayLocale));
      for (final Map.Entry <String, Object> aEntry : aCustomAttrs.entrySet ())
      {
        final String sName = aEntry.getKey ();
        if (aHandledAttrs == null || !aHandledAttrs.contains (sName))
        {
          final String sValue = String.valueOf (aEntry.getValue ());
          aAttrTable.addBodyRow ().addCells (sName, sValue);
        }
      }

      // Maybe all custom attributes where handled in
      // showCustomAttrsOfSelectedObject
      if (aAttrTable.hasBodyRows ())
        aTable.createItemRow ().setLabel (EText.LABEL_ATTRIBUTES.getDisplayText (aDisplayLocale)).setCtrl (aAttrTable);
    }

    // Callback
    onShowSelectedObjectTableEnd (aTable, aSelectedObject, aDisplayLocale);
  }

  /**
   * Validate custom data of the input field.
   * 
   * @param aWPEC
   *        Current web page execution context. Never <code>null</code>.
   * @param aSelectedObject
   *        The selected user. May be <code>null</code>.
   * @param aFormErrors
   *        The form errors to be filled. Never <code>null</code>.
   * @param bEdit
   *        <code>true</code> if we're in edit mode
   * @return The custom parameter to be added to the used upon success. If an
   *         error occurred, this map may be <code>null</code>.
   */
  @OverrideOnDemand
  @Nullable
  protected Map <String, String> validateCustomParameters (@Nonnull final WebPageExecutionContext aWPEC,
                                                           @Nullable final IUser aSelectedObject,
                                                           @Nonnull final FormErrors aFormErrors,
                                                           final boolean bEdit)
  {
    return null;
  }

  @Override
  @SuppressWarnings ("null")
  protected void validateAndSaveInputParameters (@Nonnull final WebPageExecutionContext aWPEC,
                                                 @Nullable final IUser aSelectedObject,
                                                 @Nonnull final FormErrors aFormErrors,
                                                 final boolean bEdit)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final boolean bIsAdministrator = aSelectedObject != null && aSelectedObject.isAdministrator ();
    final AccessManager aAccessMgr = AccessManager.getInstance ();
    String sLoginName = aWPEC.getAttr (FIELD_LOGINNAME);
    final String sFirstName = aWPEC.getAttr (FIELD_FIRSTNAME);
    final String sLastName = aWPEC.getAttr (FIELD_LASTNAME);
    final String sEmailAddress = aWPEC.getAttr (FIELD_EMAILADDRESS);
    final String sPassword = aWPEC.getAttr (FIELD_PASSWORD);
    final String sPasswordConf = aWPEC.getAttr (FIELD_PASSWORD_CONFIRM);
    final boolean bEnabled = bIsAdministrator ? true : aWPEC.getCheckBoxAttr (FIELD_ENABLED, DEFAULT_ENABLED);
    final Collection <String> aUserGroupIDs = bIsAdministrator ? aAccessMgr.getAllUserGroupIDsWithAssignedUser (aSelectedObject.getID ())
                                                              : aWPEC.getAttrs (FIELD_USERGROUPS);

    if (useEmailAddressAsLoginName ())
    {
      sLoginName = sEmailAddress;
    }
    else
    {
      if (StringHelper.hasNoText (sLoginName))
        aFormErrors.addFieldError (FIELD_LOGINNAME, EText.ERROR_LOGINNAME_REQUIRED.getDisplayText (aDisplayLocale));
    }

    if (StringHelper.hasNoText (sLastName))
    {
      if (isLastNameMandatory ())
        aFormErrors.addFieldError (FIELD_LASTNAME, EText.ERROR_LASTNAME_REQUIRED.getDisplayText (aDisplayLocale));
    }

    if (StringHelper.hasNoText (sEmailAddress))
    {
      if (isEmailMandatory ())
        aFormErrors.addFieldError (FIELD_EMAILADDRESS, EText.ERROR_EMAIL_REQUIRED.getDisplayText (aDisplayLocale));
    }
    else
      if (!EmailAddressUtils.isValid (sEmailAddress))
        aFormErrors.addFieldError (FIELD_EMAILADDRESS, EText.ERROR_EMAIL_INVALID.getDisplayText (aDisplayLocale));
      else
      {
        final IUser aSameLoginUser = aAccessMgr.getUserOfLoginName (sEmailAddress);
        if (aSameLoginUser != null)
          if (!bEdit || !aSameLoginUser.equals (aSelectedObject))
            aFormErrors.addFieldError (FIELD_EMAILADDRESS, EText.ERROR_EMAIL_IN_USE.getDisplayText (aDisplayLocale));
      }

    if (!bEdit)
    {
      final List <String> aPasswordErrors = GlobalPasswordSettings.getPasswordConstraintList ()
                                                                  .getInvalidPasswordDescriptions (sPassword,
                                                                                                   aDisplayLocale);
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

    // Call custom method
    final Map <String, String> aCustomAttrMap = validateCustomParameters (aWPEC, aSelectedObject, aFormErrors, bEdit);

    if (aFormErrors.isEmpty ())
    {
      // All fields are valid -> save
      if (bEdit)
      {
        final String sUserID = aSelectedObject.getID ();

        final Map <String, Object> aAttrMap = aSelectedObject.getAllAttributes ();
        if (aCustomAttrMap != null)
          aAttrMap.putAll (aCustomAttrMap);

        // We're editing an existing object
        aAccessMgr.setUserData (sUserID,
                                sLoginName,
                                sEmailAddress,
                                sFirstName,
                                sLastName,
                                m_aDefaultUserLocale,
                                aAttrMap,
                                !bEnabled);
        aNodeList.addChild (getStyler ().createSuccessBox (EText.SUCCESS_EDIT.getDisplayText (aDisplayLocale)));

        // assign to the matching user groups
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
        final IUser aNewUser = aAccessMgr.createNewUser (sLoginName,
                                                         sEmailAddress,
                                                         sPassword,
                                                         sFirstName,
                                                         sLastName,
                                                         m_aDefaultUserLocale,
                                                         aCustomAttrMap,
                                                         !bEnabled);
        if (aNewUser != null)
        {
          aNodeList.addChild (getStyler ().createSuccessBox (EText.SUCCESS_CREATE.getDisplayText (aDisplayLocale)));

          // assign to the matching internal user groups
          for (final String sUserGroupID : aUserGroupIDs)
            aAccessMgr.assignUserToUserGroup (sUserGroupID, aNewUser.getID ());
        }
        else
          aNodeList.addChild (getStyler ().createErrorBox (EText.FAILURE_CREATE.getDisplayText (aDisplayLocale)));
      }
    }
  }

  /**
   * Modifiy the user group selector, e.g. for adding JS event handlers.
   * 
   * @param aSelect
   *        The user group selector. Never <code>null</code>.
   */
  @OverrideOnDemand
  protected void showInputFormModifyUserGroupSelect (@Nonnull final UserGroupForUserSelect aSelect)
  {}

  /**
   * Add details after the regular show form.
   * 
   * @param aWPEC
   *        The web page execution context. Never <code>null</code>.
   * @param aSelectedObject
   *        The currently selected object. May be <code>null</code> for newly
   *        created objects.
   * @param aForm
   *        The parent form. Use this as parent and not the node list from the
   *        web page execution context! Never <code>null</code>.
   * @param bEdit
   *        <code>true</code> if edit mode
   * @param bCopy
   *        <code>true</code> if copy mode
   * @param aFormErrors
   *        Previous errors from validation. Never <code>null</code> but maybe
   *        empty.
   * @param aTable
   *        The table where new fields should be added. Never <code>null</code>.
   */
  @OverrideOnDemand
  protected void showInputFormEnd (@Nonnull final WebPageExecutionContext aWPEC,
                                   @Nullable final IUser aSelectedObject,
                                   @Nonnull final HCForm aForm,
                                   final boolean bEdit,
                                   final boolean bCopy,
                                   @Nonnull final FormErrors aFormErrors,
                                   @Nonnull final IHCTableForm <?> aTable)
  {}

  @Override
  protected void showInputForm (@Nonnull final WebPageExecutionContext aWPEC,
                                @Nullable final IUser aSelectedObject,
                                @Nonnull final HCForm aForm,
                                final boolean bEdit,
                                final boolean bCopy,
                                @Nonnull final FormErrors aFormErrors)
  {
    if (bEdit && !isEditAllowed (aSelectedObject))
      throw new IllegalStateException ("Won't work!");

    final boolean bIsAdministrator = aSelectedObject != null && aSelectedObject.isAdministrator ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final AccessManager aMgr = AccessManager.getInstance ();
    final IHCTableForm <?> aTable = aForm.addAndReturnChild (getStyler ().createTableForm (new HCCol (170),
                                                                                           HCCol.star (),
                                                                                           new HCCol (20)));
    aTable.setSpanningHeaderContent (bEdit ? EText.TITLE_EDIT.getDisplayTextWithArgs (aDisplayLocale,
                                                                                      aSelectedObject.getDisplayName ())
                                          : EText.TITLE_CREATE.getDisplayText (aDisplayLocale));

    if (!useEmailAddressAsLoginName ())
    {
      final String sLoginName = EText.LABEL_LOGINNAME.getDisplayText (aDisplayLocale);
      aTable.createItemRow ()
            .setLabelMandatory (sLoginName)
            .setCtrl (new HCEdit (new RequestField (FIELD_LOGINNAME,
                                                    aSelectedObject == null ? null : aSelectedObject.getLoginName ())).setPlaceholder (sLoginName))
            .setErrorList (aFormErrors.getListOfField (FIELD_LOGINNAME));
    }

    {
      final String sFirstName = EText.LABEL_FIRSTNAME.getDisplayText (aDisplayLocale);
      aTable.createItemRow ()
            .setLabel (sFirstName)
            .setCtrl (new HCEdit (new RequestField (FIELD_FIRSTNAME,
                                                    aSelectedObject == null ? null : aSelectedObject.getFirstName ())).setPlaceholder (sFirstName))
            .setErrorList (aFormErrors.getListOfField (FIELD_FIRSTNAME));
    }

    {
      final String sLastName = EText.LABEL_LASTNAME.getDisplayText (aDisplayLocale);
      aTable.createItemRow ()
            .setLabel (sLastName, isLastNameMandatory () ? ELabelType.MANDATORY : ELabelType.OPTIONAL)
            .setCtrl (new HCEdit (new RequestField (FIELD_LASTNAME,
                                                    aSelectedObject == null ? null : aSelectedObject.getLastName ())).setPlaceholder (sLastName))
            .setErrorList (aFormErrors.getListOfField (FIELD_LASTNAME));
    }

    {
      final String sEmail = EText.LABEL_EMAIL.getDisplayText (aDisplayLocale);
      aTable.createItemRow ()
            .setLabel (sEmail, isEmailMandatory () ? ELabelType.MANDATORY : ELabelType.OPTIONAL)
            .setCtrl (new HCEdit (new RequestField (FIELD_EMAILADDRESS,
                                                    aSelectedObject == null ? null : aSelectedObject.getEmailAddress ())).setPlaceholder (sEmail))
            .setErrorList (aFormErrors.getListOfField (FIELD_EMAILADDRESS));
    }

    if (!bEdit)
    {
      // Password is only shown on creation of a new user
      final boolean bHasAnyPasswordConstraint = GlobalPasswordSettings.getPasswordConstraintList ().hasConstraints ();

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
    }

    if (bIsAdministrator)
    {
      // Cannot edit enabled state of administrator
      aTable.createItemRow ()
            .setLabel (EText.LABEL_ENABLED.getDisplayText (aDisplayLocale))
            .setCtrl (EWebBasicsText.getYesOrNo (aSelectedObject.isEnabled (), aDisplayLocale));
    }
    else
    {
      aTable.createItemRow ()
            .setLabelMandatory (EText.LABEL_ENABLED.getDisplayText (aDisplayLocale))
            .setCtrl (new HCCheckBox (new RequestFieldBoolean (FIELD_ENABLED,
                                                               aSelectedObject == null ? DEFAULT_ENABLED
                                                                                      : aSelectedObject.isEnabled ())))
            .setErrorList (aFormErrors.getListOfField (FIELD_ENABLED));
    }

    {
      final Collection <String> aUserGroupIDs = aSelectedObject == null ? aWPEC.getAttrs (FIELD_USERGROUPS)
                                                                       : aMgr.getAllUserGroupIDsWithAssignedUser (aSelectedObject.getID ());
      final UserGroupForUserSelect aSelect = new UserGroupForUserSelect (new RequestField (FIELD_USERGROUPS),
                                                                         aDisplayLocale,
                                                                         aUserGroupIDs);
      aTable.createItemRow ()
            .setLabelMandatory (EText.LABEL_USERGROUPS_0.getDisplayText (aDisplayLocale))
            .setCtrl (aSelect)
            .setErrorList (aFormErrors.getListOfField (FIELD_USERGROUPS));
      if (bIsAdministrator)
      {
        // Cannot edit user groups of administrator
        aSelect.setReadonly (true);
      }
      showInputFormModifyUserGroupSelect (aSelect);
    }

    // Custom overridable
    showInputFormEnd (aWPEC, aSelectedObject, aForm, bEdit, bCopy, aFormErrors, aTable);
  }

  @Override
  protected boolean handleCustomActions (@Nonnull final WebPageExecutionContext aWPEC,
                                         @Nullable final IUser aSelectedObject)
  {
    if (aWPEC.hasAction (ACTION_RESET_PASSWORD) && aSelectedObject != null)
    {
      if (!canResetPassword (aSelectedObject))
        throw new IllegalStateException ("Won't work!");

      final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
      final boolean bShowForm = true;
      final FormErrors aFormErrors = new FormErrors ();
      if (aWPEC.hasSubAction (ACTION_PERFORM))
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
          AccessManager.getInstance ().setUserPassword (aSelectedObject.getID (), sPlainTextPassword);
          aWPEC.getNodeList ()
               .addChild (getStyler ().createSuccessBox (EText.SUCCESS_RESET_PASSWORD.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                              aSelectedObject.getDisplayName ())));
          return true;
        }
      }
      if (bShowForm)
      {
        // Show input form
        final boolean bHasAnyPasswordConstraint = GlobalPasswordSettings.getPasswordConstraintList ().hasConstraints ();
        final HCForm aForm = aWPEC.getNodeList ().addAndReturnChild (createFormSelf ());
        final IHCTableForm <?> aTable = aForm.addAndReturnChild (getStyler ().createTableForm (new HCCol (200),
                                                                                               HCCol.star (),
                                                                                               new HCCol (20)));
        aTable.setSpanningHeaderContent (EText.TITLE_RESET_PASSWORD.getDisplayTextWithArgs (aDisplayLocale,
                                                                                            aSelectedObject.getDisplayName ()));

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
        aToolbar.addHiddenField (CHCParam.PARAM_ACTION, ACTION_RESET_PASSWORD);
        aToolbar.addHiddenField (CHCParam.PARAM_OBJECT, aSelectedObject.getID ());
        aToolbar.addHiddenField (CHCParam.PARAM_SUBACTION, ACTION_PERFORM);
        aToolbar.addSubmitButtonSave (aDisplayLocale);
        aToolbar.addButtonCancel (aDisplayLocale);
      }
      return false;
    }
    return true;
  }

  @Nullable
  @OverrideOnDemand
  protected IHCNode getResetPasswordIcon ()
  {
    return EDefaultIcon.KEY.getIcon ().getAsNode ();
  }

  @Nonnull
  protected IHCNode getTabWithUsers (@Nonnull final Locale aDisplayLocale,
                                     @Nonnull final Collection <? extends IUser> aUsers,
                                     @Nonnull @Nonempty final String sTableID)
  {
    final boolean bSeparateLoginName = !useEmailAddressAsLoginName ();
    final AccessManager aMgr = AccessManager.getInstance ();
    // List existing
    final List <HCCol> aCols = new ArrayList <HCCol> ();
    aCols.add (new HCCol (200));
    if (bSeparateLoginName)
      aCols.add (new HCCol (200));
    aCols.add (HCCol.star ());
    aCols.add (new HCCol (150));
    aCols.add (createActionCol (3));
    final IHCTable <?> aTable = getStyler ().createTable (ArrayHelper.newArray (aCols, HCCol.class)).setID (sTableID);
    final HCRow aHeaderRow = aTable.addHeaderRow ();
    aHeaderRow.addCell (EText.HEADER_NAME.getDisplayText (aDisplayLocale));
    if (bSeparateLoginName)
      aHeaderRow.addCell (EText.HEADER_LOGINNAME.getDisplayText (aDisplayLocale));
    aHeaderRow.addCells (EText.HEADER_EMAIL.getDisplayText (aDisplayLocale),
                         EText.HEADER_USERGROUPS.getDisplayText (aDisplayLocale),
                         EWebBasicsText.MSG_ACTIONS.getDisplayText (aDisplayLocale));

    for (final IUser aCurUser : aUsers)
    {
      final ISimpleURL aViewLink = createViewURL (aCurUser);

      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (new HCA (aViewLink).addChild (aCurUser.getDisplayName ()));
      if (bSeparateLoginName)
        aRow.addCell (new HCA (aViewLink).addChild (aCurUser.getLoginName ()));
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

      final IHCCell <?> aActionCell = aRow.addCell ();

      // Edit user
      if (isEditAllowed (aCurUser))
        aActionCell.addChild (createEditLink (aCurUser, aDisplayLocale));
      else
        aActionCell.addChild (createEmptyAction ());

      // Copy
      aActionCell.addChild (createCopyLink (aCurUser, aDisplayLocale));

      // Reset password of user
      if (canResetPassword (aCurUser))
      {
        aActionCell.addChild (new HCA (LinkUtils.getSelfHref ()
                                                .add (CHCParam.PARAM_ACTION, ACTION_RESET_PASSWORD)
                                                .add (CHCParam.PARAM_OBJECT, aCurUser.getID ())).setTitle (EText.TITLE_RESET_PASSWORD.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                                                                              aCurUser.getDisplayName ()))
                                                                                                .addChild (getResetPasswordIcon ()));
      }
      else
        aActionCell.addChild (createEmptyAction ());
    }

    final HCNodeList aNodeList = new HCNodeList ();
    aNodeList.addChild (aTable);

    final DataTables aDataTables = getStyler ().createDefaultDataTables (aTable, aDisplayLocale);
    aDataTables.getOrCreateColumnOfTarget (3).setSortable (false);
    aDataTables.setInitialSorting (1, ESortOrder.ASCENDING);
    aNodeList.addChild (aDataTables);

    // Required for best layout inside a tab!
    aTable.removeAllColumns ();

    return aNodeList;
  }

  @Override
  protected void showListOfExistingObjects (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final HCNodeList aNodeList = aWPEC.getNodeList ();

    // Toolbar on top
    final IButtonToolbar <?> aToolbar = aNodeList.addAndReturnChild (getStyler ().createToolbar ());
    aToolbar.addButtonNew (EText.BUTTON_CREATE_NEW_USER.getDisplayText (aDisplayLocale), createCreateURL ());

    final ITabBox <?> aTabBox = getStyler ().createTabBox ();

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
