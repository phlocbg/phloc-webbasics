/**
 * Copyright (C) 2006-2012 phloc systems
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
import com.phloc.webbasics.form.FormState;
import com.phloc.webbasics.form.FormStateManager;

public class AjaxHandlerSaveFormState extends AbstractAjaxHandler
{
  public static final String FIELD_FLOW_ID = "$flowid";
  public static final String FIELD_RESTORE_FLOW_ID = "$restoreflowid";
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
  protected AjaxDefaultResponse mainHandleRequest (@Nonnull final MapBasedAttributeContainer aParams) throws Exception
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
        String sFieldName = aEntry.getKey ().substring (PREFIX_FIELD.length ());
        // Skip the potential array suffix (for multi selects)
        sFieldName = StringHelper.trimEnd (sFieldName, "[]");
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
