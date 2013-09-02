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
import com.phloc.webctrls.bootstrap.BootstrapTable;
import com.phloc.webctrls.bootstrap.CBootstrapCSS;
import com.phloc.webctrls.bootstrap.EBootstrapButtonType;
import com.phloc.webctrls.bootstrap.derived.BootstrapErrorBox;
import com.phloc.webctrls.bootstrap.derived.BootstrapInfoBox;
import com.phloc.webctrls.bootstrap.derived.BootstrapQuestionBox;
import com.phloc.webctrls.bootstrap.derived.BootstrapSuccessBox;
import com.phloc.webctrls.bootstrap.derived.BootstrapTableFormView;
import com.phloc.webctrls.bootstrap.derived.BootstrapToolbar;
import com.phloc.webctrls.bootstrap.ext.BootstrapDataTables;
import com.phloc.webctrls.custom.toolbar.IButtonToolbar;

public class WebPageStylerBootstrap2 extends WebPageStylerSimple
{
  @Override
  @Nonnull
  public IHCElement <?> createErrorBox (@Nullable final String sText)
  {
    return BootstrapErrorBox.create (sText);
  }

  @Override
  @Nonnull
  public IHCElement <?> createInfoBox (@Nullable final String sText)
  {
    return BootstrapInfoBox.create (sText);
  }

  @Override
  @Nonnull
  public IHCElement <?> createSuccessBox (@Nullable final String sText)
  {
    return BootstrapSuccessBox.create (sText);
  }

  @Override
  @Nonnull
  public IHCElement <?> createQuestionBox (@Nullable final String sText)
  {
    return BootstrapQuestionBox.create (sText);
  }

  @Override
  @Nonnull
  public BootstrapTableFormView createTable (@Nullable final HCCol... aCols)
  {
    return new BootstrapTableFormView (aCols);
  }

  @Override
  @Nonnull
  public BootstrapDataTables createDefaultDataTables (@Nonnull final AbstractHCBaseTable <?> aTable,
                                                      @Nonnull final Locale aDisplayLocale)
  {
    final BootstrapDataTables ret = new BootstrapDataTables ((BootstrapTable) aTable);
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
  public IButtonToolbar <?> createToolbar ()
  {
    return new BootstrapToolbar ();
  }
}
