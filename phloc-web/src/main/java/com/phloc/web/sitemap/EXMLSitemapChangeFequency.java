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
package com.phloc.web.sitemap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;

/**
 * The determined change frequency of a sitemap entry
 * 
 * @author Philip Helger
 */
public enum EXMLSitemapChangeFequency
{
  ALWAYS ("always"),
  HOURLY ("hourly"),
  DAILY ("daily"),
  WEEKLY ("weekly"),
  MONTHLY ("monthly"),
  YEARLY ("yearly"),
  NEVER ("never");

  private final String m_sText;

  private EXMLSitemapChangeFequency (@Nonnull @Nonempty final String sText)
  {
    m_sText = sText;
  }

  @Nonnull
  @Nonempty
  public String getText ()
  {
    return m_sText;
  }

  @Nullable
  public static EXMLSitemapChangeFequency getFromTextOrNull (@Nullable final String sText)
  {
    if (StringHelper.hasText (sText))
      for (final EXMLSitemapChangeFequency e : values ())
        if (e.getText ().equals (sText))
          return e;
    return null;
  }
}
