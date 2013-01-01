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
package com.phloc.webctrls.custom;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.webctrls.famfam.EFamFamIcon;

/**
 * This class maintains the actual icon objects to the different default icons.
 * 
 * @author philip
 */
public final class DefaultIcons
{
  private static final Map <EDefaultIcon, IIcon> s_aMap = new HashMap <EDefaultIcon, IIcon> ();

  static
  {
    // Vendor and library independent
    EFamFamIcon.setAsDefault ();
  }

  private DefaultIcons ()
  {}

  @Nullable
  public static IIcon get (@Nullable final EDefaultIcon eIcon)
  {
    return s_aMap.get (eIcon);
  }

  public static void set (@Nonnull final EDefaultIcon eIcon, @Nullable final IIcon aIcon)
  {
    if (eIcon == null)
      throw new NullPointerException ("default");
    s_aMap.put (eIcon, aIcon);
  }
}
