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
package com.phloc.webbasics.browser;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.version.Version;

/**
 * Special browser info for Internet Explorer.
 * 
 * @author philip
 */
public final class BrowserInfoIE extends BrowserInfo
{
  @SuppressWarnings ("hiding")
  public static final BrowserInfoIE IS_IT_NOT = new BrowserInfoIE ();

  private final boolean m_bIsIECompatibilityMode;

  private BrowserInfoIE ()
  {
    m_bIsIECompatibilityMode = false;
  }

  public BrowserInfoIE (@Nonnull final Version aVersion, final boolean bIsIECompatibilityMode)
  {
    super (EBrowserType.IE, aVersion);
    m_bIsIECompatibilityMode = bIsIECompatibilityMode;
  }

  /**
   * @return <code>true</code> if any IE compatibility mode is active
   */
  public boolean isIECompatibilityMode ()
  {
    return m_bIsIECompatibilityMode;
  }

  @Override
  public String getDisplayText (@Nonnull final Locale aContentLocale)
  {
    return super.getDisplayText (aContentLocale) +
           (isIECompatibilityMode () ? EBrowserText.IE_COMPATIBILITY_MODE.getDisplayText (aContentLocale) : "");
  }

  @Override
  public String toString ()
  {
    if (isItNot ())
      return new ToStringGenerator (null).append ("isIt", "not").toString ();
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("ieCompatibilityMode", m_bIsIECompatibilityMode)
                            .toString ();
  }
}
