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
package com.phloc.bootstrap3.alert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.html.css.ICSSClassProvider;

/**
 * Type of alert
 * 
 * @author Philip Helger
 */
public enum EBootstrapAlertType implements ICSSClassProvider
{
  SUCCESS (CBootstrapCSS.ALERT_SUCCESS),
  INFO (CBootstrapCSS.ALERT_INFO),
  WARNING (CBootstrapCSS.ALERT_WARNING),
  DANGER (CBootstrapCSS.ALERT_DANGER);

  private final ICSSClassProvider m_aCSSClass;

  private EBootstrapAlertType (@Nonnull final ICSSClassProvider aCSSClass)
  {
    m_aCSSClass = aCSSClass;
  }

  @Nullable
  public String getCSSClass ()
  {
    return m_aCSSClass.getCSSClass ();
  }
}
