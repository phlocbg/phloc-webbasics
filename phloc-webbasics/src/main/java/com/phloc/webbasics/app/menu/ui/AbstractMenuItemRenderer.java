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
package com.phloc.webbasics.app.menu.ui;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.html.hc.html.AbstractHCList;
import com.phloc.html.hc.html.HCLI;

/**
 * Abstract base implementation of {@link IMenuItemRenderer}
 * 
 * @author Philip Helger
 */
public abstract class AbstractMenuItemRenderer <T extends AbstractHCList <?>> implements IMenuItemRenderer <T>
{
  private final Locale m_aContentLocale;

  public AbstractMenuItemRenderer (@Nonnull final Locale aContentLocale)
  {
    if (aContentLocale == null)
      throw new NullPointerException ("contentLocale");
    m_aContentLocale = aContentLocale;
  }

  @Nonnull
  public final Locale getContentLocale ()
  {
    return m_aContentLocale;
  }

  public void onLevelDown (@Nonnull final T aNewLevel)
  {}

  public void onLevelUp (@Nonnull final T aLastLevel)
  {}

  public void onMenuSeparatorItem (@Nonnull final HCLI aLI)
  {}

  public void onMenuItemPageItem (@Nonnull final HCLI aLI,
                                  final boolean bHasChildren,
                                  final boolean bIsSelected,
                                  final boolean bIsExpanded)
  {}

  public void onMenuItemExternalItem (@Nonnull final HCLI aLI,
                                      final boolean bHasChildren,
                                      final boolean bIsSelected,
                                      final boolean bIsExpanded)
  {}
}
