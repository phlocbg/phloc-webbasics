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
package com.phloc.webdemoapp.page;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.name.IHasDisplayName;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.js.builder.JSArray;
import com.phloc.webbasics.app.layout.CLayout;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webbasics.form.ajax.AjaxHandlerSaveFormState;
import com.phloc.webctrls.bootstrap.BootstrapTable;
import com.phloc.webctrls.bootstrap.derived.BootstrapErrorBox;
import com.phloc.webctrls.bootstrap.derived.BootstrapInfoBox;
import com.phloc.webctrls.bootstrap.derived.BootstrapToolbarAdvanced;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webctrls.datatables.ajax.AjaxHandlerDataTables;
import com.phloc.webctrls.page.AbstractWebPageForm;
import com.phloc.webctrls.page.JSFormHelper;
import com.phloc.webdemoapp.app.ajax.config.CDemoAppAjaxConfig;
import com.phloc.webdemoapp.app.ajax.view.CDemoAppAjaxView;

public abstract class AbstractEcowareFormPage <DATATYPE extends IHasID <String>> extends AbstractWebPageForm <DATATYPE>
{
  public AbstractEcowareFormPage (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  @Override
  @Nullable
  public String getHeaderText (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final DATATYPE aSelectedObject = getSelectedObject (aWPEC, getSelectedObjectID (aWPEC));
    if (aSelectedObject instanceof IHasDisplayName)
      return ((IHasDisplayName) aSelectedObject).getDisplayName ();
    return super.getHeaderText (aWPEC);
  }

  @Override
  protected void modifyCreateToolbar (@Nonnull final WebPageExecutionContext aWPEC,
                                      @Nonnull final BootstrapToolbarAdvanced aToolbar)
  {
    final JSArray aSuccessUpdates = new JSArray ();
    // Update menu via Ajax
    aSuccessUpdates.add (JSFormHelper.createUpdateParam (CLayout.LAYOUT_AREAID_MENU,
                                                         CDemoAppAjaxConfig.CONFIG_UPDATE_MENU_VIEW));

    // Update special area directly with code
    IHCNode aSpecialNode = BootstrapInfoBox.create ("Die Daten wurden erfolgreich gemerkt!");
    aSuccessUpdates.add (JSFormHelper.createUpdateParam (CLayout.LAYOUT_AREAID_SPECIAL, aSpecialNode));
    final JSArray aFailureUpdates = new JSArray ();
    // Update special area directly with code
    aSpecialNode = BootstrapErrorBox.create ("Fehler beim Merken der Daten!");
    aFailureUpdates.add (JSFormHelper.createUpdateParam (CLayout.LAYOUT_AREAID_SPECIAL, aSpecialNode));
    aToolbar.addButton ("Merken", JSFormHelper.saveFormData (INPUT_FORM_ID,
                                                             AjaxHandlerSaveFormState.PREFIX_FIELD,
                                                             getID (),
                                                             CDemoAppAjaxConfig.CONFIG_SAVE_FORM_STATE,
                                                             aSuccessUpdates,
                                                             aFailureUpdates), EDefaultIcon.SAVE);
  }

  @Nonnull
  public static DataTables createDataTables (@Nonnull final BootstrapTable aTable, @Nonnull final Locale aDisplayLocale)
  {
    final DataTables ret = createBootstrapDataTables (aTable, aDisplayLocale);
    ret.setUseJQueryAjax (true)
       .setAjaxSource (CDemoAppAjaxView.VIEW_DATATABLES.getInvocationURL ())
       .setServerParams (ContainerHelper.newMap (AjaxHandlerDataTables.OBJECT_ID, aTable.getID ()))
       .addAllColumns (aTable);
    return ret;
  }
}
