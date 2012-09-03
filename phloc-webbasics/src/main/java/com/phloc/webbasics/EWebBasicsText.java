/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webbasics;

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
 * Contains some global texts.
 * 
 * @author philip
 */
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
  LOGIN_LOGOUT ("Abmelden", "Logout"),

  MSG_ERR_INCORRECT_INPUT ("Auf Grund fehlender oder falscher Eingaben konnte nicht gespeichert werden! Überprüfen Sie Ihre Eingaben und folgen Sie den Aufforderungen zur Fehlerbehebung.", "Due to missing or incorrect input saving was not possible. Check your input and follow the suggestions for error correction."),
  PLEASE_SELECT ("(Wählen Sie einen Eintrag)", "(Select an item)"),
  SELECT_NONE ("(Kein)", "(None)"),
  OPEN_CALENDAR ("Kalender...", "Calendar..."),
  TAB_PROGRESS_MSG ("Daten werden geladen...", "Loading..."),
  PAGING_PAGE (" Seite ", " Page "),
  PAGING_OF (" von {0}", " of {0}"),
  PAGING_TOOLTIP_PAGE_START ("Erste Seite", "First page"),
  PAGING_TOOLTIP_PAGE_PREV ("Vorherige Seite", "Previous page"),
  PAGING_TOOLTIP_PAGE_NEXT ("Nächste Seite", "Next page"),
  PAGING_TOOLTIP_PAGE_END ("Letzte Seite", "Last page"),
  PAGING_LABEL_PAGE_SIZE ("Einträge pro Seite: ", "Entries per page: "),
  PAGING_TOOLTIP_ACCEPT ("Anwenden", "Apply"),
  PAGING_SHOW_RANGE ("Einträge {0} - {1} von insgesamt {2}", "Entries {0} - {1} of total {2}"),
  TREE_FILTER ("Einträge werden gefiltert...", "Filtering entries..."),
  TREE_EXPAND_ALL ("Daten werden geladen...", "Loading..."),
  TREE_COLLAPSE_ALL ("Daten werden geladen...", "Loading..."),
  TREE_EXPAND ("Daten werden geladen...", "Loading..."),
  TREE_COLLAPSE ("Daten werden geladen...", "Loading..."),
  TREE_LABEL_FILTER ("Einträge filtern", "Filter entries"),
  TREE_TREEITEM_COLLAPSE ("Eintrag zuklappen", "Collapse item"),
  TREE_TREEITEM_EXPAND ("Eintrag aufklappen", "Expand item"),
  TREE_TREEITEMS_COLLAPSE ("Alle Einträge zuklappen", "Collapse all items"),
  TREE_TREEITEMS_EXPAND ("Alle Einträge aufklappen", "Expand all items"),
  SELECT_SHOW_ALL ("Alle anzeigen...", "Show all..."),
  FILE_SELECT ("Dateiauswahl", "File Selection"),
  MSG_ACTIONS ("Aktionen", "Actions"),

  // FIXME: how could this be unified with ECoreText without having to change
  // all calls to pass the display locale?
  MSG_BACK_TO_OVERVIEW ("Zurück zur Übersicht", "Back to the list"),
  MSG_BUTTON_YES ("Ja", "Yes"),
  MSG_BUTTON_NO ("Nein", "No"),
  MSG_BUTTON_BACK ("Zurück", "Back"),
  MSG_BUTTON_NEXT ("Weiter", "Next"),
  MSG_BUTTON_SAVE ("Speichern", "Save"),
  MSG_BUTTON_SAVE_ALL ("Alles Speichern", "Save all"),
  MSG_BUTTON_SAVE_AS ("Speichern unter...", "Save as..."),
  MSG_BUTTON_SAVE_CLOSE ("Speichern und schließen", "Save and close"),
  MSG_BUTTON_RESET ("Zurücksetzen", "Reset"),
  MSG_BUTTON_CANCEL ("Abbrechen", "Cancel"),
  MSG_BUTTON_SELECT ("Auswählen...", "Select..."),
  MSG_BUTTON_DELETE ("Löschen", "Delete"),
  MSG_BUTTON_EDIT ("Bearbeiten", "Edit"),
  MSG_BUTTON_UPLOAD ("Hochladen", "Upload"),
  MSG_TRUE ("Wahr", "True"),
  MSG_FALSE ("Falsch", "False");

  private final ITextProvider m_aTP;

  private EWebBasicsText (final String sDE, final String sEN)
  {
    m_aTP = TextProvider.create_DE_EN (sDE, sEN);
  }

  public String getDisplayText (@Nonnull final Locale aContentLocale)
  {
    return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
  }

  public String getDisplayTextWithArgs (@Nonnull final Locale aContentLocale, @Nullable final Object... aArgs)
  {
    return DefaultTextResolver.getTextWithArgs (this, m_aTP, aContentLocale, aArgs);
  }
}
