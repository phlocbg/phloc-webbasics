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
 * Type of alert
 * 
 * @author Philip Helger
 */
public enum EBootstrapAlertType implements ICSSClassProvider
{
  DEFAULT (null),
  SUCCESS (CBootstrapCSS.ALERT_SUCCESS),
  DANGER (CBootstrapCSS.ALERT_DANGER),
  ERROR (CBootstrapCSS.ALERT_ERROR),
  INFO (CBootstrapCSS.ALERT_INFO);

  private final ICSSClassProvider m_aCSSClass;

  private EBootstrapAlertType (@Nullable final ICSSClassProvider aCSSClass)
  {
    m_aCSSClass = aCSSClass;
  }

  @Nullable
  public String getCSSClass ()
  {
    return m_aCSSClass == null ? null : m_aCSSClass.getCSSClass ();
  }
}
