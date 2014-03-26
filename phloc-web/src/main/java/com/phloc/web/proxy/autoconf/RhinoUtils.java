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
package com.phloc.web.proxy.autoconf;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.streams.StreamUtils;

@Immutable
public final class RhinoUtils
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (RhinoUtils.class);

  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final RhinoUtils s_aInstance = new RhinoUtils ();

  private RhinoUtils ()
  {}

  public static void readFile (@Nonnull final Scriptable aScope,
                               @Nonnull final Context aCtx,
                               @Nonnull final IReadableResource aRes,
                               @Nonnull final String sResName,
                               @Nonnull final String sCharset)
  {
    final InputStream aIS = aRes.getInputStream ();
    if (aIS != null)
    {
      Reader aReader = null;
      try
      {
        aReader = new InputStreamReader (aIS, sCharset);
        aCtx.evaluateReader (aScope, aReader, sResName, 1, null);
      }
      catch (final Exception ex)
      {
        throw new IllegalArgumentException ("Failed to read JS resource '" + sResName + "'", ex);
      }
      finally
      {
        StreamUtils.close (aReader);
      }
    }
    else
      s_aLogger.warn ("Failed to open JS file '" + sResName + "'");
  }

  public static void readString (@Nonnull final Scriptable aScope,
                                 @Nonnull final Context aCtx,
                                 @Nonnull final String sJSCode)
  {
    ValueEnforcer.notNull (aScope, "Scope");
    ValueEnforcer.notNull (aCtx, "Ctx");
    ValueEnforcer.notNull (sJSCode, "JSCode");
    aCtx.evaluateString (aScope, sJSCode, "code", 1, null);
  }
}
