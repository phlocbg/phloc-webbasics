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
package com.phloc.webctrls.bootstrap3.alert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.html.css.ICSSClassProvider;
import com.phloc.webctrls.bootstrap3.CBootstrap3CSS;

/**
 * Type of alert
 * 
 * @author Philip Helger
 */
public enum EBootstrap3AlertType implements ICSSClassProvider
{
  SUCCESS (CBootstrap3CSS.ALERT_SUCCESS),
  INFO (CBootstrap3CSS.ALERT_INFO),
  WARNING (CBootstrap3CSS.ALERT_WARNING),
  DANGER (CBootstrap3CSS.ALERT_DANGER);

  private final ICSSClassProvider m_aCSSClass;

  private EBootstrap3AlertType (@Nonnull final ICSSClassProvider aCSSClass)
  {
    m_aCSSClass = aCSSClass;
  }

  @Nullable
  public String getCSSClass ()
  {
    return m_aCSSClass.getCSSClass ();
  }
}
