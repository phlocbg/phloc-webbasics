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
package com.phloc.webctrls.bootstrap3.nav;

import java.util.List;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.webctrls.bootstrap3.CBootstrap3CSS;

/**
 * Type of Nav
 * 
 * @author Philip Helger
 */
public enum EBootstrap3NavType
{
  TABS (CBootstrap3CSS.NAV_TABS),
  TABS_JUSTIFIED (CBootstrap3CSS.NAV_TABS, CBootstrap3CSS.NAV_JUSTIFIED),
  PILLS (CBootstrap3CSS.NAV_PILLS),
  PILLS_STACKED (CBootstrap3CSS.NAV_PILLS, CBootstrap3CSS.NAV_STACKED),
  PILLS_JUSTIFIED (CBootstrap3CSS.NAV_PILLS, CBootstrap3CSS.NAV_JUSTIFIED);

  private final List <ICSSClassProvider> m_aCSSClasses;

  private EBootstrap3NavType (@Nonnull @Nonempty final ICSSClassProvider... aCSSClasses)
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
