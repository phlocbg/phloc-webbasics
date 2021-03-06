/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.web.servlet.cookie;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phloc.commons.CGlobal;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.string.StringHelper;

/**
 * Misc. helper methods on HTTP cookies.
 *
 * @author Philip Helger
 */
@Immutable
public final class CookieHelper
{
  public static final int DEFAULT_MAX_AGE_SECONDS = 30 * CGlobal.SECONDS_PER_DAY;

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final CookieHelper s_aInstance = new CookieHelper ();

  private CookieHelper ()
  {}

  @Nonnull
  @ReturnsMutableCopy
  public static Map <String, Cookie> getAllCookies (@Nonnull final HttpServletRequest aHttpRequest)
  {
    ValueEnforcer.notNull (aHttpRequest, "HttpRequest");

    final Map <String, Cookie> ret = new LinkedHashMap <String, Cookie> ();
    final Cookie [] aCookies = aHttpRequest.getCookies ();
    if (aCookies != null)
      for (final Cookie aCookie : aCookies)
        ret.put (aCookie.getName (), aCookie);
    return ret;
  }

  @Nullable
  public static Cookie getCookie (@Nonnull final HttpServletRequest aHttpRequest, @Nonnull final String sCookieName)
  {
    ValueEnforcer.notNull (aHttpRequest, "HttpRequest");
    ValueEnforcer.notNull (sCookieName, "CookieName");

    final Cookie [] aCookies = aHttpRequest.getCookies ();
    if (aCookies != null)
      for (final Cookie aCookie : aCookies)
        if (aCookie.getName ().equals (sCookieName))
          return aCookie;
    return null;
  }

  public static boolean containsCookie (@Nonnull final HttpServletRequest aHttpRequest,
                                        @Nonnull final String sCookieName)
  {
    return getCookie (aHttpRequest, sCookieName) != null;
  }

  /**
   * Create a cookie that is bound on a certain path within the local web
   * server.
   *
   * @param sName
   *        The cookie name.
   * @param sValue
   *        The cookie value.
   * @param sPath
   *        The path the cookie is valid for.
   * @param bExpireWhenBrowserIsClosed
   *        Whether or not the cookie should expire on browser close
   * @return The created cookie object.
   */
  @Nonnull
  public static Cookie createCookie (@Nonnull final String sName,
                                     @Nullable final String sValue,
                                     final String sPath,
                                     final boolean bExpireWhenBrowserIsClosed)
  {
    final Cookie aCookie = new Cookie (sName, sValue);
    aCookie.setPath (sPath);
    if (bExpireWhenBrowserIsClosed)
      aCookie.setMaxAge (-1);
    else
      aCookie.setMaxAge (DEFAULT_MAX_AGE_SECONDS);
    return aCookie;
  }

  @Nonnull
  public static Cookie createContextCookie (@Nonnull final HttpServletRequest aHttpRequest,
                                            @Nonnull final String sName,
                                            @Nullable final String sValue,
                                            final boolean bExpireWhenBrowserIsClosed)
  {
    ValueEnforcer.notNull (aHttpRequest, "HttpRequest");

    final String sContextPath = aHttpRequest.getContextPath ();
    return createCookie (sName,
                         sValue,
                         StringHelper.hasText (sContextPath) ? sContextPath : "/",
                         bExpireWhenBrowserIsClosed);
  }

  /**
   * Remove a cookie by setting the max age to 0.
   *
   * @param aHttpResponse
   *        The HTTP response. May not be <code>null</code>.
   * @param aCookie
   *        The cookie to be removed. May not be <code>null</code>.
   */
  public static void removeCookie (@Nonnull final HttpServletResponse aHttpResponse, @Nonnull final Cookie aCookie)
  {
    ValueEnforcer.notNull (aHttpResponse, "HttpResponse");
    ValueEnforcer.notNull (aCookie, "aCookie");

    // expire the cookie!
    aCookie.setMaxAge (0);
    aHttpResponse.addCookie (aCookie);
  }
}
