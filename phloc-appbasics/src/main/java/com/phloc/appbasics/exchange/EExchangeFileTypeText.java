/**
 * Copyright (C) 2006-2014 phloc systems
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
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayTextWithArgs;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;

/**
 * File type texts
 * 
 * @author Philip Helger
 */
@Translatable
public enum EExchangeFileTypeText implements IHasDisplayTextWithArgs
{
  EXPORT_AS ("Exportieren als {0}", "Export as {0}"),
  SAVE_AS ("Speichern als {0}", "Save as {0}");

  private final ITextProvider m_aTP;

  private EExchangeFileTypeText (@Nonnull final String sDE, @Nonnull final String sEN)
  {
    m_aTP = TextProvider.create_DE_EN (sDE, sEN);
  }

  public String getDisplayTextWithArgs (@Nonnull final Locale aContentLocale, @Nullable final Object... aArgs)
  {
    return DefaultTextResolver.getTextWithArgs (this, m_aTP, aContentLocale, aArgs);
  }
}
