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
package com.phloc.webctrls.datatables.comparator;

import java.text.Collator;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.compare.CollatorUtils;
import com.phloc.commons.format.IFormatter;

public class ComparatorTableString extends AbstractComparatorTable
{
  private final Collator m_aCollator;

  public ComparatorTableString (@Nonnull final Locale aDisplayLocale)
  {
    this (null, aDisplayLocale);
  }

  public ComparatorTableString (@Nullable final IFormatter aFormatter, @Nonnull final Locale aDisplayLocale)
  {
    super (aFormatter);
    m_aCollator = CollatorUtils.getCollatorSpaceBeforeDot (aDisplayLocale);
  }

  @Override
  protected final int internalCompare (@Nonnull final String sText1, @Nonnull final String sText2)
  {
    return m_aCollator.compare (sText1, sText2);
  }
}
