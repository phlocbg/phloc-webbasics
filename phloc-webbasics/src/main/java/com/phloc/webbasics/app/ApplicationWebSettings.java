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
package com.phloc.webbasics.app;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.html.EHTMLVersion;

/**
 * Contains settings for the HTML output.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class ApplicationWebSettings
{
  private static EHTMLVersion s_eHTMLVersion = EHTMLVersion.DEFAULT;

  private ApplicationWebSettings ()
  {}

  @Nonnull
  public static EHTMLVersion getHTMLVersion ()
  {
    return s_eHTMLVersion;
  }

  public static void setHTMLVersion (@Nonnull final EHTMLVersion eHTMLVersion)
  {
    if (eHTMLVersion == null)
      throw new NullPointerException ("version");
    s_eHTMLVersion = eHTMLVersion;
  }
}
