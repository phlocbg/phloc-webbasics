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
import com.phloc.appbasics.security.CSecurity;
import com.phloc.appbasics.security.role.IRole;
import com.phloc.appbasics.security.user.IUser;
import com.phloc.appbasics.security.usergroup.IUserGroup;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.name.ComparatorHasName;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.name.IHasDisplayTextWithArgs;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.IHCCell;
import com.phloc.html.hc.IHCTable;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCEM;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.validation.error.FormErrors;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webbasics.form.RequestField;
import com.phloc.webctrls.custom.table.IHCTableForm;
import com.phloc.webctrls.custom.table.IHCTableFormView;
import com.phloc.webctrls.custom.toolbar.IButtonToolbar;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webpages.AbstractWebPageForm;
import com.phloc.webpages.EWebPageText;

public class BasePageUserGroupManagement extends AbstractWebPageForm <IUserGroup>
{
  @Translatable
  protected static enum EText implements IHasDisplayText, IHasDisplayTextWithArgs
  {
    BUTTON_CREATE_NEW_USERGROUP ("Neue Benutzergruppe anlegen", "Create new user group"),
    HEADER_NAME ("Name", "Name"),
    HEADER_IN_USE ("Verwendet?", "In use?"),
    HEADER_VALUE ("Wert", "Value"),
    HEADER_DETAILS ("Details von Benutzergruppe {0}", "Details of user group {0}"),
    LABEL_NAME ("Name", "Name"),
    LABEL_USERS_0 ("Benutzer", "Users"),
    LABEL_USERS_N ("Benutzer ({0})", "Users ({0})"),
    LABEL_ROLES_0 ("Rollen", "Roles"),
    LABEL_ROLES_N ("Rollen ({0})", "Roles ({0})"),
    LABEL_ATTRIBUTES ("Attribute", "Attributes"),
    NONE_ASSIGNED ("keine zugeordnet", "none assigned"),
    TITLE_CREATE ("Neue Benutzergruppe anlegen", "Create new user group"),
    TITLE_EDIT ("Benutzergruppe ''{0}'' bearbeiten", "Edit user group ''{0}''"),
    ERROR_NAME_REQUIRED ("Es muss ein Name angegeben werden!", "A name must be specified!"),
    ERROR_NO_ROLE ("Es muss mindestens eine Rolle ausgewählt werden!", "At least one role must be selected!"),
    ERROR_INVALID_ROLES ("Mindestens eine der angegebenen Rolle ist ungültig!", "At least one selected role is invalid!"),
    DELETE_QUERY ("Soll die Benutzergruppe ''{0}'' wirklich gelöscht werden?", "Are you sure to delete the user group ''{0}''?"),
    DELETE_SUCCESS ("Die Benutzergruppe ''{0}'' wurden erfolgreich gelöscht!", "The user group ''{0}'' was successfully deleted!"),
    DELETE_ERROR ("Fehler beim Löschen der Benutzergruppe ''{0}''!", "Error deleting the user group ''{0}''!"),
    SUCCESS_CREATE ("Die neue BenutzerGruppe wurde erfolgreich angelegt!", "Successfully created the new user group!"),
    SUCCESS_EDIT ("Die Benutzergruppe wurde erfolgreich bearbeitet!", "Sucessfully edited the user group!");

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

  public static final String FIELD_NAME = "name";
  public static final String FIELD_ROLES = "roles";

  public BasePageUserGroupManagement (@Nonnull @Nonempty final String sID)
  {
    super (sID, EWebPageText.PAGE_NAME_SECURITY_USER_GROUPS.getAsMLT ());
  }

  public BasePageUserGroupManagement (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sName)
  {
    super (sID, sName);
  }

