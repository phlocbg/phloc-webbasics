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
package com.phloc.webctrls.page;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.url.SMap;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCHiddenField;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.form.FormState;
import com.phloc.webbasics.form.FormStateManager;
import com.phloc.webbasics.form.RequestField;
import com.phloc.webbasics.form.ajax.AjaxHandlerSaveFormState;
import com.phloc.webbasics.form.validation.FormErrors;
import com.phloc.webctrls.bootstrap.derived.BootstrapButtonToolbarAdvanced;

public abstract class AbstractWebPageForm <DATATYPE extends IHasID <String>> extends AbstractWebPageExt
{
  // all internal IDs starting with "$" to prevent accidental overwrite with
  // actual field
  public static final String FIELD_FLOW_ID = AjaxHandlerSaveFormState.FIELD_FLOW_ID;
  public static final String FIELD_RESTORE_FLOW_ID = AjaxHandlerSaveFormState.FIELD_RESTORE_FLOW_ID;
  protected static final String INPUT_FORM_ID = "inputform";

  public AbstractWebPageForm (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  @Nullable
  private String _getSelectedObjectID ()
  {
    return getAttr (CHCParam.PARAM_OBJECT);
  }

  /**
   * Create toolbar for viewing an existing object. Contains the back button and
   * the edit button.
   * 
   * @param aDisplayLocale
   *        The display locale to use
   * @param bCanGoBack
   *        true to enable back button
   * @param bCanEdit
   *        true to enable edit button
   * @param sSelectedObjectID
   *        The ID of the selected object
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static BootstrapButtonToolbarAdvanced createViewToolbar (@Nonnull final Locale aDisplayLocale,
                                                                  final boolean bCanGoBack,
                                                                  final boolean bCanEdit,
                                                                  @Nonnull final String sSelectedObjectID)
  {
    final BootstrapButtonToolbarAdvanced aToolbar = new BootstrapButtonToolbarAdvanced ();
    if (bCanGoBack)
    {
      // Back to list
      aToolbar.addButtonBack (aDisplayLocale);
    }
    if (bCanEdit)
    {
      // Edit object
      aToolbar.addButtonEdit (aDisplayLocale, LinkUtils.getSelfHref (new SMap ().add (CHCParam.PARAM_ACTION,
                                                                                      ACTION_EDIT)
                                                                                .add (CHCParam.PARAM_OBJECT,
                                                                                      sSelectedObjectID)));
    }
    return aToolbar;
  }

  /**
   * Create toolbar for editing an existing object
   * 
   * @param sSelectedObjectID
   *        The ID of the selected object.
   * @param aDisplayLocale
   *        The display locale to use. Never <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static BootstrapButtonToolbarAdvanced createEditToolbar (@Nonnull final String sSelectedObjectID,
                                                                  @Nonnull final Locale aDisplayLocale)
  {
    final BootstrapButtonToolbarAdvanced aToolbar = new BootstrapButtonToolbarAdvanced ();
    aToolbar.addHiddenField (CHCParam.PARAM_ACTION, ACTION_EDIT);
    aToolbar.addHiddenField (CHCParam.PARAM_OBJECT, sSelectedObjectID);
    aToolbar.addHiddenField (CHCParam.PARAM_SUBACTION, ACTION_SAVE);
    // Save button
    aToolbar.addSubmitButtonSave (aDisplayLocale);
    // Cancel button
    aToolbar.addButtonCancel (aDisplayLocale);
    return aToolbar;
  }

  /**
   * Add additional buttons to the create toolbar
   * 
   * @param aToolbar
   *        The toolbar to where the buttons should be added
   */
  @OverrideOnDemand
  protected void addAdditionalCreateToolbarButtons (@Nonnull final BootstrapButtonToolbarAdvanced aToolbar)
  {}

  /**
   * Create toolbar for creating a new object
   * 
   * @param aSelectedObject
   *        Optional selected object. May be <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to use
   * @return Never <code>null</code>.
   */
  @Nonnull
  private BootstrapButtonToolbarAdvanced _createCreateToolbar (@Nullable final IHasID <String> aSelectedObject,
                                                               @Nonnull final Locale aDisplayLocale)
  {
    final BootstrapButtonToolbarAdvanced aToolbar = new BootstrapButtonToolbarAdvanced ();
    aToolbar.addHiddenField (CHCParam.PARAM_ACTION, ACTION_CREATE);
    if (aSelectedObject != null)
      aToolbar.addHiddenField (CHCParam.PARAM_OBJECT, aSelectedObject.getID ());
    aToolbar.addHiddenField (CHCParam.PARAM_SUBACTION, ACTION_SAVE);
    // Save button
    aToolbar.addSubmitButtonSave (aDisplayLocale);
    // Cancel button
    aToolbar.addButtonCancel (aDisplayLocale);

    addAdditionalCreateToolbarButtons (aToolbar);
    return aToolbar;
  }

  @OverrideOnDemand
  protected boolean isEditAllowed ()
  {
    return true;
  }

  /**
   * Get the selected object we're operating on
   * 
   * @param sID
   *        selected object ID
   * @return <code>null</code> if no such object exists
   */
  @Nullable
  protected abstract DATATYPE getSelectedObject (@Nullable String sID);

  /**
   * @param aSelectedObject
   * @param aDisplayLocale
   * @param aNodeList
   */
  protected abstract void showSelectedObject (@Nonnull DATATYPE aSelectedObject,
                                              @Nonnull Locale aDisplayLocale,
                                              @Nonnull HCNodeList aNodeList);

  /**
   * @param aFormErrors
   * @param aDisplayLocale
   * @param aNodeList
   * @param bEdit
   * @param aSelectedObject
   */
  protected abstract void validateAndSaveInputParameters (@Nonnull FormErrors aFormErrors,
                                                          @Nonnull Locale aDisplayLocale,
                                                          @Nonnull HCNodeList aNodeList,
                                                          boolean bEdit,
                                                          DATATYPE aSelectedObject);

  /**
   * Add additional form IDs (e.g. client and accounting area)
   * 
   * @param aForm
   *        the form to add the elements to
   */
  @OverrideOnDemand
  protected void addAdditionalInputFormIDs (@Nonnull final HCForm aForm)
  {}

  /**
   * @param aSelectedObject
   * @param aDisplayLocale
   * @param aForm
   * @param bEdit
   * @param bCopy
   * @param aFormErrors
   */
  protected abstract void showInputForm (DATATYPE aSelectedObject,
                                         @Nonnull Locale aDisplayLocale,
                                         @Nonnull HCForm aForm,
                                         boolean bEdit,
                                         boolean bCopy,
                                         @Nonnull FormErrors aFormErrors);

  /**
   * Perform object delete
   * 
   * @param aSelectedObject
   *        The object to be deleted. Never <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to use. Never <code>null</code>.
   * @param aNodeList
   *        The node list to append the output to. Never <code>null</code>.
   * @return <code>true</code> to show the list afterwards. <code>false</code>
   *         if the object list should not be shown.
   */
  @OverrideOnDemand
  protected boolean handleDeleteAction (@Nonnull final DATATYPE aSelectedObject,
                                        @Nonnull final Locale aDisplayLocale,
                                        @Nonnull final HCNodeList aNodeList)
  {
    return true;
  }

  /**
   * Perform object undelete
   * 
   * @param aSelectedObject
   *        The object to be undeleted. Never <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to use. Never <code>null</code>.
   * @param aNodeList
   *        The node list to append the output to. Never <code>null</code>.
   * @return <code>true</code> to show the list afterwards. <code>false</code>
   *         if the object list should not be shown.
   */
  @OverrideOnDemand
  protected boolean handleUndeleteAction (@Nonnull final DATATYPE aSelectedObject,
                                          @Nonnull final Locale aDisplayLocale,
                                          @Nonnull final HCNodeList aNodeList)
  {
    return true;
  }

  /**
   * Handle some other custom action
   * 
   * @param aSelectedObject
   *        The selected object. May be <code>null</code>.
   * @param aDisplayLocale
   *        The display locale to use. Never <code>null</code>.
   * @param aNodeList
   *        The node list to append the output to. Never <code>null</code>.
   * @return <code>true</code> to show the list afterwards. <code>false</code>
   *         if the object list should not be shown.
   */
  @OverrideOnDemand
  protected boolean handleCustomActions (@Nullable final DATATYPE aSelectedObject,
                                         @Nonnull final Locale aDisplayLocale,
                                         @Nonnull final HCNodeList aNodeList)
  {
    return true;
  }

  /**
   * @param aDisplayLocale
   * @param aNodeList
   */
  protected abstract void showListOfExistingObjects (@Nonnull Locale aDisplayLocale, @Nonnull HCNodeList aNodeList);

  @Override
  protected final void fillContent (@Nonnull final Locale aDisplayLocale, @Nonnull final HCNodeList aNodeList)
  {
    final DATATYPE aSelectedObject = getSelectedObject (_getSelectedObjectID ());
    final boolean bIsEditAllowed = isEditAllowed ();

    boolean bShowList = true;

    if (hasAction (ACTION_VIEW) && aSelectedObject != null)
    {
      // Valid object found - show details
      showSelectedObject (aSelectedObject, aDisplayLocale, aNodeList);

      // Toolbar on bottom
      aNodeList.addChild (createViewToolbar (aDisplayLocale, true, bIsEditAllowed, aSelectedObject.getID ()));

      bShowList = false;
    }
    else
      if (hasAction (ACTION_CREATE) ||
          (hasAction (ACTION_EDIT) && aSelectedObject != null && bIsEditAllowed) ||
          (hasAction (ACTION_COPY) && aSelectedObject != null))
      {
        // Create or edit a client
        final boolean bEdit = hasAction (ACTION_EDIT);
        final boolean bCopy = hasAction (ACTION_COPY);
        final FormErrors aFormErrors = new FormErrors ();
        boolean bShowInputForm = true;

        if (hasSubAction (CHCParam.ACTION_SAVE))
        {
          // try to save
          validateAndSaveInputParameters (aFormErrors, aDisplayLocale, aNodeList, bEdit, aSelectedObject);
          if (aFormErrors.isEmpty ())
          {
            // Save successful
            bShowInputForm = false;

            // Remove an optionally stored state
            FormStateManager.getInstance ().deleteFormState (getAttr (FIELD_FLOW_ID));
          }
          else
            aNodeList.addChild (createIncorrectInputBox (aDisplayLocale));
        }

        if (bShowInputForm)
        {
          // Show the input form. Either for the first time or because of form
          // errors a n-th time
          bShowList = false;
          final HCForm aForm = aNodeList.addAndReturnChild (createFormSelf ());
          aForm.setID (INPUT_FORM_ID);

          // The unique form ID, that allows to identify on "transaction"
          // -> Used only for "form state remembering"
          aForm.addChild (new HCHiddenField (new RequestField (FIELD_FLOW_ID, GlobalIDFactory.getNewStringID ())));

          addAdditionalInputFormIDs (aForm);

          // Is there as saved state to use?
          final String sRestoreFlowID = getAttr (FIELD_RESTORE_FLOW_ID);
          if (sRestoreFlowID != null)
          {
            final FormState aSavedState = FormStateManager.getInstance ().getFormStateOfID (sRestoreFlowID);
            if (aSavedState != null)
            {
              aForm.addChild (new HCScriptOnDocumentReady (JSFormHelper.setAllFormValues (INPUT_FORM_ID,
                                                                                          aSavedState.getAsAssocArray ())));
            }
          }

          showInputForm (aSelectedObject, aDisplayLocale, aForm, bEdit, bCopy, aFormErrors);

          // Toolbar on bottom
          aForm.addChild (bEdit ? createEditToolbar (aSelectedObject.getID (), aDisplayLocale)
                               : _createCreateToolbar (aSelectedObject, aDisplayLocale));
        }
      }
      else
        if (hasAction (ACTION_DELETE) && aSelectedObject != null)
        {
          bShowList = handleDeleteAction (aSelectedObject, aDisplayLocale, aNodeList);
        }
        else
          if (hasAction (ACTION_UNDELETE) && aSelectedObject != null)
          {
            bShowList = handleUndeleteAction (aSelectedObject, aDisplayLocale, aNodeList);
          }
          else
          {
            // Other proprietary actions
            bShowList = handleCustomActions (aSelectedObject, aDisplayLocale, aNodeList);
          }

    if (bShowList)
    {
      showListOfExistingObjects (aDisplayLocale, aNodeList);
    }
  }
}
