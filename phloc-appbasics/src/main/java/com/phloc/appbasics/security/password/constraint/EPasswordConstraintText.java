/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.appbasics.security.password.constraint;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.name.IHasDisplayTextWithArgs;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;

/**
 * Password constraint texts
 * 
 * @author Philip Helger
 */
@Translatable
public enum EPasswordConstraintText implements IHasDisplayText, IHasDisplayTextWithArgs
{
  DESC_MIN_LENGTH ("Das Passwort muss mindestens {0} Zeichen haben.", "The password must have at least {0} character(s)."),
  DESC_MAX_LENGTH ("Das Passwort darf maximal {0} Zeichen haben.", "The password must have at last {0} character(s)."),
  DESC_MUST_CONTAIN_DIGITS ("Das Passwort muss mindestens {0} Zahl(en) enthalten.", "The password must contain at least {0} digit(s)."),
  DESC_MUST_CONTAIN_LETTERS ("Das Passwort muss mindestens {0} Buchstaben enthalten.", "The password must contain at least {0} letter(s)."),
  DESC_MUST_CONTAIN_LETTERS_LOWERCASE ("Das Passwort muss mindestens {0} Kleinbuchstaben enthalten.", "The password must contain at least {0} lowercase letter(s)."),
  DESC_MUST_CONTAIN_LETTERS_UPPERCASE ("Das Passwort muss mindestens {0} Großbuchstaben enthalten.", "The password must contain at least {0} uppercase letter(s)."),
  DESC_MUST_CONTAIN_SPECIALS ("Das Passwort muss mindestens {0} Sonderzeichen enthalten.", "The password must contain at least {0} special char(s).");

  private final ITextProvider m_aTP;

  private EPasswordConstraintText (@Nonnull final String sDE, @Nonnull final String sEN)
  {
    m_aTP = TextProvider.create_DE_EN (sDE, sEN);
  }

  @Nullable
  public String getDisplayText (@Nonnull final Locale aContentLocale)
  {
    return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
  }

  @Nullable
  public String getDisplayTextWithArgs (@Nonnull final Locale aContentLocale, @Nullable final Object... aArgs)
  {
    return DefaultTextResolver.getTextWithArgs (this, m_aTP, aContentLocale, aArgs);
  }
}
