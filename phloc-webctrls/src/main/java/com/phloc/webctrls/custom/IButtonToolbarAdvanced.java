package com.phloc.webctrls.custom;

import java.util.Locale;

import javax.annotation.Nonnull;

/**
 * Base advanced button toolbar
 * 
 * @author Philip Helger
 * @param <IMPLTYPE>
 *        Real implementation type
 */
public interface IButtonToolbarAdvanced <IMPLTYPE extends IButtonToolbar <IMPLTYPE>> extends IButtonToolbar <IMPLTYPE>
{
  @Nonnull
  IMPLTYPE addButtonBack (@Nonnull Locale aDisplayLocale);

  @Nonnull
  IMPLTYPE addButtonCancel (@Nonnull Locale aDisplayLocale);

  @Nonnull
  IMPLTYPE addButtonNo (@Nonnull Locale aDisplayLocale);
}
