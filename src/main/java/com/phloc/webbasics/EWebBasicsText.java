package com.phloc.webbasics;

import java.util.Locale;

import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.name.IHasDisplayTextWithArgs;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;

@Translatable
public enum EWebBasicsText implements IHasDisplayText, IHasDisplayTextWithArgs
{
  // Boolean representation
  BOOLEAN_TRUE ("Ja", "Yes"),
  BOOLEAN_FALSE ("Nein", "No"),
  // Misc texts
  PAGE_HELP_TITLE ("Hilfe zu ''{0}'' anzeigen", "Show help for ''{0}''"),
  DOWNLOAD ("Download", "Download"),
  LOGIN_HEADER ("Login", "Login"),
  LOGIN_ERROR_MSG ("Benutzername und/oder Passwort sind ungültig!", "User name and/or password are invalid!"),
  LOGIN_FIELD_USERNAME ("Benutzername", "User name"),
  LOGIN_FIELD_PASSWORD ("Passwort", "Password"),
  LOGIN_BUTTON_SUBMIT ("Login", "Login"),
  LOGIN_LOGOUT ("Abmelden", "Logout");

  private final ITextProvider m_aTP;

  private EWebBasicsText (final String sDE, final String sEN)
  {
    m_aTP = TextProvider.create_DE_EN (sDE, sEN);
  }

  public String getDisplayText (final Locale aContentLocale)
  {
    return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
  }

  public String getDisplayTextWithArgs (final Locale aContentLocale, final Object... aArgs)
  {
    return DefaultTextResolver.getTextWithArgs (this, m_aTP, aContentLocale, aArgs);
  }
}
