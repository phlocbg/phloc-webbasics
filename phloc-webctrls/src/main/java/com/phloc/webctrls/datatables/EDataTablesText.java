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
package com.phloc.webctrls.datatables;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;

@Translatable
public enum EDataTablesText implements IHasDisplayText
{
  // paginate
  FIRST ("Erster", "First"),
  PREVIOUS ("Zurück", "Previous"),
  NEXT ("Nächster", "Next"),
  LAST ("Letzter", "Last"),
  // main
  PROCESSING ("Bitte warten...", "Processing..."),
  LENGTH_MENU ("_MENU_ Einträge anzeigen", "Show _MENU_ entries"),
  ZERO_RECORDS ("Keine Einträge vorhanden.", "No matching records found."),
  INFO ("_START_ bis _END_ von _TOTAL_ Einträgen", "Showing _START_ to _END_ of _TOTAL_ entries"),
  INFO_EMPTY ("0 bis 0 von 0 Einträgen", "Showing 0 to 0 of 0 entries"),
  INFO_FILTERED ("(gefiltert von _MAX_ Einträgen)", "(filtered from _MAX_ total entries)"),
  INFO_POSTFIX ("", ""),
  SEARCH ("Suchen:", "Search:"),
  URL ("", "");

  private final ITextProvider m_aTP;

  private EDataTablesText (@Nonnull final String sDE, @Nonnull final String sEN)
  {
    m_aTP = TextProvider.create_DE_EN (sDE, sEN);
  }

  @Nullable
  public String getDisplayText (@Nonnull final Locale aContentLocale)
  {
    return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
  }
}
