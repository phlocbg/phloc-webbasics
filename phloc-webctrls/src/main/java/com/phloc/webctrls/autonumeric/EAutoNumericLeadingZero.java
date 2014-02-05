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
package com.phloc.webctrls.autonumeric;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;

public enum EAutoNumericLeadingZero
{
  /**
   * allows leading zero to be entered. They are removed on focusout event
   * (default)
   */
  ALLOW ("allow"),
  /** leading zeros not allowed. */
  DENY ("deny"),
  /** leading zeros allowed and will be retained on the focusout event */
  KEEP ("keep");

  private final String m_sID;

  private EAutoNumericLeadingZero (@Nonnull @Nonempty final String sID)
  {
    m_sID = sID;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }
}