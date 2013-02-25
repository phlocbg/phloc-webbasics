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
package com.phloc.web.session;

import java.util.Enumeration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.state.EChange;

/**
 * HTTP session utilities.
 * 
 * @author philip
 */
@Immutable
public final class SessionHelper
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (SessionHelper.class);

  private SessionHelper ()
  {}

  /**
   * Invalidate the session if the session is still active.
   * 
   * @param aSession
   *        The session to be invalidated. May be <code>null</code>.
   * @return {@link EChange#CHANGED} if the session was invalidated,
   *         {@link EChange#UNCHANGED} otherwise.
   */
  @Nonnull
  public static EChange safeInvalidateSession (@Nullable final HttpSession aSession)
  {
    if (aSession != null)
    {
      try
      {
        aSession.invalidate ();
        return EChange.CHANGED;
      }
      catch (final IllegalStateException ex)
      {
        // session already invalidated
      }
    }
    return EChange.UNCHANGED;
  }

  @Deprecated
  @Nonnull
  public static HttpSession safeGetSession (@Nonnull final HttpServletRequest aRequest)
  {
    return aRequest.getSession (true);
  }

  @Nonnull
  public static Enumeration <String> getAllAttributes (@Nonnull final HttpSession aSession)
  {
    try
    {
      return GenericReflection.<Enumeration <?>, Enumeration <String>> uncheckedCast (aSession.getAttributeNames ());
    }
    catch (final IllegalStateException ex)
    {
      // Session no longer valid
      return ContainerHelper.<String> getEmptyEnumeration ();
    }
  }

  @Nonnull
  public static HttpSession safeRenewSession (@Nonnull final HttpServletRequest aRequest)
  {
    if (aRequest == null)
      throw new NullPointerException ("request");

    // Is there any existing session?
    final HttpSession aSession = aRequest.getSession (false);
    if (aSession != null)
    {
      try
      {
        s_aLogger.info ("Invalidating session " + aSession.getId ());
        aSession.invalidate ();
      }
      catch (final IllegalStateException ex)
      {
        // session already invalidated
      }
    }

    // Create the new session
    return aRequest.getSession (true);
  }
}
