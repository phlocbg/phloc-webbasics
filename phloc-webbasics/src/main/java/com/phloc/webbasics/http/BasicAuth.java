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
package com.phloc.webbasics.http;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.base64.Base64Helper;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.string.StringHelper;

@Immutable
public final class BasicAuth
{
  private static final String HEADER_VALUE_PREFIX_BASIC = "Basic ";

  private BasicAuth ()
  {}

  @Nonnull
  @Nonempty
  public static String getRequestHeaderValue (@Nonnull final String sUsername, @Nullable final String sPassword)
  {
    if (StringHelper.hasNoText (sUsername))
      throw new IllegalArgumentException ("username is missing");

    final String sCombined = StringHelper.getConcatenatedOnDemand (sUsername, ":", sPassword);
    return HEADER_VALUE_PREFIX_BASIC + Base64Helper.safeEncode (sCombined, CCharset.CHARSET_ISO_8859_1_OBJ);
  }
}
