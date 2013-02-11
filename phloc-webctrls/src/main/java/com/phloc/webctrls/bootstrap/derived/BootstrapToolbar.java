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
package com.phloc.webctrls.bootstrap.derived;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.html.AbstractHCDiv;
import com.phloc.html.hc.html.HCHiddenField;
import com.phloc.html.js.IJSCodeProvider;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webctrls.bootstrap.BootstrapButton;
import com.phloc.webctrls.bootstrap.BootstrapButton_Submit;
import com.phloc.webctrls.custom.EDefaultIcon;

/**
 * Bootstrap button toolbar
 * 
 * @author philip
 */
public class BootstrapToolbar extends AbstractHCDiv <BootstrapToolbar>
{
  public BootstrapToolbar ()
  {
    super ();
  }

  @Nonnull
  public final BootstrapToolbar addHiddenField (@Nullable final String sName, final int nValue)
  {
    addChild (new HCHiddenField (sName, nValue));
    return this;
  }

  @Nonnull
  public final BootstrapToolbar addHiddenField (@Nullable final String sName, @Nullable final String sValue)
  {
    addChild (new HCHiddenField (sName, sValue));
    return this;
  }

  @Nonnull
  public final BootstrapToolbar addHiddenFields (@Nullable final Map <String, String> aValues)
  {
    if (aValues != null)
      for (final Map.Entry <String, String> aEntry : aValues.entrySet ())
        addChild (new HCHiddenField (aEntry.getKey (), aEntry.getValue ()));
    return this;
  }

  @Nonnull
  public final BootstrapToolbar addButton (@Nullable final String sCaption, @Nonnull final IJSCodeProvider aJSCode)
  {
    addChild (BootstrapButton.create (sCaption, aJSCode));
    return this;
  }

  @Nonnull
  public final BootstrapToolbar addButton (@Nullable final String sCaption, @Nonnull final ISimpleURL aURL)
  {
    addChild (BootstrapButton.create (sCaption, aURL));
    return this;
  }

  @Nonnull
  public final BootstrapToolbar addButton (@Nullable final String sCaption,
                                           @Nonnull final IJSCodeProvider aJSCode,
                                           @Nullable final EDefaultIcon eIcon)
  {
    addChild (BootstrapButton.create (sCaption, aJSCode, eIcon));
    return this;
  }

  @Nonnull
  public final BootstrapToolbar addButton (@Nullable final String sCaption,
                                           @Nonnull final ISimpleURL aURL,
                                           @Nullable final EDefaultIcon eIcon)
  {
    addChild (BootstrapButton.create (sCaption, aURL, eIcon));
    return this;
  }

  @Nonnull
  public BootstrapToolbar addButtonEdit (@Nonnull final Locale aDisplayLocale, @Nonnull final ISimpleURL aURL)
  {
    return addButton (EWebBasicsText.MSG_BUTTON_EDIT.getDisplayText (aDisplayLocale), aURL, EDefaultIcon.EDIT);
  }

  @Nonnull
  public final BootstrapToolbar addSubmitButton (final String sCaption)
  {
    addChild (BootstrapButton_Submit.create (sCaption));
    return this;
  }

  @Nonnull
  public final BootstrapToolbar addSubmitButton (@Nullable final String sCaption, @Nullable final EDefaultIcon eIcon)
  {
    addChild (BootstrapButton_Submit.create (sCaption, eIcon));
    return this;
  }

  @Nonnull
  public final BootstrapToolbar addSubmitButtonSave (@Nonnull final Locale aDisplayLocale)
  {
    return addSubmitButton (EWebBasicsText.MSG_BUTTON_SAVE.getDisplayText (aDisplayLocale), EDefaultIcon.SAVE);
  }

  @Nonnull
  public final BootstrapToolbar addSubmitButtonYes (@Nonnull final Locale aDisplayLocale)
  {
    return addSubmitButton (EWebBasicsText.MSG_BUTTON_YES.getDisplayText (aDisplayLocale), EDefaultIcon.YES);
  }
}
