/**
 * Copyright (C) 2006-2012 phloc systems
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
 * Type of badge
 * 
 * @author philip
 */
public enum EBootstrapBadgeType implements ICSSClassProvider
{
  DEFAULT (null),
  SUCCESS ("badge-success"),
  WARNING ("badge-warning"),
  IMPORTANT ("badge-important"),
  INFO ("badge-info"),
  INVERSE ("badge-inverse");

  private final String m_sCSSClass;

  private EBootstrapBadgeType (@Nullable final String sCSSClass)
  {
    m_sCSSClass = sCSSClass;
  }

  @Nullable
  public String getCSSClass ()
  {
    return m_sCSSClass;
  }
}
