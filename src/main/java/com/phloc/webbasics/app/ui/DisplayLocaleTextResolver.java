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
package com.phloc.webbasics.app.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.webbasics.app.ApplicationRequestManager;

/**
 * Resolves the current display locale of the application and passes the request
 * on to the {@link DefaultTextResolver}.
 * 
 * @author philip
 */
@Immutable
public final class DisplayLocaleTextResolver
{
  private DisplayLocaleTextResolver ()
  {}

  /**
   * Resolve the passed text element in the application's display locale. No
   * argument replacement happens.
   * 
   * @param aEnum
   *        The text enum element (required for the ID)
   * @param aTP
   *        The main text provider keeping the static text elements.
   * @return The text in the current display locale.
   */
  @Nullable
  public static String getText (@Nonnull final Enum <? extends IPredefinedLocaleTextProvider> aEnum,
                                @Nonnull final ITextProvider aTP)
  {
    return DefaultTextResolver.getText (aEnum, aTP, ApplicationRequestManager.getRequestDisplayLocale ());
  }

  /**
   * Resolve the passed text element in the application's display locale.
   * Argument replacement takes place.
   * 
   * @param aEnum
   *        The text enum element (required for the ID)
   * @param aTP
   *        The main text provider keeping the static text elements.
   * @param aArgs
   *        The arguments to be inserted into the text elements.
   * @return The text in the current display locale with all parameters already
   *         replaced.
   * @see com.phloc.commons.text.impl.TextFormatter#getFormattedText(String,
   *      Object...)
   */
  @Nullable
  public static String getTextWithArgs (@Nonnull final Enum <? extends IPredefinedLocaleTextProvider> aEnum,
                                        @Nonnull final ITextProvider aTP,
                                        @Nonnull final Object [] aArgs)
  {
    return DefaultTextResolver.getTextWithArgs (aEnum, aTP, ApplicationRequestManager.getRequestDisplayLocale (), aArgs);
  }
}
