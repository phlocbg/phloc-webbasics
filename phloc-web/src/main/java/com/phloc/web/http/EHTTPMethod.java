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
package com.phloc.web.http;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.lang.EnumHelper;
import com.phloc.commons.name.IHasName;

/**
 * HTTP 1.1 methods.<br>
 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html plus PACTH (rfc5789)
 *
 * @author Boris Gregorcic
 */
public enum EHTTPMethod implements IHasName
{
 OPTIONS ("OPTIONS", true, true),
 GET ("GET", true, true),
 HEAD ("HEAD", true, true),
 POST ("POST", false, false),
 PUT ("PUT", true, false),
 DELETE ("DELETE", true, false),
 TRACE ("TRACE", true, false),
 CONNECT ("CONNECT", false, false),
 PATCH ("PATCH", false, false);

  private final String m_sName;
  // https://tools.ietf.org/html/rfc2616#section-9.1
  private boolean m_bIdempotent;
  private boolean m_bSafe;

  private EHTTPMethod (@Nonnull @Nonempty final String sName, final boolean bIdempotent, final boolean bSafe)
  {
    this.m_sName = sName;
    this.m_bIdempotent = bIdempotent;
    this.m_bSafe = bSafe;
  }

  @Override
  @Nonnull
  @Nonempty
  public String getName ()
  {
    return this.m_sName;
  }

  public boolean isIdempodent ()
  {
    return this.m_bIdempotent;
  }

  public boolean isSAfe ()
  {
    return this.m_bSafe;
  }

  public boolean isContentAllowed ()
  {
    return this != HEAD;
  }

  @Nullable
  public static EHTTPMethod getFromNameOrNull (@Nullable final String sName)
  {
    return EnumHelper.getFromNameCaseInsensitiveOrNull (EHTTPMethod.class, sName);
  }
}
