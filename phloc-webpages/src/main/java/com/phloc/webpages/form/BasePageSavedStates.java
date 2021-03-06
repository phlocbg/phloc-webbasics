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
package com.phloc.webpages.form;

import java.util.Collection;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.menu.ApplicationMenuTree;
import com.phloc.appbasics.app.menu.GlobalMenuTree;
import com.phloc.appbasics.app.menu.IMenuItem;
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.datetime.format.PDTToString;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.IHCCell;
import com.phloc.html.hc.IHCTable;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.validation.error.FormErrors;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webbasics.form.FormState;
import com.phloc.webbasics.form.FormStateManager;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webctrls.custom.toolbar.IButtonToolbar;
import com.phloc.webpages.AbstractWebPageForm;

@SuppressWarnings ("deprecation")
public class BasePageSavedStates extends AbstractWebPageForm <FormState>
{
  @Translatable
  protected static enum EText implements IHasDisplayText
  {
    DELETE_QUERY ("Sollen diese gemerkten Daten wirklich gelöscht werden?", "Are you sure to delete this saved data?"),
    DELETE_SUCCESS ("Die gemerkten Daten wurden erfolgreich gelöscht!", "The saved data was successfully deleted!"),
    DELETE_ERROR ("Fehler beim Löschen der gemerkten Daten!", "Error deleting the saved data!"),
    DELETE_ALL_QUERY ("Sollen alle gemerkten Daten wirklich gelöscht werden?", "Are you sure to delete all saved data?"),
    DELETE_ALL_SUCCESS ("Alle gemerkten Daten wurden erfolgreich gelöscht!", "All saved data was successfully deleted!"),
    DELETE_ALL_ERROR ("Fehler beim Löschen der gemerkten Daten!", "Error deleting the saved data!"),
    NONE_PRESENT ("Es sind keine gemerkten Daten vorhanden!", "No saved data is available"),
    BUTTON_DELETE ("Alle löschen", "Delete all"),
    SAVED_STATE_EDIT ("Daten weiter bearbeiten", "Continue editing this data"),
    SAVED_STATE_DELETE ("Lösche diese gemerkten Daten", "Delete this saved state"),
    HEADER_PAGE ("Seite", "Page"),
    HEADER_REMEMBERED_AT ("Gemerkt am", "Remebered at"),
    HEADER_ACTIONS ("Aktionen", "Actions");

    private final ITextProvider m_aTP;

    private EText (@Nonnull final String sDE, @Nonnull final String sEN)
    {
      m_aTP = TextProvider.create_DE_EN (sDE, sEN);
    }

