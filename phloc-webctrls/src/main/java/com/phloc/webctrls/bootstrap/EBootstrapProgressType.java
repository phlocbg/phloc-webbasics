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
package com.phloc.webctrls.bootstrap;

import javax.annotation.Nullable;

import com.phloc.html.css.ICSSClassProvider;

/**
 * Type of progress
 * 
 * @author philip
 */
public enum EBootstrapProgressType implements ICSSClassProvider
{
  SUCCESS (CBootstrapCSS.PROGRESS_SUCCESS),
  WARNING (CBootstrapCSS.PROGRESS_WARNING),
  DANGER (CBootstrapCSS.PROGRESS_DANGER),
  INFO (CBootstrapCSS.PROGRESS_INFO);

  private final ICSSClassProvider m_aCSSClass;

  private EBootstrapProgressType (@Nullable final ICSSClassProvider aCSSClass)
  {
    m_aCSSClass = aCSSClass;
  }

  @Nullable
  public String getCSSClass ()
  {
    return m_aCSSClass == null ? null : m_aCSSClass.getCSSClass ();
  }
}
