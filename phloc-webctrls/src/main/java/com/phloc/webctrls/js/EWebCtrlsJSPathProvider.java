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
package com.phloc.webctrls.js;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.resource.js.IJSPathProvider;
import com.phloc.html.resource.js.JSFilenameHelper;

public enum EWebCtrlsJSPathProvider implements IJSPathProvider
{
  /** BigDecimal support for JS */
  BIG_DECIMAL ("js/big.js", true),
  FORM ("js/form.js", true),
  JQUERY_UTILS ("js/jquery-utils.js", true),
  /**
   * Source: https://github.com/scottjehl/Respond - only for IE6-8 so use it
   * only in a conditional comment!
   */
  RESPOND ("js/respond.js", false),
  STACKTRACE ("js/stacktrace.js", true),
  /** Insert in &lt;head&gt; element (after or before your CSS) for IE &lt; 9 */
  HTML5SHIV_3_7_2 ("html5shiv/3.7.2/html5shiv.js", false),
  /** Edit placeholder fix for IE &lt; 10 */
  PLACEHOLDER_FIX ("placeholder/placeholder-fix.js", false),
  /** JQuery 1.7 */
  JQUERY_1_7 ("jquery/jquery-1.7.2.js", true),
  /** JQuery 1.8 */
  JQUERY_1_8 ("jquery/jquery-1.8.3.js", true),
  /** JQuery 1.9 */
  JQUERY_1_9 ("jquery/jquery-1.9.1.js", true),
  /** JQuery 1.10 */
  JQUERY_1_10 ("jquery/jquery-1.10.2.js", true),
  /** JQuery 1.11 */
  JQUERY_1_11 ("jquery/jquery-1.11.1.js", true),
  /** JQuery 2.0 */
  JQUERY_2_0 ("jquery/jquery-2.0.3.js", true),
  /** JQuery 2.1 */
  JQUERY_2_1 ("jquery/jquery-2.1.1.js", true);

  private final String m_sPath;
  private final boolean m_bCanBeBundled;

  private EWebCtrlsJSPathProvider (@Nonnull @Nonempty final String sPath, final boolean bCanBeBundled)
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
}
