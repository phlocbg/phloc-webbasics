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
package com.phloc.webctrls.custom;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCRequestField;
import com.phloc.html.hc.html.HCOption;
import com.phloc.html.hc.html.HCSelect;
import com.phloc.webbasics.EWebBasicsText;

public class ExtHCSelect extends HCSelect
{
  public static final ICSSClassProvider SPECIAL_OPTION = DefaultCSSClassProvider.create ("select-option-special");

  public ExtHCSelect (@Nonnull final IHCRequestField aRF)
  {
    super (aRF);
  }

  @Nonnull
  public static HCOption createOptionPleaseSelect (@Nonnull final Locale aDisplayLocale)
  {
    final HCOption aOption = new HCOption ().setValue ("")
                                            .addChild (EWebBasicsText.PLEASE_SELECT.getDisplayText (aDisplayLocale));
    aOption.addClass (SPECIAL_OPTION);
    return aOption;
  }

  @Nonnull
  public HCOption addOptionPleaseSelect (@Nonnull final Locale aDisplayLocale)
  {
    return addOptionAtIndex (0, createOptionPleaseSelect (aDisplayLocale));
  }

  @Nonnull
  public static HCOption createOptionNone (@Nonnull final Locale aDisplayLocale)
  {
    final HCOption aOption = new HCOption ().setValue ("")
                                            .addChild (EWebBasicsText.SELECT_NONE.getDisplayText (aDisplayLocale));
    aOption.addClass (SPECIAL_OPTION);
    return aOption;
  }

  @Nonnull
  public HCOption addOptionNone (@Nonnull final Locale aDisplayLocale)
  {
    return addOptionAtIndex (0, createOptionNone (aDisplayLocale));
  }
}
