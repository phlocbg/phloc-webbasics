/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.appbasics.migration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joda.time.DateTime;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.microdom.utils.MicroUtils;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.StringParser;

public final class SystemMigrationResultMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ATTR_MIGRATION_ID = "id";
  private static final String ATTR_EXECUTION_DT = "executiondt";
  private static final String ATTR_SUCCESS = "success";
  private static final String ELEMENT_ERROR_MSG = "errormsg";

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull @Nonempty final String sTagName)
  {
    final SystemMigrationResult aValue = (SystemMigrationResult) aObject;
    final IMicroElement aElement = new MicroElement (sNamespaceURI, sTagName);
    aElement.setAttribute (ATTR_MIGRATION_ID, aValue.getID ());
    aElement.setAttributeWithConversion (ATTR_EXECUTION_DT, aValue.getExecutionDateTime ());
    aElement.setAttribute (ATTR_SUCCESS, Boolean.toString (aValue.isSuccess ()));
    if (StringHelper.hasText (aValue.getErrorMessage ()))
      aElement.appendElement (sNamespaceURI, ELEMENT_ERROR_MSG).appendText (aValue.getErrorMessage ());
    return aElement;
  }

  @Nonnull
  public SystemMigrationResult convertToNative (@Nonnull final IMicroElement aElement)
  {
    final String sID = aElement.getAttribute (ATTR_MIGRATION_ID);
    final DateTime aExecDT = aElement.getAttributeWithConversion (ATTR_EXECUTION_DT, DateTime.class);
    final String sSuccess = aElement.getAttribute (ATTR_SUCCESS);
    final boolean bSuccess = StringParser.parseBool (sSuccess);
    final String sErrorMsg = MicroUtils.getChildTextContent (aElement, ELEMENT_ERROR_MSG);
    return new SystemMigrationResult (sID, aExecDT, bSuccess, sErrorMsg);
  }
}
