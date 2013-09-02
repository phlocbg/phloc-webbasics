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
package com.phloc.webpages.theme;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.html.AbstractHCBaseTable;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webctrls.bootstrap3.CBootstrap3CSS;
import com.phloc.webctrls.bootstrap3.alert.Bootstrap3ErrorBox;
import com.phloc.webctrls.bootstrap3.alert.Bootstrap3QuestionBox;
import com.phloc.webctrls.bootstrap3.alert.Bootstrap3SuccessBox;
import com.phloc.webctrls.bootstrap3.button.EBootstrap3ButtonType;
import com.phloc.webctrls.bootstrap3.ext.Bootstrap3DataTables;
import com.phloc.webctrls.bootstrap3.table.Bootstrap3Table;
import com.phloc.webctrls.bootstrap3.table.Bootstrap3TableFormView;

public class WebPageStyleBootstrap3 extends WebPageStyleSimple
{
  @Override
  @Nonnull
  public IHCElement <?> createErrorBox (@Nullable final String sText)
  {
    return Bootstrap3ErrorBox.create (sText);
  }

  @Override
  @Nonnull
  public IHCElement <?> createQuestionBox (@Nullable final String sText)
  {
    return Bootstrap3QuestionBox.create (sText);
  }

  @Override
  @Nonnull
  public IHCElement <?> createSuccessBox (@Nullable final String sText)
  {
    return Bootstrap3SuccessBox.create (sText);
  }

  @Override
  @Nonnull
  public Bootstrap3TableFormView createTable (@Nullable final HCCol... aCols)
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
}
