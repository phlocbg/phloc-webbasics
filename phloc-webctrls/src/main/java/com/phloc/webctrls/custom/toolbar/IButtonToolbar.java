package com.phloc.webctrls.custom.toolbar;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCElementWithChildren;
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
public interface IButtonToolbar <IMPLTYPE extends IButtonToolbar <IMPLTYPE>> extends IHCElementWithChildren <IMPLTYPE>
{
  @Nonnull
  IMPLTYPE addHiddenField (@Nullable String sName, int nValue);

  @Nonnull
  IMPLTYPE addHiddenField (@Nullable String sName, @Nullable String sValue);

  @Nonnull
  IMPLTYPE addHiddenFields (@Nullable Map <String, String> aValues);

  @Nonnull
  IMPLTYPE addButton (@Nullable String sCaption, @Nullable IJSCodeProvider aJSCode);

  @Nonnull
  IMPLTYPE addButton (@Nullable String sCaption, @Nullable IJSCodeProvider aJSCode, @Nullable EDefaultIcon eIcon);

  @Nonnull
  IMPLTYPE addButton (@Nullable String sCaption, @Nullable IJSCodeProvider aJSCode, @Nullable IIcon aIcon);

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
