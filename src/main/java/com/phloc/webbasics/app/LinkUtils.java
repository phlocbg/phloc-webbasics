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
package com.phloc.webbasics.app;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.EURLProtocol;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.ReadonlySimpleURL;
import com.phloc.commons.url.URLUtils;
import com.phloc.webbasics.app.scope.ScopeManager;

/**
 * Misc utilities to create link URLs.
 * 
 * @author philip
 */
@Immutable
public final class LinkUtils
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LinkUtils.class);

  private LinkUtils ()
  {}

  /**
   * Get the path to the passed servlet by prepending the context path.
   * 
   * @param sServletPath
   *        The servlet path to use. <b>Must</b> start with a "/" character!
   * @return The context path + the servlet path.<br>
   *         <code>/context/<i>servletpath</i></code>
   */
  @Nonnull
  public static ISimpleURL getServletURL (@Nonnull final String sServletPath)
  {
    return getServletURL (sServletPath, null);
  }

  @Nonnull
  public static ISimpleURL getServletURL (@Nonnull final String sServletPath,
                                          @Nullable final Map <String, String> aParams)
  {
    final String sURL = URLUtils.getURLString (getContextAwareURI (sServletPath), aParams, null, null);
    return new ReadonlySimpleURL (sURL);
  }

  /**
   * Prefix the passed href with the relative context path in case the passed
   * href has no protocol yet.
   * 
   * @param sHRef
   *        The href to be extended.
   * @return Either the original href if already absolute or
   *         <code>/webapp-context/sHRef</code> otherwise.
   */
  @Nonnull
  public static String getContextAwareURI (@Nonnull final String sHRef)
  {
    // If known protocol, keep it
    if (EURLProtocol.hasKnownProtocol (sHRef))
      return sHRef;

    final String sContextPath = ScopeManager.getGlobalScope ().getServletContext ().getContextPath ();
    if (StringHelper.hasText (sContextPath) && sHRef.startsWith (sContextPath))
    {
      s_aLogger.warn ("The passed href '" + sHRef + "' already contains the context path '" + sContextPath + "'!");
      return sHRef;
    }

    // Always prefix with context path!
    final StringBuilder aSB = new StringBuilder (sContextPath);
    if (!StringHelper.startsWith (sHRef, '/'))
      aSB.append ('/');
    return aSB.append (sHRef).toString ();
  }

  /**
   * Prefix the passed href with the absolute server + context path in case the
   * passed href has no protocol yet.
   * 
   * @param aHRef
   *        The href to be extended.
   * @return Either the original href if already absolute or
   *         <code>http://servername:8123/webapp-context/sHRef</code> otherwise.
   */
  @Nonnull
  public static StringBuilder getFullyQualifiedExternalURI (@Nonnull final CharSequence aHRef)
  {
    // If known protocol, keep it
    if (EURLProtocol.hasKnownProtocol (aHRef))
      return new StringBuilder (aHRef);

    // Always prefix with context path!
    final StringBuilder aSB = new StringBuilder (ScopeManager.getRequestScope ().getFullContextPath ());
    if (!StringHelper.startsWith (aHRef, '/'))
      aSB.append ('/');
    return aSB.append (aHRef);
  }

  @Nonnull
  public static ISimpleURL makeAbsoluteSimpleURL (@Nonnull final String sURL)
  {
    return new ReadonlySimpleURL (makeAbsoluteURL (sURL));
  }

  @Nonnull
  public static String makeAbsoluteURL (@Nullable final String sURL)
  {
    if (EURLProtocol.hasKnownProtocol (sURL))
      return sURL;

    // Full server and context path + URL
    return ScopeManager.getRequestScope ().getFullContextPath () + sURL;
  }
}
