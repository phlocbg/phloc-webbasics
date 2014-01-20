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
package com.phloc.bootstrap3.datetimepicker;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.resource.js.ConstantJSPathProvider;
import com.phloc.html.resource.js.IJSPathProvider;
import com.phloc.html.resource.js.IJSPathProviderWithParam;
import com.phloc.html.resource.js.JSFilenameHelper;

/**
 * JS code for DateTime Picker from
 * http://www.malot.fr/bootstrap-datetimepicker/index.php
 * 
 * @author Philip Helger
 */
public enum EDateTimePickerJSPathProvider implements IJSPathProviderWithParam
{
  DATETIMEPICKER ("bootstrap/datetimepicker/bootstrap-datetimepicker.js", true),
  DATETIMEPICKER_LOCALE ("bootstrap/datetimepicker/locales/bootstrap-datetimepicker.{0}.js", false);

  private final String m_sPath;
  private final boolean m_bCanBeBundled;

  private EDateTimePickerJSPathProvider (@Nonnull @Nonempty final String sPath, final boolean bCanBeBundled)
  {
    if (!JSFilenameHelper.isJSFilename (sPath))
      throw new IllegalArgumentException ("path");
    m_sPath = sPath;
    m_bCanBeBundled = bCanBeBundled;
  }

  @Nonnull
  @Nonempty
  public String getJSItemPath (final boolean bRegular)
  {
    return bRegular ? m_sPath : JSFilenameHelper.getMinifiedJSPath (m_sPath);
  }

  public boolean canBeBundled ()
  {
    return m_bCanBeBundled;
  }

  @Nonnull
  public IJSPathProvider getInstance (@Nonnull @Nonempty final String sLanguage)
  {
    return new ConstantJSPathProvider (m_sPath.replace ("{0}", sLanguage), true);
  }
}
