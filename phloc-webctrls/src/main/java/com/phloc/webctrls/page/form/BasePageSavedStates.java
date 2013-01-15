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
package com.phloc.webctrls.page.form;

import java.util.Collection;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.menu.GlobalMenuTree;
import com.phloc.appbasics.app.menu.IMenuItem;
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.url.SMap;
import com.phloc.datetime.format.PDTToString;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.html.AbstractHCCell;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.form.FormState;
import com.phloc.webbasics.form.FormStateManager;
import com.phloc.webbasics.form.validation.FormErrors;
import com.phloc.webctrls.bootstrap.BootstrapTable;
import com.phloc.webctrls.bootstrap.derived.BootstrapButtonToolbarAdvanced;
import com.phloc.webctrls.bootstrap.derived.BootstrapErrorBox;
import com.phloc.webctrls.bootstrap.derived.BootstrapInfoBox;
import com.phloc.webctrls.bootstrap.derived.BootstrapQuestionBox;
import com.phloc.webctrls.bootstrap.derived.BootstrapSuccessBox;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webctrls.page.AbstractWebPageForm;

public class BasePageSavedStates extends AbstractWebPageForm <FormState>
{
  public BasePageSavedStates (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  @Nonnull
  @OverrideOnDemand
  protected IMenuTree getMenuTree ()
  {
    return GlobalMenuTree.getInstance ();
  }

  @Override
  @Nullable
  protected FormState getSelectedObject (@Nullable final String sID)
  {
    return FormStateManager.getInstance ().getFormStateOfID (sID);
  }

  @Override
  protected void showSelectedObject (final FormState aSelectedObject,
                                     final Locale aDisplayLocale,
                                     final HCNodeList aNodeList)
  {}

  @Override
  protected void validateAndSaveInputParameters (final FormErrors aFormErrors,
                                                 final Locale aDisplayLocale,
                                                 final HCNodeList aNodeList,
                                                 final boolean bEdit,
                                                 final FormState aSelectedObject)
  {}

  @Override
  protected void showInputForm (final FormState aSelectedObject,
                                final Locale aDisplayLocale,
                                final HCForm aForm,
                                final boolean bEdit,
                                final boolean bCopy,
                                final FormErrors aFormErrors)
  {}

  @Override
  protected boolean handleDeleteAction (@Nonnull final FormState aSelectedObject,
                                        @Nonnull final Locale aDisplayLocale,
                                        @Nonnull final HCNodeList aNodeList)
  {
    if (hasSubAction (CHCParam.ACTION_SAVE))
    {
      if (FormStateManager.getInstance ().deleteFormState (aSelectedObject.getID ()).isChanged ())
        aNodeList.addChild (BootstrapSuccessBox.create ("Die gemerkten Daten wurden erfolgreich gelöscht!"));
      else
        aNodeList.addChild (BootstrapErrorBox.create ("Fehler beim Löschen der gemerkten Daten!"));
      return true;
    }

    final HCForm aForm = aNodeList.addAndReturnChild (createFormSelf ());
    aForm.addChild (BootstrapQuestionBox.create ("Sollen diese gemerkten Daten wirklich gelöscht werden?"));
    final BootstrapButtonToolbarAdvanced aToolbar = aForm.addAndReturnChild (new BootstrapButtonToolbarAdvanced ());
    aToolbar.addHiddenField (CHCParam.PARAM_ACTION, ACTION_DELETE);
    aToolbar.addHiddenField (CHCParam.PARAM_OBJECT, aSelectedObject.getID ());
    aToolbar.addHiddenField (CHCParam.PARAM_SUBACTION, ACTION_SAVE);
    aToolbar.addSubmitButtonYes (aDisplayLocale);
    aToolbar.addButtonNo (aDisplayLocale);
    return false;
  }

  @Override
  protected boolean handleCustomActions (@Nullable final FormState aSelectedObject,
                                         @Nonnull final Locale aDisplayLocale,
                                         @Nonnull final HCNodeList aNodeList)
  {
    if (hasAction (ACTION_DELETE_ALL))
    {
      if (hasSubAction (CHCParam.ACTION_SAVE))
      {
        if (FormStateManager.getInstance ().deleteAllFormStates ().isChanged ())
          aNodeList.addChild (BootstrapSuccessBox.create ("Alle gemerkten Daten wurden erfolgreich gelöscht!"));
        else
          aNodeList.addChild (BootstrapErrorBox.create ("Fehler beim Löschen der gemerkten Daten!"));
        return true;
      }

      final HCForm aForm = aNodeList.addAndReturnChild (createFormSelf ());
      aForm.addChild (BootstrapQuestionBox.create ("Sollen alle gemerkten Daten wirklich gelöscht werden?"));
      final BootstrapButtonToolbarAdvanced aToolbar = aForm.addAndReturnChild (new BootstrapButtonToolbarAdvanced ());
      aToolbar.addHiddenField (CHCParam.PARAM_ACTION, ACTION_DELETE_ALL);
      aToolbar.addHiddenField (CHCParam.PARAM_SUBACTION, ACTION_SAVE);
      aToolbar.addSubmitButtonYes (aDisplayLocale);
      aToolbar.addButtonNo (aDisplayLocale);
      return false;
    }

    return true;
  }

  @Override
  protected void showListOfExistingObjects (final Locale aDisplayLocale, final HCNodeList aNodeList)
  {
    final FormStateManager aFSM = FormStateManager.getInstance ();

    final Collection <FormState> aAllFormStates = aFSM.getAllFormStates ();
    if (aAllFormStates.isEmpty ())
    {
      aNodeList.addChild (BootstrapInfoBox.create ("Es sind keine gemerkten Daten vorhanden!"));
    }
    else
    {
      final BootstrapButtonToolbarAdvanced aToolbar = new BootstrapButtonToolbarAdvanced ();
      aToolbar.addButton ("Alle löschen",
                          LinkUtils.getSelfHref (new SMap (CHCParam.PARAM_ACTION, ACTION_DELETE_ALL)),
                          EDefaultIcon.DELETE);
      aNodeList.addChild (aToolbar);

      // Start emitting saved states
      final BootstrapTable aPerPage = aNodeList.addAndReturnChild (new BootstrapTable (HCCol.star (),
                                                                                       new HCCol (170),
                                                                                       createActionCol (2)));
      aPerPage.addHeaderRow ().addCells ("Seite", "Gemerkt am", "Aktionen");
      for (final FormState aFormState : aAllFormStates)
      {
        final HCRow aRow = aPerPage.addBodyRow ();

        final String sPageID = aFormState.getPageID ();
        final IMenuItem aMenuItem = (IMenuItem) getMenuTree ().getMenuObjectOfID (sPageID);
        aRow.addCell (aMenuItem.getDisplayText (aDisplayLocale));

        aRow.addCell (PDTToString.getAsString (aFormState.getDateTime (), aDisplayLocale));

        final AbstractHCCell aActionCell = aRow.addCell ();
        // Original action (currently always create even for copy)
        final String sAction = aFormState.getAttributes ().getAttributeAsString (CHCParam.PARAM_ACTION, ACTION_CREATE);
        // Original object ID
        final String sObjectID = aFormState.getAttributes ().getAttributeAsString (CHCParam.PARAM_OBJECT);
        aActionCell.addChild (new HCA (LinkUtils.getLinkToMenuItem (aFormState.getPageID ())
                                                .add (CHCParam.PARAM_ACTION, sAction)
                                                .addIfNonNull (CHCParam.PARAM_OBJECT, sObjectID)
                                                .add (FIELD_FLOW_ID, aFormState.getFlowID ())
                                                .add (FIELD_RESTORE_FLOW_ID, aFormState.getFlowID ())).setTitle ("Daten weiter bearbeiten")
                                                                                                      .addChild (getCreateImg ()));
        aActionCell.addChild (createDeleteLink (aFormState, "Lösche diese gemerkten Daten"));
      }
    }
  }
}
