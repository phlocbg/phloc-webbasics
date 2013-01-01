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
package com.phloc.appbasics.exchange;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;

/**
 * Exchange file type names.
 * 
 * @author philip
 */
@Translatable
public enum EExchangeFileTypeName implements IHasDisplayText
{
  /** CSV - comma separated values */
  CSV ("CSV", "CSV"),
  /** Old Excel */
  XLS ("Excel 2003 (XLS)", "Excel 2003 (XLS)"),
  /** New Excel */
  XLSX ("Excel 2007 (XLSX)", "Excel 2007 (XLSX)"),
  /** XML structured text */
  XML ("XML", "XML"),
  /** Pure text file */
  TXT ("Text", "Text");

  private final ITextProvider m_aTP;

  private EExchangeFileTypeName (@Nonnull final String sDE, @Nonnull final String sEN)
  {
    m_aTP = TextProvider.create_DE_EN (sDE, sEN);
  }

  public String getDisplayText (final Locale aContentLocale)
  {
    return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
  }
}
