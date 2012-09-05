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
package com.phloc.webctrls.custom;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DefaultIcons
{
  private static Map <EDefaultIcon, IIcon> m_aMap = new HashMap <EDefaultIcon, IIcon> ();

  private DefaultIcons ()
  {}

  @Nullable
  public static IIcon get (@Nullable final EDefaultIcon eIcon)
  {
    return m_aMap.get (eIcon);
  }

  public static void set (@Nonnull final EDefaultIcon eIcon, @Nullable final IIcon aIcon)
  {
    if (eIcon == null)
      throw new NullPointerException ("default");
    m_aMap.put (eIcon, aIcon);
  }
}
