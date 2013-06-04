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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.security.AccessManager;
import com.phloc.appbasics.security.CSecurity;
import com.phloc.appbasics.security.role.IRole;
import com.phloc.appbasics.security.user.IUser;
import com.phloc.appbasics.security.usergroup.IUserGroup;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.name.ComparatorHasName;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.name.IHasDisplayTextWithArgs;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.html.AbstractHCCell;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCEM;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webbasics.form.validation.FormErrors;
import com.phloc.webctrls.bootstrap.BootstrapTable;
import com.phloc.webctrls.bootstrap.derived.BootstrapErrorBox;
import com.phloc.webctrls.bootstrap.derived.BootstrapQuestionBox;
import com.phloc.webctrls.bootstrap.derived.BootstrapSuccessBox;
import com.phloc.webctrls.bootstrap.derived.BootstrapTableFormView;
import com.phloc.webctrls.bootstrap.derived.BootstrapToolbarAdvanced;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webpages.AbstractWebPageForm;
import com.phloc.webpages.EWebPageText;

public class BasePageUserGroupManagement extends AbstractWebPageForm <IUserGroup>
{
  @Translatable
  protected static enum EText implements IHasDisplayText, IHasDisplayTextWithArgs
  {
    MSG_NAME ("Name", "Name"),
    MSG_IN_USE ("Verwendet?", "In use?"),
    HEADER_DETAILS ("Details von Benutzergruppe {0}", "Details of user group {0}"),
    MSG_USERS_0 ("Benutzer", "Users"),
    MSG_USERS_N ("Benutzer ({0})", "Users ({0})"),
    MSG_ROLES_0 ("Rollen", "Roles"),
    MSG_ROLES_N ("Rollen ({0})", "Roles ({0})"),
    MSG_NONE_ASSIGNED ("keine zugeordnet", "none assigned"),
    DELETE_QUERY ("Soll die Benutzergruppe ''{0}'' wirklich gelöscht werden?", "Are you sure to delete the user group ''{0}''?"),
    DELETE_SUCCESS ("Die Benutzergruppe ''{0}'' wurden erfolgreich gelöscht!", "The user group ''{0}'' was successfully deleted!"),
    DELETE_ERROR ("Fehler beim Löschen der Benutzergruppe ''{0}''!", "Error deleting the user group ''{0}''!");

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

  @Override
  protected final boolean isEditAllowed (@Nullable final IUserGroup aLoginInfo)
  {
    return false;
  }

  @Override
  protected void showSelectedObject (@Nonnull final WebPageExecutionContext aWPEC,
                                     @Nonnull final IUserGroup aSelectedObject)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    final BootstrapTableFormView aTable = aNodeList.addAndReturnChild (new BootstrapTableFormView (new HCCol (170),
                                                                                                   HCCol.star ()));
    aTable.setSpanningHeaderContent (EText.HEADER_DETAILS.getDisplayTextWithArgs (aDisplayLocale,
                                                                                  aSelectedObject.getName ()));

    aTable.addItemRow (EText.MSG_NAME.getDisplayText (aDisplayLocale), aSelectedObject.getName ());

    // All users assigned to this user group
    final Collection <String> aAssignedUserIDs = aSelectedObject.getAllContainedUserIDs ();
    if (aAssignedUserIDs.isEmpty ())
    {
      aTable.addItemRow (EText.MSG_USERS_0.getDisplayText (aDisplayLocale),
                         HCEM.create (EText.MSG_NONE_ASSIGNED.getDisplayText (aDisplayLocale)));
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
      aTable.addItemRow (EText.MSG_USERS_N.getDisplayTextWithArgs (aDisplayLocale,
                                                                   Integer.toString (aAssignedUserIDs.size ())),
                         aUserUI);
    }

    // All roles assigned to this user group
    final Collection <String> aAssignedRoleIDs = aSelectedObject.getAllContainedRoleIDs ();
    if (aAssignedRoleIDs.isEmpty ())
    {
      aTable.addItemRow (EText.MSG_ROLES_0.getDisplayText (aDisplayLocale),
                         HCEM.create (EText.MSG_NONE_ASSIGNED.getDisplayText (aDisplayLocale)));
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
      aTable.addItemRow (EText.MSG_ROLES_N.getDisplayTextWithArgs (aDisplayLocale,
                                                                   Integer.toString (aAssignedRoleIDs.size ())),
                         aRoleUI);
    }
  }

  @Override
  protected void validateAndSaveInputParameters (@Nonnull final WebPageExecutionContext aWPEC,
                                                 @Nullable final IUserGroup aSelectedObject,
                                                 @Nonnull final FormErrors aFormErrors,
                                                 final boolean bEdit)
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  protected void showInputForm (@Nonnull final WebPageExecutionContext aWPEC,
                                @Nullable final IUserGroup aSelectedObject,
                                @Nonnull final HCForm aForm,
                                final boolean bEdit,
                                final boolean bCopy,
                                @Nonnull final FormErrors aFormErrors)
  {
    throw new UnsupportedOperationException ();
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
        aNodeList.addChild (BootstrapSuccessBox.create (EText.DELETE_SUCCESS.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                     aSelectedObject.getName ())));
      else
        aNodeList.addChild (BootstrapErrorBox.create (EText.DELETE_ERROR.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                 aSelectedObject.getName ())));
      return true;
    }

    final HCForm aForm = aNodeList.addAndReturnChild (createFormSelf ());
    aForm.addChild (BootstrapQuestionBox.create (EText.DELETE_QUERY.getDisplayTextWithArgs (aDisplayLocale,
                                                                                            aSelectedObject.getName ())));
    final BootstrapToolbarAdvanced aToolbar = aForm.addAndReturnChild (new BootstrapToolbarAdvanced ());
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

    final BootstrapTable aTable = new BootstrapTable (HCCol.star (), new HCCol (110), createActionCol (1)).setID (getID ());
    aTable.addHeaderRow ().addCells (EText.MSG_NAME.getDisplayText (aDisplayLocale),
                                     EText.MSG_IN_USE.getDisplayText (aDisplayLocale),
                                     EWebBasicsText.MSG_ACTIONS.getDisplayText (aDisplayLocale));
    final Collection <? extends IUserGroup> aUserGroups = AccessManager.getInstance ().getAllUserGroups ();
    for (final IUserGroup aUserGroup : aUserGroups)
    {
      final ISimpleURL aViewLink = createViewURL (aUserGroup);

      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (new HCA (aViewLink).addChild (aUserGroup.getName ()));
      aRow.addCell (EWebBasicsText.getYesOrNo (aUserGroup.hasContainedUsers (), aDisplayLocale));

      final AbstractHCCell aActionCell = aRow.addCell ();
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

    final DataTables aDataTables = createDefaultDataTables (aTable, aDisplayLocale);
    aDataTables.getOrCreateColumnOfTarget (2).setSortable (false);
    aDataTables.setInitialSorting (0, ESortOrder.ASCENDING);
    aNodeList.addChild (aDataTables);
  }
}
