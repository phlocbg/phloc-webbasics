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
package com.phloc.bootstrap3.nav;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.html.css.ICSSClassProvider;

/**
 * Type of Nav
 * 
 * @author Philip Helger
 */
public enum EBootstrapNavType
{
  DEFAULT,
  TABS (CBootstrapCSS.NAV_TABS),
  TABS_JUSTIFIED (CBootstrapCSS.NAV_TABS, CBootstrapCSS.NAV_JUSTIFIED),
  PILLS (CBootstrapCSS.NAV_PILLS),
  PILLS_STACKED (CBootstrapCSS.NAV_PILLS, CBootstrapCSS.NAV_STACKED),
  PILLS_JUSTIFIED (CBootstrapCSS.NAV_PILLS, CBootstrapCSS.NAV_JUSTIFIED);

  private final List <ICSSClassProvider> m_aCSSClasses;

  private EBootstrapNavType (@Nullable final ICSSClassProvider... aCSSClasses)
  {
    m_aCSSClasses = ContainerHelper.newList (aCSSClasses);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <ICSSClassProvider> getAllCSSClasses ()
  {
    return ContainerHelper.newList (m_aCSSClasses);
  }
}
