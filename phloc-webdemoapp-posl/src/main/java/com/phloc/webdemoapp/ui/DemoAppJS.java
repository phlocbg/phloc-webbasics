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
package com.phloc.webdemoapp.ui;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.html.js.builder.JSExpr;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.JSRef;

@Immutable
public final class DemoAppJS
{
  private DemoAppJS ()
  {}

  @Nonnull
  public static JSRef getDemoApp ()
  {
    // Match the JS file in src/main/webapp/js
    return JSExpr.ref ("DemoApp");
  }

  @Nonnull
  public static JSInvocation viewLogin ()
  {
    // Invoke the JS function "viewLogin" on the object
    return getDemoApp ().invoke ("viewLogin");
  }
}