  public BasePageUserGroupManagement (@Nonnull @Nonempty final String sID,
                                      @Nonnull final String sName,
                                      @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageUserGroupManagement (@Nonnull @Nonempty final String sID,
                                      @Nonnull final IReadonlyMultiLingualText aName,
                                      @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @Override
  @Nullable
  protected IUserGroup getSelectedObject (final WebPageExecutionContext aWPEC, @Nullable final String sID)
  {
    return AccessManager.getInstance ().getUserGroupOfID (sID);
  }

  /**
   * Callback for manually extracting custom attributes. This method is called
   * independently if custom attributes are present or not.
   * 
   * @param aCurrentUserGroup
   *        The user group currently shown
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
  protected Set <String> showCustomAttrsOfSelectedObject (@Nonnull final IUserGroup aCurrentUserGroup,
                                                          @Nonnull final Map <String, ?> aCustomAttrs,
                                                          @Nonnull final IHCTableFormView <?> aTable,
                                                          @Nonnull final Locale aDisplayLocale)
  {
    return null;
  }

  @Override
  protected void showSelectedObject (@Nonnull final WebPageExecutionContext aWPEC,
                                     @Nonnull final IUserGroup aSelectedObject)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    final IHCTableFormView <?> aTable = aNodeList.addAndReturnChild (getStyler ().createTableFormView (new HCCol (170),
                                                                                                       HCCol.star ()));
    aTable.setSpanningHeaderContent (EText.HEADER_DETAILS.getDisplayTextWithArgs (aDisplayLocale,
                                                                                  aSelectedObject.getName ()));

    aTable.createItemRow ()
          .setLabel (EText.LABEL_NAME.getDisplayText (aDisplayLocale))
          .setCtrl (aSelectedObject.getName ());

    // All users assigned to this user group
    final Collection <String> aAssignedUserIDs = aSelectedObject.getAllContainedUserIDs ();
    if (aAssignedUserIDs.isEmpty ())
    {
      aTable.createItemRow ()
            .setLabel (EText.LABEL_USERS_0.getDisplayText (aDisplayLocale))
            .setCtrl (HCEM.create (EText.NONE_ASSIGNED.getDisplayText (aDisplayLocale)));
    }
    else
    {
      // Convert IDs to objects
      final AccessManager aMgr = AccessManager.getInstance ();
      final List <IUser> aAssignedUsers = new ArrayList <IUser> (aAssignedUserIDs.size ());
      for (final String sUserID : aAssignedUserIDs)
        aAssignedUsers.add (aMgr.getUserOfID (sUserID));

      final HCNodeList aUserUI = new HCNodeList ();
      for (final IUser aUser : ContainerHelper.getSorted (aAssignedUsers,
                                                          new ComparatorHasName <IUser> (aDisplayLocale)))
        aUserUI.addChild (HCDiv.create (aUser.getName ()));
      aTable.createItemRow ()
            .setLabel (EText.LABEL_USERS_N.getDisplayTextWithArgs (aDisplayLocale,
                                                                   Integer.toString (aAssignedUserIDs.size ())))
            .setCtrl (aUserUI);
    }

    // All roles assigned to this user group
    final Collection <String> aAssignedRoleIDs = aSelectedObject.getAllContainedRoleIDs ();
    if (aAssignedRoleIDs.isEmpty ())
    {
      aTable.createItemRow ()
            .setLabel (EText.LABEL_ROLES_0.getDisplayText (aDisplayLocale))
            .setCtrl (HCEM.create (EText.NONE_ASSIGNED.getDisplayText (aDisplayLocale)));
    }
    else
    {
      // Convert IDs to objects
      final AccessManager aMgr = AccessManager.getInstance ();
      final List <IRole> aAssignedRoles = new ArrayList <IRole> (aAssignedRoleIDs.size ());
      for (final String sRoleID : aAssignedRoleIDs)
        aAssignedRoles.add (aMgr.getRoleOfID (sRoleID));

      final HCNodeList aRoleUI = new HCNodeList ();
      for (final IRole aRole : ContainerHelper.getSorted (aAssignedRoles,
                                                          new ComparatorHasName <IRole> (aDisplayLocale)))
        aRoleUI.addChild (HCDiv.create (aRole.getName ()));
      aTable.createItemRow ()
            .setLabel (EText.LABEL_ROLES_N.getDisplayTextWithArgs (aDisplayLocale,
                                                                   Integer.toString (aAssignedRoleIDs.size ())))
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
  }

  @Override
  protected void validateAndSaveInputParameters (@Nonnull final WebPageExecutionContext aWPEC,
                                                 @Nullable final IUserGroup aSelectedObject,
                                                 @Nonnull final FormErrors aFormErrors,
                                                 final boolean bEdit)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final AccessManager aAccessMgr = AccessManager.getInstance ();
    final String sName = aWPEC.getAttr (FIELD_NAME);
    final Collection <String> aRoleIDs = aWPEC.getAttrs (FIELD_ROLES);

    if (StringHelper.hasNoText (sName))
      aFormErrors.addFieldError (FIELD_NAME, EText.ERROR_NAME_REQUIRED.getDisplayText (aDisplayLocale));

    if (ContainerHelper.isEmpty (aRoleIDs))
      aFormErrors.addFieldError (FIELD_ROLES, EText.ERROR_NO_ROLE.getDisplayText (aDisplayLocale));
    else
      if (!aAccessMgr.containsAllRolesWithID (aRoleIDs))
        aFormErrors.addFieldError (FIELD_ROLES, EText.ERROR_INVALID_ROLES.getDisplayText (aDisplayLocale));

    if (aFormErrors.isEmpty ())
    {
      // All fields are valid -> save
      if (bEdit)
      {
        final String sUserGroupID = aSelectedObject.getID ();

        // We're editing an existing object
        aAccessMgr.setUserGroupData (sUserGroupID, sName, aSelectedObject.getAllAttributes ());
        aNodeList.addChild (getStyler ().createSuccessBox (EText.SUCCESS_EDIT.getDisplayText (aDisplayLocale)));

        // assign to the matching roles
        final Collection <String> aPrevRoleIDs = aSelectedObject.getAllContainedRoleIDs ();
        // Create all missing assignments
        final Set <String> aRolesToBeAssigned = ContainerHelper.getDifference (aRoleIDs, aPrevRoleIDs);
        for (final String sRoleID : aRolesToBeAssigned)
          aAccessMgr.assignRoleToUserGroup (sUserGroupID, sRoleID);

        // Delete all old assignments
        final Set <String> aRolesToBeUnassigned = ContainerHelper.getDifference (aPrevRoleIDs, aRoleIDs);
        for (final String sRoleID : aRolesToBeUnassigned)
          aAccessMgr.unassignRoleFromUserGroup (sUserGroupID, sRoleID);
      }
      else
      {
        // We're creating a new object
        final IUserGroup aNewUserGroup = aAccessMgr.createNewUserGroup (sName);
        aNodeList.addChild (getStyler ().createSuccessBox (EText.SUCCESS_CREATE.getDisplayText (aDisplayLocale)));

        // assign to the matching internal user groups
        for (final String sRoleID : aRoleIDs)
          aAccessMgr.assignRoleToUserGroup (aNewUserGroup.getID (), sRoleID);
      }
    }
  }

  @Override
  protected void showInputForm (@Nonnull final WebPageExecutionContext aWPEC,
                                @Nullable final IUserGroup aSelectedObject,
                                @Nonnull final HCForm aForm,
                                final boolean bEdit,
                                final boolean bCopy,
                                @Nonnull final FormErrors aFormErrors)
  {
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final IHCTableForm <?> aTable = aForm.addAndReturnChild (getStyler ().createTableForm (new HCCol (170),
                                                                                           HCCol.star ()));
    aTable.setSpanningHeaderContent (bEdit ? EText.TITLE_EDIT.getDisplayTextWithArgs (aDisplayLocale,
                                                                                      aSelectedObject.getName ())
                                          : EText.TITLE_CREATE.getDisplayText (aDisplayLocale));

    final String sName = EText.LABEL_NAME.getDisplayText (aDisplayLocale);
    aTable.createItemRow ()
          .setLabelMandatory (sName)
          .setCtrl (new HCEdit (new RequestField (FIELD_NAME, aSelectedObject == null ? null
                                                                                     : aSelectedObject.getName ())).setPlaceholder (sName))
          .setErrorList (aFormErrors.getListOfField (FIELD_NAME));

    final Collection <String> aRoleIDs = aSelectedObject == null ? aWPEC.getAttrs (FIELD_ROLES)
                                                                : aSelectedObject.getAllContainedRoleIDs ();
    final RoleForUserGroupSelect aSelect = new RoleForUserGroupSelect (new RequestField (FIELD_ROLES),
                                                                       aDisplayLocale,
                                                                       aRoleIDs);
    aTable.createItemRow ()
          .setLabelMandatory (EText.LABEL_ROLES_0.getDisplayText (aDisplayLocale))
          .setCtrl (aSelect)
          .setErrorList (aFormErrors.getListOfField (FIELD_ROLES));
  }

  protected static boolean canDeleteUserGroup (@Nonnull final IUserGroup aUserGroup)
  {
    return !aUserGroup.hasContainedUsers () && !aUserGroup.getID ().equals (CSecurity.USERGROUP_ADMINISTRATORS_ID);
  }

  @Override
  protected boolean handleDeleteAction (@Nonnull final WebPageExecutionContext aWPEC,
                                        @Nonnull final IUserGroup aSelectedObject)
  {
    if (!canDeleteUserGroup (aSelectedObject))
      return true;

    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    if (aWPEC.hasSubAction (CHCParam.ACTION_SAVE))
    {
      if (AccessManager.getInstance ().deleteUserGroup (aSelectedObject.getID ()).isChanged ())
        aNodeList.addChild (getStyler ().createSuccessBox (EText.DELETE_SUCCESS.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                        aSelectedObject.getName ())));
      else
        aNodeList.addChild (getStyler ().createErrorBox (EText.DELETE_ERROR.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                    aSelectedObject.getName ())));
      return true;
    }

    final HCForm aForm = aNodeList.addAndReturnChild (createFormSelf ());
    aForm.addChild (getStyler ().createQuestionBox (EText.DELETE_QUERY.getDisplayTextWithArgs (aDisplayLocale,
                                                                                               aSelectedObject.getName ())));
    final IButtonToolbar <?> aToolbar = aForm.addAndReturnChild (getStyler ().createToolbar ());
    aToolbar.addHiddenField (CHCParam.PARAM_ACTION, ACTION_DELETE);
    aToolbar.addHiddenField (CHCParam.PARAM_OBJECT, aSelectedObject.getID ());
    aToolbar.addHiddenField (CHCParam.PARAM_SUBACTION, ACTION_SAVE);
    aToolbar.addSubmitButtonYes (aDisplayLocale);
    aToolbar.addButtonNo (aDisplayLocale);
    return false;
  }

  @Override
  protected void showListOfExistingObjects (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final HCNodeList aNodeList = aWPEC.getNodeList ();

    // Toolbar on top
    final IButtonToolbar <?> aToolbar = aNodeList.addAndReturnChild (getStyler ().createToolbar ());
    aToolbar.addButtonNew (EText.BUTTON_CREATE_NEW_USERGROUP.getDisplayText (aDisplayLocale), createCreateURL ());

    final IHCTable <?> aTable = getStyler ().createTable (HCCol.star (), new HCCol (110), createActionCol (2))
                                            .setID (getID ());
    aTable.addHeaderRow ().addCells (EText.HEADER_NAME.getDisplayText (aDisplayLocale),
                                     EText.HEADER_IN_USE.getDisplayText (aDisplayLocale),
                                     EWebBasicsText.MSG_ACTIONS.getDisplayText (aDisplayLocale));
    final Collection <? extends IUserGroup> aUserGroups = AccessManager.getInstance ().getAllUserGroups ();
    for (final IUserGroup aUserGroup : aUserGroups)
    {
      final ISimpleURL aViewLink = createViewURL (aUserGroup);

      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (new HCA (aViewLink).addChild (aUserGroup.getName ()));
      aRow.addCell (EWebBasicsText.getYesOrNo (aUserGroup.hasContainedUsers (), aDisplayLocale));

      final IHCCell <?> aActionCell = aRow.addCell ();
      aActionCell.addChild (createEditLink (aUserGroup,
                                            EWebPageText.OBJECT_EDIT.getDisplayTextWithArgs (aDisplayLocale,
                                                                                             aUserGroup.getName ())));
      if (canDeleteUserGroup (aUserGroup))
      {
        aActionCell.addChild (createDeleteLink (aUserGroup,
                                                EWebPageText.OBJECT_DELETE.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                   aUserGroup.getName ())));
      }
      else
      {
        aActionCell.addChild (createEmptyAction ());
      }
    }

    aNodeList.addChild (aTable);

    final DataTables aDataTables = getStyler ().createDefaultDataTables (aTable, aDisplayLocale);
    aDataTables.getOrCreateColumnOfTarget (2).setSortable (false);
    aDataTables.setInitialSorting (0, ESortOrder.ASCENDING);
    aNodeList.addChild (aDataTables);
  }
}
