/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.webbasics.web;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.string.StringHelper;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;

/**
 * Misc. helper methods on HTTP cookies.
 * 
 * @author philip
 */
@Immutable
public final class CookieHelper
{
  private static final int DEFAULT_MAX_AGE_SECONDS = 30 * CGlobal.SECONDS_PER_DAY;
  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final CookieHelper s_aInstance = new CookieHelper ();

  private CookieHelper ()
  {}

  @Nullable
  public static Cookie getCookie (@Nonnull final HttpServletRequest aHttpRequest, @Nonnull final String sCookieName)
  {
    if (aHttpRequest == null)
      throw new NullPointerException ("request");
    if (sCookieName == null)
      throw new NullPointerException ("cookieName");

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
  public static Cookie createContextCookie (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                            @Nonnull final String sName,
                                            @Nullable final String sValue,
                                            final boolean bExpireWhenBrowserIsClosed)
  {
    return createContextCookie (aRequestScope.getRequest (), sName, sValue, bExpireWhenBrowserIsClosed);
  }

  @Nonnull
  public static Cookie createContextCookie (@Nonnull final HttpServletRequest aHttpRequest,
                                            @Nonnull final String sName,
                                            @Nullable final String sValue,
                                            final boolean bExpireWhenBrowserIsClosed)
  {
    if (aHttpRequest == null)
      throw new NullPointerException ("request");

    final String sContextPath = aHttpRequest.getContextPath ();
    return createCookie (sName,
                         sValue,
                         StringHelper.hasText (sContextPath) ? sContextPath : "/",
                         bExpireWhenBrowserIsClosed);
  }

  public static void removeCookie (@Nonnull final HttpServletResponse aHttpResponse, @Nonnull final Cookie aCookie)
  {
    if (aHttpResponse == null)
      throw new NullPointerException ("httpResponse");
    if (aCookie == null)
      throw new NullPointerException ("cookie");

    // expire the cookie!
    aCookie.setMaxAge (0);
    aHttpResponse.addCookie (aCookie);
  }
}
