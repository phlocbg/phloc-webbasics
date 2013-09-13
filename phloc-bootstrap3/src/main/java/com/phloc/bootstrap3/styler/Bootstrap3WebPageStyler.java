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
package com.phloc.bootstrap3.styler;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrap3CSS;
import com.phloc.bootstrap3.alert.Bootstrap3ErrorBox;
import com.phloc.bootstrap3.alert.Bootstrap3InfoBox;
import com.phloc.bootstrap3.alert.Bootstrap3QuestionBox;
import com.phloc.bootstrap3.alert.Bootstrap3SuccessBox;
import com.phloc.bootstrap3.button.Bootstrap3ButtonToolbar;
import com.phloc.bootstrap3.button.EBootstrap3ButtonType;
import com.phloc.bootstrap3.ext.Bootstrap3DataTables;
import com.phloc.bootstrap3.nav.Bootstrap3TabBox;
import com.phloc.bootstrap3.table.Bootstrap3Table;
import com.phloc.bootstrap3.table.Bootstrap3TableForm;
import com.phloc.bootstrap3.table.Bootstrap3TableFormView;
import com.phloc.html.hc.html.AbstractHCBaseTable;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webctrls.styler.SimpleWebPageStyler;

public class Bootstrap3WebPageStyler extends SimpleWebPageStyler
{
  @Override
  @Nonnull
  public Bootstrap3ErrorBox createErrorBox (@Nullable final String sText)
  {
    return Bootstrap3ErrorBox.create (sText);
  }

  @Override
  @Nonnull
  public Bootstrap3InfoBox createInfoBox (@Nullable final String sText)
  {
    return Bootstrap3InfoBox.create (sText);
  }

  @Override
  @Nonnull
  public Bootstrap3SuccessBox createSuccessBox (@Nullable final String sText)
  {
    return Bootstrap3SuccessBox.create (sText);
  }

  @Override
  @Nonnull
  public Bootstrap3QuestionBox createQuestionBox (@Nullable final String sText)
  {
    return Bootstrap3QuestionBox.create (sText);
  }

  @Override
  @Nonnull
  public Bootstrap3Table createTable (@Nullable final HCCol... aCols)
  {
    // Must be a regular Bootstrap3Table to work with the data tables
    return new Bootstrap3Table (aCols);
  }

  @Override
  @Nonnull
  public Bootstrap3TableForm createTableForm (@Nullable final HCCol... aCols)
  {
    return new Bootstrap3TableForm (aCols);
  }

  @Override
  @Nonnull
  public Bootstrap3TableFormView createTableFormView (@Nullable final HCCol... aCols)
  {
    return new Bootstrap3TableFormView (aCols);
  }

  @Override
  @Nonnull
  public Bootstrap3DataTables createDefaultDataTables (@Nonnull final AbstractHCBaseTable <?> aTable,
                                                       @Nonnull final Locale aDisplayLocale)
  {
    final Bootstrap3DataTables ret = new Bootstrap3DataTables ((Bootstrap3Table) aTable);
    ret.setDisplayLocale (aDisplayLocale);
    ret.addAllColumns (aTable);
    return ret;
  }

  @Override
  @Nonnull
  public HCSpan createUploadButton (@Nonnull final Locale aDisplayLocale)
  {
    return new HCSpan ().addClasses (CBootstrap3CSS.BTN, EBootstrap3ButtonType.SUCCESS)
                        .addChild (EWebBasicsText.FILE_SELECT.getDisplayText (aDisplayLocale));
  }

  @Override
  @Nonnull
  public Bootstrap3ButtonToolbar createToolbar ()
  {
    return new Bootstrap3ButtonToolbar ();
  }

  @Override
  @Nonnull
  public Bootstrap3TabBox createTabBox ()
  {
    return new Bootstrap3TabBox ();
  }
}
