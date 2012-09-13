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
package com.phloc.webctrls.datetime;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.resource.js.IJSPathProvider;
import com.phloc.html.resource.js.IJSPathProviderWithParam;
import com.phloc.html.resource.js.JSFilenameHelper;

public enum EDateTimeJSPathProvider implements IJSPathProviderWithParam
{
  CALENDAR ("datetime/calendar.js"),
  CALENDAR_LANGUAGE ("datetime/lang/calendar-{0}.js"),
  CALENDAR_SETUP ("datetime/calendar-setup.js");

  private final String m_sPath;

  private EDateTimeJSPathProvider (@Nonnull @Nonempty final String sPath)
  {
    if (!JSFilenameHelper.isJSFilename (sPath))
      throw new IllegalArgumentException ("path");
    m_sPath = sPath;
  }

  @Nonnull
  @Nonempty
  public String getJSItemPath (final boolean bRegular)
  {
    return bRegular ? m_sPath : JSFilenameHelper.getMinifiedJSPath (m_sPath);
  }

  @Nonnull
  public IJSPathProvider getInstance (@Nonnull @Nonempty final String sLanguage)
  {
    return new IJSPathProvider ()
    {
      @Nonnull
      @Nonempty
      public String getJSItemPath (final boolean bRegular)
      {
        return EDateTimeJSPathProvider.this.getJSItemPath (bRegular).replace ("{0}", sLanguage);
      }
    };
  }
}
