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

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.name.IHasName;

public enum EDataTableJSONKeyword implements IHasName
{
  RETRIEVE ("bRetrieve"),
  DESTROY ("bDestroy"),
  JQUERY ("bJQueryUI"),
  PAGINATION_TYPE ("sPaginationType"),
  LANGUAGE ("oLanguage"),
  PROCESSING ("bProcessing"),
  SERVER_SIDE ("bServerSide"),
  AJAX_SOURCE ("sAjaxSource"),
  DATA_PROP ("mDataProp"),
  DATA_TYPE ("dataType"),
  TYPE ("type"),
  URL ("url"),
  DATA ("data"),
  DATA_ARRAY ("aaData"),
  PARAM_DATA ("aoData"),
  PARAM_SOURCE ("sSource"),
  FUNCTION_CALLBACK ("fnCallback"),
  FUNCTION_SERVER_DATA ("fnServerData"),
  SUCCESS ("success"),
  NAME ("name"),
  VALUE ("value"),
  COLUMNS ("aoColumns"),
  COLUMN_DEFS ("aoColumnDefs"),
  ECHO ("sEcho"),
  TOTAL_RECORDS ("iTotalRecords"),
  TOTAL_DISPLAY_RECORDS ("iTotalDisplayRecords"),
  SORTING ("aaSorting"),
  LENGTH_MENU ("aLengthMenu"),
  DISPLAY_LENGTH ("iDisplayLength"),
  DEFER_RENDER ("bDeferRender"),
  CLASS ("sClass"),
  STATE_SAVE ("bStateSave"),
  SORTABLE ("bSortable"),
  COOKIE_DURATION ("iCookieDuration"),
  SCROLL_Y ("sScrollY"),
  WIDTH ("sWidth"),
  PAGINATE ("bPaginate"),
  TARGETS ("aTargets"),
  SEARCHABLE ("bSearchable"),
  VISIBLE ("bVisible"),
  S_NAME ("sName"),
  S_TYPE ("sType"),
  DATASORT ("aDataSort");

  private final String m_sName;

  private EDataTableJSONKeyword (@Nonnull @Nonempty final String sName)
  {
    m_sName = sName;
  }

  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }
}
