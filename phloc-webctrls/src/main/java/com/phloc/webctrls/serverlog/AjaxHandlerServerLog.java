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
package com.phloc.webctrls.serverlog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.log.LogUtils;
import com.phloc.commons.string.StringHelper;
import com.phloc.webbasics.ajax.AbstractAjaxHandler;
import com.phloc.webbasics.ajax.AjaxDefaultResponse;
import com.phloc.webbasics.ajax.IAjaxResponse;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

public class AjaxHandlerServerLog extends AbstractAjaxHandler
{
  private static final EErrorLevel DEFAULT_SEVERITY = EErrorLevel.INFO;
  private static final String PARAM_SEVERITY = "severity";
  private static final String PARAM_MESSAGE = "message";
  private static final String PARAM_KEY = "key";

  @Nonnull
  public static EErrorLevel getErrorLevelFromString (@Nullable final String sSeverity)
  {
    if (StringHelper.hasText (sSeverity))
    {
      if ("fatal".equalsIgnoreCase (sSeverity))
        return EErrorLevel.FATAL_ERROR;
      if ("error".equalsIgnoreCase (sSeverity))
        return EErrorLevel.ERROR;
      if ("warn".equalsIgnoreCase (sSeverity) || "warning".equalsIgnoreCase (sSeverity))
        return EErrorLevel.WARN;
      if ("info".equalsIgnoreCase (sSeverity))
        return EErrorLevel.INFO;
      if ("success".equalsIgnoreCase (sSeverity) ||
          "debug".equalsIgnoreCase (sSeverity) ||
          "trace".equalsIgnoreCase (sSeverity))
        return EErrorLevel.SUCCESS;
    }
    return DEFAULT_SEVERITY;
  }

  @Override
  @Nonnull
  protected IAjaxResponse mainHandleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                             @Nonnull final MapBasedAttributeContainer aParams) throws Exception
  {
    final String sSeverity = aParams.getAttributeAsString (PARAM_SEVERITY);
    final String sMessage = aParams.getAttributeAsString (PARAM_MESSAGE);
    final String sKey = aParams.getAttributeAsString (PARAM_KEY);
    final String sExpectedKey = ServerLogSessionKey.getGeneratedSessionKey ();
    if (StringHelper.hasNoText (sMessage) || sExpectedKey == null || !sExpectedKey.equals (sKey))
      return AjaxDefaultResponse.createError (null);

    // Main logging
    final EErrorLevel eSeverity = getErrorLevelFromString (sSeverity);
    LogUtils.log (AjaxHandlerServerLog.class, eSeverity, sMessage);

    // Convert the response to JSON
    return AjaxDefaultResponse.createSuccess (aRequestScope);
  }
}
