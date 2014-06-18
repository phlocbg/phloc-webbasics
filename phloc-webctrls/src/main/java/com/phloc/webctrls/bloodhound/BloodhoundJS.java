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
package com.phloc.webctrls.bloodhound;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.html.js.builder.JSExpr;
import com.phloc.html.js.builder.JSFieldRef;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.JSRef;

@Immutable
public final class BloodhoundJS
{
  private BloodhoundJS ()
  {}

  @Nonnull
  public static JSRef bloodhound ()
  {
    return JSExpr.ref ("Bloodhound");
  }

  @Nonnull
  public static JSFieldRef bloodhoundTokenizers ()
  {
    return bloodhound ().ref ("tokenizers");
  }

  @Nonnull
  public static JSFieldRef bloodhoundTokenizersWhitespace ()
  {
    return bloodhoundTokenizers ().ref ("whitespace");
  }

  @Nonnull
  public static JSFieldRef bloodhoundTokenizersNonword ()
  {
    return bloodhoundTokenizers ().ref ("nonword");
  }

  @Nonnull
  public static JSInvocation newBloodhound (@Nonnull final BloodhoundOptions aOptions)
  {
    ValueEnforcer.notNull (aOptions, "Options");

    return new JSInvocation (bloodhound ()).arg (aOptions.getAsJSObject ());
  }
}
