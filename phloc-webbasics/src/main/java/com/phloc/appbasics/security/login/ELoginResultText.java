/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.appbasics.security.login;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;

/**
 * Login result explanations
 * 
 * @author Philip Helger
 */
@Translatable
public enum ELoginResultText implements IHasDisplayText
{
  SUCCESS ("Die Anmeldung war erfolgreich.", "User logged in successfully."),
  USER_NOT_EXISTING ("Der Benutzername ist ungültig.", "No such user exists."),
  USER_IS_DELETED ("Der Benutzer existiert nicht mehr.", "The user not longer exists."),
  USER_IS_DISABLED ("Der Benutzer ist deaktiviert.", "The user is disabled."),
  USER_IS_MISSING_ROLE ("Der Benutzer hat keine Berechtigung sich anzumelden.", "The user has no rights to login."),
  INVALID_PASSWORD ("Das Passwort ist ungültig.", "Invalid password provided."),
  USER_ALREADY_LOGGED_IN ("Der Benutzer ist bereits angemeldet.", "The user is already logged in."),
  SESSION_ALREADY_HAS_USER ("Es ist bereits ein anderer Benutzer angemeldet.", "Another user is already logged in.");

  private final ITextProvider m_aTP;

  private ELoginResultText (@Nonnull final String sDE, @Nonnull final String sEN)
  {
    m_aTP = TextProvider.create_DE_EN (sDE, sEN);
  }

  public String getDisplayText (@Nonnull final Locale aContentLocale)
  {
    return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
  }
}
