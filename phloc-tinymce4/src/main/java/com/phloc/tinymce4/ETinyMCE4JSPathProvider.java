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
package com.phloc.tinymce4;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.resource.js.IJSPathProvider;
import com.phloc.html.resource.js.JSFilenameHelper;

public enum ETinyMCE4JSPathProvider implements IJSPathProvider
{
  TINYMCE_4 ("tinymce-dev/tinymce.jquery.js", "tinymce-min/tinymce.min.js");

  private final String m_sRegularPath;
  private final String m_sMinPath;

  private ETinyMCE4JSPathProvider (@Nonnull @Nonempty final String sRegularPath)
  {
    this (sRegularPath, JSFilenameHelper.getMinifiedJSPath (sRegularPath));
  }

  private ETinyMCE4JSPathProvider (@Nonnull @Nonempty final String sRegularPath,
                                   @Nonnull @Nonempty final String sMinPath)
  {
    if (!JSFilenameHelper.isJSFilename (sRegularPath))
      throw new IllegalArgumentException ("RegularPath");
    if (!JSFilenameHelper.isJSFilename (sMinPath))
      throw new IllegalArgumentException ("MinPath");
    m_sRegularPath = sRegularPath;
    m_sMinPath = sMinPath;
  }

  @Nonnull
  @Nonempty
  public String getJSItemPath (final boolean bRegular)
  {
    return bRegular ? m_sRegularPath : m_sMinPath;
  }

  public boolean canBeBundled ()
  {
    return true;
  }
}
