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
package com.phloc.webctrls.datatable;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.json.IJSONObject;
import com.phloc.json.impl.JSONObject;

@Translatable
public enum EDataTableText implements IHasDisplayText
{
  PROCESSING ("Bitte warten...", "Processing..."),
  LENGTH_MENU ("_MENU_ Einträge anzeigen", "Show _MENU_ entries"),
  ZERO_RECORDS ("Keine Einträge vorhanden.", "No matching records found."),
  INFO ("_START_ bis _END_ von _TOTAL_ Einträgen", "Showing _START_ to _END_ of _TOTAL_ entries"),
  INFO_EMPTY ("0 bis 0 von 0 Einträgen", "Showing 0 to 0 of 0 entries"),
  INFO_FILTERED ("(gefiltert von _MAX_  Einträgen)", "(filtered from _MAX_ total entries)"),
  INFO_POSTFIX ("", ""),
  SEARCH ("Suchen:", "Search:"),
  URL ("", ""),
  FIRST ("Erster", "First"),
  PREVIOUS ("Zurück", "Previous"),
  NEXT ("Nächster", "Next"),
  LAST ("Letzter", "Last");

  private final ITextProvider m_aTP;

  private EDataTableText (@Nonnull final String sDE, @Nonnull final String sEN)
  {
    m_aTP = TextProvider.create_DE_EN (sDE, sEN);
  }

  @Nullable
  public String getDisplayText (@Nonnull final Locale aContentLocale)
  {
    return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
  }

  @Nonnull
  public static IJSONObject getAsJSON (@Nonnull final Locale aLocale)
  {
    final IJSONObject aJSON = new JSONObject ();
    aJSON.setStringProperty ("sProcessing", PROCESSING.getDisplayText (aLocale));
    aJSON.setStringProperty ("sLengthMenu", LENGTH_MENU.getDisplayText (aLocale));
    aJSON.setStringProperty ("sZeroRecords", ZERO_RECORDS.getDisplayText (aLocale));
    aJSON.setStringProperty ("sInfo", INFO.getDisplayText (aLocale));
    aJSON.setStringProperty ("sInfoEmpty", INFO_EMPTY.getDisplayText (aLocale));
    aJSON.setStringProperty ("sInfoFiltered", INFO_FILTERED.getDisplayText (aLocale));
    aJSON.setStringProperty ("sInfoPostFix", INFO_POSTFIX.getDisplayText (aLocale));
    aJSON.setStringProperty ("sSearch", SEARCH.getDisplayText (aLocale));
    aJSON.setStringProperty ("sUrl", URL.getDisplayText (aLocale));
    final IJSONObject aPagination = new JSONObject ();
    aPagination.setStringProperty ("sFirst", FIRST.getDisplayText (aLocale));
    aPagination.setStringProperty ("sPrevious", PREVIOUS.getDisplayText (aLocale));
    aPagination.setStringProperty ("sNext", NEXT.getDisplayText (aLocale));
    aPagination.setStringProperty ("sLast", LAST.getDisplayText (aLocale));
    aJSON.setObjectProperty ("oPaginate", aPagination);
    return aJSON;
  }
}
