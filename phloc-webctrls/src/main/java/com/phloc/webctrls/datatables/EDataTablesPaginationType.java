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
package com.phloc.webctrls.datatables;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.DevelopersNote;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Since;
import com.phloc.commons.name.IHasName;

/**
 * DataTables pagination type
 * 
 * @author Philip Helger
 */
public enum EDataTablesPaginationType implements IHasName
{
  @Deprecated
  @DevelopersNote ("DataTables 1.9.4")
  TWO_BUTTON ("twobutton"),
  @Since ("1.10")
  SIMPLE ("simple"),
  @Since ("1.10")
  SIMPLE_NUMBERS ("simple_numbers"),
  @Since ("1.10")
  FULL ("full"),
  FULL_NUMBERS ("full_numbers"),
  /**
   * Only available if the respective datatables plugin is present and only for
   * Bootstrap 1.9 - not for 1.10!
   */
  @Deprecated
  @DevelopersNote ("DataTables 1.9.4")
  BOOTSTRAP ("bootstrap");

  private final String m_sName;

  private EDataTablesPaginationType (@Nonnull @Nonempty final String sName)
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
