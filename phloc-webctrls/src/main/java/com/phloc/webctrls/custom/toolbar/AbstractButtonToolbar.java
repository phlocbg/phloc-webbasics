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
import com.phloc.commons.url.SimpleURL;
import com.phloc.html.hc.html.AbstractHCDiv;
import com.phloc.html.hc.html.HCHiddenField;
import com.phloc.html.js.IJSCodeProvider;
import com.phloc.html.js.builder.html.JSHtml;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webctrls.custom.IIcon;

/**
 * Abstract button toolbar implementation
 * 
 * @author Philip Helger
 */
public abstract class AbstractButtonToolbar <IMPLTYPE extends AbstractButtonToolbar <IMPLTYPE>> extends AbstractHCDiv <IMPLTYPE> implements IButtonToolbar <IMPLTYPE>
{
  private final SimpleURL m_aSelfHref;

  public AbstractButtonToolbar ()
  {
    this (LinkUtils.getSelfHref ());
  }

  public AbstractButtonToolbar (@Nonnull final SimpleURL aSelfHref)
  {
    if (aSelfHref == null)
      throw new NullPointerException ("selfHref");
    m_aSelfHref = aSelfHref;
  }

  @Nonnull
  public final IMPLTYPE addHiddenField (@Nullable final String sName, final int nValue)
  {
    addChild (new HCHiddenField (sName, nValue));
    return thisAsT ();
  }

  @Nonnull
  public final IMPLTYPE addHiddenField (@Nullable final String sName, @Nullable final String sValue)
  {
    addChild (new HCHiddenField (sName, sValue));
    return thisAsT ();
  }

  @Nonnull
  public final IMPLTYPE addHiddenFields (@Nullable final Map <String, String> aValues)
  {
    if (aValues != null)
      for (final Map.Entry <String, String> aEntry : aValues.entrySet ())
        addChild (new HCHiddenField (aEntry.getKey (), aEntry.getValue ()));
    return thisAsT ();
  }

  @Nonnull
  public final IMPLTYPE addButton (@Nullable final String sCaption, @Nonnull final IJSCodeProvider aJSCode)
  {
    return addButton (sCaption, aJSCode, (IIcon) null);
  }

  @Nonnull
  public final IMPLTYPE addButton (@Nullable final String sCaption,
                                   @Nonnull final IJSCodeProvider aJSCode,
                                   @Nullable final EDefaultIcon eIcon)
  {
    return addButton (sCaption, aJSCode, eIcon == null ? null : eIcon.getIcon ());
  }

  @Nonnull
  public final IMPLTYPE addButton (@Nullable final String sCaption, @Nonnull final ISimpleURL aURL)
  {
    return addButton (sCaption, aURL, (IIcon) null);
  }

  @Nonnull
  public final IMPLTYPE addButton (@Nullable final String sCaption,
                                   @Nonnull final ISimpleURL aURL,
                                   @Nullable final EDefaultIcon eIcon)
  {
    return addButton (sCaption, aURL, eIcon == null ? null : eIcon.getIcon ());
  }

  @Nonnull
  public final IMPLTYPE addButton (@Nullable final String sCaption,
                                   @Nonnull final ISimpleURL aURL,
                                   @Nullable final IIcon aIcon)
  {
    return addButton (sCaption, JSHtml.windowLocationHref (aURL), aIcon);
  }

  @Nonnull
  public IMPLTYPE addButtonBack (@Nonnull final Locale aDisplayLocale, @Nonnull final ISimpleURL aURL)
  {
    return addButton (EWebBasicsText.MSG_BUTTON_BACK.getDisplayText (aDisplayLocale), aURL, EDefaultIcon.BACK);
  }

  @Nonnull
  public IMPLTYPE addButtonBack (@Nonnull final Locale aDisplayLocale)
  {
    return addButtonBack (aDisplayLocale, m_aSelfHref);
  }

  @Nonnull
  public IMPLTYPE addButtonCancel (@Nonnull final Locale aDisplayLocale, @Nonnull final ISimpleURL aURL)
  {
    return addButton (EWebBasicsText.MSG_BUTTON_CANCEL.getDisplayText (aDisplayLocale), aURL, EDefaultIcon.CANCEL);
  }

  @Nonnull
  public IMPLTYPE addButtonCancel (@Nonnull final Locale aDisplayLocale)
  {
    return addButtonCancel (aDisplayLocale, m_aSelfHref);
  }

  @Nonnull
  public IMPLTYPE addButtonNo (@Nonnull final Locale aDisplayLocale, @Nonnull final ISimpleURL aURL)
  {
    return addButton (EWebBasicsText.MSG_BUTTON_NO.getDisplayText (aDisplayLocale), aURL, EDefaultIcon.NO);
  }

  @Nonnull
  public IMPLTYPE addButtonNo (@Nonnull final Locale aDisplayLocale)
  {
    return addButtonNo (aDisplayLocale, m_aSelfHref);
  }

  @Nonnull
  public IMPLTYPE addButtonEdit (@Nonnull final Locale aDisplayLocale, @Nonnull final ISimpleURL aURL)
  {
    return addButton (EWebBasicsText.MSG_BUTTON_EDIT.getDisplayText (aDisplayLocale), aURL, EDefaultIcon.EDIT);
  }

  @Nonnull
  public IMPLTYPE addButtonNew (final String sCaption, @Nonnull final ISimpleURL aURL)
  {
    return addButton (sCaption, aURL, EDefaultIcon.NEW);
  }

  @Nonnull
  public final IMPLTYPE addSubmitButton (@Nullable final String sCaption)
  {
    return addSubmitButton (sCaption, (IJSCodeProvider) null, (IIcon) null);
  }

  @Nonnull
  public final IMPLTYPE addSubmitButton (@Nullable final String sCaption, @Nullable final IJSCodeProvider aOnClick)
  {
    return addSubmitButton (sCaption, aOnClick, (IIcon) null);
  }

  @Nonnull
  public final IMPLTYPE addSubmitButton (@Nullable final String sCaption, @Nullable final EDefaultIcon eIcon)
  {
    return addSubmitButton (sCaption, (IJSCodeProvider) null, eIcon == null ? null : eIcon.getIcon ());
  }

  @Nonnull
  public final IMPLTYPE addSubmitButton (@Nullable final String sCaption, @Nullable final IIcon aIcon)
  {
    return addSubmitButton (sCaption, (IJSCodeProvider) null, aIcon);
  }

  @Nonnull
  public final IMPLTYPE addSubmitButtonSave (@Nonnull final Locale aDisplayLocale)
  {
    return addSubmitButton (EWebBasicsText.MSG_BUTTON_SAVE.getDisplayText (aDisplayLocale), EDefaultIcon.SAVE);
  }

  @Nonnull
  public final IMPLTYPE addSubmitButtonYes (@Nonnull final Locale aDisplayLocale)
  {
    return addSubmitButton (EWebBasicsText.MSG_BUTTON_YES.getDisplayText (aDisplayLocale), EDefaultIcon.YES);
  }
}
