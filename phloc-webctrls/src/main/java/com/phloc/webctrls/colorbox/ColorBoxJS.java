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
package com.phloc.webctrls.colorbox;

import javax.annotation.Nonnull;

import com.phloc.commons.ValueEnforcer;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.jquery.IJQuerySelector;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;

/**
 * jQuery colorbox plugin from
 * 
 * <pre>
 * http://www.jacklmoore.com/colorbox
 * </pre>
 * 
 * @author Philip Helger
 */
public class ColorBoxJS
{
  private ColorBoxJS ()
  {}

  @Nonnull
  public static JSInvocation invokeColorBox (@Nonnull final IJQuerySelector aSelector)
  {
    ValueEnforcer.notNull (aSelector, "Selector");
    registerExternalResources ();

    return aSelector.invoke ().invoke ("colorbox");
  }

  @Nonnull
  public static JSInvocation invokeColorBox (@Nonnull final IJQuerySelector aSelector,
                                             @Nonnull final ColorBoxOptions aOptions)
  {
    ValueEnforcer.notNull (aOptions, "Options");
    return invokeColorBox (aSelector).arg (aOptions.getJSOptions ());
  }

  public static void registerExternalResources ()
  {
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EColorBoxCSSPathProvider.COLORBOX);
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EColorBoxJSPathProvider.COLORBOX);
  }
}
