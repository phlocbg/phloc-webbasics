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
package com.phloc.appbasics.app.menu;

import javax.annotation.Nonnull;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;

/**
 * Contains predefined window targets. They are also available in HCA_Target of
 * phloc-html, but we don't include this library here....
 * 
 * @author Philip Helger
 */
public enum EMenuItemExternalTarget
{
  /** New window */
  BLANK ("_blank"),
  /** This window */
  SELF ("_self"),
  /** Parent frame */
  PARENT ("_parent"),
  /** Out of frames */
  TOP ("_top");

  public static final EMenuItemExternalTarget DEFAULT = SELF;

  private final String m_sName;

  private EMenuItemExternalTarget (@Nonnull @Nonempty final String sName)
  {
    m_sName = ValueEnforcer.notEmpty (sName, "Name");
  }

  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }
}
