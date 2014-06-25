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
package com.phloc.webctrls.autosize;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;
import com.phloc.html.hc.html.HCTextArea;
import com.phloc.html.request.IHCRequestField;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;

/**
 * jQuery autosize plugin from
 * 
 * <pre>
 * http://www.jacklmoore.com/autosize
 * </pre>
 * 
 * @author Philip Helger
 */
public class HCTextAreaAutosize extends HCTextArea
{
  public HCTextAreaAutosize (@Nullable final String sName)
  {
    super (sName);
  }

  public HCTextAreaAutosize (@Nullable final String sName, @Nullable final String sValue)
  {
    super (sName, sValue);
  }

  public HCTextAreaAutosize (@Nonnull final IHCRequestField aRF)
  {
    super (aRF);
  }

  @Override
  @OverrideOnDemand
  protected void internalBeforeConvertToNode (@Nonnull final IHCConversionSettingsToNode aConversionSettings)
  {
    super.internalBeforeConvertToNode (aConversionSettings);
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EAutosizeJSPathProvider.AUTOSIZE);
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EAutosizeJSPathProvider.AUTOSIZE_ALL);
  }
}
