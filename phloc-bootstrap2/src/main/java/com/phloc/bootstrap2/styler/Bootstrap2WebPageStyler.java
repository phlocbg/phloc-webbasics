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
package com.phloc.bootstrap2.styler;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap2.AbstractBootstrapTable;
import com.phloc.bootstrap2.BootstrapTabBox;
import com.phloc.bootstrap2.BootstrapTable;
import com.phloc.bootstrap2.CBootstrapCSS;
import com.phloc.bootstrap2.EBootstrapButtonType;
import com.phloc.bootstrap2.derived.BootstrapErrorBox;
import com.phloc.bootstrap2.derived.BootstrapInfoBox;
import com.phloc.bootstrap2.derived.BootstrapQuestionBox;
import com.phloc.bootstrap2.derived.BootstrapSuccessBox;
import com.phloc.bootstrap2.derived.BootstrapTableForm;
import com.phloc.bootstrap2.derived.BootstrapTableFormView;
import com.phloc.bootstrap2.derived.BootstrapToolbar;
import com.phloc.bootstrap2.ext.BootstrapDataTables;
import com.phloc.html.hc.IHCTable;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webctrls.styler.SimpleWebPageStyler;

public class Bootstrap2WebPageStyler extends SimpleWebPageStyler
{
  @Override
  @Nonnull
  public BootstrapErrorBox createErrorBox (@Nullable final String sText)
  {
    return BootstrapErrorBox.create (sText);
  }

  @Override
  @Nonnull
  public BootstrapInfoBox createInfoBox (@Nullable final String sText)
  {
    return BootstrapInfoBox.create (sText);
  }

  @Override
  @Nonnull
  public BootstrapSuccessBox createSuccessBox (@Nullable final String sText)
  {
    return BootstrapSuccessBox.create (sText);
  }

  @Override
  @Nonnull
  public BootstrapQuestionBox createQuestionBox (@Nullable final String sText)
  {
    return BootstrapQuestionBox.create (sText);
  }

  @Override
  @Nonnull
  public BootstrapTable createTable (@Nullable final HCCol... aCols)
  {
    return new BootstrapTable (aCols);
  }

  @Override
  @Nonnull
  public BootstrapTableForm createTableForm (@Nullable final HCCol... aCols)
  {
    return new BootstrapTableForm (aCols);
  }

  @Override
  @Nonnull
  public BootstrapTableFormView createTableFormView (@Nullable final HCCol... aCols)
  {
    return new BootstrapTableFormView (aCols);
  }

  @Override
  @Nonnull
  public BootstrapDataTables createDefaultDataTables (@Nonnull final IHCTable <?> aTable,
                                                      @Nonnull final Locale aDisplayLocale)
  {
    final BootstrapDataTables ret = new BootstrapDataTables ((AbstractBootstrapTable <?>) aTable);
    ret.setDisplayLocale (aDisplayLocale);
    ret.addAllColumns (aTable);
    return ret;
  }

  @Override
  @Nonnull
  public HCSpan createUploadButton (@Nonnull final Locale aDisplayLocale)
  {
    return new HCSpan ().addClasses (CBootstrapCSS.BTN, EBootstrapButtonType.SUCCESS)
                        .addChild (EWebBasicsText.FILE_SELECT.getDisplayText (aDisplayLocale));
  }

  @Override
  @Nonnull
  public BootstrapToolbar createToolbar ()
  {
    return new BootstrapToolbar ();
  }

  @Override
  @Nonnull
  public BootstrapTabBox createTabBox ()
  {
    return new BootstrapTabBox ();
  }
}
