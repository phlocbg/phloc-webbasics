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
package com.phloc.bootstrap2;

import javax.annotation.Nullable;

import com.phloc.html.css.ICSSClassProvider;

/**
 * Button type
 * 
 * @author Philip Helger
 */
public enum EBootstrapButtonType implements ICSSClassProvider
{
  DEFAULT (null),
  PRIMARY (CBootstrapCSS.BTN_PRIMARY),
  INFO (CBootstrapCSS.BTN_INFO),
  SUCCESS (CBootstrapCSS.BTN_SUCCESS),
  WARNING (CBootstrapCSS.BTN_WARNING),
  DANGER (CBootstrapCSS.BTN_DANGER),
  INVERSE (CBootstrapCSS.BTN_INVERSE),
  LINK (CBootstrapCSS.BTN_LINK);

  private final ICSSClassProvider m_aCSSClass;

  private EBootstrapButtonType (@Nullable final ICSSClassProvider aCSSClass)
  {
    m_aCSSClass = aCSSClass;
  }

  @Nullable
  public String getCSSClass ()
  {
    return m_aCSSClass == null ? null : m_aCSSClass.getCSSClass ();
  }
}
