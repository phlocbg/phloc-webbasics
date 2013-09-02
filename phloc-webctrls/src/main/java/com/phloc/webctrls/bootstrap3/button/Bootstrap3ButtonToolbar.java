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
package com.phloc.webctrls.bootstrap3.button;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.html.AbstractHCDiv;
import com.phloc.html.hc.html.HCHiddenField;
import com.phloc.html.js.IJSCodeProvider;
import com.phloc.html.js.builder.html.JSHtml;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webctrls.bootstrap3.CBootstrap3CSS;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webctrls.custom.IIcon;
import com.phloc.webctrls.custom.toolbar.IButtonToolbar;

/**
 * Bootstrap3 button toolbar
 * 
 * @author Philip Helger
 */
public class Bootstrap3ButtonToolbar extends AbstractHCDiv <Bootstrap3ButtonToolbar> implements IButtonToolbar <Bootstrap3ButtonToolbar>
{
  public Bootstrap3ButtonToolbar ()
  {
    addClass (CBootstrap3CSS.BTN_TOOLBAR);
  }

  @Nonnull
  public final Bootstrap3ButtonToolbar addHiddenField (@Nullable final String sName, final int nValue)
  {
    addChild (new HCHiddenField (sName, nValue));
    return this;
  }

  @Nonnull
  public final Bootstrap3ButtonToolbar addHiddenField (@Nullable final String sName, @Nullable final String sValue)
  {
    addChild (new HCHiddenField (sName, sValue));
    return this;
  }

  @Nonnull
  public final Bootstrap3ButtonToolbar addHiddenFields (@Nullable final Map <String, String> aValues)
  {
    if (aValues != null)
      for (final Map.Entry <String, String> aEntry : aValues.entrySet ())
        addChild (new HCHiddenField (aEntry.getKey (), aEntry.getValue ()));
    return this;
  }

  @Nonnull
  public final Bootstrap3ButtonToolbar addButton (@Nullable final String sCaption,
                                                  @Nonnull final IJSCodeProvider aJSCode)
  {
    return addButton (sCaption, aJSCode, (IIcon) null);
  }

  @Nonnull
  public final Bootstrap3ButtonToolbar addButton (@Nullable final String sCaption,
                                                  @Nonnull final IJSCodeProvider aJSCode,
                                                  @Nullable final EDefaultIcon eIcon)
  {
    return addButton (sCaption, aJSCode, eIcon == null ? null : eIcon.getIcon ());
  }

  @Nonnull
  public final Bootstrap3ButtonToolbar addButton (@Nullable final String sCaption,
                                                  @Nonnull final IJSCodeProvider aJSCode,
                                                  @Nullable final IIcon aIcon)
  {
    addChild (new Bootstrap3Button ().setIcon (aIcon).addChild (sCaption).setOnClick (aJSCode));
    return this;
  }

  @Nonnull
  public final Bootstrap3ButtonToolbar addButton (@Nullable final String sCaption, @Nonnull final ISimpleURL aURL)
  {
    return addButton (sCaption, aURL, (IIcon) null);
  }

  @Nonnull
  public final Bootstrap3ButtonToolbar addButton (@Nullable final String sCaption,
                                                  @Nonnull final ISimpleURL aURL,
                                                  @Nullable final EDefaultIcon eIcon)
  {
    return addButton (sCaption, aURL, eIcon == null ? null : eIcon.getIcon ());
  }

  @Nonnull
  public final Bootstrap3ButtonToolbar addButton (@Nullable final String sCaption,
                                                  @Nonnull final ISimpleURL aURL,
                                                  @Nullable final IIcon aIcon)
  {
    return addButton (sCaption, JSHtml.windowLocationHref (aURL), aIcon);
  }

  @Nonnull
  public Bootstrap3ButtonToolbar addButtonBack (@Nonnull final Locale aDisplayLocale, @Nonnull final ISimpleURL aURL)
  {
    return addButton (EWebBasicsText.MSG_BUTTON_BACK.getDisplayText (aDisplayLocale), aURL, EDefaultIcon.BACK);
  }

  @Nonnull
  public Bootstrap3ButtonToolbar addButtonCancel (@Nonnull final Locale aDisplayLocale, @Nonnull final ISimpleURL aURL)
  {
    return addButton (EWebBasicsText.MSG_BUTTON_CANCEL.getDisplayText (aDisplayLocale), aURL, EDefaultIcon.CANCEL);
  }

  @Nonnull
  public Bootstrap3ButtonToolbar addButtonNo (@Nonnull final Locale aDisplayLocale, @Nonnull final ISimpleURL aURL)
  {
    return addButton (EWebBasicsText.MSG_BUTTON_NO.getDisplayText (aDisplayLocale), aURL, EDefaultIcon.NO);
  }

  @Nonnull
  public Bootstrap3ButtonToolbar addButtonEdit (@Nonnull final Locale aDisplayLocale, @Nonnull final ISimpleURL aURL)
  {
    return addButton (EWebBasicsText.MSG_BUTTON_EDIT.getDisplayText (aDisplayLocale), aURL, EDefaultIcon.EDIT);
  }

  @Nonnull
  public Bootstrap3ButtonToolbar addButtonNew (final String sCaption, @Nonnull final ISimpleURL aURL)
  {
    return addButton (sCaption, aURL, EDefaultIcon.NEW);
  }

  @Nonnull
  public final Bootstrap3ButtonToolbar addSubmitButton (@Nullable final String sCaption)
  {
    return addSubmitButton (sCaption, (IJSCodeProvider) null, (IIcon) null);
  }

  @Nonnull
  public final Bootstrap3ButtonToolbar addSubmitButton (@Nullable final String sCaption,
                                                        @Nullable final IJSCodeProvider aOnClick)
  {
    return addSubmitButton (sCaption, aOnClick, (IIcon) null);
  }

  @Nonnull
  public final Bootstrap3ButtonToolbar addSubmitButton (@Nullable final String sCaption,
                                                        @Nullable final EDefaultIcon eIcon)
  {
    return addSubmitButton (sCaption, (IJSCodeProvider) null, eIcon == null ? null : eIcon.getIcon ());
  }

  @Nonnull
  public final Bootstrap3ButtonToolbar addSubmitButton (@Nullable final String sCaption, @Nullable final IIcon aIcon)
  {
    return addSubmitButton (sCaption, (IJSCodeProvider) null, aIcon);
  }

  @Nonnull
  public final Bootstrap3ButtonToolbar addSubmitButton (@Nullable final String sCaption,
                                                        @Nullable final IJSCodeProvider aOnClick,
                                                        @Nullable final IIcon aIcon)
  {
    addChild (new Bootstrap3SubmitButton ().setIcon (aIcon).setOnClick (aOnClick).addChild (sCaption));
    return this;
  }

  @Nonnull
  public final Bootstrap3ButtonToolbar addSubmitButtonSave (@Nonnull final Locale aDisplayLocale)
  {
    return addSubmitButton (EWebBasicsText.MSG_BUTTON_SAVE.getDisplayText (aDisplayLocale), EDefaultIcon.SAVE);
  }

  @Nonnull
  public final Bootstrap3ButtonToolbar addSubmitButtonYes (@Nonnull final Locale aDisplayLocale)
  {
    return addSubmitButton (EWebBasicsText.MSG_BUTTON_YES.getDisplayText (aDisplayLocale), EDefaultIcon.YES);
  }
}
