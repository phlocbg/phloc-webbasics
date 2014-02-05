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
package com.phloc.bootstrap3.modal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.html.css.ICSSClassProvider;

/**
 * Modal dialog size
 * 
 * @author Philip Helger
 */
public enum EBootstrapModalSize implements ICSSClassProvider
{
  SMALL (CBootstrapCSS.MODAL_SM),
  NORMAL (null),
  LARGE (CBootstrapCSS.MODAL_LG);

  public static final EBootstrapModalSize DEFAULT = NORMAL;

  private final ICSSClassProvider m_aCSSClass;

  private EBootstrapModalSize (@Nonnull final ICSSClassProvider aCSSClass)
  {
    m_aCSSClass = aCSSClass;
  }

  @Nullable
  public String getCSSClass ()
  {
    return m_aCSSClass == null ? null : m_aCSSClass.getCSSClass ();
  }
}