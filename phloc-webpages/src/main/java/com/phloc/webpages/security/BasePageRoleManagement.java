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

import java.util.Collection;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.security.AccessManager;
import com.phloc.appbasics.security.role.IRole;
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
import com.phloc.webctrls.bootstrap.derived.BootstrapTableFormView;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webpages.AbstractWebPageForm;

public class BasePageRoleManagement extends AbstractWebPageForm <IRole>
{
  @Translatable
  protected static enum EText implements IHasDisplayText, IHasDisplayTextWithArgs
  {
    MSG_NAME ("Name", "Name"),
    MSG_IN_USE ("Verwendet?", "In use?"),
    HEADER_DETAILS ("Details von Rolle {0}", "Details of role {0}"),
    MSG_USERGROUPS_0 ("Benutzergruppen", "User groups"),
    MSG_USERGROUPS_N ("Benutzergruppen ({0})", "User groups ({0})"),
    MSG_NONE_ASSIGNED ("keine zugeordnet", "none assigned");

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

  public BasePageRoleManagement (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sName)
  {
    super (sID, sName);
  }

  public BasePageRoleManagement (@Nonnull @Nonempty final String sID,
                                 @Nonnull final String sName,
                                 @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageRoleManagement (@Nonnull @Nonempty final String sID,
                                 @Nonnull final IReadonlyMultiLingualText aName,
                                 @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @Override
  @Nullable
  protected IRole getSelectedObject (final WebPageExecutionContext aWPEC, @Nullable final String sID)
  {
    return AccessManager.getInstance ().getRoleOfID (sID);
  }

  @Override
  protected final boolean isEditAllowed (@Nullable final IRole aLoginInfo)
  {
    return false;
  }

  @Override
  protected void showSelectedObject (@Nonnull final WebPageExecutionContext aWPEC, @Nonnull final IRole aSelectedObject)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    final BootstrapTableFormView aTable = aNodeList.addAndReturnChild (new BootstrapTableFormView (new HCCol (170),
                                                                                                   HCCol.star ()));
    aTable.setSpanningHeaderContent (EText.HEADER_DETAILS.getDisplayTextWithArgs (aDisplayLocale,
                                                                                  aSelectedObject.getName ()));

    aTable.addItemRow (EText.MSG_NAME.getDisplayText (aDisplayLocale), aSelectedObject.getName ());

    // All user groups to which the role is assigned
    final Collection <IUserGroup> aAssignedUserGroups = AccessManager.getInstance ()
                                                                     .getAllUserGroupsWithAssignedRole (aSelectedObject.getID ());
    if (aAssignedUserGroups.isEmpty ())
    {
      aTable.addItemRow (EText.MSG_USERGROUPS_0.getDisplayText (aDisplayLocale),
                         HCEM.create (EText.MSG_NONE_ASSIGNED.getDisplayText (aDisplayLocale)));
    }
    else
    {
      final HCNodeList aUserGroupUI = new HCNodeList ();
      for (final IUserGroup aUserGroup : ContainerHelper.getSorted (aAssignedUserGroups,
                                                                    new ComparatorHasName <IUserGroup> (aDisplayLocale)))
        aUserGroupUI.addChild (HCDiv.create (aUserGroup.getName ()));
      aTable.addItemRow (EText.MSG_USERGROUPS_N.getDisplayTextWithArgs (aDisplayLocale,
                                                                        Integer.toString (aAssignedUserGroups.size ())),
                         aUserGroupUI);
    }
  }

  @Override
  protected void validateAndSaveInputParameters (@Nonnull final WebPageExecutionContext aWPEC,
                                                 @Nullable final IRole aSelectedObject,
                                                 @Nonnull final FormErrors aFormErrors,
                                                 final boolean bEdit)
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  protected void showInputForm (@Nonnull final WebPageExecutionContext aWPEC,
                                @Nullable final IRole aSelectedObject,
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

    final BootstrapTable aTable = new BootstrapTable (HCCol.star (), new HCCol (110)).setID (getID ());
    aTable.addHeaderRow ().addCells (EText.MSG_NAME.getDisplayText (aDisplayLocale),
                                     EText.MSG_IN_USE.getDisplayText (aDisplayLocale));
    final Collection <? extends IRole> aRoles = AccessManager.getInstance ().getAllRoles ();
    for (final IRole aRole : aRoles)
    {
      final ISimpleURL aViewLink = createViewURL (aRole);

      final Collection <IUserGroup> aAssignedUserGroups = AccessManager.getInstance ()
                                                                       .getAllUserGroupsWithAssignedRole (aRole.getID ());

      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (new HCA (aViewLink).addChild (aRole.getName ()));
      aRow.addCell (EWebBasicsText.getYesOrNo (!aAssignedUserGroups.isEmpty (), aDisplayLocale));
    }

    aNodeList.addChild (aTable);

    final DataTables aDataTables = createDefaultDataTables (aTable, aDisplayLocale);
    aDataTables.setInitialSorting (0, ESortOrder.ASCENDING);
    aNodeList.addChild (aDataTables);
  }
}
