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
package com.phloc.webpages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCHiddenField;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.validation.error.FormErrors;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webbasics.form.FormState;
import com.phloc.webbasics.form.FormStateManager;
import com.phloc.webbasics.form.RequestField;
import com.phloc.webbasics.form.ajax.AjaxHandlerSaveFormState;
import com.phloc.webctrls.custom.toolbar.IButtonToolbar;
import com.phloc.webctrls.js.JSFormHelper;

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

  public AbstractWebPageForm (@Nonnull @Nonempty final String sID, @Nonnull final IReadonlyMultiLingualText aName)
  {
    super (sID, aName);
  }

  public AbstractWebPageForm (@Nonnull @Nonempty final String sID,
                              @Nonnull final String sName,
                              @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public AbstractWebPageForm (@Nonnull @Nonempty final String sID,
                              @Nonnull final IReadonlyMultiLingualText aName,
                              @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  /**
   * @return <code>true</code> if the form for
   *         {@link #showInputForm(WebPageExecutionContext, IHasID, HCForm, boolean, boolean, FormErrors)}
   *         should be a file-upload form, <code>false</code> if a regular form
   *         is sufficient.
   */
  @OverrideOnDemand
  protected boolean isFileUploadForm ()
  {
    return false;
  }

  @Nullable
  protected final String getSelectedObjectID (@Nonnull final WebPageExecutionContext aWPEC)
  {
    return aWPEC.getAttr (CHCParam.PARAM_OBJECT);
  }

  /**
   * @return A newly created toolbar. May be overridden to create other types of
   *         toolbars :). May not be <code>null</code>.
   */
  @Nonnull
  @OverrideOnDemand
  protected IButtonToolbar <?> createNewViewToolbar ()
  {
    return getStyler ().createToolbar ();
  }

  /**
   * @param aWPEC
   *        The web page execution context
   * @param aSelectedObject
   *        The selected object
   * @return <code>true</code> to show the view toolbar, <code>false</code> to
   *         draw your own toolbar
   */
  @OverrideOnDemand
  protected boolean showViewToolbar (@Nonnull final WebPageExecutionContext aWPEC,
                                     @Nonnull final DATATYPE aSelectedObject)
  {
    return true;
  }

  /**
   * Add additional elements to the view toolbar
   * 
   * @param aWPEC
   *        The web page execution context
   * @param aSelectedObject
   *        The selected object
   * @param aToolbar
   *        The toolbar to be modified
   */
  @OverrideOnDemand
  protected void modifyViewToolbar (@Nonnull final WebPageExecutionContext aWPEC,
                                    @Nonnull final DATATYPE aSelectedObject,
                                    @Nonnull final IButtonToolbar <?> aToolbar)
  {}

  /**
   * Create toolbar for viewing an existing object. Contains the back button and
   * the edit button.
   * 
   * @param aWPEC
   *        The display locale to use
   * @param bCanGoBack
   *        true to enable back button
   * @param bCanEdit
   *        true to enable edit button
   * @param aSelectedObject
   *        The selected object
   * @return Never <code>null</code>.
   */
  @Nonnull
  @OverrideOnDemand
  protected IButtonToolbar <?> createViewToolbar (@Nonnull final WebPageExecutionContext aWPEC,
                                                  final boolean bCanGoBack,
                                                  final boolean bCanEdit,
                                                  @Nonnull final DATATYPE aSelectedObject)
  {
    final IButtonToolbar <?> aToolbar = createNewViewToolbar ();
    if (bCanGoBack)
    {
      // Back to list
      aToolbar.addButtonBack (aWPEC.getDisplayLocale ());
    }
    if (bCanEdit)
    {
      // Edit object
      aToolbar.addButtonEdit (aWPEC.getDisplayLocale (), createEditURL (aSelectedObject));
    }
    modifyViewToolbar (aWPEC, aSelectedObject, aToolbar);
    return aToolbar;
  }

  /**
   * @return A newly created toolbar. May be overridden to create other types of
   *         toolbars :). May not be <code>null</code>.
   */
  @Nonnull
  @OverrideOnDemand
  protected IButtonToolbar <?> createNewEditToolbar ()
  {
    return getStyler ().createToolbar ();
  }

  /**
   * @param aWPEC
   *        The web page execution context
   * @param aSelectedObject
   *        The selected object
   * @return <code>true</code> to show the edit toolbar, <code>false</code> to
   *         draw your own toolbar
   */
  @OverrideOnDemand
  protected boolean showEditToolbar (@Nonnull final WebPageExecutionContext aWPEC,
                                     @Nonnull final DATATYPE aSelectedObject)
  {
    return true;
  }

  /**
   * Add additional elements to the edit toolbar
   * 
   * @param aWPEC
   *        The web page execution context
   * @param aSelectedObject
   *        The selected object. Never <code>null</code>.
   * @param aToolbar
   *        The toolbar to be modified
   */
  @OverrideOnDemand
  protected void modifyEditToolbar (@Nonnull final WebPageExecutionContext aWPEC,
                                    @Nonnull final DATATYPE aSelectedObject,
                                    @Nonnull final IButtonToolbar <?> aToolbar)
  {}

  /**
   * Create toolbar for editing an existing object
   * 
   * @param aWPEC
   *        The web page execution context. Never <code>null</code>.
   * @param aSelectedObject
   *        The selected object. Never <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @OverrideOnDemand
  protected IButtonToolbar <?> createEditToolbar (@Nonnull final WebPageExecutionContext aWPEC,
                                                  @Nonnull final DATATYPE aSelectedObject)
  {
    final IButtonToolbar <?> aToolbar = createNewEditToolbar ();
    aToolbar.addHiddenField (CHCParam.PARAM_ACTION, ACTION_EDIT);
    aToolbar.addHiddenField (CHCParam.PARAM_OBJECT, aSelectedObject.getID ());
    aToolbar.addHiddenField (CHCParam.PARAM_SUBACTION, ACTION_SAVE);
    // Save button
    aToolbar.addSubmitButtonSave (aWPEC.getDisplayLocale ());
    // Cancel button
    aToolbar.addButtonCancel (aWPEC.getDisplayLocale ());
    modifyEditToolbar (aWPEC, aSelectedObject, aToolbar);
    return aToolbar;
  }

  /**
   * @param aWPEC
   *        The web page execution context
   * @param aSelectedObject
   *        The selected object. May be <code>null</code>.
   * @return <code>true</code> to show the create toolbar, <code>false</code> to
   *         draw your own toolbar
   */
  @OverrideOnDemand
  protected boolean showCreateToolbar (@Nonnull final WebPageExecutionContext aWPEC,
                                       @Nullable final DATATYPE aSelectedObject)
  {
    return true;
  }

  /**
   * @return A newly created toolbar. May be overridden to create other types of
   *         toolbars :). May not be <code>null</code>.
   */
  @Nonnull
  @OverrideOnDemand
  protected IButtonToolbar <?> createNewCreateToolbar ()
  {
    return getStyler ().createToolbar ();
  }

  /**
   * Add additional elements to the create toolbar
   * 
   * @param aWPEC
   *        The web page execution context
   * @param aToolbar
   *        The toolbar to be modified
   */
  @OverrideOnDemand
  protected void modifyCreateToolbar (@Nonnull final WebPageExecutionContext aWPEC,
                                      @Nonnull final IButtonToolbar <?> aToolbar)
  {}

  /**
   * Create toolbar for creating a new object
   * 
   * @param aWPEC
   *        The web page execution context
   * @param aSelectedObject
   *        Optional selected object. May be <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  @OverrideOnDemand
  protected IButtonToolbar <?> createCreateToolbar (@Nonnull final WebPageExecutionContext aWPEC,
                                                    @Nullable final DATATYPE aSelectedObject)
  {
    final IButtonToolbar <?> aToolbar = createNewCreateToolbar ();
    aToolbar.addHiddenField (CHCParam.PARAM_ACTION, ACTION_CREATE);
    if (aSelectedObject != null)
      aToolbar.addHiddenField (CHCParam.PARAM_OBJECT, aSelectedObject.getID ());
    aToolbar.addHiddenField (CHCParam.PARAM_SUBACTION, ACTION_SAVE);
    // Save button
    aToolbar.addSubmitButtonSave (aWPEC.getDisplayLocale ());
    // Cancel button
    aToolbar.addButtonCancel (aWPEC.getDisplayLocale ());

    modifyCreateToolbar (aWPEC, aToolbar);
    return aToolbar;
  }

  /**
   * Check if editing the passed object is allowed.
   * 
   * @param aSelectedObject
   *        The currently selected object. May be <code>null</code>.
   * @return <code>true</code> if edit is allowed, <code>false</code> if not
   */
  @OverrideOnDemand
  protected boolean isEditAllowed (@Nullable final DATATYPE aSelectedObject)
  {
    return true;
  }

  /**
   * Get the selected object we're operating on
   * 
   * @param aWPEC
   *        The web page execution context
   * @param sID
   *        selected object ID
   * @return <code>null</code> if no such object exists
   */
  @Nullable
  protected abstract DATATYPE getSelectedObject (@Nonnull WebPageExecutionContext aWPEC, @Nullable String sID);

  /**
   * @param aWPEC
   *        The web page execution context
   * @param aSelectedObject
   *        The currently selected object
   */
  protected abstract void showSelectedObject (@Nonnull WebPageExecutionContext aWPEC, @Nonnull DATATYPE aSelectedObject);

  protected final void handleViewObject (@Nonnull final WebPageExecutionContext aWPEC,
                                         @Nonnull final DATATYPE aSelectedObject,
                                         final boolean bIsEditAllowed)
  {
    // Valid object found - show details
    showSelectedObject (aWPEC, aSelectedObject);

    if (showViewToolbar (aWPEC, aSelectedObject))
    {
      // Toolbar on bottom
      aWPEC.getNodeList ().addChild (createViewToolbar (aWPEC, true, bIsEditAllowed, aSelectedObject));
    }
  }

  /**
   * @param aWPEC
   *        Web page execution context
   * @param aSelectedObject
   *        The currently selected object. May be <code>null</code> when
   *        creating a new object
   * @param aFormErrors
   *        Object for storing the validation error
   * @param bEdit
   *        <code>true</code> if in edit mode, <code>false</code> if in create
   *        or copy mode
   */
  protected abstract void validateAndSaveInputParameters (@Nonnull WebPageExecutionContext aWPEC,
                                                          @Nullable DATATYPE aSelectedObject,
                                                          @Nonnull FormErrors aFormErrors,
                                                          boolean bEdit);

  /**
   * Add additional form IDs (e.g. client and accounting area). This method is
   * called before
   * {@link #showInputForm(WebPageExecutionContext, IHasID, HCForm, boolean, boolean, FormErrors)}
   * is called.
   * 
   * @param aForm
   *        the form to add the elements to
   */
  @OverrideOnDemand
  protected void modifyFormBeforeShowInputForm (@Nonnull final HCForm aForm)
  {}

  /**
   * Show the input form for a new or existing object.
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
   */
  protected abstract void showInputForm (@Nonnull WebPageExecutionContext aWPEC,
                                         @Nullable DATATYPE aSelectedObject,
                                         @Nonnull HCForm aForm,
                                         boolean bEdit,
                                         boolean bCopy,
                                         @Nonnull FormErrors aFormErrors);

  /**
   * Perform object delete
   * 
   * @param aWPEC
   *        The web page execution context
   * @param aSelectedObject
   *        The object to be deleted. Never <code>null</code>.
   * @return <code>true</code> to show the list afterwards. <code>false</code>
   *         if the object list should not be shown.
   */
  @OverrideOnDemand
  protected boolean handleDeleteAction (@Nonnull final WebPageExecutionContext aWPEC,
                                        @Nonnull final DATATYPE aSelectedObject)
  {
    return true;
  }

  /**
   * Perform object undelete
   * 
   * @param aWPEC
   *        The web page execution context
   * @param aSelectedObject
   *        The object to be undeleted. Never <code>null</code>.
   * @return <code>true</code> to show the list afterwards. <code>false</code>
   *         if the object list should not be shown.
   */
  @OverrideOnDemand
  protected boolean handleUndeleteAction (@Nonnull final WebPageExecutionContext aWPEC,
                                          @Nonnull final DATATYPE aSelectedObject)
  {
    return true;
  }

  /**
   * Handle some other custom action
   * 
   * @param aWPEC
   *        The web page execution context
   * @param aSelectedObject
   *        The selected object. May be <code>null</code>.
   * @return <code>true</code> to show the list afterwards. <code>false</code>
   *         if the object list should not be shown.
   */
  @OverrideOnDemand
  protected boolean handleCustomActions (@Nonnull final WebPageExecutionContext aWPEC,
                                         @Nullable final DATATYPE aSelectedObject)
  {
    return true;
  }

  /**
   * @param aWPEC
   *        The web page execution context. Never <code>null</code>.
   */
  protected abstract void showListOfExistingObjects (@Nonnull WebPageExecutionContext aWPEC);

  @Override
  protected final void fillContent (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final DATATYPE aSelectedObject = getSelectedObject (aWPEC, getSelectedObjectID (aWPEC));
    final boolean bIsEditAllowed = isEditAllowed (aSelectedObject);
    boolean bShowList = true;

    if (aWPEC.hasAction (ACTION_VIEW) && aSelectedObject != null)
    {
      // Valid object found - show details
      handleViewObject (aWPEC, aSelectedObject, bIsEditAllowed);

      bShowList = false;
    }
    else
      if (aWPEC.hasAction (ACTION_CREATE) ||
          (aWPEC.hasAction (ACTION_EDIT) && aSelectedObject != null && bIsEditAllowed) ||
          (aWPEC.hasAction (ACTION_COPY) && aSelectedObject != null))
      {
        // Create or edit a client
        final boolean bEdit = aWPEC.hasAction (ACTION_EDIT);
        final boolean bCopy = aWPEC.hasAction (ACTION_COPY);
        final FormErrors aFormErrors = new FormErrors ();
        boolean bShowInputForm = true;

        if (aWPEC.hasSubAction (CHCParam.ACTION_SAVE))
        {
          // try to save
          validateAndSaveInputParameters (aWPEC, aSelectedObject, aFormErrors, bEdit);
          if (aFormErrors.isEmpty ())
          {
            // Save successful
            bShowInputForm = false;

            // Remove an optionally stored state
            FormStateManager.getInstance ().deleteFormState (aWPEC.getAttr (FIELD_FLOW_ID));
          }
          else
          {
            // Show: changes could not be saved...
            aWPEC.getNodeList ().addChild (getStyler ().createIncorrectInputBox (aWPEC.getDisplayLocale ()));
          }
        }

        if (bShowInputForm)
        {
          // Show the input form. Either for the first time or because of form
          // errors a n-th time
          bShowList = false;
          final HCForm aForm = isFileUploadForm () ? createFormFileUploadSelf () : createFormSelf ();
          aWPEC.getNodeList ().addChild (aForm);
          aForm.setID (INPUT_FORM_ID);

          // The unique form ID, that allows to identify on "transaction"
          // -> Used only for "form state remembering"
          aForm.addChild (new HCHiddenField (new RequestField (FIELD_FLOW_ID, GlobalIDFactory.getNewStringID ())));

          modifyFormBeforeShowInputForm (aForm);

          // Is there as saved state to use?
          final String sRestoreFlowID = aWPEC.getAttr (FIELD_RESTORE_FLOW_ID);
          if (sRestoreFlowID != null)
          {
            final FormState aSavedState = FormStateManager.getInstance ().getFormStateOfID (sRestoreFlowID);
            if (aSavedState != null)
            {
              aForm.addChild (new HCScriptOnDocumentReady (JSFormHelper.setAllFormValues (INPUT_FORM_ID,
                                                                                          aSavedState.getAsAssocArray ())));
            }
          }

          showInputForm (aWPEC, aSelectedObject, aForm, bEdit, bCopy, aFormErrors);

          // Toolbar on bottom
          if (bEdit)
          {
            if (showEditToolbar (aWPEC, aSelectedObject))
              aForm.addChild (createEditToolbar (aWPEC, aSelectedObject));
          }
          else
          {
            if (showCreateToolbar (aWPEC, aSelectedObject))
              aForm.addChild (createCreateToolbar (aWPEC, aSelectedObject));
          }
        }
      }
      else
        if (aWPEC.hasAction (ACTION_DELETE) && aSelectedObject != null)
        {
          bShowList = handleDeleteAction (aWPEC, aSelectedObject);
        }
        else
          if (aWPEC.hasAction (ACTION_UNDELETE) && aSelectedObject != null)
          {
            bShowList = handleUndeleteAction (aWPEC, aSelectedObject);
          }
          else
          {
            // Other proprietary actions
            bShowList = handleCustomActions (aWPEC, aSelectedObject);
          }

    if (bShowList)
    {
      showListOfExistingObjects (aWPEC);
    }
  }
}
