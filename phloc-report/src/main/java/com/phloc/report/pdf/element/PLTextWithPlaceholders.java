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
package com.phloc.report.pdf.element;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.string.StringHelper;
import com.phloc.report.pdf.render.ERenderingOption;
import com.phloc.report.pdf.render.RenderingContext;
import com.phloc.report.pdf.spec.FontSpec;

public class PLTextWithPlaceholders extends PLText
{
  public PLTextWithPlaceholders (@Nonnull final String sText, @Nonnull final FontSpec aFont)
  {
    super (sText, aFont);
  }

  @Override
  @OverrideOnDemand
  protected String getTextToDraw (@Nonnull final String sText, @Nonnull final RenderingContext aCtx)
  {
    final Map <String, String> aReplacements = new HashMap <String, String> ();
    // Check if any of the ERenderingOption elements is contained in the text
    for (final ERenderingOption eOption : ERenderingOption.values ())
    {
      final String sCtxValue = aCtx.getOption (eOption);
      if (sCtxValue != null)
        aReplacements.put (eOption.getPlaceholder (), sCtxValue);
    }
    // Replace all at once
    return StringHelper.replaceMultiple (sText, aReplacements);
  }
}
