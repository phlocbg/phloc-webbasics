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
package com.phloc.webpages.form;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.menu.GlobalMenuTree;
import com.phloc.appbasics.app.menu.IMenuItem;
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.url.SMap;
import com.phloc.datetime.format.PDTToString;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.html.AbstractHCCell;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCRow;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webbasics.form.FormState;
import com.phloc.webbasics.form.FormStateManager;
import com.phloc.webbasics.form.validation.FormErrors;
import com.phloc.webctrls.bootstrap.BootstrapTable;
import com.phloc.webctrls.bootstrap.derived.BootstrapErrorBox;
import com.phloc.webctrls.bootstrap.derived.BootstrapInfoBox;
import com.phloc.webctrls.bootstrap.derived.BootstrapQuestionBox;
import com.phloc.webctrls.bootstrap.derived.BootstrapSuccessBox;
import com.phloc.webctrls.bootstrap.derived.BootstrapToolbarAdvanced;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webpages.AbstractWebPageForm;

public class BasePageSavedStates extends AbstractWebPageForm <FormState>
{
  public BasePageSavedStates (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  public BasePageSavedStates (@Nonnull @Nonempty final String sID,
                              @Nonnull final String sName,
                              @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageSavedStates (@Nonnull @Nonempty final String sID,
                              @Nonnull final IReadonlyMultiLingualText aName,
                              @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @Nonnull
  @OverrideOnDemand
  protected IMenuTree getMenuTree ()
  {
    return GlobalMenuTree.getInstance ();
  }

  @Override
  @Nullable
  protected FormState getSelectedObject (final WebPageExecutionContext aWPEC, @Nullable final String sID)
  {
    return FormStateManager.getInstance ().getFormStateOfID (sID);
  }

  @Override
  protected void showSelectedObject (final WebPageExecutionContext aWPEC, final FormState aSelectedObject)
  {}

  @Override
  protected void validateAndSaveInputParameters (@Nonnull final WebPageExecutionContext aWPEC,
                                                 @Nullable final FormState aSelectedObject,
                                                 @Nonnull final FormErrors aFormErrors,
                                                 final boolean bEdit)
  {}

  @Override
  protected void showInputForm (@Nonnull final WebPageExecutionContext aWPEC,
                                @Nullable final FormState aSelectedObject,
                                @Nonnull final HCForm aForm,
                                final boolean bEdit,
                                final boolean bCopy,
                                @Nonnull final FormErrors aFormErrors)
  {}

  @Override
  protected boolean handleDeleteAction (@Nonnull final WebPageExecutionContext aWPEC,
                                        @Nonnull final FormState aSelectedObject)
  {
    if (aWPEC.hasSubAction (CHCParam.ACTION_SAVE))
    {
      if (FormStateManager.getInstance ().deleteFormState (aSelectedObject.getID ()).isChanged ())
        aWPEC.getNodeList ().addChild (BootstrapSuccessBox.create ("Die gemerkten Daten wurden erfolgreich gelöscht!"));
      else
        aWPEC.getNodeList ().addChild (BootstrapErrorBox.create ("Fehler beim Löschen der gemerkten Daten!"));
      return true;
    }

    final HCForm aForm = aWPEC.getNodeList ().addAndReturnChild (createFormSelf ());
    aForm.addChild (BootstrapQuestionBox.create ("Sollen diese gemerkten Daten wirklich gelöscht werden?"));
    final BootstrapToolbarAdvanced aToolbar = aForm.addAndReturnChild (new BootstrapToolbarAdvanced ());
    aToolbar.addHiddenField (CHCParam.PARAM_ACTION, ACTION_DELETE);
    aToolbar.addHiddenField (CHCParam.PARAM_OBJECT, aSelectedObject.getID ());
    aToolbar.addHiddenField (CHCParam.PARAM_SUBACTION, ACTION_SAVE);
    aToolbar.addSubmitButtonYes (aWPEC.getDisplayLocale ());
    aToolbar.addButtonNo (aWPEC.getDisplayLocale ());
    return false;
  }

  @Override
  protected boolean handleCustomActions (@Nonnull final WebPageExecutionContext aWPEC,
                                         @Nullable final FormState aSelectedObject)
  {
    if (aWPEC.hasAction (ACTION_DELETE_ALL))
    {
      if (aWPEC.hasSubAction (CHCParam.ACTION_SAVE))
      {
        if (FormStateManager.getInstance ().deleteAllFormStates ().isChanged ())
          aWPEC.getNodeList ()
               .addChild (BootstrapSuccessBox.create ("Alle gemerkten Daten wurden erfolgreich gelöscht!"));
        else
          aWPEC.getNodeList ().addChild (BootstrapErrorBox.create ("Fehler beim Löschen der gemerkten Daten!"));
        return true;
      }

      final HCForm aForm = aWPEC.getNodeList ().addAndReturnChild (createFormSelf ());
      aForm.addChild (BootstrapQuestionBox.create ("Sollen alle gemerkten Daten wirklich gelöscht werden?"));
      final BootstrapToolbarAdvanced aToolbar = aForm.addAndReturnChild (new BootstrapToolbarAdvanced ());
      aToolbar.addHiddenField (CHCParam.PARAM_ACTION, ACTION_DELETE_ALL);
      aToolbar.addHiddenField (CHCParam.PARAM_SUBACTION, ACTION_SAVE);
      aToolbar.addSubmitButtonYes (aWPEC.getDisplayLocale ());
      aToolbar.addButtonNo (aWPEC.getDisplayLocale ());
      return false;
    }

    return true;
  }

  @Override
  protected void showListOfExistingObjects (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final FormStateManager aFSM = FormStateManager.getInstance ();

    final Collection <FormState> aAllFormStates = aFSM.getAllFormStates ();
    if (aAllFormStates.isEmpty ())
    {
      aWPEC.getNodeList ().addChild (BootstrapInfoBox.create ("Es sind keine gemerkten Daten vorhanden!"));
    }
    else
    {
      final BootstrapToolbarAdvanced aToolbar = new BootstrapToolbarAdvanced ();
      aToolbar.addButton ("Alle löschen",
                          LinkUtils.getSelfHref (new SMap (CHCParam.PARAM_ACTION, ACTION_DELETE_ALL)),
                          EDefaultIcon.DELETE);
      aWPEC.getNodeList ().addChild (aToolbar);

      // Start emitting saved states
      final BootstrapTable aPerPage = aWPEC.getNodeList ().addAndReturnChild (new BootstrapTable (HCCol.star (),
                                                                                                  new HCCol (170),
                                                                                                  createActionCol (2)));
      aPerPage.addHeaderRow ().addCells ("Seite", "Gemerkt am", "Aktionen");
      for (final FormState aFormState : aAllFormStates)
      {
        final HCRow aRow = aPerPage.addBodyRow ();

        final String sPageID = aFormState.getPageID ();
        final IMenuItem aMenuItem = (IMenuItem) getMenuTree ().getMenuObjectOfID (sPageID);
        aRow.addCell (aMenuItem.getDisplayText (aWPEC.getDisplayLocale ()));

        aRow.addCell (PDTToString.getAsString (aFormState.getDateTime (), aWPEC.getDisplayLocale ()));

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
