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
package com.phloc.tinymce4;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSExpr;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.JSRef;

/**
 * JS wrapped for TinyMCE4
 * 
 * @author Philip Helger
 */
@Immutable
public final class JSTinyMCE4
{
  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final JSTinyMCE4 s_aInstance = new JSTinyMCE4 ();

  private JSTinyMCE4 ()
  {}

  @Nonnull
  public static JSRef tinymce ()
  {
    return JSExpr.ref ("tinymce");
  }

  @Nonnull
  public static JSInvocation init (@Nonnull final JSAssocArray aOptions)
  {
    return tinymce ().invoke ("init").arg (aOptions);
  }
}
