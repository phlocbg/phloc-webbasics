package com.phloc.webctrls.custom;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.js.IJSCodeProvider;

/**
 * Base button toolbar
 * 
 * @author Philip Helger
 * @param <IMPLTYPE>
 *        Real implementation type
 */
public interface IButtonToolbar <IMPLTYPE extends IButtonToolbar <IMPLTYPE>>
{
  @Nonnull
  IMPLTYPE addHiddenField (@Nullable String sName, int nValue);

  @Nonnull
  IMPLTYPE addHiddenField (@Nullable String sName, @Nullable String sValue);

  @Nonnull
  IMPLTYPE addHiddenFields (@Nullable Map <String, String> aValues);

  @Nonnull
  IMPLTYPE addButton (@Nullable String sCaption, IJSCodeProvider aJSCode);

  @Nonnull
  IMPLTYPE addButton (@Nullable String sCaption, IJSCodeProvider aJSCode, @Nullable EDefaultIcon eIcon);

  @Nonnull
  IMPLTYPE addButton (@Nullable String sCaption, IJSCodeProvider aJSCode, @Nullable IIcon aIcon);

  @Nonnull
  IMPLTYPE addButton (@Nullable String sCaption, ISimpleURL aURL);

  @Nonnull
  IMPLTYPE addButton (@Nullable String sCaption, ISimpleURL aURL, @Nullable EDefaultIcon eIcon);

  @Nonnull
  IMPLTYPE addButton (@Nullable String sCaption, ISimpleURL aURL, @Nullable IIcon aIcon);

  @Nonnull
  IMPLTYPE addButtonBack (Locale aDisplayLocale, ISimpleURL aURL);

  @Nonnull
  IMPLTYPE addButtonCancel (Locale aDisplayLocale, ISimpleURL aURL);

  @Nonnull
  IMPLTYPE addButtonNo (Locale aDisplayLocale, ISimpleURL aURL);

  @Nonnull
  IMPLTYPE addButtonEdit (Locale aDisplayLocale, ISimpleURL aURL);

  @Nonnull
  IMPLTYPE addButtonNew (@Nullable String sCaption, ISimpleURL aURL);

  @Nonnull
  IMPLTYPE addSubmitButton (String sCaption);

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
