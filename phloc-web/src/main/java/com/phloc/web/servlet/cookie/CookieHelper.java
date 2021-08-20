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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.string.StringHelper;
import com.phloc.web.http.CHTTPHeader;

/**
 * Misc. helper methods on HTTP cookies.
 *
 * @author Boris Gregorcic
 */
@Immutable
public final class CookieHelper
{
  public static final int DEFAULT_MAX_AGE_SECONDS = 30 * CGlobal.SECONDS_PER_DAY;

  private static final AtomicBoolean FORCE_COOKIES_SECURE = new AtomicBoolean ();
  private static final AtomicBoolean FORCE_COOKIES_SAMESITE_NONE = new AtomicBoolean ();

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final CookieHelper s_aInstance = new CookieHelper ();

  private CookieHelper ()
  {}

  public static void setForceCookiesSameSiteNone (final boolean bForce)
  {
    FORCE_COOKIES_SAMESITE_NONE.set (bForce);
  }

  public static void setForceCookiesSecure (final boolean bForce)
  {
    FORCE_COOKIES_SECURE.set (bForce);
  }

  @Nonnull
  @ReturnsMutableCopy
  public static Map <String, Cookie> getAllCookies (@Nonnull final HttpServletRequest aHttpRequest)
  {
    ValueEnforcer.notNull (aHttpRequest, "HttpRequest"); //$NON-NLS-1$

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
    ValueEnforcer.notNull (aHttpRequest, "HttpRequest"); //$NON-NLS-1$
    ValueEnforcer.notNull (sCookieName, "CookieName"); //$NON-NLS-1$

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
    ValueEnforcer.notNull (aHttpRequest, "HttpRequest"); //$NON-NLS-1$

    final String sContextPath = aHttpRequest.getContextPath ();
    return createCookie (sName,
                         sValue,
                         StringHelper.hasText (sContextPath) ? sContextPath : "/", //$NON-NLS-1$
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
    ValueEnforcer.notNull (aHttpResponse, "HttpResponse"); //$NON-NLS-1$
    ValueEnforcer.notNull (aCookie, "aCookie"); //$NON-NLS-1$

    // expire the cookie!
    aCookie.setMaxAge (0);
    aHttpResponse.addCookie (aCookie);
  }

  public static void correctCookieHeaders (final HttpServletResponse aHttpResponse)
  {
    if (!FORCE_COOKIES_SECURE.get () && !FORCE_COOKIES_SAMESITE_NONE.get ())
    {
      return;
    }
    final List <String> aCookieHeaders = ContainerHelper.newList ();

    for (final String sHeaderName : aHttpResponse.getHeaderNames ())
    {
      if (EqualsUtils.nullSafeEqualsIgnoreCase (sHeaderName, CHTTPHeader.SET_COOKIE))
      {
        aCookieHeaders.addAll (aHttpResponse.getHeaders (sHeaderName));
      }
    }
    if (ContainerHelper.isNotEmpty (aCookieHeaders))
    {
      boolean bFirst = true;
      for (final String sCookieHeader : aCookieHeaders)
      {
        final String sCorrected = CookieHelper.correctCookieHeader (sCookieHeader);
        if (bFirst)
        {
          aHttpResponse.setHeader (CHTTPHeader.SET_COOKIE, sCorrected);
        }
        else
        {
          aHttpResponse.addHeader (CHTTPHeader.SET_COOKIE, sCorrected);
        }
        bFirst = false;
      }
    }
  }

  private static String correctCookieHeader (final String sHeader)
  {
    String sCorrected = sHeader;
    if ((FORCE_COOKIES_SECURE.get () || FORCE_COOKIES_SAMESITE_NONE.get ()) &&
        !StringHelper.containsIgnoreCase (sHeader, "Secure", Locale.ENGLISH)) //$NON-NLS-1$
    {
      sCorrected += "; Secure"; //$NON-NLS-1$
    }
    if (FORCE_COOKIES_SAMESITE_NONE.get () && !StringHelper.containsIgnoreCase (sHeader, "SameSite", Locale.ENGLISH)) //$NON-NLS-1$
    {
      sCorrected += "; SameSite=none"; //$NON-NLS-1$
    }
    return sCorrected;
  }
}
