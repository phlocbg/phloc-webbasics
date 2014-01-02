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
package com.phloc.webctrls.datatables.comparator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.compare.AbstractComparator;
import com.phloc.commons.format.IFormatter;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Base class for all table comparators
 * 
 * @author Philip Helger
 */
public abstract class AbstractComparatorTable extends AbstractComparator <String>
{
  private final IFormatter m_aFormatter;

  public AbstractComparatorTable ()
  {
    this (null);
  }

  public AbstractComparatorTable (@Nullable final IFormatter aFormatter)
  {
    super ();
    m_aFormatter = aFormatter;
  }

  @Nullable
  public final IFormatter getFormatter ()
  {
    return m_aFormatter;
  }

  /**
   * Get the formatted text of the passed text
   * 
   * @param sCell
   *        Original cell text
   * @return Never <code>null</code>.
   */
  @OverrideOnDemand
  @Nonnull
  protected String getFormattedText (@Nullable final String sCell)
  {
    return sCell == null ? "" : m_aFormatter == null ? sCell : m_aFormatter.getFormattedValue (sCell);
  }

  protected abstract int internalCompare (@Nonnull final String sText1, @Nonnull final String sText2);

  @Override
  protected final int mainCompare (@Nullable final String sCellText1, @Nullable final String sCellText2)
  {
    final String sText1 = getFormattedText (sCellText1);
    final String sText2 = getFormattedText (sCellText2);

    return internalCompare (sText1, sText2);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).appendIfNotNull ("formatter", m_aFormatter).toString ();
  }
}
