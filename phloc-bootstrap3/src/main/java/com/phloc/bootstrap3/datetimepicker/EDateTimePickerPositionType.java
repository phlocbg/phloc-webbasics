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
package com.phloc.bootstrap3.datetimepicker;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;

/**
 * Defines the different position types of DTP.
 * 
 * @author Philip Helger
 */
public enum EDateTimePickerPositionType
{
  BOTTOM_RIGHT ("bottom-right"),
  BOTTOM_LEFT ("bottom-left");

  private final String m_sJSValue;

  private EDateTimePickerPositionType (@Nonnull @Nonempty final String sJSValue)
  {
    m_sJSValue = sJSValue;
  }

  @Nonnull
  @Nonempty
  public String getJSValue ()
  {
    return m_sJSValue;
  }
}
