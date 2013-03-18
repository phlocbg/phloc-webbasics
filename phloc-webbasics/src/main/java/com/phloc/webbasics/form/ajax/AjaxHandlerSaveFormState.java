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
package com.phloc.webbasics.form.ajax;

import java.util.Map;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.CHCParam;
import com.phloc.webbasics.ajax.AbstractAjaxHandler;
import com.phloc.webbasics.ajax.AjaxDefaultResponse;
import com.phloc.webbasics.ajax.IAjaxResponse;
import com.phloc.webbasics.form.FormState;
import com.phloc.webbasics.form.FormStateManager;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

public class AjaxHandlerSaveFormState extends AbstractAjaxHandler
{
  /** Special field for the flow ID */
  public static final String FIELD_FLOW_ID = "$flowid";
  /** Special field to restore a flow ID */
  public static final String FIELD_RESTORE_FLOW_ID = "$restoreflowid";
  /** Special prefix for field names */
  public static final String PREFIX_FIELD = "field-";
  // Same as in form.js!
  private static final String ATTR_PAGE_ID = "$pageID";

  @OverrideOnDemand
  protected void saveFormState (@Nonnull final String sPageID,
                                @Nonnull final String sFlowID,
                                @Nonnull final MapBasedAttributeContainer aFieldCont)
  {
    FormStateManager.getInstance ().saveFormState (new FormState (sPageID, sFlowID, aFieldCont));
  }

  @Override
  @Nonnull
  protected IAjaxResponse mainHandleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                             @Nonnull final MapBasedAttributeContainer aParams) throws Exception
  {
    // Extract page ID
    final String sPageID = aParams.getAttributeAsString (ATTR_PAGE_ID);
    if (sPageID == null)
      return AjaxDefaultResponse.createError ("Page ID is missing!");

    // Filter all fields
    final MapBasedAttributeContainer aFieldCont = new MapBasedAttributeContainer ();
    for (final Map.Entry <String, Object> aEntry : aParams.getAllAttributes ().entrySet ())
      if (aEntry.getKey ().startsWith (PREFIX_FIELD))
      {
        // Skip the prefix
        final String sFieldName = aEntry.getKey ().substring (PREFIX_FIELD.length ());
        // Array value are suffixes with "[]" which is important, as they must
        // be restored as array values!
        // This affects checkboxes, radio buttons and multi selects
        if (StringHelper.hasText (sFieldName))
          aFieldCont.setAttribute (sFieldName, aEntry.getValue ());
      }

    // Extract the flow ID
    final String sFlowID = aFieldCont.getAttributeAsString (FIELD_FLOW_ID);
    if (sFlowID == null)
      return AjaxDefaultResponse.createError ("Flow ID is missing!");
    aFieldCont.removeAttribute (FIELD_FLOW_ID);
    aFieldCont.removeAttribute (CHCParam.PARAM_SUBACTION);
    // Leave action and object

    saveFormState (sPageID, sFlowID, aFieldCont);
    return AjaxDefaultResponse.createSuccess (null);
  }
}
