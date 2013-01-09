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
   * @param bCanGoBack
   *        true to enable back button
   * @param bCanEdit
   *        true to enable edit button
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static BootstrapButtonToolbarAdvanced createViewToolbar (final boolean bCanGoBack,
                                                                  final boolean bCanEdit,
                                                                  @Nonnull final String sSelectedObjectID)
  {
    final BootstrapButtonToolbarAdvanced aToolbar = new BootstrapButtonToolbarAdvanced ();
    if (bCanGoBack)
    {
      // Back to list
      aToolbar.addButtonBack ();
    }
    if (bCanEdit)
    {
      // Edit object
      aToolbar.addButtonEdit (LinkUtils.getSelfHref (new SMap ().add (CHCParam.PARAM_ACTION, ACTION_EDIT)
                                                                .add (CHCParam.PARAM_OBJECT, sSelectedObjectID)));
    }
    return aToolbar;
  }

  /**
   * Create toolbar for editing an existing object
   * 
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static BootstrapButtonToolbarAdvanced createEditToolbar (@Nonnull final String sSelectedObjectID)
  {
    final BootstrapButtonToolbarAdvanced aToolbar = new BootstrapButtonToolbarAdvanced ();
    aToolbar.addHiddenField (CHCParam.PARAM_ACTION, ACTION_EDIT);
    aToolbar.addHiddenField (CHCParam.PARAM_OBJECT, sSelectedObjectID);
    aToolbar.addHiddenField (CHCParam.PARAM_SUBACTION, ACTION_SAVE);
    // Save button
    aToolbar.addSubmitButtonSave ();
    // Cancel button
    aToolbar.addButtonCancel ();
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
   * @return Never <code>null</code>.
   */
  @Nonnull
  private BootstrapButtonToolbarAdvanced _createCreateToolbar (@Nullable final IHasID <String> aSelectedObject)
  {
    final BootstrapButtonToolbarAdvanced aToolbar = new BootstrapButtonToolbarAdvanced ();
    aToolbar.addHiddenField (CHCParam.PARAM_ACTION, ACTION_CREATE);
    if (aSelectedObject != null)
      aToolbar.addHiddenField (CHCParam.PARAM_OBJECT, aSelectedObject.getID ());
    aToolbar.addHiddenField (CHCParam.PARAM_SUBACTION, ACTION_SAVE);
    // Save button
    aToolbar.addSubmitButtonSave ();
    // Cancel button
    aToolbar.addButtonCancel ();

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
   * @param aSelectedObject
   * @param aDisplayLocale
   * @param aNodeList
   * @return <code>true</code> to show the list afterwards
   */
  @OverrideOnDemand
  protected boolean handleDeleteAction (@Nonnull final DATATYPE aSelectedObject,
                                        @Nonnull final Locale aDisplayLocale,
                                        @Nonnull final HCNodeList aNodeList)
  {
    return true;
  }

  /**
   * @param aDisplayLocale
   * @param aNodeList
   * @return <code>true</code> to show the list afterwards
   */
  @OverrideOnDemand
  protected boolean handleCustomActions (@Nonnull final Locale aDisplayLocale, @Nonnull final HCNodeList aNodeList)
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
      aNodeList.addChild (createViewToolbar (true, bIsEditAllowed, aSelectedObject.getID ()));

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
        boolean bShowForm = true;

        if (hasSubAction (CHCParam.ACTION_SAVE))
        {
          // try to save
          validateAndSaveInputParameters (aFormErrors, aDisplayLocale, aNodeList, bEdit, aSelectedObject);
          if (aFormErrors.isEmpty ())
          {
            // Save successful
            bShowForm = false;

            // Remove an optionally stored state
            FormStateManager.getInstance ().deleteFormState (getAttr (FIELD_FLOW_ID));
          }
          else
            aNodeList.addChild (createIncorrectInputBox (aDisplayLocale));
        }

        if (bShowForm)
        {
          // Show the input form. Either for the first time or because of form
          // errors a n-th time
          bShowList = false;
          final HCForm aForm = aNodeList.addAndReturnChild (createFormSelf ());
          aForm.setID (INPUT_FORM_ID);

          // The unique form ID, that allows to identify on "transaction"
          // -> Used only for "form state remembering"
          aForm.addChild (new HCHiddenField (new RequestField (FIELD_FLOW_ID, GlobalIDFactory.getNewStringID ())));

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
          aForm.addChild (bEdit ? createEditToolbar (aSelectedObject.getID ()) : _createCreateToolbar (aSelectedObject));
        }
      }
      else
        if (hasAction (ACTION_DELETE) && aSelectedObject != null)
        {
          bShowList = handleDeleteAction (aSelectedObject, aDisplayLocale, aNodeList);
        }
        else
        {
          // Other proprietary actions
          bShowList = handleCustomActions (aDisplayLocale, aNodeList);
        }

    if (bShowList)
    {
      showListOfExistingObjects (aDisplayLocale, aNodeList);
    }
  }
}
