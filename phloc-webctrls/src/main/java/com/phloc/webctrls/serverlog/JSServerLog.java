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
package com.phloc.webctrls.serverlog;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.js.builder.IJSExpression;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;

@Immutable
public final class JSServerLog
{
  private JSServerLog ()
  {}

  public static void registerResources ()
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EServerLogJSPathProvider.SERVERLOG);
  }

  @Nonnull
  public static JSInvocation serverLogInit (@Nonnull final ISimpleURL aURL,
                                            @Nonnull final String sKey,
                                            final boolean bDebugMode)
  {
    return new JSInvocation ("serverLogInit").arg (aURL.getAsString ()).arg (sKey).arg (bDebugMode);
  }

  @Nonnull
  public static JSInvocation serverLog (@Nonnull final String sSeverity, @Nonnull final String sMessage)
  {
    return new JSInvocation ("serverLog").arg (sSeverity).arg (sMessage);
  }

  @Nonnull
  public static JSInvocation setupServerLogForWindow ()
  {
    return new JSInvocation ("setupServerLogForWindow");
  }

  @Nonnull
  public static JSInvocation addExceptionHandlers (@Nonnull final IJSExpression aExpr)
  {
    return new JSInvocation ("addExceptionHandlers").arg (aExpr);
  }
}
