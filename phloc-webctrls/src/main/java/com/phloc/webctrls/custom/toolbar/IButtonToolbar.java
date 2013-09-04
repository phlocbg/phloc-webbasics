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
package com.phloc.webctrls.custom.toolbar;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCDiv;
import com.phloc.html.js.IJSCodeProvider;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webctrls.custom.IIcon;

/**
 * Base button toolbar
 * 
 * @author Philip Helger
 * @param <IMPLTYPE>
 *        Real implementation type
 */
public interface IButtonToolbar <IMPLTYPE extends IButtonToolbar <IMPLTYPE>> extends IHCDiv <IMPLTYPE>
{
  @Nonnull
  IMPLTYPE addHiddenField (@Nullable String sName, int nValue);

  @Nonnull
  IMPLTYPE addHiddenField (@Nullable String sName, @Nullable String sValue);

  @Nonnull
  IMPLTYPE addHiddenFields (@Nullable Map <String, String> aValues);

  @Nonnull
  IMPLTYPE addButton (@Nullable String sCaption, @Nonnull IJSCodeProvider aJSCode);

  @Nonnull
  IMPLTYPE addButton (@Nullable String sCaption, @Nonnull IJSCodeProvider aJSCode, @Nullable EDefaultIcon eIcon);

  @Nonnull
  IMPLTYPE addButton (@Nullable String sCaption, @Nonnull IJSCodeProvider aJSCode, @Nullable IIcon aIcon);

  @Nonnull
  IMPLTYPE addButton (@Nullable String sCaption, @Nonnull ISimpleURL aURL);

  @Nonnull
  IMPLTYPE addButton (@Nullable String sCaption, @Nonnull ISimpleURL aURL, @Nullable EDefaultIcon eIcon);

  @Nonnull
  IMPLTYPE addButton (@Nullable String sCaption, @Nonnull ISimpleURL aURL, @Nullable IIcon aIcon);

  @Nonnull
  IMPLTYPE addButtonBack (@Nonnull Locale aDisplayLocale, @Nonnull ISimpleURL aURL);

  @Nonnull
  IMPLTYPE addButtonBack (@Nonnull Locale aDisplayLocale);

  @Nonnull
  IMPLTYPE addButtonCancel (@Nonnull Locale aDisplayLocale, @Nonnull ISimpleURL aURL);

  @Nonnull
  IMPLTYPE addButtonCancel (@Nonnull Locale aDisplayLocale);

  @Nonnull
  IMPLTYPE addButtonNo (@Nonnull Locale aDisplayLocale, @Nonnull ISimpleURL aURL);

  @Nonnull
  IMPLTYPE addButtonNo (@Nonnull Locale aDisplayLocale);

  @Nonnull
  IMPLTYPE addButtonEdit (@Nonnull Locale aDisplayLocale, @Nonnull ISimpleURL aURL);

  @Nonnull
  IMPLTYPE addButtonNew (@Nullable String sCaption, @Nonnull ISimpleURL aURL);

  @Nonnull
  IMPLTYPE addSubmitButton (@Nullable String sCaption);

  @Nonnull
  IMPLTYPE addSubmitButton (@Nullable String sCaption, @Nullable IJSCodeProvider aOnClick);

  @Nonnull
  IMPLTYPE addSubmitButton (@Nullable String sCaption, @Nullable EDefaultIcon eIcon);

  @Nonnull
  IMPLTYPE addSubmitButton (@Nullable String sCaption, @Nullable IIcon aIcon);

  @Nonnull
  IMPLTYPE addSubmitButton (@Nullable String sCaption, @Nullable IJSCodeProvider aOnClick, @Nullable IIcon aIcon);

  @Nonnull
  IMPLTYPE addSubmitButtonSave (@Nonnull Locale aDisplayLocale);

  @Nonnull
  IMPLTYPE addSubmitButtonYes (@Nonnull Locale aDisplayLocale);
}
