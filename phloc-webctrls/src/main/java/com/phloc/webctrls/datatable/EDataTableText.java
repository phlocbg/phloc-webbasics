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
  INFO_FILTERED ("(gefiltert von _MAX_  Einträgen)", "(filtered from _MAX_ total entries)"),
  INFO_POSTFIX ("", ""),
  SEARCH ("Suchen:", "Search:"),
  URL ("", "");

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
  public static IJSONObject getAsJSON (@Nonnull final Locale aDisplayLocale)
  {
    final IJSONObject aJSON = new JSONObject ();
    final IJSONObject aPagination = new JSONObject ();
    aPagination.setStringProperty ("sFirst", FIRST.getDisplayText (aDisplayLocale));
    aPagination.setStringProperty ("sPrevious", PREVIOUS.getDisplayText (aDisplayLocale));
    aPagination.setStringProperty ("sNext", NEXT.getDisplayText (aDisplayLocale));
    aPagination.setStringProperty ("sLast", LAST.getDisplayText (aDisplayLocale));
    aJSON.setObjectProperty ("oPaginate", aPagination);
    aJSON.setStringProperty ("sProcessing", PROCESSING.getDisplayText (aDisplayLocale));
    aJSON.setStringProperty ("sLengthMenu", LENGTH_MENU.getDisplayText (aDisplayLocale));
    aJSON.setStringProperty ("sZeroRecords", ZERO_RECORDS.getDisplayText (aDisplayLocale));
    aJSON.setStringProperty ("sInfo", INFO.getDisplayText (aDisplayLocale));
    aJSON.setStringProperty ("sInfoEmpty", INFO_EMPTY.getDisplayText (aDisplayLocale));
    aJSON.setStringProperty ("sInfoFiltered", INFO_FILTERED.getDisplayText (aDisplayLocale));
    aJSON.setStringProperty ("sInfoPostFix", INFO_POSTFIX.getDisplayText (aDisplayLocale));
    aJSON.setStringProperty ("sSearch", SEARCH.getDisplayText (aDisplayLocale));
    aJSON.setStringProperty ("sUrl", URL.getDisplayText (aDisplayLocale));
    return aJSON;
  }
}
