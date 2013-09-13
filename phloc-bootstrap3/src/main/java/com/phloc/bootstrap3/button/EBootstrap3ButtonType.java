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
package com.phloc.bootstrap3.button;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrap3CSS;
import com.phloc.html.css.ICSSClassProvider;

/**
 * Button type
 * 
 * @author Philip Helger
 */
public enum EBootstrap3ButtonType implements ICSSClassProvider
{
  DEFAULT (CBootstrap3CSS.BTN_DEFAULT),
  PRIMARY (CBootstrap3CSS.BTN_PRIMARY),
  SUCCESS (CBootstrap3CSS.BTN_SUCCESS),
  INFO (CBootstrap3CSS.BTN_INFO),
  WARNING (CBootstrap3CSS.BTN_WARNING),
  DANGER (CBootstrap3CSS.BTN_DANGER),
  LINK (CBootstrap3CSS.BTN_LINK);

  private final ICSSClassProvider m_aCSSClass;

  private EBootstrap3ButtonType (@Nonnull final ICSSClassProvider aCSSClass)
  {
    m_aCSSClass = aCSSClass;
  }

  @Nullable
  public String getCSSClass ()
  {
    return m_aCSSClass.getCSSClass ();
  }
}
