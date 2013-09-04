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
package com.phloc.webctrls.custom;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.html.HCOption;
import com.phloc.html.hc.html.HCSelect;
import com.phloc.html.request.IHCRequestField;
import com.phloc.webbasics.EWebBasicsText;

public class HCExtSelect extends HCSelect
{
  /** The value of the "please select" field */
  public static final String VALUE_PLEASE_SELECT = "";
  /** The value of the "none" field */
  public static final String VALUE_NONE = "";

  public static final ICSSClassProvider CSS_CLASS_SPECIAL_OPTION = DefaultCSSClassProvider.create ("phloc-select-option-special");

  public HCExtSelect (@Nonnull final IHCRequestField aRF)
  {
    super (aRF);
  }

  @Nonnull
  public static HCOption createOptionPleaseSelect (@Nonnull final Locale aDisplayLocale)
  {
    final HCOption aOption = new HCOption ().setValue (VALUE_PLEASE_SELECT)
                                            .addChild (EWebBasicsText.PLEASE_SELECT.getDisplayText (aDisplayLocale));
    aOption.addClass (CSS_CLASS_SPECIAL_OPTION);
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
    final HCOption aOption = new HCOption ().setValue (VALUE_NONE)
                                            .addChild (EWebBasicsText.SELECT_NONE.getDisplayText (aDisplayLocale));
    aOption.addClass (CSS_CLASS_SPECIAL_OPTION);
    return aOption;
  }

  @Nonnull
  public HCOption addOptionNone (@Nonnull final Locale aDisplayLocale)
  {
    return addOptionAtIndex (0, createOptionNone (aDisplayLocale));
  }
}
