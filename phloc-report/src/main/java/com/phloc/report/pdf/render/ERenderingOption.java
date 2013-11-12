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
package com.phloc.report.pdf.render;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;

/**
 * This enum contains all the default placeholders supported.
 * 
 * @author Philip Helger
 */
public enum ERenderingOption
{
  /** 1-based page number in current page set */
  PAGESET_PAGENUM_CURRENT ("pageset-pagenum-current"),
  /** page count in current page set */
  PAGESET_PAGENUM_TOTAL ("pageset-pagenum-total"),
  /** 1-based page number over all page sets */
  TOTAL_PAGENUM_CURRENT ("total-pagenum-current"),
  /** page count over all page sets */
  TOTAL_PAGENUM_TOTAL ("total-pagenum-total");

  private final String m_sMacroName;
  private final String m_sPlaceholder;

  private ERenderingOption (@Nonnull @Nonempty final String sMacroName)
  {
    m_sMacroName = sMacroName;
    m_sPlaceholder = "${" + sMacroName + "}";
  }

  /**
   * @return The macro name of this option. E.g. <code>pagenum-current</code>.
   *         Never <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getMacroName ()
  {
    return m_sMacroName;
  }

  /**
   * @return The placeholder representation of this option. E.g.
   *         <code>${pagenum-current}</code>. Never <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getPlaceholder ()
  {
    return m_sPlaceholder;
  }
}