    @Nullable
    public String getDisplayText (@Nonnull final Locale aContentLocale)
    {
      return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
    }
  }

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
    IMenuTree ret = ApplicationMenuTree.getTree ();
    // XXX hack alert :(
    if (!ret.getRootItem ().hasChildren ())
      ret = GlobalMenuTree.getTree ();
    return ret;
  }

  @Override
  @Nullable
  protected FormState getSelectedObject (@Nonnull final WebPageExecutionContext aWPEC, @Nullable final String sID)
  {
    return FormStateManager.getInstance ().getFormStateOfID (sID);
  }

  @Override
  protected void showSelectedObject (@Nonnull final WebPageExecutionContext aWPEC,
                                     @Nonnull final FormState aSelectedObject)
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
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    if (aWPEC.hasSubAction (CHCParam.ACTION_SAVE))
    {
      if (FormStateManager.getInstance ().deleteFormState (aSelectedObject.getID ()).isChanged ())
        aNodeList.addChild (getStyler ().createSuccessBox (EText.DELETE_SUCCESS.getDisplayText (aDisplayLocale)));
      else
        aNodeList.addChild (getStyler ().createErrorBox (EText.DELETE_ERROR.getDisplayText (aDisplayLocale)));
      return true;
    }

    final HCForm aForm = aNodeList.addAndReturnChild (createFormSelf ());
    aForm.addChild (getStyler ().createQuestionBox (EText.DELETE_QUERY.getDisplayText (aDisplayLocale)));
    final IButtonToolbar <?> aToolbar = aForm.addAndReturnChild (getStyler ().createToolbar ());
    aToolbar.addHiddenField (CHCParam.PARAM_ACTION, ACTION_DELETE);
    aToolbar.addHiddenField (CHCParam.PARAM_OBJECT, aSelectedObject.getID ());
    aToolbar.addHiddenField (CHCParam.PARAM_SUBACTION, ACTION_SAVE);
    aToolbar.addSubmitButtonYes (aDisplayLocale);
    aToolbar.addButtonNo (aDisplayLocale);
    return false;
  }

  @Override
  protected boolean handleCustomActions (@Nonnull final WebPageExecutionContext aWPEC,
                                         @Nullable final FormState aSelectedObject)
  {
    if (aWPEC.hasAction (ACTION_DELETE_ALL))
    {
      final HCNodeList aNodeList = aWPEC.getNodeList ();
      final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

      if (aWPEC.hasSubAction (CHCParam.ACTION_SAVE))
      {
        if (FormStateManager.getInstance ().deleteAllFormStates ().isChanged ())
          aNodeList.addChild (getStyler ().createSuccessBox (EText.DELETE_ALL_SUCCESS.getDisplayText (aDisplayLocale)));
        else
          aNodeList.addChild (getStyler ().createErrorBox (EText.DELETE_ALL_ERROR.getDisplayText (aDisplayLocale)));
        return true;
      }

      final HCForm aForm = aNodeList.addAndReturnChild (createFormSelf ());
      aForm.addChild (getStyler ().createQuestionBox (EText.DELETE_ALL_QUERY.getDisplayText (aDisplayLocale)));
      final IButtonToolbar <?> aToolbar = aForm.addAndReturnChild (getStyler ().createToolbar ());
      aToolbar.addHiddenField (CHCParam.PARAM_ACTION, ACTION_DELETE_ALL);
      aToolbar.addHiddenField (CHCParam.PARAM_SUBACTION, ACTION_SAVE);
      aToolbar.addSubmitButtonYes (aDisplayLocale);
      aToolbar.addButtonNo (aDisplayLocale);
      return false;
    }

    return true;
  }

  @Override
  protected void showListOfExistingObjects (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final FormStateManager aFSM = FormStateManager.getInstance ();

    final Collection <FormState> aAllFormStates = aFSM.getAllFormStates ();
    if (aAllFormStates.isEmpty ())
    {
      aNodeList.addChild (getStyler ().createInfoBox (EText.NONE_PRESENT.getDisplayText (aDisplayLocale)));
    }
    else
    {
      final IButtonToolbar <?> aToolbar = getStyler ().createToolbar ();
      aToolbar.addButton (EText.BUTTON_DELETE.getDisplayText (aDisplayLocale),
                          aWPEC.getSelfHref ().add (CHCParam.PARAM_ACTION, ACTION_DELETE_ALL),
                          EDefaultIcon.DELETE);
      aNodeList.addChild (aToolbar);

      // Start emitting saved states
      final IHCTable <?> aPerPage = aNodeList.addAndReturnChild (getStyler ().createTable (HCCol.star (),
                                                                                           new HCCol (170),
                                                                                           createActionCol (2)));
      aPerPage.addHeaderRow ().addCells (EText.HEADER_PAGE.getDisplayText (aDisplayLocale),
                                         EText.HEADER_REMEMBERED_AT.getDisplayText (aDisplayLocale),
                                         EText.HEADER_ACTIONS.getDisplayText (aDisplayLocale));
      for (final FormState aFormState : aAllFormStates)
      {
        final HCRow aRow = aPerPage.addBodyRow ();

        final String sPageID = aFormState.getPageID ();
        final IMenuItem aMenuItem = (IMenuItem) getMenuTree ().getMenuObjectOfID (sPageID);
        aRow.addCell (aMenuItem.getDisplayText (aDisplayLocale));

        aRow.addCell (PDTToString.getAsString (aFormState.getDateTime (), aDisplayLocale));

        final IHCCell <?> aActionCell = aRow.addCell ();
        // Original action (currently always create even for copy)
        final String sAction = aFormState.getAttributes ().getAttributeAsString (CHCParam.PARAM_ACTION, ACTION_CREATE);
        // Original object ID
        final String sObjectID = aFormState.getAttributes ().getAttributeAsString (CHCParam.PARAM_OBJECT);
        aActionCell.addChild (new HCA (LinkUtils.getLinkToMenuItem (aFormState.getPageID ())
                                                .add (CHCParam.PARAM_ACTION, sAction)
                                                .addIfNonNull (CHCParam.PARAM_OBJECT, sObjectID)
                                                .add (FIELD_FLOW_ID, aFormState.getFlowID ())
                                                .add (FIELD_RESTORE_FLOW_ID, aFormState.getFlowID ())).setTitle (EText.SAVED_STATE_EDIT.getDisplayText (aDisplayLocale))
                                                                                                      .addChild (getCreateImg ()));
        aActionCell.addChild (createDeleteLink (aFormState, EText.SAVED_STATE_DELETE.getDisplayText (aDisplayLocale)));
      }
    }
  }
}
